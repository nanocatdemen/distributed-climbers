package cl.uchile.dcc.cc5303;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IBenchManager extends Remote {

	void add(IBench bench) throws RemoteException;

	void setBenches(ArrayList<IBench> benches) throws RemoteException;

	void resetBenchs() throws RemoteException;
	
	IBench getBenches(int bench) throws RemoteException;

	int nbOfBenches() throws RemoteException;

}
