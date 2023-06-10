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

import com.softwaremagico.tm.character.Gender;
import com.softwaremagico.tm.random.RandomSelector;

import java.util.Set;

public enum GenderPreferences implements ICharacterDescriptionPreference<GenderPreferences> {

    // Gaussian distribution.
    MALE(1, 0),

    FEMALE(0, 1),

    USUALLY_MALE(20, 1),

    USUALLY_FEMALE(1, 20),

    PROBABLY_MALE(50, 1),

    PROBABLY_FEMALE(1, 50),

    ANY(1, 1);

    private final int maleProbability;
    private final int femaleProbability;

    GenderPreferences(int maleProbability, int femaleProbability) {
        this.maleProbability = maleProbability;
        this.femaleProbability = femaleProbability;
    }

    public static GenderPreferences getSelected(Set<IRandomPreference<?>> preferences) {
        for (final IRandomPreference<?> preference : preferences) {
            if (preference instanceof GenderPreferences) {
                return (GenderPreferences) preference;
            }
        }
        return ANY;
    }

    @Override
    public int maximum() {
        return 0;
    }

    @Override
    public int minimum() {
        return 0;
    }

    public Gender randomGender() {
        final int randomValue = RandomSelector.RANDOM.nextInt(maleProbability + femaleProbability);
        return randomValue < maleProbability ? Gender.MALE : Gender.FEMALE;
    }

    @Override
    public IRandomPreference<GenderPreferences> getDefault() {
        return getDefaultOption();
    }

    public static GenderPreferences getDefaultOption() {
        return GenderPreferences.ANY;
    }
}
