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

/**
 * The adapted version of the problem class to be solved by wollowski boids.
 */
public class WollowskiProblem extends Problem {

	/**
	 * Instantiates a new wollowski problem.
	 * 
	 * @param distanceGraph
	 *            the distance graph
	 * @param tMax
	 *            the t max
	 */
	public WollowskiProblem(FlockingGraph distanceGraph, int tMax) {
		super(distanceGraph, tMax);
	}

	/**
	 * Creates the new boid.
	 * 
	 * @param newWouldBePos
	 *            the new would be pos
	 * @param speed
	 *            the speed
	 * @param visionRange
	 *            the vision range
	 * @param numberOfChoicesWeight
	 *            the weight of number of choices in the formula
	 * @param visibleBoidsInPathWeight
	 *            the weight of visible boids in path in the formula
	 * @param environment
	 *            the environment
	 * @param goal
	 *            the goal
	 * @param r
	 *            the random instance
	 * @return the boid
	 */
	@Override
	protected Boid createNewBoid(Position newWouldBePos, double speed, double visionRange,
			double numberOfChoicesWeight, double visibleBoidsInPathWeight, Environment environment, GoalEvaluator goal,
			Random r) {
		return new WollowskiBoid(newWouldBePos, speed, visionRange, numberOfChoicesWeight, visibleBoidsInPathWeight,
				environment, goal, r);
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
}
