package cl.uchile.dcc.cc5303.server;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IServer extends Remote {

	void serve() throws RemoteException, MalformedURLException;

	void set(Remote o, String path) throws RemoteException;

	Remote get(String path) throws RemoteException;

	void addNeighbour(String url) throws RemoteException;

	void publish() throws RemoteException, MalformedURLException;

	ArrayList<String> getNeighbours() throws RemoteException;

	String getServerURL() throws RemoteException;
	
	public void setServerURL(String url) throws RemoteException;
	
	public void migrateData(IServer destServer) throws RemoteException, MalformedURLException, NotBoundException;
	
	public ArrayList<Remote> getObjects() throws RemoteException;

	public void setObjects(ArrayList<Remote> objects) throws RemoteException;

	public ArrayList<String> getPaths() throws RemoteException;

	public void setPaths(ArrayList<String> paths) throws RemoteException;

	public void setNeighbours(ArrayList<String> neighbours) throws RemoteException;

	public int playerSize() throws RemoteException;

}