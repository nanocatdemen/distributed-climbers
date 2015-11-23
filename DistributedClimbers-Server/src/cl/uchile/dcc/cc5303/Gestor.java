package cl.uchile.dcc.cc5303;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Gestor extends UnicastRemoteObject implements IGestor {

	private static final long serialVersionUID = 1L;
	ArrayList<Boolean> taken;
	ArrayList<Integer> disconected;
	ArrayList<Boolean> revanchaWanters;
	boolean gameOver = false;
	protected Mutex lock;
	int nbOfPlayers;
	int nbOfBenches;
	boolean pause = false;
	CyclicBarrier barrier;
	
	
	public Gestor(int players, int benches) throws RemoteException {
		barrier = new CyclicBarrier(players);
		taken = new ArrayList<Boolean>();
		revanchaWanters = new ArrayList<Boolean>();
		disconected = new ArrayList<>();
		for(int i = 0; i < players; i++) {
			taken.add(false);
			revanchaWanters.add(false);
		}
		lock = new Mutex();
		nbOfPlayers = players;
		nbOfBenches = benches;
	}

	@Override
	public int giffPlayer() { //KOTL GIFF me MANA
		if(this.disconected.size()>0)
			return this.disconected.remove(0);
		for(int i = 0; i < taken.size(); i++) {
			if(!taken.get(i)) {
				taken.set(i, true);
				return i;
			}
		}
		// full
		return -1;
	}
	
	@Override
	public boolean areAllTaken() {
		boolean ret = true;
		for(boolean b : this.taken) {
			ret = ret && b;
		}
		return ret;
	}
	
	@Override
	public void doWait() {
		synchronized(this.lock){
			try {
				this.lock.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void doNotifyAll() {
		synchronized(this.lock){
			this.lock.notifyAll();
		}
	}
	
	@Override
	public int getNbOfPlayers() {
		return nbOfPlayers;
	}

	@Override
	public int getNbOfBenches() {
		return nbOfBenches;
	}

	@Override
	public boolean gameOver(ArrayList<IPlayer> allPlayers) throws RemoteException {
		int cnt = 0;
		for(IPlayer player : allPlayers){
			if(player.getLives()<0){
				cnt++;
			}
		}
		return cnt==(allPlayers.size()-1);
	}

	@Override
	public void IWantRevancha(int i) {
		revanchaWanters.set(i, true);
		
	}

	@Override
	public boolean allWantRevancha() {
		boolean ret = true;
		for(boolean b : this.revanchaWanters) {
			ret = ret && b;
		}
		return ret;
	}

	@Override
	public void resetGame(ArrayList<IPlayer> allPlayers, IBenchManager benchManager) throws RemoteException {
		for(int i = 0; i < this.revanchaWanters.size(); i++) {
			this.revanchaWanters.set(i,false);
		}
		for(IPlayer p : allPlayers) {
			p.reset();
		}
		benchManager.resetBenchs();
	}

	@Override
	public void weLost(int i) throws RemoteException { //gg la climbers
		this.gameOver = true;
	}

	@Override
	public Mutex getMutex() throws RemoteException {
		return lock;
	}

	@Override
	public void pause() throws RemoteException {
		pause = true;
	}

	@Override
	public void resume() throws RemoteException {
		pause = false;
	}

	@Override
	public boolean isPaused() throws RemoteException {
		return pause;
	}

	@Override
	public ArrayList<Boolean> getTaken() {
		return taken;
	}

	@Override
	public void setTaken(ArrayList<Boolean> taken) {
		this.taken = taken;
	}

	@Override
	public ArrayList<Boolean> getRevanchaWanters() {
		return revanchaWanters;
	}

	@Override
	public void setRevanchaWanters(ArrayList<Boolean> revanchaWanters) {
		this.revanchaWanters = revanchaWanters;
	}

	@Override
	public boolean isGameOver() {
		return gameOver;
	}

	@Override
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	@Override
	public Mutex getLock() {
		return lock;
	}

	@Override
	public void setLock(Mutex lock) {
		this.lock = lock;
	}

	@Override
	public boolean isPause() {
		return pause;
	}

	@Override
	public void setPause(boolean pause) {
		this.pause = pause;
	}

	@Override
	public void deletePlayer(int quiterID) throws RemoteException {
		disconected.add(quiterID);
	}
	
	@Override
	public ArrayList<Integer> getDisconected() {
		return disconected;
	}

	@Override
	public void setDisconected(ArrayList<Integer> disconected) {
		this.disconected = disconected;
	}

	@Override
	public void doAwait() throws InterruptedException, BrokenBarrierException {
		this.barrier.await();
	}

}
