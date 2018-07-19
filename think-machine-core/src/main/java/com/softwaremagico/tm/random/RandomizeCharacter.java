package com.softwaremagico.tm.random;

/*-
 * #%L
 * Think Machine (Core)
 * %%
 * Copyright (C) 2017 Softwaremagico
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
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.FreeStyleCharacterCreation;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.characteristics.CharacteristicType;
import com.softwaremagico.tm.log.MachineLog;
import com.softwaremagico.tm.random.exceptions.DuplicatedPreferenceException;
import com.softwaremagico.tm.random.selectors.BodyPreferences;
import com.softwaremagico.tm.random.selectors.CombatPreferences;
import com.softwaremagico.tm.random.selectors.IRandomPreferences;
import com.softwaremagico.tm.random.selectors.TechnologicalPreferences;

public class RandomizeCharacter {
	private CharacterPlayer characterPlayer;
	private int experiencePoints;
	// Weight -> Characteristic.
	private final TreeMap<Integer, Characteristic> weightedCharacteristics;
	private final int totalWeight;
	private final Set<IRandomPreferences> preferences;
	private Random rand = new Random();

	public RandomizeCharacter(CharacterPlayer characterPlayer, int experiencePoints, IRandomPreferences... preferences) throws DuplicatedPreferenceException {
		this.characterPlayer = characterPlayer;
		this.experiencePoints = experiencePoints;
		this.preferences = new HashSet<>(Arrays.asList(preferences));
		weightedCharacteristics = assignCharacteristicsWeight();
		totalWeight = assignTotalWeight();
		checkValidPreferences();
	}

	private void checkValidPreferences() throws DuplicatedPreferenceException {
		Set<Class<? extends IRandomPreferences>> existingPreferences = new HashSet<>();
		// Only one of each class allowed.
		for (IRandomPreferences preference : preferences) {
			if (existingPreferences.contains(preference.getClass())) {
				throw new DuplicatedPreferenceException("Preference '" + preference + "' collides with another preference. Only one of each type is allowed.");
			}
			existingPreferences.add(preference.getClass());
		}
	}

	public void createCharacter() throws InvalidXmlElementException {
		initializeCharacter();
		spendCharacteristicsPoints();
		spendSkillsPoints();
		spendExperiencePoints();
	}

	private void initializeCharacter() {
		// Check if race is set.
		if (characterPlayer.getRace() == null) {

		}
	}

	private void spendSkillsPoints() throws InvalidXmlElementException {
		while (characterPlayer.getSkillsTotalPoints() < FreeStyleCharacterCreation.SKILLS_POINTS) {

		}
	}

	private void spendExperiencePoints() {

	}

	private void spendCharacteristicsPoints() {
		// Set minimum values of characteristics.
		assignMinimumValuesOfCharacteristics();

		// Assign random values by weight
		while (characterPlayer.getCharacteristicsTotalPoints() < FreeStyleCharacterCreation.CHARACTERISTICS_POINTS) {
			Characteristic selectedCharacteristic = selectCharacteristicByWeight();
			MachineLog.debug(this.getClass().getName(), "Selected characteristic is '" + selectedCharacteristic.getName() + "'.");
			if (selectedCharacteristic.getValue() < FreeStyleCharacterCreation.MAX_INITIAL_SKILL_VALUE)
				selectedCharacteristic.setValue(selectedCharacteristic.getValue() + 1);
		}
	}

	private void assignMinimumValuesOfCharacteristics() {
		for (IRandomPreferences preference : preferences) {
			if (preference instanceof TechnologicalPreferences) {
				characterPlayer.getCharacteristic(CharacteristicName.TECH).setValue(((TechnologicalPreferences) preference).minimumValue());
			}
		}
	}

	private TreeMap<Integer, Characteristic> assignCharacteristicsWeight() {
		TreeMap<Integer, Characteristic> weightedCharacteristics = new TreeMap<>();
		Integer count = 0;

		for (Characteristic characteristic : characterPlayer.getCharacteristics()) {
			int weight = getWeight(characteristic);
			if (weight > 0) {
				weightedCharacteristics.put(count, characteristic);
				count += weight;
			}
		}

		return weightedCharacteristics;
	}

	private Integer assignTotalWeight() {
		int totalWeight = 0;
		for (Integer value : weightedCharacteristics.keySet()) {
			totalWeight += value;
		}
		return totalWeight;
	}

	/**
	 * Assign a weight to each characteristic depending on the preferences
	 * selected.
	 * 
	 * @param characteristicType
	 * @return
	 */
	private int getWeight(Characteristic characteristic) {
		if (characteristic == null) {
			return 0;
		}
		int weight = 1;
		if (CharacteristicType.BODY.equals(characteristic.getType())) {
			if (preferences.contains(BodyPreferences.BODY)) {
				weight += 2;
			}
			if (preferences.contains(CombatPreferences.BELLIGERENT)) {
				weight += 1;
			}
		}
		if (CharacteristicType.MIND.equals(characteristic.getType())) {
			if (preferences.contains(BodyPreferences.MIND)) {
				weight += 2;
			}
		}
		if (CharacteristicType.SPIRIT.equals(characteristic.getType())) {
			if (preferences.contains(BodyPreferences.SPIRIT)) {
				weight += 2;
			}
		}
		return weight;
	}

	/**
	 * Selects a characteristic depending on its weight.
	 */
	private Characteristic selectCharacteristicByWeight() {
		Integer value = new Integer((int) (rand.nextDouble() * totalWeight));
		return weightedCharacteristics.get(weightedCharacteristics.floorKey(value));
	}
}
