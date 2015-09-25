package cl.uchile.dcc.cc5303;

import java.awt.Graphics;

public interface IPlayer {

	void jump();

	void moveRight();

	void moveLeft();

	void update(int dx);

	void draw(Graphics g);

	String toString();

	boolean collide(Bench b);

	boolean hit(Bench b);

	int top();

	int left();

	int bottom();

	int right();

	int getPosX();

	void setPosX(int posX);

	int getPosY();

	void setPosY(int posY);

	int getW();

	void setW(int w);

	int getH();

	void setH(int h);

	double getSpeed();

	void setSpeed(double speed);

	boolean isStandUp();

	void setStandUp(boolean standUp);

}