package com.softwaremagico.tm.random;

import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.FreeStyleCharacterCreation;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.characteristics.CharacteristicType;
import com.softwaremagico.tm.log.MachineLog;
import com.softwaremagico.tm.random.selectors.BodyPreferences;
import com.softwaremagico.tm.random.selectors.CombatPreferences;
import com.softwaremagico.tm.random.selectors.IRandomPreferences;
import com.softwaremagico.tm.random.selectors.TechnologicalPreferences;

public class RandomCharacteristics {
	private CharacterPlayer characterPlayer;
	private final Set<IRandomPreferences> preferences;
	private Random rand = new Random();

	// Weight -> Characteristic.
	private final TreeMap<Integer, Characteristic> weightedCharacteristics;
	private final int totalWeight;

	public RandomCharacteristics(CharacterPlayer characterPlayer, Set<IRandomPreferences> preferences) {
		this.characterPlayer = characterPlayer;
		this.preferences = preferences;
		weightedCharacteristics = assignCharacteristicsWeight();
		totalWeight = assignTotalWeight();
		spendCharacteristicsPoints();
	}

	public void spendCharacteristicsPoints() {
		// Set minimum values of characteristics.
		assignMinimumValuesOfCharacteristics();

		// Assign random values by weight
		while (characterPlayer.getCharacteristicsTotalPoints() < FreeStyleCharacterCreation.CHARACTERISTICS_POINTS) {
			Characteristic selectedCharacteristic = selectCharacteristicByWeight();
			MachineLog.debug(this.getClass().getName(), "Selected characteristic is '" + selectedCharacteristic.getName() + "'.");
			if (selectedCharacteristic.getValue() < FreeStyleCharacterCreation.MAX_INITIAL_SKILL_VALUE) {
				selectedCharacteristic.setValue(selectedCharacteristic.getValue() + 1);
			}
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
		int count = 0;

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
