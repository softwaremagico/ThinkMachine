package com.softwaremagico.tm.random.profile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.random.selectors.CharacteristicsPreferences;
import com.softwaremagico.tm.random.selectors.CombatPreferences;
import com.softwaremagico.tm.random.selectors.IRandomPreference;
import com.softwaremagico.tm.random.selectors.SkillGroupPreferences;
import com.softwaremagico.tm.random.selectors.SpecializationPreferences;
import com.softwaremagico.tm.random.selectors.StatusPreferences;
import com.softwaremagico.tm.random.selectors.TechnologicalPreferences;
import com.softwaremagico.tm.random.selectors.WeaponsPreferences;

public class Soldier extends RandomProfile implements IRandomProfile {
	private final static IRandomPreference[] PREFERENCES = { CombatPreferences.BELLIGERENT, CharacteristicsPreferences.BODY, SkillGroupPreferences.COMBAT,
			SpecializationPreferences.SPECIALIZED, StatusPreferences.LOW, TechnologicalPreferences.MODERN, WeaponsPreferences.HIGH };

	private final static Set<IRandomPreference> RANDOM_PREFERENCES = new HashSet<>(Arrays.asList(PREFERENCES));

	public Soldier() {
		super(RANDOM_PREFERENCES);
	}

	protected Soldier(Set<IRandomPreference> newPreferences) {
		super(RANDOM_PREFERENCES);
		addPreferences(newPreferences);
	}

	@Override
	public Map<CharacteristicName, Integer> getCharacteristicsMinimumValues() {
		Map<CharacteristicName, Integer> characteristicsValues = new HashMap<CharacteristicName, Integer>();
		characteristicsValues.put(CharacteristicName.DEXTERITY, 6);
		return characteristicsValues;
	}
}
