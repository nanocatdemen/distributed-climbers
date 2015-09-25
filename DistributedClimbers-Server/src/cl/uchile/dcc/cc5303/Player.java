package cl.uchile.dcc.cc5303;

import java.awt.*;

/**
 * Created by sebablasko on 9/11/15.
 */
public class Player implements IPlayer {

    int posX, posY, w = 7, h = 10;
    double speed = 0.4;
    public boolean standUp = false;

    public Player(int x, int y){
        this.posX = x;
        this.posY = y;
    }

    /* (non-Javadoc)
	 * @see cl.uchile.dcc.cc5303.IPlayer#jump()
	 */
    @Override
	public void jump(){
        if(this.standUp)
            this.speed = -0.9;
        this.standUp = false;
    }

    /* (non-Javadoc)
	 * @see cl.uchile.dcc.cc5303.IPlayer#moveRight()
	 */
    @Override
	public void moveRight() {
        this.posX += 2;
    }

    /* (non-Javadoc)
	 * @see cl.uchile.dcc.cc5303.IPlayer#moveLeft()
	 */
    @Override
	public void moveLeft() {
        this.posX -= 2;
    }

    /* (non-Javadoc)
	 * @see cl.uchile.dcc.cc5303.IPlayer#update(int)
	 */
    @Override
	public void update(int dx){
        this.posY += this.speed*dx;
        this.speed += this.speed < 0.8 ? 0.02: 0;
        this.standUp = (this.speed == 0);
    }

    /* (non-Javadoc)
	 * @see cl.uchile.dcc.cc5303.IPlayer#draw(java.awt.Graphics)
	 */
    @Override
	public void draw(Graphics g){
        g.fillRect(this.posX, this.posY, this.w, this.h);
    }

    /* (non-Javadoc)
	 * @see cl.uchile.dcc.cc5303.IPlayer#toString()
	 */
	@Override
    public String toString(){
        return "player: position ("+this.posX+","+this.posY+")";
    }

//    /* (non-Javadoc)
//	 * @see cl.uchile.dcc.cc5303.IPlayer#collide(cl.uchile.dcc.cc5303.Bench)
//	 */
//    @Override
//	public boolean collide(Bench b){
//        return Math.abs(bottom() - b.top()) < 5 && right() <= b.right() && left() >= b.left();
//    }
//
//    /* (non-Javadoc)
//	 * @see cl.uchile.dcc.cc5303.IPlayer#hit(cl.uchile.dcc.cc5303.Bench)
//	 */
//    @Override
//	public boolean hit(Bench b){
//        return Math.abs(top() - b.bottom()) < 5 && right() <= b.right() && left() >= b.left();
//    }

    /* (non-Javadoc)
	 * @see cl.uchile.dcc.cc5303.IPlayer#top()
	 */
    @Override
	public int top() {
        return this.posY;
    }

    /* (non-Javadoc)
	 * @see cl.uchile.dcc.cc5303.IPlayer#left()
	 */
    @Override
	public int left() {
        return this.posX;
    }

    /* (non-Javadoc)
	 * @see cl.uchile.dcc.cc5303.IPlayer#bottom()
	 */
    @Override
	public int bottom() {
        return this.posY + this.h;
    }

    /* (non-Javadoc)
	 * @see cl.uchile.dcc.cc5303.IPlayer#right()
	 */
    @Override
	public int right() {
        return this.posX + this.w;
    }

	/* (non-Javadoc)
	 * @see cl.uchile.dcc.cc5303.IPlayer#getPosX()
	 */
	@Override
	public int getPosX() {
		return posX;
	}

	/* (non-Javadoc)
	 * @see cl.uchile.dcc.cc5303.IPlayer#setPosX(int)
	 */
	@Override
	public void setPosX(int posX) {
		this.posX = posX;
	}

	/* (non-Javadoc)
	 * @see cl.uchile.dcc.cc5303.IPlayer#getPosY()
	 */
	@Override
	public int getPosY() {
		return posY;
	}

	/* (non-Javadoc)
	 * @see cl.uchile.dcc.cc5303.IPlayer#setPosY(int)
	 */
	@Override
	public void setPosY(int posY) {
		this.posY = posY;
	}

	/* (non-Javadoc)
	 * @see cl.uchile.dcc.cc5303.IPlayer#getW()
	 */
	@Override
	public int getW() {
		return w;
	}

	/* (non-Javadoc)
	 * @see cl.uchile.dcc.cc5303.IPlayer#setW(int)
	 */
	@Override
	public void setW(int w) {
		this.w = w;
	}

	/* (non-Javadoc)
	 * @see cl.uchile.dcc.cc5303.IPlayer#getH()
	 */
	@Override
	public int getH() {
		return h;
	}

	/* (non-Javadoc)
	 * @see cl.uchile.dcc.cc5303.IPlayer#setH(int)
	 */
	@Override
	public void setH(int h) {
		this.h = h;
	}

	/* (non-Javadoc)
	 * @see cl.uchile.dcc.cc5303.IPlayer#getSpeed()
	 */
	@Override
	public double getSpeed() {
		return speed;
	}

	/* (non-Javadoc)
	 * @see cl.uchile.dcc.cc5303.IPlayer#setSpeed(double)
	 */
	@Override
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	/* (non-Javadoc)
	 * @see cl.uchile.dcc.cc5303.IPlayer#isStandUp()
	 */
	@Override
	public boolean isStandUp() {
		return standUp;
	}

	/* (non-Javadoc)
	 * @see cl.uchile.dcc.cc5303.IPlayer#setStandUp(boolean)
	 */
	@Override
	public void setStandUp(boolean standUp) {
		this.standUp = standUp;
	}
}
