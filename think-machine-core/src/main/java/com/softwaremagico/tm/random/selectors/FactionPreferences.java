package com.softwaremagico.tm.random.selectors;

import java.util.Set;

public enum FactionPreferences implements IRandomPreferences {

	NOBILITY,

	CHURCH,

	GUILD,

	XENO;

	@Override
	public int maximumValue() {
		return 0;
	}

	@Override
	public int minimumValue() {
		return 0;
	}

	public static FactionPreferences getSelected(Set<IRandomPreferences> preferences) {
		for (IRandomPreferences preference : preferences) {
			if (preference instanceof FactionPreferences) {
				return (FactionPreferences) preference;
			}
		}
		return null;
	}

}
