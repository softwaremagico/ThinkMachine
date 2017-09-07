package com.softwaremagico.tm.factory;

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.traits.BlessingFactory;

@Test(groups = { "blessingFactory" })
public class BlessingFactoryTests {

	@Test
	public void readBlessings() throws InvalidXmlElementException {
		Assert.assertEquals(3, BlessingFactory.getInstance().getElements("es").size());
	}
}
