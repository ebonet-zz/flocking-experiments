package agent;

import graph.Edge;
import graph.Position;
import graph.Tour;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class AchieverBoid extends Boid {

	private Tour pathToFollow;

	public AchieverBoid(Boid boid) {
		super(boid);
		this.pathToFollow = new Tour(this.pathTaken);
		this.pathToFollow.calculateCost(getGraph());
		this.environment.registerPath(this.pathToFollow);

		Random rand = new Random();

		float r = rand.nextFloat();
		float g = 0;
		float b = rand.nextFloat();

		this.color = new Color(r, g, b);

		// this.color = Color.BLUE;
		this.speed = this.speed * getSpeedModifier(this.pathToFollow.getCost(getGraph()));
	}

	@Override
	public void tryToMove(double distance) {

		// 1) Check for other achiever boids around
		Set<AchieverBoid> boidsInSight = getBoidsInSight();

		// 2) Compare the traveled distance and 3) Update the path
		for (AchieverBoid boid : boidsInSight) {

			// TODO: maybe this should consider only the current walked distance, aka visible current tiredness
			if (boid.getPathDistance() < this.getPathDistance()) {
				this.environment.unregisterPath(this.pathToFollow);

				this.pathToFollow = new Tour(boid.getPathToFollow());

				this.environment.registerPath(this.pathToFollow);
				this.speed = boid.speed;
			}

			if (boid.getPathDistance() <= this.getPathDistance()) {
				this.color = new Color(boid.color.getRed(), boid.color.getGreen(), boid.color.getBlue());
			}

		}

		// 4) Move as before

		super.tryToMove(distance);
	}

	@Override
	public Double getPathDistance() {
		return this.pathToFollow.lastCalculatedCost;
	}

	private Set<AchieverBoid> getBoidsInSight() {
		Set<AchieverBoid> boidsInSight = new HashSet<>();

		for (AchieverBoid b : this.environment.getAllAchievers()) {

			if (canSee(b)) {
				boidsInSight.add(b);
			}
		}

		return boidsInSight;
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
				List<Edge> neighbors = generatePossibleEdges();
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
	protected List<Edge> generatePossibleEdges() {
		ArrayList<Integer> neighbors = getGraph().getNeighborsOf(this.pos.edge.getTo());

		List<Edge> possibleEdges = generateEdges(neighbors);
		return possibleEdges;
	}

	public Tour getPathToFollow() {
		return this.pathToFollow;
	}

	@Override
	public void decide() {
		if (this.pathTaken.lastLocation() != this.pos.edge.getTo())
			this.pathTaken.offer(this.getPos().edge.getTo());

		if (this.goalEvaluator.isGoal(getGraph(), this.pathTaken)) {
			respawn();
			return;
		}

		int currentNode = this.getPos().edge.getTo();
		int nodeIndex = this.pathToFollow.indexOf(currentNode);
		int nextNode = this.pathToFollow.get(nodeIndex + 1);

		moveToNextEdge(loadEdge(currentNode, nextNode));
	}

	public void respawn() {
		// System.out.println(this.pathToFollow.toString());
		this.pathTaken.clear();
		this.pathTaken.offer(this.pathToFollow.get(0));
		this.setPosition(new Position(loadEdge(this.pathToFollow.get(0), this.pathToFollow.get(1)), 0d));
	}

	private static double getSpeedModifier(double pathLength) {
		int magnitude = 1;
		while (pathLength > magnitude) {
			magnitude *= 10;
		}
		magnitude = magnitude / 10;
		return (1 + magnitude / pathLength);
	}

}
