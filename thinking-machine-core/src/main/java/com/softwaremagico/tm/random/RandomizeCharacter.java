package com.softwaremagico.tm.random;

/*-
 * #%L
 * The Thinking Machine (Core)
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

import java.util.Random;
import java.util.TreeMap;

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.FreeStyleCharacterCreation;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.characteristics.CharacteristicType;
import com.softwaremagico.tm.log.MachineLog;

public class RandomizeCharacter {
	private int bodyLevel;
	private int mentalLevel;
	private int spiritLevel;
	private CharacterPlayer characterPlayer;
	private int experiencePoints;
	private TreeMap<Integer, CharacteristicName> weightedCharacteristics;
	private Random rand = new Random();

	public RandomizeCharacter(CharacterPlayer characterPlayer, int experiencePoints) {
		this.characterPlayer = characterPlayer;
		this.experiencePoints = experiencePoints;
	}

	public void createCharacter() {
		weightedCharacteristics = assignCharacteristicsWeight();
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

	private void spendSkillsPoints() {
		while (characterPlayer.getSkillsTotalPoints() < FreeStyleCharacterCreation.SKILLS_POINTS) {

		}
	}

	private void spendExperiencePoints() {

	}

	private void spendCharacteristicsPoints() {
		while (characterPlayer.getCharacteristicsTotalPoints() < FreeStyleCharacterCreation.CHARACTERISTICS_POINTS) {
			CharacteristicName selectedCharacteristic = selectCharacteristicByWeight();
			MachineLog.debug(this.getClass().getName(),
					"Selected characteristic is '" + characterPlayer.getCharacteristics().getCharacteristic(selectedCharacteristic) + "'.");
			if (characterPlayer.getCharacteristics().getCharacteristic(selectedCharacteristic).getValue() < FreeStyleCharacterCreation.MAX_INITIAL_SKILL_VALUE)
				characterPlayer.getCharacteristics().getCharacteristic(selectedCharacteristic)
						.setValue(characterPlayer.getCharacteristics().getCharacteristic(selectedCharacteristic).getValue() + 1);
		}
	}

	private TreeMap<Integer, CharacteristicName> assignCharacteristicsWeight() {
		TreeMap<Integer, CharacteristicName> weightedCharacteristics = new TreeMap<>();
		Integer count = 0;
		for (CharacteristicType characteristicType : CharacteristicType.values()) {
			int weight = getWeight(characteristicType);
			if (weight > 0) {
				for (CharacteristicName characteristicName : characteristicType.getCharacteristics()) {
					weightedCharacteristics.put(count, characteristicName);
					count += weight;
				}
			}
		}
		return weightedCharacteristics;
	}

	private int getWeight(CharacteristicType characteristicType) {
		if (CharacteristicType.BODY.equals(characteristicType)) {
			return bodyLevel;
		}
		if (CharacteristicType.MIND.equals(characteristicType)) {
			return mentalLevel;
		}
		if (CharacteristicType.SPIRIT.equals(characteristicType)) {
			return spiritLevel;
		}
		return 0;
	}

	/**
	 * Selects a characteristic depending on its weight.
	 */
	private CharacteristicName selectCharacteristicByWeight() {
		Integer value = new Integer((int) (rand.nextDouble() * (bodyLevel * 3 + mentalLevel * 3 + spiritLevel * 3)));
		return weightedCharacteristics.get(weightedCharacteristics.floorKey(value));
	}
}
