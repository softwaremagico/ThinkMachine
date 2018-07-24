package com.softwaremagico.tm.character.skills;

import java.util.HashSet;
import java.util.Set;

import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.character.race.Race;

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

public class SkillRandomDefinitions {

	private int minimumTechLevel;
	private final Set<Faction> recommendedFactions = new HashSet<>();
	private final Set<Race> recommendedRaces = new HashSet<>();
	private final Set<FactionGroup> recommendedFactionGroups = new HashSet<>();
	private SkillRandomProbability probability = SkillRandomProbability.FAIR;

	public int getMinimumTechLevel() {
		return minimumTechLevel;
	}

	public void setMinimumTechLevel(int minimumTechLevel) {
		this.minimumTechLevel = minimumTechLevel;
	}

	public Set<Faction> getRecommendedFactions() {
		return recommendedFactions;
	}

	public void addRecommendedFactionGroup(FactionGroup recommendedFactionGroup) {
		if (recommendedFactionGroup != null) {
			recommendedFactionGroups.add(recommendedFactionGroup);
		}
	}

	public Set<FactionGroup> getRecommendedFactionGroups() {
		return recommendedFactionGroups;
	}

	public void addRecommendedFaction(Faction faction) {
		if (faction != null) {
			recommendedFactions.add(faction);
		}
	}

	public SkillRandomProbability getProbability() {
		return probability;
	}

	public void setProbability(SkillRandomProbability probability) {
		this.probability = probability;
	}

	public void addRecommendedRace(Race race) {
		if (race != null) {
			recommendedRaces.add(race);
		}
	}

	public Set<Race> getRecommendedRaces() {
		return recommendedRaces;
	}
}
