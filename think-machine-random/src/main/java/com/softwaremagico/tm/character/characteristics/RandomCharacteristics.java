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
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.RandomSelector;
import com.softwaremagico.tm.random.exceptions.ImpossibleToAssignMandatoryElementException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.CharacteristicsPreferences;
import com.softwaremagico.tm.random.selectors.CombatPreferences;
import com.softwaremagico.tm.random.selectors.CyberneticPointsPreferences;
import com.softwaremagico.tm.random.selectors.DifficultLevelPreferences;
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
		SpecializationPreferences selectedSpecialization = SpecializationPreferences.getSelected(getPreferences());

		IRandomPreference techPreference = null;
		for (IRandomPreference preference : getPreferences()) {
			if (preference instanceof TechnologicalPreferences) {
				techPreference = preference;
			}
		}

		DifficultLevelPreferences difficultLevel = DifficultLevelPreferences.getSelected(getPreferences());

		// Assign random values by weight
		while (getCharacterPlayer().getCharacteristicsTotalPoints() < FreeStyleCharacterCreation.getCharacteristicsPoints(getCharacterPlayer().getInfo()
				.getAge()) + difficultLevel.getCharacteristicsBonus()) {
			Characteristic selectedCharacteristic = selectElementByWeight();
			if (selectedCharacteristic.getValue() >= selectedSpecialization.maximum()) {
				removeElementWeight(selectedCharacteristic);
				continue;
			}

			if (selectedCharacteristic.getValue() < FreeStyleCharacterCreation.getMaxInitialCharacteristicsValues(
					selectedCharacteristic.getCharacteristicName(), getCharacterPlayer().getInfo().getAge(), getCharacterPlayer().getRace())) {
				if (selectedCharacteristic.getCharacteristicName() != CharacteristicName.TECH
						|| (techPreference == null || selectedCharacteristic.getValue() < techPreference.maximum())) {
					RandomGenerationLog.debug(this.getClass().getName(), "Increased value of '" + selectedCharacteristic + "' in 1.");
					selectedCharacteristic.setValue(selectedCharacteristic.getValue() + 1);
				}
			}
		}
	}

	@Override
	protected Collection<Characteristic> getAllElements() throws InvalidXmlElementException {
		return getCharacterPlayer().getCharacteristics();
	}

	@Override
	protected int getWeight(Characteristic characteristic) throws InvalidRandomElementSelectedException {
		if (characteristic == null) {
			throw new InvalidRandomElementSelectedException("Null characteristic not allowed.");
		}
		// Others characteristics cannot be assigned ranks.
		if (CharacteristicType.OTHERS.equals(characteristic.getType())) {
			throw new InvalidRandomElementSelectedException("Group '" + CharacteristicType.OTHERS + "' of '" + characteristic + "' cannot have assigned ranks.");
		}

		DifficultLevelPreferences preference = DifficultLevelPreferences.getSelected(getPreferences());

		int weight = 1;
		if (CharacteristicType.BODY.equals(characteristic.getType())) {
			if (getPreferences().contains(CharacteristicsPreferences.BODY)) {
				weight += 3;
			}
			if (getPreferences().contains(CombatPreferences.BELLIGERENT)) {
				weight += 2;
			}

			// More tought characters.
			switch (preference) {
			case HARD:
				weight += 1;
				break;
			case VERY_HARD:
				weight += 2;
				break;
			default:
				break;
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

		// Tech
		if (characteristic.getCharacteristicName() == CharacteristicName.TECH) {
			// More technological characters.
			switch (preference) {
			case HARD:
				weight += FAIR_PROBABILITY;
				break;
			case VERY_HARD:
				weight += GOOD_PROBABILITY;
				break;
			default:
				break;
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
		characteristic.setValue(getCharacterPlayer().getStartingValue(characteristic.getCharacteristicName()));

		if (getCharacterPlayer().isCharacteristicTrained(characteristic)) {
			SpecializationPreferences selectedSpecialization = SpecializationPreferences.getSelected(getPreferences());
			int characteristicRanks = getCharacterPlayer().getCharacteristic(characteristic.getCharacteristicName()).getValue();
			if (characteristicRanks < selectedSpecialization.minimum()) {
				characteristic.setValue(selectedSpecialization.minimum());
			}
		}

		if (characteristic.getCharacteristicName() == CharacteristicName.TECH) {
			// Minimum tech level for preferences.
			TechnologicalPreferences preference = TechnologicalPreferences.getSelected(getPreferences());
			if (preference != null) {
				getCharacterPlayer().getCharacteristic(CharacteristicName.TECH).setValue(((TechnologicalPreferences) preference).minimum());
			}

			// Minimum tech level for equipment.
			int techLevel = getCharacterPlayer().getEquipmentMaxTechnologicalLevel();
			if (techLevel < characteristic.getValue() && (techLevel > getCharacterPlayer().getRace().get(CharacteristicName.TECH).getInitialValue())
					&& techLevel < getCharacterPlayer().getRace().get(CharacteristicName.TECH).getMaximumValue()) {
				characteristic.setValue(techLevel);
			}

			// Avoid to have more that the allowed values.
			if (getCharacterPlayer().getCharacteristic(CharacteristicName.TECH).getValue() > FreeStyleCharacterCreation.getMaxInitialCharacteristicsValues(
					getCharacterPlayer().getCharacteristic(CharacteristicName.TECH).getCharacteristicName(), getCharacterPlayer().getInfo().getAge(),
					getCharacterPlayer().getRace())) {
				getCharacterPlayer().getCharacteristic(CharacteristicName.TECH).setValue(
						FreeStyleCharacterCreation.getMaxInitialCharacteristicsValues(getCharacterPlayer().getCharacteristic(CharacteristicName.TECH)
								.getCharacteristicName(), getCharacterPlayer().getInfo().getAge(), getCharacterPlayer().getRace()));
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

	}
}
