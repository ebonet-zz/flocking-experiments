package problem;

import goal.GoalEvaluator;
import graph.FlockingGraph;
import graph.Position;
import graph.Tour;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

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
		Set<Boid> boids = environment.getAllBoids();
		for (Integer n : neighborsOf0) {
			for (Boid b : boids) {
				WollowskiBoid boid = (WollowskiBoid) b;
				if (!boid.citiesChecked.isEmpty() && boid.citiesChecked.getLast() == n) {
					Tour t = new Tour();
					t.offer(0);
					Iterator<Integer> it = boid.citiesChecked.descendingIterator();
					while (it.hasNext()) {
						t.offer(it.next());
					}

					if (goal.isGoal(environment.getFlockingGraph(), t)) {
						t.calculateCost(environment.getFlockingGraph());
						return t;
					}
				}
			}
		}
		return null;
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
