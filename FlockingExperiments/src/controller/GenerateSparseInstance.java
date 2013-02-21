package controller;

import graph.TraditionalGraph;

import java.util.Random;

/**
 * The Class GenerateSparseInstance.
 */
public class GenerateSparseInstance {

	/**
	 * The main method, for testing only.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String args[]) {

		int numberOfCities = 100;

		TraditionalGraph world = GenerateSparseGraph(numberOfCities);

		System.out.println(world.toString());

		for (int i = 0; i < numberOfCities; i++) {
			Integer[] temp = world.getArrayOfNeighborsOf(i);
			int j = 0;
			while (j < temp.length) {
				System.out.println(i + " " + temp[j] + " " + world.getEdgeLength(i, temp[j]));
				j++;
			}
		}
	}

	/**
	 * Generate sparse graph.
	 * 
	 * @param numberOfCities
	 *            the number of cities
	 * @return the traditional graph
	 */
	public static TraditionalGraph GenerateSparseGraph(int numberOfCities) {
		TraditionalGraph world = new TraditionalGraph(numberOfCities);

		Random r = new Random(0);
		int distance;
		int dice;

		// To generate large, sparse random graphs we use the Erdos-Renyi model of random graphs.

		for (int i = 0; i < numberOfCities; i++) {
			for (int j = i + 1; j < numberOfCities; j++) {
				distance = r.nextInt(100) + 1;
				dice = r.nextInt(numberOfCities);
				if (dice <= (numberOfCities / 10)) {
					world.setEdgeLength(i, j, distance);
				}

			}
			if (world.getArrayOfNeighborsOf(i).length == 0) {
				distance = r.nextInt(100) + 1;
				dice = r.nextInt(numberOfCities);
				world.setEdgeLength(i, dice, distance);
			}
			while (world.getArrayOfNeighborsOf(i).length == 1) {
				dice = r.nextInt(numberOfCities);
				if (dice != world.getArrayOfNeighborsOf(i)[0]) {
					distance = r.nextInt(100) + 1;
					world.setEdgeLength(i, dice, distance);
				}
			}
		}

		return world;
	}
}
