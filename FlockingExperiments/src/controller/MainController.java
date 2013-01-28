package controller;

import graph.FlockingGraph;
import graph.TraditionalGraph;

/**
 * TODO Put here a description of what this class does.
 * 
 * @author Balthazar. Created Jan 28, 2013.
 */
public class MainController {
	public static void main(String[] args) {
		int maxIterations = 100; // Max iterations

		// Constants I believe their optimal values depend on the number of cities
		int numberOfCities = 4;
		int multiplierBoidSpawn = numberOfCities / 2 + 1;
		float densityThreshold = 0.7f;

		// Constants I believe do not depend on the number of cities
		float weightOfDistance = 1f;
		float weightOfOccupancy = 1f;
		float boidVisionRange = 2f;
		float boidSpeed = 1f;

		TraditionalGraph graph = GenerateBasicInstance.GenerateBasicGraph(); // Sparse
		// TraditionalGraph graph = GenerateSparseInstance.GenerateSparseGraph(numberOfCities); // Sparse
		// TraditionalGraph graph = GenerateInstance.GenerateFullyConnectedGraph(n); // Fully connected

		long startTime = System.nanoTime();
		Problem problem = new Problem(new FlockingGraph(graph), maxIterations);
		System.out.println(problem.solve(multiplierBoidSpawn, densityThreshold, boidSpeed, boidVisionRange,
				weightOfDistance, weightOfOccupancy));
		long estimatedTime = System.nanoTime() - startTime;

		System.out.println("Execution Time: " + estimatedTime / 1000000000.0f + " seconds.");

	}
}
