package agent;

import graph.FlockingGraph;
import graph.Tour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import util.SortableKeyValue;

/**
 * The Environment class where all the agents exist.
 */
public class Environment {

	/** The free boids (explorers). */
	private Set<Boid> freeBoids;

	/** The achievers. */
	private Set<AchieverBoid> achievers;

	/** The graph. */
	private FlockingGraph graph;

	/** The populations for tours found. */
	private HashMap<Tour, Integer> foundToursPopulations;

	/**
	 * Instantiates a new environment.
	 * 
	 * @param graph
	 *            the graph
	 */
	public Environment(FlockingGraph graph) {
		this.freeBoids = new HashSet<>();
		this.achievers = new HashSet<>();
		this.graph = graph;
		this.foundToursPopulations = new HashMap<>();
	}

	/**
	 * Adds the new achiever.
	 * 
	 * @param achiever
	 *            the achiever
	 */
	public void addNewAchiever(AchieverBoid achiever) {
		this.achievers.add(achiever);
	}

	/**
	 * Adds the new free boid.
	 * 
	 * @param boid
	 *            the boid
	 */
	public void addNewFreeBoid(Boid boid) {
		this.freeBoids.add(boid);
	}

	/**
	 * Boid died.
	 * 
	 * @param boid
	 *            the boid
	 */
	public void boidDied(Boid boid) {
		this.freeBoids.remove(boid);
	}

	/**
	 * Gets the all achievers.
	 * 
	 * @return the all achievers
	 */
	public Set<AchieverBoid> getAllAchievers() {
		return this.achievers;
	}

	/**
	 * Gets the all boids.
	 * 
	 * @return the all boids
	 */
	public Set<Boid> getAllBoids() {
		Set<Boid> allBoids = new HashSet<>();
		allBoids.addAll(this.freeBoids);

		allBoids.addAll(this.achievers);
		return allBoids;
	}

	/**
	 * Count all boids.
	 * 
	 * @return the int
	 */
	public int countAllBoids() {
		return this.freeBoids.size() + this.achievers.size();
	}

	/**
	 * Gets the flocking graph.
	 * 
	 * @return the flocking graph
	 */
	public FlockingGraph getFlockingGraph() {
		return this.graph;
	}

	/**
	 * Gets the most dense path.
	 * 
	 * @return the most dense path
	 */
	public SortableKeyValue<Tour, Double> getMostDensePath() {
		ArrayList<SortableKeyValue<Tour, Double>> densities = new ArrayList<SortableKeyValue<Tour, Double>>();
		for (Tour t : this.foundToursPopulations.keySet()) {
			densities.add(new SortableKeyValue<Tour, Double>(t, this.foundToursPopulations.get(t)
					/ (double) countAllBoids()));
		}
		
		Collections.sort(densities);
		if(densities.isEmpty()) {
			return null;
		}
		return densities.get(densities.size() - 1);
	}

	public List<SortableKeyValue<Tour, Integer>> getAllPaths() {
		ArrayList<SortableKeyValue<Tour, Integer>> toursPopulation = new ArrayList<SortableKeyValue<Tour, Integer>>();
		for (Tour t : this.foundToursPopulations.keySet()) {
			toursPopulation.add(new SortableKeyValue<Tour, Integer>(t, this.foundToursPopulations.get(t)));
		}
		
		Collections.sort(toursPopulation);
		return toursPopulation;
	}

	/**
	 * Prints the distances map.
	 */
	public void printDistancesMap() {
		for (Entry<Tour, Integer> entry : this.foundToursPopulations.entrySet()) {
			System.out.println(entry.getKey().toString() + " boids: " + entry.getValue().toString());
		}
	}

	/**
	 * Register an achiever path.
	 * 
	 * @param pathToFollow
	 *            the path to follow
	 */
	public void registerPath(Tour pathToFollow) {
		if (this.foundToursPopulations.containsKey(pathToFollow)) {
			this.foundToursPopulations.put(pathToFollow, this.foundToursPopulations.get(pathToFollow) + 1);
		} else {
			this.foundToursPopulations.put(pathToFollow, 1);
		}
	}

	/**
	 * Turn into achiever.
	 * 
	 * @param boid
	 *            the boid
	 */
	public void turnIntoAchiever(Boid boid) {
		boid.die();
		AchieverBoid achiever = new AchieverBoid(boid);
		achiever.respawn();
		this.addNewAchiever(achiever);
	}

	/**
	 * Unregister achiever path.
	 * 
	 * @param pathToFollow
	 *            the path to follow
	 */
	public void unregisterPath(Tour pathToFollow) {
		if (this.foundToursPopulations.containsKey(pathToFollow)) {
			if (this.foundToursPopulations.get(pathToFollow) == 1) {
				this.foundToursPopulations.remove(pathToFollow);
			} else {
				this.foundToursPopulations.put(pathToFollow, this.foundToursPopulations.get(pathToFollow) - 1);
			}
		}
	}
}
