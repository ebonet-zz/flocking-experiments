package graph;

import java.util.Formatter;
import java.util.Locale;

/**
 * Class that represents a specific location in a graph edge
 * 
 * @author Balthazar. Created Jan 22, 2013.
 */
public final class Position implements Cloneable, Comparable<Position> {
	public final Edge edge;
	public final double distanceFromStart;

	public Position(Edge edge, double distance) {
		this.edge = edge;
		this.distanceFromStart = distance;
	}

	public boolean canDeslocate(double distance) {
		if (new Double(getDistanceToEdgeEnd()).compareTo(distance) >= 0)
			return true;
		return false;
	}

	public Position deslocate(double distance) {
		return new Position(this.edge, this.distanceFromStart + distance);
	}

	public double getDistance() {
		return this.distanceFromStart;
	}

	public double getDistanceToEdgeEnd() {
		return this.edge.getLength() - this.distanceFromStart;
	}

	public int getFrom() {
		return this.edge.getFrom();
	}

	public int getTo() {
		return this.edge.getTo();
	}

	public boolean isAfterOrEqual(double l) {
		return new Double(this.distanceFromStart).compareTo(l) >= 0;
	}

	public boolean isAfterOrEqual(Position p) {
		return isAfterOrEqual(p.distanceFromStart);
	}

	public boolean isBefore(double l) {
		return new Double(this.distanceFromStart).compareTo(l) < 0;
	}

	public boolean isBefore(Position p) {
		return isBefore(p.distanceFromStart);
	}

	public boolean isSameEdge(int from, int to) {
		return this.edge.isSameEdge(from, to);
	}

	public boolean isSameEdge(Position p) {
		return this.edge.isSameEdge(p.edge);
	}

	@Override
	public Position clone() {
		return new Position(this.edge, this.distanceFromStart);
	}

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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb, Locale.US);
		sb.append(this.edge.toString() + " at ");
		formatter.format("%.0f, ", this.distanceFromStart);
		return sb.toString();
	}
}
