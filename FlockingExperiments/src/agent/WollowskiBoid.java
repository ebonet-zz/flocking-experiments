package agent;

import goal.GoalEvaluator;
import graph.Edge;
import graph.FlockingGraph;
import graph.Position;
import graph.Tour;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * The Class WollowskiBoid for simple flocking modeling.
 */
public class WollowskiBoid extends Boid {

	/** The cities checked as reached by the chain of agents. */
	public LinkedList<Integer> citiesChecked;

	/** Weight for the visible boids in path. */
	double visibleBoidsInPathWeight;

	/** Weight for the number of choices X. */
	double numberOfChoicesWeight;

	/**
	 * Instantiates a new wollowski boid out of another boid.
	 * 
	 * @param otherBoid
	 *            the other boid
	 */
	public WollowskiBoid(Boid otherBoid) {
		super(otherBoid);
		this.visibleBoidsInPathWeight = this.occupancyChoiceWeight;
		this.numberOfChoicesWeight = this.distanceChoiceWeight;
		this.citiesChecked = new LinkedList<Integer>();
	}

	/**
	 * Instantiates a new wollowski boid.
	 * 
	 * @param position
	 *            the position
	 * @param speed
	 *            the speed
	 * @param visionRange
	 *            the vision range
	 * @param numberOfChoicesWeight
	 *            the number of choices weight
	 * @param visibleBoidsInPathWeight
	 *            the visible boids in path weight
	 * @param enviroment
	 *            the enviroment
	 * @param goalEvaluator
	 *            the goal evaluator
	 * @param r
	 *            the r
	 */
	public WollowskiBoid(Position position, Double speed, Double visionRange, Double numberOfChoicesWeight,
			Double visibleBoidsInPathWeight, Environment enviroment, GoalEvaluator goalEvaluator, Random r) {
		super(position, speed, visionRange, numberOfChoicesWeight, visibleBoidsInPathWeight, enviroment, goalEvaluator,
				r);
		this.visibleBoidsInPathWeight = visibleBoidsInPathWeight;
		this.numberOfChoicesWeight = numberOfChoicesWeight;
		this.citiesChecked = new LinkedList<Integer>();
	}

	/**
	 * Try to move.
	 * 
	 * @param distance
	 *            the distance
	 */
	@Override
	public void tryToMove(double distance) {

		// 1) Check for other boids around
		Set<Boid> boidsInSight = getBoidsInSight();

		// 2) Compare and update the cities reached by the chain
		for (Boid boid : boidsInSight) {
			if (boid.pos.isStrictlySameEdge(this.pos)) {
				WollowskiBoid b = (WollowskiBoid) boid;
				if (!b.citiesChecked.equals(this.citiesChecked) && b.citiesChecked.contains(this.pos.getTo())) {
					this.citiesChecked.clear();
					this.citiesChecked.addAll(b.citiesChecked);
				}
			}
		}

		// 4) Move as before
		super.tryToMove(distance);
	}

	/**
	 * Tests if this boid can see the given boid. This is more strict than the common boid. This will not let you see
	 * boids in opposed directions.
	 * 
	 * @param b
	 *            the other boid
	 * @return true, if the other boid is visible for this boid
	 */
	@Override
	protected boolean canSee(Boid b) {
		if (new Double(this.pos.getDistanceToEdgeEnd()).compareTo(this.visionRange) >= 0) {
			// Vision is completely inside the edge
			if (!b.pos.isStrictlySameEdge(this.pos)) {
				return false;
			}
			Double difference = b.getPos().getDistanceFromStart() - this.pos.getDistanceFromStart();
			if (difference.compareTo(0d) < 0) {
				return false;
			}
			return difference.compareTo(this.visionRange) <= 0;
		} else {
			// Vision exceeds edge
			if (b.pos.isStrictlySameEdge(this.pos)) {
				Double difference = b.getPos().getDistanceFromStart() - this.pos.getDistanceFromStart();
				if (difference.compareTo(0d) >= 0) {
					return true;
				} else {
					return false;
				}
			} else {
				List<Edge> neighbors = generatePossibleNextEdges();
				Double visibleDistance = this.visionRange - this.pos.getDistanceToEdgeEnd();
				for (Edge e : neighbors) {
					if (b.pos.edge.isStrictlySameEdge(e)) {
						if (visibleDistance.compareTo(b.pos.distanceFromStart) >= 0) {
							return true;
						}
					}
				}
				return false;
			}
		}
	}

	/**
	 * Decide where to go next, what edge to take.
	 */
	@Override
	protected void decide() {
		boolean added = false; // was something added to my path?
		// test to see if I need to add the location I'm crossing needs to be added to my path
		if (this.pathTaken.lastLocation() != this.pos.edge.getTo()
				|| this.getGraph().getEdgeLength(this.pathTaken.lastLocation(), this.pathTaken.lastLocation()) != -1) {

			// i'm passing a new location in my next move, it should be in my path taken
			this.pathTaken.offer(this.pos.edge.getTo());
			added = true;
		}

		// am I going to achieve my goal?
		if (this.goalEvaluator.isGoal(getGraph(), this.pathTaken)) {

			// is it in my checked list yet?
			if (!this.citiesChecked.contains(this.pos.edge.getTo())) {
				this.citiesChecked.add(this.pos.edge.getTo());
			}

			// since I'm going to achieve my goal, its time to stop at the end of the edge. So I'm not gonna cross
			// anything.
			if (added) {
				this.pathTaken.locations.remove(this.pathTaken.locations.size() - 1);
				added = false;
			}

			// go for the end or how far you can
			super.tryToMove(this.pos.getDistanceToEdgeEnd() - FlockingGraph.MINIMUM_DISTANCE_MARGIN);
			return;
		}

		List<Edge> possibleEdges = generatePossibleNextEdges();

		// Update my list of checked cities from what I see nearby
		updateCheckedCities(possibleEdges);

		if (possibleEdges.isEmpty()) {
			die();
			return;
		}

		Edge nextEgde = selectNextEdge(possibleEdges);

		tryToMoveToNextEdge(nextEgde);
	}

	/**
	 * Gets the partial choice probability.
	 * 
	 * @param edge
	 *            the edge
	 * @param possibleEdges
	 *            the possible edges
	 * @return the partial choice probability
	 */
	@Override
	protected Double getPartialChoiceProbability(Edge edge, List<Edge> possibleEdges) {
		// count the boids visible in the next edge to use in the formula score
		Set<Boid> visibleBoidsInEdge = new HashSet<>();
		visibleBoidsInEdge.addAll(getBoidsInSight());

		Iterator<Boid> it = visibleBoidsInEdge.iterator();
		while (it.hasNext()) {
			Boid b = it.next();
			if (!b.pos.edge.isStrictlySameEdge(edge)) {
				it.remove();
			}
		}

		int totalEdgeChoices = possibleEdges.size();
		int visibleBoidsInThisChoice = visibleBoidsInEdge.size();

		// TODO: maybe this should consider distances to don't be so dumb
		Double probability = Math.pow(visibleBoidsInThisChoice, this.visibleBoidsInPathWeight)
				+ Math.pow(1d / totalEdgeChoices, this.numberOfChoicesWeight);

		return probability;
	}

	/**
	 * Gets the boids in sight.
	 * 
	 * @return the boids in sight
	 */
	private Set<Boid> getBoidsInSight() {
		Set<Boid> boidsInSight = new HashSet<>();

		for (Boid b : this.environment.getAllBoids()) {
			if (canSee(b)) {
				boidsInSight.add(b);
			}
		}

		boidsInSight.remove(this);
		return boidsInSight;
	}

	/**
	 * Update checked cities to determine what nodes have been met by the chain.
	 * 
	 * @param possibleEdges
	 *            the possible edges
	 */
	private void updateCheckedCities(List<Edge> possibleEdges) {
		Set<Boid> visibleBoids = getBoidsInSight();
		for (Edge e : possibleEdges) {
			// for each possible next edge
			if (getGraph().isEdgeFull(e)) {
				// if is a full edge
				Set<Boid> visibleBoidsInEdge = new HashSet<>();
				visibleBoidsInEdge.addAll(visibleBoids);

				Iterator<Boid> it = visibleBoidsInEdge.iterator();
				while (it.hasNext()) {
					Boid b = it.next();
					if (!b.pos.edge.isStrictlySameEdge(e)) {
						it.remove();
					}
				}
				// get the visible boids in this edge

				for (Boid boid : visibleBoidsInEdge) {
					// for each visible boid in this edge
					WollowskiBoid b = (WollowskiBoid) boid;
					// if the boid has a checked city that I dont have
					Iterator<Integer> i = b.citiesChecked.descendingIterator();
					Tour t = new Tour(this.pathTaken);
					while (i.hasNext()) {
						t.offer(i.next());
					}

					// if is all I need
					if (this.goalEvaluator.isGoal(getGraph(), t)) {
						// I mark this potential new city as checked
						this.citiesChecked.clear();
						this.citiesChecked.addAll(b.citiesChecked);
						this.citiesChecked.add(e.getFrom());
						// and chose this edge
						possibleEdges.clear();
						possibleEdges.add(e);
						return;
					}
				}
			}
		}
	}
}
