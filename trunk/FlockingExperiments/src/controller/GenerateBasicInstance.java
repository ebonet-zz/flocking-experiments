package controller;

import graph.TraditionalGraph;

/**
 * Generates the most basic shortest path problem graph.
 * 
 * @author Balthazar. Created Jan 28, 2013.
 */
public class GenerateBasicInstance {

	/**
	 * The main method, for testing only.
	 * 
	 * @param args
	 *            the arguments
	 */
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

	/**
	 * Generate basic graph.
	 * 
	 * @return the traditional graph
	 */
	public static TraditionalGraph GenerateBasicGraph() {

		TraditionalGraph world = new TraditionalGraph(5);

		world.setEdgeLength(0, 1, 10);
		world.setEdgeLength(1, 4, 20);
		world.setEdgeLength(1, 2, 12);
		world.setEdgeLength(2, 3, 15);
		world.setEdgeLength(4, 3, 9);

		return world;
	}
}
