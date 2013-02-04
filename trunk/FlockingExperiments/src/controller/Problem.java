package controller;

import graph.FlockingGraph;
import graph.Position;
import graph.Tour;

import java.util.ArrayList;
import java.util.HashSet;
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
	int multiplierForBoidSpawn = 3;

	/**
	 * Number of cities
	 */
	int numberOfCities = 10;

	/**
	 * Max number of iterations
	 */
	int maxIterations = 1000;

	/**
	 * Weight of visibility (distances)
	 */
	double weightOfDistance = 2;

	/**
	 * Weight of occupancy (crowdedness)
	 */
	double weightOfOccupancy = 1;

	/**
	 * End of algorithm density threshold
	 */
	double occupancyDensityThreshold = 0.70f;

	/**
	 * boid vision range
	 */
	double visionRange = 2f;

	/**
	 * boid speed
	 */
	double speed = 1f;

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
	public String solve(int boidsPerSecond, double densityThreshold, double wDist, double wOccup, double vision,
			double speed, GoalEvaluator goal) {

		this.graphics = true;

		FlockingGraphViewer viewer = null;
		if (this.graphics) {
			viewer = new FlockingGraphViewer(this.distanceGraph);
			viewer.openViewer();
		}

		// Number of boids spawned per time unit
		this.multiplierForBoidSpawn = boidsPerSecond;

		// Set constants
		this.occupancyDensityThreshold = densityThreshold;
		this.weightOfDistance = wDist;
		this.weightOfOccupancy = wOccup;
		this.visionRange = vision;
		this.speed = speed;

		// Random and seed
		Random r = new Random();

		Tour shortestTour = null;

		// Set<Boid> enviroment = new HashSet<Boid>();
		Environment environment = new Environment();
		Boid testBoid = new Boid(this.distanceGraph, new Position(this.distanceGraph.getEdge(0, 1), 0d), this.speed,
				this.visionRange, this.weightOfDistance, this.weightOfOccupancy, environment, goal);

		// Main Loop
		for (int t = 1; t <= this.maxIterations; t++) { // In each iteration

			for (int i = 0; i < this.multiplierForBoidSpawn; i++) {
				Boid b = new Boid(this.distanceGraph, new Position(this.distanceGraph.getEdge(0, 1),
						this.distanceGraph.getEdgeLength(0, 1) * r.nextFloat()), this.speed, this.visionRange,
						this.weightOfDistance, this.weightOfOccupancy, environment, goal);
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
					// TODO Auto-generated catch-block stub.
					exception.printStackTrace();
				}
			}
		}

		if (shortestTour != null)
			return shortestTour.toString();
		else
			return "No Tour found";
	}

	private void draw(FlockingGraphViewer viewer, Set<Boid> boids) {
		viewer.updateViewer(extractBoidPositions(boids));
	}
	
	public List<MovingObject> extractBoidPositions(Set<Boid> boids) {
		List<MovingObject> ps = new ArrayList<MovingObject>();
		for (Boid b : boids) {
			ps.add(new MovingObject(b.getPos(),b.getColor()));
		}
		return ps;
	}

}
