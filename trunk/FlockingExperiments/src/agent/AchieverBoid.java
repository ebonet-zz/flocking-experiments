package agent;

import graph.Position;
import graph.Tour;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

public class AchieverBoid extends Boid {

	// private Flock flock;
	private Tour pathToFollow;

	public AchieverBoid(Boid boid) {
		super(boid);
		this.pathToFollow = new Tour(this.pathTaken);
		this.pathToFollow.calculateCost(this.graph);
	}

	@Override
	public void tryToMove(double distance) {

		// 1) Check for other achiever boids around
		Set<AchieverBoid> boidsInSight = getBoidsInSight();

		// 2) Compare the traveled distance and 3) Update the path
		for (Boid boid : boidsInSight) {

			if (boid.traveledDistance < this.traveledDistance) {
				this.pathTaken = new Tour(boid.pathTaken);
			}

		}

		// 4) Move as before

		super.tryToMove(distance);
	}

	private Set<AchieverBoid> getBoidsInSight() {

		Set<AchieverBoid> boidsInSight = new HashSet<>();

		for (AchieverBoid b : environment.getAllAchievers()) {

			if (b.isInSight(this)) {
				boidsInSight.add(b);
			}
		}

		System.out.println("Number of boids on sight:"+boidsInSight.size());

		return boidsInSight;
	}

	public boolean isInSight(Boid b) {
		return b.pos.isSameEdge(pos) && (Math.abs(b.getPos().getDistance() - pos.getDistance()) < b.visionRange);
	}

	public Tour getPathToFollow() {
		return this.pathToFollow;
	}

	public void setPathToFollow(Tour newPath) {
		// what if path changes mid-move?
	}

	@Override
	public void decide() {
		if (this.pathTaken.lastLocation() != this.pos.edge.getTo())
			this.pathTaken.offer(this.getPos().edge.getTo());

		if (this.goalEvaluator.isGoal(this.graph, this.pathTaken)) {
			respawn();
			return;
		}

		int currentNode = this.getPos().edge.getTo();
		int nodeIndex = this.pathToFollow.indexOf(currentNode);
		int nextNode = this.pathToFollow.get(nodeIndex + 1);

		moveToNextEdge(loadEdge(currentNode, nextNode));
	}

	public void respawn() {
		System.out.println(this.pathToFollow.toString());
		this.pathTaken.clear();
		this.pathTaken.offer(this.pathToFollow.get(0));
		this.setPosition(new Position(loadEdge(this.pathToFollow.get(0), this.pathToFollow.get(1)), 0d));
	}

	@Override
	public Color getColor() {
		return Color.blue;
	}
}
