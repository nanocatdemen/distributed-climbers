package cl.uchile.dcc.cc5303;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;


public class Controller extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;

	private static final String JUMP = "jump";
	private static final String MOVE_LEFT = "move_left";
	private static final String MOVE_RIGHT = "move_right";
	static MyComponent comp = new MyComponent();
	private IPlayer p;

	public Controller(String title) {
		super(title);
		comp.getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), JUMP);
		comp.getActionMap().put(JUMP, new Jump());
		comp.getInputMap(IFW).put(KeyStroke.getKeyStroke("LEFT"), MOVE_LEFT);
		comp.getActionMap().put(MOVE_LEFT, new MoveLeft());
		comp.getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), MOVE_RIGHT);
		comp.getActionMap().put(MOVE_RIGHT, new MoveRight());
		add(comp);
	}
	
	public void subscribePlayer(IPlayer player) {
		p = player;
	}

	private class Jump extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				p.jump();
			} catch (RemoteException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	private class MoveLeft extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				p.moveLeft();
			} catch (RemoteException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	private class MoveRight extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				p.moveRight();
			} catch (RemoteException ex) {
				ex.printStackTrace();
			}
		}
	}

}
