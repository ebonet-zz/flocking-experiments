package controller;

import graph.TraditionalGraph;

/**
 * TODO Put here a description of what this class does.
 * 
 * @author Balthazar. Created Jan 28, 2013.
 */
public class GenerateBasicInstance {

	public static void main(String args[]) {

		TraditionalGraph world = GenerateBasicGraph();

		System.out.println(world.toString());

		for (int i = 0; i < 5; i++) {
			Integer[] temp = world.getArrayOfNeighborsOf(i);
			int j = 0;
			while (j < temp.length) {
				System.out.println(i + " " + temp[j] + " " + world.getEdgeLength(i, temp[j]));
				j++;
			}
		}
	}

	public static TraditionalGraph GenerateBasicGraph() {

		TraditionalGraph world = new TraditionalGraph(5);

		world.setEdgeLength(0, 1, 5);
		world.setEdgeLength(1, 2, 10);
		world.setEdgeLength(1, 3, 5);
		world.setEdgeLength(2, 4, 10);
		world.setEdgeLength(3, 4, 5);

		return world;
	}
}
