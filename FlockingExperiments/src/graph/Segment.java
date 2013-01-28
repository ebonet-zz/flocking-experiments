package graph;

/**
 * TODO Put here a description of what this class does.
 * 
 * @author Balthazar. Created Jan 22, 2013.
 */
public class Segment implements Comparable<Segment> {
	public Position startLocation;
	public Position exclusiveEndLocation;
	public int currentOccupancy;
	public int maxOccupancy;

	public Segment(Position location, double segmentLength, int maxOccupancy) {
		this.startLocation = location;
		this.exclusiveEndLocation = this.startLocation.clone();
		this.exclusiveEndLocation.deslocate(segmentLength);
		this.maxOccupancy = maxOccupancy;
		this.currentOccupancy = 0;
	}

	public boolean isFull() {
		return this.currentOccupancy >= this.maxOccupancy;
	}

	public boolean contains(Position pos) {
		if (pos == null) {
			return false;
		}

		if (!pos.isSameEdge(this.startLocation)) {
			return false;
		}

		if (pos.isAfterOrEqual(this.exclusiveEndLocation.distanceFromStart)) {
			return false;
		}

		if (pos.isBefore(this.startLocation.distanceFromStart)) {
			return false;
		}

		return true;
	}

	@Override
	public int compareTo(Segment s) {
		return this.startLocation.compareTo(s.startLocation);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.currentOccupancy;
		result = prime * result + ((this.exclusiveEndLocation == null) ? 0 : this.exclusiveEndLocation.hashCode());
		result = prime * result + ((this.startLocation == null) ? 0 : this.startLocation.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Segment other = (Segment) obj;
		if (this.currentOccupancy != other.currentOccupancy)
			return false;
		if (this.exclusiveEndLocation == null) {
			if (other.exclusiveEndLocation != null)
				return false;
		} else if (!this.exclusiveEndLocation.equals(other.exclusiveEndLocation))
			return false;
		if (this.startLocation == null) {
			if (other.startLocation != null)
				return false;
		} else if (!this.startLocation.equals(other.startLocation))
			return false;
		return true;
	}

}
