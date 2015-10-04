package cl.uchile.dcc.cc5303;

import java.rmi.RemoteException;

public class Main {

    //Parametros del Juego
    static final public int ANCHO = 800;
    static final public int ALTO = 600;

    public static void main(String[] args) throws RemoteException {
        MainThread m = new MainThread();
        m.start();
    }
}
