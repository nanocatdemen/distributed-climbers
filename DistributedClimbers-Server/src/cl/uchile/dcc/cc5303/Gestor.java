package cl.uchile.dcc.cc5303;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Gestor extends UnicastRemoteObject implements IGestor {

	private static final long serialVersionUID = 1L;
	ArrayList<Boolean> taken;
	protected Mutex lock;
	
	public Gestor(int players) throws RemoteException {
		taken = new ArrayList<Boolean>();
		for(int i = 0; i < players; i++) {
			taken.add(false);
		}
		lock = new Mutex();
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
		System.out.println(this.taken);
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

}
