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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.benefices.BeneficeDefinition;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.random.selectors.IRandomPreference;

public class ProfileMerger {
	private static final String DEFAULT_ID = "merged_profile";

	public static RandomProfile merge(String language, IRandomProfile... profiles) throws InvalidXmlElementException {
		if (profiles == null || profiles.length == 0) {
			return null;
		}

		return merge(new HashSet<IRandomProfile>(Arrays.asList(profiles)), language);
	}

	public static RandomProfile merge(Set<IRandomProfile> profiles, String language) throws InvalidXmlElementException {
		return merge(profiles, new HashSet<IRandomPreference>(), new HashSet<AvailableSkill>(),
				new HashSet<AvailableSkill>(), new HashSet<BeneficeDefinition>(), new HashSet<BeneficeDefinition>(),
				language);
	}

	public static RandomProfile merge(Set<IRandomProfile> profiles, Set<IRandomPreference> extraPreferences,
			Set<AvailableSkill> requiredSkills, Set<AvailableSkill> suggestedSkills,
			Set<BeneficeDefinition> mandatoryBenefices, Set<BeneficeDefinition> suggestedBenefices, String language)
			throws InvalidXmlElementException {
		if (profiles == null) {
			profiles = new HashSet<>();
		}

		if (extraPreferences == null) {
			extraPreferences = new HashSet<>();
		}

		// Store all information in a new profile.
		final RandomProfile finalProfile = new RandomProfile(DEFAULT_ID, "", language);

		// Merge profiles
		for (final IRandomProfile profile : profiles) {
			// Merge preferences.
			mergePreferences(finalProfile.getPreferences(), profile.getPreferences());

			// Merge characteristics.
			mergeCharacteristics(finalProfile.getCharacteristicsMinimumValues(),
					profile.getCharacteristicsMinimumValues());

			// Merge Skills.
			mergeSkills(finalProfile.getRequiredSkills(), profile.getRequiredSkills());

			// Merge Skills
			mergeSkills(finalProfile.getSuggestedSkills(), profile.getSuggestedSkills());

			mergeBenefices(finalProfile.getMandatoryBenefices(), profile.getMandatoryBenefices());

			mergeBenefices(finalProfile.getSuggestedBenefices(), profile.getSuggestedBenefices());

		}

		// Add selected preferences with more priority.
		mergePreferences(extraPreferences, finalProfile.getPreferences());
		finalProfile.getPreferences().clear();
		finalProfile.getPreferences().addAll(extraPreferences);

		mergeSkills(requiredSkills, finalProfile.getRequiredSkills());
		finalProfile.getRequiredSkills().clear();
		finalProfile.getRequiredSkills().addAll(requiredSkills);

		mergeSkills(suggestedSkills, finalProfile.getSuggestedSkills());
		finalProfile.getSuggestedSkills().clear();
		finalProfile.getSuggestedSkills().addAll(suggestedSkills);

		mergeBenefices(mandatoryBenefices, finalProfile.getMandatoryBenefices());
		finalProfile.getMandatoryBenefices().clear();
		finalProfile.getMandatoryBenefices().addAll(mandatoryBenefices);

		mergeBenefices(suggestedBenefices, finalProfile.getSuggestedBenefices());
		finalProfile.getSuggestedBenefices().clear();
		finalProfile.getSuggestedBenefices().addAll(suggestedBenefices);

		return finalProfile;
	}

	private static void mergeCharacteristics(Map<CharacteristicName, Integer> originalCharacteristicsMinimumValues,
			Map<CharacteristicName, Integer> preferredCharacteristicsMinimumValues) {
		// Merge Characteristics
		for (final Entry<CharacteristicName, Integer> entry : preferredCharacteristicsMinimumValues.entrySet()) {
			originalCharacteristicsMinimumValues.put(entry.getKey(), entry.getValue());
		}
	}

	private static void mergeBenefices(Set<BeneficeDefinition> originalBenefices, Set<BeneficeDefinition> extraBenefices) {
		originalBenefices.addAll(extraBenefices);
	}

	private static void mergeSkills(Set<AvailableSkill> originalRequiredSkills, Set<AvailableSkill> requiredSkills) {
		// Merge Characteristics
		originalRequiredSkills.addAll(requiredSkills);
	}

	private static void mergePreferences(Set<IRandomPreference> originalPreferences,
			Set<IRandomPreference> preferredPreferences) {
		for (final IRandomPreference preferredPreference : preferredPreferences) {
			originalPreferences = removeAny(originalPreferences, preferredPreference);
		}
		originalPreferences.addAll(preferredPreferences);
	}

	private static Set<IRandomPreference> removeAny(Set<IRandomPreference> originalPreferences,
			IRandomPreference preferenceToRemove) {
		for (final IRandomPreference randomPreference : new HashSet<>(originalPreferences)) {
			if (randomPreference.getClass().equals(preferenceToRemove.getClass())) {
				originalPreferences.remove(randomPreference);
			}
		}
		return originalPreferences;
	}

}
