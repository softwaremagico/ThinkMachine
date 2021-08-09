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

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.RestrictedElementException;
import com.softwaremagico.tm.character.UnofficialElementNotAllowedException;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.RandomSelector;
import com.softwaremagico.tm.random.exceptions.ImpossibleToAssignMandatoryElementException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RandomCharacteristics extends RandomSelector<Characteristic> {
    private static final int MIN_FAITH_FOR_THEURGY = 6;
    private static final int MIN_WILL_FOR_PSIQUE = 5;

    public RandomCharacteristics(CharacterPlayer characterPlayer, Set<IRandomPreference<?>> preferences,
                                 Set<Characteristic> characteristicsMinimumValues) throws InvalidXmlElementException,
            RestrictedElementException, UnofficialElementNotAllowedException {
        super(characterPlayer, null, preferences, characteristicsMinimumValues, new HashSet<>());
    }

    @Override
    public void assign() throws InvalidRandomElementSelectedException {
        final SpecializationPreferences selectedSpecialization = SpecializationPreferences
                .getSelected(getPreferences());

        IRandomPreference<?> techPreference = null;
        for (final IRandomPreference<?> preference : getPreferences()) {
            if (preference instanceof TechnologicalPreferences) {
                techPreference = preference;
            }
        }

        final DifficultLevelPreferences difficultLevel = DifficultLevelPreferences.getSelected(getPreferences());

        // Assign random values by weight
        while (getCharacterPlayer().getCharacteristicsTotalPoints() < FreeStyleCharacterCreation
                .getCharacteristicsPoints(getCharacterPlayer().getInfo().getAge())
                + difficultLevel.getCharacteristicsBonus()) {
            final Characteristic selectedCharacteristic = selectElementByWeight();
            if (selectedCharacteristic.getValue() >= selectedSpecialization.maximum()) {
                removeElementWeight(selectedCharacteristic);
                continue;
            }

            if (selectedCharacteristic.getValue() < FreeStyleCharacterCreation.getMaxInitialCharacteristicsValues(
                    selectedCharacteristic.getCharacteristicDefinition().getCharacteristicName(),
                    getCharacterPlayer().getInfo().getAge(), getCharacterPlayer().getRace())) {
                if (selectedCharacteristic.getCharacteristicDefinition()
                        .getCharacteristicName() != CharacteristicName.TECH
                        || (techPreference == null || selectedCharacteristic.getValue() < techPreference.maximum())) {
                    RandomGenerationLog.debug(this.getClass().getName(),
                            "Increased value of '" + selectedCharacteristic + "' in 1.");
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
        if (CharacteristicType.OTHERS.equals(characteristic.getCharacteristicDefinition().getType())) {
            throw new InvalidRandomElementSelectedException("Group '" + CharacteristicType.OTHERS + "' of '"
                    + characteristic + "' cannot have assigned ranks.");
        }

        final DifficultLevelPreferences preference = DifficultLevelPreferences.getSelected(getPreferences());

        int weight = 1;
        if (CharacteristicType.BODY.equals(characteristic.getCharacteristicDefinition().getType())) {
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
        if (CharacteristicType.MIND.equals(characteristic.getCharacteristicDefinition().getType())) {
            if (getPreferences().contains(CharacteristicsPreferences.MIND)) {
                weight += FAIR_PROBABILITY;
            }
        }
        if (CharacteristicType.SPIRIT.equals(characteristic.getCharacteristicDefinition().getType())) {
            if (getPreferences().contains(CharacteristicsPreferences.SPIRIT)) {
                weight += FAIR_PROBABILITY;
            }
        }

        // Race
        if (getCharacterPlayer().getRace() != null) {
            // Races with better than human initial value.
            if (getCharacterPlayer().getRace().get(characteristic.getCharacteristicDefinition().getCharacteristicName())
                    .getInitialValue() > Characteristic.DEFAULT_HUMAN_INITIAL_VALUE) {
                weight += FAIR_PROBABILITY;
            } else if (getCharacterPlayer().getRace()
                    .get(characteristic.getCharacteristicDefinition().getCharacteristicName())
                    .getInitialValue() < Characteristic.DEFAULT_HUMAN_INITIAL_VALUE) {
                weight += BAD_PROBABILITY;
            }

            // Races with better max init value.
            if (getCharacterPlayer().getRace().get(characteristic.getCharacteristicDefinition().getCharacteristicName())
                    .getMaximumInitialValue() > Characteristic.DEFAULT_INITIAL_MAX_VALUE) {
                weight += LITTLE_PROBABILITY;
            } else if (getCharacterPlayer().getRace()
                    .get(characteristic.getCharacteristicDefinition().getCharacteristicName())
                    .getMaximumInitialValue() < Characteristic.DEFAULT_INITIAL_MAX_VALUE) {
                weight += PENALIZED_PROBABILITY;
            }

            // Races with better max value.
            if (getCharacterPlayer().getRace().get(characteristic.getCharacteristicDefinition().getCharacteristicName())
                    .getMaximumValue() > Characteristic.MAX_VALUE) {
                weight += LITTLE_PROBABILITY;
            } else if (getCharacterPlayer().getRace()
                    .get(characteristic.getCharacteristicDefinition().getCharacteristicName())
                    .getMaximumValue() < Characteristic.MAX_VALUE) {
                weight += PENALIZED_PROBABILITY;
            }
        }

        // Tech
        if (characteristic.getCharacteristicDefinition().getCharacteristicName() == CharacteristicName.TECH) {
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
        if (characteristic.getCharacteristicDefinition().getCharacteristicName() == CharacteristicName.FAITH) {
            if (getCharacterPlayer().getFaction() != null
                    && (getCharacterPlayer().getFaction().getFactionGroup() == FactionGroup.CHURCH ||
                    getCharacterPlayer().getFaction().getFactionGroup() == FactionGroup.MINOR_CHURCH)) {
                return FAIR_PROBABILITY;
            }
        }

        // Psique
        if (characteristic.getCharacteristicDefinition().getCharacteristicName() == CharacteristicName.WILL) {
            final OccultismLevelPreferences psique = OccultismLevelPreferences.getSelected(getPreferences());
            switch (psique) {
                case FAIR:
                case HIGH:
                    return FAIR_PROBABILITY;
                default:
                    break;
            }

            final CyberneticPointsPreferences cyberneticPoints = CyberneticPointsPreferences
                    .getSelected(getPreferences());
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
        if (getCharacterPlayer().getFaction() != null
                && getCharacterPlayer().getFaction().getFactionGroup() == FactionGroup.NOBILITY) {
            if (characteristic.getCharacteristicDefinition().getCharacteristicName() == CharacteristicName.PRESENCE) {
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
    protected void assignIfMandatory(Characteristic characteristic)
            throws InvalidXmlElementException, ImpossibleToAssignMandatoryElementException {
        // If selected characteristic (has ranks), must have at least the
        // minimum.
        if (characteristic.getValue() < getCharacterPlayer()
                .getStartingValue(characteristic.getCharacteristicDefinition().getCharacteristicName())) {
            characteristic.setValue(getCharacterPlayer()
                    .getStartingValue(characteristic.getCharacteristicDefinition().getCharacteristicName()));
        }

        if (getCharacterPlayer().isCharacteristicTrained(characteristic)) {
            final SpecializationPreferences selectedSpecialization = SpecializationPreferences
                    .getSelected(getPreferences());
            final int characteristicRanks = getCharacterPlayer()
                    .getCharacteristicValue(characteristic.getCharacteristicDefinition().getCharacteristicName());
            if (characteristicRanks < selectedSpecialization.minimum()) {
                characteristic.setValue(selectedSpecialization.minimum());
            }
        }

        if (characteristic.getCharacteristicDefinition().getCharacteristicName() == CharacteristicName.TECH) {
            // Minimum tech level for preferences.
            final TechnologicalPreferences preference = TechnologicalPreferences.getSelected(getPreferences());
            if (preference != null) {
                if (getCharacterPlayer()
                        .getCharacteristicValue(CharacteristicName.TECH) < preference.minimum()) {
                    getCharacterPlayer().getCharacteristic(CharacteristicName.TECH)
                            .setValue(preference.minimum());
                }
            }

            // Minimum tech level for equipment.
            final int techLevel = getCharacterPlayer().getEquipmentMaxTechnologicalLevel();
            if (techLevel < characteristic.getValue()
                    && (techLevel > getCharacterPlayer().getRace().get(CharacteristicName.TECH).getInitialValue())
                    && techLevel < FreeStyleCharacterCreation.getMaxInitialCharacteristicsValues(
                    CharacteristicName.TECH, getCharacterPlayer().getInfo().getAge(),
                    getCharacterPlayer().getRace())) {
                characteristic.setValue(techLevel);
            }

            // Avoid to have more that the allowed values.
            if (getCharacterPlayer().getCharacteristic(CharacteristicName.TECH).getValue() > FreeStyleCharacterCreation
                    .getMaxInitialCharacteristicsValues(
                            getCharacterPlayer().getCharacteristic(CharacteristicName.TECH)
                                    .getCharacteristicDefinition().getCharacteristicName(),
                            getCharacterPlayer().getInfo().getAge(), getCharacterPlayer().getRace())) {
                getCharacterPlayer().getCharacteristic(CharacteristicName.TECH)
                        .setValue(FreeStyleCharacterCreation.getMaxInitialCharacteristicsValues(
                                getCharacterPlayer().getCharacteristic(CharacteristicName.TECH)
                                        .getCharacteristicDefinition().getCharacteristicName(),
                                getCharacterPlayer().getInfo().getAge(), getCharacterPlayer().getRace()));
            }
        }

        // Theurgy
        if (characteristic.getCharacteristicDefinition().getCharacteristicName() == CharacteristicName.FAITH) {
            final OccultismLevelPreferences psique = OccultismLevelPreferences.getSelected(getPreferences());
            if (psique.maximum() > 2) {
                if (getCharacterPlayer().getFaction() != null
                        && (getCharacterPlayer().getFaction().getFactionGroup() == FactionGroup.CHURCH ||
                        getCharacterPlayer().getFaction().getFactionGroup() == FactionGroup.MINOR_CHURCH)) {
                    if (characteristic.getValue() < Math.min(MIN_FAITH_FOR_THEURGY,
                            FreeStyleCharacterCreation.getMaxInitialCharacteristicsValues(
                                    characteristic.getCharacteristicDefinition().getCharacteristicName(),
                                    getCharacterPlayer().getInfo().getAge(), getCharacterPlayer().getRace()))) {
                        characteristic.setValue(Math.min(MIN_FAITH_FOR_THEURGY,
                                FreeStyleCharacterCreation.getMaxInitialCharacteristicsValues(
                                        characteristic.getCharacteristicDefinition().getCharacteristicName(),
                                        getCharacterPlayer().getInfo().getAge(), getCharacterPlayer().getRace())));
                    }
                }
            }
        }
        // Psique
        if (characteristic.getCharacteristicDefinition().getCharacteristicName() == CharacteristicName.WILL) {
            final OccultismLevelPreferences psique = OccultismLevelPreferences.getSelected(getPreferences());
            if (psique.maximum() > 2) {
                if (characteristic.getValue() < Math.min(MIN_WILL_FOR_PSIQUE,
                        FreeStyleCharacterCreation.getMaxInitialCharacteristicsValues(
                                characteristic.getCharacteristicDefinition().getCharacteristicName(),
                                getCharacterPlayer().getInfo().getAge(), getCharacterPlayer().getRace()))) {
                    characteristic.setValue(Math.min(MIN_WILL_FOR_PSIQUE,
                            FreeStyleCharacterCreation.getMaxInitialCharacteristicsValues(
                                    characteristic.getCharacteristicDefinition().getCharacteristicName(),
                                    getCharacterPlayer().getInfo().getAge(), getCharacterPlayer().getRace())));
                }
            }
        }
    }

    @Override
    protected void assignMandatoryValues(Set<Characteristic> mandatoryValues) throws InvalidXmlElementException {
        for (final Characteristic characteristic : mandatoryValues) {
            if (getCharacterPlayer().getCharacteristicValue(
                    characteristic.getCharacteristicDefinition().getCharacteristicName()) < characteristic.getValue()) {
                getCharacterPlayer()
                        .getCharacteristic(characteristic.getCharacteristicDefinition().getCharacteristicName())
                        .setValue(characteristic.getValue());
            }
        }
    }
}
