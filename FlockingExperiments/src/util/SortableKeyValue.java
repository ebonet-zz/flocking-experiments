package util;

/**
 * Helper class for the sorting methods.
 * 
 * @author Balthazar. Created Dec 10, 2012.
 * @param <K>
 *            keyObject type
 * @param <V>
 *            valueToUseOnSorting type
 */
public class SortableKeyValue<K, V extends Comparable<V>> implements Comparable<SortableKeyValue<K, V>> {
	/**
	 * The valuable data to be sorted
	 */
	public K keyObject;

	/**
	 * The property that is going to determine the order.
	 */
	public V valueToUseOnSorting;

	/**
	 * Basic constructor
	 * 
	 * @param objectToBeSorted
	 * @param propertyToUseOnSorting
	 */
	public SortableKeyValue(K objectToBeSorted, V propertyToUseOnSorting) {
		this.keyObject = objectToBeSorted;
		this.valueToUseOnSorting = propertyToUseOnSorting;
	}

	@Override
	public int compareTo(SortableKeyValue<K, V> o) {
		return (this.valueToUseOnSorting.compareTo(o.valueToUseOnSorting));
	}
}