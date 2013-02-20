package agent;

import goal.GoalEvaluator;
import graph.Edge;
import graph.FlockingGraph;
import graph.Position;
import graph.Segment;
import graph.Tour;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import util.SortableKeyValue;
import util.WeightedRouletteWheelSelector;

/**
 * Our explorational Bird-like object agent
 * 
 * @author Balthazar. Created Jan 22, 2013.
 */
public class Boid {
	public static final double MINIMUM_DISTANCE_MARGIN = 0.01d;

	protected Environment environment;
	protected Position pos;
	protected Double speed;
	protected Double visionRange;
	protected Double traveledDistance; // Tiredness
	protected Tour pathTaken;
	protected Color color;
	protected double distanceChoiceWeight;
	protected double occupancyChoiceWeight;
	protected GoalEvaluator goalEvaluator;
	protected Random rand;

	public Boid(Boid otherBoid) {
		this.pos = otherBoid.pos;
		this.speed = otherBoid.speed;
		this.visionRange = otherBoid.visionRange;
		this.distanceChoiceWeight = otherBoid.distanceChoiceWeight;
		this.occupancyChoiceWeight = otherBoid.occupancyChoiceWeight;
		this.environment = otherBoid.environment;
		this.goalEvaluator = otherBoid.goalEvaluator;

		this.traveledDistance = otherBoid.traveledDistance;
		this.pathTaken = otherBoid.pathTaken;
		this.color = otherBoid.color;
		this.rand = otherBoid.rand;

		Segment segment = getSegment(this.pos);
		segment.incrementOccupancy();
	}

	public Boid(Position position, Double speed, Double visionRange, Double distanceChoiceWeight,
			Double occupancyChoiceWeight, Environment enviroment, GoalEvaluator goalEvaluator, Random r) {
		this.pos = position;
		this.speed = speed;
		this.visionRange = visionRange;
		this.distanceChoiceWeight = distanceChoiceWeight;
		this.occupancyChoiceWeight = occupancyChoiceWeight;
		this.environment = enviroment;
		this.goalEvaluator = goalEvaluator;

		this.traveledDistance = 0d;
		this.pathTaken = new Tour();
		this.pathTaken.offer(this.pos.edge.getFrom());
		this.color = Color.GREEN;
		this.rand = r;

		this.environment.addNewFreeBoid(this);
		Segment segment = getSegment(this.pos);
		segment.incrementOccupancy();
	}

	public void die() {
		Segment segment = getSegment(this.pos);
		segment.decrementOccupancy();
		this.environment.boidDied(this);
	}

	public Color getColor() {
		return this.color;
	}

	public FlockingGraph getGraph() {
		return this.environment.getFlockingGraph();
	}

	public Double getPathDistance() {
		return Double.MAX_VALUE;
	}

	public Position getPos() {
		return this.pos;
	}

	public Segment getSegment(Position pos) {
		return getGraph().getSegmentForPosition(pos);
	}

	public Double getSpeed() {
		return this.speed;
	}

	public void tryToMove(double distance) {
		if (checkEdgeBoundaries(distance)) {
			Position newWouldBePos = this.pos.clone();
			newWouldBePos.deslocate(distance);
			Segment nextWouldBeSegment = getSegment(newWouldBePos);
			if (checkSegmentOccupation(nextWouldBeSegment)) {
				moveDistance(distance);
			} else {
				moveToFarthestAvailableLocation(getSegment(this.pos), nextWouldBeSegment);
			}
		} else {
			decide();
		}
	}

	protected boolean canSee(Boid b) {
		return false;
	}

	protected boolean checkSegmentOccupation(Segment seg) {
		return !seg.isFull();
	}

	protected void decide() {
		boolean added = false;
		if (this.pathTaken.lastLocation() != this.pos.edge.getTo()) {
			this.pathTaken.offer(this.pos.edge.getTo());
			added = true;
		}

		if (this.goalEvaluator.isGoal(getGraph(), this.pathTaken)) {
			if (checkSegmentOccupation(getSegment(new Position(loadEdge(this.pathTaken.get(0), this.pathTaken.get(1)),
					0d)))) {
				becomeAchiever();
			} else {
				if (added) {
					this.pathTaken.locations.remove(this.pathTaken.locations.size() - 1);
					added = false;
				}
			}
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

	protected List<Edge> generateEdges(ArrayList<Integer> closestNeighbors) {
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for (int neighbor : closestNeighbors) {
			edges.add(loadEdge(this.pos.edge.getTo(), neighbor));
		}

		return edges;
	}

	protected List<Edge> generatePossibleNextEdges() {
		ArrayList<Integer> closestNeighbors = getGraph().getClosestNeighborsSortedByDistance(this.pos.edge.getTo());

		if (!closestNeighbors.isEmpty()) {
			ArrayList<Integer> closestUnvisitedNeighbors = new ArrayList<>();
			closestUnvisitedNeighbors.addAll(closestNeighbors);

			for (int i : this.pathTaken.locations) {
				closestUnvisitedNeighbors.remove(new Integer(i));
			}

			if (!closestUnvisitedNeighbors.isEmpty()) {
				closestNeighbors = closestUnvisitedNeighbors;
			} else {
				boolean gotAllCities = true;
				for (int i = 0; i < this.getGraph().getNumberOfNodes(); i++) {
					if (!this.pathTaken.locations.contains(new Integer(i))) {
						gotAllCities = false;
						break;
					}
				}
				if (gotAllCities) {
					Integer startNode = this.pathTaken.firstLocation();
					if (closestNeighbors.contains(startNode)) {
						closestNeighbors.clear();
						closestNeighbors.add(startNode);
					}
				}
			}
		}

		List<Edge> possibleEdges = generateEdges(closestNeighbors);
		return possibleEdges;
	}

	protected Edge loadEdge(int from, int to) {
		return getGraph().getEdge(from, to);
	}

	protected void moveToNextEdge(Edge edge) {
		if (edge != null) {
			double distanceWithin = this.pos.getDistanceToEdgeEnd();
			double distanceOnNext = getNextEdgeDistance(this.speed);

			if (distanceOnNext > edge.getLength()) {
				distanceOnNext = edge.getLength();
			}

			Position minNextEdgePosition = new Position(edge, 0d);
			Position maxNextEdgePosition = new Position(edge, distanceOnNext);

			Segment minSegmentNext = getSegment(minNextEdgePosition);
			Segment maxSegmentNext = getSegment(maxNextEdgePosition);

			Segment farthestAvailableOnNext = getGraph().getFarthestAvailableSegment(minSegmentNext, maxSegmentNext);

			if (farthestAvailableOnNext.isFull()) {
				this.pathTaken.locations.remove(this.pathTaken.locations.size() - 1);

				Position minEdgePosition = this.pos;
				Position maxEdgePosition = new Position(this.pos.edge, this.pos.distanceFromStart + distanceWithin
						- MINIMUM_DISTANCE_MARGIN);

				Segment minSegmentHere = getSegment(minEdgePosition);
				Segment maxSegmentHere = getSegment(maxEdgePosition);

				moveToFarthestAvailableLocation(minSegmentHere, maxSegmentHere);
			} else {
				if (distanceOnNext >= farthestAvailableOnNext.exclusiveEndLocation.distanceFromStart) {
					double distance = farthestAvailableOnNext.exclusiveEndLocation.distanceFromStart
							- MINIMUM_DISTANCE_MARGIN;
					this.traveledDistance += distance;
					this.setPosition(new Position(edge, distance));
				} else {
					this.traveledDistance += distanceOnNext;
					this.setPosition(new Position(edge, distanceOnNext));
				}
			}
		}
	}

	protected void setPosition(Position pos) {
		Segment currentSegment = getSegment(this.pos);
		currentSegment.decrementOccupancy();

		this.pos = pos;

		Segment nextSegment = getSegment(this.pos);
		nextSegment.incrementOccupancy();
	}

	private void becomeAchiever() {
		this.environment.turnIntoAchiever(this);

	}

	private boolean checkEdgeBoundaries(double distance) {
		return this.pos.canDeslocate(distance);
	}

	private double getNextEdgeDistance(double overallDistance) {
		return overallDistance - (this.pos.getDistanceToEdgeEnd());
	}

	protected Double getPartialChoiceProbability(Edge edge, List<Edge> possibleEdges) {
		// TODO: Maybe update this to include all the visible segments?
		Position p = new Position(edge, 0d);
		Segment firstSegment = getSegment(p);

		Double probability = Math.pow((firstSegment.maxOccupancy - firstSegment.getCurrentOccupancy()),
				this.occupancyChoiceWeight) * Math.pow(1d / edge.getLength(), this.distanceChoiceWeight);
		return probability;
	}

	private void moveDistance(double distance) {
		Segment currentSegment = getSegment(this.pos);
		currentSegment.decrementOccupancy();

		this.pos.deslocate(distance);
		if (this.pos.distanceFromStart > this.pos.edge.getLength()) {
			throw new RuntimeException("Deslocate limit error");
		}

		Segment nextSegment = getSegment(this.pos);
		nextSegment.incrementOccupancy();

		this.traveledDistance += distance;
	}

	private void moveToFarthestAvailableLocation(Segment current, Segment limit) {
		Segment farthestAvailable = getGraph().getFarthestAvailableSegment(current, limit);
		double endOfTheSegment = farthestAvailable.exclusiveEndLocation.distanceFromStart;
		// get to right before the end of the segment
		double diff = endOfTheSegment - MINIMUM_DISTANCE_MARGIN - this.pos.distanceFromStart;
		if (diff < -0.1d) {
			throw new RuntimeException("Walking backwards");
		}
		double distance = diff < this.speed ? diff : this.speed;
		if (distance > 0d) {
			moveDistance(distance);
		}
	}

	protected Edge selectNextEdge(List<Edge> possibleEdges) {
		List<SortableKeyValue<?, Double>> edgeProbabilityPairs = new ArrayList<SortableKeyValue<?, Double>>();
		Double totalSum = 0d;

		// calculate
		for (Edge e : possibleEdges) {
			Double probability = getPartialChoiceProbability(e, possibleEdges);
			if (probability.compareTo(0d) > 0) {
				totalSum += probability;
				edgeProbabilityPairs.add(new SortableKeyValue<Edge, Double>(e, probability));
			}
		}

		if (totalSum.compareTo(0d) <= 0) {
			return null;
		}

		// normalize
		for (SortableKeyValue<?, Double> pair : edgeProbabilityPairs) {
			pair.valueToUseOnSorting = pair.valueToUseOnSorting / totalSum;
		}

		Collections.sort(edgeProbabilityPairs);

		WeightedRouletteWheelSelector selector = new WeightedRouletteWheelSelector(edgeProbabilityPairs, this.rand);
		Edge e = (Edge) selector.getRandom().keyObject;

		return e;
	}

}
