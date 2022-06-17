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

import com.softwaremagico.tm.random.RandomSelector;

import java.util.Set;

public enum AgePreferences implements ICharacterDescriptionPreference<AgePreferences>, IGaussianDistribution {

    // Gaussian distribution.
    CHILD(6, 12, 9, 2),

    TEENAGER(13, 16, 15, 1),

    YOUNG(17, 20, 19, 1),

    ADULT(21, 30, 25, 3),

    EXPERIENCED_ADULT(31, 50, 40, 8),

    OLD(51, 70, 60, 8),

    VERY_OLD(71, 110, 85, 10),

    ANY(6, 110, 50, 50);

    private final int minimum;
    private final int maximum;
    private final int mean;
    private final int variance;

    AgePreferences(int minimumValue, int maximumValue, int mean, int variance) {
        this.maximum = maximumValue;
        this.minimum = minimumValue;
        this.variance = variance;
        this.mean = mean;
    }

    @Override
    public int maximum() {
        return maximum;
    }

    @Override
    public int minimum() {
        return minimum;
    }

    @Override
    public int variance() {
        return variance;
    }

    @Override
    public int mean() {
        return mean;
    }

    public static AgePreferences getSelected(Set<IRandomPreference<?>> preferences) {
        for (final IRandomPreference<?> preference : preferences) {
            if (preference instanceof AgePreferences) {
                return (AgePreferences) preference;
            }
        }
        return EXPERIENCED_ADULT;
    }

    @Override
    public int randomGaussian() {
        int selectedValue;
        do {
            selectedValue = (int) (RandomSelector.random.nextGaussian() * Math.sqrt(variance) + mean);
        } while (selectedValue < minimum() || selectedValue > maximum());
        return selectedValue;
    }

    @Override
    public IRandomPreference<AgePreferences> getDefault() {
        return getDefaultOption();
    }

    public static AgePreferences getDefaultOption() {
        return AgePreferences.EXPERIENCED_ADULT;
    }
}
