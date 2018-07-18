package com.softwaremagico.tm.random.selectors;

public enum CombatPreferences implements IRandomPreferences {
	PEACEFUL(0, 1),

	FAIR(2, 5),

	BELLIGERENT(4, 10);

	private final int minimumValue;
	private final int maximumValue;

	private CombatPreferences(int minimumValue, int maximumValue) {
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
