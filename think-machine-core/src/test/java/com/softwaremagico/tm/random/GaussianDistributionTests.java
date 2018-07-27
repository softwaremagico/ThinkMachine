package com.softwaremagico.tm.random;

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
