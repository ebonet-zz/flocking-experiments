package graph;

import java.util.ArrayList;

/**
 * Models a Tour around the graph for the ACS-TSP problem.
 * 
 * @author Balthazar. Created Dec 10, 2012.
 */
public class Tour {
	/**
	 * The visited locations for this tour, in order
	 */
	public ArrayList<Integer> locations;

	/**
	 * The total cost of the tour
	 */
	double lastCalculatedCost;

//	/**
//	 * The iteration when this tour was found
//	 */
//	int foundInIteration;

	/**
	 * Basic constructor
	 */
	public Tour() {
		this.locations = new ArrayList<Integer>();
	}

	/**
	 * Adds a location to this tour history
	 * 
	 * @param locationIndex
	 */
	public void offer(int locationIndex) {
		this.locations.add(locationIndex);
	}

	/**
	 * Finds the position of the given location in the list
	 * 
	 * @param location
	 * @return the position index inside the list
	 */
	public int indexOf(Integer location) {
		return this.locations.indexOf(location);
	}

	/**
	 * Gets the location by the given index
	 * 
	 * @param index
	 * @return the node index in under the sequence index passed in
	 */
	public int get(int index) {
		return this.locations.get(index);
	}

	/**
	 * Resets the tour, removing all locations and costs calculated.
	 * 
	 */
	public void clear() {
		this.locations.clear();
		this.lastCalculatedCost = 0;
		//this.foundInIteration = 0;
	}

	/**
	 * Gets size (the amount of locations) of the Tour
	 * 
	 * @return The amount of locations in the Tour
	 */
	public int size() {
		return this.locations.size();
	}

	/**
	 * Calculates the distance cost of the tour given a distance graph.
	 * 
	 * @param distanceGraph
	 * @return The total cost of the tour
	 */
	public Integer getCost(TraditionalGraph distanceGraph) {
		int cost = 0;
		for (int i = 1; i <= this.locations.size() - 1; i++) {
			cost += distanceGraph.getEdgeLength(this.locations.get(i - 1), this.locations.get(i));
		}
		return cost;
	}

	/**
	 * Calculates and stores the total cost of this tour using the given graph
	 *
	 * @param distanceGraph
	 */
	public void calculateCost(TraditionalGraph distanceGraph) {
		this.lastCalculatedCost = getCost(distanceGraph);
	}

	/**
	 * Retrieves the last location in the tour.
	 * 
	 * @return The las visited location
	 */
	public Integer lastLocation() {
		return this.locations.get(this.locations.size() - 1);
	}

	/**
	 * Shift the order of the tour so 0 is the first visited city. This should not alter the cost, since the path is
	 * closed.
	 */
	public void shiftToZero() {
		if (this.locations.size() > 2 && this.locations.get(0) != 0
				&& this.locations.get(0) == this.locations.get(this.locations.size() - 1)) {
			int zeroPos = this.locations.indexOf(new Integer(0));
			ArrayList<Integer> temp = new ArrayList<Integer>();
			temp.addAll(this.locations.subList(zeroPos, this.locations.size()));
			temp.addAll(this.locations.subList(1, zeroPos));
			temp.add(0);

			if (temp.get(1) > temp.get(temp.size() - 2)) {
				this.locations.clear();
				for (int i = temp.size() - 1; i >= 0; i--) {
					this.locations.add(temp.get(i));
				}
			} else {
				this.locations = temp;
			}
		}
	}

	@Override
	public int hashCode() {
		return this.locations.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (obj.getClass() != getClass())
			return false;
		Tour other = (Tour) obj;
		return this.locations.equals(other.locations);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append("Tour: ");
		sb.append(this.locations.toString());
		sb.append("\n");
		sb.append("Length: ");
		sb.append(this.lastCalculatedCost);
		sb.append("\n");
		sb.append("Size: ");
		sb.append(this.locations.size());
		sb.append("\n");
//		sb.append("Found in iteration: ");
//		sb.append(this.foundInIteration);
//		sb.append("\n");
		return sb.toString();
	}
}
