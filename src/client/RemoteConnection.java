package client;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by maciejmarzeta on 13.06.2018.
 */
public interface RemoteConnection extends Remote {
    int[] calcMinKey(Boolean[] mstSet,
                     Integer[] key,
                     int v_from,
                     int v_to) throws RemoteException;
}
