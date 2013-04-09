package controller;

import java.io.IOException;

import log.TestsLogger;

import goal.EndNodeGoalEvaluator;
import goal.GoalEvaluator;
import goal.TSPGoalEvaluator;
import graph.EuclideanGraph;
import graph.FlockingGraph;
import graph.Tour;
import graph.TraditionalGraph;
import problem.Problem;
import problem.WollowskiProblem;
import viewer.EuclideanGraphViewer;
import viewer.FlockingGraphViewer;

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
	 * Solve TSP with Optimized boids in an Euclidean graph instance from TSPLIB.
	 */
	public static void solveOptimizedTSPLib(String pathToGraph, int numberOfCities) {
		int maxIterations = 15000; // Max iterations
		boolean displaySteps = false; // show boids' movement on each iteration
		boolean verbose = true; // print detailed information to std out
		
		// Constants I believe their optimal values depend on the number of cities
//		int numberOfCities = 51;
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

		EuclideanGraph eucGraph;
		try {
			eucGraph = GraphReaderTSPLIB.generateGraphFromFile(pathToGraph);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		FlockingGraph flockingGraph = new FlockingGraph(eucGraph, segmentLength, segmentCapacity);
		// create an EuclideanGraph Viewer here, something like
		// FlockingGraphViewer viewer = new EuclideanGraphViewer(eucGraph);
		// FlockingGraphViewer viewer = new FlockingGraphViewer(flockingGraph);
		EuclideanGraphViewer viewer = new EuclideanGraphViewer(eucGraph);
		
		
		Problem problem = new Problem(flockingGraph, maxIterations);

		System.out.println(problem.solve(multiplierBoidSpawn, maxAgents, densityThreshold, weightOfDistance,
				weightOfOccupancy, boidVisionRange, boidSpeed, goal, displaySteps, viewer, verbose));
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
		String filepath = GraphReaderTSPLIB.EIL_51;
		int numberOfCities = 51;
		if (args.length > 0) {
			filepath = args[0];
			numberOfCities = Integer.parseInt(args[1]);
		}
		//solveOptimizedTSPLib(filepath, numberOfCities);
		testBoidSpeed(numberOfCities, filepath, 45);
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
	public static void testBoidSpeed(int numberOfCities, String pathToGraph, float initialSpeed) {
		int maxIterations = 15000; // Max iterations
		boolean displaySteps = false; // show boids' movement on each iteration
		boolean verbose = true; // print detailed information to std out
		
		// Constants I believe their optimal values depend on the number of cities
//		int numberOfCities = 51;
		int maxAgents = 3 * numberOfCities * numberOfCities;
		float multiplierBoidSpawn = 1f;
		float densityThreshold = 0.7f;

		// Constants I believe do not depend on the number of cities
		float weightOfDistance = 13f;
		float weightOfOccupancy = 1f;

		int segmentCapacity = 3;
		float segmentLength = 1f;

		GoalEvaluator goal = new TSPGoalEvaluator();

		EuclideanGraph eucGraph;
		try {
			eucGraph = GraphReaderTSPLIB.generateGraphFromFile(pathToGraph);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		FlockingGraph flockingGraph = new FlockingGraph(eucGraph, segmentLength, segmentCapacity);
		// create an EuclideanGraph Viewer here, something like
		// FlockingGraphViewer viewer = new EuclideanGraphViewer(eucGraph);
		// FlockingGraphViewer viewer = new FlockingGraphViewer(flockingGraph);
		EuclideanGraphViewer viewer = null;
		if (displaySteps)
			viewer = new EuclideanGraphViewer(eucGraph);
		
		
		Problem problem = new Problem(flockingGraph, maxIterations);

		int runsPerValue = 15;
		for (float speed = initialSpeed; speed <= initialSpeed + 20; speed += 10) {
			TestsLogger.logMessage("Starting tests with speed=" + speed);
			float average = 0f;
			float divider = runsPerValue;
			float boidVisionRange = 3*speed;
			for (int i = 0; i < runsPerValue; i++) {
				Tour solution = problem.solve(multiplierBoidSpawn, maxAgents, densityThreshold, weightOfDistance,
						weightOfOccupancy, boidVisionRange, speed, goal, displaySteps);
				if (solution != null) {
					average += solution.lastCalculatedCost;
					TestsLogger.logMessage("Tour found: " + solution.toString());
				} else {
					divider--;
					// TestsLogger.logMessage("No tour found.");
				}
			}

			average /= divider;
			TestsLogger.logSpeedTest(speed, divider*100/runsPerValue, average);
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
