package controller;

import graph.TraditionalGraph;

import java.util.Random;

public class GenerateInstance {

	public static void main(String args[]) {

		int numberOfCities = 300;

		TraditionalGraph world = GenerateFullyConnectedGraph(numberOfCities);

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

	public static TraditionalGraph GenerateFullyConnectedGraph(int numberOfCities) {

		// Ok, I am fixing the seed, so that instead of reading in the
		// data from the text files, you can use this function to
		// generate the data I posted.
		Random r = new Random(0);

		TraditionalGraph world = new TraditionalGraph(numberOfCities);

		int distance;

		for (int i = 0; i < numberOfCities; i++) {
			for (int j = i + 1; j < numberOfCities; j++) {
				distance = (r.nextInt(100) + 1);
				world.setEdgeLength(i, j, distance);
			}
		}

		return world;
	}
}