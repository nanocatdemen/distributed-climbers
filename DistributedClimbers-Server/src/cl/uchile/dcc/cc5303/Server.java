package cl.uchile.dcc.cc5303;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Server {
	
	public static String urlServer = "rmi://localhost:1099/iceClimbers";

	public static void main(String[] args) {
		int width = 800, height = 600;
		try {
			ArrayList<IPlayer> players = new ArrayList<IPlayer>();
			for(int i = 0; i < 4; i++) {
				//TODO: ver bien el width
				players.add(new Player(width/(i+1)-50,height-50));
			}
			int i = 0;
			for(IPlayer player : players) {
				Naming.rebind(urlServer + "/player" + i, player);
				System.out.println("Player published in server: " + urlServer + "/player" + i);
				i++;
			}
            IGestor gestor = new Gestor(4);
            Naming.rebind(urlServer + "/gestor", gestor);
            System.out.println("Gestor published in server: " + urlServer + "/gestor");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

}
