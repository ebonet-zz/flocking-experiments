package agent;

import goal.GoalEvaluator;
import graph.Edge;
import graph.Position;
import graph.Segment;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class WollowskiBoid extends Boid {

	double visibleBoidsInPathWeight;
	double numberOfChoicesWeight;

	public WollowskiBoid(Boid otherBoid) {
		super(otherBoid);
		this.visibleBoidsInPathWeight = this.occupancyChoiceWeight;
		this.numberOfChoicesWeight = this.distanceChoiceWeight;
	}

	public WollowskiBoid(Position position, Double speed, Double visionRange, Double distanceChoiceWeight,
			Double occupancyChoiceWeight, Environment enviroment, GoalEvaluator goalEvaluator, Random r) {
		super(position, speed, visionRange, distanceChoiceWeight, occupancyChoiceWeight, enviroment, goalEvaluator, r);
		this.visibleBoidsInPathWeight = this.occupancyChoiceWeight;
		this.numberOfChoicesWeight = this.distanceChoiceWeight;
	}

	@Override
	protected void decide() {
		boolean added = false;
		if (this.pathTaken.lastLocation() != this.pos.edge.getTo()) {
			this.pathTaken.offer(this.pos.edge.getTo());
			added = true;
		}

		if (this.goalEvaluator.isGoal(getGraph(), this.pathTaken)) {
			// if (checkSegmentOccupation(getSegment(new Position(loadEdge(this.pathTaken.get(0),
			// this.pathTaken.get(1)),
			// 0d)))) {
			// becomeAchiever();
			// } else {
			if (added) {
				this.pathTaken.locations.remove(this.pathTaken.locations.size() - 1);
				added = false;
			}
			// }
			return;
		}

		List<Edge> possibleEdges = generatePossibleNextEdges();

		if (possibleEdges.isEmpty()) {
			die();
			return;
		}

		Edge nextEgde = selectNextEdge(possibleEdges);

		moveToNextEdge(nextEgde);
	}

	@Override
	protected Double getPartialChoiceProbability(Edge edge, List<Edge> possibleEdges) {

		// Pruning when one edge is already full
//		for (Edge e : possibleEdges) {
//
//			if (getGraph().isEdgeFull(e)) {
//				if (!e.equals(edge)) {
//					return 0d;
//				} else {
//					return 1d;
//				}
//			}
//		}

		// No option is full, go probabilities
		Set<Boid> visibleBoidsInEdge = new HashSet<>();
		visibleBoidsInEdge.addAll(this.environment.getAllBoids());

		Iterator<Boid> it = visibleBoidsInEdge.iterator();
		while (it.hasNext()) {
			Boid b = it.next();
			if (!b.pos.edge.isSameEdge(edge) || !canSee(b)) {
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

	private LinkedList<Segment> getVisibleSegments(Edge edge, Double visibleDistance) {
		Position lastVisiblePosition = new Position(edge, visibleDistance);
		LinkedList<Segment> segmentsForEdge = this.getGraph().getSegmentsUpToPosition(lastVisiblePosition);
		return segmentsForEdge;
	}

	@Override
	protected boolean canSee(Boid b) {
		if (new Double(this.pos.getDistanceToEdgeEnd()).compareTo(this.visionRange) >= 0) {
			// Vision is completely inside the edge
			if (!b.pos.isSameEdge(this.pos)) {
				return false;
			}

			Double difference = b.getPos().getDistance() - this.pos.getDistance();

			if (difference.compareTo(0d) < 0) {
				return false;
			}

			return difference.compareTo(this.visionRange) <= 0;

		} else {
			// Vision exceeds edge
			if (b.pos.isSameEdge(this.pos)) {
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
					if (b.pos.edge.isSameEdge(e)) {
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
