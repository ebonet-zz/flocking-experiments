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
	static int maxIterations = 5000; // Max iterations
	static boolean displaySteps = true; // show boids' movement on each iteration

	// Constants I believe their optimal values depend on the number of cities
	static int numberOfCities = 8;
	static int maxAgents = 3 * numberOfCities * numberOfCities * 10000;
	static float multiplierBoidSpawn = 1f;
	static float densityThreshold = 0.7f;

	// Constants I believe do not depend on the number of cities
	static float weightOfDistance = 3f;
	static float weightOfOccupancy = 1f;
	static float boidSpeed = 2f;
	static float boidVisionRange = boidSpeed * 2;

	static int segmentCapacity = 1;
	static float segmentLength = 1f;

	// static GoalEvaluator goal = new EndNodeGoalEvaluator(3);
	static GoalEvaluator goal = new TSPGoalEvaluator();

//	static TraditionalGraph graph = GenerateBasicInstance.GenerateBasicGraph(); // Sparse
//	 static TraditionalGraph graph = GenerateSparseInstance.GenerateSparseGraph(numberOfCities); // Sparse
	 static TraditionalGraph graph = GenerateInstance.GenerateFullyConnectedGraph(numberOfCities); // Fully connected

//	static Problem problem = new Problem(new FlockingGraph(graph, segmentLength, segmentCapacity), maxIterations);

	 static Problem problem = new WollowskiProblem(new FlockingGraph(graph, segmentLength, segmentCapacity),
	 maxIterations);

	public static void main(String[] args) {
		long startTime = System.nanoTime();

		problem.graphics = displaySteps;
		System.out.println(problem.solve(multiplierBoidSpawn, maxAgents, densityThreshold, weightOfDistance,
				weightOfOccupancy, boidVisionRange, boidSpeed, goal));

		// testWD(problem);
		// testWO(problem);
		// testBoidSpeed(problem);
		// testMaxAgents(problem);
		// testBoidVision(problem);
		// testSegmentCapacity();
		// testSegmentLength();

		long estimatedTime = System.nanoTime() - startTime;
		System.out.println("Execution Time: " + estimatedTime / 1000000000.0f + " seconds.");
	}

	private static void testWD(Problem problem) {
		for (float wD = 2.0f; wD <= 5f; wD += 0.5f) {
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
			System.out.println();
		}
	}

	private static void testWO(Problem problem) {
		for (float wO = 0.0f; wO <= 5f; wO += 0.5f) {
			float average = 0f;
			float divider = 100f;
			for (int i = 0; i < 100; i++) {
				Tour solution = problem.solve(multiplierBoidSpawn, maxAgents, densityThreshold, weightOfDistance, wO,
						boidVisionRange, boidSpeed, goal);
				if (solution != null) {
					average += solution.lastCalculatedCost;
				} else {
					divider--;
				}
			}

			average /= divider;
			System.out.println("For wO = " + wO + " " + divider + "% of the problems had a solution");
			System.out.println("For wO = " + wO + " the average path was " + average);
			System.out.println();
		}
	}

	private static void testBoidSpeed(Problem problem) {
		for (float speed = 1.0f; speed <= 10f; speed += 1f) {
			float average = 0f;
			float divider = 100f;
			for (int i = 0; i < 100; i++) {
				Tour solution = problem.solve(multiplierBoidSpawn, maxAgents, densityThreshold, weightOfDistance,
						weightOfOccupancy, boidVisionRange, speed, goal);
				if (solution != null) {
					average += solution.lastCalculatedCost;
				} else {
					divider--;
				}
			}

			average /= divider;
			System.out.println("For speed = " + speed + " " + divider + "% of the problems had a solution");
			System.out.println("For speed = " + speed + " the average path was " + average);
			System.out.println();
		}
	}

	private static void testBoidVision(Problem problem) {
		for (float visionMultiplier = 1.0f; visionMultiplier <= 10f; visionMultiplier += 1f) {
			float average = 0f;
			float divider = 100f;
			for (int i = 0; i < 100; i++) {
				Tour solution = problem.solve(multiplierBoidSpawn, maxAgents, densityThreshold, weightOfDistance,
						weightOfOccupancy, visionMultiplier * boidSpeed, boidSpeed, goal);
				if (solution != null) {
					average += solution.lastCalculatedCost;
				} else {
					divider--;
				}
			}

			average /= divider;
			System.out.println("For visionMultiplier = " + visionMultiplier + " " + divider
					+ "% of the problems had a solution");
			System.out.println("For visionMultiplier = " + visionMultiplier + " the average path was " + average);
			System.out.println();
		}
	}

	private static void testMaxAgents(Problem problem) {
		for (int maxAgentsMultiplier = 1; maxAgentsMultiplier <= 100; maxAgentsMultiplier += 10) {
			float average = 0f;
			float divider = 100f;
			for (int i = 0; i < 100; i++) {
				Tour solution = problem.solve(multiplierBoidSpawn, numberOfCities * maxAgentsMultiplier,
						densityThreshold, weightOfDistance, weightOfOccupancy, boidVisionRange, boidSpeed, goal);
				if (solution != null) {
					average += solution.lastCalculatedCost;
				} else {
					divider--;
				}
			}

			average /= divider;
			System.out.println("For maxAgentsMultiplier = " + maxAgentsMultiplier + " " + divider
					+ "% of the problems had a solution");
			System.out.println("For maxAgentsMultiplier = " + maxAgentsMultiplier + " the average path was " + average);
			System.out.println();
		}
	}

	private static void testSegmentCapacity() {
		for (int capacity = 1; capacity <= 10; capacity += 1) {
			float average = 0f;
			float divider = 100f;
			for (int i = 0; i < 100; i++) {
				Problem problem = new Problem(new FlockingGraph(graph, segmentLength, capacity), maxIterations);
				Tour solution = problem.solve(multiplierBoidSpawn, maxAgents, densityThreshold, weightOfDistance,
						weightOfOccupancy, boidVisionRange, boidSpeed, goal);
				if (solution != null) {
					average += solution.lastCalculatedCost;
				} else {
					divider--;
				}
			}

			average /= divider;
			System.out.println("For capacity = " + capacity + " " + divider + "% of the problems had a solution");
			System.out.println("For capacity = " + capacity + " the average path was " + average);
			System.out.println();
		}
	}

	private static void testSegmentLength() {
		for (float segmentLength = 1; segmentLength <= 10; segmentLength += 1) {
			float average = 0f;
			float divider = 100f;
			for (int i = 0; i < 100; i++) {
				Problem problem = new Problem(new FlockingGraph(graph, segmentLength, segmentCapacity), maxIterations);
				Tour solution = problem.solve(multiplierBoidSpawn, maxAgents, densityThreshold, weightOfDistance,
						weightOfOccupancy, boidVisionRange, boidSpeed, goal);
				if (solution != null) {
					average += solution.lastCalculatedCost;
				} else {
					divider--;
				}
			}

			average /= divider;
			System.out.println("For segmentLength = " + segmentLength + " " + divider
					+ "% of the problems had a solution");
			System.out.println("For segmentLength = " + segmentLength + " the average path was " + average);
			System.out.println();
		}
	}
}
