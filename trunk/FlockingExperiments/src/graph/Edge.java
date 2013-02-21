package graph;

/**
 * Represents an edge between two nodes in a graph.
 * 
 * @author Balthazar. Created Jan 25, 2013.
 */
public final class Edge implements Comparable<Edge> {

	/** The from node index. */
	private final int fromNodeIndex;

	/** The to node index. */
	private final int toNodeIndex;

	/** The length. */
	private final int length;

	/**
	 * Instantiates a new edge.
	 * 
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @param length
	 *            the length
	 */
	public Edge(int from, int to, int length) {
		this.fromNodeIndex = from;
		this.toNodeIndex = to;
		this.length = length;
	}

	/**
	 * Gets the from.
	 * 
	 * @return the from
	 */
	public int getFrom() {
		return this.fromNodeIndex;
	}

	/**
	 * Gets the length.
	 * 
	 * @return the length
	 */
	public int getLength() {
		return this.length;
	}

	/**
	 * Gets the to.
	 * 
	 * @return the to
	 */
	public int getTo() {
		return this.toNodeIndex;
	}

	/**
	 * Checks if is same edge.
	 * 
	 * @param p
	 *            the p
	 * @return true, if is same edge
	 */
	public boolean isSameEdge(Edge p) {
		return isSameEdge(p.fromNodeIndex, p.toNodeIndex);
	}

	/**
	 * Checks if is same edge.
	 * 
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @return true, if is same edge
	 */
	public boolean isSameEdge(int from, int to) {
		return (from == this.fromNodeIndex && to == this.toNodeIndex)
				|| (from == this.toNodeIndex && to == this.fromNodeIndex);
	}

	/**
	 * Checks if is strictly same edge and direction.
	 * 
	 * @param e
	 *            the e
	 * @return true, if is strictly same edge
	 */
	public boolean isStrictlySameEdge(Edge e) {
		return isStrictlySameEdge(e.fromNodeIndex, e.toNodeIndex);
	}

	/**
	 * Checks if is strictly same edge and direction.
	 * 
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @return true, if is strictly same edge
	 */
	public boolean isStrictlySameEdge(int from, int to) {
		return (from == this.fromNodeIndex && to == this.toNodeIndex);
	}

	/**
	 * Compare to.
	 * 
	 * @param s
	 *            the s
	 * @return the int
	 */
	@Override
	public int compareTo(Edge s) {
		if (s != null) {
			if (s.fromNodeIndex != this.fromNodeIndex) {
				return this.fromNodeIndex - s.fromNodeIndex;
			}

			if (s.toNodeIndex != this.toNodeIndex) {
				return this.toNodeIndex - s.toNodeIndex;
			}

			return 0;
		}

		return -1;
	}

	/**
	 * Equals.
	 * 
	 * @param obj
	 *            the obj
	 * @return true, if successful
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge other = (Edge) obj;
		if (this.fromNodeIndex != other.fromNodeIndex)
			return false;
		if (this.length != other.length)
			return false;
		if (this.toNodeIndex != other.toNodeIndex)
			return false;
		return true;
	}

	/**
	 * Hash code.
	 * 
	 * @return the int
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.fromNodeIndex;
		result = prime * result + this.length;
		result = prime * result + this.toNodeIndex;
		return result;
	}

	/**
	 * To string.
	 * 
	 * @return the string
	 */
	@Override
	public String toString() {
		return this.getFrom() + "->" + this.getTo() + "[length:" + this.getLength() + "]";
	}

}
