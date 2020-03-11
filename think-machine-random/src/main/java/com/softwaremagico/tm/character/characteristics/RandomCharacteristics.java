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
import java.util.HashSet;
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
	private static final int MIN_FAITH_FOR_THEURGY = 6;
	private static final int MIN_WILL_FOR_PSIQUE = 5;

	public RandomCharacteristics(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences, Set<Characteristic> characteristicsMinimumValues)
			throws InvalidXmlElementException {
		super(characterPlayer, null, preferences, characteristicsMinimumValues, new HashSet<Characteristic>());
	}

	@Override
	public void assign() throws InvalidRandomElementSelectedException {
		final SpecializationPreferences selectedSpecialization = SpecializationPreferences.getSelected(getPreferences());

		IRandomPreference techPreference = null;
		for (final IRandomPreference preference : getPreferences()) {
			if (preference instanceof TechnologicalPreferences) {
				techPreference = preference;
			}
		}

		final DifficultLevelPreferences difficultLevel = DifficultLevelPreferences.getSelected(getPreferences());

		// Assign random values by weight
		while (getCharacterPlayer().getCharacteristicsTotalPoints() < FreeStyleCharacterCreation
				.getCharacteristicsPoints(getCharacterPlayer().getInfo().getAge()) + difficultLevel.getCharacteristicsBonus()) {
			final Characteristic selectedCharacteristic = selectElementByWeight();
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
			throw new InvalidRandomElementSelectedException(
					"Group '" + CharacteristicType.OTHERS + "' of '" + characteristic + "' cannot have assigned ranks.");
		}

		final DifficultLevelPreferences preference = DifficultLevelPreferences.getSelected(getPreferences());

		int weight = 1;
		if (CharacteristicType.BODY.equals(characteristic.getType())) {
			if (getPreferences().contains(CharacteristicsPreferences.BODY)) {
				weight += FAIR_PROBABILITY;
			}
			if (getPreferences().contains(CombatPreferences.BELLIGERENT)) {
				weight += LITTLE_PROBABILITY;
			}

			// More thought characters.
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
		if (CharacteristicType.MIND.equals(characteristic.getType())) {
			if (getPreferences().contains(CharacteristicsPreferences.MIND)) {
				weight += FAIR_PROBABILITY;
			}
		}
		if (CharacteristicType.SPIRIT.equals(characteristic.getType())) {
			if (getPreferences().contains(CharacteristicsPreferences.SPIRIT)) {
				weight += FAIR_PROBABILITY;
			}
		}

		// Race
		if (getCharacterPlayer().getRace() != null) {
			// Races with better initial value.
			if (getCharacterPlayer().getRace().get(characteristic.getCharacteristicName()).getInitialValue() > Characteristic.DEFAULT_INITIAL_VALUE) {
				weight += FAIR_PROBABILITY;
			} else if (getCharacterPlayer().getRace().get(characteristic.getCharacteristicName()).getInitialValue() < Characteristic.DEFAULT_INITIAL_VALUE) {
				weight += BAD_PROBABILITY;
			}

			// Races with better max init value.
			if (getCharacterPlayer().getRace().get(characteristic.getCharacteristicName())
					.getMaximumInitialValue() > Characteristic.DEFAULT_INITIAL_MAX_VALUE) {
				weight += LITTLE_PROBABILITY;
			} else if (getCharacterPlayer().getRace().get(characteristic.getCharacteristicName())
					.getMaximumInitialValue() < Characteristic.DEFAULT_INITIAL_MAX_VALUE) {
				weight += PENALIZED_PROBABILITY;
			}

			// Races with better max value.
			if (getCharacterPlayer().getRace().get(characteristic.getCharacteristicName()).getMaximumValue() > Characteristic.MAX_VALUE) {
				weight += LITTLE_PROBABILITY;
			} else if (getCharacterPlayer().getRace().get(characteristic.getCharacteristicName()).getMaximumValue() < Characteristic.MAX_VALUE) {
				weight += PENALIZED_PROBABILITY;
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
			final PsiqueLevelPreferences psique = PsiqueLevelPreferences.getSelected(getPreferences());
			switch (psique) {
			case FAIR:
			case HIGH:
				return FAIR_PROBABILITY;
			default:
				break;
			}

			final CyberneticPointsPreferences cyberneticPoints = CyberneticPointsPreferences.getSelected(getPreferences());
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

		// Always some probability.
		if (weight < 1) {
			weight = 1;
		}
		return weight;
	}

	@Override
	protected void assignIfMandatory(Characteristic characteristic) throws InvalidXmlElementException, ImpossibleToAssignMandatoryElementException {
		// If selected characteristic (has ranks), must have at least the
		// minimum.
		if (characteristic.getValue() < getCharacterPlayer().getStartingValue(characteristic.getCharacteristicName())) {
			characteristic.setValue(getCharacterPlayer().getStartingValue(characteristic.getCharacteristicName()));
		}

		if (getCharacterPlayer().isCharacteristicTrained(characteristic)) {
			final SpecializationPreferences selectedSpecialization = SpecializationPreferences.getSelected(getPreferences());
			final int characteristicRanks = getCharacterPlayer().getCharacteristic(characteristic.getCharacteristicName()).getValue();
			if (characteristicRanks < selectedSpecialization.minimum()) {
				characteristic.setValue(selectedSpecialization.minimum());
			}
		}

		if (characteristic.getCharacteristicName() == CharacteristicName.TECH) {
			// Minimum tech level for preferences.
			final TechnologicalPreferences preference = TechnologicalPreferences.getSelected(getPreferences());
			if (preference != null) {
				getCharacterPlayer().getCharacteristic(CharacteristicName.TECH).setValue(((TechnologicalPreferences) preference).minimum());
			}

			// Minimum tech level for equipment.
			final int techLevel = getCharacterPlayer().getEquipmentMaxTechnologicalLevel();
			if (techLevel < characteristic.getValue() && (techLevel > getCharacterPlayer().getRace().get(CharacteristicName.TECH).getInitialValue())
					&& techLevel < getCharacterPlayer().getRace().get(CharacteristicName.TECH).getMaximumValue()) {
				characteristic.setValue(techLevel);
			}

			// Avoid to have more that the allowed values.
			if (getCharacterPlayer().getCharacteristic(CharacteristicName.TECH).getValue() > FreeStyleCharacterCreation.getMaxInitialCharacteristicsValues(
					getCharacterPlayer().getCharacteristic(CharacteristicName.TECH).getCharacteristicName(), getCharacterPlayer().getInfo().getAge(),
					getCharacterPlayer().getRace())) {
				getCharacterPlayer().getCharacteristic(CharacteristicName.TECH)
						.setValue(FreeStyleCharacterCreation.getMaxInitialCharacteristicsValues(
								getCharacterPlayer().getCharacteristic(CharacteristicName.TECH).getCharacteristicName(),
								getCharacterPlayer().getInfo().getAge(), getCharacterPlayer().getRace()));
			}
		}

		// Theurgy
		if (characteristic.getCharacteristicName() == CharacteristicName.FAITH) {
			final PsiqueLevelPreferences psique = PsiqueLevelPreferences.getSelected(getPreferences());
			if (psique.maximum() > 2) {
				if (getCharacterPlayer().getFaction() != null && getCharacterPlayer().getFaction().getFactionGroup() == FactionGroup.CHURCH) {
					if (characteristic.getValue() < Math.min(MIN_FAITH_FOR_THEURGY, FreeStyleCharacterCreation.getMaxInitialCharacteristicsValues(
							characteristic.getCharacteristicName(), getCharacterPlayer().getInfo().getAge(), getCharacterPlayer().getRace()))) {
						characteristic.setValue(Math.min(MIN_FAITH_FOR_THEURGY, FreeStyleCharacterCreation.getMaxInitialCharacteristicsValues(
								characteristic.getCharacteristicName(), getCharacterPlayer().getInfo().getAge(), getCharacterPlayer().getRace())));
					}
				}
			}
		}
		// Psique
		if (characteristic.getCharacteristicName() == CharacteristicName.WILL) {
			final PsiqueLevelPreferences psique = PsiqueLevelPreferences.getSelected(getPreferences());
			if (psique.maximum() > 2) {
				if (characteristic.getValue() < Math.min(MIN_WILL_FOR_PSIQUE, FreeStyleCharacterCreation.getMaxInitialCharacteristicsValues(
						characteristic.getCharacteristicName(), getCharacterPlayer().getInfo().getAge(), getCharacterPlayer().getRace()))) {
					characteristic.setValue(Math.min(MIN_WILL_FOR_PSIQUE, FreeStyleCharacterCreation.getMaxInitialCharacteristicsValues(
							characteristic.getCharacteristicName(), getCharacterPlayer().getInfo().getAge(), getCharacterPlayer().getRace())));
				}
			}
		}
	}

	@Override
	protected void assignMandatoryValues(Set<Characteristic> mandatoryValues) throws InvalidXmlElementException {
		for (final Characteristic characteristic : mandatoryValues) {
			if (getCharacterPlayer().getCharacteristic(characteristic.getCharacteristicName()).getValue() < characteristic.getValue()) {
				getCharacterPlayer().getCharacteristic(characteristic.getCharacteristicName()).setValue(characteristic.getValue());
			}
		}
	}
}
