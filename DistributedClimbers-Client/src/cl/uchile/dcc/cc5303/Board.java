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
	public Bench[] bases;
	public Image img;
	public Graphics buffer;

	public Board(int w, int h){
		this.width = w;
		this.height = h;
	}

	@Override
	public void update(Graphics g) { paint(g); }

	@Override
	public void paint(Graphics g) {
		if(buffer==null){
			img = createImage(getWidth(),getHeight() );
			buffer = img.getGraphics();
		}
		
		Color[] colors = {Color.red, Color.blue, Color.green, Color.yellow};

		buffer.setColor(Color.black);
		buffer.fillRect(0, 0, getWidth(), getHeight());

		int i = 0;
		for(IPlayer player: players) {
			try {
				Board.drawPlayer(buffer, player, colors[i]);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			i++;
		}

		buffer.setColor(Color.white);
		for(Bench base : bases){
			buffer.fillRect(base.posX, base.posY, base.w, base.h);
		}

		g.drawImage(img, 0, 0, null);
	}

	//    @Override
	//    public String toString(){
	//        String ret = "Tablero: dimensions " + this.width + "x" + this.height + "\n";
	//        ret += p1.toString() + "\n" + p2.toString();
	//        return ret;
	//    }

	public void setBenches(Bench[] benches) {
		this.bases = benches;
	}

	public void levelsDown() throws RemoteException {
		for(Bench base: bases) {
			base.levelDown();
		}
		for(IPlayer player: players){
			player.down();
		}
	}

	static void drawPlayer(Graphics buffer, IPlayer p, Color color) throws RemoteException {
		buffer.setColor(color);
		buffer.fillRect(p.getPosX(), p.getPosY(), p.getW(), p.getH());
		if(p.isWaiting()) {
			buffer.setFont(new Font("ComicSans", Font.PLAIN, 20));
			buffer.setColor(Color.white);
			buffer.drawString("Waiting", p.getPosX() - 35, p.getPosY() - 3);
		}
	}
}
