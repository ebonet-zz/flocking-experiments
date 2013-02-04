package controller;

import graph.FlockingGraph;
import graph.TraditionalGraph;
import agent.EndNodeGoalEvaluator;
import agent.GoalEvaluator;

/**
 * TODO Put here a description of what this class does.
 * 
 * @author Balthazar. Created Jan 28, 2013.
 */
public class MainController {
	public static void main(String[] args) {
		int maxIterations = 100; // Max iterations

		// Constants I believe their optimal values depend on the number of cities
		int numberOfCities = 5;
		int multiplierBoidSpawn = numberOfCities / 2 + 1;
		float densityThreshold = 0.7f;

		// Constants I believe do not depend on the number of cities
		float weightOfDistance = 1f;
		float weightOfOccupancy = 1f;
		float boidVisionRange = 2f;
		float boidSpeed = 0.001f;

		TraditionalGraph graph = GenerateBasicInstance.GenerateBasicGraph(); // Sparse
		// TraditionalGraph graph = GenerateSparseInstance.GenerateSparseGraph(numberOfCities); // Sparse
		// TraditionalGraph graph = GenerateInstance.GenerateFullyConnectedGraph(n); // Fully connected

		GoalEvaluator goal = new EndNodeGoalEvaluator(4);

		long startTime = System.nanoTime();
		Problem problem = new Problem(new FlockingGraph(graph), maxIterations);
		System.out.println(problem.solve(multiplierBoidSpawn, densityThreshold, boidSpeed, boidVisionRange,
				weightOfDistance, weightOfOccupancy, goal));
		long estimatedTime = System.nanoTime() - startTime;

		System.out.println("Execution Time: " + estimatedTime / 1000000000.0f + " seconds.");

	}
}
