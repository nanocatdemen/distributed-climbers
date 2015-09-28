package cl.uchile.dcc.cc5303;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.rmi.RemoteException;

public class Board extends Canvas {

	private static final long serialVersionUID = -3391617458338607181L;

	public int width, height;

    public IPlayer p1; 
    public Player p2;
    public Bench[] bases;
    public Image img;
    public Graphics buffer;

    public Board(int w, int h){
        this.width = w;
        this.height = h;
    }

    public void update(Graphics g) { paint(g); }

    public void paint(Graphics g){
        if(buffer==null){
        	System.out.println("?");
            img = createImage(getWidth(),getHeight() );
            buffer = img.getGraphics();
        }

        buffer.setColor(Color.black);
        buffer.fillRect(0, 0, getWidth(), getHeight());;

        //Move rendering to client side -- p1.draw(buffer);
        buffer.setColor(Color.red);
        try {
			buffer.fillRect(p1.getPosX(), p1.getPosY(), p1.getW(), p1.getH());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        buffer.setColor(Color.blue);
        p2.draw(buffer);

        buffer.setColor(Color.white);
        for(Bench base : bases){
            base.draw(buffer);
        }

        g.drawImage(img, 0, 0, null);
    }

    @Override
    public String toString(){
        String ret = "Tablero: dimensions " + this.width + "x" + this.height + "\n";
        ret += p1.toString() + "\n" + p2.toString();
        return ret;
    }

    public void setBenches(Bench[] benches) {
        this.bases = benches;
    }

    public void levelsDown() {
        for(Bench base: bases) {
            base.levelDown();
        }
    }




}
