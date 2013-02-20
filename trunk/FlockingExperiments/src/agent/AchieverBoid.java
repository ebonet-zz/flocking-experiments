package agent;

import graph.Edge;
import graph.Position;
import graph.Tour;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

public class AchieverBoid extends Boid {

	// TODO: Consider randomly changing it back to explorer boid in a decision point

	private static double getSpeedModifier(double pathLength) {
		int magnitude = 1;
		while (pathLength > magnitude) {
			magnitude *= 10;
		}
		magnitude = magnitude / 10;
		return (1 + magnitude / pathLength);
	}

	private Tour pathToFollow;

	private Queue<Integer> pathQueue;

	public AchieverBoid(Boid boid) {
		super(boid);
		this.pathToFollow = new Tour(this.pathTaken);
		this.pathToFollow.calculateCost(getGraph());
		this.environment.registerPath(this.pathToFollow);
		this.pathQueue = new LinkedList<>();

		Random rand = new Random();

		float r = rand.nextFloat();
		float g = 0;
		float b = rand.nextFloat();

		this.color = new Color(r, g, b);

		// this.color = Color.BLUE;
		this.speed = this.speed * getSpeedModifier(this.pathToFollow.getCost(getGraph()));
	}

	@Override
	public boolean canSee(Boid b) {
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

	@Override
	public void decide() {
		boolean added = false;
		if (this.pathTaken.lastLocation() != this.pos.edge.getTo()) {
			this.pathTaken.offer(this.pos.edge.getTo());
			added = true;
		}

		if (this.pathQueue.isEmpty()) {
			if (checkSegmentOccupation(getSegment(new Position(loadEdge(this.pathToFollow.get(0),
					this.pathToFollow.get(1)), 0d)))) {
				respawn();
			} else {
				if (added) {
					this.pathTaken.locations.remove(this.pathTaken.locations.size() - 1);
					added = false;
				}
			}
			return;
		}

		int currentNode = this.getPos().edge.getTo();
		int nextNode = this.pathQueue.peek();
		if (nextNode == currentNode) {
			throw new RuntimeException("Node Selection Glitch");
		}
		Edge nextEdge = loadEdge(currentNode, nextNode);
		if (nextEdge.getLength() < 0) {
			throw new RuntimeException("Bad edge");
		}

		Edge currentEdge = this.pos.edge;

		moveToNextEdge(nextEdge);

		if (!this.pos.edge.equals(currentEdge)) {
			this.pathQueue.poll();
		}
	}

	@Override
	public Double getPathDistance() {
		return this.pathToFollow.lastCalculatedCost;
	}

	public Tour getPathToFollow() {
		return this.pathToFollow;
	}

	public void respawn() {
		this.pathTaken.clear();

		this.pathQueue.clear();
		this.pathQueue.addAll(this.pathToFollow.locations);

		Integer firstNode = this.pathQueue.poll();
		Integer secondNode = this.pathQueue.poll();

		this.setPosition(new Position(loadEdge(firstNode, secondNode), 0d));
		this.pathTaken.offer(firstNode);
	}

	@Override
	public void tryToMove(double distance) {

		// 1) Check for other achiever boids around
		Set<AchieverBoid> boidsInSight = getBoidsInSight();

		// 2) Compare the traveled distance and 3) Update the path
		for (AchieverBoid boid : boidsInSight) {

			// TODO: maybe this should consider only the current walked distance, aka visible current tiredness
			if (boid.getPathDistance().compareTo(this.getPathDistance()) < 0
					|| (boid.getPathDistance().compareTo(this.getPathDistance()) == 0 && boid.speed > this.speed)) {
				this.environment.unregisterPath(this.pathToFollow);

				this.pathToFollow = new Tour(boid.getPathToFollow());

				this.environment.registerPath(this.pathToFollow);

				this.speed = boid.speed > this.speed ? boid.speed : this.speed;

				// update pathTaken so that the boid can respawn correctly
				updatePathQueueFromBoid(boid);

				this.color = new Color(boid.color.getRed(), boid.color.getGreen(), boid.color.getBlue());
			}

		}

		// 4) Move as before
		super.tryToMove(distance);
	}

	protected void updatePathQueueFromBoid(AchieverBoid boid) {
		this.pathQueue.clear();
		if (this.pos.getTo() == boid.pos.getFrom()) {
			// if the other boid was in the node we're trying to reach
			// our next node should be other's current endNode, but it's not in its queue anymore
			this.pathQueue.add(boid.pos.getTo());
		}
		this.pathQueue.addAll(boid.pathQueue);
	}

	@Override
	protected List<Edge> generatePossibleNextEdges() {
		ArrayList<Integer> neighbors = getGraph().getNeighborsOf(this.pos.edge.getTo());

		List<Edge> possibleEdges = generateEdges(neighbors);
		return possibleEdges;
	}

	private Set<AchieverBoid> getBoidsInSight() {
		Set<AchieverBoid> boidsInSight = new HashSet<>();

		for (AchieverBoid b : this.environment.getAllAchievers()) {
			if (canSee(b)) {
				boidsInSight.add(b);
			}
		}

		boidsInSight.remove(this);
		return boidsInSight;
	}

}
