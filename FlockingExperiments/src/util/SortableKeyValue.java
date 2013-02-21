package util;

/**
 * Helper class for the sorting methods.
 * 
 * @param <K>
 *            keyObject type
 * @param <V>
 *            valueToUseOnSorting type
 * @author Balthazar. Created Dec 10, 2012.
 */
public class SortableKeyValue<K, V extends Comparable<V>> implements Comparable<SortableKeyValue<K, V>> {

	/** The valuable data to be sorted. */
	public K keyObject;

	/**
	 * The property that is going to determine the order.
	 */
	public V valueToUseOnSorting;

	/**
	 * Basic constructor.
	 * 
	 * @param objectToBeSorted
	 *            the object to be sorted
	 * @param propertyToUseOnSorting
	 *            the property to use on sorting
	 */
	public SortableKeyValue(K objectToBeSorted, V propertyToUseOnSorting) {
		this.keyObject = objectToBeSorted;
		this.valueToUseOnSorting = propertyToUseOnSorting;
	}

	/**
	 * Compare to.
	 * 
	 * @param o
	 *            the o
	 * @return the int
	 */
	@Override
	public int compareTo(SortableKeyValue<K, V> o) {
		return (this.valueToUseOnSorting.compareTo(o.valueToUseOnSorting));
	}

	/**
	 * To string.
	 * 
	 * @return the string
	 */
	@Override
	public String toString() {
		return "{ " + this.keyObject.toString() + " , " + this.valueToUseOnSorting.toString() + " }";
	}
}