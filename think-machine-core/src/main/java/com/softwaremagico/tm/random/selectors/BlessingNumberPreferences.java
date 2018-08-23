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

import java.util.Random;
import java.util.Set;

public enum BlessingNumberPreferences implements IRandomPreferences, IGaussianDistribution {

	NONE(0, 0, 0, 0),
	
	LOW(1, 2, 1, 1),

	FAIR(2, 5, 2, 2),

	HIGH(3, 7, 4, 3);

	private final int minimum;
	private final int maximum;
	private final int mean;
	private final int variance;
	private final Random random = new Random();

	private BlessingNumberPreferences(int minimumValue, int maximumValue, int mean, int variance) {
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

	public static BlessingNumberPreferences getSelected(Set<IRandomPreferences> preferences) {
		for (IRandomPreferences preference : preferences) {
			if (preference instanceof BlessingNumberPreferences) {
				return (BlessingNumberPreferences) preference;
			}
		}
		return BlessingNumberPreferences.LOW;
	}

	@Override
	public int randomGaussian() {
		int selectedValue;
		do {
			selectedValue = (int) (random.nextGaussian() * Math.sqrt(variance) + mean);
		} while (selectedValue < minimum() || selectedValue > maximum());
		return selectedValue;
	}
}
