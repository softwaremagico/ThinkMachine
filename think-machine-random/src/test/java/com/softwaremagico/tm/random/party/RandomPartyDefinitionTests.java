package com.softwaremagico.tm.random.party;

/*-
 * #%L
 * Think Machine (Random Generator)
 * %%
 * Copyright (C) 2017 - 2019 Softwaremagico
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

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.exceptions.RestrictedElementException;
import com.softwaremagico.tm.character.exceptions.UnofficialElementNotAllowedException;
import com.softwaremagico.tm.file.PathManager;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashSet;

@Test(groups = { "randomParty" })
public class RandomPartyDefinitionTests {
	private static final String LANGUAGE = "es";

	@Test(timeOut = 5000)
	public void mandatoryMembersAdded() throws InvalidXmlElementException, RestrictedElementException, UnofficialElementNotAllowedException {
		final RandomParty thugParty = RandomPartyFactory.getInstance().getElement("thugBand", LANGUAGE,
				PathManager.DEFAULT_MODULE_FOLDER);
		final RandomPartyDefinition randomPartyDefinition = new RandomPartyDefinition(thugParty, 0,
				PathManager.DEFAULT_MODULE_FOLDER, new HashSet<>());
		randomPartyDefinition.assign();
		// One mandatory and one random element that is always added.
		Assert.assertEquals(randomPartyDefinition.getParty().getMembers().size(), 1 + 1);
		Assert.assertNotNull(randomPartyDefinition.getParty().getPartyName());
	}

	@Test(timeOut = 5000)
	public void threatLevelAddMoreMembersToParty() throws InvalidXmlElementException,
			RestrictedElementException, UnofficialElementNotAllowedException {
		final RandomParty thugParty = RandomPartyFactory.getInstance().getElement("thugBand", LANGUAGE,
				PathManager.DEFAULT_MODULE_FOLDER);
		final RandomPartyDefinition randomPartyDefinition = new RandomPartyDefinition(thugParty, 200,
				PathManager.DEFAULT_MODULE_FOLDER, new HashSet<>());
		randomPartyDefinition.assign();
		Assert.assertNotNull(randomPartyDefinition.getParty().getPartyName());

		final RandomParty thugParty2 = RandomPartyFactory.getInstance().getElement("thugBand", LANGUAGE,
				PathManager.DEFAULT_MODULE_FOLDER);
		final RandomPartyDefinition randomPartyDefinition2 = new RandomPartyDefinition(thugParty2, 450,
				PathManager.DEFAULT_MODULE_FOLDER, new HashSet<>());
		randomPartyDefinition2.assign();

		Assert.assertTrue(randomPartyDefinition2.getParty().getMembers().size() > randomPartyDefinition.getParty()
				.getMembers().size());
		Assert.assertNotNull(randomPartyDefinition2.getParty().getPartyName());
	}

}
