package controller;

import graph.FlockingGraph;
import graph.Tour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

import util.SortableKeyValue;
import agent.AchieverBoid;
import agent.Boid;

public class Environment {
	private Set<Boid> freeBoids;
	private Set<AchieverBoid> achievers;

	private FlockingGraph graph;
	private HashMap<Tour, Integer> foundToursPopulations;
	private int maxAgents;

	public Environment(FlockingGraph graph, int maxAgents) {
		this.freeBoids = new HashSet<>();
		this.achievers = new HashSet<>();
		this.graph = graph;
		this.maxAgents = maxAgents;
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
		this.addNewAchiever(achiever);
	}

	public void boidDied(Boid boid) {
		this.freeBoids.remove(boid);
	}

	public Set<Boid> getAllBoids() {
		Set<Boid> allBoids = new HashSet<>();
		allBoids.addAll(this.freeBoids);

		allBoids.addAll(this.achievers);
		return allBoids;
	}

	public Set<AchieverBoid> getAllAchievers() {
		return this.achievers;
	}

	public Set<Boid> getFreeBoids() {
		return this.freeBoids;
	}

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

	public SortableKeyValue<Tour, Double> getMostDensePath() {
		ArrayList<SortableKeyValue<Tour, Double>> densities = new ArrayList<SortableKeyValue<Tour, Double>>();
		for (Tour t : this.foundToursPopulations.keySet()) {
			densities.add(new SortableKeyValue<Tour, Double>(t, this.foundToursPopulations.get(t)
					/ (double) this.maxAgents));
		}
		if (densities.isEmpty()) {
			return null;
		}
		Collections.sort(densities);
		return densities.get(densities.size() - 1);
	}

	public void printDistancesMap() {
		for (Entry<Tour, Integer> entry : this.foundToursPopulations.entrySet()) {
			System.out.println(entry.getKey().toString() + " boids: " + entry.getValue().toString());
		}
	}
}
