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
	public String solve(int m, double e, double B, double a, double v, double s, GoalEvaluator goal) {

		this.graphics = true;

		FlockingGraphViewer viewer = null;
		if (this.graphics) {
			viewer = new FlockingGraphViewer(this.distanceGraph);
			viewer.openViewer();
		}

		// Number of boids spawned per time unit
		this.multiplierForBoidSpawn = m;

		// Set constants
		this.occupancyDensityThreshold = e;
		this.weightOfDistance = B;
		this.weightOfOccupancy = a;
		this.visionRange = v;
		this.speed = s;

		// Random and seed
		Random r = new Random();

		Tour shortestTour = null;

		Set<Boid> enviroment = new HashSet<Boid>();

		Boid testBoid = new Boid(this.distanceGraph, new Position(this.distanceGraph.getEdge(0, 1), 0d), this.speed,
				this.visionRange, this.weightOfDistance, this.weightOfOccupancy, enviroment, goal);

		// Main Loop
		for (int t = 1; t <= this.maxIterations; t++) { // In each iteration

			for (Boid b : enviroment) {
				b.tryToMove(b.getSpeed());
			}

			if (this.graphics) {
				draw(viewer, enviroment);
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

	private void draw(FlockingGraphViewer viewer, Set<Boid> enviroment) {
		viewer.updateViewer(extractBoidPositions(enviroment));
	}

	public List<Position> extractBoidPositions(Set<Boid> enviroment) {
		List<Position> ps = new ArrayList<Position>();
		for (Boid b : enviroment) {
			ps.add(b.getPos());
		}
		return ps;
	}

}
