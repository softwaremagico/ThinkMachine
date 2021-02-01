package com.softwaremagico.tm.random.selectors;

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

import java.util.Set;

public enum DifficultLevelPreferences implements IRandomPreference {

    // Threat level ~50
    VERY_EASY(-5, -15, 0),

    // Threat level ~58
    EASY(-3, -10, 0),

    // Threat level ~65
    MEDIUM(0, 0, 0),

    // Threat level ~85
    HARD(0, 5, 50),

    // Threat level ~100
    VERY_HARD(5, 10, 100);

    private final int experienceBonus;
    private final int skillsBonus;
    private final int characteristicsBonus;

    private DifficultLevelPreferences(int characteristicsBonus, int skillsBonus, int extraExperience) {
        this.characteristicsBonus = characteristicsBonus;
        this.skillsBonus = skillsBonus;
        this.experienceBonus = extraExperience;
    }

    @Override
    public int maximum() {
        return 0;
    }

    @Override
    public int minimum() {
        return 0;
    }

    @Override
    public PreferenceGroup getGroup() {
        return PreferenceGroup.CHARACTER_DESCRIPTION;
    }

    public static DifficultLevelPreferences getSelected(Set<IRandomPreference> preferences) {
        for (final IRandomPreference preference : preferences) {
            if (preference instanceof DifficultLevelPreferences) {
                return (DifficultLevelPreferences) preference;
            }
        }
        return DifficultLevelPreferences.MEDIUM;
    }

    public int getExperienceBonus() {
        return experienceBonus;
    }

    public int getSkillsBonus() {
        return skillsBonus;
    }

    public int getCharacteristicsBonus() {
        return characteristicsBonus;
    }

}
