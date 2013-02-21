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
 * Our explorational Bird-like object agent.
 * 
 * @author Balthazar. Created Jan 22, 2013.
 */
public class Boid {

	/** The environment where this boid exists. */
	protected Environment environment;

	/** The position of the boid. */
	protected Position pos;

	/** The speed of the boid. */
	protected Double speed;

	/** The vision range. */
	protected Double visionRange;

	/** The traveled distance until now. */
	protected Double traveledDistance; // Tiredness

	/** The path taken up to here. */
	protected Tour pathTaken;

	/** The color, for GUI. */
	protected Color color;

	/** The distance choice weight. */
	protected double distanceChoiceWeight;

	/** The occupancy choice weight. */
	protected double occupancyChoiceWeight;

	/** The goal evaluator. */
	protected GoalEvaluator goalEvaluator;

	/** The random instance. */
	protected Random rand;

	/**
	 * Instantiates a new boid out of other boid, copying the attributes.
	 * 
	 * @param otherBoid
	 *            the other boid
	 */
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

		// Remember to increase occupancy where I appeared
		Segment segment = getSegmentFor(this.pos);
		segment.incrementOccupancy();
	}

	/**
	 * Instantiates a new boid.
	 * 
	 * @param position
	 *            the position
	 * @param speed
	 *            the speed
	 * @param visionRange
	 *            the vision range
	 * @param distanceChoiceWeight
	 *            the distance choice weight
	 * @param occupancyChoiceWeight
	 *            the occupancy choice weight
	 * @param environment
	 *            the environment
	 * @param goalEvaluator
	 *            the goal evaluator
	 * @param r
	 *            the random instance
	 */
	public Boid(Position position, Double speed, Double visionRange, Double distanceChoiceWeight,
			Double occupancyChoiceWeight, Environment environment, GoalEvaluator goalEvaluator, Random r) {
		this.pos = position;
		this.speed = speed;
		this.visionRange = visionRange;
		this.distanceChoiceWeight = distanceChoiceWeight;
		this.occupancyChoiceWeight = occupancyChoiceWeight;
		this.environment = environment;
		this.goalEvaluator = goalEvaluator;

		this.traveledDistance = 0d;
		this.pathTaken = new Tour();
		this.pathTaken.offer(this.pos.edge.getFrom());
		this.color = Color.GREEN;
		this.rand = r;

		// Register myself in the environment as a new free boid (explorer)
		this.environment.addNewFreeBoid(this);

		// Remember to increase occupancy where I appeared
		Segment segment = getSegmentFor(this.pos);
		segment.incrementOccupancy();
	}

	/**
	 * Die, kills the boid.
	 */
	public void die() {
		// decrease occupancy where I was
		Segment segment = getSegmentFor(this.pos);
		segment.decrementOccupancy();

		// let the environment know I no longer exist
		this.environment.boidDied(this);
	}

	/**
	 * Gets the color.
	 * 
	 * @return the color
	 */
	public Color getColor() {
		return this.color;
	}

	/**
	 * Gets the graph.
	 * 
	 * @return the graph
	 */
	public FlockingGraph getGraph() {
		return this.environment.getFlockingGraph();
	}

	/**
	 * Gets the path distance.
	 * 
	 * @return the path distance
	 */
	public Double getPathDistance() {
		return Double.MAX_VALUE;
	}

	/**
	 * Gets the position.
	 * 
	 * @return the position
	 */
	public Position getPos() {
		return this.pos;
	}

	/**
	 * Gets the segment for this position.
	 * 
	 * @param position
	 *            the position
	 * @return the segment
	 */
	public Segment getSegmentFor(Position position) {
		return getGraph().getSegmentForPosition(position);
	}

	/**
	 * Gets the speed.
	 * 
	 * @return the speed
	 */
	public Double getSpeed() {
		return this.speed;
	}

	/**
	 * Try to move, if you can.
	 * 
	 * @param distance
	 *            the distance
	 */
	public void tryToMove(double distance) {
		// check if you are going to cross the edge
		if (checkEdgeBoundaries(distance)) {
			// nope, you will move entirely inside the current edge
			Position newWouldBePos = this.pos.clone();
			newWouldBePos.dislocate(distance);
			Segment nextWouldBeSegment = getSegmentFor(newWouldBePos);
			// check if you have space to move full speed
			if (checkSegmentOccupation(nextWouldBeSegment)) {
				// free space, just move
				moveDistance(distance);
			} else {
				// occupied, go as far as you can within your speed
				moveToFarthestAvailableLocation(getSegmentFor(this.pos), nextWouldBeSegment);
			}
		} else {
			// crossing edges, where should you go?
			decide();
		}
	}

	/**
	 * Tests if this boid can see the given boid.
	 * 
	 * @param b
	 *            the other boid
	 * @return true, if the other boid is visible for this boid
	 */
	protected boolean canSee(Boid b) {
		// as an explorer, I don't care for anybody yet
		return false;
	}

	/**
	 * Check segment occupation.
	 * 
	 * @param seg
	 *            the segment
	 * @return true, if not full yet (aka has space for me)
	 */
	protected boolean checkSegmentOccupation(Segment seg) {
		return !seg.isFull();
	}

	/**
	 * Decide where to go next, what edge to take.
	 */
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
			// yes, goal is going to be achieved. Do I have space to respawn as an achiever?
			if (checkSegmentOccupation(getSegmentFor(new Position(
					loadEdge(this.pathTaken.get(0), this.pathTaken.get(1)), 0d)))) {
				becomeAchiever();
			} else {
				// no space, I'm not achieving yet. Lets remove the added location since it is left for next attempt.
				if (added) {
					this.pathTaken.locations.remove(this.pathTaken.locations.size() - 1);
					added = false;
				}
			}
			// either respawning or waiting, i'm done for this iteration
			return;
		}

		List<Edge> possibleEdges = generatePossibleNextEdges();

		if (possibleEdges.isEmpty()) {
			// nowhere to go, dead end
			die();
			return;
		}

		Edge nextEgde = selectNextEdge(possibleEdges);

		tryToMoveToNextEdge(nextEgde);
	}

	/**
	 * Generate edges out of the neighbor list.
	 * 
	 * @param closestNeighbors
	 *            the closest neighbors
	 * @return the edge list
	 */
	protected List<Edge> generateEdges(ArrayList<Integer> closestNeighbors) {
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for (int neighbor : closestNeighbors) {
			edges.add(loadEdge(this.pos.edge.getTo(), neighbor));
		}

		return edges;
	}

	/**
	 * Generate possible next edges.
	 * 
	 * @return the list
	 */
	protected List<Edge> generatePossibleNextEdges() {
		// get neighbors
		ArrayList<Integer> closestNeighbors = getGraph().getClosestNeighborsSortedByDistance(this.pos.edge.getTo());

		if (!closestNeighbors.isEmpty()) {
			// I have neighbors, but lets prioritize the unvisited ones
			ArrayList<Integer> closestUnvisitedNeighbors = new ArrayList<>();
			closestUnvisitedNeighbors.addAll(closestNeighbors);

			for (int i : this.pathTaken.locations) {
				closestUnvisitedNeighbors.remove(new Integer(i));
			}

			// is any neighbor still unvisited?
			if (!closestUnvisitedNeighbors.isEmpty()) {
				// yes, prioritize them
				closestNeighbors = closestUnvisitedNeighbors;
			} else {
				// my neighbors were all visited. Did I visit everywhere?
				boolean gotAllCities = true;
				for (int i = 0; i < this.getGraph().getNumberOfNodes(); i++) {
					if (!this.pathTaken.locations.contains(new Integer(i))) {
						gotAllCities = false;
						break;
					}
				}

				// If I have been everywhere, I should return to the start (TSP)
				if (gotAllCities) {
					Integer startNode = this.pathTaken.firstLocation();
					// is there any link from here to the start?
					if (closestNeighbors.contains(startNode)) {
						// yes, lets go there for sure
						closestNeighbors.clear();
						closestNeighbors.add(startNode);
					}
					// otherwise don't touch, let it go wherever it can
				}
			}
		}

		List<Edge> possibleEdges = generateEdges(closestNeighbors);
		return possibleEdges;
	}

	/**
	 * Gets the partial choice probability for the given edge.
	 * 
	 * @param edge
	 *            the edge
	 * @param possibleEdges
	 *            the possible edges
	 * @return the partial choice probability
	 */
	protected Double getPartialChoiceProbability(Edge edge, List<Edge> possibleEdges) {
		// TODO: Maybe update this to include all the visible segments?

		// check occupation values for the first segment of this edge
		Position p = new Position(edge, 0d);
		Segment firstSegment = getSegmentFor(p);

		// formula
		Double probability = Math.pow((firstSegment.maxOccupancy - firstSegment.getCurrentOccupancy()),
				this.occupancyChoiceWeight) * Math.pow(1d / edge.getLength(), this.distanceChoiceWeight);
		return probability;
	}

	/**
	 * Load an edge object from the indexes.
	 * 
	 * @param from
	 *            the from node
	 * @param to
	 *            the to node
	 * @return the edge
	 */
	protected Edge loadEdge(int from, int to) {
		return getGraph().getEdge(from, to);
	}

	/**
	 * Select next edge.
	 * 
	 * @param possibleEdges
	 *            the possible edges
	 * @return the edge
	 */
	protected Edge selectNextEdge(List<Edge> possibleEdges) {
		List<SortableKeyValue<?, Double>> edgeProbabilityPairs = new ArrayList<SortableKeyValue<?, Double>>();
		Double totalSum = 0d;

		// calculate edge probabilities
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

		// normalize the probabilities
		for (SortableKeyValue<?, Double> pair : edgeProbabilityPairs) {
			pair.valueToUseOnSorting = pair.valueToUseOnSorting / totalSum;
		}

		// sort and pick one
		Collections.sort(edgeProbabilityPairs);

		WeightedRouletteWheelSelector selector = new WeightedRouletteWheelSelector(edgeProbabilityPairs, this.rand);
		Edge e = (Edge) selector.getRandom().keyObject;

		return e;
	}

	/**
	 * Sets the position to the given one, handling the occupancy changes.
	 * 
	 * @param pos
	 *            the new position
	 */
	protected void setPosition(Position pos) {
		Segment currentSegment = getSegmentFor(this.pos);
		currentSegment.decrementOccupancy();

		this.pos = pos;

		Segment nextSegment = getSegmentFor(this.pos);
		nextSegment.incrementOccupancy();
	}

	/**
	 * Try to move to next edge.
	 * 
	 * @param edge
	 *            the edge
	 */
	protected void tryToMoveToNextEdge(Edge edge) {
		if (edge != null) {
			// how much are you gonna walk inside your current edge and how far will you reach after?
			double distanceWithin = this.pos.getDistanceToEdgeEnd();
			double distanceOnNext = getNextEdgeDistance(this.speed);

			if (distanceOnNext > edge.getLength()) {
				// you can only go as far as the edge
				distanceOnNext = edge.getLength();
			}

			// set your movement range for the next edge, depending on the occupation only
			Position minNextEdgePosition = new Position(edge, 0d);
			Position maxNextEdgePosition = new Position(edge, distanceOnNext);

			Segment minSegmentNext = getSegmentFor(minNextEdgePosition);
			Segment maxSegmentNext = getSegmentFor(maxNextEdgePosition);

			// what is the farthest segment you can reach and is available?
			Segment farthestAvailableOnNext = getGraph().getFarthestAvailableSegment(minSegmentNext, maxSegmentNext);

			// if even the segment returned is null, you have no space on the next edge
			if (farthestAvailableOnNext.isFull()) {
				// you won't cross now, remove the location from you path since you will move inside only
				this.pathTaken.locations.remove(this.pathTaken.locations.size() - 1);

				// move as far as you can inside
				Position minEdgePosition = this.pos;
				Position maxEdgePosition = new Position(this.pos.edge, this.pos.distanceFromStart + distanceWithin
						- FlockingGraph.MINIMUM_DISTANCE_MARGIN);

				if (minEdgePosition.isAfterOrEqual(maxEdgePosition)) {
					// if you are already in the limit of the edge, don't get lost within precision cases, just stay
					maxEdgePosition = minEdgePosition;
				}

				Segment minSegmentHere = getSegmentFor(minEdgePosition);
				Segment maxSegmentHere = getSegmentFor(maxEdgePosition);

				// move inside
				moveToFarthestAvailableLocation(minSegmentHere, maxSegmentHere);
			} else {
				// you can move to the next edge, there is space somewhere
				if (distanceOnNext >= farthestAvailableOnNext.exclusiveEndLocation.distanceFromStart) {
					// your potential movement is greater than the farthest available position, you need to move less
					double distance = farthestAvailableOnNext.exclusiveEndLocation.distanceFromStart
							- FlockingGraph.MINIMUM_DISTANCE_MARGIN;
					this.traveledDistance += distance;
					this.setPosition(new Position(edge, distance));
				} else {
					// your potential movement is acceptable, move
					this.traveledDistance += distanceOnNext;
					this.setPosition(new Position(edge, distanceOnNext));
				}
			}
		}
	}

	/**
	 * Become achiever, register in the environment.
	 */
	private void becomeAchiever() {
		this.environment.turnIntoAchiever(this);
	}

	/**
	 * Check edge boundaries to see if you can dislocate the given amount safely.
	 * 
	 * @param distance
	 *            the distance
	 * @return true, if successful
	 */
	private boolean checkEdgeBoundaries(double distance) {
		return this.pos.canDislocate(distance);
	}

	/**
	 * Gets the remaining distance you can reach into the next edge from where you are.
	 * 
	 * @param overallDistance
	 *            the overall distance
	 * @return the within the next edge
	 */
	private double getNextEdgeDistance(double overallDistance) {
		return overallDistance - (this.pos.getDistanceToEdgeEnd());
	}

	/**
	 * Moves the given distance within the same edge.
	 * 
	 * @param distance
	 *            the distance
	 */
	private void moveDistance(double distance) {
		Segment currentSegment = getSegmentFor(this.pos);
		currentSegment.decrementOccupancy();

		this.pos.dislocate(distance);
		if (this.pos.distanceFromStart > this.pos.edge.getLength()) {
			throw new RuntimeException("Deslocate limit error");
		}

		Segment nextSegment = getSegmentFor(this.pos);
		nextSegment.incrementOccupancy();

		this.traveledDistance += distance;
	}

	/**
	 * Move to farthest available location within the range.
	 * 
	 * @param current
	 *            the current
	 * @param limit
	 *            the limit
	 */
	private void moveToFarthestAvailableLocation(Segment current, Segment limit) {
		Segment farthestAvailable = getGraph().getFarthestAvailableSegment(current, limit);
		double endOfTheSegment = farthestAvailable.exclusiveEndLocation.distanceFromStart;
		// get to right before the end of the segment
		double diff = endOfTheSegment - FlockingGraph.MINIMUM_DISTANCE_MARGIN - this.pos.distanceFromStart;

		// move only as far as possible
		double distance = diff < this.speed ? diff : this.speed;
		// don't move back due to floating point imprecision
		if (distance > 0d) {
			moveDistance(distance);
		}
	}

}
