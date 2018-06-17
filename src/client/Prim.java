package client;

public class Prim {
    int num_of_vertices;
    // do przechowywania wierzcholkow ktore juz sa MST
    Integer[] parent;
    // do wyboru najmniejszych krawedzi
    Integer[] key;
    // do trzymania wierzcholkow ktore jeszcze nie sa zawarte w grafie
    Boolean[] mstSet;
    // trzyma caly graf
    Integer[][] graph;
    // polaczenia z serwerami
    RemoteConnection server1;
    RemoteConnection server2;


    public Prim(String adj_matrix_name, RemoteConnection srv1, RemoteConnection srv2) {
        this.graph = Helper.textToMatrixAdjacency(adj_matrix_name);
        this.num_of_vertices = graph.length;
        this.parent = new Integer[this.num_of_vertices];
        this.key = new Integer[this.num_of_vertices];
        this.mstSet = new Boolean[this.num_of_vertices];
        this.server1 = srv1;
        this.server2 = srv2;

        // ustaw klucze na inf
        for (int i = 0; i < this.num_of_vertices; i++) {
            this.key[i] = Integer.MAX_VALUE;
            this.mstSet[i] = false;
        }
    }

    private int getMin(int v1, int v2) {
        if (Math.min(v1, v2) < 0) {
            return Math.max(v1, v2);
        } else {
            return Math.min(v1, v2);
        }
    }

    // wykonuje algorytm
    public void compute() {
        // ustaw 1szy wierzcholek
        this.key[0] = 0;
        this.parent[0] = -1; // -1 jako korzen


        for (int count = 0; count < this.num_of_vertices - 1; count++) {
            int border = (this.num_of_vertices / 2);
            System.out.println("Border: " + border);

            MultiThreadCalc thread1 = new MultiThreadCalc(
                    this.mstSet,
                    this.key,
                    0,
                    this.num_of_vertices,
                    this.server1,
                    "Thread 1");
            MultiThreadCalc thread2 = new MultiThreadCalc(
                    this.mstSet,
                    this.key,
                    0,
                    this.num_of_vertices,
                    this.server2,
                    "Thread 2");

            try {
                // implicite odpala run()
                thread1.getThread().join();
                thread2.getThread().join();
            } catch (InterruptedException e) {
                System.err.println("Interrupt error: " + e.toString());
            }

            int[] u1 = thread1.getResult();
            int[] u2 = thread2.getResult();

            int u = getMin(u1[1], u2[1]);
            int serv_choice;

            if (u1[1] == u) {
                u = u1[0];
                serv_choice = 1;
            } else {
                u = u2[0];
                serv_choice = 2;
            }
            System.out.println("Choosen index of vertices: " + u + " from Server" + serv_choice);
            System.out.println("-------");

            mstSet[u] = true;

            for (int v = 0; v < this.num_of_vertices; v++) {
                if (this.graph[u][v] != 0 && this.mstSet[v] == false &&
                        this.graph[u][v] < key[v]) {
                    parent[v] = u;
                    key[v] = this.graph[u][v];
                }
            }

        }
    }

    // printuje
    public void printMST() {
        System.out.println("Krawedz  Waga");
        int waga_MST = 0;
        for (int i = 1; i < this.num_of_vertices; i++) {
            System.out.println(this.parent[i]
                    + " - " + i + "    " + this.graph[i][parent[i]]);
            waga_MST += this.graph[i][parent[i]];
        }
        System.out.println("Waga MST: " + waga_MST);
    }

    public static void main(String[] args) {
        RemoteConnection[] srv = Helper.getConnection(args);
        Prim p = new Prim("adj_matrix", srv[0], srv[1]);

        p.compute();
        p.printMST();
    }
}
