package cl.uchile.dcc.cc5303;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;

public class BenchManager extends UnicastRemoteObject implements IBenchManager{
	
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
	public IBench getBenches(int bench){
		return benches.get(bench);
	}
	@Override
	public void setBenches(ArrayList<IBench> benches){
		this.benches = benches;
	}
	@Override
	public void resetBenchs() throws RemoteException {
		int[][] benches  = {
	            {0, 410, 0},
	            {500, 400, 0},
	            {100, 200, 1},
	            {400, 200, 1},
	            {300, 100, 2},
	            {600, 200, 2},
	            {150, 200, 3},
	            {700, 100, 3},
	            {75, 200, 4},
	            {350, 350, 4},
	            {200, 200, 5},
	            {400, 400, 6},
	            {200, 400, 7},
	            {150, 200, 8},
	            {275, 100, 9},
	            {350, 100, 10}
	    };
		this.benches.clear();
		for(int i = 0; i < benches.length; i++){
			IBench bench = new Bench(benches[i][0],benches[i][1],benches[i][2]);
			this.add(bench);
		}	
	}
	@Override
	public int nbOfBenches() throws RemoteException {
		return benches.size();
	}
}
