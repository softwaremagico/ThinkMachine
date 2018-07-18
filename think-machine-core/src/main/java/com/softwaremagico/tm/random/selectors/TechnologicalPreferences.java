package com.softwaremagico.tm.random.selectors;

public enum TechnologicalPreferences implements IRandomPreferences {

	PREHISTORIC(0, 0, 1),

	MEDIEVAL(1, 1, 2),

	MODERN(2, 3, 5),

	FUTURIST(3, 4, 7),

	MAXIMAL(4, 7, 10);

	private final int minimumValue;
	private final int maximumValue;
	private final int order;

	private TechnologicalPreferences(int order, int minimumValue, int maximumValue) {
		this.maximumValue = maximumValue;
		this.minimumValue = minimumValue;
		this.order = order;
	}

	@Override
	public int maximumValue() {
		return maximumValue;
	}

	@Override
	public int minimumValue() {
		return minimumValue;
	}

	public int getOrder() {
		return order;
	}

	/**
	 * Checks if the current preference is greater equals than other status.
	 * 
	 * @param preference
	 *            status to compare.
	 * @return true if it is passed.
	 */
	public boolean isMoreThan(TechnologicalPreferences preference) {
		return order >= preference.order;
	}

}
