package controller;

import graph.FlockingGraph;
import graph.TraditionalGraph;
import agent.EndNodeGoalEvaluator;
import agent.GoalEvaluator;
import agent.TSPGoalEvaluator;

/**
 * Main program controller and entry point. Starts everything.
 * 
 * @author Balthazar. Created Jan 28, 2013.
 */
public class MainController {
	public static void main(String[] args) {
		int maxIterations = 5000; // Max iterations
		boolean displaySteps = true; // show boids' movement on each iteration
		
		// Constants I believe their optimal values depend on the number of cities
		int numberOfCities = 8;
		// GoalEvaluator goal = new EndNodeGoalEvaluator(3);
		GoalEvaluator goal = new TSPGoalEvaluator();
		
		int maxAgents = numberOfCities * 50;
		float multiplierBoidSpawn = 1f;
		float densityThreshold = 0.7f;

		// Constants I believe do not depend on the number of cities
		float weightOfDistance = 1f;
		float weightOfOccupancy = 1f;
		float boidSpeed = 2f;
		float boidVisionRange = boidSpeed * 2;

		// TraditionalGraph graph = GenerateBasicInstance.GenerateBasicGraph(); // Sparse
		// TraditionalGraph graph = GenerateSparseInstance.GenerateSparseGraph(numberOfCities); // Sparse
		TraditionalGraph graph = GenerateInstance.GenerateFullyConnectedGraph(numberOfCities); // Fully connected

		long startTime = System.nanoTime();

		Problem problem = new TSPProblem(new FlockingGraph(graph), maxIterations);
		problem.graphics = displaySteps;
		System.out.println(problem.solve(multiplierBoidSpawn, maxAgents, densityThreshold, weightOfDistance, weightOfOccupancy,
				boidVisionRange, boidSpeed, goal));

		long estimatedTime = System.nanoTime() - startTime;

		System.out.println("Execution Time: " + estimatedTime / 1000000000.0f + " seconds.");

	}
}
