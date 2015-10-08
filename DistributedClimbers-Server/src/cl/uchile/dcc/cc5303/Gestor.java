package cl.uchile.dcc.cc5303;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Gestor extends UnicastRemoteObject implements IGestor {

	private static final long serialVersionUID = 1L;
	ArrayList<Boolean> taken;
	ArrayList<Boolean> revanchaWanters;
	protected Mutex lock;
	int nbOfPlayers;
	int nbOfBenches;
	
	public Gestor(int players, int benches) throws RemoteException {
		taken = new ArrayList<Boolean>();
		revanchaWanters = new ArrayList<Boolean>();
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

}
