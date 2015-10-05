package cl.uchile.dcc.cc5303;

import java.awt.Graphics;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPlayer extends Remote {

	void jump() throws RemoteException;

	void moveRight() throws RemoteException;

	void moveLeft() throws RemoteException;

	void update(int dx) throws RemoteException;

	void draw(Graphics g) throws RemoteException;

	//String toString() throws RemoteException;

	boolean collide(IBench b) throws RemoteException;

	boolean hit(IBench b) throws RemoteException;

	int top() throws RemoteException;

	int left() throws RemoteException;

	int bottom() throws RemoteException;

	int right() throws RemoteException;

	int getPosX() throws RemoteException;

	void setPosX(int posX) throws RemoteException;

	int getPosY() throws RemoteException;

	void setPosY(int posY) throws RemoteException;

	int getW() throws RemoteException;

	void setW(int w) throws RemoteException;

	int getH() throws RemoteException;

	void setH(int h) throws RemoteException;

	double getSpeed() throws RemoteException;

	void setSpeed(double speed) throws RemoteException;

	boolean isStandUp() throws RemoteException;

	void setStandUp(boolean standUp) throws RemoteException;
	
	public boolean isWaiting() throws RemoteException;

	public void setWaiting(boolean waiting) throws RemoteException;

	public void down() throws RemoteException;

}