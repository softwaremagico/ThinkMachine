package com.softwaremagico.tm.character.factions;

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

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.softwaremagico.tm.Element;

public class Faction extends Element<Faction> {
	private final FactionGroup factionGroup;
	private final Set<FactionRankTranslation> ranksTranslations = new HashSet<>();

	public Faction(String id, String name, FactionGroup factionGroup) {
		super(id, name);
		this.factionGroup = factionGroup;
	}

	public FactionGroup getFactionGroup() {
		return factionGroup;
	}

	public void addRankTranslation(FactionRankTranslation factionRank) {
		ranksTranslations.add(factionRank);
	}

	public Set<FactionRankTranslation> getRanksTranslations() {
		return ranksTranslations;
	}

	public FactionRankTranslation getRankTranslation(String rankId) {
		for (FactionRankTranslation factionRankTranslation : getRanksTranslations()) {
			if (Objects.equals(factionRankTranslation.getId(), rankId)) {
				return factionRankTranslation;
			}
		}
		return null;
	}
}
