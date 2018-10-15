package com.softwaremagico.tm.random.profile;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.random.selectors.IRandomPreference;

public abstract class RandomProfile implements IRandomProfile {
	private Set<IRandomPreference> randomPreferences = new HashSet<>();

	protected RandomProfile(Set<IRandomPreference> newPreferences) {
		addPreferences(newPreferences);
	}

	protected static Set<IRandomPreference> removeAny(Set<IRandomPreference> originalPreferences, IRandomPreference preferenceToRemove) {
		for (IRandomPreference randomPreference : new HashSet<>(originalPreferences)) {
			if (randomPreference.getClass().equals(preferenceToRemove.getClass())) {
				originalPreferences.remove(randomPreference);
			}
		}
		return originalPreferences;
	}

	protected static Set<IRandomPreference> mergePreferences(Set<IRandomPreference> originalPreferences, Set<IRandomPreference> preferredPreferences) {
		for (IRandomPreference preferredPreference : preferredPreferences) {
			originalPreferences = removeAny(originalPreferences, preferredPreference);
		}
		originalPreferences.addAll(preferredPreferences);
		return originalPreferences;
	}

	protected void addPreferences(Set<IRandomPreference> newPreferences) {
		randomPreferences = mergePreferences(randomPreferences, newPreferences);
	}

	@Override
	public Map<CharacteristicName, Integer> getCharacteristicsMinimumValues() {
		return new HashMap<>();
	}

	@Override
	public Map<AvailableSkill, Integer> getSkillsMinimumValues() {
		return new HashMap<>();
	}

	@Override
	public Set<Blessing> getBlessings() {
		return new HashSet<>();
	}

	@Override
	public Set<AvailableBenefice> getBenefices() {
		return new HashSet<>();
	}

	@Override
	public int getExperiencePoints() {
		return 0;
	}

	@Override
	public Set<IRandomPreference> getPreferences() {
		return randomPreferences;
	}
}
