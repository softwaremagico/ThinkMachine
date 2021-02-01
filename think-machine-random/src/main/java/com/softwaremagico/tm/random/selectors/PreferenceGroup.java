package com.softwaremagico.tm.random.selectors;

/*-
 * #%L
 * Think Machine (Random Generator)
 * %%
 * Copyright (C) 2017 - 2021 Softwaremagico
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

public enum PreferenceGroup {

    EQUIPMENT(WeaponsPreferences.class, ArmourPreferences.class, ShieldPreferences.class, TechnologicalPreferences.class),

    CYBERNETICS(CyberneticPointsPreferences.class, CyberneticTotalDevicesPreferences.class, CyberneticVisibilityPreferences.class),

    PSI(PsiqueLevelPreferences.class, PsiquePathLevelPreferences.class),

    CHARACTER_CREATION(BlessingNumberPreferences.class, CharacteristicsPreferences.class, CurseNumberPreferences.class,
            ExtraBeneficesNumberPreferences.class, SkillGroupPreferences.class, TraitCostPreferences.class),

    CHARACTER_DESCRIPTION(AgePreferences.class, BlessingPreferences.class, CombatPreferences.class, DifficultLevelPreferences.class,
            FactionPreferences.class, NamesPreferences.class, RacePreferences.class, SpecializationPreferences.class, StatusPreferences.class);

    private final Class<? extends IRandomPreference>[] randomPreferences;

    PreferenceGroup(Class<? extends IRandomPreference>... randomPreferences) {
        this.randomPreferences = randomPreferences;
    }

    public Class<? extends IRandomPreference>[] getRandomPreferences() {
        return randomPreferences;
    }
}
