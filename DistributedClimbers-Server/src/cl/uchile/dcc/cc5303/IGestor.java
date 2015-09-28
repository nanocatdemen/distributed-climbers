package cl.uchile.dcc.cc5303;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGestor extends Remote {

	int giffPlayer() throws RemoteException;

}