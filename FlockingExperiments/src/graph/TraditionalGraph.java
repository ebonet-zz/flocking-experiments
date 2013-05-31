package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import util.SortableKeyValue;

/**
 * Supporting class for graph representation.
 * 
 * @author Balthazar. Created Jan 22, 2013.
 */
public class TraditionalGraph {

	/** The Constant INVALID_VALUE for inexistant edges. */
	public static final int INVALID_VALUE = -1;

	/** The distance matrix. */
	public int[][] distanceMatrix;

	/** The number of nodes. */
	int numberOfNodes;

	HashMap<Integer, ArrayList<Integer>> neighborsCache = new HashMap<Integer, ArrayList<Integer>>();
	HashMap<Integer, ArrayList<SortableKeyValue<Integer, Integer>>> sortableNeighborsCache = new HashMap<Integer, ArrayList<SortableKeyValue<Integer, Integer>>>();

	/**
	 * Fixed width graph constructor.
	 * 
	 * @param numberOfNodes
	 *            the number of nodes
	 */
	public TraditionalGraph(int numberOfNodes) {
		this.numberOfNodes = numberOfNodes;
		this.distanceMatrix = new int[numberOfNodes][numberOfNodes];
		fillAll(INVALID_VALUE, this.distanceMatrix);
	}

	/**
	 * Creates a graph from a distance matrix.
	 * 
	 * @param dMatrix
	 *            The distance matrix
	 */
	public TraditionalGraph(int[][] dMatrix) {
		this.numberOfNodes = dMatrix[0].length;
		this.distanceMatrix = dMatrix.clone();
	}

	/**
	 * Fills every matrix cell with the given value.
	 * 
	 * @param value
	 *            the value
	 * @param matrix
	 *            the matrix
	 */
	public void fillAll(int value, int[][] matrix) {
		for (int i = 0; i < matrix.length; i++)
			java.util.Arrays.fill(matrix[i], value);
		neighborsCache.clear();
		sortableNeighborsCache.clear();
	}

	/**
	 * Returns the value of the field called 'numberOfNodes'.
	 * 
	 * @return Returns the numberOfNodes.
	 */
	public int getNumberOfNodes() {
		return this.numberOfNodes;
	}

	/**
	 * Retrieves the distances between two nodes.
	 * 
	 * @param nodeIndexA
	 *            the node index a
	 * @param nodeIndexB
	 *            the node index b
	 * @return The distance
	 */
	public int getEdgeLength(int nodeIndexA, int nodeIndexB) {
		return this.distanceMatrix[nodeIndexA][nodeIndexB];
	}

	/**
	 * Creates a new link between the nodes with the current edge value for both ways.
	 * 
	 * @param nodeIndexA
	 *            the node index a
	 * @param nodeIndexB
	 *            the node index b
	 * @param edgeValue
	 *            the edge value
	 */
	public void setEdgeLength(int nodeIndexA, int nodeIndexB, int edgeValue) {
		if (edgeValue != this.distanceMatrix[nodeIndexA][nodeIndexB]) {
			this.sortableNeighborsCache.remove(nodeIndexA);
			this.sortableNeighborsCache.remove(nodeIndexB);
		}

		if (edgeValue < 0 || this.distanceMatrix[nodeIndexA][nodeIndexB] == INVALID_VALUE) {
			this.neighborsCache.remove(nodeIndexA);
			this.neighborsCache.remove(nodeIndexB);
		}

		this.distanceMatrix[nodeIndexA][nodeIndexB] = edgeValue;
		this.distanceMatrix[nodeIndexB][nodeIndexA] = edgeValue;
	}

	/**
	 * Retrieve the neighbors and cost properties wrapped in a sortable location object
	 * 
	 * @param nodeIndex
	 * @return the neighbors and cost properties
	 */
	public ArrayList<SortableKeyValue<Integer, Integer>> getSortableNeighborsOf(int nodeIndex) {
		if (this.sortableNeighborsCache.containsKey(nodeIndex)) {
			return this.sortableNeighborsCache.get(nodeIndex);
		}

		ArrayList<SortableKeyValue<Integer, Integer>> neighborIndexes = new ArrayList<SortableKeyValue<Integer, Integer>>();
		for (int i = 0; i < this.numberOfNodes; i++) {
			if (this.distanceMatrix[nodeIndex][i] != INVALID_VALUE) {
				neighborIndexes.add(new SortableKeyValue<Integer, Integer>(i, this.distanceMatrix[nodeIndex][i]));
			}
		}

		this.sortableNeighborsCache.put(nodeIndex, neighborIndexes);

		return neighborIndexes;
	}

	/**
	 * Returns an array with the indexes that are neighbors to this one.
	 * 
	 * @param nodeIndex
	 *            the node index
	 * @return The array of neighbor indexes
	 */
	public Integer[] getArrayOfNeighborsOf(int nodeIndex) {
		ArrayList<Integer> neighbors = getNeighborsOf(nodeIndex);
		return neighbors.toArray(new Integer[neighbors.size()]);
	}

	/**
	 * Returns an array with the indexes that are neighbors to this one.
	 * 
	 * @param nodeIndex
	 *            the node index
	 * @return The array of neighbor indexes
	 */
	public ArrayList<Integer> getNeighborsOf(int nodeIndex) {
		if (this.neighborsCache.containsKey(nodeIndex)) {
			return this.neighborsCache.get(nodeIndex);
		}

		ArrayList<Integer> neighborIndexes = new ArrayList<Integer>();
		for (int i = 0; i < this.numberOfNodes; i++) {
			if (this.distanceMatrix[nodeIndex][i] != INVALID_VALUE) {
				neighborIndexes.add(i);
			}
		}

		this.neighborsCache.put(nodeIndex, neighborIndexes);

		return neighborIndexes;
	}

	/**
	 * Sorts the neighbors by edge value (distance) and returns the sorted set.
	 * 
	 * @param nodeIndex
	 *            the node index
	 * @return an ArrayList with the neighbors sorted by distance
	 */
	public ArrayList<Integer> getClosestNeighborsSortedByDistance(int nodeIndex) {
		return getClosestNeighborsSortedByDistance(nodeIndex, this.numberOfNodes);
	}

	/**
	 * Sorts the neighbors by edge value (distance) and returns the sorted set.
	 * 
	 * @param nodeIndex
	 *            the node index
	 * @param maxListSize
	 *            the max list size
	 * @return an ArrayList with the neighbors sorted by distance
	 */
	public ArrayList<Integer> getClosestNeighborsSortedByDistance(int nodeIndex, int maxListSize) {
		ArrayList<SortableKeyValue<Integer, Integer>> closestNeighbors = getSortableNeighborsOf(nodeIndex);
		Collections.sort(closestNeighbors);

		ArrayList<Integer> neighborIndexes = new ArrayList<Integer>();

		List<SortableKeyValue<Integer, Integer>> sortedNeighborsSublist = null;
		if (maxListSize < closestNeighbors.size()) {
			sortedNeighborsSublist = closestNeighbors.subList(0, maxListSize);
		} else {
			sortedNeighborsSublist = closestNeighbors;
		}

		for (SortableKeyValue<Integer, Integer> c : sortedNeighborsSublist) {
			neighborIndexes.add(c.keyObject);
		}

		return neighborIndexes;
	}

	/**
	 * Helper method to print a matrix line.
	 * 
	 * @param lineNumber
	 *            the line number
	 * @return a matrix line
	 */
	private String printLine(int lineNumber) {
		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb, Locale.US);
		formatter.format("%3d [", lineNumber);
		for (int i = 0; i < this.numberOfNodes; i++) {
			formatter.format("%3d, ", this.distanceMatrix[lineNumber][i]);
		}
		formatter.close();
		sb.deleteCharAt(sb.length() - 1);
		sb.deleteCharAt(sb.length() - 1);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * To string.
	 * 
	 * @return the string
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb, Locale.US);
		sb.append("Ind. ");
		for (int i = 0; i < this.numberOfNodes; i++) {
			formatter.format("%3d, ", i);
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.deleteCharAt(sb.length() - 1);
		sb.append("\n");
		formatter.close();

		for (int i = 0; i < this.numberOfNodes; i++) {
			sb.append(printLine(i) + "\n");
		}
		return sb.toString();
	}

	public int[][] getDistanceMatrix() {
		return distanceMatrix;
	}

}
