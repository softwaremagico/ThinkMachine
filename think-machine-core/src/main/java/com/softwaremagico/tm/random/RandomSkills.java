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

import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.FreeStyleCharacterCreation;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.characteristics.CharacteristicType;
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.character.skills.SkillDefinition;
import com.softwaremagico.tm.character.skills.SkillGroup;
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
	private final static int DIFFICULT_PROBABILITY = -10;
	private final static int SUBTLE_PROBABILITY = 1;
	private final static int LITTLE_PROBABILITY = 6;
	private final static int ACCEPTABLE_PROBABILITY = 11;
	private final static int GOOD_PROBABILITY = 21;
	private final static int MAX_PROBABILITY = 100;

	private final static int SKILL_TECH_DIFFERENCE_TO_BE_PRIMITIVE = 3;

	private List<Entry<CharacteristicType, Integer>> preferredCharacteristicsTypeSorted;

	public RandomSkills(CharacterPlayer characterPlayer, Set<IRandomPreferences> preferences) throws InvalidXmlElementException {
		super(characterPlayer, preferences);
	}

	public void spendSkillsPoints() throws InvalidXmlElementException, InvalidRandomElementSelectedException {
		// Set minimum values of skills by preferences.
		assignMinimumValuesOfSkills();

		// Merge some skills that are similar.
		removeUndesiredSkills();

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

	private void removeUndesiredSkills() throws InvalidXmlElementException {
		// Remove combat skills too primitives.
		for (AvailableSkill availableSkill : AvailableSkillsFactory.getInstance().getSkillsByGroup(SkillGroup.COMBAT, getCharacterPlayer().getLanguage())) {
			if (availableSkill.getRandomDefinition().getMinimumTechLevel() <= getCharacterPlayer().getCharacteristic(CharacteristicName.TECH).getValue()
					- SKILL_TECH_DIFFERENCE_TO_BE_PRIMITIVE) {
				removeElementWeight(availableSkill);
			}
		}
	}

	public void mergeSkills(AvailableSkill availableSkill, SkillGroup skillGroup) throws InvalidXmlElementException {
		int weight = getWeight(availableSkill);
		while (weight > 0) {
			for (AvailableSkill selectedSkill : AvailableSkillsFactory.getInstance().getSkillsByGroup(skillGroup, getCharacterPlayer().getLanguage())) {
				if (weight > 0 && !Objects.equals(availableSkill, selectedSkill)) {
					updateWeight(selectedSkill, getWeight(selectedSkill) + 1);
					weight--;
				}
			}
		}
		removeElementWeight(availableSkill);
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

		int characteristicsWeight = weightByCharacteristics(skill);
		MachineLog.debug(this.getClass().getName(), "Weight for '" + skill + "' by characteristics modification is '" + characteristicsWeight + "'.");
		weight += characteristicsWeight;

		int definitionWeight = weightBySkillDefinition(skill);
		MachineLog.debug(this.getClass().getName(), "Weight for '" + skill + "' by skill definition modification is '" + definitionWeight + "'.");
		weight += definitionWeight;

		int preferencesWeight = weightByPreferences(skill);
		MachineLog.debug(this.getClass().getName(), "Weight for '" + skill + "' by preferences modification is '" + preferencesWeight + "'.");
		weight += preferencesWeight;

		int raceWeight = weightByRace(skill);
		MachineLog.debug(this.getClass().getName(), "Weight for '" + skill + "' by race modification is '" + raceWeight + "'.");
		weight += raceWeight;

		int factionWeight = weightByFactions(skill);
		MachineLog.debug(this.getClass().getName(), "Weight for '" + skill + "' by faction modification is '" + factionWeight + "'.");
		weight += factionWeight;

		int nobilityWeight = weightByNobility(skill);
		MachineLog.debug(this.getClass().getName(), "Weight for '" + skill + "' by nobility modification is '" + nobilityWeight + "'.");
		weight += nobilityWeight;

		int technologyWeight = weightByTechnologyLimitations(skill);
		MachineLog.debug(this.getClass().getName(), "Weight for '" + skill + "' by technology modification is '" + technologyWeight + "'.");
		weight += technologyWeight;

		int specializationWeight = weightBySpecialization(skill);
		MachineLog.debug(this.getClass().getName(), "Weight for '" + skill + "' by specialization modification is '" + technologyWeight + "'.");
		weight += specializationWeight;

		return weight;
	}

	private int weightBySkillDefinition(AvailableSkill skill) {
		// Weapons only if technology is enough.
		switch (skill.getRandomDefinition().getProbability()) {
		case MINIMUM:
			return BAD_PROBABILITY;
		case LOW:
			return DIFFICULT_PROBABILITY;
		case FAIR:
			return 0;
		case GOOD:
			return ACCEPTABLE_PROBABILITY;
		}
		return 0;
	}

	private int weightByCharacteristics(AvailableSkill skill) {
		if (skill.getSkillDefinition().getSkillGroup().getPreferredCharacteristicsGroups() != null && !getPreferredCharacteristicsTypeSorted().isEmpty()) {
			if (Objects.equals(skill.getSkillDefinition().getSkillGroup().getPreferredCharacteristicsGroups(), getPreferredCharacteristicsTypeSorted().get(0))) {
				return LITTLE_PROBABILITY;
			}
			if (Objects.equals(skill.getSkillDefinition().getSkillGroup().getPreferredCharacteristicsGroups(), getPreferredCharacteristicsTypeSorted().get(1))) {
				return SUBTLE_PROBABILITY;
			}
		}
		return 0;
	}

	private int weightByTechnologyLimitations(AvailableSkill skill) {
		// Weapons only if technology is enough.
		if (getCharacterPlayer().getCharacteristic(CharacteristicName.TECH).getValue() < skill.getRandomDefinition().getMinimumTechLevel()) {
			return NO_PROBABILITY;
		}
		// Ride is common in medieval age but not so common in modern age.
		if (skill.getId().equalsIgnoreCase("ride")) {
			if (getCharacterPlayer().getCharacteristic(CharacteristicName.TECH).getValue() > 4) {
				return DIFFICULT_PROBABILITY;
			}
		}
		return 0;
	}

	private int weightByFactions(AvailableSkill skill) {
		// No faction skills
		if (skill.getSkillDefinition().isLimitedToFaction()) {
			if (!skill.getSkillDefinition().getFactions().contains(getCharacterPlayer().getFaction())) {
				return NO_PROBABILITY;
			} else if (getCharacterPlayer().getFaction() != null
			// Recommended to my faction and only this faction can do it.
					&& skill.getRandomDefinition().getRecommendedFactions().contains(getCharacterPlayer().getFaction())) {
				return MAX_PROBABILITY;
			}
		}
		// Recommended to my faction group.
		if (getCharacterPlayer().getFaction() != null
				&& skill.getRandomDefinition().getRecommendedFactionGroups().contains(getCharacterPlayer().getFaction().getFactionGroup())) {
			return ACCEPTABLE_PROBABILITY;
		}
		// Recommended to my faction.
		if (getCharacterPlayer().getFaction() != null && skill.getRandomDefinition().getRecommendedFactions().contains(getCharacterPlayer().getFaction())) {
			return GOOD_PROBABILITY;
		}
		return 0;
	}

	private int weightByRace(AvailableSkill skill) {
		// Recommended to my faction group.
		if (getCharacterPlayer().getRace() != null && skill.getRandomDefinition().getRecommendedRaces().contains(getCharacterPlayer().getRace())) {
			return ACCEPTABLE_PROBABILITY;
		}
		return 0;
	}

	private int weightByNobility(AvailableSkill skill) {
		if (Objects.equals(getCharacterPlayer().getFaction().getFactionGroup(), FactionGroup.NOBILITY)) {
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

	public List<Entry<CharacteristicType, Integer>> getPreferredCharacteristicsTypeSorted() {
		if (preferredCharacteristicsTypeSorted == null) {
			preferredCharacteristicsTypeSorted = getCharacterPlayer().getPreferredCharacteristicsTypeSorted();
		}
		return preferredCharacteristicsTypeSorted;
	}
}
