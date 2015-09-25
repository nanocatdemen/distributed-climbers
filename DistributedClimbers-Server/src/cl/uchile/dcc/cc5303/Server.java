package cl.uchile.dcc.cc5303;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class Server {
	
	public static String urlServer = "rmi://localhost:1099/iceClimbers";

	public static void main(String[] args) {
		try {
            IPlayer banco = new Player();
			Naming.rebind(urlServer, banco);
			System.out.println("Object published in "+urlServer);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

}
