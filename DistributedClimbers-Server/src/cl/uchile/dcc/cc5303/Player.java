package cl.uchile.dcc.cc5303;

import java.awt.*;

public class Player implements IPlayer {

    int posX, posY, w = 7, h = 10;
    double speed = 0.4;
    public boolean standUp = false;

    public Player(int x, int y){
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

	@Override
    public String toString(){
        return "player: position ("+this.posX+","+this.posY+")";
    }

	@Override
	public boolean collide(Bench b){
        return Math.abs(bottom() - b.top()) < 5 && right() <= b.right() && left() >= b.left();
    }

	@Override 
	public boolean hit(Bench b){
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
}
