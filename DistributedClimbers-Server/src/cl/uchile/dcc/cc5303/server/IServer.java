package cl.uchile.dcc.cc5303.server;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import cl.uchile.dcc.cc5303.*;

public interface IServer extends Remote {

	public void serve() throws RemoteException, MalformedURLException;
	
	public void addPlayer(IPlayer p, String path) throws RemoteException;
	
	public void addGestor(IGestor g, String path) throws RemoteException;
	
	public void addBenchManager(IBenchManager b, String path) throws RemoteException;
	
	public IPlayer getPlayer(String path) throws RemoteException;
	
	public IGestor getGestor(String path) throws RemoteException;
	
	public IBenchManager getBenchManager(String path) throws RemoteException;

	public void addNeighbour(String url) throws RemoteException;

	public void publish() throws RemoteException, MalformedURLException;

	public ArrayList<String> getNeighbours() throws RemoteException;

	public String getServerURL() throws RemoteException;
	
	public void setServerURL(String url) throws RemoteException;
	
	public void migrateData(IServer destServer) throws RemoteException, MalformedURLException, NotBoundException;
	
	public void setNeighbours(ArrayList<String> neighbours) throws RemoteException;

	public int playerSize() throws RemoteException;
	
	public ArrayList<Boolean> needMigrate() throws RemoteException;
	
	public void setNeedMigrate(ArrayList<Boolean> b) throws RemoteException;

	public boolean CPUover75() throws RemoteException;
	
	public double CPUload() throws RemoteException;

	public void deletePlayer(int myID) throws RemoteException, MalformedURLException, NotBoundException;

	public String getServerMinLoad() throws RemoteException, MalformedURLException, NotBoundException;

	public void setMigrateURL(String anotherServerURL) throws RemoteException;
	
	public String getMigrateURL() throws RemoteException;

	public void setActive(boolean b) throws RemoteException;

	public boolean getActive() throws RemoteException;

	public String getActiveServer() throws RemoteException, MalformedURLException, NotBoundException;

	public IServer migrate(String string) throws RemoteException, MalformedURLException, NotBoundException;

	public void makePlayerJump(IPlayer myPlayer) throws RemoteException;

	public void movePlayerRight(IPlayer myPlayer) throws RemoteException;

	public void movePlayerLeft(IPlayer myPlayer) throws RemoteException;

	public void setPlayerScore(IPlayer myPlayer, int i) throws RemoteException;

}