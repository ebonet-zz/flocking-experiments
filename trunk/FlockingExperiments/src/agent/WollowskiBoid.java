package agent;

import goal.GoalEvaluator;
import graph.Edge;
import graph.Position;
import graph.Tour;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class WollowskiBoid extends Boid {

	public LinkedList<Integer> citiesChecked;
	double visibleBoidsInPathWeight;
	double numberOfChoicesWeight;

	public WollowskiBoid(Boid otherBoid) {
		super(otherBoid);
		this.visibleBoidsInPathWeight = this.occupancyChoiceWeight;
		this.numberOfChoicesWeight = this.distanceChoiceWeight;
		this.citiesChecked = new LinkedList<Integer>();
	}

	public WollowskiBoid(Position position, Double speed, Double visionRange, Double distanceChoiceWeight,
			Double occupancyChoiceWeight, Environment enviroment, GoalEvaluator goalEvaluator, Random r) {
		super(position, speed, visionRange, distanceChoiceWeight, occupancyChoiceWeight, enviroment, goalEvaluator, r);
		this.visibleBoidsInPathWeight = this.occupancyChoiceWeight;
		this.numberOfChoicesWeight = this.distanceChoiceWeight;
		this.citiesChecked = new LinkedList<Integer>();
	}

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

	@Override
	public void tryToMove(double distance) {

		// 1) Check for other boids around
		Set<Boid> boidsInSight = getBoidsInSight();

		// 2) Compare
		for (Boid boid : boidsInSight) {
			if (boid.pos.isStrictlySameEdge(this.pos)) {
				WollowskiBoid b = (WollowskiBoid) boid;
				if (b.citiesChecked.contains(this.pos.getTo())) {
					this.citiesChecked.clear();
					this.citiesChecked.addAll(b.citiesChecked);
				}
			}
		}

		// 4) Move as before
		super.tryToMove(distance);
	}

	@Override
	protected void decide() {
		boolean added = false;
		if (this.pathTaken.lastLocation() != this.pos.edge.getTo()
				|| this.getGraph().getEdgeLength(this.pathTaken.lastLocation(), this.pathTaken.lastLocation()) != -1) {
			this.pathTaken.offer(this.pos.edge.getTo());
			added = true;
		}

		if (this.goalEvaluator.isGoal(getGraph(), this.pathTaken)) {

			if (!this.citiesChecked.contains(this.pos.edge.getTo())) {
				this.citiesChecked.add(this.pos.edge.getTo());
			}

			if (added) {
				this.pathTaken.locations.remove(this.pathTaken.locations.size() - 1);
				added = false;
			}

			super.tryToMove(this.pos.getDistanceToEdgeEnd() - MINIMUM_DISTANCE_MARGIN);
			return;
		}

		List<Edge> possibleEdges = generatePossibleNextEdges();

		updateCheckedCities(possibleEdges);

		if (possibleEdges.isEmpty()) {
			die();
			return;
		}

		Edge nextEgde = selectNextEdge(possibleEdges);

		moveToNextEdge(nextEgde);
	}

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

	@Override
	protected Double getPartialChoiceProbability(Edge edge, List<Edge> possibleEdges) {

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

	@Override
	protected boolean canSee(Boid b) {
		if (new Double(this.pos.getDistanceToEdgeEnd()).compareTo(this.visionRange) >= 0) {
			// Vision is completely inside the edge
			if (!b.pos.isStrictlySameEdge(this.pos)) {
				return false;
			}

			Double difference = b.getPos().getDistance() - this.pos.getDistance();

			if (difference.compareTo(0d) < 0) {
				return false;
			}

			return difference.compareTo(this.visionRange) <= 0;

		} else {
			// Vision exceeds edge
			if (b.pos.isStrictlySameEdge(this.pos)) {
				Double difference = b.getPos().getDistance() - this.pos.getDistance();

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

}
