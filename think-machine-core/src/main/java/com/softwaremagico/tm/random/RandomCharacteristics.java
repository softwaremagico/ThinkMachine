package com.softwaremagico.tm.random;

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

import java.util.Set;
import java.util.TreeMap;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.characteristics.CharacteristicType;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.BodyPreferences;
import com.softwaremagico.tm.random.selectors.CombatPreferences;
import com.softwaremagico.tm.random.selectors.IRandomPreferences;
import com.softwaremagico.tm.random.selectors.PsiqueLevelPreferences;
import com.softwaremagico.tm.random.selectors.SpecializationPreferences;
import com.softwaremagico.tm.random.selectors.TechnologicalPreferences;

public class RandomCharacteristics extends RandomSelector<Characteristic> {
	private final static int MAX_PROBABILITY = 100000;
	private final static int GOOD_PROBABILITY = 10;

	public RandomCharacteristics(CharacterPlayer characterPlayer, Set<IRandomPreferences> preferences)
			throws InvalidXmlElementException {
		super(characterPlayer, preferences);
	}

	public void spendCharacteristicsPoints() throws InvalidRandomElementSelectedException {
		// Set minimum values of characteristics.
		assignMinimumValuesOfCharacteristics();

		// Assign random values by weight
		while (getCharacterPlayer().getCharacteristicsTotalPoints() < FreeStyleCharacterCreation.CHARACTERISTICS_POINTS) {
			Characteristic selectedCharacteristic = selectElementByWeight();
			if (selectedCharacteristic.getValue() < FreeStyleCharacterCreation.MAX_INITIAL_SKILL_VALUE) {
				selectedCharacteristic.setValue(selectedCharacteristic.getValue() + 1);
				getCharacterPlayer().getFreeStyleCharacterCreation().getSelectedCharacteristicsValues()
						.put(selectedCharacteristic.getCharacteristicName(), selectedCharacteristic.getValue());
			}
		}
	}

	private void assignMinimumValuesOfCharacteristics() {
		// Default minimums.
		for (CharacteristicName characteristicName : CharacteristicName.values()) {
			getCharacterPlayer().getCharacteristic(characteristicName).setValue(
					getCharacterPlayer().getStartingValue(characteristicName));
		}

		for (IRandomPreferences preference : getPreferences()) {
			if (preference instanceof TechnologicalPreferences) {
				getCharacterPlayer().getCharacteristic(CharacteristicName.TECH).setValue(
						((TechnologicalPreferences) preference).minimum());
			}
		}
	}

	@Override
	protected TreeMap<Integer, Characteristic> assignElementsWeight() {
		TreeMap<Integer, Characteristic> weightedCharacteristics = new TreeMap<>();
		int count = 1;

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
		// Others characteristics cannot be assigned ranks.
		if (CharacteristicType.OTHERS.equals(characteristic.getType())) {
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

		// Specialization desired.
		SpecializationPreferences selectedSpecialization = SpecializationPreferences.getSelected(getPreferences());
		if (selectedSpecialization != null) {
			int characteristicRanks = getCharacterPlayer().getCharacteristic(characteristic.getCharacteristicName())
					.getValue();
			// No more that the maximum allowed.
			if (characteristicRanks > selectedSpecialization.maximum()) {
				return 0;
			}
			// If selected characteristic (has ranks), must have at least the
			// minimum.
			if (getCharacterPlayer().isCharacteristicTrained(characteristic)
					&& characteristicRanks < selectedSpecialization.minimum()) {
				return MAX_PROBABILITY;
			}

			// Good probability for values between the specialization.
			if (characteristicRanks > selectedSpecialization.minimum()) {
				return GOOD_PROBABILITY;
			}
		}

		// Theurgy
		if (getCharacterPlayer().getFaction() != null
				&& getCharacterPlayer().getFaction().getFactionGroup() == FactionGroup.CHURCH) {
			if (characteristic.getId().equals("faith")) {
				return MAX_PROBABILITY;
			}
		}

		// Psique
		if (characteristic.getId().equals("will")) {
			PsiqueLevelPreferences psique = PsiqueLevelPreferences.getSelected(getPreferences());
			switch (psique) {
			case FAIR:
				return GOOD_PROBABILITY;
			case HIGH:
				return MAX_PROBABILITY;
			default:
				break;
			}
		}

		// Nobility
		if (getCharacterPlayer().getFaction() != null
				&& getCharacterPlayer().getFaction().getFactionGroup() == FactionGroup.NOBILITY) {
			if (characteristic.getId().equals("presence")) {
				return GOOD_PROBABILITY;
			}
		}

		return weight;
	}

}
