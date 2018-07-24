package com.softwaremagico.tm.random;

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

import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.FreeStyleCharacterCreation;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.character.skills.SkillDefinition;
import com.softwaremagico.tm.character.skills.SkillsDefinitionsFactory;
import com.softwaremagico.tm.log.MachineLog;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.IRandomPreferences;
import com.softwaremagico.tm.random.selectors.SkillGroupPreferences;
import com.softwaremagico.tm.random.selectors.SpecializationPreferences;
import com.softwaremagico.tm.random.selectors.TechnologicalPreferences;

public class RandomSkills extends RandomSelector<AvailableSkill> {
	private final static int NO_PROBABILITY = -1000;
	private final static int BAD_PROBABILITY = -20;
	private final static int LOW_PROBABILITY = -10;
	private final static int ACCEPTABLE_PROBABILITY = 11;
	private final static int GOOD_PROBABILITY = 21;
	private final static int MAX_PROBABILITY = 100;

	public RandomSkills(CharacterPlayer characterPlayer, Set<IRandomPreferences> preferences) throws InvalidXmlElementException {
		super(characterPlayer, preferences);
	}

	public void spendSkillsPoints() throws InvalidXmlElementException, InvalidRandomElementSelectedException {
		// Set minimum values of skills by preferences.
		assignMinimumValuesOfSkills();

		// Meanwhile are ranks to expend.
		while (getCharacterPlayer().getSkillsTotalPoints() < FreeStyleCharacterCreation.SKILLS_POINTS) {
			// Select a skill randomly.
			AvailableSkill selectedSkill = selectElementByWeight();

			// Assign random ranks to the skill.
			RandomSkillRanks randomSkillRanks = new RandomSkillRanks(getCharacterPlayer(), selectedSkill, getPreferences());
			randomSkillRanks.assignRandomRanks();

			// Remove skill from options to avoid adding more ranks.
			removeElementWeight(selectedSkill);
		}
	}

	private void assignMinimumValuesOfSkills() {
		for (IRandomPreferences preference : getPreferences()) {
			if (preference instanceof TechnologicalPreferences) {
				getCharacterPlayer().getCharacteristic(CharacteristicName.TECH).setValue(((TechnologicalPreferences) preference).minimum());
			}
		}
	}

	@Override
	protected TreeMap<Integer, AvailableSkill> assignElementsWeight() throws InvalidXmlElementException {
		TreeMap<Integer, AvailableSkill> weightedSkills = new TreeMap<>();
		int count = 1;

		for (SkillDefinition skillDefinition : SkillsDefinitionsFactory.getInstance().getElements(getCharacterPlayer().getLanguage())) {
			for (AvailableSkill skill : AvailableSkillsFactory.getInstance().getAvailableSkills(skillDefinition, getCharacterPlayer().getLanguage())) {
				int weight = getWeight(skill);
				if (weight > 0) {
					weightedSkills.put(count, skill);
					count += weight;
				}
			}
		}

		return weightedSkills;
	}

	@Override
	protected int getWeight(AvailableSkill skill) {
		int weight = 1;

		if (skill == null) {
			return NO_PROBABILITY;
		}

		int definitionWeight = weightForSkillDefinition(skill);
		MachineLog.debug(this.getClass().getName(), "Weight for '" + skill + "' by skill definition modification is '" + definitionWeight + "'.");
		weight += definitionWeight;

		int preferencesWeight = weightByPreferences(skill);
		MachineLog.debug(this.getClass().getName(), "Weight for '" + skill + "' by preferences modification is '" + preferencesWeight + "'.");
		weight += preferencesWeight;

		int raceWeight = weightForRace(skill);
		MachineLog.debug(this.getClass().getName(), "Weight for '" + skill + "' by race modification is '" + raceWeight + "'.");
		weight += raceWeight;

		int factionWeight = weightForFactions(skill);
		MachineLog.debug(this.getClass().getName(), "Weight for '" + skill + "' by faction modification is '" + factionWeight + "'.");
		weight += factionWeight;

		int nobilityWeight = weightForNobility(skill);
		MachineLog.debug(this.getClass().getName(), "Weight for '" + skill + "' by nobility modification is '" + nobilityWeight + "'.");
		weight += nobilityWeight;

		int technologyWeight = weightForTechnologyLimitations(skill);
		MachineLog.debug(this.getClass().getName(), "Weight for '" + skill + "' by technology modification is '" + technologyWeight + "'.");
		weight += technologyWeight;

		int specializationWeight = weightBySpecialization(skill);
		MachineLog.debug(this.getClass().getName(), "Weight for '" + skill + "' by specialization modification is '" + technologyWeight + "'.");
		weight += specializationWeight;

		return weight;
	}

	private int weightForSkillDefinition(AvailableSkill skill) {
		// Weapons only if technology is enough.
		switch (skill.getRandomDefinition().getProbability()) {
		case MINIMUM:
			return BAD_PROBABILITY;
		case LOW:
			return LOW_PROBABILITY;
		case FAIR:
			return 0;
		case GOOD:
			return ACCEPTABLE_PROBABILITY;
		}
		return 0;
	}

	private int weightForTechnologyLimitations(AvailableSkill skill) {
		// Weapons only if technology is enough.
		if (getCharacterPlayer().getCharacteristic(CharacteristicName.TECH).getValue() < skill.getRandomDefinition().getMinimumTechLevel()) {
			return NO_PROBABILITY;
		}
		// Ride is common in medieval age but not so common in modern age.
		if (skill.getId().equalsIgnoreCase("ride")) {
			if (getCharacterPlayer().getCharacteristic(CharacteristicName.TECH).getValue() > 4) {
				return LOW_PROBABILITY;
			}
		}
		return 0;
	}

	private int weightForFactions(AvailableSkill skill) {
		// No faction skills
		if (skill.getSkillDefinition().isLimitedToFaction()) {
			if (!skill.getSkillDefinition().getFactions().contains(getCharacterPlayer().getInfo().getFaction())) {
				return NO_PROBABILITY;
			} else if (getCharacterPlayer().getInfo().getFaction() != null
			// Recommended to my faction and only this faction can do it.
					&& skill.getRandomDefinition().getRecommendedFactions().contains(getCharacterPlayer().getInfo().getFaction())) {
				return MAX_PROBABILITY;
			}
		}
		// Recommended to my faction group.
		if (getCharacterPlayer().getInfo().getFaction() != null
				&& skill.getRandomDefinition().getRecommendedFactionGroups().contains(getCharacterPlayer().getInfo().getFaction().getFactionGroup())) {
			return ACCEPTABLE_PROBABILITY;
		}
		// Recommended to my faction.
		if (getCharacterPlayer().getInfo().getFaction() != null
				&& skill.getRandomDefinition().getRecommendedFactions().contains(getCharacterPlayer().getInfo().getFaction())) {
			return GOOD_PROBABILITY;
		}
		return 0;
	}

	private int weightForRace(AvailableSkill skill) {
		// Recommended to my faction group.
		if (getCharacterPlayer().getRace() != null && skill.getRandomDefinition().getRecommendedRaces().contains(getCharacterPlayer().getRace())) {
			return ACCEPTABLE_PROBABILITY;
		}
		return 0;
	}

	private int weightForNobility(AvailableSkill skill) {
		if (Objects.equals(getCharacterPlayer().getInfo().getFaction().getFactionGroup(), FactionGroup.NOBILITY)) {
			// beastcraft for nobility is not common in my point of view.
			if (skill.getId().equalsIgnoreCase("beastcraft")) {
				return NO_PROBABILITY;
			}
			// Ride is very common for nobility.
			if (skill.getId().equalsIgnoreCase("ride")) {
				return ACCEPTABLE_PROBABILITY;
			}
		}
		return 0;
	}

	private int weightByPreferences(AvailableSkill skill) {
		// Specialization by selection.
		if (getPreferences().contains(SkillGroupPreferences.getSkillGroupPreference(skill.getSkillDefinition().getSkillGroup().name()))) {
			int skillRanks = getCharacterPlayer().getSkillRanks(skill);

			// Good probability for values between the specialization.
			if (skillRanks < SkillGroupPreferences.getSkillGroupPreference(skill.getSkillDefinition().getSkillGroup().name()).minimum()) {
				return MAX_PROBABILITY;
			}
		}
		return 0;
	}

	private int weightBySpecialization(AvailableSkill skill) {
		SpecializationPreferences selectedSpecialization = SpecializationPreferences.getSelected(getPreferences());
		if (selectedSpecialization != null) {
			int skillRanks = getCharacterPlayer().getSkillRanks(skill);
			// No more that the maximum allowed.
			if (skillRanks > selectedSpecialization.maximum()) {
				return NO_PROBABILITY;
			}
			// If selected skill (has ranks), must have at least the minimum.
			if (getCharacterPlayer().isSkillTrained(skill) && skillRanks < selectedSpecialization.minimum()) {
				return MAX_PROBABILITY;
			}

			// Good probability for values between the specialization.
			if (skillRanks > selectedSpecialization.minimum()) {
				return GOOD_PROBABILITY;
			}
		}
		return 0;
	}
}
