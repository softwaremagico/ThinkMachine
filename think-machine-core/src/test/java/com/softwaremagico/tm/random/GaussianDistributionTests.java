package com.softwaremagico.tm.random;

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

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.softwaremagico.tm.random.selectors.IGaussianDistribution;
import com.softwaremagico.tm.random.selectors.StatusPreferences;

@Test(groups = { "gaussianDistribution" })
public class GaussianDistributionTests {

	private Map<Integer, Integer> getValues(IGaussianDistribution gaussianDistributionToTest, int maxIterations) {
		Map<Integer, Integer> totalValues = new HashMap<>();
		for (int i = 0; i < maxIterations; i++) {
			int value = gaussianDistributionToTest.randomGaussian();
			if (totalValues.get(value) == null) {
				totalValues.put(value, 1);
			} else {
				totalValues.put(value, totalValues.get(value) + 1);
			}
		}
		return totalValues;
	}

	@Test
	public void checkGaussianValuesForPreference() {
		System.out.println(getValues(StatusPreferences.HIGHT, 10000));
		System.out.println(getValues(StatusPreferences.GOOD, 10000));
		System.out.println(getValues(StatusPreferences.LOW, 10000));
	}
}
