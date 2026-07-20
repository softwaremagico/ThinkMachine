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

public enum CashPreferences implements ICharacterDescriptionPreference<CashPreferences>, IGaussianDistribution {

    LOW(0, 4, 0, 4),

    FAIR(4, 8, 4, 4),

    GOOD(8, 16, 12, 4),

    HIGH(12, 24, 16, 8),

    VERY_HIGH(16, 24, 20, 4),

    ANY(0, 24, 14, 10);

    private final int minimum;
    private final int maximum;
    private final int mean;
    private final int variance;

    CashPreferences(int minimumValue, int maximumValue, int mean, int variance) {
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

    public static CashPreferences getSelected(Set<IRandomPreference<?>> preferences) {
        for (final IRandomPreference<?> preference : preferences) {
            if (preference instanceof CashPreferences) {
                return (CashPreferences) preference;
            }
        }
        return null;
    }

    public static CashPreferences get(FactionPreferences factionPreferences) {
        if (factionPreferences != null) {
            switch (factionPreferences) {
                case NOBILITY:
                    return CashPreferences.HIGH;
                case GUILD:
                    return CashPreferences.GOOD;
                case CHURCH:
                    return CashPreferences.FAIR;
                case MINOR_GUILD:
                    return CashPreferences.LOW;
                default:
                    return null;
            }
        }
        return null;
    }

    public static CashPreferences get(DifficultLevelPreferences difficultLevelPreferences) {
        if (difficultLevelPreferences != null) {
            switch (difficultLevelPreferences) {
                case EASY:
                case VERY_EASY:
                    return CashPreferences.LOW;
                case MEDIUM:
                case HARD:
                    // Be careful. maxPoints can limit this value.
                    return CashPreferences.GOOD;
                case VERY_HARD:
                    // Be careful. maxPoints can limit this value.
                    return CashPreferences.HIGH;
                default:
                    return null;
            }
        }
        return null;
    }

    public static CashPreferences get(RankPreferences rankPreferences) {
        if (rankPreferences != null) {
            switch (rankPreferences) {
                case GOOD:
                    return CashPreferences.GOOD;
                case HIGH:
                    return CashPreferences.HIGH;
                default:
                    return null;
            }
        }
        return null;
    }

    @Override
    public int randomGaussian() {
        int selectedValue;
        do {
            selectedValue = (int) (RandomSelector.RANDOM.nextGaussian() * Math.sqrt(variance) + mean);
        } while (selectedValue < minimum() || selectedValue > maximum());
        return selectedValue;
    }

    public static CashPreferences get(Float money) {
        final int cashCost = (int) Math.ceil(money / 250);
        for (final CashPreferences preference : CashPreferences.values()) {
            if (preference.minimum() >= cashCost) {
                return preference;
            }
        }
        return VERY_HIGH;
    }

    @Override
    public IRandomPreference<CashPreferences> getDefault() {
        return getDefaultOption();
    }

    public static CashPreferences getDefaultOption() {
        return CashPreferences.FAIR;
    }
}
