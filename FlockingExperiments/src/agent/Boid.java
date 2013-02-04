package agent;

import graph.Edge;
import graph.FlockingGraph;
import graph.Position;
import graph.Segment;
import graph.Tour;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import util.SortableKeyValue;
import util.WeightedRouletteWheelSelector;
import controller.Environment;

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

	public Boid(Boid otherBoid) {
		this.environment = otherBoid.environment;
		this.pos = otherBoid.pos;
		this.speed = otherBoid.speed;
		this.visionRange = otherBoid.visionRange;
		this.occupancyChoiceWeight = otherBoid.distanceChoiceWeight;
		this.distanceChoiceWeight = otherBoid.distanceChoiceWeight;
		this.traveledDistance = otherBoid.traveledDistance;
		this.pathTaken = otherBoid.pathTaken;
		this.goalEvaluator = otherBoid.goalEvaluator;
		this.color = otherBoid.color;
	}

	public Boid(Position position, Double speed, Double visionRange, Double distanceChoiceWeight,
			Double occupancyChoiceWeight, Environment enviroment, GoalEvaluator goalEvaluator) {
		this.environment = enviroment;
		this.pos = position;
		this.speed = speed;
		this.visionRange = visionRange;
		this.occupancyChoiceWeight = occupancyChoiceWeight;
		this.distanceChoiceWeight = distanceChoiceWeight;
		this.traveledDistance = 0d;
		this.pathTaken = new Tour();

		this.pathTaken.offer(this.pos.edge.getFrom());
		this.environment.addNewBoid(this);

		this.goalEvaluator = goalEvaluator;

		// Random rand = new Random();
		//
		// float r = rand.nextFloat();
		// float g = rand.nextFloat();
		// float b = rand.nextFloat();
		//
		// // // Will produce only bright / light colours:
		// // float r = rand.nextFloat() / 2f + 0.5f;
		// // float g = rand.nextFloat() / 2f + 0.5f;
		// // float b = rand.nextFloat() / 2f + 0.5f;
		//
		// this.color = new Color(r, g, b);

		this.color = Color.GREEN;
	}

	public void die() {
		this.environment.boidDied(this);
	}

	public Color getColor() {
		return this.color;
	}

	public Environment getEnvironment() {
		return this.environment;
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
		if (this.pathTaken.lastLocation() != this.pos.edge.getTo())
			this.pathTaken.offer(this.pos.edge.getTo());

		if (this.goalEvaluator.isGoal(getGraph(), this.pathTaken)) {
			becomeAchiever();
			return;
		}

		ArrayList<Integer> closestNeighbors = getGraph().getClosestNeighborsSortedByDistance(this.pos.edge.getTo());
		for (int i : this.pathTaken.locations) {
			closestNeighbors.remove(new Integer(i));
		}

		if (closestNeighbors.isEmpty()) {
			die();
			return;
		}

		List<Edge> possibleEdges = generateEdges(closestNeighbors);

		Edge nextEgde = selectNextEdge(possibleEdges);

		moveToNextEdge(nextEgde);

	}

	protected boolean isInSight(Boid b) {
		return false;
	}

	protected Edge loadEdge(int from, int to) {
		return getGraph().getEdge(from, to);
	}

	protected void moveToNextEdge(Edge edge) {
		if (edge != null) {
			double distanceWithin = this.pos.getDistanceToEdgeEnd();
			double distanceOnNext = getNextEdgeDistance(this.speed);

			moveDistance(distanceWithin);
			this.setPosition(new Position(edge, 0d));
			tryToMove(distanceOnNext);
		}
	}

	protected void setPosition(Position pos) {
		Segment currentSegment = getSegment(this.pos);
		currentSegment.currentOccupancy--;

		this.pos = pos;

		Segment nextSegment = getSegment(this.pos);
		nextSegment.currentOccupancy++;
	}

	private void becomeAchiever() {
		this.environment.turnIntoAchiever(this);

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
		// TODO: Maybe update this to include all the visible segments?
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
		Segment farthestAvailable = getGraph().getFarthestAvailableSegment(current, limit);
		double endOfTheSegment = farthestAvailable.exclusiveEndLocation.distanceFromStart;
		// get to right before the end of the segment
		double diff = endOfTheSegment - MINIMUM_DISTANCE_MARGIN - this.pos.distanceFromStart;
		moveDistance(diff < this.speed ? diff : this.speed);
	}

	private Edge selectNextEdge(List<Edge> possibleEdges) {
		List<SortableKeyValue<?, Double>> edgeProbabilityPairs = new ArrayList<SortableKeyValue<?, Double>>();
		Double totalSum = 0d;

		// calculate
		for (Edge e : possibleEdges) {
			Double probability = getPartialChoiceProbability(e);
			if (probability.compareTo(new Double(0d)) > 0) {
				totalSum += probability;
				edgeProbabilityPairs.add(new SortableKeyValue<Edge, Double>(e, probability));
			}
		}

		if (totalSum.compareTo(new Double(0d)) <= 0) {
			return null;
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

}
