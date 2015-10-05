package cl.uchile.dcc.cc5303;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class BenchManager extends UnicastRemoteObject implements IBenchManager {
	
	private static final long serialVersionUID = 8149048047551563401L;
	private ArrayList<IBench> benches;
	
	public BenchManager() throws RemoteException {
		benches = new ArrayList<>();
	}
	@Override
	public void add(IBench bench){
		benches.add(bench);		
	}
	@Override
	public ArrayList<IBench> getBenches(){
		return benches;
	}
	@Override
	public void setBenches(ArrayList<IBench> benches){
		this.benches = benches;
	}
}
