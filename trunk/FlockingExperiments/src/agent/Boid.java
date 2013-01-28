package agent;

import graph.Edge;
import graph.FlockingGraph;
import graph.Position;
import graph.Segment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import util.SortableKeyValue;
import util.WeightedRouletteWheelSelector;

/**
 * Our explorational Bird-like object agent
 * 
 * @author Balthazar. Created Jan 22, 2013.
 */
public class Boid {
	public static final double MINIMUM_DISTANCE_MARGIN = 0.01d;

	private FlockingGraph graph;
	private Position pos;
	private Double speed;
	private Double visionRange;
	private Double traveledDistance; // Tiredness
	protected LinkedList<Integer> pathTaken;
	// private boolean isAchiever;
	private double distanceChoiceWeight;
	private double occupancyChoiceWeight;

	public Boid(Boid otherBoid) {
		this.graph = otherBoid.graph;
		this.pos = otherBoid.pos;
		this.speed = otherBoid.speed;
		this.visionRange = otherBoid.visionRange;
		this.occupancyChoiceWeight = otherBoid.distanceChoiceWeight;
		this.distanceChoiceWeight = otherBoid.distanceChoiceWeight;
		this.traveledDistance = 0d;
		this.pathTaken = new LinkedList<>();
	}

	public Boid(FlockingGraph graph, Position position, Double speed, Double visionRange, Double distanceChoiceWeight,
			Double occupancyChoiceWeight) {
		this.graph = graph;
		this.pos = position;
		this.speed = speed;
		this.visionRange = visionRange;
		this.occupancyChoiceWeight = occupancyChoiceWeight;
		this.distanceChoiceWeight = distanceChoiceWeight;
		this.traveledDistance = 0d;
		this.pathTaken = new LinkedList<>();
		// isAchiever = false;
	}

	public boolean completedTour() {
		return this.pathTaken.size() == this.graph.getNumberOfNodes();
	}

	public void die() {
		// TODO:
	}

	public FlockingGraph getGraph() {
		return this.graph;
	}

	public Position getPos() {
		return this.pos;
	}

	public Segment getSegment(Position pos) {
		return this.graph.getSegmentForPosition(pos);
	}

	public Double getSpeed() {
		return this.speed;
	}

	public void respawn() {
		this.setPosition(new Position(loadEdge(this.pathTaken.get(0), this.pathTaken.get(1)), 0d));
		this.pathTaken.clear();
	}

	public void setGraph(FlockingGraph graph) {
		this.graph = graph;
	}

	public void setPos(Position pos) {
		this.pos = pos;
	}

	public void setSpeed(Double speed) {
		this.speed = speed;
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

	protected void decide() {
		this.pathTaken.offer(this.pos.edge.getTo());

		ArrayList<Integer> closestNeighbors = this.graph.getClosestNeighborsSortedByDistance(this.pos.edge.getTo());
		for (int i : this.pathTaken) {
			closestNeighbors.remove(i);
		}

		if (closestNeighbors.isEmpty()) {
			if (completedTour()) {
				becomeAchiever();
			} else {
				die();
			}
		}

		List<Edge> possibleEdges = generateEdges(closestNeighbors);

		Edge nextEgde = selectNextEdge(possibleEdges);

		moveToNextEdge(nextEgde);

	}

	protected Edge loadEdge(int from, int to) {
		return this.graph.getEdge(from, to);
	}

	protected void moveToNextEdge(Edge edge) {
		double distanceWithin = this.pos.getDistanceToEdgeEnd();
		double distanceOnNext = getNextEdgeDistance(this.speed);

		moveDistance(distanceWithin);
		this.setPosition(new Position(edge, 0d));
		tryToMove(distanceOnNext);
	}

	private void becomeAchiever() {
		// this.isAchiever = true;
		// TODO: register in the main controller the new AchieverBoid created from this Boid
		AchieverBoid achiever = new AchieverBoid(this);
		achiever.respawn();
	}

	private boolean checkEdgeBoundaries(double distance) {
		return this.pos.canDeslocate(distance);
	}

	private boolean checkSegmentOccupation(Segment seg) {
		return !seg.isFull();
	}

	private List<Edge> generateEdges(ArrayList<Integer> closestNeighbors) {
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for (int neighbor : closestNeighbors) {
			edges.add(loadEdge(this.pos.edge.getTo(), neighbor));
		}

		return edges;
	}

	private double getNextEdgeDistance(double overallDistance) {
		return overallDistance - (this.pos.getDistanceToEdgeEnd());
	}

	private Double getPartialChoiceProbability(Edge edge) {
		Position p = new Position(edge, 0d);
		Segment firstSegment = getSegment(p);

		return Math.pow((firstSegment.maxOccupancy - firstSegment.currentOccupancy), this.occupancyChoiceWeight)
				* Math.pow(1d / edge.getLength(), this.distanceChoiceWeight);
	}

	private void moveDistance(double distance) {
		Segment currentSegment = getSegment(this.pos);
		currentSegment.currentOccupancy--;

		this.pos.deslocate(distance);

		Segment nextSegment = getSegment(this.pos);
		nextSegment.currentOccupancy++;

		this.traveledDistance += distance;
	}

	private void moveToFarthestAvailableLocation(Segment current, Segment limit) {
		Segment farthestAvailable = this.graph.getFarthestAvailableSegment(current, limit);
		double endOfTheSegment = farthestAvailable.exclusiveEndLocation.distanceFromStart;
		// get to right before the end of the segment
		double diff = endOfTheSegment - MINIMUM_DISTANCE_MARGIN - this.pos.distanceFromStart;
		moveDistance(diff);
	}

	private Edge selectNextEdge(List<Edge> possibleEdges) {
		List<SortableKeyValue<?, Double>> edgeProbabilityPairs = new ArrayList<SortableKeyValue<?, Double>>();
		Double totalSum = 0d;

		// calculate
		for (Edge e : possibleEdges) {
			Double probability = getPartialChoiceProbability(e);
			totalSum += probability;
			edgeProbabilityPairs.add(new SortableKeyValue<Edge, Double>(e, probability));
		}

		// normalize
		for (SortableKeyValue<?, Double> pair : edgeProbabilityPairs) {
			pair.valueToUseOnSorting = pair.valueToUseOnSorting / totalSum;
		}

		Collections.sort(edgeProbabilityPairs);

		WeightedRouletteWheelSelector selector = new WeightedRouletteWheelSelector(edgeProbabilityPairs);
		Edge e = (Edge) selector.getRandom().keyObject;

		return e;
	}

	private void setPosition(Position pos) {
		Segment currentSegment = getSegment(this.pos);
		currentSegment.currentOccupancy--;

		this.pos = pos;

		Segment nextSegment = getSegment(this.pos);
		nextSegment.currentOccupancy++;
	}

}
