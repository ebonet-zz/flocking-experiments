package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
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
		this.distanceMatrix[nodeIndexA][nodeIndexB] = edgeValue;
		this.distanceMatrix[nodeIndexB][nodeIndexA] = edgeValue;
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
		ArrayList<Integer> neighborIndexes = new ArrayList<Integer>();
		for (int i = 0; i < this.numberOfNodes; i++) {
			if (this.distanceMatrix[nodeIndex][i] != INVALID_VALUE) {
				neighborIndexes.add(i);
			}
		}

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
		ArrayList<Integer> neighborIndexes = getNeighborsOf(nodeIndex);

		ArrayList<SortableKeyValue<Integer, Integer>> closestNeighbors = new ArrayList<SortableKeyValue<Integer, Integer>>();
		for (Integer n : neighborIndexes) {
			closestNeighbors.add(new SortableKeyValue<Integer, Integer>(n, getEdgeLength(nodeIndex, n)));
		}
		Collections.sort(closestNeighbors);

		neighborIndexes.clear();
		for (SortableKeyValue<Integer, Integer> c : closestNeighbors.subList(0,
				maxListSize > closestNeighbors.size() ? closestNeighbors.size() : maxListSize)) {
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

}
