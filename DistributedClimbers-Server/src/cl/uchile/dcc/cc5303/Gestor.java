package cl.uchile.dcc.cc5303;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Gestor extends UnicastRemoteObject implements IGestor {

	private static final long serialVersionUID = 1L;
	ArrayList<Boolean> taken;
	
	public Gestor(int players) throws RemoteException {
		taken = new ArrayList<Boolean>();
		for(int i = 0; i < players; i++) {
			taken.add(false);
		}
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
}
