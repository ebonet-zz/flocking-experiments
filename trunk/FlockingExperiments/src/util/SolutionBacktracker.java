package util;

import goal.GoalEvaluator;
import graph.Edge;
import graph.Tour;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import agent.Environment;

public class SolutionBacktracker {
	Environment environment;
	GoalEvaluator goal;

	public SolutionBacktracker(Environment environment, GoalEvaluator goal) {
		this.environment = environment;
		this.goal = goal;
	}

	public Tour doBacktrack() {
		List<Tour> starts = root();
		if (starts.isEmpty()) {
			return null;
		}

		for (Tour t : starts) {
			Tour r = backtrack(t);
			if (r != null) {
				return r;
			}
		}

		return null;
	}

	private Tour backtrack(Tour c) {
		if (reject(c)) {
			return null;
		}
		if (accept(c)) {
			return c;
		}

		Tour s = firstCandidateOutOf(c);
		while (s != null) {
			Tour b = backtrack(s);
			if (b != null) {
				return b;
			}

			s = nextCandidateAfter(s);
		}
		return null;
	}

	private List<Tour> root() {
		List<Tour> r = new ArrayList<>();
		List<Integer> candidates = getFullNeighborsOf(0);
		for (Integer i : candidates) {
			Tour c = new Tour();
			c.offer(0);
			c.offer(i);
			r.add(c);
		}
		return r;
	}

	private boolean reject(Tour t) {

		if (t.size() >= 4) {
			if (t.lastLocation() == t.locations.get(t.locations.size() - 3)) {
				if (t.locations.get(t.locations.size() - 2) == t.locations.size() - 4) {
					return true;
				}
			}
		}

		for (Integer l : t.locations) {
			int firstAppearance = t.locations.indexOf(l);
			int lastAppearance = t.locations.lastIndexOf(l);
			if (firstAppearance != lastAppearance) {
				// appears more than once
				if (lastAppearance < t.locations.size() - 1) {
					// not in the end
					int lastSuccessor = t.locations.get(lastAppearance + 1);

					for (int i = lastAppearance - 1; i > 0; i--) {
						if (t.locations.get(i) == lastSuccessor && t.locations.get(i - 1) == l) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	private Tour firstCandidateOutOf(Tour t) {
		Integer i = t.lastLocation();

		List<Integer> neighbors = getFullNeighborsOf(i);

		// repeated to the end
		List<Integer> repeated = new ArrayList<Integer>();
		for (Integer l : t.locations) {
			if (neighbors.contains(l)) {
				repeated.add(l);
				neighbors.remove(l);
			}
		}
		neighbors.addAll(repeated);

		if (neighbors.isEmpty()) {
			return null;
		}

		Tour f = new Tour(t);
		f.offer(neighbors.get(0));

		return f;
	}

	private Tour nextCandidateAfter(Tour t) {
		Integer i = t.lastLocation();
		Integer father = t.locations.get(t.locations.size() - 2);

		List<Integer> neighbors = getFullNeighborsOf(father);

		// repeated to the end
		List<Integer> repeated = new ArrayList<Integer>();
		for (Integer l : t.locations) {
			if (neighbors.contains(l)) {
				repeated.add(l);
				neighbors.remove(l);
			}
		}
		neighbors.addAll(repeated);

		if (neighbors.isEmpty()) {
			return null;
		}

		int indexOfLast = neighbors.indexOf(i);

		if (indexOfLast == neighbors.size() - 1) {
			return null;
		}

		Tour f = new Tour(t);
		f.locations.remove(f.locations.size() - 1);

		f.offer(neighbors.get(indexOfLast + 1));

		return f;
	}

	private boolean accept(Tour t) {
		return this.goal.isGoal(environment.getFlockingGraph(), t);
	}

	private List<Integer> getFullNeighborsOf(Integer from) {
		List<Integer> neighbors = this.environment.getFlockingGraph().getClosestNeighborsSortedByDistance(from);
		Iterator<Integer> it = neighbors.iterator();

		while (it.hasNext()) {
			Integer n = it.next();
			Edge e = this.environment.getFlockingGraph().getEdge(from, n);
			if (!this.environment.getFlockingGraph().isEdgeFull(e)) {
				it.remove();
			}
		}

		return neighbors;
	}
}
