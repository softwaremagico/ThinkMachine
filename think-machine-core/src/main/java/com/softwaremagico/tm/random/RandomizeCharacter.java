package com.softwaremagico.tm.random;

/*-
 * #%L
 * Think Machine (Core)
 * %%
 * Copyright (C) 2017 Softwaremagico
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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.random.exceptions.DuplicatedPreferenceException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.IRandomPreferences;
import com.softwaremagico.tm.random.selectors.SpecializationPreferences;
import com.softwaremagico.tm.random.selectors.TraitCostPreferences;

public class RandomizeCharacter {
	private CharacterPlayer characterPlayer;
	private final Set<IRandomPreferences> preferences;

	public RandomizeCharacter(CharacterPlayer characterPlayer, int experiencePoints, IRandomPreferences... preferences) throws DuplicatedPreferenceException {
		this.characterPlayer = characterPlayer;
		this.preferences = new HashSet<>(Arrays.asList(preferences));

		checkValidPreferences();
	}

	private void checkValidPreferences() throws DuplicatedPreferenceException {
		Set<Class<? extends IRandomPreferences>> existingPreferences = new HashSet<>();
		// Only one of each class allowed.
		for (IRandomPreferences preference : preferences) {
			if (existingPreferences.contains(preference.getClass())) {
				throw new DuplicatedPreferenceException("Preference '" + preference + "' collides with another preference. Only one of each type is allowed.");
			}
			existingPreferences.add(preference.getClass());
		}
	}

	public void createCharacter() throws InvalidXmlElementException, InvalidRandomElementSelectedException {
		setDefaultPreferences();
		setCharacterDefinition();
		setStartingValues();
		// Expend XP if any.
		spendExperiencePoints();
	}

	private void setDefaultPreferences() {
		// Point distribution is "Fair" by default.
		SpecializationPreferences selectedSpecialization = SpecializationPreferences.getSelected(preferences);
		if (selectedSpecialization == null) {
			selectedSpecialization = SpecializationPreferences.FAIR;
			preferences.add(selectedSpecialization);
		}

		// Low traits by default.
		TraitCostPreferences traitCostPreferences = TraitCostPreferences.getSelected(preferences);
		if (traitCostPreferences == null) {
			traitCostPreferences = TraitCostPreferences.LOW;
			preferences.add(traitCostPreferences);
		}
	}

	protected void setCharacterDefinition() throws InvalidXmlElementException, InvalidRandomElementSelectedException {
		// Check if race is set.
		if (characterPlayer.getRace() == null) {
			RandomRace randomRace = new RandomRace(characterPlayer, preferences);
			randomRace.assignRace();
		}

		if (characterPlayer.getFaction() == null) {
			RandomFaction randomFaction = new RandomFaction(characterPlayer, preferences);
			randomFaction.assignFaction();
		}
	}

	/**
	 * Using free style character generation.
	 * 
	 * @throws InvalidXmlElementException
	 * @throws InvalidRandomElementSelectedException
	 */
	protected void setStartingValues() throws InvalidXmlElementException, InvalidRandomElementSelectedException {
		characterPlayer.setFreeStyleCharacterCreation(new FreeStyleCharacterCreation());
		// Characteristics
		RandomCharacteristics randomCharacteristics = new RandomCharacteristics(characterPlayer, preferences);
		randomCharacteristics.spendCharacteristicsPoints();
		// Skills
		RandomSkills randomSkills = new RandomSkills(characterPlayer, preferences);
		randomSkills.spendSkillsPoints();
		// Traits
		RandomBeneficeDefinition randomBenefice = new RandomBeneficeDefinition(characterPlayer, preferences);
		randomBenefice.assignAvailableBenefices();
	}

	private void spendExperiencePoints() {

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(characterPlayer.getInfo().getName() + " (" + characterPlayer.getRace() + ") [" + characterPlayer.getFaction() + "]");
		sb.append(characterPlayer.getFreeStyleCharacterCreation().getSelectedCharacteristicsValues());
		sb.append(characterPlayer.getFreeStyleCharacterCreation().getDesiredSkillRanks());
		return sb.toString();
	}
}
