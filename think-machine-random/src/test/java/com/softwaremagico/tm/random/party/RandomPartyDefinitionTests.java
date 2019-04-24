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

import org.testng.annotations.Test;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.party.Party;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.IRandomPreference;
import com.softwaremagico.tm.txt.PartySheet;

@Test(groups = { "randomParty" })
public class RandomPartyDefinitionTests {
	private final static String LANGUAGE = "en";

	@Test
	public void createThugParty() throws InvalidXmlElementException, InvalidRandomElementSelectedException {
		RandomParty thugParty = RandomPartyFactory.getInstance().getElement("thugBand", LANGUAGE);
		RandomPartyDefinition randomPartyDefinition = new RandomPartyDefinition(thugParty, 200,
				new HashSet<IRandomPreference>());
		randomPartyDefinition.assign();
		Party party = randomPartyDefinition.getParty();
		PartySheet partySheet = new PartySheet(party);
		System.out.println(partySheet.toString());
	}

}
