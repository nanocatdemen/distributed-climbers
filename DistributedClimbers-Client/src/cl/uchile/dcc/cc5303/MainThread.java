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

import cl.uchile.dcc.cc5303.server.IServer;

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
	private IServer server;
	private int myID;

	int frames = new Random().nextInt(2 * framesToNewBench);

	public MainThread(String urlServer) throws RemoteException {
		urlServer = "rmi://" + urlServer + ":1099/iceClimbers";
		keys = new boolean[KeyEvent.KEY_LAST];

		//Jugadores
		allPlayers = new ArrayList<IPlayer>();
		//allBenches = new ArrayList<IBench>();
		try {
			server = (IServer) Naming.lookup(urlServer + "/server");
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

		tablero = new Board(WIDTH, HEIGHT, gestor.getNbOfPlayers());
		tablero.players = allPlayers;
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

	@Override
	public void run() {
		try {
			while(true){
//				System.out.println(server.needMigrate());
//				if(server.needMigrate().get(myID)) {
//					System.out.println("need Migrate");
//					server.needMigrate().set(myID, false);
//					String anotherServerURL = server.getNeighbours().get(0);
//					IServer anotherServer = (IServer) Naming.lookup(anotherServerURL + "server");
//					anotherServer.migrateData(server);
//					server = anotherServer;
//					System.out.println(server.getServerURL());
//					System.out.println(server.getNeighbours());
//					gestor = (IGestor) Naming.lookup(anotherServerURL + "gestor");
//					myPlayer = (IPlayer) Naming.lookup(anotherServerURL + "player" + myID);
//					for(int i = 0; i < gestor.getNbOfPlayers(); i++) {
//						allPlayers.set(i, (IPlayer) Naming.lookup(anotherServerURL + "player" + i));
//					}
//					benchManager = (IBenchManager) Naming.lookup(anotherServerURL + "benchManager");
//					// TODO: notify other clients about the migration
//					try {
//						Thread.sleep(1000);
//					} catch (InterruptedException ex) {
//
//					}
//				}
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
				if(server.CPUover75()) {
					String anotherServerURL = server.getServerMinLoad();
					IServer anotherServer = (IServer) Naming.lookup(anotherServerURL + "server");
					anotherServer.migrateData(server);
					server.setMigrateURL(anotherServerURL);
					System.out.println(server.getMigrateURL());
					ArrayList<Boolean> migrate = server.needMigrate();
					for(int i = 0; i < migrate.size(); i++) {
						if(i != myID) {
							System.out.println(i + " = true");
							migrate.set(i, true);
						}
					}
					server.setNeedMigrate(migrate);
					server = anotherServer;
					gestor = (IGestor) Naming.lookup(anotherServerURL + "gestor");
					myPlayer = (IPlayer) Naming.lookup(anotherServerURL + "player" + myID);
					for(int i = 0; i < gestor.getNbOfPlayers(); i++) {
						allPlayers.set(i, (IPlayer) Naming.lookup(anotherServerURL + "player" + i));
					}
					benchManager = (IBenchManager) Naming.lookup(anotherServerURL + "benchManager");
				}
				if(server.needMigrate().get(myID)) {
					//System.out.println("need Migrate");
					ArrayList<Boolean> migrate = server.needMigrate();
					migrate.set(myID, false);
					server.setNeedMigrate(migrate);
//					System.out.println("Servers load");
//					System.out.println(server.getServerURL() + " load " + server.CPUload());
//					for(String sserver: server.getNeighbours()){
//						IServer otherServer = (IServer)Naming.lookup(sserver+"server");	
//						System.out.println(otherServer.getServerURL() + " load " + otherServer.CPUload());
//					}
					String anotherServerURL = server.getMigrateURL();
					System.out.println("Server to migrate " + anotherServerURL);
					IServer anotherServer = (IServer) Naming.lookup(anotherServerURL + "server");
					anotherServer.migrateData(server);
					server = anotherServer;
//					System.out.println(server.getServerURL());
//					System.out.println(server.getNeighbours());
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
				//Check game state
				//Check Pause
				while(gestor.isPaused()){
					//Listen to Unpause
					if(keys[KeyEvent.VK_P]) {
						gestor.resume();
						tablero.repaint();
					}
				}
				//Check Game Over
				tablero.isGameOver = false;
				if(gestor.gameOver(allPlayers)) {
					for(int i = 0; i<gestor.getNbOfPlayers(); i++){
						tablero.results[i] = gestor.getResults()[i];
					}
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
							if (gestor.dedGaem()) {
								System.out.println("ded gram");
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
					if(gestor.dedGaem()){
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
					if(keys[KeyEvent.VK_P]) {
						gestor.pause();
					}
					//Initialize migration
//					if (keys[KeyEvent.VK_M]) {
//						try {
//							Thread.sleep(1000);
//						} catch (InterruptedException ex) {
//
//						}
//					}
					if(keys[KeyEvent.VK_Q]) {
						server.deletePlayer(myID);
						System.exit(1);
					}
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
						gestor.feed(myID);
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
					Thread.sleep(200 / UPDATE_RATE);
				} catch (InterruptedException ex) {

				}
			}
		}
		catch (RemoteException | MalformedURLException | NotBoundException e){
			e.printStackTrace();
		}
	}
}