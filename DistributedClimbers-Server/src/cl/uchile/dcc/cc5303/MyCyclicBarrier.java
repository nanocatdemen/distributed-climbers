package cl.uchile.dcc.cc5303;

import java.io.Serializable;
import java.util.concurrent.CyclicBarrier;

public class MyCyclicBarrier extends CyclicBarrier implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2162396347103568324L;

	public MyCyclicBarrier(int parties) {
		super(parties);
	}

}
