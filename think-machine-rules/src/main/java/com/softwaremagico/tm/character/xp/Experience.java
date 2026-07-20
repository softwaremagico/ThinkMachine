package com.softwaremagico.tm.character.xp;

/*-
 * #%L
 * Think Machine (Rules)
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

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.occultism.*;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.SkillGroup;

import java.util.*;

public class Experience {
    // Element id, set of increases.
    private final Map<String, Set<ExperienceIncrease>> ranksIncreased;
    private int totalExperience = 0;

    public Experience() {
        ranksIncreased = new HashMap<>();
    }

    public int getTotalExperience() {
        return totalExperience;
    }

    public Map<String, Set<ExperienceIncrease>> getRanksIncreased() {
        return Collections.unmodifiableMap(ranksIncreased);
    }

    public Set<ExperienceIncrease> getExperienceIncreased(Element<?> element) {
        if (element == null) {
            return new HashSet<>();
        }
        return getExperienceIncreased(element.getId());
    }

    public Set<ExperienceIncrease> getExperienceIncreased(String elementId) {
        if (getRanksIncreased().get(elementId) == null) {
            return new HashSet<>();
        }
        return getRanksIncreased().get(elementId);
    }

    public ExperienceIncrease setExperienceIncrease(Element<?> parent, Element<?> element, int value, int cost) {
        ranksIncreased.computeIfAbsent(parent.getId(), k -> new HashSet<>());
        final ExperienceIncrease experienceIncrease = new ExperienceIncrease(element, value, cost);
        ranksIncreased.get(parent.getId()).add(experienceIncrease);
        return experienceIncrease;
    }

    public ExperienceIncrease setExperienceIncrease(Element<?> element, int value, int cost) {
        ranksIncreased.computeIfAbsent(element.getId(), k -> new HashSet<>());
        final ExperienceIncrease experienceIncrease = new ExperienceIncrease(element, value, cost);
        ranksIncreased.get(element.getId()).add(experienceIncrease);
        return experienceIncrease;
    }

    public void remove(OccultismPath occultismPath, OccultismPower occultismPower) throws InvalidPowerLevelException {
        // Must be added.
        if (ranksIncreased.get(occultismPath.getId()) == null) {
            return;
        }
        // Cannot be removed if there are extra levels for Psi.
        for (final ExperienceIncrease experienceIncrease : ranksIncreased.get(occultismPath.getId())) {
            if (((OccultismPower) experienceIncrease.getElement()).getLevel() > occultismPower.getLevel()) {
                throw new InvalidPowerLevelException(
                        "Power '" + occultismPower + "' cannot be removed due to a higher level has been adquired.");
            }
        }

        remove(occultismPath, occultismPower, occultismPower.getLevel());
    }

    public void remove(Element<?> element, int value) {
        remove(element, element, value);
    }

    public void remove(Element<?> parent, Element<?> element, int value) {
        if (element == null) {
            return;
        }
        remove(parent, new ExperienceIncrease(element, value, 0));
    }

    public void remove(Element<?> parent, ExperienceIncrease experienceIncrease) {
        if (ranksIncreased.get(parent.getId()) != null) {
            ranksIncreased.get(parent.getId()).remove(experienceIncrease);
        }
    }

    public static int getExperienceCostFor(Element<?> element, int valueToPurchase, CharacterPlayer characterPlayer)
            throws ElementCannotBeUpgradeWithExperienceException {
        int newCostMultiplier = 1;
        int existingCostMultiplier = 1;
        if (characterPlayer.hasBlessing("fixedDevelopment") && characterPlayer.getInfo().getAge() > 30) {
            newCostMultiplier = 3;
            existingCostMultiplier = 2;
        }
        if (element instanceof AvailableSkill) {
            // Lore skills are cheaper
            if (((AvailableSkill) element).getSkillDefinition().getSkillGroup().equals(SkillGroup.LORE)) {
                if (valueToPurchase == 1) {
                    return (int) (valueToPurchase * 1.5 * newCostMultiplier);
                }
                return (int) (valueToPurchase * 1.5 * existingCostMultiplier);
            }
            if (valueToPurchase == 1) {
                return valueToPurchase * 2 * newCostMultiplier;
            }
            return valueToPurchase * 2 * existingCostMultiplier;
        }
        if (element instanceof Characteristic) {
            return valueToPurchase * 3;
        }
        if (element instanceof OccultismType) {
            return valueToPurchase * 3;
        }
        if (element instanceof OccultismPower) {
            return valueToPurchase * 2;
        }
        if (element instanceof Wyrd) {
            return valueToPurchase * 2;
        }
        throw new ElementCannotBeUpgradeWithExperienceException(
                "Not upgradable element '" + element + "'. Experience cannot be used on it.");
    }

    public void setTotalExperience(int totalExperience) {
        this.totalExperience = totalExperience;
    }
}
