package problem;

import goal.GoalEvaluator;
import graph.FlockingGraph;
import graph.Position;
import graph.Segment;
import graph.Tour;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import util.SortableKeyValue;
import viewer.FlockingGraphViewer;
import viewer.MovingObject;
import agent.Boid;
import agent.Environment;

/**
 * Defines a Flocking-TSP problem.
 * 
 * @author Balthazar. Created Dec 10, 2012.
 */
public class Problem {

	/** The graphics. */
	boolean graphics = false;

	/** Verbose output */
	boolean verbose = true;
	
	/** Distances between nodes. */
	FlockingGraph distanceGraph;

	/** Number of cities. */
	int numberOfCities;

	/** Max number of iterations. */
	int maxIterations;

	/**
	 * Constructor that instantiates a new problem based on a distance graph and max number of iterations.
	 * 
	 * @param distanceGraph
	 *            the distance graph
	 * @param tMax
	 *            the t max
	 */
	public Problem(FlockingGraph distanceGraph, int tMax) {
		this.distanceGraph = distanceGraph;
		this.numberOfCities = distanceGraph.getNumberOfNodes();
		this.maxIterations = tMax;
	}

	/**
	 * Attempts to solve the problem given the constant values below.
	 * 
	 * @param boidsPerIteration
	 *            the boids per iteration
	 * @param maxBoids
	 *            the max boids
	 * @param densityThreshold
	 *            the density threshold
	 * @param wDist
	 *            the weight of distance
	 * @param wOccup
	 *            the weight of occupancy
	 * @param vision
	 *            the vision
	 * @param speed
	 *            the speed
	 * @param goal
	 *            the goal
	 * @param displaySteps
	 *            show graphical representation of steps
	 * @param viewer
	 * 			  A UI object used for graphical representation of the steps. If displaySteps is false, this can be null.
	 * @param verbose
	 * 			  print detailed information to standard output
	 * @return The information about the best found Tour (path)
	 */
	public Tour solve(double boidsPerIteration, int maxBoids, double densityThreshold, double wDist, double wOccup,
			double vision, double speed, GoalEvaluator goal, boolean displaySteps, FlockingGraphViewer viewer, boolean verbose) {
		this.graphics = displaySteps;
		this.verbose = verbose;
		if (this.verbose) {
			System.out.println("Algorithm started!");
		}

		System.gc();

		this.distanceGraph.resetAndBuildSegments();

		if (this.graphics) {
			viewer.openViewer();
		}

		// Random and seed
		Random r = new Random();

		Environment environment = new Environment(this.distanceGraph);

		double currentBoidCreationProgress = 0d;
		// Main Loop
		for (int t = 1; t <= this.maxIterations; t++) { // In each iteration

			// check if we need to create more agents
			if (environment.countAllBoids() < maxBoids
					&& environment.getAllAchievers().size() <= (environment.countAllBoids() / 3)) {
				currentBoidCreationProgress += boidsPerIteration;

				// try to build a new agent
				while (new Double(currentBoidCreationProgress).compareTo(1d) >= 0) {
					try {
						spawnBoid(r, environment, goal, speed, vision, wDist, wOccup);
					} catch (Exception exception) {
						if (this.verbose) {
							System.out.println("Skept a boid creation because initial paths are too crowded.");
							System.out.println("This indicates a possible 'clogged graph' deadlock.");
							System.out.println("Consider using less agents for this problem config.\n");
						}
					}
					currentBoidCreationProgress -= 1d;
				}
			}

			// act with every agent
			Set<Boid> aliveBoids = environment.getAllBoids();
			for (Boid b : aliveBoids) {
				b.tryToMove(b.getSpeed());
			}

			Tour result = testAlgorithmTermination(environment, densityThreshold, goal);
			if (result != null) {
				if (this.verbose) {
					System.out.println("Converged in Iteration " + t);
				}
				return result;
			}

			if (this.graphics) {
				draw(viewer, aliveBoids);
				try {
					Thread.sleep(100);
				} catch (InterruptedException exception) {
					exception.printStackTrace();
				}
			}
		}

		if (this.verbose) {
			System.out.println("The algorithm did not converge in " + this.maxIterations + " iterations.");
			System.out.println("Current flocks:");
			for (SortableKeyValue<Tour, Double> tour : environment.getAllPaths()) {
				System.out.println(tour.keyObject.toString());
				System.out.println("Density: " + tour.valueToUseOnSorting);
				System.out.println();
			}
		}
			
		return null;
	}
	
	public Tour solve(double boidsPerIteration, int maxBoids, double densityThreshold, double wDist, double wOccup,
			double vision, double speed, GoalEvaluator goal, boolean displaySteps) {
		FlockingGraphViewer viewer = null;
		if (displaySteps == true) {
			viewer = new FlockingGraphViewer(this.distanceGraph);
		}
		return this.solve(boidsPerIteration, maxBoids, densityThreshold, wDist, wOccup, vision, speed, goal, displaySteps, viewer, true);
	}

	/**
	 * Test algorithm termination.
	 * 
	 * @param environment
	 *            the environment
	 * @param densityThreshold
	 *            the density threshold
	 * @param goal
	 *            the goal
	 * @return the tour
	 */
	protected Tour testAlgorithmTermination(Environment environment, double densityThreshold, GoalEvaluator goal) {
		SortableKeyValue<Tour, Double> mostDensePath = environment.getMostDensePath();
		if (mostDensePath != null) {
			Double density = mostDensePath.valueToUseOnSorting;

			if (density.compareTo(densityThreshold) >= 0) {
				return mostDensePath.keyObject;
			}
		}
		return null;
	}

	/**
	 * Spawn boid.
	 * 
	 * @param r
	 *            the random instance
	 * @param environment
	 *            the environment
	 * @param goal
	 *            the goal
	 * @param boidSpeed
	 *            the boid speed
	 * @param visionRange
	 *            the vision range
	 * @param weightOfDistance
	 *            the weight of distance
	 * @param weightOfOccupancy
	 *            the weight of occupancy
	 * @throws Exception
	 *             the 'too crowded' exception
	 */
	private void spawnBoid(Random r, Environment environment, GoalEvaluator goal, double boidSpeed, double visionRange,
			double weightOfDistance, double weightOfOccupancy) throws Exception {
		Position newWouldBePos = getSpawnPosition(r);
		double speed = randomize(boidSpeed, r);
		Boid newBoid = createNewBoid(newWouldBePos, speed, visionRange, weightOfDistance, weightOfOccupancy,
				environment, goal, r);
	}

	/**
	 * Gets the spawn position.
	 * 
	 * @param r
	 *            the random instance
	 * @return the spawn position
	 * @throws Exception
	 *             the 'too crowded' exception
	 */
	protected Position getSpawnPosition(Random r) throws Exception {
		Integer startNode = 0;
		Integer[] possibleEndNodes = this.distanceGraph.getArrayOfNeighborsOf(startNode);
		int nextNodeIndex = r.nextInt(possibleEndNodes.length);
		Integer endNode = possibleEndNodes[nextNodeIndex];

		Position newWouldBePos = new Position(this.distanceGraph.getEdge(startNode, endNode), 0d);
		Segment nextWouldBeSegment = this.distanceGraph.getSegmentForPosition(newWouldBePos);

		int attemptCount = 0;

		while ((nextWouldBeSegment.isFull() && attemptCount++ < 10)) {
			System.gc();

			nextNodeIndex = r.nextInt(possibleEndNodes.length);
			endNode = possibleEndNodes[nextNodeIndex];

			newWouldBePos = new Position(this.distanceGraph.getEdge(startNode, endNode),
					this.distanceGraph.getEdgeLength(startNode, endNode) * r.nextFloat());
			nextWouldBeSegment = this.distanceGraph.getSegmentForPosition(newWouldBePos);
		}

		if (attemptCount > 10) {
			throw new Exception("Too crowded to spawn new agent");
		}

		return newWouldBePos;
	}

	/**
	 * Creates the new boid.
	 * 
	 * @param newWouldBePos
	 *            the new would be position
	 * @param speed
	 *            the speed
	 * @param visionRange
	 *            the vision range
	 * @param weightOfDistance
	 *            the weight of distance
	 * @param weightOfOccupancy
	 *            the weight of occupancy
	 * @param environment
	 *            the environment
	 * @param goal
	 *            the goal
	 * @param r
	 *            the random instance
	 * @return the boid
	 */
	protected Boid createNewBoid(Position newWouldBePos, double speed, double visionRange, double weightOfDistance,
			double weightOfOccupancy, Environment environment, GoalEvaluator goal, Random r) {
		return new Boid(newWouldBePos, speed, visionRange, weightOfDistance, weightOfOccupancy, environment, goal, r);
	}

	/**
	 * Randomize a given value a little bit.
	 * 
	 * @param value
	 *            the value
	 * @param r
	 *            the random instance
	 * @return the double
	 */
	private Double randomize(double value, Random r) {
		return value * (1 + r.nextDouble() * 0.4 - 0.2);
	}

	/**
	 * Draw the environment.
	 * 
	 * @param viewer
	 *            the viewer
	 * @param boids
	 *            the boids
	 */
	private void draw(FlockingGraphViewer viewer, Set<Boid> boids) {
		viewer.updateViewer(extractBoidPositions(boids));
	}

	/**
	 * Extract boid positions to draw.
	 * 
	 * @param boids
	 *            the boids
	 * @return the list
	 */
	public List<MovingObject> extractBoidPositions(Set<Boid> boids) {
		List<MovingObject> ps = new ArrayList<MovingObject>();
		for (Boid b : boids) {
			ps.add(new MovingObject(b.getPos(), b.getColor()));
		}
		return ps;
	}

}
