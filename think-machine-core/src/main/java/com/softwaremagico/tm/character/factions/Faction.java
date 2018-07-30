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
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.race.Race;

public class Faction extends Element<Faction> {
	private final FactionGroup factionGroup;
	private final Set<FactionRankTranslation> ranksTranslations = new HashSet<>();

	private final Race restrictedRace;
	private final Set<Blessing> blessings;

	public Faction(String id, String name, FactionGroup factionGroup, Race restrictedRace, Set<Blessing> mandatoryBlessings) {
		super(id, name);
		this.factionGroup = factionGroup;
		this.restrictedRace = restrictedRace;
		this.blessings = mandatoryBlessings;
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

	public Race getRestrictedRace() {
		return restrictedRace;
	}

	public Set<Blessing> getBlessings() {
		if (blessings == null) {
			return new HashSet<Blessing>();
		}
		return blessings;
	}
}
