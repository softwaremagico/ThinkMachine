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

import java.util.HashSet;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.IRandomPreference;
import com.softwaremagico.tm.rules.InvalidXmlElementException;

@Test(groups = { "randomParty" })
public class RandomPartyDefinitionTests {
	private static final String LANGUAGE = "es";

	@Test
	public void mandatoryMembersAdded() throws InvalidXmlElementException, InvalidRandomElementSelectedException {
		final RandomParty thugParty = RandomPartyFactory.getInstance().getElement("thugBand", LANGUAGE);
		final RandomPartyDefinition randomPartyDefinition = new RandomPartyDefinition(thugParty, 0,
				new HashSet<IRandomPreference>());
		randomPartyDefinition.assign();
		// One mandatory and one random element that is always added.
		Assert.assertEquals(randomPartyDefinition.getParty().getMembers().size(), 1 + 1);
		Assert.assertNotNull(randomPartyDefinition.getParty().getPartyName());
	}

	@Test
	public void threatLevelAddMoreMembersToParty() throws InvalidXmlElementException,
			InvalidRandomElementSelectedException {
		final RandomParty thugParty = RandomPartyFactory.getInstance().getElement("thugBand", LANGUAGE);
		final RandomPartyDefinition randomPartyDefinition = new RandomPartyDefinition(thugParty, 200,
				new HashSet<IRandomPreference>());
		randomPartyDefinition.assign();
		Assert.assertNotNull(randomPartyDefinition.getParty().getPartyName());

		final RandomParty thugParty2 = RandomPartyFactory.getInstance().getElement("thugBand", LANGUAGE);
		final RandomPartyDefinition randomPartyDefinition2 = new RandomPartyDefinition(thugParty2, 450,
				new HashSet<IRandomPreference>());
		randomPartyDefinition2.assign();

		Assert.assertTrue(randomPartyDefinition2.getParty().getMembers().size() > randomPartyDefinition.getParty()
				.getMembers().size());
		Assert.assertNotNull(randomPartyDefinition2.getParty().getPartyName());
	}

}
