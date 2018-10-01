package com.softwaremagico.tm.factory;

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

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.equipment.weapons.AccessoryFactory;
import com.softwaremagico.tm.character.equipment.weapons.AmmunitionFactory;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;

@Test(groups = { "weaponsFactory" })
public class WeaponsFactoryTests {
	private final static String LANGUAGE = "es";

	@Test
	public void readWeapons() throws InvalidXmlElementException {
		Assert.assertTrue(WeaponFactory.getInstance().getElements(LANGUAGE).size() > 90);
	}

	@Test
	public void readAmmunition() throws InvalidXmlElementException {
		Assert.assertTrue(AmmunitionFactory.getInstance().getElements(LANGUAGE).size() > 0);
	}

	@Test
	public void readAccessory() throws InvalidXmlElementException {
		Assert.assertTrue(AccessoryFactory.getInstance().getElements(LANGUAGE).size() > 0);
	}

	@Test
	public void checkShotgun() throws InvalidXmlElementException {
		Assert.assertEquals(1, WeaponFactory.getInstance().getElement("typicalShotgun", LANGUAGE).getAmmunitions().size());
	}

	@Test
	public void checkBasicHuntingRifle() throws InvalidXmlElementException {
		Assert.assertEquals(3, WeaponFactory.getInstance().getElement("basicHuntingRifle", LANGUAGE).getAccesories().size());
	}

}
