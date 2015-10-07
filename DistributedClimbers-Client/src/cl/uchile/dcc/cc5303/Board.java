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
	public IBenchManager bases;
	public Image img;
	public Graphics buffer;
	public boolean isGameOver;

	public Board(int w, int h){
		this.width = w;
		this.height = h;
		isGameOver = false;
	}

	@Override
	public void update(Graphics g) { paint(g); }

	@Override
	public void paint(Graphics g) {
		if(buffer==null){
			img = createImage(getWidth(),getHeight() );
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
			for(IBench base : bases.getBenches()){
					buffer.fillRect(base.left(), base.top(), base.right()-base.left(), base.bottom()-base.top());
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		g.drawImage(img, 0, 0, null);

		//if (isGameOver) drawGameOver(buffer);
	}

	//    @Override
	//    public String toString(){
	//        String ret = "Tablero: dimensions " + this.width + "x" + this.height + "\n";
	//        ret += p1.toString() + "\n" + p2.toString();
	//        return ret;
	//    }

	public void setBenches(ArrayList<IBench> benches) throws RemoteException {
		this.bases.setBenches(benches);
	}

	public void levelsDown() throws RemoteException {
		for(IBench base: bases.getBenches()) {
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

	static void drawGameOver(Graphics buffer) {
		buffer.setFont(new Font("ComicSans", Font.PLAIN, 50));
		buffer.setColor(Color.white);
		buffer.drawString("GAME OVER", WIDTH, HEIGHT/2-100);
	}
}
