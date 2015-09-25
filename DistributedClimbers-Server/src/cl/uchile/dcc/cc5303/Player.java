package cl.uchile.dcc.cc5303;

import java.awt.Graphics;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Player extends UnicastRemoteObject implements IPlayer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Player() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void jump() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveRight() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveLeft() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(int dx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int top() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int left() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int bottom() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int right() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPosX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setPosX(int posX) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getPosY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setPosY(int posY) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getW() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setW(int w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getH() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setH(int h) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setSpeed(double speed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isStandUp() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setStandUp(boolean standUp) {
		// TODO Auto-generated method stub
		
	}

}
