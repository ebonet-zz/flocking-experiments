package controller;

import java.io.IOException;

import goal.EndNodeGoalEvaluator;
import goal.GoalEvaluator;
import goal.TSPGoalEvaluator;
import graph.FlockingGraph;
import graph.Tour;
import graph.TraditionalGraph;
import problem.Problem;
import problem.WollowskiProblem;

/**
 * Main program controller and entry point. Starts everything.
 * 
 * @author Balthazar. Created Jan 28, 2013.
 */
public class MainController {

	/**
	 * Solve Shortest Path with Wollowski boids in the basic graph.
	 */
	public static void solveWollowskiSP() {
		int maxIterations = 5000; // Max iterations
		boolean displaySteps = true; // show boids' movement on each iteration

		// Constants I believe their optimal values depend on the number of cities
		int maxAgents = Integer.MAX_VALUE;
		float multiplierBoidSpawn = 1f;
		float densityThreshold = 0.7f;

		// Constants I believe do not depend on the number of cities
		float numberOfChoicesWeight = 1f;
		float visibleBoidsInPathWeight = 0.1f;
		float boidSpeed = 2 * multiplierBoidSpawn;
		float boidVisionRange = boidSpeed * 3;

		int segmentCapacity = 1;
		float segmentLength = 1f;

		GoalEvaluator goal = new EndNodeGoalEvaluator(3);

		TraditionalGraph graph = GenerateBasicInstance.GenerateBasicGraph(); // Sparse

		Problem problem = new WollowskiProblem(new FlockingGraph(graph, segmentLength, segmentCapacity), maxIterations);

		System.out.println(problem.solve(multiplierBoidSpawn, maxAgents, densityThreshold, numberOfChoicesWeight,
				visibleBoidsInPathWeight, boidVisionRange, boidSpeed, goal, displaySteps));
	}

	/**
	 * Solve TSP with Wollowski boids in the basic graph.
	 */
	public static void solveWollowskiTSPBasicGraph() {
		int maxIterations = 5000; // Max iterations
		boolean displaySteps = true; // show boids' movement on each iteration

		// Constants I believe their optimal values depend on the number of cities
		int maxAgents = Integer.MAX_VALUE;
		float multiplierBoidSpawn = 1f;
		float densityThreshold = 0.7f;

		// Constants I believe do not depend on the number of cities
		float numberOfChoicesWeight = 1f;
		float visibleBoidsInPathWeight = 0.1f;
		float boidSpeed = 2 * multiplierBoidSpawn;
		float boidVisionRange = boidSpeed * 4;

		int segmentCapacity = 1;
		float segmentLength = 1f;

		GoalEvaluator goal = new TSPGoalEvaluator();

		TraditionalGraph graph = GenerateBasicInstance.GenerateBasicGraph(); // Sparse

		Problem problem = new WollowskiProblem(new FlockingGraph(graph, segmentLength, segmentCapacity), maxIterations);

		System.out.println(problem.solve(multiplierBoidSpawn, maxAgents, densityThreshold, numberOfChoicesWeight,
				visibleBoidsInPathWeight, boidVisionRange, boidSpeed, goal, displaySteps));
	}

	/**
	 * Solve TSP with Wollowski boids in the 4 cities fully connected graph.
	 */
	public static void solveWollowskiTSP4CitiesFullConnectedGraph() {
		int maxIterations = 5000; // Max iterations
		boolean displaySteps = true; // show boids' movement on each iteration

		// Constants I believe their optimal values depend on the number of cities
		int numberOfCities = 4;
		int maxAgents = Integer.MAX_VALUE;
		float multiplierBoidSpawn = 1f;
		float densityThreshold = 0.7f;

		// Constants I believe do not depend on the number of cities
		float numberOfChoicesWeight = 1f;
		float visibleBoidsInPathWeight = 0.1f;
		float boidSpeed = 2 * multiplierBoidSpawn;
		float boidVisionRange = boidSpeed * 3;

		int segmentCapacity = 1;
		float segmentLength = 1f;

		GoalEvaluator goal = new TSPGoalEvaluator();

		TraditionalGraph graph = GenerateFullyConnectedInstance.GenerateFullyConnectedGraph(numberOfCities); // Fully
																												// connected

		Problem problem = new WollowskiProblem(new FlockingGraph(graph, segmentLength, segmentCapacity), maxIterations);

		System.out.println(problem.solve(multiplierBoidSpawn, maxAgents, densityThreshold, numberOfChoicesWeight,
				visibleBoidsInPathWeight, boidVisionRange, boidSpeed, goal, displaySteps));
	}

	/**
	 * Solve TSP with Wollowski boids in the 4 cities sparse graph.
	 */
	public static void solveWollowskiTSP4CitiesSparseGraph() {
		int maxIterations = 5000; // Max iterations
		boolean displaySteps = true; // show boids' movement on each iteration

		// Constants I believe their optimal values depend on the number of cities
		int numberOfCities = 4;
		int maxAgents = Integer.MAX_VALUE;
		float multiplierBoidSpawn = 1f;
		float densityThreshold = 0.7f;

		// Constants I believe do not depend on the number of cities
		float numberOfChoicesWeight = 1f;
		float visibleBoidsInPathWeight = 0.1f;
		float boidSpeed = 2 * multiplierBoidSpawn;
		float boidVisionRange = boidSpeed * 3;

		int segmentCapacity = 1;
		float segmentLength = 1f;

		GoalEvaluator goal = new TSPGoalEvaluator();

		TraditionalGraph graph = GenerateSparseInstance.GenerateSparseGraph(numberOfCities);

		Problem problem = new WollowskiProblem(new FlockingGraph(graph, segmentLength, segmentCapacity), maxIterations);

		System.out.println(problem.solve(multiplierBoidSpawn, maxAgents, densityThreshold, numberOfChoicesWeight,
				visibleBoidsInPathWeight, boidVisionRange, boidSpeed, goal, displaySteps));
	}

	/**
	 * Solve Shortest Path with Optimized boids in the basic graph.
	 */
	public static void solveOptimizedSP() {
		int maxIterations = 5000; // Max iterations
		boolean displaySteps = true; // show boids' movement on each iteration

		// Constants I believe their optimal values depend on the number of cities
		int numberOfCities = 5;
		int maxAgents = 3 * numberOfCities * numberOfCities;
		float multiplierBoidSpawn = 1f;
		float densityThreshold = 0.7f;

		// Constants I believe do not depend on the number of cities
		float weightOfDistance = 5f;
		float weightOfOccupancy = 1f;
		float boidSpeed = 2 * multiplierBoidSpawn;
		float boidVisionRange = boidSpeed * 3;

		int segmentCapacity = 3;
		float segmentLength = 1f;

		GoalEvaluator goal = new EndNodeGoalEvaluator(3);

		TraditionalGraph graph = GenerateBasicInstance.GenerateBasicGraph(); // Sparse

		Problem problem = new Problem(new FlockingGraph(graph, segmentLength, segmentCapacity), maxIterations);

		System.out.println(problem.solve(multiplierBoidSpawn, maxAgents, densityThreshold, weightOfDistance,
				weightOfOccupancy, boidVisionRange, boidSpeed, goal, displaySteps));
	}

	/**
	 * Solve TSP with Optimized boids in the basic graph.
	 */
	public static void solveOptimizedTSPBasicGraph() {
		int maxIterations = 5000; // Max iterations
		boolean displaySteps = true; // show boids' movement on each iteration

		// Constants I believe their optimal values depend on the number of cities
		int numberOfCities = 5;
		int maxAgents = 3 * numberOfCities * numberOfCities;
		float multiplierBoidSpawn = 1f;
		float densityThreshold = 0.7f;

		// Constants I believe do not depend on the number of cities
		float weightOfDistance = 5f;
		float weightOfOccupancy = 1f;
		float boidSpeed = 2 * multiplierBoidSpawn;
		float boidVisionRange = boidSpeed * 3;

		int segmentCapacity = 3;
		float segmentLength = 1f;

		GoalEvaluator goal = new TSPGoalEvaluator();

		TraditionalGraph graph = GenerateBasicInstance.GenerateBasicGraph(); // Sparse

		Problem problem = new Problem(new FlockingGraph(graph, segmentLength, segmentCapacity), maxIterations);

		System.out.println(problem.solve(multiplierBoidSpawn, maxAgents, densityThreshold, weightOfDistance,
				weightOfOccupancy, boidVisionRange, boidSpeed, goal, displaySteps));
	}

	/**
	 * Solve TSP with Optimized boids in the 4 cities fully connected graph.
	 */
	public static void solveOptimizedTSP4CitiesFullConnectedGraph() {
		int maxIterations = 5000; // Max iterations
		boolean displaySteps = true; // show boids' movement on each iteration

		// Constants I believe their optimal values depend on the number of cities
		int numberOfCities = 4;
		int maxAgents = 3 * numberOfCities * numberOfCities;
		float multiplierBoidSpawn = 1f;
		float densityThreshold = 0.7f;

		// Constants I believe do not depend on the number of cities
		float weightOfDistance = 5f;
		float weightOfOccupancy = 1f;
		float boidSpeed = 2 * multiplierBoidSpawn;
		float boidVisionRange = boidSpeed * 3;

		int segmentCapacity = 3;
		float segmentLength = 1f;

		// GoalEvaluator goal = new EndNodeGoalEvaluator(3);
		GoalEvaluator goal = new TSPGoalEvaluator();

		TraditionalGraph graph = GenerateFullyConnectedInstance.GenerateFullyConnectedGraph(numberOfCities); // Fully
																												// connected

		Problem problem = new Problem(new FlockingGraph(graph, segmentLength, segmentCapacity), maxIterations);

		System.out.println(problem.solve(multiplierBoidSpawn, maxAgents, densityThreshold, weightOfDistance,
				weightOfOccupancy, boidVisionRange, boidSpeed, goal, displaySteps));
	}

	/**
	 * Solve TSP with Optimized boids in the 4 cities sparse graph.
	 */
	public static void solveOptimizedTSP4CitiesSparseGraph() {
		int maxIterations = 5000; // Max iterations
		boolean displaySteps = true; // show boids' movement on each iteration

		// Constants I believe their optimal values depend on the number of cities
		int numberOfCities = 4;
		int maxAgents = 3 * numberOfCities * numberOfCities;
		float multiplierBoidSpawn = 1f;
		float densityThreshold = 0.7f;

		// Constants I believe do not depend on the number of cities
		float weightOfDistance = 5f;
		float weightOfOccupancy = 1f;
		float boidSpeed = 2 * multiplierBoidSpawn;
		float boidVisionRange = boidSpeed * 3;

		int segmentCapacity = 3;
		float segmentLength = 1f;

		GoalEvaluator goal = new TSPGoalEvaluator();

		TraditionalGraph graph = GenerateSparseInstance.GenerateSparseGraph(numberOfCities); // Sparse

		Problem problem = new Problem(new FlockingGraph(graph, segmentLength, segmentCapacity), maxIterations);

		System.out.println(problem.solve(multiplierBoidSpawn, maxAgents, densityThreshold, weightOfDistance,
				weightOfOccupancy, boidVisionRange, boidSpeed, goal, displaySteps));
	}

	/**
	 * Solve TSP with Optimized boids in the 8 cities fully connected graph.
	 */
	public static void solveOptimizedTSP8CitiesFullConnectedGraph() {
		int maxIterations = 5000; // Max iterations
		boolean displaySteps = true; // show boids' movement on each iteration

		// Constants I believe their optimal values depend on the number of cities
		int numberOfCities = 8;
		int maxAgents = 3 * numberOfCities * numberOfCities;
		float multiplierBoidSpawn = 1f;
		float densityThreshold = 0.7f;

		// Constants I believe do not depend on the number of cities
		float weightOfDistance = 5f;
		float weightOfOccupancy = 1f;
		float boidSpeed = 2 * multiplierBoidSpawn;
		float boidVisionRange = boidSpeed * 3;

		int segmentCapacity = 3;
		float segmentLength = 1f;

		GoalEvaluator goal = new TSPGoalEvaluator();

		TraditionalGraph graph = GenerateFullyConnectedInstance.GenerateFullyConnectedGraph(numberOfCities); // Fully
																												// connected

		Problem problem = new Problem(new FlockingGraph(graph, segmentLength, segmentCapacity), maxIterations);

		System.out.println(problem.solve(multiplierBoidSpawn, maxAgents, densityThreshold, weightOfDistance,
				weightOfOccupancy, boidVisionRange, boidSpeed, goal, displaySteps));
	}

	/**
	 * Solve TSP with Optimized boids in the 8 cities sparse graph.
	 */
	public static void solveOptimizedTSP8CitiesSparseGraph() {
		int maxIterations = 5000; // Max iterations
		boolean displaySteps = true; // show boids' movement on each iteration

		// Constants I believe their optimal values depend on the number of cities
		int numberOfCities = 8;
		int maxAgents = 3 * numberOfCities * numberOfCities;
		float multiplierBoidSpawn = 1f;
		float densityThreshold = 0.7f;

		// Constants I believe do not depend on the number of cities
		float weightOfDistance = 5f;
		float weightOfOccupancy = 1f;
		float boidSpeed = 2 * multiplierBoidSpawn;
		float boidVisionRange = boidSpeed * 3;

		int segmentCapacity = 3;
		float segmentLength = 1f;

		GoalEvaluator goal = new TSPGoalEvaluator();

		TraditionalGraph graph = GenerateSparseInstance.GenerateSparseGraph(numberOfCities); // Sparse

		Problem problem = new Problem(new FlockingGraph(graph, segmentLength, segmentCapacity), maxIterations);

		System.out.println(problem.solve(multiplierBoidSpawn, maxAgents, densityThreshold, weightOfDistance,
				weightOfOccupancy, boidVisionRange, boidSpeed, goal, displaySteps));
	}

	/**
	 * Solve TSP with Optimized boids in the 30 cities fully connected graph.
	 */
	public static void solveOptimizedTSP30CitiesFullConnectedGraph() {
		int maxIterations = 5000; // Max iterations
		boolean displaySteps = true; // show boids' movement on each iteration

		// Constants I believe their optimal values depend on the number of cities
		int numberOfCities = 30;
		int maxAgents = 3 * numberOfCities * numberOfCities;
		float multiplierBoidSpawn = 1f;
		float densityThreshold = 0.7f;

		// Constants I believe do not depend on the number of cities
		float weightOfDistance = 5f;
		float weightOfOccupancy = 1f;
		float boidSpeed = 2 * multiplierBoidSpawn;
		float boidVisionRange = boidSpeed * 3;

		int segmentCapacity = 3;
		float segmentLength = 1f;

		GoalEvaluator goal = new TSPGoalEvaluator();

		TraditionalGraph graph = GenerateFullyConnectedInstance.GenerateFullyConnectedGraph(numberOfCities); // Fully
																												// connected

		Problem problem = new Problem(new FlockingGraph(graph, segmentLength, segmentCapacity), maxIterations);

		System.out.println(problem.solve(multiplierBoidSpawn, maxAgents, densityThreshold, weightOfDistance,
				weightOfOccupancy, boidVisionRange, boidSpeed, goal, displaySteps));
	}

	/**
	 * Solve TSP with Optimized boids in the Eil51 graph.
	 */
	public static void solveOptimizedTSPEil51() {
		int maxIterations = 5000; // Max iterations
		boolean displaySteps = true; // show boids' movement on each iteration

		// Constants I believe their optimal values depend on the number of cities
		int numberOfCities = 51;
		int maxAgents = 3 * numberOfCities * numberOfCities;
		float multiplierBoidSpawn = 1f;
		float densityThreshold = 0.7f;

		// Constants I believe do not depend on the number of cities
		float weightOfDistance = 5f;
		float weightOfOccupancy = 1f;
		float boidSpeed = 2 * multiplierBoidSpawn;
		float boidVisionRange = boidSpeed * 3;

		int segmentCapacity = 3;
		float segmentLength = 1f;

		GoalEvaluator goal = new TSPGoalEvaluator();

		TraditionalGraph graph;
		try {
			graph = GraphReaderTSPLIB.generateGraphFromFile(GraphReaderTSPLIB.EIL_51);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		Problem problem = new Problem(new FlockingGraph(graph, segmentLength, segmentCapacity), maxIterations);

		System.out.println(problem.solve(multiplierBoidSpawn, maxAgents, densityThreshold, weightOfDistance,
				weightOfOccupancy, boidVisionRange, boidSpeed, goal, displaySteps));
	}
	/**
	 * The main method of the whole system.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		long startTime = System.nanoTime();

		// solveWollowskiSP();
		// solveWollowskiTSPBasicGraph();
		// solveWollowskiTSP4CitiesFullConnectedGraph();
		// solveWollowskiTSP4CitiesSparseGraph();

		// solveOptimizedSP();
		// solveOptimizedTSPBasicGraph();
		// solveOptimizedTSP4CitiesFullConnectedGraph();
		// solveOptimizedTSP4CitiesSparseGraph();

		// solveOptimizedTSP8CitiesFullConnectedGraph();
		// solveOptimizedTSP8CitiesSparseGraph();
		// solveOptimizedTSP30CitiesFullConnectedGraph();
		
		solveOptimizedTSPEil51();
		// testWD();
		// testWO();
		// testBoidSpeed();
		// testMaxAgents();
		// testBoidVision();
		// testSegmentCapacity();
		// testSegmentLength();

		long estimatedTime = System.nanoTime() - startTime;
		System.out.println("Execution Time: " + estimatedTime / 1000000000.0f + " seconds.");
	}

	/**
	 * Test weight of distance.
	 */
	public static void testWD() {
		int maxIterations = 5000; // Max iterations
		boolean displaySteps = false; // show boids' movement on each iteration

		// Constants I believe their optimal values depend on the number of cities
		int numberOfCities = 30;
		int maxAgents = 3 * numberOfCities * numberOfCities;
		float multiplierBoidSpawn = 1f;
		float densityThreshold = 0.7f;

		// Constants I believe do not depend on the number of cities
		float weightOfOccupancy = 1f;
		float boidSpeed = 2 * multiplierBoidSpawn;
		float boidVisionRange = boidSpeed * 3;

		int segmentCapacity = 3;
		float segmentLength = 1f;

		GoalEvaluator goal = new TSPGoalEvaluator();

		TraditionalGraph graph = GenerateFullyConnectedInstance.GenerateFullyConnectedGraph(numberOfCities); // Fully
																												// connected

		Problem problem = new Problem(new FlockingGraph(graph, segmentLength, segmentCapacity), maxIterations);

		for (float wD = 2.0f; wD <= 5f; wD += 0.5f) {
			float average = 0f;
			float divider = 100f;
			for (int i = 0; i < 100; i++) {
				Tour solution = problem.solve(multiplierBoidSpawn, maxAgents, densityThreshold, wD, weightOfOccupancy,
						boidVisionRange, boidSpeed, goal, displaySteps);
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

	/**
	 * Test weight of occupancy.
	 */
	public static void testWO() {
		int maxIterations = 5000; // Max iterations
		boolean displaySteps = false; // show boids' movement on each iteration

		// Constants I believe their optimal values depend on the number of cities
		int numberOfCities = 8;
		int maxAgents = 3 * numberOfCities * numberOfCities;
		float multiplierBoidSpawn = 1f;
		float densityThreshold = 0.7f;

		// Constants I believe do not depend on the number of cities
		float weightOfDistance = 5f;
		float boidSpeed = 2 * multiplierBoidSpawn;
		float boidVisionRange = boidSpeed * 3;

		int segmentCapacity = 3;
		float segmentLength = 1f;

		GoalEvaluator goal = new TSPGoalEvaluator();

		TraditionalGraph graph = GenerateFullyConnectedInstance.GenerateFullyConnectedGraph(numberOfCities); // Fully
																												// connected

		Problem problem = new Problem(new FlockingGraph(graph, segmentLength, segmentCapacity), maxIterations);

		for (float wO = 0.0f; wO <= 5f; wO += 0.5f) {
			float average = 0f;
			float divider = 100f;
			for (int i = 0; i < 100; i++) {
				Tour solution = problem.solve(multiplierBoidSpawn, maxAgents, densityThreshold, weightOfDistance, wO,
						boidVisionRange, boidSpeed, goal, displaySteps);
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

	/**
	 * Test boid speed.
	 */
	public static void testBoidSpeed() {
		int maxIterations = 5000; // Max iterations
		boolean displaySteps = false; // show boids' movement on each iteration

		// Constants I believe their optimal values depend on the number of cities
		int numberOfCities = 8;
		int maxAgents = 3 * numberOfCities * numberOfCities;
		float multiplierBoidSpawn = 1f;
		float densityThreshold = 0.7f;

		// Constants I believe do not depend on the number of cities
		float weightOfDistance = 5f;
		float weightOfOccupancy = 1f;
		float boidVisionRange = 2 * multiplierBoidSpawn * 3;

		int segmentCapacity = 3;
		float segmentLength = 1f;

		GoalEvaluator goal = new TSPGoalEvaluator();

		TraditionalGraph graph = GenerateFullyConnectedInstance.GenerateFullyConnectedGraph(numberOfCities); // Fully
																												// connected

		Problem problem = new Problem(new FlockingGraph(graph, segmentLength, segmentCapacity), maxIterations);

		for (float speed = 1.0f; speed <= 10f; speed += 1f) {
			float average = 0f;
			float divider = 100f;
			for (int i = 0; i < 100; i++) {
				Tour solution = problem.solve(multiplierBoidSpawn, maxAgents, densityThreshold, weightOfDistance,
						weightOfOccupancy, boidVisionRange, speed, goal, displaySteps);
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

	/**
	 * Test boid vision.
	 */
	public static void testBoidVision() {
		int maxIterations = 5000; // Max iterations
		boolean displaySteps = false; // show boids' movement on each iteration

		// Constants I believe their optimal values depend on the number of cities
		int numberOfCities = 8;
		int maxAgents = 3 * numberOfCities * numberOfCities;
		float multiplierBoidSpawn = 1f;
		float densityThreshold = 0.7f;

		// Constants I believe do not depend on the number of cities
		float weightOfDistance = 5f;
		float weightOfOccupancy = 1f;
		float boidSpeed = 2 * multiplierBoidSpawn;

		int segmentCapacity = 3;
		float segmentLength = 1f;

		GoalEvaluator goal = new TSPGoalEvaluator();

		TraditionalGraph graph = GenerateFullyConnectedInstance.GenerateFullyConnectedGraph(numberOfCities); // Fully
																												// connected

		Problem problem = new Problem(new FlockingGraph(graph, segmentLength, segmentCapacity), maxIterations);

		for (float visionMultiplier = 1.0f; visionMultiplier <= 10f; visionMultiplier += 1f) {
			float average = 0f;
			float divider = 100f;
			for (int i = 0; i < 100; i++) {
				Tour solution = problem.solve(multiplierBoidSpawn, maxAgents, densityThreshold, weightOfDistance,
						weightOfOccupancy, visionMultiplier * boidSpeed, boidSpeed, goal, displaySteps);
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

	/**
	 * Test max agents.
	 */
	public static void testMaxAgents() {
		int maxIterations = 5000; // Max iterations
		boolean displaySteps = false; // show boids' movement on each iteration

		// Constants I believe their optimal values depend on the number of cities
		int numberOfCities = 8;
		float multiplierBoidSpawn = 1f;
		float densityThreshold = 0.7f;

		// Constants I believe do not depend on the number of cities
		float weightOfDistance = 5f;
		float weightOfOccupancy = 1f;
		float boidSpeed = 2 * multiplierBoidSpawn;
		float boidVisionRange = boidSpeed * 3;

		int segmentCapacity = 3;
		float segmentLength = 1f;

		GoalEvaluator goal = new TSPGoalEvaluator();

		TraditionalGraph graph = GenerateFullyConnectedInstance.GenerateFullyConnectedGraph(numberOfCities); // Fully
																												// connected

		Problem problem = new Problem(new FlockingGraph(graph, segmentLength, segmentCapacity), maxIterations);

		for (int maxAgentsMultiplier = 1; maxAgentsMultiplier <= 100; maxAgentsMultiplier += 10) {
			float average = 0f;
			float divider = 100f;
			for (int i = 0; i < 100; i++) {
				Tour solution = problem.solve(multiplierBoidSpawn, numberOfCities * maxAgentsMultiplier,
						densityThreshold, weightOfDistance, weightOfOccupancy, boidVisionRange, boidSpeed, goal,
						displaySteps);
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

	/**
	 * Test segment capacity.
	 */
	public static void testSegmentCapacity() {
		int maxIterations = 5000; // Max iterations
		boolean displaySteps = false; // show boids' movement on each iteration

		// Constants I believe their optimal values depend on the number of cities
		int numberOfCities = 8;
		int maxAgents = 3 * numberOfCities * numberOfCities;
		float multiplierBoidSpawn = 1f;
		float densityThreshold = 0.7f;

		// Constants I believe do not depend on the number of cities
		float weightOfDistance = 5f;
		float weightOfOccupancy = 1f;
		float boidSpeed = 2 * multiplierBoidSpawn;
		float boidVisionRange = boidSpeed * 3;

		float segmentLength = 1f;

		GoalEvaluator goal = new TSPGoalEvaluator();

		TraditionalGraph graph = GenerateFullyConnectedInstance.GenerateFullyConnectedGraph(numberOfCities); // Fully
																												// connected

		for (int capacity = 1; capacity <= 10; capacity += 1) {
			float average = 0f;
			float divider = 100f;
			for (int i = 0; i < 100; i++) {
				Problem problem = new Problem(new FlockingGraph(graph, segmentLength, capacity), maxIterations);
				Tour solution = problem.solve(multiplierBoidSpawn, maxAgents, densityThreshold, weightOfDistance,
						weightOfOccupancy, boidVisionRange, boidSpeed, goal, displaySteps);
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

	/**
	 * Test segment length.
	 */
	public static void testSegmentLength() {
		int maxIterations = 5000; // Max iterations
		boolean displaySteps = false; // show boids' movement on each iteration

		// Constants I believe their optimal values depend on the number of cities
		int numberOfCities = 8;
		int maxAgents = 3 * numberOfCities * numberOfCities;
		float multiplierBoidSpawn = 1f;
		float densityThreshold = 0.7f;

		// Constants I believe do not depend on the number of cities
		float weightOfDistance = 5f;
		float weightOfOccupancy = 1f;
		float boidSpeed = 2 * multiplierBoidSpawn;
		float boidVisionRange = boidSpeed * 3;

		int segmentCapacity = 3;

		GoalEvaluator goal = new TSPGoalEvaluator();

		TraditionalGraph graph = GenerateFullyConnectedInstance.GenerateFullyConnectedGraph(numberOfCities); // Fully
																												// connected

		for (float segmentLength = 1; segmentLength <= 10; segmentLength += 1) {
			float average = 0f;
			float divider = 100f;
			for (int i = 0; i < 100; i++) {
				Problem problem = new Problem(new FlockingGraph(graph, segmentLength, segmentCapacity), maxIterations);
				Tour solution = problem.solve(multiplierBoidSpawn, maxAgents, densityThreshold, weightOfDistance,
						weightOfOccupancy, boidVisionRange, boidSpeed, goal, displaySteps);
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
