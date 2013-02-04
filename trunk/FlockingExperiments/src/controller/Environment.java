package controller;

import graph.FlockingGraph;
import graph.Tour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import util.SortableKeyValue;
import agent.AchieverBoid;
import agent.Boid;

public class Environment {
	private Set<Boid> freeBoids;
	// private Set<Flock> flocks;
	private Set<AchieverBoid> achievers;

	private FlockingGraph graph;
	private HashMap<Tour, Integer> foundToursPopulations;

	public Environment(FlockingGraph graph) {
		this.freeBoids = new HashSet<>();
		// this.flocks = new HashSet<>();
		this.achievers = new HashSet<>();
		this.graph = graph;
		this.foundToursPopulations = new HashMap<>();
	}

	public FlockingGraph getFlockingGraph() {
		return this.graph;
	}

	public void addNewAchiever(AchieverBoid achiever) {
		this.achievers.add(achiever);
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
		// Flock newFlock = new Flock(achiever);
		// this.registerNewFlock(newFlock);
		this.addNewAchiever(achiever);
	}

	public void boidDied(Boid boid) {
		this.freeBoids.remove(boid);
	}

	// public void registerNewFlock(Flock flock) {
	// this.flocks.add(flock);
	// }

	// public void joinFlocks(Flock flock1, Flock flock2) {
	// if (flock1.getPathLength() > flock2.getPathLength()) {
	// flock1.acceptOtherFlock(flock2);
	// // TODO: Remove flock2 from map and update number of boids on flock1's path
	// } else {
	// flock2.acceptOtherFlock(flock1);
	// // TODO: Remove flock1 from map and update number of boids on flock2's path
	// }
	// }

	// public void flockDisintegrated(Flock flock) {
	// this.flocks.remove(flock);
	// // TODO: remove this flock's path from the Map of Tours
	// System.out.println("Flock joined another flock");
	// }
	//
	public Set<Boid> getAllBoids() {
		Set<Boid> allBoids = new HashSet<>();
		allBoids.addAll(this.freeBoids);
		// for (Flock flock : this.flocks) {
		// allBoids.addAll(flock.getBoidsInFlock());
		// }
		allBoids.addAll(this.achievers);
		return allBoids;
	}

	public Set<AchieverBoid> getAllAchievers() {
		// Set<AchieverBoid> achievers = new HashSet<>();
		// for (Flock flock : this.flocks) {
		// achievers.addAll(flock.getBoidsInFlock());
		// }
		//
		// return achievers;
		return this.achievers;
	}

	public Set<Boid> getFreeBoids() {
		return this.freeBoids;
	}

	//
	// public int getNumberOfFlocks() {
	// return this.flocks.size();
	// }

	public int getNumberOfFreeBoids() {
		return this.freeBoids.size();
	}

	public void registerPath(Tour pathToFollow) {
		if (this.foundToursPopulations.containsKey(pathToFollow)) {
			this.foundToursPopulations.put(pathToFollow, this.foundToursPopulations.get(pathToFollow) + 1);
		} else {
			this.foundToursPopulations.put(pathToFollow, 1);
		}

		// System.out.println("Registering Boid");
		// System.out.println(this.pathsTaken.toString());
		// System.out.println("");
	}

	public void unregisterPath(Tour pathToFollow) {
		if (this.foundToursPopulations.containsKey(pathToFollow)) {
			if (this.foundToursPopulations.get(pathToFollow) == 1) {
				this.foundToursPopulations.remove(pathToFollow);
			} else {
				this.foundToursPopulations.put(pathToFollow, this.foundToursPopulations.get(pathToFollow) - 1);
			}
		}

		// System.out.println("Unregistering Boid");
		// System.out.println(this.pathsTaken.toString());
		// System.out.println("");

	}

	public SortableKeyValue<Tour,Double> getMostDensePath() {
		ArrayList<SortableKeyValue<Tour, Double>> densities = new ArrayList<SortableKeyValue<Tour, Double>>();
		for (Tour t : this.foundToursPopulations.keySet()) {
			densities.add(new SortableKeyValue<Tour, Double>(t, this.foundToursPopulations.get(t)
					/ t.lastCalculatedCost));
		}
		if (densities.isEmpty()) {
			return null;
		}
		Collections.sort(densities);
		return densities.get(densities.size() - 1);
	}

}
