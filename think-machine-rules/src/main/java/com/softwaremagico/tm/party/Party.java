package com.softwaremagico.tm.party;

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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.ThreatLevel;

public class Party extends Element<Party> {
	private final Set<CharacterPlayer> characterPlayers;
	private final Map<CharacterPlayer, Integer> threatByCharacter;
	private String partyName;

	public Party(String id, String name, String language) {
		super(id, name, language);
		characterPlayers = new HashSet<>();
		threatByCharacter = new HashMap<>();
	}

	public void addCharacter(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
		characterPlayers.add(characterPlayer);
		threatByCharacter.put(characterPlayer, ThreatLevel.getThreatLevel(characterPlayer));
	}

	public void removeCharacter(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
		characterPlayers.remove(characterPlayer);
		threatByCharacter.remove(characterPlayer);
	}

	public int getThreatLevel() {
		int threatLevel = 0;
		for (Integer threat : threatByCharacter.values()) {
			threatLevel += threat;
		}
		return threatLevel;
	}

	public int getThreatLevel(CharacterPlayer characterPlayer) {
		if (threatByCharacter.get(characterPlayer) == null) {
			return 0;
		}
		return threatByCharacter.get(characterPlayer);
	}

	public Set<CharacterPlayer> getMembers() {
		return Collections.unmodifiableSet(characterPlayers);
	}

	public String getPartyName() {
		return partyName;
	}

	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}
}
