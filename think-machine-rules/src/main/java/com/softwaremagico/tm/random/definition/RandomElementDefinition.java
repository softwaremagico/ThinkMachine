package com.softwaremagico.tm.random.definition;

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
import java.util.Set;

import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.character.races.Race;

public class RandomElementDefinition {
	private Integer staticProbability;
	private Integer minimumTechLevel;
	private Integer maximumTechLevel;
	private Double probabilityMultiplier;
	private final Set<Faction> restrictedFactions = new HashSet<>();
	private final Set<Faction> recommendedFactions = new HashSet<>();
	private final Set<Race> forbiddenRaces = new HashSet<>();
	private final Set<Race> restrictedRaces = new HashSet<>();
	private final Set<Race> recommendedRaces = new HashSet<>();
	private final Set<FactionGroup> restrictedFactionGroups = new HashSet<>();
	private final Set<FactionGroup> recommendedFactionGroups = new HashSet<>();
	private RandomProbabilityDefinition probability;

	public RandomElementDefinition() {

	}

	public RandomElementDefinition(RandomElementDefinition... randomDefinitions) {
		this();
		for (RandomElementDefinition randomDefinition : randomDefinitions) {
			update(randomDefinition);
		}
	}

	private void update(RandomElementDefinition randomDefinition) {
		if (randomDefinition.getStaticProbability() != null) {
			setStaticProbability(randomDefinition.getStaticProbability());
		}
		if (randomDefinition.getMinimumTechLevel() != null) {
			setMinimumTechLevel(randomDefinition.getMinimumTechLevel());
		}
		if (randomDefinition.getMaximumTechLevel() != null) {
			setMaximumTechLevel(randomDefinition.getMaximumTechLevel());
		}
		if (randomDefinition.getProbabilityMultiplier() != null) {
			setProbabilityMultiplier(randomDefinition.getProbabilityMultiplier());
		}
		if (randomDefinition.getRestrictedFactions() != null && !randomDefinition.getRestrictedFactions().isEmpty()) {
			restrictedFactions.clear();
			restrictedFactions.addAll(randomDefinition.getRestrictedFactions());
		}
		if (randomDefinition.getRecommendedFactions() != null && !randomDefinition.getRecommendedFactions().isEmpty()) {
			recommendedFactions.clear();
			recommendedFactions.addAll(randomDefinition.getRecommendedFactions());
		}
		if (randomDefinition.getRecommendedRaces() != null && !randomDefinition.getRecommendedRaces().isEmpty()) {
			recommendedRaces.clear();
			recommendedRaces.addAll(randomDefinition.getRecommendedRaces());
		}
		if (randomDefinition.getForbiddenRaces() != null && !randomDefinition.getForbiddenRaces().isEmpty()) {
			forbiddenRaces.clear();
			forbiddenRaces.addAll(randomDefinition.getForbiddenRaces());
		}
		if (randomDefinition.getRestrictedRaces() != null && !randomDefinition.getRestrictedRaces().isEmpty()) {
			restrictedRaces.clear();
			restrictedRaces.addAll(randomDefinition.getRestrictedRaces());
		}
		if (randomDefinition.getRestrictedFactionGroups() != null && !randomDefinition.getRestrictedFactionGroups().isEmpty()) {
			restrictedFactionGroups.clear();
			restrictedFactionGroups.addAll(randomDefinition.getRestrictedFactionGroups());
		}
		if (randomDefinition.getRecommendedFactionsGroups() != null && !randomDefinition.getRecommendedFactionsGroups().isEmpty()) {
			recommendedFactionGroups.clear();
			recommendedFactionGroups.addAll(randomDefinition.getRecommendedFactionsGroups());
		}
		if (randomDefinition.getProbability() != null) {
			setProbability(randomDefinition.getProbability());
		}
	}

	public Integer getMinimumTechLevel() {
		if (minimumTechLevel == null) {
			return 0;
		}
		return minimumTechLevel;
	}

	public void setMinimumTechLevel(Integer minimumTechLevel) {
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

	public Set<FactionGroup> getRecommendedFactionsGroups() {
		return recommendedFactionGroups;
	}

	public void addRecommendedFaction(Faction faction) {
		if (faction != null) {
			recommendedFactions.add(faction);
		}
	}

	public void addRecommendedRaces(Race race) {
		if (race != null) {
			restrictedRaces.add(race);
		}
	}

	public RandomProbabilityDefinition getProbability() {
		return probability;
	}

	public void setProbability(RandomProbabilityDefinition probability) {
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

	public Integer getMaximumTechLevel() {
		if (maximumTechLevel == null) {
			return Characteristic.MAX_VALUE;
		}
		return maximumTechLevel;
	}

	public void setMaximumTechLevel(Integer maximumTechLevel) {
		this.maximumTechLevel = maximumTechLevel;
	}

	public Integer getStaticProbability() {
		return staticProbability;
	}

	public void setStaticProbability(Integer staticProbability) {
		this.staticProbability = staticProbability;
	}

	public Double getProbabilityMultiplier() {
		if (probabilityMultiplier == null) {
			return 1d;
		}
		return probabilityMultiplier;
	}

	public void setProbabilityMultiplier(Double probabilityMultiplier) {
		this.probabilityMultiplier = probabilityMultiplier;
	}

	public Set<Faction> getRestrictedFactions() {
		return restrictedFactions;
	}

	public void addRestrictedFactionGroup(FactionGroup restrictedFactionGroup) {
		if (restrictedFactionGroup != null) {
			restrictedFactionGroups.add(restrictedFactionGroup);
		}
	}

	public void addRestrictedRace(Race restrictedRace) {
		if (restrictedRace != null) {
			restrictedRaces.add(restrictedRace);
		}
	}

	public Set<Race> getRestrictedRaces() {
		return restrictedRaces;
	}

	public void addForbiddenRace(Race forbiddenRace) {
		if (forbiddenRace != null) {
			forbiddenRaces.add(forbiddenRace);
		}
	}

	public Set<Race> getForbiddenRaces() {
		return forbiddenRaces;
	}

	public Set<FactionGroup> getRestrictedFactionGroups() {
		return restrictedFactionGroups;
	}

	@Override
	public String toString() {
		return minimumTechLevel + "";
	}
}
