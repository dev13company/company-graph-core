package com.company.graph.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class GraphColorAlgo {


	public static void nodeColoring(int n, int m, int[] nodei,
			int[] nodej, int[] color){
		int i, j, k, loop, currentnode, newc, ncolor,
		paint, index, nodek, up, low;
		int degree[] = new int[n+1];
		int choices[] = new int[n+1];
		int maxcolornum[] = new int[n+1];
		int currentcolor[] = new int[n+1];
		int feasiblecolor[] = new int[n+1];
		int firstedges[] = new int[n+2];
		int endnode[] = new int[m+m+1];
		int availc[][] = new int[n+1][n+1];
		boolean more;

		for(i=0; i<=n; i++)
			degree[i] = 0;

		k=0;
		for(i=1;i<=n;i++){
			firstedges[i] = k+1;
			for(j=1;j<=m;j++)
				if(nodei[j]==i){
					k++;
					endnode[k] = nodej[j];
					degree[i]++;
				}
				else{
					if(nodej[j]==i){
						k++;
						endnode[k] = nodei[j];
						degree[i]++;
					}
				}
		}

		firstedges[n+1] = k+1;
		for(i=1; i<=n ; i++){
			feasiblecolor[i] = degree[i] + 1;
			if(feasiblecolor[i] > i) feasiblecolor[i] = i;
			choices[i] = feasiblecolor[i];
			loop = feasiblecolor[i];
			for(j=1; j<=loop; j++)
				availc[i][j] = n;
			k = feasiblecolor[i] + 1;
			if(k <= n)
				for(j=k; j<=n; j++)
					availc[i][j] = 0;
		}
		currentnode = 1;
		newc = 1;
		ncolor = n;
		paint = 0;
		more = true;
		do{
			if(more){
				index = choices[currentnode];
				if(index > paint +1) index = paint + 1;
				while((availc[currentnode][newc] < currentnode) && (newc <= index))
					newc++;
				if(newc == index +1)
					more = false;
				else{
					if(currentnode == n){
						currentcolor[currentnode] = newc;
						for(i=1;i<=n; i++)
							color[i] = currentcolor[i];
						if(newc > paint) paint++;
						ncolor = paint;
						if(ncolor > 2){
							index = 1;
							while(color[index] != ncolor)
								index++;
							j=n;
							while (j >= index) {
								currentnode--;
								newc = currentcolor[currentnode];
								paint = maxcolornum[currentnode];
								low = firstedges[currentnode];
								up = firstedges[currentnode + 1];
								if(up > low){
									up--;
									for(k=low; k<=up; k++){
										nodek = endnode[k];
										if(nodek > currentnode)
											if(availc[nodek][newc] == currentnode){
												availc[nodek][newc] = n;
												feasiblecolor[nodek]++;
											}
									}
								}
								newc++;
								more = false;
								j--;
							}
							paint = ncolor -1;
							for(i=1; i<=n; i++){
								loop = choices[i];
								if(loop > paint){
									k = paint+1;
									for(j=k; j<=loop; j++)
										if(availc[i][j] == n) feasiblecolor[i]--;
									choices[i] = paint;
								}
							}
						}
					}
					else{
						low = firstedges[currentnode];
						up = firstedges[currentnode + 1];
						if(up > low){
							up--;
							k = low;
							while((k <= up) && more){
								nodek = endnode[k];
								if(nodek > currentnode)
									more = !((feasiblecolor[nodek]==1) && (availc[nodek][newc] >= currentnode));
								k++;
							}
						}
						if(more){
							currentcolor[currentnode] = newc;
							maxcolornum[currentnode] = paint;
							if(newc > paint) paint++;
							low = firstedges[currentnode];
							up = firstedges[currentnode+1];
							if (up > low){
								up--;
								for(k=low; k <=up; k++){
									nodek = endnode[k];
									if(nodek > currentnode)
										if(availc[nodek][newc] >= currentnode){
											availc[nodek][newc] = currentnode;
											feasiblecolor[nodek]--;
										}
								}
							}
							currentnode++;
							newc = 1;
						}
						else
							newc++;
					}
				}
			}
			else{
				more = true;
				if((newc > choices[currentnode]) || (newc > paint + 1)){
					currentnode--;
					newc = currentcolor[currentnode];
					paint = maxcolornum[currentnode];
					low = firstedges[currentnode];
					up = firstedges[currentnode +1];
					if( up > low) {
						up--;
						for(k=low; k<=up; k++){
							nodek = endnode[k];
							if(nodek > currentnode)
								if(availc[nodek][newc] == currentnode){
									availc[nodek][newc] = n;
									feasiblecolor[nodek]++;
								}
						}
					}
					newc++;
					more = false;
				}
			}
		}while((currentnode != 1) && (ncolor != 2));
		color[0] = ncolor;
	}

	public static void main(String[] args) {

		try {
			BufferedReader br = new BufferedReader(new FileReader(args[0]));
			int n = 0, m = 0 ;
			String str ;
			int lines=0;
			List<Integer> nodeilist = new ArrayList<Integer>();
			List<Integer> nodejlist = new ArrayList<Integer>();
			
			while((str = br.readLine())!=null){
				String[] words = StringUtils.split(str);
				if(str.isEmpty())
					continue;
				if(lines == 0){
					n = Integer.parseInt(words[0]);
				}else if(lines == 1){
					m = Integer.parseInt(words[0]);
					nodeilist.add(0);
					nodejlist.add(0);
				}else{
					nodeilist.add(Integer.parseInt(words[0])+1);
					nodejlist.add(Integer.parseInt(words[1])+1);
				}
				lines++;	
			}
			int color[] = new int[n+1];
			int nodei[] = ArrayUtils.toPrimitive(nodeilist.toArray(new Integer[nodeilist.size()]));
			int nodej[] = ArrayUtils.toPrimitive(nodejlist.toArray(new Integer[nodejlist.size()]));

			GraphColorAlgo.nodeColoring(n, m, nodei, nodej, color);

			System.out.println(" Chromatic number "+ color[0] + "\n \n Node :");
			for(int i=1; i<=n; i++)
				System.out.printf("%3d ",i);
			System.out.print(" \n Color: ");
			for(int i=1; i <=n; i++)
				System.out.printf("%3d",color[i]);
			System.out.println();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
