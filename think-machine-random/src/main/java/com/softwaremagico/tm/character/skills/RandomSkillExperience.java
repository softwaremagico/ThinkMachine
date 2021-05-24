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

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.RestrictedElementException;
import com.softwaremagico.tm.character.xp.ElementCannotBeUpgradeWithExperienceException;
import com.softwaremagico.tm.character.xp.Experience;
import com.softwaremagico.tm.character.xp.NotEnoughExperienceException;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.IRandomPreference;
import com.softwaremagico.tm.random.selectors.SpecializationPreferences;

import java.util.HashSet;
import java.util.Set;

public class RandomSkillExperience extends RandomSkills {

    public RandomSkillExperience(CharacterPlayer characterPlayer, Set<IRandomPreference<?>> preferences) throws InvalidXmlElementException,
            RestrictedElementException {
        super(characterPlayer, preferences, new HashSet<>(), new HashSet<>());
    }

    @Override
    protected void assignIfMandatory(AvailableSkill skill) throws InvalidXmlElementException {
        // Nothing
    }

    @Override
    protected void assignMandatoryValues(Set<AvailableSkill> mandatoryValues) throws InvalidXmlElementException {
        // Nothing
    }

    @Override
    public void assign() throws InvalidXmlElementException, InvalidRandomElementSelectedException {
        // Meanwhile are XP to expend.
        while (getCharacterPlayer().getExperienceExpended() < (getCharacterPlayer().getExperienceEarned() - 1)
                && getWeightedElements().size() > 0) {
            // Select a skill randomly.
            final AvailableSkill selectedSkill = selectElementByWeight();
            final int newValue = getCharacterPlayer().getSkillAssignedRanks(selectedSkill)
                    + getCharacterPlayer().getExperienceIncrease(selectedSkill).size() + 1;
            try {
                if (getCharacterPlayer().getExperienceExpended() + Experience.getExperienceCostFor(selectedSkill, newValue, getCharacterPlayer())
                        <= getCharacterPlayer().getExperienceEarned()) {
                    RandomGenerationLog
                            .debug(this.getClass().getName(),
                                    "Spent '"
                                            + Experience.getExperienceCostFor(selectedSkill, newValue, getCharacterPlayer())
                                            + "' experience points on '"
                                            + selectedSkill
                                            + "'. Remaining experience '"
                                            + (getCharacterPlayer().getExperienceEarned() - getCharacterPlayer().getExperienceExpended() - Experience
                                            .getExperienceCostFor(selectedSkill, newValue, getCharacterPlayer())) + "'.");
                    getCharacterPlayer().setExperienceIncreasedRanks(selectedSkill, 1);
                } else {
                    // Remove skill from options to avoid adding more ranks.
                    removeElementWeight(selectedSkill);
                }
            } catch (ElementCannotBeUpgradeWithExperienceException e) {
                throw new InvalidRandomElementSelectedException(e.getMessage(), e);
            } catch (NotEnoughExperienceException e) {
                // Remove skill from options to avoid adding more ranks.
                removeElementWeight(selectedSkill);
            }
        }
    }

    @Override
    protected int getWeight(AvailableSkill skill) throws InvalidRandomElementSelectedException {
        final SpecializationPreferences specializationPreferences = SpecializationPreferences.getSelected(getPreferences());
        if (specializationPreferences.ordinal() > 2) {
            final int totalRanks = getCharacterPlayer().getSkillTotalRanks(skill);
            if (totalRanks > 0) {
                return totalRanks * super.getWeight(skill);
            }
        }
        return super.getWeight(skill);
    }
}
