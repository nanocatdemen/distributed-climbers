package cl.uchile.dcc.cc5303.server;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Server extends UnicastRemoteObject implements IServer {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String urlServer;
	ArrayList<Remote> objects;
	ArrayList<String> paths;
	ArrayList<String> neighbours;

	public Server(String urlServer) throws RemoteException {
		this.urlServer = urlServer;
		this.objects = new ArrayList<>();
		this.paths = new ArrayList<>();
		this.neighbours = new ArrayList<>();
	}

	//clone
	public Server(IServer server) throws RemoteException {
		this.urlServer = server.getServerURL();
		this.objects = server.getObjects();
		this.paths = server.getPaths();
		this.neighbours = server.getNeighbours();
	}

	@Override
	public void serve() throws RemoteException, MalformedURLException {
		int i = 0;
		for(Remote o : this.objects) {
			Naming.rebind(this.urlServer + this.paths.get(i), o);
			System.out.println("Instance of " + o.getClass() + " published in: " + this.urlServer + this.paths.get(i++));
		}
	}

	@Override
	public void set(Remote o, String path) {
		int i = this.paths.indexOf(path);
		if (i != -1) {
			this.paths.set(i, path);
			this.objects.set(i, o);
		} else {
			this.paths.add(path);
			this.objects.add(o);
		}
	}

	@Override
	public Remote get(String path) {
		int i = this.paths.indexOf(path);
		return this.objects.get(i);
	}
	
	@Override
	public void addNeighbour(String url) {
		this.neighbours.add(url);
	}
	
	@Override
	public ArrayList<String> getNeighbours() {
		return this.neighbours;
	}
	
	@Override
	public void publish() throws RemoteException, MalformedURLException {
		Naming.rebind(this.urlServer + "server", this);
		System.out.println("Instance of " + this.getClass() + " published in: " + this.urlServer + "server");
	}

	@Override
	public String getServerURL() throws RemoteException {
		return this.urlServer;
	}
	
	@Override
	public void setServerURL(String url) throws RemoteException {
		this.urlServer = url;
	}
	
	@Override
	public void migrateData(IServer destServer) throws RemoteException {
		System.out.println("beforegetURL");
		String url = destServer.getServerURL();
		System.out.println("beforeClone");
		destServer = new Server(this);
		System.out.println("afterClone");
		destServer.setServerURL(url);
	}

	public ArrayList<Remote> getObjects() {
		return objects;
	}

	public void setObjects(ArrayList<Remote> objects) {
		this.objects = objects;
	}

	public ArrayList<String> getPaths() {
		return paths;
	}

	public void setPaths(ArrayList<String> paths) {
		this.paths = paths;
	}

	public void setNeighbours(ArrayList<String> neighbours) {
		this.neighbours = neighbours;
	}

}
