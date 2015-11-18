package cl.uchile.dcc.cc5303.server;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import cl.uchile.dcc.cc5303.*;

public interface IServer extends Remote {

	void serve() throws RemoteException, MalformedURLException;
	
	void addPlayer(IPlayer p, String path) throws RemoteException;
	
	void addGestor(IGestor g, String path) throws RemoteException;
	
	void addBenchManager(IBenchManager b, String path) throws RemoteException;
	
	IPlayer getPlayer(String path) throws RemoteException;
	
	IGestor getGestor(String path) throws RemoteException;
	
	IBenchManager getBenchManager(String path) throws RemoteException;

	void addNeighbour(String url) throws RemoteException;

	void publish() throws RemoteException, MalformedURLException;

	ArrayList<String> getNeighbours() throws RemoteException;

	String getServerURL() throws RemoteException;
	
	public void setServerURL(String url) throws RemoteException;
	
	public void migrateData(IServer destServer) throws RemoteException, MalformedURLException, NotBoundException;
	
	public void setNeighbours(ArrayList<String> neighbours) throws RemoteException;

	public int playerSize() throws RemoteException;
	
	public ArrayList<Boolean> needMigrate() throws RemoteException;
	
	public void setNeedMigrate(ArrayList<Boolean> b) throws RemoteException;

	public boolean CPUover75() throws RemoteException;
	
	public double CPUload() throws RemoteException;

	public void deletePlayer(int myID) throws RemoteException;

	public String getServerMinLoad() throws RemoteException, MalformedURLException, NotBoundException;

	public void setMigrateURL(String anotherServerURL) throws RemoteException;
	
	public String getMigrateURL() throws RemoteException;

	public void setActive(boolean b) throws RemoteException;

	public boolean getActive() throws RemoteException;

	public String getActiveServer() throws RemoteException, MalformedURLException, NotBoundException;

}