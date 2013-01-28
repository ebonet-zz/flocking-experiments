package agent;

import graph.Edge;
import graph.FlockingGraph;
import graph.Position;
import graph.Segment;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * TODO Put here a description of what this class does.
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
	private LinkedList<Integer> pathTaken;
	private boolean isAchiever;

	public void moveDistance(double distance) {
		Segment currentSegment = getSegment(this.pos);
		currentSegment.currentOccupancy--;

		this.pos.deslocate(distance);

		Segment nextSegment = getSegment(this.pos);
		nextSegment.currentOccupancy++;

		this.traveledDistance += distance;
	}

	public void moveToNextEdge(Edge edge) {
		double distanceWithin = this.pos.getDistanceToEdgeEnd();
		double distanceOnNext = getNextEdgeDistance(this.speed);

		moveDistance(distanceWithin);
		this.setPosition(new Position(edge, 0d));
		tryToMove(distanceOnNext);
	}

	public void setPosition(Position pos) {
		Segment currentSegment = getSegment(this.pos);
		currentSegment.currentOccupancy--;

		this.pos = pos;

		Segment nextSegment = getSegment(this.pos);
		nextSegment.currentOccupancy++;
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

	public Segment getSegment(Position pos) {
		return this.graph.getSegmentForPosition(pos);
	}

	public boolean checkEdgeBoundaries(double distance) {
		return this.pos.canDeslocate(distance);
	}

	public boolean checkSegmentOccupation(Segment seg) {
		return seg.isFull();
	}

	public void moveToFarthestAvailableLocation(Segment current, Segment limit) {
		Segment farthestAvailable = this.graph.getFarthestAvailableSegment(current, limit);
		double endOfTheSegment = farthestAvailable.exclusiveEndLocation.distanceFromStart;
		// get to right before the end of the segment
		double diff = endOfTheSegment - MINIMUM_DISTANCE_MARGIN - this.pos.distanceFromStart;
		moveDistance(diff);
	}

	public void decide() {
		this.pathTaken.offer(this.pos.edge.getTo());

		if (this.isAchiever) {

			if (completedTour()) {
				respawn();
				return;
			}

			int currentNode = this.pos.edge.getTo();
			int nodeIndex = this.pathTaken.indexOf(currentNode);
			int nextNode = this.pathTaken.get(nodeIndex + 1);

			// TODO: finish this
			moveToNextEdge(new Edge(currentNode, nextNode, this.graph.getEdgeLength(currentNode, nextNode)));

		} else {
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

			// TODO more stuff
		}

	}

	public double getNextEdgeDistance(double overallDistance) {
		return overallDistance - (this.pos.getDistanceToEdgeEnd());
	}

	public boolean completedTour() {
		return this.pathTaken.size() == this.graph.getNumberOfNodes();
	}

	public void becomeAchiever() {
		this.isAchiever = true;
		respawn();
	}

	public void respawn() {
		this.setPosition(new Position(new Edge(this.pathTaken.get(0), this.pathTaken.get(1), this.graph.getEdgeLength(
				this.pathTaken.get(0), this.pathTaken.get(1))), 0d));
	}

	public void die() {
		// TODO: 
	}

}
