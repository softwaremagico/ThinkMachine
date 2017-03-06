package com.softwaremagico.tm.random;

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

	public RandomizeCharacter(CharacterPlayer characterPlayer, int bodyLevel, int mentalLevel, int spiritLevel, int experiencePoints) {
		this.characterPlayer = characterPlayer;
		this.bodyLevel = bodyLevel;
		this.mentalLevel = mentalLevel;
		this.spiritLevel = spiritLevel;
		this.experiencePoints = experiencePoints;
	}

	public void createCharacter() {
		weightedCharacteristics = assignCharacteristicsWeight();
		spendCharacteristicsPoints();
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
