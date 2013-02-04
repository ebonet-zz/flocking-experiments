package controller;

import java.util.HashSet;
import java.util.Set;

import flock.Flock;
import graph.FlockingGraph;

import agent.AchieverBoid;
import agent.Boid;

public class Environment {
	private Set<Boid> freeBoids;
	private Set<Flock> flocks;
	private FlockingGraph graph;

	public Environment(FlockingGraph graph) {
		this.freeBoids = new HashSet<>();
		this.flocks = new HashSet<>();
		this.graph = graph;
	}
	
	public FlockingGraph getFlockingGraph() {
		return this.graph;
	}
	
	public void addNewBoid(Boid boid) {
		this.freeBoids.add(boid);
	}

	public void spawnNewBoid() {
		// spawning logic
	}
	
	public void turnIntoAchiever(Boid boid) {
		this.freeBoids.remove(boid);
		AchieverBoid achiever = new AchieverBoid(boid);
		achiever.respawn();
		Flock newFlock = new Flock(achiever);
		this.registerNewFlock(newFlock);
	}

	public void boidDied(Boid boid) {
		this.freeBoids.remove(boid);
	}
	
	public void registerNewFlock(Flock flock) {
		this.flocks.add(flock);
	}
	
	public void flockDisintegrated(Flock flock) {
		this.flocks.remove(flock);
		// TODO: remove this flock's path from the Map of Tours
	}
	
	public Set<Boid> getAllBoids() {
		Set<Boid> allBoids = new HashSet<>();
		allBoids.addAll(this.freeBoids);
		for (Flock flock : this.flocks) {
			allBoids.addAll(flock.getBoidsInFlock());
		}
		
		return allBoids;
	}
	
	public int getNumberOfFlocks() {
		return this.flocks.size();
	}
	
	public int getNumberOfFreeBoids() {
		return this.freeBoids.size();
	}
	
}
