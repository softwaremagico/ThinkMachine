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

public enum TechnologicalPreferences implements IEquipmentPreference<TechnologicalPreferences> {

    PREHISTORIC(0, 1),

    MEDIEVAL(1, 2),

    BASIC(2, 3),

    MODERN(3, 5),

    FUTURIST(4, 7),

    SPACE_CITIZEN(6, 9),

    MAXIMAL(7, 10);

    private final int minimum;
    private final int maximum;

    private TechnologicalPreferences(int minimum, int maximum) {
        this.maximum = maximum;
        this.minimum = minimum;
    }

    @Override
    public int maximum() {
        return maximum;
    }

    @Override
    public int minimum() {
        return minimum;
    }

    /**
     * Checks if the current preference is greater equals than other status.
     *
     * @param preference status to compare.
     * @return true if it is passed.
     */
    public boolean isMoreThan(TechnologicalPreferences preference) {
        return maximum >= preference.maximum;
    }

    public static TechnologicalPreferences getSelected(Set<IRandomPreference<?>> preferences) {
        for (final IRandomPreference<?> preference : preferences) {
            if (preference instanceof TechnologicalPreferences) {
                return (TechnologicalPreferences) preference;
            }
        }
        return MODERN;
    }

    @Override
    public IRandomPreference<TechnologicalPreferences> getDefault() {
        return getDefaultOption();
    }

    public static TechnologicalPreferences getDefaultOption() {
        return null;
    }

}
