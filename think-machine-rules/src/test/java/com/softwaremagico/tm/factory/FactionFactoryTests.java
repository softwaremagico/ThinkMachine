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

import org.junit.BeforeClass;
import org.testng.annotations.Test;

import com.softwaremagico.tm.CacheHandler;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.Gender;
import com.softwaremagico.tm.character.benefices.AvailableBeneficeFactory;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionsFactory;

@Test(groups = { "factionsFactory" })
public class FactionFactoryTests {
	private final static int DEFINED_FACTIONS = 19;
	private final static int DEFINED_MALE_NAMES = 103;
	private final static int DEFINED_FEMALE_NAMES = 100;
	private final static int DEFINED_SURNAMES = 125;
	private final static String LANGUAGE = "es";

	@BeforeClass
	public void clearCache() {
		CacheHandler.clearCache();
	}

	@Test
	public void readFactions() throws InvalidXmlElementException {
		Assert.assertEquals(DEFINED_FACTIONS, FactionsFactory.getInstance().getElements(LANGUAGE).size());
	}

	@Test
	public void readAfflictions() throws InvalidXmlElementException {
		Faction vorox = FactionsFactory.getInstance().getElement("vorox", LANGUAGE);
		Assert.assertTrue(vorox.getBenefices().contains(AvailableBeneficeFactory.getInstance().getElement("noOccult", LANGUAGE)));
	}

	@Test
	public void readNames() throws InvalidXmlElementException {
		Faction hazat = FactionsFactory.getInstance().getElement("hazat", LANGUAGE);
		Assert.assertNotNull(hazat);
		Assert.assertTrue(FactionsFactory.getInstance().getAllNames(hazat, Gender.MALE).size() >= DEFINED_MALE_NAMES);
		Assert.assertTrue(FactionsFactory.getInstance().getAllNames(hazat, Gender.FEMALE).size() >= DEFINED_FEMALE_NAMES);
		Assert.assertTrue(FactionsFactory.getInstance().getAllSurnames(hazat).size() >= DEFINED_SURNAMES);
	}
}
