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
import java.util.Set;

import com.softwaremagico.tm.random.IElementWithRandomElements;
import com.softwaremagico.tm.rules.Element;

public class RandomParty extends Element<RandomParty> implements IElementWithRandomElements<RandomPartyMember> {
	private final HashSet<RandomPartyMember> randomPartyMembers;

	public RandomParty(String id, String name, String language) {
		super(id, name, language);
		randomPartyMembers = new HashSet<>();
	}

	public HashSet<RandomPartyMember> getRandomPartyMembers() {
		return randomPartyMembers;
	}

	@Override
	public Set<RandomPartyMember> getAllElements() {
		return getRandomPartyMembers();
	}

	@Override
	public Set<RandomPartyMember> getMandatoryElements() {
		final Set<RandomPartyMember> mandatoryElements = new HashSet<>();
		for (final RandomPartyMember member : getRandomPartyMembers()) {
			if (member.getMinNumber() != null && member.getMinNumber() > 0) {
				mandatoryElements.add(member);
			}
		}
		return mandatoryElements;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

}
