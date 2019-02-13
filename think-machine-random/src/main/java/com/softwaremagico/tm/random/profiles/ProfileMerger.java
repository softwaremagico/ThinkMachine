package com.softwaremagico.tm.random.profiles;

/*-
 * #%L
 * Think Machine (Random Generator)
 * %%
 * Copyright (C) 2017 - 2019 Softwaremagico
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

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.random.selectors.IRandomPreference;

public class ProfileMerger {
	private final static String DEFAULT_ID = "merged_profile";

	public static RandomProfile merge(IRandomProfile... profiles) {
		if (profiles == null || profiles.length == 0) {
			return null;
		}

		RandomProfile finalProfile = new RandomProfile(DEFAULT_ID, "", profiles[0].getLanguage());
		for (IRandomProfile profile : profiles) {
			// Merge preferences.
			mergePreferences(finalProfile.getPreferences(), profile.getPreferences());

			// Merge characteristics.
			mergeCharacteristics(finalProfile.getCharacteristicsMinimumValues(), profile.getCharacteristicsMinimumValues());

		}
		return finalProfile;
	}

	private static void mergeCharacteristics(Map<CharacteristicName, Integer> originalCharacteristicsMinimumValues,
			Map<CharacteristicName, Integer> preferredCharacteristicsMinimumValues) {
		// Merge Characteristics
		for (Entry<CharacteristicName, Integer> entry : preferredCharacteristicsMinimumValues.entrySet()) {
			originalCharacteristicsMinimumValues.put(entry.getKey(), entry.getValue());
		}
	}

	private static void mergePreferences(Set<IRandomPreference> originalPreferences, Set<IRandomPreference> preferredPreferences) {
		for (IRandomPreference preferredPreference : preferredPreferences) {
			originalPreferences = removeAny(originalPreferences, preferredPreference);
		}
		originalPreferences.addAll(preferredPreferences);
	}

	private static Set<IRandomPreference> removeAny(Set<IRandomPreference> originalPreferences, IRandomPreference preferenceToRemove) {
		for (IRandomPreference randomPreference : new HashSet<>(originalPreferences)) {
			if (randomPreference.getClass().equals(preferenceToRemove.getClass())) {
				originalPreferences.remove(randomPreference);
			}
		}
		return originalPreferences;
	}

}
