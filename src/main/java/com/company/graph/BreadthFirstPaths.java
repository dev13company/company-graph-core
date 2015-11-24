package com.company.graph;

import java.io.IOException;
import java.util.Stack;

import org.apache.commons.lang3.StringUtils;

import com.company.graph.util.In;
import com.company.graph.util.Queue;

public class BreadthFirstPaths {

	private static final int INFINITY = Integer.MAX_VALUE;
	private boolean[] marked;  // marked[v] = is there an s-v path
	private int[] edgeTo;      // edgeTo[v] = previous edge on shortest s-v path
	private int[] distTo;      // distTo[v] = number of edges shortest s-v path

	/**
	 * Computes the shortest path between the source vertex <tt>s</tt>
	 * and every other vertex in the graph <tt>G</tt>.
	 * @param G the graph
	 * @param s the source vertex
	 */
	public BreadthFirstPaths(Graph G, int s) {
		marked = new boolean[G.V()];
		distTo = new int[G.V()];
		edgeTo = new int[G.V()];
		bfs(G, s);

		assert check(G, s);
	}

	/**
	 * Computes the shortest path between any one of the source vertices in <tt>sources</tt>
	 * and every other vertex in graph <tt>G</tt>.
	 * @param G the graph
	 * @param sources the source vertices
	 */
	public BreadthFirstPaths(Graph G, Iterable<Integer> sources) {
		marked = new boolean[G.V()];
		distTo = new int[G.V()];
		edgeTo = new int[G.V()];
		for (int v = 0; v < G.V(); v++)
			distTo[v] = INFINITY;
		bfs(G, sources);
	}


	// breadth-first search from a single source
	private void bfs(Graph G, int s) {
		Queue<Integer> q = new Queue<Integer>();
		for (int v = 0; v < G.V(); v++)
			distTo[v] = INFINITY;
		distTo[s] = 0;
		marked[s] = true;
		q.enqueue(s);

		while (!q.isEmpty()) {
			int v = q.dequeue();
			for (int w : G.adj(v)) {
				if (!marked[w]) {
					edgeTo[w] = v;
					distTo[w] = distTo[v] + 1;
					marked[w] = true;
					q.enqueue(w);
				}
			}
		}
	}

	// breadth-first search from multiple sources
	private void bfs(Graph G, Iterable<Integer> sources) {
		Queue<Integer> q = new Queue<Integer>();
		for (int s : sources) {
			marked[s] = true;
			distTo[s] = 0;
			q.enqueue(s);
		}
		while (!q.isEmpty()) {
			int v = q.dequeue();
			for (int w : G.adj(v)) {
				if (!marked[w]) {
					edgeTo[w] = v;
					distTo[w] = distTo[v] + 1;
					marked[w] = true;
					q.enqueue(w);
				}
			}
		}
	}

	/**
	 * Is there a path between the source vertex <tt>s</tt> (or sources) and vertex <tt>v</tt>?
	 * @param v the vertex
	 * @return <tt>true</tt> if there is a path, and <tt>false</tt> otherwise
	 */
	public boolean hasPathTo(int v) {
		return marked[v];
	}

	/**
	 * Returns the number of edges in a shortest path between the source vertex <tt>s</tt>
	 * (or sources) and vertex <tt>v</tt>?
	 * @param v the vertex
	 * @return the number of edges in a shortest path
	 */
	public int distTo(int v) {
		return distTo[v];
	}

	/**
	 * Returns a shortest path between the source vertex <tt>s</tt> (or sources)
	 * and <tt>v</tt>, or <tt>null</tt> if no such path.
	 * @param v the vertex
	 * @return the sequence of vertices on a shortest path, as an Iterable
	 */
	public Iterable<Integer> pathTo(int v) {
		if (!hasPathTo(v)) return null;
		Stack<Integer> path = new Stack<Integer>();
		int x;
		for (x = v; distTo[x] != 0; x = edgeTo[x])
			path.push(x);
		path.push(x);
		return path;
	}


	// check optimality conditions for single source
	private boolean check(Graph G, int s) {

		// check that the distance of s = 0
		if (distTo[s] != 0) {
			System.out.println("distance of source " + s + " to itself = " + distTo[s]);
			return false;
		}

		// check that for each edge v-w dist[w] <= dist[v] + 1
		// provided v is reachable from s
		for (int v = 0; v < G.V(); v++) {
			for (int w : G.adj(v)) {
				if (hasPathTo(v) != hasPathTo(w)) {
					System.out.println("edge " + v + "-" + w);
					System.out.println("hasPathTo(" + v + ") = " + hasPathTo(v));
					System.out.println("hasPathTo(" + w + ") = " + hasPathTo(w));
					return false;
				}
				if (hasPathTo(v) && (distTo[w] > distTo[v] + 1)) {
					System.out.println("edge " + v + "-" + w);
					System.out.println("distTo[" + v + "] = " + distTo[v]);
					System.out.println("distTo[" + w + "] = " + distTo[w]);
					return false;
				}
			}
		}

		// check that v = edgeTo[w] satisfies distTo[w] + distTo[v] + 1
		// provided v is reachable from s
		for (int w = 0; w < G.V(); w++) {
			if (!hasPathTo(w) || w == s) continue;
			int v = edgeTo[w];
			if (distTo[w] != distTo[v] + 1) {
				System.out.println("shortest path edge " + v + "-" + w);
				System.out.println("distTo[" + v + "] = " + distTo[v]);
				System.out.println("distTo[" + w + "] = " + distTo[w]);
				return false;
			}
		}

		return true;
	}

	public static void main(String[] args) throws IOException {
		In in = new In(args[0]);
		Graph G = new Graph(in);
		// StdOut.println(G);

		int s = Integer.parseInt(args[1]);
		double sum=0;double max = 10;
		int[] vertices = new int[2022];
		for(int i=0; i< vertices.length ; i++){
			vertices[i] = i;
		}
		int count_0=0;int count_274=0;int count_242=0;int count_266=0; int count_332=0; int count_400=0;
		for(int i=0; i< vertices.length; i++){
			BreadthFirstPaths bfs = new BreadthFirstPaths(G, vertices[i]);
			for (int v = 0; v < G.V(); v++) {
				if (bfs.hasPathTo(v)) {
					System.out.printf("%d to %d (%d):  ", vertices[i], v, bfs.distTo(v));
					for (int x : bfs.pathTo(v)) {
						if (x == s)
							System.out.print("-" +x);
						else 
							System.out.print("-" + x);
						if(x==0)
							count_0+=1;
						else if(x==274)
							count_274+=1;
						else if(x==242)
							count_242+=1;
						else if(x==266)
							count_266+=1;
						else if(x==332)
							count_332+=1;
						else if(x==400)
							count_400+=1;
					}
					System.out.println();
					sum += bfs.distTo(v);
					if(max < bfs.distTo(v))
						max = bfs.distTo(v);
				}

				else {
					System.out.printf("%d to %d (-):  not connected\n", vertices[i], v);
					sum+=max+1;
				}

			}
		}
		System.out.println(" Average distance for "+ s +" node "+ (sum/G.V()) +" count for 0"+ count_0 +" count for 274 "+count_274+ " count for 242 "+count_242 +" count for 266 "+count_266 +"  count for 332 "+count_332 +" count for 400 "+ count_400);
	}
}
