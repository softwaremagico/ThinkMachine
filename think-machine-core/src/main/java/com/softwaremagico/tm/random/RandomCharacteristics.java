package com.softwaremagico.tm.random;

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

public class RandomCharacteristics extends RandomSelector<Characteristic> {

	public RandomCharacteristics(CharacterPlayer characterPlayer, Set<IRandomPreferences> preferences) {
		super(characterPlayer, preferences);
		spendCharacteristicsPoints();
	}

	public void spendCharacteristicsPoints() {
		// Set minimum values of characteristics.
		assignMinimumValuesOfCharacteristics();

		// Assign random values by weight
		while (getCharacterPlayer().getCharacteristicsTotalPoints() < FreeStyleCharacterCreation.CHARACTERISTICS_POINTS) {
			Characteristic selectedCharacteristic = selectElementByWeight();
			if (selectedCharacteristic.getValue() < FreeStyleCharacterCreation.MAX_INITIAL_SKILL_VALUE) {
				selectedCharacteristic.setValue(selectedCharacteristic.getValue() + 1);
			}
		}
	}

	private void assignMinimumValuesOfCharacteristics() {
		for (IRandomPreferences preference : getPreferences()) {
			if (preference instanceof TechnologicalPreferences) {
				getCharacterPlayer().getCharacteristic(CharacteristicName.TECH).setValue(((TechnologicalPreferences) preference).minimumValue());
			}
		}
	}

	@Override
	protected TreeMap<Integer, Characteristic> assignElementsWeight() {
		TreeMap<Integer, Characteristic> weightedCharacteristics = new TreeMap<>();
		int count = 0;

		for (Characteristic characteristic : getCharacterPlayer().getCharacteristics()) {
			int weight = getWeight(characteristic);
			if (weight > 0) {
				weightedCharacteristics.put(count, characteristic);
				count += weight;
			}
		}

		return weightedCharacteristics;
	}

	@Override
	protected int getWeight(Characteristic characteristic) {
		if (characteristic == null) {
			return 0;
		}
		int weight = 1;
		if (CharacteristicType.BODY.equals(characteristic.getType())) {
			if (getPreferences().contains(BodyPreferences.BODY)) {
				weight += 2;
			}
			if (getPreferences().contains(CombatPreferences.BELLIGERENT)) {
				weight += 1;
			}
		}
		if (CharacteristicType.MIND.equals(characteristic.getType())) {
			if (getPreferences().contains(BodyPreferences.MIND)) {
				weight += 2;
			}
		}
		if (CharacteristicType.SPIRIT.equals(characteristic.getType())) {
			if (getPreferences().contains(BodyPreferences.SPIRIT)) {
				weight += 2;
			}
		}
		return weight;
	}

}
