package util;

import java.util.List;
import java.util.Random;

/**
 * Weighted Roulette Wheel Selector
 * 
 * @author Balthazar. Created Dec 10, 2012.
 */
public class WeightedRouletteWheelSelector {
	/**
	 * The items that might be selected
	 */
	List<SortableKeyValue<?, Double>> items;

	@SuppressWarnings("javadoc")
	Random rand = new Random();

	/**
	 * The cumulative sum of all the properties.
	 */
	double totalSum = 0;

	/**
	 * Constructor that calculates the totalSum
	 * 
	 * @param items
	 */
	public WeightedRouletteWheelSelector(List<SortableKeyValue<?, Double>> items) {
		this.items = items;
		for (SortableKeyValue<?, Double> item : items) {
			this.totalSum += item.valueToUseOnSorting;
		}
	}

	/**
	 * Selects one item at random based on the weighted probabilities
	 * 
	 * @return the selected item
	 */
	public SortableKeyValue<?, Double> getRandom() {
		double rolledValue = this.rand.nextDouble() * this.totalSum;
		double cumulativeSum = 0;
		int i = 0;
		while (cumulativeSum <= rolledValue) {
			cumulativeSum += this.items.get(i++).valueToUseOnSorting;
		}
		return this.items.get(i - 1);
	}
}
