package cl.uchile.dcc.cc5303;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IGestor extends Remote {

	int giffPlayer() throws RemoteException;

	boolean areAllTaken() throws RemoteException;

	void doNotifyAll() throws RemoteException;

	void doWait() throws RemoteException;

	int getNbOfPlayers() throws RemoteException;
	
	int getNbOfBenches() throws RemoteException;

	boolean gameOver(ArrayList<IPlayer> allPlayers) throws RemoteException;

	void IWantRevancha(int myID) throws RemoteException;

	boolean allWantRevancha() throws RemoteException;

	void resetGame(ArrayList<IPlayer> allPlayers, IBenchManager benchManager) throws RemoteException;
	
	boolean dedGaem() throws RemoteException;
	
	void weLost(int i) throws RemoteException;

	void feed(int myID) throws RemoteException;

	void resetScore() throws RemoteException;

	int[] getResults() throws RemoteException;
	
	Mutex getMutex() throws RemoteException;
	
}