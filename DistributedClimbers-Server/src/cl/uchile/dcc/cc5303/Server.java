package cl.uchile.dcc.cc5303;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Server{
	
	public static String URLSERVER;
	public static int NB_OF_PLAYERS;
	public static int NB_OF_LIVES;
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
	public static void main(String[] args) {
		URLSERVER = "rmi://" + args[0] + ":1099/iceClimbers";
		NB_OF_PLAYERS = Integer.parseInt(args[1]);
		NB_OF_LIVES = Integer.parseInt(args[2]);
		int width = 800, height = 600;
		try {
			ArrayList<IPlayer> players = new ArrayList<IPlayer>();
			for(int i = 0; i < NB_OF_PLAYERS; i++) {
				players.add(new Player(100+width/4*(i), height-50, NB_OF_LIVES));
			}
			int i = 0;
			for(IPlayer player : players) {
				Naming.rebind(URLSERVER + "/player" + i, player);
				System.out.println("Player published in server: " + URLSERVER + "/player" + i);
				i++;
			}
			IBenchManager manager= new BenchManager();
			for(i = 0; i < benches.length; i++){
				IBench bench = new Bench(benches[i][0],benches[i][1],benches[i][2]);
				manager.add(bench);
			}
			Naming.rebind(URLSERVER + "/benchManager", manager);
			System.out.println("Bench manager published in server: " + URLSERVER + "/benchManager");
            IGestor gestor = new Gestor(NB_OF_PLAYERS, benches.length);
            Naming.rebind(URLSERVER + "/gestor", gestor);
            System.out.println("Gestor published in server: " + URLSERVER + "/gestor");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

}
