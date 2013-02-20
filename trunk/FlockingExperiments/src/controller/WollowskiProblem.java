package controller;

import graph.Edge;
import graph.FlockingGraph;
import graph.Position;
import graph.Tour;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import agent.Boid;
import agent.GoalEvaluator;
import agent.WollowskiBoid;

/**
 * TODO Put here a description of what this class does.
 * 
 * @author Balthazar. Created Feb 20, 2013.
 */
public class WollowskiProblem extends Problem {

	public WollowskiProblem(FlockingGraph distanceGraph, int tMax) {
		super(distanceGraph, tMax);
	}

	@Override
	protected Boid createNewBoid(Position newWouldBePos, double speed, double visionRange, double weightOfDistance,
			double weightOfOccupancy, Environment environment, GoalEvaluator goal, Random r) {
		return new WollowskiBoid(newWouldBePos, speed, visionRange, weightOfDistance, weightOfOccupancy, environment,
				goal, r);
	}

	@Override
	protected Tour testAlgorithmTermination(Environment environment, double densityThreshold, GoalEvaluator goal) {
		List<Integer> neighborsOf0 = environment.getFlockingGraph().getNeighborsOf(0);
		for (Integer n : neighborsOf0) {
			Edge e = environment.getFlockingGraph().getEdge(0, n);

			if (environment.getFlockingGraph().isEdgeFull(e)) {
				// TODO: The pruning is making this end before it should, because boids get clogged in subtours
				return traceTourFoundByAlgorithm(environment, goal);
			}
		}
		return null;
	}

	private Tour traceTourFoundByAlgorithm(Environment environment, GoalEvaluator goal) {
		// TODO: Fix this, its wrong
		Tour t = new Tour();

		int currentNode = 0;
		t.offer(currentNode);

		boolean linking = false;
		do {
			List<Integer> neighbors = environment.getFlockingGraph().getNeighborsOf(currentNode);

			ArrayList<Integer> unvisitedNeighbors = new ArrayList<>();
			unvisitedNeighbors.addAll(neighbors);

			for (int i : t.locations) {
				unvisitedNeighbors.remove(new Integer(i));
			}

			if (!unvisitedNeighbors.isEmpty()) {
				neighbors = unvisitedNeighbors;
			} else {
				boolean gotAllCities = true;
				for (int i = 0; i < environment.getFlockingGraph().getNumberOfNodes(); i++) {
					if (!t.locations.contains(new Integer(i))) {
						gotAllCities = false;
						break;
					}
				}
				if (gotAllCities) {
					Integer startNode = t.firstLocation();
					if (neighbors.contains(startNode)) {
						neighbors.clear();
						neighbors.add(startNode);
					}
				}
			}

			linking = false;
			for (Integer n : neighbors) {
				Edge e = environment.getFlockingGraph().getEdge(currentNode, n);

				if (environment.getFlockingGraph().isEdgeFull(e)) {
					t.offer(n);
					currentNode = n;
					linking = true;
					break;
				}
			}

		} while (linking && !goal.isGoal(environment.getFlockingGraph(), t));

		t.calculateCost(environment.getFlockingGraph());

		return t;
	}

	private int countOccurences(Tour t, Integer index) {
		int r = 0;
		for (Integer i : t.locations) {
			if (i.equals(index))
				r++;
		}
		return r;
	}

}
