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

}
