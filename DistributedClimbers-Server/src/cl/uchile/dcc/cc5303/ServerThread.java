package cl.uchile.dcc.cc5303;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import cl.uchile.dcc.cc5303.server.IServer;
import cl.uchile.dcc.cc5303.server.Server;

public class ServerThread extends Thread {

	public static String URLSERVER;
	public static int NB_OF_PLAYERS, NB_OF_LIVES, WIDTH, HEIGHT;
	private static int[][] benches  = {
			//TODO ver como generar estas cosas de manera más aleatoria y bonita
			{0, 410, 0},
			{500, 400, 0},
			{100, 200, 1},
			{400, 200, 1},
			{300, 100, 2},
			{600, 200, 2},
			{150, 200, 3},
			{700, 100, 3},
			{75, 200, 4},
			{350, 350, 4},
			{200, 200, 5},
			{400, 400, 6},
			{200, 400, 7},
			{150, 200, 8},
			{275, 100, 9},
			{350, 100, 10}
	};
	public ServerThread(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
		WIDTH = 800;
		HEIGHT = 600;
		URLSERVER = "rmi://" + args[0] + "/iceClimbers/";
		IServer server = new Server(URLSERVER);
		// means join, connects to the given server and gets how many players are published
		// then publish the same amount of players.
		if(args.length == 2) {
			String externalUrl = "rmi://" + args[1] + "/iceClimbers/";
			// TODO: throw error when the server is not found
			IServer refServer = (IServer) Naming.lookup(externalUrl + "server");
			ArrayList<String> neighbours = refServer.getNeighbours();
			for (String neighbour : neighbours) {
				// TODO: throw error when the server is not found
				IServer aServer = (IServer) Naming.lookup("rmi://" + neighbour + "/iceClimbers/server");
				server.addNeighbour(aServer.getServerURL());
				aServer.addNeighbour(URLSERVER);
			}
			server.addNeighbour(externalUrl);
			refServer.addNeighbour(URLSERVER);

			int nPlayers = refServer.playerSize();
			
			// Data does not matter, just initialization
			IBenchManager manager = new BenchManager();
			for(int i = 0; i < benches.length; i++){
				IBench bench = new Bench(benches[i][0],benches[i][1],benches[i][2]);
				manager.add(bench);
			}
			IGestor gestor = new Gestor(nPlayers, benches.length);

			for(int i = 0; i < nPlayers; i++) {
				server.addPlayer(new Player(100 + WIDTH/4*(i), HEIGHT - 50, 1), "player" + i);
			}
			server.addBenchManager(manager, "benchManager");
			server.addGestor(gestor, "gestor");
			//server.serve();
			// migrate data
			server.migrateData(refServer);
			server.serve();
			//System.out.println(((IPlayer) server.getObjects().get(0)).getLives());
		} // means create 
		else {
			NB_OF_PLAYERS = Integer.parseInt(args[1]);
			NB_OF_LIVES = Integer.parseInt(args[2]);
			IBenchManager manager = new BenchManager();
			for(int i = 0; i < benches.length; i++){
				IBench bench = new Bench(benches[i][0], benches[i][1], benches[i][2]);
				manager.add(bench);
			}
			IGestor gestor = new Gestor(NB_OF_PLAYERS, benches.length);
			
			for(int i = 0; i < NB_OF_PLAYERS; i++) {
				server.addPlayer(new Player(100 + WIDTH/4*(i), HEIGHT - 50, NB_OF_LIVES), "player" + i);
			}
			server.addBenchManager(manager, "benchManager");
			server.addGestor(gestor, "gestor");
			server.serve();
		}
		server.publish();
	}
	
	@Override
	public void run() {
		while(true) {
			
		}
	}

}
