package com.softwaremagico.tm.random.party;

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
import com.softwaremagico.tm.file.PathManager;
import com.softwaremagico.tm.random.selectors.AgePreferences;
import com.softwaremagico.tm.random.selectors.DifficultLevelPreferences;

@Test(groups = { "randomPartyFactory" })
public class RandomPartyFactoryTests {

	private static final int DEFINED_PARTIES = 1;
	private static final String LANGUAGE = "en";

	@Test
	public void readParties() throws InvalidXmlElementException {
		Assert.assertEquals(RandomPartyFactory.getInstance().getElements(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).size(), DEFINED_PARTIES);
	}

	@Test
	public void readNames() throws InvalidXmlElementException {
		Assert.assertNotNull(RandomPartyFactory.getInstance().getNames(
				RandomPartyFactory.getInstance().getElement("thugBand", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)));
		Assert.assertNotNull(RandomPartyFactory.getInstance().getAdjectives(
				RandomPartyFactory.getInstance().getElement("thugBand", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)));
	}

	@Test
	public void readThugParty() throws InvalidXmlElementException {
		Assert.assertEquals(RandomPartyFactory.getInstance().getElement("thugBand", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getRandomPartyMembers()
				.size(), 3);
		int checkedMemebers = 0;
		for (final RandomPartyMember member : RandomPartyFactory.getInstance()
				.getElement("thugBand", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
				.getRandomPartyMembers()) {
			if (member.getId().equals("thugBand_0")) {
				checkedMemebers++;
				Assert.assertEquals((int) member.getMinNumber(), 1);
				Assert.assertEquals((int) member.getMaxNumber(), 1);
				Assert.assertTrue(member.getRandomPreferences().contains(DifficultLevelPreferences.HARD));
			} else if (member.getId().equals("thugBand_1")) {
				checkedMemebers++;
				Assert.assertEquals((int) member.getWeight(), 3);
			} else if (member.getId().equals("thugBand_2")) {
				checkedMemebers++;
				Assert.assertEquals((int) member.getWeight(), 1);
				Assert.assertTrue(member.getRandomPreferences().contains(AgePreferences.TEENAGER));
			}
		}
		Assert.assertEquals(checkedMemebers, 3);
	}

}
