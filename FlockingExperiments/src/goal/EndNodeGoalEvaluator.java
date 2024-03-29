package goal;

import graph.FlockingGraph;
import graph.Tour;

/**
 * Evaluates a solution for a shortest path problem that goes from 0 to goalNode.
 * 
 * @author Balthazar. Created Feb 3, 2013.
 */
public class EndNodeGoalEvaluator implements GoalEvaluator {

	/** The end node. */
	private int endNode;

	/**
	 * Instantiates a new end node goal evaluator.
	 * 
	 * @param goalNode
	 *            the goal node
	 */
	public EndNodeGoalEvaluator(int goalNode) {
		this.endNode = goalNode;
	}

	/**
	 * Checks if is goal.
	 * 
	 * @param graph
	 *            the graph
	 * @param pathTaken
	 *            the path taken
	 * @return true, if is goal
	 */
	@Override
	public boolean isGoal(FlockingGraph graph, Tour pathTaken) {
		return pathTaken.lastLocation() == this.endNode;
	}

}
