package com.softwaremagico.tm.random.selectors;

import java.util.Set;

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

public enum CombatPreferences implements ICharacterDescriptionPreference {
    // Threat level ~40
    PEACEFUL(0, 1, WeaponsPreferences.LOW, ArmourPreferences.NONE, ShieldPreferences.NONE),

    // Threat level ~65
    FAIR(2, 5, WeaponsPreferences.MEDIUM, ArmourPreferences.MEDIUM, ShieldPreferences.LOW),

    // Threat level ~85
    BELLIGERENT(4, 10, WeaponsPreferences.HIGH, ArmourPreferences.HIGH, ShieldPreferences.MEDIUM);

    private final int minimum;
    private final int maximum;

    private final WeaponsPreferences defaultWeaponPreferences;
    private final ArmourPreferences defaultArmourPreferences;
    private final ShieldPreferences defaultShieldPreferences;

    private CombatPreferences(int minimum, int maximum, WeaponsPreferences defaultWeaponPreferences, ArmourPreferences defaultArmourPreferences,
                              ShieldPreferences defaultShieldPreferences) {
        this.maximum = maximum;
        this.minimum = minimum;
        this.defaultWeaponPreferences = defaultWeaponPreferences;
        this.defaultArmourPreferences = defaultArmourPreferences;
        this.defaultShieldPreferences = defaultShieldPreferences;
    }

    @Override
    public int maximum() {
        return maximum;
    }

    @Override
    public int minimum() {
        return minimum;
    }

    public static CombatPreferences getSelected(Set<IRandomPreference> preferences) {
        for (final IRandomPreference preference : preferences) {
            if (preference instanceof CombatPreferences) {
                return (CombatPreferences) preference;
            }
        }
        final DifficultLevelPreferences difficultPreferences = DifficultLevelPreferences.getSelected(preferences);
        switch (difficultPreferences) {
            case EASY:
            case VERY_EASY:
            case MEDIUM:
                return FAIR;
            case HARD:
            case VERY_HARD:
                return BELLIGERENT;
        }
        return FAIR;
    }

    public WeaponsPreferences getDefaultWeaponPreferences() {
        return defaultWeaponPreferences;
    }

    public ArmourPreferences getDefaultArmourPreferences() {
        return defaultArmourPreferences;
    }

    public ShieldPreferences getDefaultShieldPreferences() {
        return defaultShieldPreferences;
    }

    @Override
    public IRandomPreference getDefault() {
        return getDefaultOption();
    }

    public static CombatPreferences getDefaultOption() {
        return null;
    }
}
