package agent;

import java.util.LinkedList;

public class AchieverBoid extends Boid {

	//private Flock flock;
	private LinkedList<Integer> pathToFollow;
	
	public AchieverBoid(Boid boid) {
		super(boid);
		this.pathToFollow = boid.pathTaken;
		
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
