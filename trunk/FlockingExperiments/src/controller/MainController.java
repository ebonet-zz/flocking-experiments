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
		int maxT = 5000; // Max iterations

		// Constants I believe their optimal values depend on the number of cities
		int n = 30;
		int m = n / 2 + 1; 
		float e = 0.7f;

		// Constants I believe do not depend on the number of cities
		float B = 1; 
		float a = 1f;

		TraditionalGraph graph = GenerateSparseInstance.GenerateSparseGraph(n); // Sparse
		// TraditionalGraph graph = GenerateInstance.GenerateFullyConnectedGraph(n); // Fully connected

		long startTime = System.nanoTime();
		Problem problem = new Problem(new FlockingGraph(graph), maxT);
		System.out.println(problem.solve(m, e, B, a));
		long estimatedTime = System.nanoTime() - startTime;

		System.out.println("Execution Time: " + estimatedTime / 1000000000.0f + " seconds.");

	}
}
