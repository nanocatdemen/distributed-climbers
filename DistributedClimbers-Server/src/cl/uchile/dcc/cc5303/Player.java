package cl.uchile.dcc.cc5303;

import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Player extends UnicastRemoteObject implements IPlayer {

	private static final long serialVersionUID = 3446923264217560693L;
	int posX, posY, w = 12, h = 20, lives;
	double speed = 0.4;
	boolean standUp = false;
	boolean waiting = true;
	int startPosX, startPosY, startLives;
	boolean alive = true;
	int score = 0;
	int id;

	public Player(int x, int y, int lives, int id) throws RemoteException {
		this.posX = x;
		this.posY = y;
		this.startPosX = x;
		this.startPosY = y;
		this.lives = lives;
		this.startLives = lives;
		this.id = id;
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
		if(this.posX > 800){
			this.posX = this.posX - 800;
		}
	}

	@Override
	public void moveLeft() {
		this.posX -= 2;
		if(this.posX < 0){
			this.posX = this.posX + 800;
		}
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

	@Override
	public int getLives(){
		return this.lives;
	}

	@Override
	public void setLives(int lives) throws RemoteException {
		this.lives = lives;
	}

	@Override
	public boolean hasLives() throws RemoteException {
		return this.getLives()>=0;
	}

	@Override
	public void reset() throws RemoteException {
		this.posX = this.startPosX;
		this.posY = this.startPosY;
		this.lives = this.startLives;
		this.alive = true;
		this.score = 0;
	}
	
	@Override
	public void die(){
		this.alive = false;
	}
	
	@Override
	public boolean isAlive(){
		return this.alive;
	}
	
	@Override
	public void setAlive(boolean b){
		this.alive = b;
	}
	
	@Override
	public int getScore() {
		return score;
	}

	@Override
	public void setScore(int score) {
		this.score = score;
	}
	
	@Override
	public int getStartLives() {
		return startLives;
	}

	@Override
	public void setStartLives(int startLives) {
		this.startLives = startLives;
	}
	
	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}
}
