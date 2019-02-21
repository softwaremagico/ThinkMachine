package com.softwaremagico.tm.character.characteristics;

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
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.random.RandomSelector;
import com.softwaremagico.tm.random.exceptions.ImpossibleToAssignMandatoryElementException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.CharacteristicsPreferences;
import com.softwaremagico.tm.random.selectors.CombatPreferences;
import com.softwaremagico.tm.random.selectors.CyberneticPointsPreferences;
import com.softwaremagico.tm.random.selectors.IRandomPreference;
import com.softwaremagico.tm.random.selectors.PsiqueLevelPreferences;
import com.softwaremagico.tm.random.selectors.SpecializationPreferences;
import com.softwaremagico.tm.random.selectors.TechnologicalPreferences;

public class RandomCharacteristics extends RandomSelector<Characteristic> {
	private final static int MIN_FAITH_FOR_THEURGY = 6;
	private final static int MIN_WILL_FOR_PSIQUE = 5;

	public RandomCharacteristics(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences) throws InvalidXmlElementException {
		super(characterPlayer, preferences);
	}

	@Override
	public void assign() throws InvalidRandomElementSelectedException {
		// Set minimum values of characteristics.
		assignMinimumValuesOfCharacteristics();
		SpecializationPreferences selectedSpecialization = SpecializationPreferences.getSelected(getPreferences());

		IRandomPreference techPreference = null;
		for (IRandomPreference preference : getPreferences()) {
			if (preference instanceof TechnologicalPreferences) {
				techPreference = preference;
			}
		}

		// Assign random values by weight
		while (getCharacterPlayer().getCharacteristicsTotalPoints() < FreeStyleCharacterCreation.getCharacteristicsPoints(getCharacterPlayer().getInfo()
				.getAge())) {
			Characteristic selectedCharacteristic = selectElementByWeight();
			if (selectedCharacteristic.getValue() >= selectedSpecialization.maximum()) {
				removeElementWeight(selectedCharacteristic);
				continue;
			}

			if (selectedCharacteristic.getValue() < FreeStyleCharacterCreation.getMaxInitialCharacteristicsValues(
					selectedCharacteristic.getCharacteristicName(), getCharacterPlayer().getInfo().getAge(), getCharacterPlayer().getRace())) {
				if (selectedCharacteristic.getCharacteristicName() != CharacteristicName.TECH
						|| (techPreference == null || selectedCharacteristic.getValue() < techPreference.maximum())) {
					selectedCharacteristic.setValue(selectedCharacteristic.getValue() + 1);
				}
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

			// Good probability for values between the specialization.
			if (characteristicRanks > selectedSpecialization.minimum()) {
				return FAIR_PROBABILITY;
			}
		}
		// Theurgy
		if (characteristic.getCharacteristicName() == CharacteristicName.FAITH) {
			if (getCharacterPlayer().getFaction() != null && getCharacterPlayer().getFaction().getFactionGroup() == FactionGroup.CHURCH) {
				return FAIR_PROBABILITY;
			}
		}

		// Psique
		if (characteristic.getCharacteristicName() == CharacteristicName.WILL) {
			PsiqueLevelPreferences psique = PsiqueLevelPreferences.getSelected(getPreferences());
			switch (psique) {
			case FAIR:
			case HIGH:
				return FAIR_PROBABILITY;
			default:
				break;
			}

			CyberneticPointsPreferences cyberneticPoints = CyberneticPointsPreferences.getSelected(getPreferences());
			switch (cyberneticPoints) {
			case HIGH:
				return LITTLE_PROBABILITY;
			case CYBORG:
				return FAIR_PROBABILITY;
			case SOUL_LESS:
				return GOOD_PROBABILITY;
			default:
				break;
			}
		}

		// Nobility
		if (getCharacterPlayer().getFaction() != null && getCharacterPlayer().getFaction().getFactionGroup() == FactionGroup.NOBILITY) {
			if (characteristic.getCharacteristicName() == CharacteristicName.PRESENCE) {
				return FAIR_PROBABILITY;
			}
		}

		return weight;
	}

	@Override
	protected void assignIfMandatory(Characteristic characteristic) throws InvalidXmlElementException, ImpossibleToAssignMandatoryElementException {
		// If selected characteristic (has ranks), must have at least the
		// minimum.
		if (getCharacterPlayer().isCharacteristicTrained(characteristic)) {
			SpecializationPreferences selectedSpecialization = SpecializationPreferences.getSelected(getPreferences());
			int characteristicRanks = getCharacterPlayer().getCharacteristic(characteristic.getCharacteristicName()).getValue();
			if (characteristicRanks < selectedSpecialization.minimum()) {
				characteristic.setValue(selectedSpecialization.minimum());
			}
		}
		// Minimum tech level for equipment.
		if (characteristic.getCharacteristicName() == CharacteristicName.TECH) {
			int techLevel = getCharacterPlayer().getEquipmentMaxTechnologicalLevel();
			if (techLevel < characteristic.getValue() && (techLevel > getCharacterPlayer().getRace().get(CharacteristicName.TECH).getInitialValue())
					&& techLevel < getCharacterPlayer().getRace().get(CharacteristicName.TECH).getMaximumValue()) {
				characteristic.setValue(techLevel);
			}
		}
		// Theurgy
		if (characteristic.getCharacteristicName() == CharacteristicName.FAITH) {
			if (getCharacterPlayer().getFaction() != null && getCharacterPlayer().getFaction().getFactionGroup() == FactionGroup.CHURCH) {
				if (characteristic.getValue() < MIN_FAITH_FOR_THEURGY) {
					characteristic.setValue(MIN_FAITH_FOR_THEURGY);
				}
			}
		}
		// Psique
		if (characteristic.getCharacteristicName() == CharacteristicName.WILL) {
			PsiqueLevelPreferences psique = PsiqueLevelPreferences.getSelected(getPreferences());
			if ((psique == PsiqueLevelPreferences.HIGH) || (psique == PsiqueLevelPreferences.FAIR)) {
				if (characteristic.getValue() < MIN_WILL_FOR_PSIQUE) {
					characteristic.setValue(MIN_WILL_FOR_PSIQUE);
				}
			}
		}
	}

	@Override
	protected void assignMandatoryValues(Set<Characteristic> mandatoryValues) throws InvalidXmlElementException {
		return;
	}
}
