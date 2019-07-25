package com.softwaremagico.tm.random.profiles;

import java.util.ArrayList;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Objects;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.benefices.BeneficeDefinition;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.equipment.armours.Armour;
import com.softwaremagico.tm.character.equipment.shields.Shield;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
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
				new HashSet<Weapon>(), new HashSet<Armour>(), new HashSet<Shield>(), language);
	}

	public static RandomProfile merge(Set<IRandomProfile> profiles, Set<IRandomPreference> extraPreferences,
			Set<AvailableSkill> requiredSkills, Set<AvailableSkill> suggestedSkills,
			Set<BeneficeDefinition> mandatoryBenefices, Set<BeneficeDefinition> suggestedBenefices,
			Set<Weapon> mandatoryWeapons, Set<Armour> mandatoryArmours, Set<Shield> mandatoryShields, String language)
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

			mergeWeapons(finalProfile.getMandatoryWeapons(), profile.getMandatoryWeapons());

			mergeArmours(finalProfile.getMandatoryArmours(), profile.getMandatoryArmours());

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

		mergeWeapons(mandatoryWeapons, finalProfile.getMandatoryWeapons());
		finalProfile.getMandatoryWeapons().clear();
		finalProfile.getMandatoryWeapons().addAll(mandatoryWeapons);

		mergeArmours(mandatoryArmours, finalProfile.getMandatoryArmours());
		finalProfile.getMandatoryArmours().clear();
		finalProfile.getMandatoryArmours().addAll(mandatoryArmours);

		return finalProfile;
	}

	private static void mergeCharacteristics(Set<Characteristic> originalCharacteristicsMinimumValues,
			Set<Characteristic> preferredCharacteristicsMinimumValues) {
		// Merge Characteristics
		for (final Characteristic newCharacteristic : preferredCharacteristicsMinimumValues) {
			boolean added = false;
			for (final Characteristic characteristic : originalCharacteristicsMinimumValues) {
				if (Objects.equal(characteristic.getCharacteristicName(), newCharacteristic.getCharacteristicName())) {
					if (characteristic.getValue() < newCharacteristic.getValue()) {
						characteristic.setValue(newCharacteristic.getValue());
						added = true;
						break;
					}
				}
			}
			if (!added) {
				originalCharacteristicsMinimumValues.add(newCharacteristic);
			}
		}
	}

	private static void mergeBenefices(Set<BeneficeDefinition> originalBenefices,
			Set<BeneficeDefinition> extraBenefices) {
		originalBenefices.addAll(extraBenefices);
	}

	private static void mergeSkills(Set<AvailableSkill> originalRequiredSkills, Set<AvailableSkill> requiredSkills) {
		// Merge Characteristics
		originalRequiredSkills.addAll(requiredSkills);
	}

	private static void mergeWeapons(Set<Weapon> originalWeapons, Set<Weapon> weapons) {
		final List<Weapon> sortedWeapons = new ArrayList<>();
		sortedWeapons.addAll(originalWeapons);
		sortedWeapons.addAll(weapons);
		Collections.sort(sortedWeapons, new Comparator<Weapon>() {

			@Override
			public int compare(Weapon weapon0, Weapon weapon1) {
				return weapon0.getCost() > weapon1.getCost() ? -1 : weapon0.getCost() < weapon1.getCost() ? 1 : 0;
			}
		});
		// Keep only the most expensives ones.
		if (!sortedWeapons.isEmpty()) {
			originalWeapons.clear();
			originalWeapons.addAll(sortedWeapons.subList(0,
					sortedWeapons.size() / 2 > originalWeapons.size() ? sortedWeapons.size() / 2 + 1
							: sortedWeapons.size()));
		}
	}

	private static void mergeArmours(Set<Armour> originalArmour, Set<Armour> armour) {
		final List<Armour> sortedArmour = new ArrayList<>();
		sortedArmour.addAll(originalArmour);
		sortedArmour.addAll(armour);
		Collections.sort(sortedArmour, new Comparator<Armour>() {

			@Override
			public int compare(Armour armour0, Armour armour1) {
				return armour0.getCost() > armour1.getCost() ? -1 : armour0.getCost() < armour1.getCost() ? 1 : 0;
			}
		});
		// Keep the most expensive one
		if (!sortedArmour.isEmpty()) {
			originalArmour.clear();
			originalArmour.add(sortedArmour.get(0));
		}
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
