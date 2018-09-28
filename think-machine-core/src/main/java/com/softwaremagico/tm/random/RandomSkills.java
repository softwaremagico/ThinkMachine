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
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.characteristics.CharacteristicType;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.character.skills.SkillDefinition;
import com.softwaremagico.tm.character.skills.SkillGroup;
import com.softwaremagico.tm.character.skills.SkillsDefinitionsFactory;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.CombatPreferences;
import com.softwaremagico.tm.random.selectors.IRandomPreferences;
import com.softwaremagico.tm.random.selectors.SkillGroupPreferences;
import com.softwaremagico.tm.random.selectors.SpecializationPreferences;
import com.softwaremagico.tm.random.selectors.TechnologicalPreferences;

public class RandomSkills extends RandomSelector<AvailableSkill> {
	private final static int NO_PROBABILITY = -1000;
	private final static int BAD_PROBABILITY = -20;
	private final static int DIFFICULT_PROBABILITY = -10;
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
			assignRandomRanks(selectedSkill);

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
		if (skill.getSkillDefinition().isNatural()) {
			RandomGenerationLog.debug(this.getClass().getName(), "Weight for '" + skill + "' as natural skill is increased.");
			weight += 3;
		}

		int characteristicsWeight = weightByCharacteristics(skill);
		RandomGenerationLog.debug(this.getClass().getName(), "Weight for '" + skill + "' by characteristics modification is '" + characteristicsWeight + "'.");
		weight += characteristicsWeight;

		int definitionWeight = weightBySkillDefinition(skill);
		RandomGenerationLog.debug(this.getClass().getName(), "Weight for '" + skill + "' by skill definition modification is '" + definitionWeight + "'.");
		weight += definitionWeight;

		int preferencesWeight = weightByPreferences(skill);
		RandomGenerationLog.debug(this.getClass().getName(), "Weight for '" + skill + "' by preferences modification is '" + preferencesWeight + "'.");
		weight += preferencesWeight;

		int raceWeight = weightByRace(skill);
		RandomGenerationLog.debug(this.getClass().getName(), "Weight for '" + skill + "' by race modification is '" + raceWeight + "'.");
		weight += raceWeight;

		int factionWeight = weightByFactions(skill);
		RandomGenerationLog.debug(this.getClass().getName(), "Weight for '" + skill + "' by faction modification is '" + factionWeight + "'.");
		weight += factionWeight;

		int nobilityWeight = weightByNobility(skill);
		RandomGenerationLog.debug(this.getClass().getName(), "Weight for '" + skill + "' by nobility modification is '" + nobilityWeight + "'.");
		weight += nobilityWeight;

		int technologyWeight = weightByTechnologyLimitations(skill);
		RandomGenerationLog.debug(this.getClass().getName(), "Weight for '" + skill + "' by technology modification is '" + technologyWeight + "'.");
		weight += technologyWeight;

		int specializationWeight = weightBySpecializationPreferences(skill);
		RandomGenerationLog.debug(this.getClass().getName(), "Weight for '" + skill + "' by specialization modification is '" + technologyWeight + "'.");
		weight += specializationWeight;

		int psiqueWeight = weightByPsique(skill);
		RandomGenerationLog.debug(this.getClass().getName(), "Weight for '" + skill + "' by psique modification is '" + psiqueWeight + "'.");
		weight += psiqueWeight;

		int combatWeight = weightByCombat(skill);
		RandomGenerationLog.debug(this.getClass().getName(), "Weight for '" + skill + "' by combat definitions is '" + combatWeight + "'.");
		weight += combatWeight;

		int specializationMultiplier = weightBySpecializationSize(skill);
		RandomGenerationLog.debug(this.getClass().getName(), "Specialization multiplier for '" + skill + "' is '" + specializationMultiplier + "'.");
		return weight * specializationMultiplier;
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
				return ACCEPTABLE_PROBABILITY;
			}
			if (Objects.equals(skill.getSkillDefinition().getSkillGroup().getPreferredCharacteristicsGroups(), getPreferredCharacteristicsTypeSorted().get(1))) {
				return LITTLE_PROBABILITY;
			}
		}
		return 0;
	}

	/**
	 * Skills with lots of specializations has more chance to have at least one
	 * of the specializations selected. This methods reduces its probability.
	 * 
	 * @param skill
	 *            skill to check.
	 * @return multiplier for the other bonus.
	 */
	private int weightBySpecializationSize(AvailableSkill skill) {
		// Skills with lots of specializations has more probability to get one
		// of them that other skills. Reduce this probability.
		if (skill.getSkillDefinition().getSpecializations() == null || skill.getSkillDefinition().getSpecializations().isEmpty()) {
			return AvailableSkillsFactory.getInstance().getMaximumNumberOfSpecializations();
		}
		return AvailableSkillsFactory.getInstance().getMaximumNumberOfSpecializations() / skill.getSkillDefinition().getSpecializations().size();
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
			int skillRanks = getCharacterPlayer().getSkillTotalRanks(skill);

			// Good probability for values between the specialization.
			if (skillRanks < SkillGroupPreferences.getSkillGroupPreference(skill.getSkillDefinition().getSkillGroup().name()).minimum()) {
				return MAX_PROBABILITY;
			}
		}
		return 0;
	}

	private int weightBySpecializationPreferences(AvailableSkill skill) {
		SpecializationPreferences selectedSpecialization = SpecializationPreferences.getSelected(getPreferences());
		int skillRanks = getCharacterPlayer().getSkillTotalRanks(skill);
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
		return 0;
	}

	private int weightByPsique(AvailableSkill skill) {
		if (getCharacterPlayer().getPsiqueLevel(OccultismTypeFactory.getPsi(getCharacterPlayer().getLanguage())) > 0) {
			// Self control useful for psique.
			if (skill.getId().equals("selfControl")) {
				return MAX_PROBABILITY;
			}
		}
		if (getCharacterPlayer().getPsiqueLevel(OccultismTypeFactory.getTheurgy(getCharacterPlayer().getLanguage())) > 0) {
			if (skill.getId().equals("influence")) {
				return MAX_PROBABILITY;
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

	private int assignRandomRanks(AvailableSkill availableSkill) throws InvalidXmlElementException, InvalidRandomElementSelectedException {
		int finalRanks = getRankValue(availableSkill);
		// Final ranks cannot be greater that the total points remaining.
		if (getCharacterPlayer().getSkillsTotalPoints() + (finalRanks - getCharacterPlayer().getSkillAssignedRanks(availableSkill)) > FreeStyleCharacterCreation.SKILLS_POINTS) {
			finalRanks = FreeStyleCharacterCreation.SKILLS_POINTS - getCharacterPlayer().getSkillsTotalPoints()
					+ getCharacterPlayer().getSkillAssignedRanks(availableSkill);
		}
		if (finalRanks < 0) {
			finalRanks = 0;
		}
		// Only if adding more ranks.
		if (finalRanks < getCharacterPlayer().getSkillAssignedRanks(availableSkill)) {
			return 0;
		}
		getCharacterPlayer().setSkillRank(availableSkill, finalRanks);
		getCharacterPlayer().setDesiredSkillRanks(availableSkill, finalRanks);
		return finalRanks;
	}

	protected int getRankValue(AvailableSkill availableSkill) throws InvalidXmlElementException, InvalidRandomElementSelectedException {
		int skillValue = 0;
		SpecializationPreferences selectedSpecialization = SpecializationPreferences.getSelected(getPreferences());
		int minimumValue = selectedSpecialization.minimum();
		// Natural skills always a minimum value of 3.
		if (availableSkill.getSkillDefinition().isNatural() && minimumValue < SkillDefinition.NATURAL_SKILLS_MINIMUM_VALUE) {
			minimumValue = SkillDefinition.NATURAL_SKILLS_MINIMUM_VALUE;
		}
		// Gaussian distribution.
		skillValue = selectedSpecialization.randomGaussian();
		if (skillValue < minimumValue) {
			skillValue = minimumValue;
		}
		return skillValue;
	}

	/**
	 * Combat skills must be interesting for fighters.
	 * 
	 * @param availableSkill
	 * @return
	 */
	private int weightByCombat(AvailableSkill availableSkill) {
		CombatPreferences combatPreferences = CombatPreferences.getSelected(getPreferences());
		// Set some attack skills
		if (combatPreferences.minimum() >= CombatPreferences.FAIR.minimum()) {
			if (getCharacterPlayer().getCharacteristic(CharacteristicName.TECH).getValue() >= 5) {
				if (Objects.equals(availableSkill.getId(), "energyGuns")) {
					return GOOD_PROBABILITY * combatPreferences.maximum();
				}
			} else if (getCharacterPlayer().getCharacteristic(CharacteristicName.TECH).getValue() >= 3) {
				if (Objects.equals(availableSkill.getId(), "slugGuns")) {
					return GOOD_PROBABILITY * combatPreferences.maximum();
				}
			} else {
				if (Objects.equals(availableSkill.getId(), "archery")) {
					return ACCEPTABLE_PROBABILITY * combatPreferences.maximum();
				}
			}
			if (Objects.equals(availableSkill.getId(), "fight")) {
				if (getCharacterPlayer().getCharacteristic(CharacteristicName.TECH).getValue() >= 3) {
					return ACCEPTABLE_PROBABILITY * combatPreferences.maximum();
				} else {
					return GOOD_PROBABILITY * combatPreferences.maximum();
				}
			}
		}
		return 0;
	}
}
