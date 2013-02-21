package graph;

/**
 * Represents an edge between two nodes in a graph
 * 
 * @author Balthazar. Created Jan 25, 2013.
 */
public final class Edge implements Comparable<Edge> {
	private final int fromNodeIndex;
	private final int toNodeIndex;
	private final int length;

	public Edge(int from, int to, int length) {
		this.fromNodeIndex = from;
		this.toNodeIndex = to;
		this.length = length;
	}

	public int getFrom() {
		return this.fromNodeIndex;
	}

	public int getLength() {
		return this.length;
	}

	public int getTo() {
		return this.toNodeIndex;
	}

	public boolean isSameEdge(Edge p) {
		return isSameEdge(p.fromNodeIndex, p.toNodeIndex);
	}

	public boolean isSameEdge(int from, int to) {
		return (from == this.fromNodeIndex && to == this.toNodeIndex)/*
				|| (from == this.toNodeIndex && to == this.fromNodeIndex)*/;
	}

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.fromNodeIndex;
		result = prime * result + this.length;
		result = prime * result + this.toNodeIndex;
		return result;
	}

	@Override
	public String toString() {
		return this.getFrom() + "->" + this.getTo() + "[length:" + this.getLength() + "]";
	}
}
