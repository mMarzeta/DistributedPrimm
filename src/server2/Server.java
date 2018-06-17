package server2;

import client.RemoteConnection;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by maciejmarzeta on 13.06.2018.
 */
public class Server implements RemoteConnection {
    public Server() {
        super();
    }

    @Override
    public int[] calcMinKey(Boolean[] mstSet, Integer[] key,
                            int v_from, int v_to) throws RemoteException {
        int min = Integer.MAX_VALUE;
        int min_index = -1;

        for (int v = v_from; v < v_to; v += 2) {
            if (mstSet[v] == false && key[v] < min) {
                min = key[v];
                min_index = v;
            }
        }
        System.out.println("Serv2 index: " + min_index);
        System.out.println("Serv2 value: " + min);
        return new int[]{min_index, min};
    }

    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "Server2";
            RemoteConnection engine = new Server();
            RemoteConnection stub = (RemoteConnection) UnicastRemoteObject.exportObject(
                    engine, 0
            );
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println(name + " up and running!");
        } catch (RemoteException e) {
            System.err.println("Server2 Exception:");
            e.printStackTrace();
        }
    }
}
