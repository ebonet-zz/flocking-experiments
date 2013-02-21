package goal;

import graph.FlockingGraph;
import graph.Tour;

/**
 * Evaluates a solution for a TSP problem.
 * 
 * @author Balthazar. Created Feb 3, 2013.
 */
public class TSPGoalEvaluator implements GoalEvaluator {

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
		if (completedTour(graph, pathTaken))
			return true;
		return false;
	}

	/**
	 * Tests for a completed tour.
	 * 
	 * @param graph
	 *            the graph
	 * @param pathTaken
	 *            the path taken
	 * @return true, if complete TSP tour
	 */
	public boolean completedTour(FlockingGraph graph, Tour pathTaken) {
		if (!pathTaken.firstLocation().equals(pathTaken.lastLocation())) {
			return false;
		}
		for (int i = 0; i < graph.getNumberOfNodes(); i++) {
			if (!pathTaken.locations.contains(new Integer(i))) {
				return false;
			}
		}

		return true;
	}
}
