package client;

import java.util.concurrent.Callable;

/**
 * Created by maciejmarzeta on 17.06.2018.
 */
public class MultiThreadCalc implements Runnable {
    Thread thread;
    String threadName;
    Boolean[] mstSet;
    Integer[] key;
    int v_from, v_to;
    int[] result;
    RemoteConnection server;

    public MultiThreadCalc(Boolean[] mstSet, Integer[] key, int v_from, int v_to, RemoteConnection server, String threadName) {
        this.mstSet = mstSet;
        this.key = key;
        this.v_from = v_from;
        this.v_to = v_to;
        this.server = server;
        this.threadName = threadName;

        this.start();
    }

    @Override
    public void run() {
        try{
            this.result = this.server.calcMinKey(this.mstSet, this.key, this.v_from, this.v_to);
        }catch (Throwable e){
            System.err.println("RemoteConnection error: " + e.toString());
        }
    }

    public void start(){
        if(thread == null){
            thread = new Thread(this, this.threadName);
            thread.start();
        }
    }

    public int[] getResult(){
        return this.result;
    }

    public Thread getThread(){
        return this.thread;
    }
}
