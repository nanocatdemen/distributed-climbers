package cl.uchile.dcc.cc5303;

import java.rmi.RemoteException;

public class Playerscore implements Comparable<Playerscore>{
	IPlayer player;
	int score;
	
	public Playerscore(IPlayer player, int score){
		this.player = player;
		this.score = score;
	}
	@Override
	public int compareTo(Playerscore o) {
		return (o.score - this.score);
	}
	
	@Override
	public String toString(){
		try {
			return this.player.getId() + " puntaje " + score;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
