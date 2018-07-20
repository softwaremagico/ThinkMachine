package com.softwaremagico.tm.random.selectors;

import java.util.Set;

public enum RacePreferences implements IRandomPreferences {
	HUMAN,

	OBUN,

	UKAR,

	VOROX;

	@Override
	public int maximumValue() {
		return 0;
	}

	@Override
	public int minimumValue() {
		return 0;
	}

	public static RacePreferences getSelected(Set<IRandomPreferences> preferences) {
		for (IRandomPreferences preference : preferences) {
			if (preference instanceof RacePreferences) {
				return (RacePreferences) preference;
			}
		}
		return null;
	}

}
