package controller;

import graph.FlockingGraph;
import graph.Position;
import graph.Segment;
import graph.Tour;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

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
	 * ArrayList of worker boids
	 */
	ArrayList<Boid> boids;

	/**
	 * Number of Boids spawned per time unit
	 */
	int multiplierForBoidSpawn;

	/**
	 * Number of cities
	 */
	int numberOfCities;

	/**
	 * Max number of iterations
	 */
	int maxIterations;

	/**
	 * Weight of visibility (distances)
	 */
	double weightOfDistance;

	/**
	 * Weight of occupancy (crowdedness)
	 */
	double weightOfOccupancy;

	/**
	 * End of algorithm density threshold
	 */
	double occupancyDensityThreshold;

	/**
	 * boid vision range
	 */
	double visionRange;

	/**
	 * boid speed
	 */
	double speed;

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

		this.boids = new ArrayList<Boid>();
	}

	/**
	 * Attempts to solve the problem given the constant values below
	 * 
	 * @return The information about the best found Tour (path)
	 */
	public String solve(int boidsPerIteration, double densityThreshold, double wDist, double wOccup, double vision,
			double speed, GoalEvaluator goal) {

		this.graphics = true;

		FlockingGraphViewer viewer = null;
		if (this.graphics) {
			viewer = new FlockingGraphViewer(this.distanceGraph);
			viewer.openViewer();
		}

		// Number of boids spawned per time unit
		this.multiplierForBoidSpawn = boidsPerIteration;

		// Set constants
		this.occupancyDensityThreshold = densityThreshold;
		this.weightOfDistance = wDist;
		this.weightOfOccupancy = wOccup;
		this.visionRange = vision;
		this.speed = speed;

		// Random and seed
		Random r = new Random();

		Tour shortestTour = null;

		// Set<Boid> environment = new HashSet<Boid>();
		Environment environment = new Environment(this.distanceGraph);
		// Boid testBoid = new Boid(new Position(this.distanceGraph.getEdge(0, 1), 0d), this.speed, this.visionRange,
		// this.weightOfDistance, this.weightOfOccupancy, environment, goal);

		// Main Loop
		for (int t = 1; t <= this.maxIterations; t++) { // In each iteration

			for (int i = 0; i < this.multiplierForBoidSpawn; i++) {
				spawnBoid(r, environment, goal);
			}

			Set<Boid> aliveBoids = environment.getAllBoids();
			for (Boid b : aliveBoids) {
				b.tryToMove(b.getSpeed());
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

		if (shortestTour != null)
			return shortestTour.toString();
		else
			return "No Tour found";
	}

	private void spawnBoid(Random r, Environment environment, GoalEvaluator goal) {
		Position newWouldBePos = new Position(this.distanceGraph.getEdge(0, 1), this.distanceGraph.getEdgeLength(0, 1)
				* r.nextFloat());
		Segment nextWouldBeSegment = this.distanceGraph.getSegmentForPosition(newWouldBePos);

		while ((nextWouldBeSegment.isFull())) {
			newWouldBePos = new Position(this.distanceGraph.getEdge(0, 1), this.distanceGraph.getEdgeLength(0, 1)
					* r.nextFloat());
			nextWouldBeSegment = this.distanceGraph.getSegmentForPosition(newWouldBePos);
		}

		Boid newBoid = new Boid(newWouldBePos, randomize(this.speed), this.visionRange, this.weightOfDistance,
				this.weightOfOccupancy, environment, goal);
	}

	private Double randomize(double value) {
		return value*(1+Math.random()*0.4-0.2);
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
