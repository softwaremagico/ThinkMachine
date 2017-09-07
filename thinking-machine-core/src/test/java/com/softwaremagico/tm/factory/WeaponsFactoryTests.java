package com.softwaremagico.tm.factory;

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.equipment.WeaponFactory;

@Test(groups = { "weaponsFactory" })
public class WeaponsFactoryTests {

	@Test
	public void readWeapons() throws InvalidXmlElementException {
		Assert.assertEquals(7, WeaponFactory.getInstance().getElements("es").size());
	}

}
