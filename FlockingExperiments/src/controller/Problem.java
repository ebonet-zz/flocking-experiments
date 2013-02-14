package controller;

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
import agent.GoalEvaluator;

/**
 * Defines a Flocking-TSP problem.
 * 
 * @author Balthazar. Created Dec 10, 2012.
 */
public class Problem {

	boolean graphics = false;

	/**
	 * Distances between nodes
	 */
	FlockingGraph distanceGraph;

	/**
	 * Number of cities
	 */
	int numberOfCities;

	/**
	 * Max number of iterations
	 */
	int maxIterations;

	/**
	 * Constructor that instantiates a new problem based on a distance graph and max number of iterations
	 * 
	 * @param distanceGraph
	 * @param tMax
	 */
	public Problem(FlockingGraph distanceGraph, int tMax) {
		this.distanceGraph = distanceGraph;
		this.numberOfCities = distanceGraph.getNumberOfNodes();
		this.maxIterations = tMax;
	}

	/**
	 * Attempts to solve the problem given the constant values below
	 * 
	 * @return The information about the best found Tour (path)
	 */
	public Tour solve(double boidsPerIteration, int maxBoids, double densityThreshold, double wDist, double wOccup,
			double vision, double speed, GoalEvaluator goal) {

		// this.graphics = true;
		Tour expectedShortestTour = null;
		this.distanceGraph.resetAndBuildSegments();

		FlockingGraphViewer viewer = null;
		if (this.graphics) {
			viewer = new FlockingGraphViewer(this.distanceGraph);
			viewer.openViewer();
		}

		// Random and seed
		Random r = new Random();

		Environment environment = new Environment(this.distanceGraph, maxBoids);

		double currentBoidCreationProgress = 0d;
		// Main Loop
		for (int t = 1; t <= this.maxIterations; t++) { // In each iteration

			if (environment.getAllBoids().size() < maxBoids) {
				currentBoidCreationProgress += boidsPerIteration;

				while (new Double(currentBoidCreationProgress).compareTo(1d) >= 0) {
					spawnBoid(r, environment, goal, speed, vision, wDist, wOccup);
					currentBoidCreationProgress -= 1d;
				}
			}

			Set<Boid> aliveBoids = environment.getAllBoids();
			for (Boid b : aliveBoids) {
				b.tryToMove(b.getSpeed());
			}

			SortableKeyValue<Tour, Double> mostDensePath = environment.getMostDensePath();
			if (mostDensePath != null) {
				expectedShortestTour = mostDensePath.keyObject;
				Double density = mostDensePath.valueToUseOnSorting;

				if (density.compareTo(densityThreshold) >= 0) {
					// System.out.println("Converged in Iteration " + t);
					break;
				}
			}

			if (this.graphics) {
				draw(viewer, aliveBoids);
				try {
					Thread.sleep(100);
				} catch (InterruptedException exception) {
					exception.printStackTrace();
				}
			}
			// System.out.println("NUmber of flocks:" + environment.getNumberOfFlocks());
		}
		// if (!this.graphics) {
		// // Show only the final positions of all the boids
		// viewer = new FlockingGraphViewer(this.distanceGraph);
		// viewer.openViewer();
		// Set<Boid> aliveBoids = environment.getAllBoids();
		// draw(viewer, aliveBoids);
		// try {
		// Thread.sleep(100);
		// } catch (InterruptedException exception) {
		// exception.printStackTrace();
		// }
		//
		// environment.printDistancesMap();
		// }

		return expectedShortestTour;
	}

	private void spawnBoid(Random r, Environment environment, GoalEvaluator goal, double boidSpeed, double visionRange,
			double weightOfDistance, double weightOfOccupancy) {
		Position newWouldBePos = getSpawnPosition(r);
		double speed = randomize(boidSpeed, r);
		Boid newBoid = createNewBoid(newWouldBePos, speed, visionRange, weightOfDistance, weightOfOccupancy,
				environment, goal, r);
	}

	protected Position getSpawnPosition(Random r) {
		Integer startNode = 0;
		Integer[] possibleEndNodes = this.distanceGraph.getArrayOfNeighborsOf(startNode);
		int nextNodeIndex = r.nextInt(possibleEndNodes.length);
		Integer endNode = possibleEndNodes[nextNodeIndex];

		Position newWouldBePos = new Position(this.distanceGraph.getEdge(startNode, endNode), 0d);
		Segment nextWouldBeSegment = this.distanceGraph.getSegmentForPosition(newWouldBePos);

		while ((nextWouldBeSegment.isFull())) {
			newWouldBePos.distanceFromStart = this.distanceGraph.getEdgeLength(startNode, endNode) * r.nextFloat();
			nextWouldBeSegment = this.distanceGraph.getSegmentForPosition(newWouldBePos);
		}
		return newWouldBePos;
	}

	protected Boid createNewBoid(Position newWouldBePos, double speed, double visionRange, double weightOfDistance,
			double weightOfOccupancy, Environment environment, GoalEvaluator goal, Random r) {
		return new Boid(newWouldBePos, speed, visionRange, weightOfDistance, weightOfOccupancy, environment, goal, r);
	}

	private Double randomize(double value, Random r) {
		return value * (1 + r.nextDouble() * 0.4 - 0.2);
	}

	private void draw(FlockingGraphViewer viewer, Set<Boid> boids) {
		viewer.updateViewer(extractBoidPositions(boids));
	}

	public List<MovingObject> extractBoidPositions(Set<Boid> boids) {
		List<MovingObject> ps = new ArrayList<MovingObject>();
		for (Boid b : boids) {
			ps.add(new MovingObject(b.getPos(), b.getColor()));
		}
		return ps;
	}

}
