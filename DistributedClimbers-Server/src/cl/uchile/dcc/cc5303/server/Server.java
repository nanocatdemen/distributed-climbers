package cl.uchile.dcc.cc5303.server;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

import cl.uchile.dcc.cc5303.*;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;

public class Server extends UnicastRemoteObject implements IServer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String urlServer;
	HashMap<String,IPlayer> players;
	HashMap<String,IGestor> gestor;
	HashMap<String,IBenchManager> benchmanager;
	ArrayList<String> neighbours;
	ArrayList<Boolean> needMigrate;
	private OperatingSystemMXBean operatingSystemMXBean = (com.sun.management.OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
	double cpuLoad;
	String migrateURL;
	boolean isActive;
	boolean expectingPlayers;


	public Server(String urlServer) throws RemoteException {
		this.urlServer = urlServer;
		this.players = new HashMap<>();
		this.gestor = new HashMap<>();
		this.benchmanager = new HashMap<>();
		this.neighbours = new ArrayList<>();
		this.needMigrate = new ArrayList<>();
		this.migrateURL = "";
		this.expectingPlayers = false;
	}

	@Override
	public void serve() throws RemoteException, MalformedURLException {
		for(String key : this.players.keySet()) {
			Naming.rebind(this.urlServer + key, this.players.get(key));
			System.out.println("Instance of " + this.players.get(key).getClass() + " published in: " + this.urlServer + key);
			this.needMigrate.add(false);
		}
		for(String key : this.gestor.keySet()) {
			Naming.rebind(this.urlServer + key, this.gestor.get(key));
			System.out.println("Instance of " + this.gestor.get(key).getClass() + " published in: " + this.urlServer + key);
		}
		for(String key : this.benchmanager.keySet()) {
			Naming.rebind(this.urlServer + key, this.benchmanager.get(key));
			System.out.println("Instance of " + this.benchmanager.get(key).getClass() + " published in: " + this.urlServer + key);
		}
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

	// For now only migrate players...
	@Override
	public void migrateData(IServer sourceServer) throws RemoteException, MalformedURLException {
		for(String path : this.players.keySet()) {
			IPlayer remotePlayer = sourceServer.getPlayer(path);
			this.players.get(path).setLives(remotePlayer.getLives());
			this.players.get(path).setWaiting(remotePlayer.isWaiting());
			this.players.get(path).setAlive(remotePlayer.isAlive());
			this.players.get(path).setPosX(remotePlayer.getPosX());
			this.players.get(path).setPosY(remotePlayer.getPosY());
			this.players.get(path).setScore(remotePlayer.getScore());
			this.players.get(path).setStartLives(remotePlayer.getStartLives());
			this.players.get(path).setId(remotePlayer.getId());
		}
		IGestor gestor = this.gestor.get("gestor");
		IGestor remoteGestor = sourceServer.getGestor("gestor");
		gestor.setGameOver(remoteGestor.isGameOver());
		gestor.setLock(remoteGestor.getLock());
		gestor.setPause(remoteGestor.isPause());
		gestor.setRevanchaWanters(remoteGestor.getRevanchaWanters());
		gestor.setTaken(remoteGestor.getTaken());
		gestor.setDisconected(remoteGestor.getDisconected());
	}

	@Override
	public void setNeighbours(ArrayList<String> neighbours) {
		this.neighbours = neighbours;
	}

	@Override
	public int playerSize() throws RemoteException {
		return this.players.size();
	}

	@Override
	public void addPlayer(IPlayer p, String path) throws RemoteException {
		this.players.put(path, p);
	}

	@Override
	public void addGestor(IGestor g, String path) throws RemoteException {
		this.gestor.put(path, g);
	}

	@Override
	public void addBenchManager(IBenchManager b, String path) throws RemoteException {
		this.benchmanager.put(path, b);
	}

	@Override
	public IPlayer getPlayer(String path) throws RemoteException {
		return this.players.get(path);
	}

	@Override
	public IGestor getGestor(String path) throws RemoteException {
		return this.gestor.get(path);
	}

	@Override
	public IBenchManager getBenchManager(String path) throws RemoteException {
		return this.benchmanager.get(path);
	}
	
	@Override
	public ArrayList<Boolean> needMigrate() {
		return this.needMigrate;
	}
	
	@Override
	public void setNeedMigrate(ArrayList<Boolean> b) {
		this.needMigrate = b;
	}

	@Override
	public boolean CPUover75(){
		if(operatingSystemMXBean.getSystemLoadAverage() > 0.75) return true;
		return false;		
	}
	
	@Override
	public double CPUload(){
		return operatingSystemMXBean.getSystemLoadAverage();		
	}

	@Override
	public void deletePlayer(int quiterID) throws RemoteException, MalformedURLException, NotBoundException {
		this.gestor.get("gestor").deletePlayer(quiterID);
		this.expectingPlayers = true;
		IPlayer quiter = (IPlayer)Naming.lookup(urlServer + "player" + quiterID);
		quiter.setLives(-1);
		quiter.die();
	}

	@Override
	public String getServerMinLoad() throws RemoteException, MalformedURLException, NotBoundException {
		ArrayList<IServer> servers = new ArrayList<>();
		for(String server: neighbours){
			servers.add((IServer)Naming.lookup(server+"server"));
		}
		double min = Double.MAX_VALUE;
		String minLoadServer = "";
		for(IServer server: servers){
			double load = server.CPUload();
			if(load<min){
				min = load;
				minLoadServer = server.getServerURL();
			}
		}
		return minLoadServer;
	}

	@Override
	public void setMigrateURL(String anotherServerURL) throws RemoteException {
		this.migrateURL = anotherServerURL;
		
	}

	@Override
	public String getMigrateURL() throws RemoteException {
		return this.migrateURL;
	}

	@Override
	public void setActive(boolean b) throws RemoteException {
		this.isActive = b;
		
	}

	@Override
	public boolean getActive() throws RemoteException {
		return this.isActive;
	}

	@Override
	public String getActiveServer() throws RemoteException, MalformedURLException, NotBoundException {
		ArrayList<IServer> servers = new ArrayList<>();
		for(String server: neighbours){
			servers.add((IServer)Naming.lookup(server+"server"));
		}
		servers.add(this);
		for(IServer server: servers){
			if(server.getActive()){
				return server.getServerURL();
			}
		}
		return "";
	}

	@Override
	public IServer migrate(String motive) throws RemoteException, MalformedURLException, NotBoundException {
		String anotherServerURL = this.getServerMinLoad();
		if(anotherServerURL != ""){
			System.out.println("\n\n-----------MIGRANDO POR " + motive + "------------\n\n");
			this.setActive(false);
			IServer anotherServer = (IServer) Naming.lookup(anotherServerURL + "server");
			anotherServer.migrateData(this);
			this.setMigrateURL(anotherServerURL);
			ArrayList<Boolean> migrate = this.needMigrate();
			for(int i = 0; i < migrate.size(); i++) {
				migrate.set(i, true);
			}
			this.getGestor("gestor").doNotifyAll();
			this.setNeedMigrate(migrate);
			anotherServer.setActive(true);
			return anotherServer;
		}
		else
			System.err.println("NO SE ENCONTRARON SERVIDORES PARA MIGRAR");
		return this;
	}

	@Override
	public void makePlayerJump(IPlayer player) throws RemoteException {
		player.jump();
	}

	@Override
	public void movePlayerRight(IPlayer player) throws RemoteException {
		player.moveRight();		
	}

	@Override
	public void movePlayerLeft(IPlayer player) throws RemoteException {
		player.moveLeft();
	}

	@Override
	public void setPlayerScore(IPlayer player, int scoreDiference) throws RemoteException {
		player.setScore(player.getScore() + scoreDiference);		
	}
}
