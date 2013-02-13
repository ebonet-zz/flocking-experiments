package controller;

import graph.FlockingGraph;
import graph.Position;

import java.util.Random;

import agent.Boid;
import agent.GoalEvaluator;
import agent.TSPBoid;

public class TSPProblem extends Problem {

	public TSPProblem(FlockingGraph distanceGraph, int tMax) {
		super(distanceGraph, tMax);
	}

	@Override
	protected Boid createNewBoid(Position newWouldBePos, double speed, double visionRange, double weightOfDistance,
			double weightOfOccupancy, Environment environment, GoalEvaluator goal, Random r) {
		return new TSPBoid(newWouldBePos, speed, visionRange, weightOfDistance, weightOfOccupancy, environment, goal, r);
	}
}
