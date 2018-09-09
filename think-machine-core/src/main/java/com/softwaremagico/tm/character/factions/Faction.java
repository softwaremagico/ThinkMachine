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
import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.race.Race;
import com.softwaremagico.tm.log.MachineLog;

public class Faction extends Element<Faction> {
	private final FactionGroup factionGroup;
	private final Set<FactionRankTranslation> ranksTranslations = new HashSet<>();
	private final Race restrictedRace;
	private Set<Blessing> blessings = null;
	private Set<AvailableBenefice> benefices = null;
	private final String language;
	private final FactionRandomDefinitions randomDefinition;

	public Faction(String id, String name, FactionGroup factionGroup, Race restrictedRace, String language) {
		super(id, name);
		this.factionGroup = factionGroup;
		this.restrictedRace = restrictedRace;
		this.language = language;
		this.randomDefinition = new FactionRandomDefinitions();
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
			// Blessings are not read with factions due to a loop
			// factions->blessings->skills->factions
			try {
				FactionsFactory.getInstance().setBlessings(this, language);
			} catch (InvalidFactionException e) {
				MachineLog.errorMessage(this.getClass().getName(), e);
			}
		}
		return blessings;
	}

	public Set<AvailableBenefice> getBenefices() {
		if (benefices == null) {
			// Benefices are not read with factions due to a loop
			// factions->benefices->skills->factions
			try {
				FactionsFactory.getInstance().setBenefices(this, language);
			} catch (InvalidFactionException e) {
				MachineLog.errorMessage(this.getClass().getName(), e);
			}
		}
		return benefices;
	}

	public void setBlessings(Set<Blessing> blessings) {
		this.blessings = blessings;
	}

	public void setBenefices(Set<AvailableBenefice> benefices) {
		this.benefices = benefices;
	}

	public FactionRandomDefinitions getRandomDefinition() {
		return randomDefinition;
	}
}
