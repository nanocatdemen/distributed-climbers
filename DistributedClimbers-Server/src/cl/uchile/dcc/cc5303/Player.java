package cl.uchile.dcc.cc5303;

import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Player extends UnicastRemoteObject implements IPlayer {

	private static final long serialVersionUID = 3446923264217560693L;
	int posX, posY, w = 7, h = 10;
    double speed = 0.4;
    boolean standUp = false;
    boolean waiting = true;

    public Player(int x, int y) throws RemoteException {
        this.posX = x;
        this.posY = y;
    }

    @Override
	public void jump(){
        if(this.standUp)
            this.speed = -0.9;
        this.standUp = false;
    }

    @Override
	public void moveRight() {
        this.posX += 2;
    }

    @Override
	public void moveLeft() {
        this.posX -= 2;
    }

    @Override
	public void update(int dx){
        this.posY += this.speed*dx;
        this.speed += this.speed < 0.8 ? 0.02: 0;
        this.standUp = (this.speed == 0);
    }

    @Override
	public void draw(Graphics g){
        g.fillRect(this.posX, this.posY, this.w, this.h);
    }

//	@Override
//    public String toString(){
//        return "player: position ("+this.posX+","+this.posY+")";
//    }

	@Override
	public boolean collideBench(IBench b) throws RemoteException{
        return Math.abs(bottom() - b.top()) < 5 && right() <= b.right() && left() >= b.left();
    }

	@Override 
	public boolean hitBench(IBench b) throws RemoteException{
        return Math.abs(top() - b.bottom()) < 5 && right() <= b.right() && left() >= b.left();
    }

    @Override
	public int top() {
        return this.posY;
    }

    @Override
	public int left() {
        return this.posX;
    }

    @Override
	public int bottom() {
        return this.posY + this.h;
    }

    @Override
	public int right() {
        return this.posX + this.w;
    }

	@Override
	public int getPosX() {
		return posX;
	}

	@Override
	public void setPosX(int posX) {
		this.posX = posX;
	}

	@Override
	public int getPosY() {
		return posY;
	}

	@Override
	public void setPosY(int posY) {
		this.posY = posY;
	}

	@Override
	public int getW() {
		return w;
	}

	@Override
	public void setW(int w) {
		this.w = w;
	}

	@Override
	public int getH() {
		return h;
	}

	@Override
	public void setH(int h) {
		this.h = h;
	}

	@Override
	public double getSpeed() {
		return speed;
	}

	@Override
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	@Override
	public boolean isStandUp() {
		return standUp;
	}

	@Override
	public void setStandUp(boolean standUp) {
		this.standUp = standUp;
	}

	@Override
	public boolean isWaiting() {
		return waiting;
	}

	@Override
	public void setWaiting(boolean waiting) {
		this.waiting = waiting;
	}

	@Override
	public void down(){
		this.posY+=100;
	}

	@Override
	public boolean pushPlayerRight(IPlayer p) throws RemoteException {
		return bottom() > p.top() && top() < p.bottom() && Math.abs(right() - p.left()) < 2;
	}

	@Override
	public boolean pushPlayerLeft(IPlayer p) throws RemoteException {
		return bottom() > p.top() && top() < p.bottom() && Math.abs(left() - p.right()) < 2;
	}
}
