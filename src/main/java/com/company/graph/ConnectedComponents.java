package com.company.graph;

import java.io.IOException;

import com.company.graph.util.In;
import com.company.graph.util.Queue;

public class ConnectedComponents {

    private boolean[] marked;   // marked[v] = has vertex v been marked?
    private int[] id;           // id[v] = id of connected component containing v
    private int[] size;         // size[id] = number of vertices in given component
    private int count;          // number of connected components

    /**
     * Computes the connected components of the undirected graph <tt>G</tt>.
     * @param G the graph
     */
    public ConnectedComponents(Graph G) {
        marked = new boolean[G.V()];
        id = new int[G.V()];
        size = new int[G.V()];
        for (int v = 0; v < G.V(); v++) {
            if (!marked[v]) {
                dfs(G, v);
                count++;
            }
        }
    }

    // depth-first search
    private void dfs(Graph G, int v) {
        marked[v] = true;
        id[v] = count;
        size[count]++;
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                dfs(G, w);
            }
        }
    }

    /**
     * Returns the component id of the connected component containing vertex <tt>v</tt>.
     * @param v the vertex
     * @return the component id of the connected component containing vertex <tt>v</tt>
     */
    public int id(int v) {
        return id[v];
    }

    /**
     * Returns the number of vertices in the connected component containing vertex <tt>v</tt>.
     * @param v the vertex
     * @return the number of vertices in the connected component containing vertex <tt>v</tt>
     */
    public int size(int v) {
        return size[id[v]];
    }

    /**
     * Returns the number of connected components.
     * @return the number of connected components
     */
    public int count() {
        return count;
    }

    /**
     * Are vertices <tt>v</tt> and <tt>w</tt> in the same connected component?
     * @param v one vertex
     * @param w the other vertex
     * @return <tt>true</tt> if vertices <tt>v</tt> and <tt>w</tt> are in the same
     *     connected component, and <tt>false</tt> otherwise
     */
    public boolean connected(int v, int w) {
        return id(v) == id(w);
    }

    /**
     * Are vertices <tt>v</tt> and <tt>w</tt> in the same connected component?
     * @param v one vertex
     * @param w the other vertex
     * @return <tt>true</tt> if vertices <tt>v</tt> and <tt>w</tt> are in the same
     *     connected component, and <tt>false</tt> otherwise
     * @deprecated Use connected(v, w) instead.
     */
    public boolean areConnected(int v, int w) {
        return id(v) == id(w);
    }


    /**
     * Unit tests the <tt>CC</tt> data type.
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        In in = new In(args[0]);
        Graph G = new Graph(in);
        ConnectedComponents cc = new ConnectedComponents(G);

        // number of connected components
        int M = cc.count();
        System.out.println(M + " components");

        // compute list of vertices in each connected component
        Queue<Integer>[] components = (Queue<Integer>[]) new Queue[M];
        for (int i = 0; i < M; i++) {
            components[i] = new Queue<Integer>();
        }
        for (int v = 0; v < G.V(); v++) {
            components[cc.id(v)].enqueue(v);
        }

        // print results
        for (int i = 0; i < M; i++) {
            for (int v : components[i]) {
            	System.out.print(v + " ");
            }
            System.out.println();
        }
    }
}
