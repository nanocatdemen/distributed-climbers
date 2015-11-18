package cl.uchile.dcc.cc5303;

public class Playerscore implements Comparable<Playerscore>{
	IPlayer player;
	int score;
	
	public Playerscore(IPlayer player, int score){
		this.player = player;
		this.score = score;
	}
	@Override
	public int compareTo(Playerscore o) {
		return o.score - this.score;
	}

}
