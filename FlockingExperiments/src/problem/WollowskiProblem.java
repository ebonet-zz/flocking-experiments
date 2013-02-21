package problem;

import goal.GoalEvaluator;
import graph.Edge;
import graph.FlockingGraph;
import graph.Position;
import graph.Tour;

import java.util.List;
import java.util.Random;

import util.SolutionBacktracker;
import agent.Boid;
import agent.Environment;
import agent.WollowskiBoid;

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
				Tour t = traceTourFoundByAlgorithm(environment, goal);
				if (t != null) {
					t.calculateCost(environment.getFlockingGraph());
					return t;
				}
			}
		}
		return null;
	}

	private Tour traceTourFoundByAlgorithm(Environment environment, GoalEvaluator goal) {
		Tour t = new SolutionBacktracker(environment, goal).doBacktrack();

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
