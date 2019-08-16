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

import org.testng.Assert;
import org.testng.annotations.Test;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.equipment.armours.ArmourFactory;

@Test(groups = { "armourFactory" })
public class ArmourFactoryTests {

	private static final int DEFINED_ARMOURS = 30;
	private static final String LANGUAGE = "es";
	private static final String MODULE = "Fading Suns Revised Edition";

	@Test
	public void readArmours() throws InvalidXmlElementException {
		Assert.assertEquals(ArmourFactory.getInstance().getElements(LANGUAGE, MODULE).size(), DEFINED_ARMOURS);
	}

	@Test
	public void readArmoursSpecifications() throws InvalidXmlElementException {
		Assert.assertEquals(ArmourFactory.getInstance().getElement("adeptRobes", LANGUAGE, MODULE).getSpecifications()
				.size(), 7);
	}

	@Test
	public void readShieldsOfArmour() throws InvalidXmlElementException {
		Assert.assertEquals(ArmourFactory.getInstance().getElement("synthsilk", LANGUAGE, MODULE).getAllowedShields().size(), 4);
		Assert.assertEquals(ArmourFactory.getInstance().getElement("adeptRobes", LANGUAGE, MODULE).getAllowedShields().size(),
				1);
	}

	@Test
	public void readDamagesOfArmour() throws InvalidXmlElementException {
		Assert.assertEquals(ArmourFactory.getInstance().getElement("ceramsteelExoframe", LANGUAGE, MODULE).getDamageTypes()
				.size(), 4);
		Assert.assertEquals(ArmourFactory.getInstance().getElement("spacesuit", LANGUAGE, MODULE).getDamageTypes().size(), 4);
	}
}
