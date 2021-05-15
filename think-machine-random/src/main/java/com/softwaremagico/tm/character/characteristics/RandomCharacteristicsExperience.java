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
import com.softwaremagico.tm.character.xp.ElementCannotBeUpgradeWithExperienceException;
import com.softwaremagico.tm.character.xp.Experience;
import com.softwaremagico.tm.character.xp.NotEnoughExperienceException;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.exceptions.ImpossibleToAssignMandatoryElementException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.IRandomPreference;
import com.softwaremagico.tm.random.selectors.SpecializationPreferences;

import java.util.HashSet;
import java.util.Set;

public class RandomCharacteristicsExperience extends RandomCharacteristics {

    public RandomCharacteristicsExperience(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences) throws InvalidXmlElementException,
            RestrictedElementException {
        super(characterPlayer, preferences, new HashSet<>());
    }

    @Override
    public void assign() throws InvalidRandomElementSelectedException {
        // We only want XP on characteristics on specialized characters.
        final SpecializationPreferences specializationPreferences = SpecializationPreferences.getSelected(getPreferences());
        for (int i = 0; i < specializationPreferences.ordinal(); i++) {
            // And only a few of them.
            if (specializationPreferences.randomGaussian() < 5) {
                continue;
            }
            final Characteristic characteristic = selectElementByWeight();
            final int newValue = getCharacterPlayer().getRawValue(characteristic.getCharacteristicDefinition().getCharacteristicName())
                    + getCharacterPlayer().getExperienceIncrease(characteristic).size() + 1;
            try {
                if (getCharacterPlayer().getExperienceExpended() + Experience.getExperienceCostFor(characteristic, newValue, getCharacterPlayer()) <= getCharacterPlayer()
                        .getExperienceEarned()) {
                    RandomGenerationLog
                            .debug(this.getClass().getName(),
                                    "Spent '" + Experience.getExperienceCostFor(characteristic, newValue, getCharacterPlayer()) +
                                            "' experience points on '" + characteristic + "'. Remaining experience '" + (getCharacterPlayer().getExperienceEarned()
                                            - getCharacterPlayer().getExperienceExpended() - Experience.getExperienceCostFor(characteristic, newValue, getCharacterPlayer()))
                                            + "'.");
                    getCharacterPlayer().setExperienceIncreasedRanks(characteristic, 1);
                } else {
                    // Remove characteristic from options to avoid adding more
                    // ranks.
                    removeElementWeight(characteristic);
                }
            } catch (ElementCannotBeUpgradeWithExperienceException e) {
                throw new InvalidRandomElementSelectedException(e.getMessage(), e);
            } catch (NotEnoughExperienceException e) {
                // Remove characteristic from options to avoid adding more
                // ranks.
                removeElementWeight(characteristic);
            }
        }
    }

    @Override
    protected int getWeight(Characteristic characteristic) throws InvalidRandomElementSelectedException {
        try {
            if (Experience.getExperienceCostFor(characteristic,
                    getCharacterPlayer().getRawValue(characteristic.getCharacteristicDefinition().getCharacteristicName())
                            + getCharacterPlayer().getExperienceIncrease(characteristic).size(), getCharacterPlayer()) >
                    getCharacterPlayer().getExperienceEarned()) {
                throw new InvalidRandomElementSelectedException("Not enought experience.");
            }
        } catch (ElementCannotBeUpgradeWithExperienceException e) {
            throw new InvalidRandomElementSelectedException("Invalid element to be updated with experience.");
        }
        final SpecializationPreferences specializationPreferences = SpecializationPreferences.getSelected(getPreferences());
        if (specializationPreferences.ordinal() > 2) {
            return getCharacterPlayer().getValue(characteristic.getCharacteristicDefinition().getCharacteristicName()) * super.getWeight(characteristic);
        }
        return super.getWeight(characteristic);
    }

    @Override
    protected void assignIfMandatory(Characteristic characteristic) throws InvalidXmlElementException, ImpossibleToAssignMandatoryElementException {
        // Nothing
    }
}
