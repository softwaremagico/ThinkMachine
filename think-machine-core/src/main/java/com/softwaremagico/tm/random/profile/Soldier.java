package com.softwaremagico.tm.random.profile;

/*-
 * #%L
 * Think Machine (Core)
 * %%
 * Copyright (C) 2017 - 2018 Softwaremagico
 * %%
 * This software is designed by Jorge Hortelano Otero. Jorge Hortelano Otero
 * <softwaremagico@gmail.com> Valencia (Spain).
 *  
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *  
 * You should have received a copy of the GNU General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
