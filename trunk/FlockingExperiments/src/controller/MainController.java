package controller;

import graph.FlockingGraph;
import graph.Tour;
import graph.TraditionalGraph;
import agent.GoalEvaluator;
import agent.TSPGoalEvaluator;

/**
 * Main program controller and entry point. Starts everything.
 * 
 * @author Balthazar. Created Jan 28, 2013.
 */
public class MainController {
	static int maxIterations = 1000; // Max iterations
	static boolean displaySteps = false; // show boids' movement on each iteration

	// Constants I believe their optimal values depend on the number of cities
	static int numberOfCities = 8;
	static int maxAgents = numberOfCities * 50;
	static float multiplierBoidSpawn = 1f;
	static float densityThreshold = 0.7f;

	// Constants I believe do not depend on the number of cities
	static float weightOfDistance = 1f;
	static float weightOfOccupancy = 1f;
	static float boidSpeed = 2f;
	static float boidVisionRange = boidSpeed * 2;

	// GoalEvaluator goal = new EndNodeGoalEvaluator(3);
	static GoalEvaluator goal = new TSPGoalEvaluator();

	// TraditionalGraph graph = GenerateBasicInstance.GenerateBasicGraph(); // Sparse
	// TraditionalGraph graph = GenerateSparseInstance.GenerateSparseGraph(numberOfCities); // Sparse
	static TraditionalGraph graph = GenerateInstance.GenerateFullyConnectedGraph(numberOfCities); // Fully connected

	static Problem problem = new TSPProblem(new FlockingGraph(graph), maxIterations);

	public static void main(String[] args) {
		long startTime = System.nanoTime();

//		problem.graphics = displaySteps;
//		System.out.println(problem.solve(multiplierBoidSpawn, maxAgents, densityThreshold, weightOfDistance,
//				weightOfOccupancy, boidVisionRange, boidSpeed, goal));

		 testWD(problem);

		long estimatedTime = System.nanoTime() - startTime;
		System.out.println("Execution Time: " + estimatedTime / 1000000000.0f + " seconds.");
	}

	private static void testWD(Problem problem) {
		// Parameter test: weight of Distance
		for (float wD = 0.0f; wD <= 2f; wD += 0.1f) {
			float average = 0f;
			float divider = 100f;
			for (int i = 0; i < 100; i++) {
				Tour solution = problem.solve(multiplierBoidSpawn, maxAgents, densityThreshold, wD, weightOfOccupancy,
						boidVisionRange, boidSpeed, goal);
				if (solution != null) {
					average += solution.lastCalculatedCost;
				} else {
					divider--;
				}
			}

			average /= divider;
			System.out.println("For wD = " + wD + " " + divider + "% of the problems had a solution");
			System.out.println("For wD = " + wD + " the average path was " + average);
		}
	}
}
