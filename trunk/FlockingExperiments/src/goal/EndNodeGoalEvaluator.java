package goal;

import graph.FlockingGraph;
import graph.Tour;

/**
 * Evaluates a solution for a shortest path problem that goes from 0 to goalNode
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
		return pathTaken.lastLocation() == this.endNode;
	}

}
