package flock;

import graph.FlockingGraph;
import graph.Tour;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import agent.AchieverBoid;
import controller.Environment;

public class Flock {
	private Set<AchieverBoid> boidsInFlock;
	private Tour pathToFollow;
	private Environment environment;
	private FlockingGraph graph;
	
	public Flock(AchieverBoid flockFounder) {
		this.boidsInFlock = new HashSet<>();
		this.boidsInFlock.add(flockFounder);
		this.pathToFollow = flockFounder.getPathToFollow();
		this.environment = flockFounder.getEnvironment();
		this.graph = this.environment.getFlockingGraph();
	}
	
	public double getPathLength() {
		return this.pathToFollow.getCost(graph);
	}
	
	public Tour getPathToFollow() {
		return this.pathToFollow;
	}
	
	public Set<AchieverBoid> getBoidsInFlock() {
		return this.boidsInFlock;
	}
	
	public void addNewBoids(Collection<AchieverBoid> newAchievers) {
		this.boidsInFlock.addAll(newAchievers);
	}
	
	public void meetFlock(Flock otherFlock) {
		if (this.getPathLength() > otherFlock.getPathLength()) {
			// join them
			this.joinOtherFlock(otherFlock);
		} else {
			// they join this flock
			otherFlock.joinOtherFlock(this);
		}
	}
	
	public void disintegrate() {
		// remove from environment
		this.environment.flockDisintegrated(this);
	}
	
	public void joinOtherFlock(Flock otherFlock) {
		otherFlock.addNewBoids(this.boidsInFlock);
		for (AchieverBoid boid : this.boidsInFlock) {
			boid.setPathToFollow(otherFlock.getPathToFollow());
		}
		this.disintegrate();

	}
}
