package cl.uchile.dcc.cc5303;

import java.rmi.RemoteException;

public class Main {

    public static void main(String[] args) throws RemoteException {
        MainThread m = new MainThread(args[0]);
        m.start();
    }
}
