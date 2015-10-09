package cl.uchile.dcc.cc5303;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Board extends Canvas {

	private static final long serialVersionUID = -3391617458338607181L;

	public int width, height;

	public ArrayList<IPlayer> players;
	public ArrayList<IBench> bases;
	public Image img;
	public Graphics buffer;
	public boolean isGameOver;
	public int[] results;
	int nbp;

	public Board(int w, int h, int nbOfPlayers){
		this.width = w;
		this.height = h;
		results = new int[nbOfPlayers];
		isGameOver = false;
		nbp = nbOfPlayers;
		bases = new ArrayList<IBench>();
	}

	@Override
	public void update(Graphics g) { paint(g); }

	@Override
	public void paint(Graphics g) {
		if(buffer==null){
			img = createImage(getWidth(),getHeight());
			buffer = img.getGraphics();
		}
		
		Color[] colors = {new Color(80,100,255), Color.pink, new Color(80,230,80), new Color(200,200,80)};
		String[] names = {"popo", "nana", "meme", "lili"};

		buffer.setColor(Color.black);
		buffer.fillRect(0, 0, getWidth(), getHeight());

		int i = 0;
		for(IPlayer player: players) {
			try {
				Board.drawPlayer(buffer, player, colors[i], names[i]);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			i++;
		}
		

		buffer.setColor(new Color(146,42,42));
		try {
			for(IBench base : bases){
					buffer.fillRect(base.left(), base.top(), base.right()-base.left(), base.bottom()-base.top());
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (isGameOver) { 
			Board.drawGameOver(buffer, this.width/2-150,this.height/2);
			Board.drawResults(buffer, results, nbp, 550, 400);
		}

		g.drawImage(img, 0, 0, null);
		
	}

	//    @Override
	//    public String toString(){
	//        String ret = "Tablero: dimensions " + this.width + "x" + this.height + "\n";
	//        ret += p1.toString() + "\n" + p2.toString();
	//        return ret;
	//    }

	public void setBenches(ArrayList<IBench> benches) throws RemoteException {
		this.bases = benches;
	}

	public void levelsDown() throws RemoteException {
		for(IBench base: bases) {
			base.levelDown();
		}
		for(IPlayer player: players){
			player.down();
		}
	}

	static void drawPlayer(Graphics buffer, IPlayer p, Color color, String name) throws RemoteException {
		buffer.setColor(color);
		buffer.fillRect(p.getPosX(), p.getPosY(), p.getW(), p.getH());
		buffer.setFont(new Font("ComicSans", Font.PLAIN, 10));
		buffer.setColor(Color.white);
		buffer.drawString(name + " - " + p.getLives()+"", p.getPosX() - 5, p.getPosY() - 3);
		if(p.isWaiting()) {
			buffer.setFont(new Font("ComicSans", Font.PLAIN, 20));
			buffer.setColor(Color.white);
			buffer.drawString("Waiting", p.getPosX() - 35, p.getPosY() - 13);
		}
	}

	static void drawGameOver(Graphics buffer, int x, int y) {
		buffer.setFont(new Font("ComicSans", Font.PLAIN, 50));
		buffer.setColor(Color.white);
		buffer.drawString("GAME OVER", x, y);
		buffer.setFont(new Font("ComicSans", Font.PLAIN, 20));
		buffer.setColor(Color.lightGray);
		buffer.drawString("Try Again?" , x, y+30);
		buffer.drawString("Enter = Yes", x, y+60);
		buffer.drawString("Esc = Salir" , x, y+90);
	}
	
	static void drawResults(Graphics buffer, int[] results, int nbp, int x, int y){
		buffer.setFont(new Font("ComicSans", Font.PLAIN, 30));
		int disp = 96;
		String player = "";
		for(int i = 0; i<nbp; i++){
			if(results[i]==0){ buffer.setColor(new Color(80,100,255)); player = "popo";}
			if(results[i]==1){ buffer.setColor(Color.pink); player = "nana";}
			if(results[i]==2){ buffer.setColor(new Color(80,230,80)); player = "meme";}
			if(results[i]==3){ buffer.setColor(new Color(200,200,80)); player = "lili";}
			buffer.drawString((nbp-i)+"º: "+player, x, y+disp);
			disp -= 32;
		}
	}
}
