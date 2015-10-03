package cl.uchile.dcc.cc5303;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGestor extends Remote {

	int giffPlayer() throws RemoteException;

	boolean areAllTaken() throws RemoteException;

	void doNotifyAll() throws RemoteException;

	void doWait() throws RemoteException;
	
}