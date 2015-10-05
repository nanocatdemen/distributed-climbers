package cl.uchile.dcc.cc5303;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Bench extends UnicastRemoteObject implements Serializable, IBench{
    /**
	 * 
	 */
	private static final long serialVersionUID = 7735274200336289228L;
	int posX, posY;
    int w, h;
    int level;

    public Bench(int x, int width, int level) throws RemoteException{
        this.w = width;
        this.h = 20;
        this.posX = x;
        // level va desde 0 hasta 5 por vista, si es -1 o 6 sale de la vista.
        this.level = level;
        this.posY = 600 - level*100 - this.h;
    }

    /* (non-Javadoc)
	 * @see cl.uchile.dcc.cc5303.IBench#top()
	 */
    @Override
	public int top() {
        return this.posY;
    }

    /* (non-Javadoc)
	 * @see cl.uchile.dcc.cc5303.IBench#left()
	 */
    @Override
	public int left() {
        return this.posX;
    }

    /* (non-Javadoc)
	 * @see cl.uchile.dcc.cc5303.IBench#bottom()
	 */
    @Override
	public int bottom() {
        return this.posY + this.h;
    }

    /* (non-Javadoc)
	 * @see cl.uchile.dcc.cc5303.IBench#right()
	 */
    @Override
	public int right() {
        return this.posX + this.w;
    }

    /* (non-Javadoc)
	 * @see cl.uchile.dcc.cc5303.IBench#levelDown()
	 */
    @Override
	public void levelDown() {
        this.level--;
        this.posY = 600 - level*100 - this.h;
    }

    /* (non-Javadoc)
	 * @see cl.uchile.dcc.cc5303.IBench#getLevel()
	 */
    @Override
	public int getLevel() {
        return level;
    }


}
