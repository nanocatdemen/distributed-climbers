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

	void resetScore() throws RemoteException;

	int[] getResults() throws RemoteException;
	
	Mutex getMutex() throws RemoteException;
	
	void pause() throws RemoteException;
	
	void resume() throws RemoteException;
	
	boolean isPaused() throws RemoteException;

	void setPause(boolean pause) throws RemoteException;

	boolean isPause() throws RemoteException;

	void setDead(int dead) throws RemoteException;

	int getDead() throws RemoteException;

	void setScore(int[] score) throws RemoteException;

	int[] getScore() throws RemoteException;

	void setLock(Mutex lock) throws RemoteException;

	Mutex getLock() throws RemoteException;

	void setDedGaem(boolean dedGaem) throws RemoteException;

	boolean isDedGaem() throws RemoteException;

	void setRevanchaWanters(ArrayList<Boolean> revanchaWanters) throws RemoteException;

	ArrayList<Boolean> getRevanchaWanters() throws RemoteException;

	void setTaken(ArrayList<Boolean> taken) throws RemoteException;

	ArrayList<Boolean> getTaken() throws RemoteException;

	void deletePlayer(int myID) throws RemoteException;

	ArrayList<Integer> getDisconected() throws RemoteException;

	void setDisconected(ArrayList<Integer> disconected) throws RemoteException;
	
}