package com.softwaremagico.tm.txt;

/*-
 * #%L
 * Think Machine (Rules)
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

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.party.Party;

public class PartySheet {
	private final Party party;

	public PartySheet(Party party) {
		this.party = party;
	}

	private Party getParty() {
		return party;
	}

	private String createContent() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(party.getName());
		stringBuilder.append("\n");
		for (CharacterPlayer characterPlayer : getParty().getMembers()) {
			CharacterSheet characterSheet = new CharacterSheet(characterPlayer);
			stringBuilder.append(characterSheet.toString());
			stringBuilder.append("\n");
		}

		return stringBuilder.toString();
	}

	@Override
	public String toString() {
		return createContent();
	}
}
