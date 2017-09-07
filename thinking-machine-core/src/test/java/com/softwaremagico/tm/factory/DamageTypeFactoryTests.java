package com.softwaremagico.tm.factory;

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.equipment.DamageTypeFactory;

@Test(groups = { "damageFactory" })
public class DamageTypeFactoryTests {
	
	@Test
	public void readDamages() throws InvalidXmlElementException {
		Assert.assertEquals(7, DamageTypeFactory.getInstance().getElements("es").size());
	}
}
