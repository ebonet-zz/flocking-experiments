package agent;

import graph.FlockingGraph;
import graph.Tour;

/**
 * TODO Put here a description of what this class does.
 * 
 * @author Balthazar. Created Feb 3, 2013.
 */
public class EndNodeGoalEvaluator implements GoalEvaluator {

	private int endNode;

	public EndNodeGoalEvaluator(int goalNode) {
		this.endNode = goalNode;
	}

	@Override
	public boolean isGoal(FlockingGraph graph, Tour pathTaken) {
		return pathTaken.lastLocation() == endNode;
	}

}
