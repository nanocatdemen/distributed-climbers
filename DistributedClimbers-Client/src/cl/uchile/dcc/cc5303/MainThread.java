package cl.uchile.dcc.cc5303;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;

import javax.swing.JFrame;

import cl.uchile.dcc.cc5303.server.IServer;

public class MainThread extends Thread {
	public boolean[] keys;
	private final static String TITLE = "Juego - CC5303";
	private final static int WIDTH = 800, HEIGHT = 600;
	private final static int UPDATE_RATE = 60;
	private final static int DX = 5;
	private final static int framesToNewBench = 100;

	private JFrame frame;
	private Board tablero;
	private IPlayer myPlayer;
	private ArrayList<IPlayer> allPlayers;
	private IBenchManager benchManager;
	private IGestor gestor;
	private IServer server;
	private int myID;

	int frames = new Random().nextInt(2 * framesToNewBench);

	public MainThread(String urlServer) throws RemoteException {
		urlServer = "rmi://" + urlServer + ":1099/iceClimbers/";
		keys = new boolean[KeyEvent.KEY_LAST];

		//Jugadores
		allPlayers = new ArrayList<IPlayer>();
		try {
			server = (IServer) Naming.lookup(urlServer + "server");
			String activeServer= server.getActiveServer();
			urlServer = activeServer;
			server = (IServer) Naming.lookup(urlServer + "server");
			gestor = (IGestor) Naming.lookup(urlServer + "gestor");

			myID = gestor.giffPlayer();
			myPlayer = (IPlayer) Naming.lookup(urlServer + "player" + myID);
			if(!myPlayer.isAlive())
				myPlayer.reset();
			for(int i = 0; i < gestor.getNbOfPlayers(); i++) {
				allPlayers.add((IPlayer) Naming.lookup(urlServer + "player" + i));
			}
			benchManager = (IBenchManager) Naming.lookup(urlServer + "benchManager"); 
		} catch (MalformedURLException | RemoteException | NotBoundException e1) {
			e1.printStackTrace();
		}
		frame = new JFrame(TITLE);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		tablero = new Board(WIDTH, HEIGHT, gestor.getNbOfPlayers());
		Board.players = allPlayers;
		for(int i = 0; i < benchManager.nbOfBenches(); i++)
			tablero.bases.add(benchManager.getBenches(i));

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

	public void clientMigrate() throws RemoteException, MalformedURLException, NotBoundException{
		if(server.needMigrate().get(myID)) {
			ArrayList<Boolean> migrate = server.needMigrate();
			migrate.set(myID, false);
			server.setNeedMigrate(migrate);
			String anotherServerURL = server.getMigrateURL();
			IServer anotherServer = (IServer) Naming.lookup(anotherServerURL + "server");
			anotherServer.migrateData(server);
			server = anotherServer;
			gestor = (IGestor) Naming.lookup(anotherServerURL + "gestor");
			myPlayer = (IPlayer) Naming.lookup(anotherServerURL + "player" + myID);
			for(int i = 0; i < gestor.getNbOfPlayers(); i++) {
				allPlayers.set(i, (IPlayer) Naming.lookup(anotherServerURL + "player" + i));
			}
			benchManager = (IBenchManager) Naming.lookup(anotherServerURL + "benchManager");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {

			}
		}
	}

	@SuppressWarnings("static-access")
	@Override
	public void run() {

		try {
			while(true){
				if(server.needMigrate().get(myID)) {
					clientMigrate();
				}
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

				if(server.needMigrate().get(myID)) {
					clientMigrate();
				}
				//Check game state
				//Check Pause
				while(gestor.isPaused()){
					//Listen to Unpause
					if(keys[KeyEvent.VK_P]) {
						gestor.resume();
						tablero.repaint();
						Thread.sleep(100);
					}
				}
				//Check Game Over
				tablero.isGameOver = false;
				while(gestor.gameOver(allPlayers)) {
					tablero.repaint();
					tablero.isGameOver = true;
					// Revancha?
					if (keys[KeyEvent.VK_ENTER]) {
						gestor.IWantRevancha(myID);
						if(gestor.allWantRevancha()) {
							synchronized (gestor.getMutex()) {
								gestor.resetGame(allPlayers, benchManager);
								for(int i = 0; i < benchManager.nbOfBenches(); i++)
									tablero.bases.set(i, benchManager.getBenches(i));
							}
							gestor.doNotifyAll();
							continue;
						} else {
							gestor.doWait();
							if (gestor.isGameOver()) {
								System.out.println("Juego terminado");
								System.exit(0);
							}
							synchronized (gestor.getMutex()) {
								for(int i = 0; i < benchManager.nbOfBenches(); i++)
									tablero.bases.set(i, benchManager.getBenches(i));
							}
							continue;
						}
					}
					if (keys[KeyEvent.VK_ESCAPE]) {
						gestor.weLost(myID);
						gestor.doNotifyAll();
						System.exit(0);
					}
					if(gestor.isGameOver()){
						System.exit(0);
					}
				}



				// Check controls
				if (keys[KeyEvent.VK_UP]) {
					server.makePlayerJump(myPlayer);
				}
				if (keys[KeyEvent.VK_RIGHT]) {
					server.movePlayerRight(myPlayer);
				}
				if (keys[KeyEvent.VK_LEFT]) {
					server.movePlayerLeft(myPlayer);
				}
				if(keys[KeyEvent.VK_P]) {
					gestor.pause();
					Thread.sleep(100);
				}
				if(keys[KeyEvent.VK_Q]) {
					server.deletePlayer(myID);
					server = server.migrate("JUGADOR QUITER");
					System.exit(1);
				}
				//update players
				myPlayer.update(DX);

				//update barras
				boolean levelsDown = false;
				for (IBench barra : tablero.bases) {
					if (myPlayer.hitBench(barra))
						myPlayer.setSpeed(0.4);
					else if (myPlayer.collideBench(barra)) {
						myPlayer.setSpeed(0.01);
						myPlayer.setStandUp(true);
						if (barra.getLevel() > 2){
							levelsDown = true;
						}
					}
				}
				if(myPlayer.getPosY() > HEIGHT && myPlayer.isAlive()){
					myPlayer.setLives(myPlayer.getLives()-1);
					if(myPlayer.hasLives()){
						myPlayer.setPosY(300);
						myPlayer.setPosX(WIDTH/2);
						myPlayer.setSpeed(-0.5);
					}
					else{
						myPlayer.die();
					}
				}

				for (IPlayer player: tablero.players){
					if(myPlayer.pushPlayerRight(player)){
						server.movePlayerRight(player);
					}
					else if(myPlayer.pushPlayerLeft(player)){
						server.movePlayerLeft(player);
					}
				}

				// Update board
				if (levelsDown) {
					tablero.levelsDown();
				}

				tablero.repaint();
				if(!gestor.isPause()){
					gestor.doAwait();
				}
				if(myPlayer.isAlive())
					server.setPlayerScore(myPlayer,+1);


				Thread.sleep(100 / UPDATE_RATE);

			}
		}
		catch (RemoteException | MalformedURLException | NotBoundException | InterruptedException | BrokenBarrierException e){
			e.printStackTrace();
		}
	}
}