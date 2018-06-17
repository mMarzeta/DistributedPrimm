package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

/**
 * Created by maciejmarzeta on 13.06.2018.
 */

public class Helper {

    public static Integer[][] textToMatrixAdjacency(String fileName) {
        ArrayList<Integer[]> result = new ArrayList<>();

        File file = new File("/Users/maciejmarzeta/Documents/ZTI/projekt/Prim/resources/" + fileName);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] splitted = line.split(", ?");
                Integer[] valLine = new Integer[splitted.length];
                for (int i = 0; i < splitted.length; i++) {
                    valLine[i] = Integer.valueOf(splitted[i]);
                }
                result.add(valLine);
            }
        } catch (IOException e) {
            System.err.println("File Not Found!");
        }

        return result.toArray(new Integer[result.size()][result.get(0).length]);
    }

    public static RemoteConnection[] getConnection(String[] args) {
        try {
            String name1 = "Server1";
            String name2 = "Server2";
            Registry registry = LocateRegistry.getRegistry(args[0]);
            RemoteConnection[] srv = new RemoteConnection[2];

            srv[0] = (RemoteConnection) registry.lookup(name1);
            srv[1] = (RemoteConnection) registry.lookup(name2);

            return srv;
        } catch (RemoteException | NotBoundException e) {
            System.err.println("ComputePrim Exception:");
            e.printStackTrace();
        }
        return null;
    }
}
