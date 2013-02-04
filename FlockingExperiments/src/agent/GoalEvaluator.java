package agent;

import graph.FlockingGraph;
import graph.Tour;

/**
 * Functor interface definition for the many different goal evaluation strategies that may be implemented
 * 
 * @author Balthazar. Created Feb 3, 2013.
 */
public interface GoalEvaluator {

	public boolean isGoal(FlockingGraph graph, Tour pathTaken);
}
