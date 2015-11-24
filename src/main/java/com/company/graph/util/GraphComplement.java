package com.company.graph.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.company.graph.BreadthFirstPaths;
import com.company.graph.Graph;

public class GraphComplement {

	public static Graph getComplementGraph(Graph graph, String opath){
		try {
			File file = new File(opath);
			if (!file.exists()) {
				file.createNewFile();
			}
			int[] vertices = new int[2022];
			for(int i=0; i< vertices.length ; i++){
				vertices[i] = i;
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
			StringBuffer sb = new StringBuffer();
			/*sb.append(graph.V());
			sb.append("\n");
			sb.append(graph.E());
			sb.append("\n");*/
			int count = 0;
			for(int i=0; i< vertices.length; i++){
				if(graph.degree(vertices[i]) < 8)
					continue;
				BreadthFirstPaths bfs = new BreadthFirstPaths(graph, vertices[i]);
				for (int v = i+1; v < graph.V(); v++) {
					if(graph.degree(v) < 9)
						continue;
					if (bfs.hasPathTo(v)) {
						if(bfs.distTo(v) > 3){
							sb.append(vertices[i]+" "+v);
							sb.append("\n");
							System.out.println(" vertice 1 "+ vertices[i]+ " vertice 2 "+ v);
							count++;
						}
					}else{
						sb.append(vertices[i]+" "+v);
						sb.append("\n");
						System.out.println(" vertice 1 "+ vertices[i]+ " vertice 2 "+ v +" not connected ");
						count++;
					}
				}
			}
			if(!sb.toString().isEmpty()){
				StringBuffer nsb = new StringBuffer();
				nsb.append(graph.V());
				nsb.append("\n");
				nsb.append(count);
				nsb.append("\n");
				nsb.append(sb.toString());
				bw.write(nsb+"\n");
				bw.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static void main(String[] args) {

		try{
			In in = new In(args[0]);
			Graph G = new Graph(in);
			GraphComplement.getComplementGraph(G, args[1]);
		} catch(Exception e){
			e.printStackTrace();
		}

	}
}
