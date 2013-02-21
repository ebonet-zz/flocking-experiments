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
import java.util.Set;

/**
 * Our Class AchieverBoid, representing the flocking boids of success.
 */
public class AchieverBoid extends Boid {

	// TODO: Consider randomly changing it back to explorer boid in a decision point

	/**
	 * Gets the speed for the new achiever boid based on the taken path before respawn.
	 * 
	 * @param pathLength
	 *            the path length
	 * @return the speed modifier
	 */
	private static double getSpeedModifier(double pathLength) {
		int magnitude = 1;
		while (pathLength > magnitude) {
			magnitude *= 10;
		}
		magnitude = magnitude / 10;
		return (1 + magnitude / pathLength);
	}

	/** The whole path to follow continuously. */
	private Tour pathToFollow;

	/** The path queue of places to visit next. */
	private Queue<Integer> pathQueue;

	/**
	 * Instantiates a new achiever boid out of the original explorer boid.
	 * 
	 * @param boid
	 *            the boid
	 */
	public AchieverBoid(Boid boid) {
		super(boid);

		// register the path to follow
		this.pathToFollow = new Tour(this.pathTaken);
		this.pathToFollow.calculateCost(getGraph());

		// let the environment know about my achiever path
		this.environment.registerPath(this.pathToFollow);

		// build my path queue
		this.pathQueue = new LinkedList<>();

		// set my color
		float r = this.rand.nextFloat();
		float g = 0;
		float b = this.rand.nextFloat();
		this.color = new Color(r, g, b);

		// set my speed
		this.speed = this.speed * getSpeedModifier(this.pathToFollow.getCost(getGraph()));
	}

	/**
	 * Tests if this boid can see the given boid.
	 * 
	 * @param b
	 *            the other boid
	 * @return true, if the other boid is visible for this boid
	 */
	@Override
	public boolean canSee(Boid b) {
		// decide based on if your vision fits within the current edge or goes beyond it

		if (new Double(this.pos.getDistanceToEdgeEnd()).compareTo(this.visionRange) >= 0) {
			// Vision is completely inside the edge
			if (!b.pos.isSameEdge(this.pos)) {
				// can't even see you edge
				return false;
			}

			Double difference = b.getPos().getDistanceFromStart() - this.pos.getDistanceFromStart();

			if (difference.compareTo(0d) < 0) {
				// behind me
				return false;
			}

			// test my sight range
			return difference.compareTo(this.visionRange) <= 0;
		} else {
			// Vision exceeds edge
			if (b.pos.isSameEdge(this.pos)) {
				// boid in same edge
				Double difference = b.getPos().getDistanceFromStart() - this.pos.getDistanceFromStart();

				if (difference.compareTo(0d) >= 0) {
					// visible
					return true;
				} else {
					// behind me
					return false;
				}
			} else {
				// different edges
				List<Edge> neighbors = generatePossibleNextEdges();
				Double visibleDistance = this.visionRange - this.pos.getDistanceToEdgeEnd(); // outside vision

				for (Edge e : neighbors) {
					if (b.pos.edge.isSameEdge(e)) {
						// boid is in neighbor edge
						if (visibleDistance.compareTo(b.pos.distanceFromStart) >= 0) {
							// inside outside vision
							return true;
						}
					}
				}
				// not visible, too far or not neighbor edge
				return false;
			}
		}
	}

	/**
	 * Decide where to go next, what edge to take.
	 */
	@Override
	public void decide() {
		boolean added = false; // was something added to my path?
		// test to see if I need to add the location I'm crossing needs to be added to my path
		if (this.pathTaken.lastLocation() != this.pos.edge.getTo()
				|| this.getGraph().getEdgeLength(this.pathTaken.lastLocation(), this.pathTaken.lastLocation()) != -1) {

			// i'm passing a new location in my next move, it should be in my path taken
			this.pathTaken.offer(this.pos.edge.getTo());
			added = true;
		}

		// check my path queue
		if (this.pathQueue.isEmpty()) {
			// I have been everywhere already, see if I can respawn
			if (checkSegmentOccupation(getSegmentFor(new Position(loadEdge(this.pathToFollow.get(0),
					this.pathToFollow.get(1)), 0d)))) {
				// there is space in the start, respawn
				respawn();
			} else {
				// there is no space to respawn, we should wait
				if (added) {
					// I'm not going to move anymore, since there is no space, thus I'm not crossing this location now
					this.pathTaken.locations.remove(this.pathTaken.locations.size() - 1);
					added = false;
				}
			}
			// either respawning or waiting, i'm done for this iteration
			return;
		}

		// get movement info for next edge
		int currentNode = this.getPos().edge.getTo();
		int nextNode = this.pathQueue.peek();

		Edge nextEdge = loadEdge(currentNode, nextNode);
		if (nextEdge.getLength() < 0) {
			throw new RuntimeException("Bad edge");
		}

		// save current edge
		Edge currentEdge = this.pos.edge;

		// try to move to next
		tryToMoveToNextEdge(nextEdge);

		// if I really moved, I consumed one edge of my queue
		if (!this.pos.edge.equals(currentEdge)) {
			this.pathQueue.poll();
		}
	}

	/**
	 * Gets the path distance.
	 * 
	 * @return the path distance
	 */
	@Override
	public Double getPathDistance() {
		return this.pathToFollow.lastCalculatedCost;
	}

	/**
	 * Gets the path to follow.
	 * 
	 * @return the path to follow
	 */
	public Tour getPathToFollow() {
		return this.pathToFollow;
	}

	/**
	 * Respawn this achiever boid.
	 */
	public void respawn() {
		// clear path taken
		this.pathTaken.clear();

		// reset queue
		this.pathQueue.clear();
		this.pathQueue.addAll(this.pathToFollow.locations);

		// adjust position
		Integer firstNode = this.pathQueue.poll();
		Integer secondNode = this.pathQueue.poll();

		this.setPosition(new Position(loadEdge(firstNode, secondNode), 0d));

		// correct path taken
		this.pathTaken.offer(firstNode);
	}

	/**
	 * Try to move, if you can.
	 * 
	 * @param distance
	 *            the distance to move (ideally)
	 */
	@Override
	public void tryToMove(double distance) {

		// 1) Check for other achiever boids around
		Set<AchieverBoid> boidsInSight = getBoidsInSight();

		// 2) Compare the traveled distance and 3) Update the path
		for (AchieverBoid boid : boidsInSight) {
			// check the boid's tiredness and speed
			if (boid.getPathDistance().compareTo(this.getPathDistance()) < 0
					|| (boid.getPathDistance().compareTo(this.getPathDistance()) == 0 && boid.speed > this.speed)) {
				// I decided to follow, the guy is better than me

				// Unregister my path, I will change it
				this.environment.unregisterPath(this.pathToFollow);

				// Get my new path
				this.pathToFollow = new Tour(boid.getPathToFollow());

				// Register it
				this.environment.registerPath(this.pathToFollow);

				// Update my speed, if to improve
				this.speed = boid.speed > this.speed ? boid.speed : this.speed;

				// update path queue so that the boid can follow correctly
				updatePathQueueFromBoid(boid);

				// absorb color for GUI purposes
				this.color = new Color(boid.color.getRed(), boid.color.getGreen(), boid.color.getBlue());
			}

		}

		// 4) Move as before
		super.tryToMove(distance);
	}

	/**
	 * Generate possible next edges.
	 * 
	 * @return the list
	 */
	@Override
	protected List<Edge> generatePossibleNextEdges() {
		ArrayList<Integer> neighbors = getGraph().getNeighborsOf(this.pos.edge.getTo());

		List<Edge> possibleEdges = generateEdges(neighbors);
		return possibleEdges;
	}

	/**
	 * Update path queue from other boid (aka where should I go now that I'm following you?).
	 * 
	 * @param boid
	 *            the boid
	 */
	protected void updatePathQueueFromBoid(AchieverBoid boid) {
		this.pathQueue.clear();
		if (this.pos.getTo() == boid.pos.getFrom()) {
			// if the other boid was in the node we're trying to reach
			// our next node should be other's current endNode, but it's not in its queue anymore
			this.pathQueue.add(boid.pos.getTo());
		}
		this.pathQueue.addAll(boid.pathQueue);
	}

	/**
	 * Gets the boids in sight.
	 * 
	 * @return the boids in sight
	 */
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
