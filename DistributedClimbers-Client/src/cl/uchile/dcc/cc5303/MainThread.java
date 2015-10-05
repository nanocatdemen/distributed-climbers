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
    private ArrayList<IBench> allBenches;
    private IGestor gestor;
    
    int frames = new Random().nextInt(2 * framesToNewBench);

//    private Bench[] benches = {
//            new Bench(0, 800, 0),
//            new Bench(100, 200, 1),
//            new Bench(400, 200, 1),
//            new Bench(300, 100, 2),
//            new Bench(600, 200, 2),
//            new Bench(150, 200, 3),
//            new Bench(700, 100, 3),
//            new Bench(75, 200, 4),
//            new Bench(350, 350, 4),
//            new Bench(200, 200, 5),
//            new Bench(400, 400, 6),
//            new Bench(200, 400, 7),
//            new Bench(150, 200, 8),
//            new Bench(75, 100, 9),
//            new Bench(50, 100, 10)
//    };

    public MainThread() throws RemoteException {
        keys = new boolean[KeyEvent.KEY_LAST];

        String urlServer = "rmi://localhost:1099/iceClimbers";
        //Jugadores
        allPlayers = new ArrayList<IPlayer>();
        allBenches = new ArrayList<IBench>();
        try {
        	gestor = (IGestor) Naming.lookup(urlServer + "/gestor");
        	int myPlayerNumber = gestor.giffPlayer();
			myPlayer = (IPlayer) Naming.lookup(urlServer + "/player" + myPlayerNumber);
			for(int i = 0; i < gestor.getNbOfPlayers(); i++) {
				allPlayers.add((IPlayer) Naming.lookup(urlServer + "/player" + i));
			}
			for(int i = 0; i < gestor.getNbOfBenches(); i++){
				allBenches.add((IBench) Naming.lookup(urlServer + "/bench" + i));
			}
		} catch (MalformedURLException | RemoteException | NotBoundException e1) {
			e1.printStackTrace();
		}

        frame = new JFrame(TITLE);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tablero = new Board(WIDTH, HEIGHT);
        tablero.players = allPlayers;
        tablero.bases = allBenches;

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
			if(gestor.areAllTaken()) {
				gestor.doNotifyAll();
				myPlayer.setWaiting(false);
			} else {
				// TODO: if possible, notify the connected players to update the state
				myPlayer.setWaiting(false);
				gestor.doWait();
			}
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
        while (true) { // Main loop
            //Check controls
        	try {
	            if (keys[KeyEvent.VK_UP]) {
					myPlayer.jump();
	            }
	            if (keys[KeyEvent.VK_RIGHT]) {
	            	myPlayer.moveRight();
	            }
	            if (keys[KeyEvent.VK_LEFT]) {
	            	myPlayer.moveLeft();
	            }
			} catch (RemoteException e) {
				e.printStackTrace();
			}

        	//update players
            try {
            	myPlayer.update(DX);
			} catch (RemoteException e) {
				e.printStackTrace();
			}

            //update barras
            boolean levelsDown = false;
	        for (IBench barra : tablero.bases) {
	        	try {
	                if (myPlayer.hit(barra))
	                	myPlayer.setSpeed(0.8);
	                else if (myPlayer.collide(barra)) {
	                	myPlayer.setSpeed(0.01);
	                	myPlayer.setStandUp(true);
	                    if (barra.getLevel() > 2){
	                        levelsDown = true;
	                    }
	                }
	            } catch (RemoteException e) {
	            	e.printStackTrace();
	            }
            }

            // Update board
            if (levelsDown) {
                try {
					tablero.levelsDown();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
            }

            tablero.repaint();

            try {
                Thread.sleep(1000 / UPDATE_RATE);
            } catch (InterruptedException ex) {

            }
        }
    }
}
