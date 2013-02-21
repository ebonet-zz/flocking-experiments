package goal;

import graph.FlockingGraph;
import graph.Tour;

/**
 * Functor interface definition for the many different goal evaluation strategies that may be implemented.
 * 
 * @author Balthazar. Created Feb 3, 2013.
 */
public interface GoalEvaluator {

	/**
	 * Checks if is goal.
	 * 
	 * @param graph
	 *            the graph
	 * @param pathTaken
	 *            the path taken
	 * @return true, if is goal
	 */
	public boolean isGoal(FlockingGraph graph, Tour pathTaken);
}
