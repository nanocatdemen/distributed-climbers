package cl.uchile.dcc.cc5303;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Server{
	
	public static final String URLSERVER = "rmi://localhost:1099/iceClimbers";
	public static final int NB_OF_PLAYERS = 2;
	private static int[][] benches  = {
			//TODO ver como generar estas cosas de manera m{as aleatoria y bonita
            {0, 800, 0},
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
            {75, 100, 9},
            {50, 100, 10}
    };
	public static void main(String[] args) {
		int width = 800, height = 600;
		try {
			ArrayList<IPlayer> players = new ArrayList<IPlayer>();
			for(int i = 0; i < NB_OF_PLAYERS; i++) {
				//TODO: ver bien el width
				players.add(new Player(width/(i+1)-50,height-50));
			}
			int i = 0;
			for(IPlayer player : players) {
				Naming.rebind(URLSERVER + "/player" + i, player);
				System.out.println("Player published in server: " + URLSERVER + "/player" + i);
				i++;
			}
			for(i = 0; i < benches.length; i++){
				IBench bench = new Bench(benches[i][0],benches[i][1],benches[i][2]);
				Naming.rebind(URLSERVER + "/bench" + i, bench);
				System.out.println("Benche published in server: " + URLSERVER + "/bench" + i);
			}
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
