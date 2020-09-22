package com.softwaremagico.tm.character.skills;

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

import java.util.HashSet;
import java.util.Set;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.creation.CostCalculator;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.IRandomPreference;

public class RandomSkillExtraPoints extends RandomSkills {

    public RandomSkillExtraPoints(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences,
                                  Set<AvailableSkill> suggestedSkills) throws InvalidXmlElementException {
        super(characterPlayer, preferences, new HashSet<AvailableSkill>(), suggestedSkills);
    }

    public int spendSkillsPoints(int remainingPoints) throws InvalidRandomElementSelectedException,
            InvalidXmlElementException {
        return spendSkillsPoints(selectElementByWeight(), remainingPoints);
    }

    public int spendSkillsPoints(AvailableSkill selectedSkill, int remainingPoints)
            throws InvalidRandomElementSelectedException, InvalidXmlElementException {
        int addedRanks = ranksToAdd(selectedSkill) - getCharacterPlayer().getSkillAssignedRanks(selectedSkill);
        if (addedRanks * CostCalculator.SKILL_EXTRA_POINTS_COST > remainingPoints) {
            addedRanks = remainingPoints / CostCalculator.SKILL_EXTRA_POINTS_COST;
        }
        if (addedRanks > 0) {
            RandomGenerationLog.info(this.getClass().getName(), "Added '{}' ranks to skill '{}'.", addedRanks, selectedSkill);
        }
        // Only if adding more ranks.
        if (getCharacterPlayer().getSkillAssignedRanks(selectedSkill) + addedRanks < getCharacterPlayer()
                .getSkillAssignedRanks(selectedSkill)) {
            return 0;
        }
        getCharacterPlayer().setSkillRank(selectedSkill,
                getCharacterPlayer().getSkillAssignedRanks(selectedSkill) + addedRanks);
        return addedRanks * CostCalculator.SKILL_EXTRA_POINTS_COST;
    }

    private int ranksToAdd(AvailableSkill availableSkill) throws InvalidXmlElementException,
            InvalidRandomElementSelectedException {
        int finalRanks = getRankValue(availableSkill);
        final int currentRanks = getCharacterPlayer().getSkillAssignedRanks(availableSkill);
        if (getCharacterPlayer().getSkillAssignedRanks(availableSkill) + finalRanks > FreeStyleCharacterCreation
                .getMaxInitialSkillsValues(getCharacterPlayer().getInfo().getAge())) {
            finalRanks = FreeStyleCharacterCreation.getMaxInitialSkillsValues(getCharacterPlayer().getInfo().getAge());
        }
        if (finalRanks > currentRanks) {
            return finalRanks - currentRanks;
        }
        return 0;
    }

    @Override
    protected int checkMaxSkillRanksValues(AvailableSkill availableSkill, int finalRanks)
            throws InvalidXmlElementException {
        // If respects age maximum.
        if (finalRanks > FreeStyleCharacterCreation.getMaxInitialSkillsValues(getCharacterPlayer().getInfo().getAge())) {
            finalRanks = FreeStyleCharacterCreation.getMaxInitialSkillsValues(getCharacterPlayer().getInfo().getAge());
        }
        return finalRanks;
    }

    @Override
    protected void assignIfMandatory(AvailableSkill skill) throws InvalidXmlElementException {
        // Nothing
    }
}
