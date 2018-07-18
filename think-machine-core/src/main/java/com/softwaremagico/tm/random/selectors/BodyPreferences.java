package com.softwaremagico.tm.random.selectors;

public enum BodyPreferences implements IRandomPreferences {
	MIND(3, 8),

	BODY(3, 8),

	SPIRIT(3, 8);

	private final int minimumValue;
	private final int maximumValue;

	private BodyPreferences(int minimumValue, int maximumValue) {
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
