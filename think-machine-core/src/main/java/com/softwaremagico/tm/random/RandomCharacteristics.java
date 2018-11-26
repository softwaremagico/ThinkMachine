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

import java.util.Collection;
import java.util.Set;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.characteristics.CharacteristicType;
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.CharacteristicsPreferences;
import com.softwaremagico.tm.random.selectors.CombatPreferences;
import com.softwaremagico.tm.random.selectors.IRandomPreference;
import com.softwaremagico.tm.random.selectors.PsiqueLevelPreferences;
import com.softwaremagico.tm.random.selectors.SpecializationPreferences;
import com.softwaremagico.tm.random.selectors.TechnologicalPreferences;

public class RandomCharacteristics extends RandomSelector<Characteristic> {

	public RandomCharacteristics(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences) throws InvalidXmlElementException {
		super(characterPlayer, preferences);
	}

	public void spendCharacteristicsPoints() throws InvalidRandomElementSelectedException {
		// Set minimum values of characteristics.
		assignMinimumValuesOfCharacteristics();

		// Assign random values by weight
		while (getCharacterPlayer().getCharacteristicsTotalPoints() < getCharacterPlayer().getFreeStyleCharacterCreation().getCharacteristicsPoints()) {
			Characteristic selectedCharacteristic = selectElementByWeight();
			if (selectedCharacteristic.getValue() < getCharacterPlayer().getFreeStyleCharacterCreation().getMaxInitialSkillsValues()) {
				selectedCharacteristic.setValue(selectedCharacteristic.getValue() + 1);
				getCharacterPlayer().getRandomDefinition().getSelectedCharacteristicsValues()
						.put(selectedCharacteristic.getCharacteristicName(), selectedCharacteristic.getValue());
			}
		}
	}

	private void assignMinimumValuesOfCharacteristics() {
		// Default minimums.
		for (CharacteristicName characteristicName : CharacteristicName.values()) {
			getCharacterPlayer().getCharacteristic(characteristicName).setValue(getCharacterPlayer().getStartingValue(characteristicName));
		}

		for (IRandomPreference preference : getPreferences()) {
			if (preference instanceof TechnologicalPreferences) {
				getCharacterPlayer().getCharacteristic(CharacteristicName.TECH).setValue(((TechnologicalPreferences) preference).minimum());
			}
		}
	}

	@Override
	protected Collection<Characteristic> getAllElements() throws InvalidXmlElementException {
		return getCharacterPlayer().getCharacteristics();
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
			if (getPreferences().contains(CharacteristicsPreferences.BODY)) {
				weight += 3;
			}
			if (getPreferences().contains(CombatPreferences.BELLIGERENT)) {
				weight += 2;
			}
		}
		if (CharacteristicType.MIND.equals(characteristic.getType())) {
			if (getPreferences().contains(CharacteristicsPreferences.MIND)) {
				weight += 3;
			}
		}
		if (CharacteristicType.SPIRIT.equals(characteristic.getType())) {
			if (getPreferences().contains(CharacteristicsPreferences.SPIRIT)) {
				weight += 3;
			}
		}

		// Specialization desired.
		SpecializationPreferences selectedSpecialization = SpecializationPreferences.getSelected(getPreferences());
		if (selectedSpecialization != null) {
			int characteristicRanks = getCharacterPlayer().getCharacteristic(characteristic.getCharacteristicName()).getValue();
			// No more that the maximum allowed.
			if (characteristicRanks > selectedSpecialization.maximum()) {
				return 0;
			}
			// If selected characteristic (has ranks), must have at least the
			// minimum.
			if (getCharacterPlayer().isCharacteristicTrained(characteristic) && characteristicRanks < selectedSpecialization.minimum()) {
				return MAX_PROBABILITY;
			}

			// Good probability for values between the specialization.
			if (characteristicRanks > selectedSpecialization.minimum()) {
				return FAIR_PROBABILITY;
			}
		}

		// Theurgy
		if (getCharacterPlayer().getFaction() != null && getCharacterPlayer().getFaction().getFactionGroup() == FactionGroup.CHURCH) {
			if (characteristic.getId().equals("faith")) {
				return MAX_PROBABILITY;
			}
		}

		// Psique
		if (characteristic.getId().equals("will")) {
			PsiqueLevelPreferences psique = PsiqueLevelPreferences.getSelected(getPreferences());
			switch (psique) {
			case FAIR:
				return FAIR_PROBABILITY;
			case HIGH:
				return MAX_PROBABILITY;
			default:
				break;
			}
		}

		// Nobility
		if (getCharacterPlayer().getFaction() != null && getCharacterPlayer().getFaction().getFactionGroup() == FactionGroup.NOBILITY) {
			if (characteristic.getId().equals("presence")) {
				return FAIR_PROBABILITY;
			}
		}

		return weight;
	}

}
