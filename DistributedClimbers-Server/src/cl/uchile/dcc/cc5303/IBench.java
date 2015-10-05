package cl.uchile.dcc.cc5303;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IBench extends Remote {

	int top() throws RemoteException;

	int left() throws RemoteException;

	int bottom() throws RemoteException;

	int right() throws RemoteException;

	void levelDown() throws RemoteException;

	int getLevel() throws RemoteException;

}