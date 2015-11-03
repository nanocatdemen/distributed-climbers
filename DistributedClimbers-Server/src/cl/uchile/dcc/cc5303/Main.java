package cl.uchile.dcc.cc5303;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Main {

    public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
        ServerThread m = new ServerThread(args);
        m.start();
    }
}
