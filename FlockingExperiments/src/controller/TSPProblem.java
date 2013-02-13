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
	protected Boid createNewBoid(Position newWouldBePos, Double speed, Environment environment, GoalEvaluator goal,
			Random r) {
		return new TSPBoid(newWouldBePos, speed, this.visionRange, this.weightOfDistance, this.weightOfOccupancy,
				environment, goal, r);
	}
}
