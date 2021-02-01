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

public enum ShieldPreferences implements IRandomPreference {

    NONE(0f),

    LOW(0.2f),

    MEDIUM(0.4f),

    HIGH(1f);

    private final float shieldProbability;

    private ShieldPreferences(float shieldProbability) {
        this.shieldProbability = shieldProbability;
    }

    public static ShieldPreferences getSelected(Set<IRandomPreference> preferences) {
        for (final IRandomPreference preference : preferences) {
            if (preference instanceof ShieldPreferences) {
                return (ShieldPreferences) preference;
            }
        }
        final DifficultLevelPreferences difficultPreferences = DifficultLevelPreferences.getSelected(preferences);
        switch (difficultPreferences) {
            case EASY:
            case VERY_EASY:
                return NONE;
            case MEDIUM:
                return LOW;
            case HARD:
                return MEDIUM;
            case VERY_HARD:
                return HIGH;
        }
        return null;
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
        return PreferenceGroup.EQUIPMENT;
    }

    public float getShieldProbability() {
        return shieldProbability;
    }

}
