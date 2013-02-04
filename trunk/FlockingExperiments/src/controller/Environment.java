package controller;

import java.util.HashSet;
import java.util.Set;

import flock.Flock;

import agent.AchieverBoid;
import agent.Boid;

public class Environment {
	Set<Boid> freeBoids;
	Set<Flock> flocks;
	
	public Environment() {
		this.freeBoids = new HashSet<>();
		this.flocks = new HashSet<>();
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
	}
	
	public Set<Boid> getAllBoids() {
		Set<Boid> allBoids = new HashSet<>();
		allBoids.addAll(this.freeBoids);
		for (Flock flock : this.flocks) {
			allBoids.addAll(flock.getBoidsInFlock());
		}
		
		return allBoids;
	}
	
}
