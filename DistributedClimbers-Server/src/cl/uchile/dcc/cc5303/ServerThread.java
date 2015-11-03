package cl.uchile.dcc.cc5303;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import cl.uchile.dcc.cc5303.server.IServer;
import cl.uchile.dcc.cc5303.server.Server;

public class ServerThread {

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
	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
		URLSERVER = "rmi://" + args[0] + "/iceClimbers/";
		NB_OF_PLAYERS = Integer.parseInt(args[1]);
		NB_OF_LIVES = Integer.parseInt(args[2]);
		WIDTH = 800;
		HEIGHT = 600;

		IServer server = new Server(URLSERVER);
		IBenchManager manager = new BenchManager();
		for(int i = 0; i < benches.length; i++){
			IBench bench = new Bench(benches[i][0],benches[i][1],benches[i][2]);
			manager.add(bench);
		}
		IGestor gestor = new Gestor(NB_OF_PLAYERS, benches.length);
		
		for(int i = 0; i < NB_OF_PLAYERS; i++) {
			server.set(new Player(100 + WIDTH/4*(i), HEIGHT - 50, NB_OF_LIVES), "player" + i);
		}
		server.set(manager, "benchManager");
		server.set(gestor, "gestor");

		server.serve();
		server.publish();
		
		// Connect the server to the network
		if(args.length > 3) {
			String url = "rmi://" + args[3] + "/iceClimbers/";
			// TODO: throw error when the server is not found
			IServer refServer = (IServer) Naming.lookup(url + "server");
			ArrayList<String> neighbours = refServer.getNeighbours();
			for (String neighbour : neighbours) {
				// TODO: throw error when the server is not found
				IServer aServer = (IServer) Naming.lookup("rmi://" + neighbour + "/iceClimbers/server");
				server.addNeighbour(aServer.getServerURL());
				aServer.addNeighbour(URLSERVER);
			}
			server.addNeighbour(url);
			refServer.addNeighbour(URLSERVER);
			System.out.println(server.getNeighbours());
			System.out.println(refServer.getNeighbours());
		}
	}

}
