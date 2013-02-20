package controller;

import graph.Edge;
import graph.FlockingGraph;
import graph.Position;
import graph.Tour;

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
	protected Tour testAlgorithmTermination(Environment environment, double densityThreshold) {
		List<Integer> neighborsOf0 = environment.getFlockingGraph().getNeighborsOf(0);
		for (Integer n : neighborsOf0) {
			Edge e = environment.getFlockingGraph().getEdge(0, n);

			if (environment.getFlockingGraph().isEdgeFull(e)) {
				return traceTourFoundByAlgorithm(environment);
			}
		}
		return null;
	}

	private Tour traceTourFoundByAlgorithm(Environment environment) {
		Tour t = new Tour();

		int currentNode = 0;
		t.offer(currentNode);

		boolean linking = false;
		do {
			List<Integer> neighbors = environment.getFlockingGraph().getNeighborsOf(currentNode);

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

		} while (linking && currentNode != 0);

		t.calculateCost(environment.getFlockingGraph());

		return t;
	}

}
