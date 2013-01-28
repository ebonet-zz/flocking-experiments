package graph;

/**
 * Helper class for the sorting methods.
 * 
 * @author Balthazar. Created Dec 10, 2012.
 */
public class SortableLocation implements Comparable<SortableLocation> {
	/**
	 * The location index in the graph
	 */
	public int locationIndex;

	/**
	 * The property that is going to determine the order.
	 */
	public Float locationProperty;

	/**
	 * Basic constructor
	 * 
	 * @param index
	 * @param property
	 */
	public SortableLocation(int index, float property) {
		this.locationIndex = index;
		this.locationProperty = property;
	}

	@Override
	public int compareTo(SortableLocation o) {
		return (this.locationProperty.compareTo(o.locationProperty));
	}
}