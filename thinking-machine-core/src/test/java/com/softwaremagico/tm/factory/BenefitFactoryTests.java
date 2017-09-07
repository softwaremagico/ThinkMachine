package com.softwaremagico.tm.factory;

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.traits.BenefitFactory;

@Test(groups = { "benefitFactory" })
public class BenefitFactoryTests {

	@Test
	public void readBenefits() throws InvalidXmlElementException {
		Assert.assertEquals(1, BenefitFactory.getInstance().getElements("es").size());
	}
}
