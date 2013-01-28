package agent;

import graph.Tour;

public class AchieverBoid extends Boid {

	// private Flock flock;
	Tour pathToFollow;

	public AchieverBoid(Boid boid) {
		super(boid);
		this.pathToFollow = boid.pathTaken;
		this.pathToFollow.calculateCost(this.graph);
	}

	@Override
	public void decide() {
		this.pathTaken.offer(this.getPos().edge.getTo());

		if (completedTour()) {
			respawn();
			return;
		}

		int currentNode = this.getPos().edge.getTo();
		int nodeIndex = this.pathToFollow.indexOf(currentNode);
		int nextNode = this.pathToFollow.get(nodeIndex + 1);

		moveToNextEdge(loadEdge(currentNode, nextNode));
	}

	@Override
	public boolean completedTour() {
		return this.pathTaken.size() == this.pathToFollow.size();
	}
}
