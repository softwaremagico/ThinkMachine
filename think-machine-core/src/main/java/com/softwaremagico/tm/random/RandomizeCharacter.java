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
import java.util.Random;
import java.util.Set;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.Gender;
import com.softwaremagico.tm.character.creation.CostCalculator;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.exceptions.DuplicatedPreferenceException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.AgePreferences;
import com.softwaremagico.tm.random.selectors.CombatPreferences;
import com.softwaremagico.tm.random.selectors.IGaussianDistribution;
import com.softwaremagico.tm.random.selectors.IRandomPreferences;
import com.softwaremagico.tm.random.selectors.PsiqueLevelPreferences;
import com.softwaremagico.tm.random.selectors.SpecializationPreferences;
import com.softwaremagico.tm.random.selectors.TraitCostPreferences;
import com.softwaremagico.tm.random.selectors.WeaponsPreferences;

public class RandomizeCharacter {
	private CharacterPlayer characterPlayer;
	private final Set<IRandomPreferences> preferences;
	private final Random random = new Random();

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
		setExtraPoints();
		setInitialEquipment();
		// Expend XP if any.
		setExperiencePoints();
	}

	private void setDefaultPreferences() {
		// Point distribution is "Fair" by default.
		SpecializationPreferences selectedSpecialization = SpecializationPreferences.getSelected(preferences);
		if (selectedSpecialization == null) {
			preferences.add(SpecializationPreferences.FAIR);
		}

		// Low traits by default.
		TraitCostPreferences traitCostPreferences = TraitCostPreferences.getSelected(preferences);
		if (traitCostPreferences == null) {
			preferences.add(TraitCostPreferences.LOW);
		}

		// Weapons depending on combatPreferences if not defined.
		WeaponsPreferences weaponPreferences = WeaponsPreferences.getSelected(preferences);
		if (weaponPreferences == null) {
			CombatPreferences combatePreferences = CombatPreferences.getSelected(preferences);
			preferences.add(combatePreferences.getDefaultWeaponPreferences());
		}
	}

	protected void setCharacterDefinition() throws InvalidXmlElementException, InvalidRandomElementSelectedException {
		// Check if race is set.
		if (characterPlayer.getRace() == null) {
			RandomRace randomRace = new RandomRace(characterPlayer, preferences);
			randomRace.assignRace();
		}

		if (characterPlayer.getInfo().getGender() == null) {
			characterPlayer.getInfo().setGender(Gender.randomGender());
		}

		if (characterPlayer.getInfo().getAge() == null) {
			IGaussianDistribution ageDistribution = AgePreferences.getSelected(preferences);
			characterPlayer.getInfo().setAge(ageDistribution.randomGaussian());
		}

		if (characterPlayer.getFaction() == null) {
			RandomFaction randomFaction = new RandomFaction(characterPlayer, preferences);
			randomFaction.assignFaction();
		}

		if (characterPlayer.getInfo().getPlanet() == null) {
			RandomPlanet randomPlanet = new RandomPlanet(characterPlayer, preferences);
			randomPlanet.assignPlanet();
		}

		if (characterPlayer.getInfo().getNames() == null || characterPlayer.getInfo().getNames().isEmpty()) {
			RandomName randomName = new RandomName(characterPlayer, preferences);
			randomName.assignName();
		}

		if (characterPlayer.getInfo().getSurname() == null) {
			RandomSurname randomSurname = new RandomSurname(characterPlayer, preferences);
			randomSurname.assignSurname();
		}
	}

	/**
	 * Using free style character generation. Only the first points to expend in
	 * a character.
	 * 
	 * @throws InvalidXmlElementException
	 * @throws InvalidRandomElementSelectedException
	 */
	private void setStartingValues() throws InvalidXmlElementException, InvalidRandomElementSelectedException {
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

	private void setExtraPoints() throws InvalidXmlElementException, InvalidRandomElementSelectedException {
		// Traits.
		// First, assign curses.
		RandomCursesDefinition randomCurses = new RandomCursesDefinition(characterPlayer, preferences);
		randomCurses.assignAvailableCurse();
		// Set blessings.
		RandomBlessingDefinition randomBlessing = new RandomBlessingDefinition(characterPlayer, preferences);
		randomBlessing.assignAvailableBlessings();
		// Set psique level
		RandomPsique randomPsique = new RandomPsique(characterPlayer, preferences);
		randomPsique.assignPsiqueLevel();
		// Set Wyrd
		IGaussianDistribution wyrdDistrubution = PsiqueLevelPreferences.getSelected(preferences);
		int extraWyrd = wyrdDistrubution.randomGaussian();
		characterPlayer.setExtraWyrd(extraWyrd - characterPlayer.getBasicWyrdValue());
		RandomGenerationLog.info(this.getClass().getName(), "Added extra wyrd '" + extraWyrd + "'.");
		// Set psi paths.
		RandomPsiquePath randomPsiquePath = new RandomPsiquePath(characterPlayer, preferences);
		randomPsiquePath.assignPsiquePaths();

		// Spend remaining points in skills and characteristics.
		int remainingPoints = FreeStyleCharacterCreation.FREE_AVAILABLE_POINTS - CostCalculator.getCost(characterPlayer);

		RandomGenerationLog.info(this.getClass().getName(), "Remaining points '" + remainingPoints + "'.");
		IGaussianDistribution specialization = SpecializationPreferences.getSelected(preferences);
		while (remainingPoints > 0) {
			// Characteristics only if is a little specialized.
			if (specialization.randomGaussian() > 4) {
				RandomCharacteristicsExtraPoints randomCharacteristicsExtraPoints = new RandomCharacteristicsExtraPoints(characterPlayer, preferences);
				remainingPoints -= randomCharacteristicsExtraPoints.spendCharacteristicsPoints(remainingPoints);
			}

			if (remainingPoints > 0) {
				RandomSkillExtraPoints randomSkillExtraPoints = new RandomSkillExtraPoints(characterPlayer, preferences);
				remainingPoints -= randomSkillExtraPoints.spendSkillsPoints(remainingPoints);
			}
		}
	}

	private void setInitialEquipment() throws InvalidXmlElementException {
		WeaponsPreferences weaponPreferences = WeaponsPreferences.getSelected(preferences);
		float probabilityOfRangedWeapon = weaponPreferences.getRangeWeaponProbability();
		float probabilityOfMeleeWeapon = weaponPreferences.getMeleeWeaponProbability();

		while (probabilityOfRangedWeapon > 0 || probabilityOfMeleeWeapon > 0) {
			if (probabilityOfRangedWeapon > 0 && random.nextFloat() < probabilityOfRangedWeapon) {
				RandomWeapon randomRangedWeapon = new RandomRangeWeapon(characterPlayer, preferences);
				try {
					randomRangedWeapon.assignWeapon();
				} catch (InvalidRandomElementSelectedException ires) {
					RandomGenerationLog.warning(this.getClass().getName(), "No ranged weapons available for '" + characterPlayer + "'.");
				}
			}
			probabilityOfRangedWeapon -= 0.3f;

			if (probabilityOfMeleeWeapon > 0 && random.nextFloat() < probabilityOfMeleeWeapon) {
				RandomWeapon randomMeleeWeapon = new RandomMeleeWeapon(characterPlayer, preferences);
				try {
					randomMeleeWeapon.assignWeapon();
				} catch (InvalidRandomElementSelectedException ires) {
					RandomGenerationLog.warning(this.getClass().getName(), "No melee weapons available for '" + characterPlayer + "'.");
				}
			}
			probabilityOfMeleeWeapon -= 0.4f;
		}
	}

	private void setExperiencePoints() {

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(characterPlayer.getNameRepresentation() + " (" + characterPlayer.getRace() + ") [" + characterPlayer.getFaction() + "]");
		sb.append(characterPlayer.getFreeStyleCharacterCreation().getSelectedCharacteristicsValues());
		sb.append(characterPlayer.getFreeStyleCharacterCreation().getDesiredSkillRanks());
		return sb.toString();
	}
}
