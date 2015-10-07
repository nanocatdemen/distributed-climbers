package cl.uchile.dcc.cc5303;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

public class MainThread extends Thread {
	public boolean[] keys;
	private final static String TITLE = "Juego - CC5303";
	private final static int WIDTH = 800, HEIGHT = 600;
	private final static int UPDATE_RATE = 60;
	private final static int DX = 5;
	@SuppressWarnings("unused")
	private final static double DV = 0.1;
	private final static int framesToNewBench = 100;
	@SuppressWarnings("unused")
	private final double vy = 0.3;

	private JFrame frame;
	private Board tablero;
	private IPlayer myPlayer;
	private ArrayList<IPlayer> allPlayers;
	//private ArrayList<IBench> allBenches;
	private IBenchManager benchManager;
	private IGestor gestor;
	private int myID;

	int frames = new Random().nextInt(2 * framesToNewBench);

	public MainThread(String urlServer) throws RemoteException {
		urlServer = "rmi://" + urlServer + ":1099/iceClimbers";
		keys = new boolean[KeyEvent.KEY_LAST];

		//Jugadores
		allPlayers = new ArrayList<IPlayer>();
		//allBenches = new ArrayList<IBench>();
		try {
			gestor = (IGestor) Naming.lookup(urlServer + "/gestor");
			myID = gestor.giffPlayer();
			myPlayer = (IPlayer) Naming.lookup(urlServer + "/player" + myID);
			for(int i = 0; i < gestor.getNbOfPlayers(); i++) {
				allPlayers.add((IPlayer) Naming.lookup(urlServer + "/player" + i));
			}
			benchManager = (IBenchManager) Naming.lookup(urlServer + "/benchManager"); 
		} catch (MalformedURLException | RemoteException | NotBoundException e1) {
			e1.printStackTrace();
		}

		frame = new JFrame(TITLE);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		tablero = new Board(WIDTH, HEIGHT);
		tablero.players = allPlayers;
		tablero.bases = benchManager;

		frame.add(tablero);
		tablero.setSize(WIDTH, HEIGHT);

		frame.pack();
		frame.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {
				keys[e.getKeyCode()] = true;
			}

			@Override
			public void keyReleased(KeyEvent e) {
				keys[e.getKeyCode()] = false;
			}
		});

	}

	@Override
	public void run() {
		try {
			while(true){
				tablero.repaint();
				if(gestor.areAllTaken()) {
					gestor.doNotifyAll();
					myPlayer.setWaiting(false);
					break;
				} else {
					myPlayer.setWaiting(false);
					gestor.doNotifyAll();
					gestor.doWait();
				}
			}
			while (true) { // Main loop
				//Check game state
				if(gestor.gameOver(allPlayers)) {
					tablero.isGameOver = true;
					// Revancha?
					if (keys[KeyEvent.VK_ENTER]) {
						gestor.IWantRevancha(myID);
						if(gestor.allWantRevancha()) {
							tablero.isGameOver = false;
							gestor.resetGame(allPlayers);
							gestor.doNotifyAll();
							continue;
						} else {
							gestor.doWait();
							tablero.isGameOver = false;
							continue;
						}
					}
					if (keys[KeyEvent.VK_ESCAPE]) {
						System.exit(0);
					}
				}
				// Check controls
				else {
					if (keys[KeyEvent.VK_UP]) {
						myPlayer.jump();
					}
					if (keys[KeyEvent.VK_RIGHT]) {
						myPlayer.moveRight();
					}
					if (keys[KeyEvent.VK_LEFT]) {
						myPlayer.moveLeft();
					}
				}
				//update players
				myPlayer.update(DX);

				//update barras
				boolean levelsDown = false;
				for (IBench barra : tablero.bases.getBenches()) {
					if (myPlayer.hitBench(barra))
						myPlayer.setSpeed(0.8);
					else if (myPlayer.collideBench(barra)) {
						myPlayer.setSpeed(0.01);
						myPlayer.setStandUp(true);
						if (barra.getLevel() > 2){
							levelsDown = true;
						}
					}
				}
				if(myPlayer.getPosY() > HEIGHT){
					myPlayer.setLives(myPlayer.getLives()-1);
					if(myPlayer.hasLives()){
						myPlayer.setPosY(0);
						myPlayer.setPosX(WIDTH/2);
					}
				}

				for (IPlayer player: tablero.players){
					if(myPlayer.pushPlayerRight(player)){
						player.moveRight();
					}
					else if(myPlayer.pushPlayerLeft(player)){
						player.moveLeft();
					}
				}

				// Update board
				if (levelsDown) {
					tablero.levelsDown();
				}

				tablero.repaint();
				try {
					Thread.sleep(1000 / UPDATE_RATE);
				} catch (InterruptedException ex) {

				}
			}
		}
		catch (RemoteException e){
			e.printStackTrace();
		}
	}
}