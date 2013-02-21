package graph;

/**
 * Represents a subsection of an edge with limited space for Boid occupancy.
 * 
 * @author Balthazar. Created Jan 22, 2013.
 */
public final class Segment implements Comparable<Segment> {

	/** The (inclusive) start location. */
	public final Position startLocation;

	/** The (exclusive) end location. */
	public final Position exclusiveEndLocation;

	/** The max occupancy. */
	public final int maxOccupancy;

	/** The current occupancy. */
	private int currentOccupancy;

	/**
	 * Instantiates a new segment.
	 * 
	 * @param location
	 *            the location
	 * @param segmentLength
	 *            the segment length
	 * @param maxOccupancy
	 *            the max occupancy
	 */
	public Segment(Position location, double segmentLength, int maxOccupancy) {
		this.startLocation = location;
		this.exclusiveEndLocation = this.startLocation.clone();
		this.exclusiveEndLocation.dislocate(segmentLength);
		this.maxOccupancy = maxOccupancy;
		this.currentOccupancy = 0;
	}

	/**
	 * Tests if contains a given position.
	 * 
	 * @param pos
	 *            the position to test
	 * @return true, if contains
	 */
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

	/**
	 * Decrement occupancy.
	 */
	public void decrementOccupancy() {
		if (this.currentOccupancy <= 0) {
			System.out.println("Negative segment occupancy.");
		}
		this.currentOccupancy--;
	}

	/**
	 * Gets the current occupancy.
	 * 
	 * @return the current occupancy
	 */
	public int getCurrentOccupancy() {
		return this.currentOccupancy;
	}

	/**
	 * Increment occupancy.
	 */
	public void incrementOccupancy() {
		if (this.currentOccupancy >= this.maxOccupancy) {
			System.out.println("Max segment occupancy exceeded.");
			throw new RuntimeException("Max segment occupancy exceeded.");
		}
		this.currentOccupancy++;
	}

	/**
	 * Checks if is full.
	 * 
	 * @return true, if is full
	 */
	public boolean isFull() {
		return this.currentOccupancy >= this.maxOccupancy;
	}

	/**
	 * Compare to.
	 * 
	 * @param s
	 *            the s
	 * @return the int
	 */
	@Override
	public int compareTo(Segment s) {
		return this.startLocation.compareTo(s.startLocation);
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

	/**
	 * Hash code.
	 * 
	 * @return the int
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.currentOccupancy;
		result = prime * result + ((this.exclusiveEndLocation == null) ? 0 : this.exclusiveEndLocation.hashCode());
		result = prime * result + ((this.startLocation == null) ? 0 : this.startLocation.hashCode());
		return result;
	}

	/**
	 * To string.
	 * 
	 * @return the string
	 */
	@Override
	public String toString() {
		return String.format("[%.0f %.0f)", this.startLocation.distanceFromStart,
				this.exclusiveEndLocation.distanceFromStart);
	}

}
