package com.softwaremagico.tm.random.selectors;

public enum TechnologicalPreferences implements IRandomPreferences {

	PREHISTORIC(0, 1),

	MEDIEVAL(1, 2),

	MODERN(3, 5),

	FUTURIST(4, 7),

	MAXIMAL(7, 10);

	private final int minimumValue;
	private final int maximumValue;

	private TechnologicalPreferences(int minimumValue, int maximumValue) {
		this.maximumValue = maximumValue;
		this.minimumValue = minimumValue;
	}

	@Override
	public int maximumValue() {
		return maximumValue;
	}

	@Override
	public int minimumValue() {
		return minimumValue;
	}

}
