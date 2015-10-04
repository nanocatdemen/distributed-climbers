package cl.uchile.dcc.cc5303;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Server {
	
	public static final String URLSERVER = "rmi://localhost:1099/iceClimbers";
	public static final int NB_OF_PLAYERS = 3;

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
            IGestor gestor = new Gestor(NB_OF_PLAYERS);
            Naming.rebind(URLSERVER + "/gestor", gestor);
            System.out.println("Gestor published in server: " + URLSERVER + "/gestor");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

}
