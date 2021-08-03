package com.softwaremagico.tm.character.characteristics;

/*-
 * #%L
 * Think Machine (Random Generator)
 * %%
 * Copyright (C) 2017 - 2019 Softwaremagico
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
import com.softwaremagico.tm.character.creation.CostCalculator;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.exceptions.ImpossibleToAssignMandatoryElementException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.IRandomPreference;
import com.softwaremagico.tm.random.selectors.SpecializationPreferences;
import com.softwaremagico.tm.random.selectors.TechnologicalPreferences;

import java.util.HashSet;
import java.util.Set;

public class RandomCharacteristicsExtraPoints extends RandomCharacteristics {

    public RandomCharacteristicsExtraPoints(CharacterPlayer characterPlayer, Set<IRandomPreference<?>> preferences) throws InvalidXmlElementException,
            RestrictedElementException {
        super(characterPlayer, preferences, new HashSet<>());
    }

    public int spendCharacteristicsPoints(int remainingPoints) throws InvalidRandomElementSelectedException {
        final SpecializationPreferences specialization = SpecializationPreferences.getSelected(getPreferences());

        IRandomPreference<?> techPreference = null;
        for (final IRandomPreference<?> preference : getPreferences()) {
            if (preference instanceof TechnologicalPreferences) {
                techPreference = preference;
            }
        }

        if (remainingPoints >= CostCalculator.CHARACTERISTIC_EXTRA_POINTS_COST) {
            final Characteristic selectedCharacteristic = selectElementByWeight();
            // If specialization allows it.
            if (specialization.randomGaussian() > selectedCharacteristic.getValue() && selectedCharacteristic.getValue() < specialization.maximum()) {
                if (selectedCharacteristic.getValue() < FreeStyleCharacterCreation.getMaxInitialCharacteristicsValues(
                        selectedCharacteristic.getCharacteristicDefinition().getCharacteristicName(), getCharacterPlayer().getInfo().getAge(),
                        getCharacterPlayer().getRace())) {
                    if (selectedCharacteristic.getCharacteristicDefinition().getCharacteristicName() != CharacteristicName.TECH
                            || (techPreference == null || selectedCharacteristic.getValue() < techPreference.maximum())) {
                        selectedCharacteristic.setValue(selectedCharacteristic.getValue() + 1);
                        RandomGenerationLog.debug(this.getClass().getName(), "Increased value of '{}' in 1.", selectedCharacteristic);
                        return CostCalculator.CHARACTERISTIC_EXTRA_POINTS_COST;
                    }
                }
            }
        }
        return 0;
    }

    @Override
    protected void assignIfMandatory(Characteristic characteristic) throws InvalidXmlElementException, ImpossibleToAssignMandatoryElementException {
        // Nothing
    }
}
