package cl.uchile.dcc.cc5303.server;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Server {
 
	String urlServer;
	ArrayList<Remote> objects;
	ArrayList<String> paths;

	public Server(String urlServer) {
		this.urlServer = urlServer;
		this.objects = new ArrayList<>();
		this.paths = new ArrayList<>();
	}
	
	public void serve() throws RemoteException, MalformedURLException {
		int i = 0;
		for(Remote o : objects) {
			Naming.rebind(this.urlServer + paths.get(i), o);
			System.out.println("Instance of " + o.getClass() + " published in: " + this.urlServer + this.paths.get(i++));
		}
	}
	
	public void add(Remote o, String path) {
		objects.add(o);
		paths.add(path);
	}
	
	public Remote get(String path) {
		int i = paths.indexOf(path);
		return objects.get(i);
	}

}
