package graph;

import java.util.Formatter;
import java.util.Locale;

/**
 * Class that represents a specific location in a graph edge.
 * 
 * @author Balthazar. Created Jan 22, 2013.
 */
public final class Position implements Cloneable, Comparable<Position> {

	/** The edge. */
	public final Edge edge;

	/** The distance from the start of the edge (from node). */
	public double distanceFromStart;

	/**
	 * Instantiates a new position.
	 * 
	 * @param edge
	 *            the edge
	 * @param distance
	 *            the distance
	 */
	public Position(Edge edge, double distance) {
		this.edge = edge;
		this.distanceFromStart = distance;
	}

	/**
	 * Tests if one can dislocate the given delta inside the same edge.
	 * 
	 * @param distance
	 *            the distance
	 * @return true, if successful
	 */
	public boolean canDislocate(double distance) {
		if (new Double(getDistanceToEdgeEnd()).compareTo(distance) >= 0)
			return true;
		return false;
	}

	/**
	 * Dislocates the given delta.
	 * 
	 * @param distance
	 *            the distance
	 */
	public void dislocate(double distance) {
		this.distanceFromStart += distance;
	}

	/**
	 * Gets the distance from the start of the edge.
	 * 
	 * @return the distance
	 */
	public double getDistanceFromStart() {
		return this.distanceFromStart;
	}

	/**
	 * Gets the distance to edge end.
	 * 
	 * @return the distance to edge end
	 */
	public double getDistanceToEdgeEnd() {
		return this.edge.getLength() - this.distanceFromStart;
	}

	/**
	 * Gets the from node.
	 * 
	 * @return the from node
	 */
	public int getFrom() {
		return this.edge.getFrom();
	}

	/**
	 * Gets the to node.
	 * 
	 * @return the to node
	 */
	public int getTo() {
		return this.edge.getTo();
	}

	/**
	 * Checks if is after or equal a given distance from the start.
	 * 
	 * @param l
	 *            the distance from the start of the edge
	 * @return true, if is after or equal
	 */
	public boolean isAfterOrEqual(double l) {
		return new Double(this.distanceFromStart).compareTo(l) >= 0;
	}

	/**
	 * Checks if is after or equal a given position.
	 * 
	 * @param p
	 *            the position to compare to
	 * @return true, if is after or equal
	 */
	public boolean isAfterOrEqual(Position p) {
		return isAfterOrEqual(p.distanceFromStart);
	}

	/**
	 * Checks if is before a given distance from the start of the edge.
	 * 
	 * @param l
	 *            the distance from the start
	 * @return true, if is before
	 */
	public boolean isBefore(double l) {
		return new Double(this.distanceFromStart).compareTo(l) < 0;
	}

	/**
	 * Checks if is before a given position.
	 * 
	 * @param p
	 *            the position to compare to
	 * @return true, if is before
	 */
	public boolean isBefore(Position p) {
		return isBefore(p.distanceFromStart);
	}

	/**
	 * Checks if is same edge as other position.
	 * 
	 * @param p
	 *            the position to compare to
	 * @return true, if is same edge
	 */
	public boolean isSameEdge(Position p) {
		return this.edge.isSameEdge(p.edge);
	}

	/**
	 * Checks if is strictly same edge as another position, considering the direction.
	 * 
	 * @param pos
	 *            the position to compare to
	 * @return true, if is strictly same edge
	 */
	public boolean isStrictlySameEdge(Position pos) {
		return this.edge.isStrictlySameEdge(pos.edge);
	}

	/**
	 * Clone.
	 * 
	 * @return the cloned position
	 */
	@Override
	public Position clone() {
		return new Position(this.edge, this.distanceFromStart);
	}

	/**
	 * Compare to.
	 * 
	 * @param s
	 *            the s
	 * @return the int
	 */
	@Override
	public int compareTo(Position s) {
		if (s != null) {
			if (!s.isSameEdge(s)) {
				return this.edge.compareTo(s.edge);
			}

			return new Double(this.distanceFromStart).compareTo(s.distanceFromStart);
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
		Position other = (Position) obj;
		if (Double.doubleToLongBits(this.distanceFromStart) != Double.doubleToLongBits(other.distanceFromStart))
			return false;
		if (this.edge == null) {
			if (other.edge != null)
				return false;
		} else if (!this.edge.equals(other.edge))
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
		long temp;
		temp = Double.doubleToLongBits(this.distanceFromStart);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((this.edge == null) ? 0 : this.edge.hashCode());
		return result;
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
		sb.append(this.edge.toString() + " at ");
		formatter.format("%.0f, ", this.distanceFromStart);
		formatter.close();
		return sb.toString();
	}

}
