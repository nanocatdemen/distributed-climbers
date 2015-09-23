import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class Server {
	
	public static String urlServer = "rmi://localhost:1099/iceClimbers";

	public static void main(String[] args) {
		try {
            IPlayer banco = new Player();
			Naming.rebind(urlServer, banco);
			System.out.println("Objeto Banco publicado en "+urlServer);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
