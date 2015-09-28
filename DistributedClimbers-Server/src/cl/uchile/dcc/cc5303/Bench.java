package cl.uchile.dcc.cc5303;

import java.awt.Graphics;
import java.io.Serializable;

public class Bench implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 7735274200336289228L;
	int posX, posY;
    int w, h;
    int level;

    public Bench(int x, int width, int level){
        this.w = width;
        this.h = 20;
        this.posX = x;
        // level va desde 0 hasta 5 por vista, si es -1 o 6 sale de la vista.
        this.level = level;
        this.posY = 600 - level*100 - this.h;
    }

    public void draw(Graphics g){
        g.fillRect(this.posX, this.posY, this.w, this.h);
    }

    public int top() {
        return this.posY;
    }

    public int left() {
        return this.posX;
    }

    public int bottom() {
        return this.posY + this.h;
    }

    public int right() {
        return this.posX + this.w;
    }

    public void levelDown() {
        this.level--;
        this.posY = 600 - level*100 - this.h;
    }

    public int getLevel() {
        return level;
    }


}
