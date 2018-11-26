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
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.Gender;
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.blessings.TooManyBlessingsException;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.creation.CostCalculator;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.character.equipment.armour.InvalidArmourException;
import com.softwaremagico.tm.character.equipment.shield.InvalidShieldException;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.InvalidSkillException;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.exceptions.DuplicatedPreferenceException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.profile.IRandomProfile;
import com.softwaremagico.tm.random.selectors.AgePreferences;
import com.softwaremagico.tm.random.selectors.ArmourPreferences;
import com.softwaremagico.tm.random.selectors.CombatPreferences;
import com.softwaremagico.tm.random.selectors.IGaussianDistribution;
import com.softwaremagico.tm.random.selectors.IRandomPreference;
import com.softwaremagico.tm.random.selectors.PsiqueLevelPreferences;
import com.softwaremagico.tm.random.selectors.ShieldPreferences;
import com.softwaremagico.tm.random.selectors.SpecializationPreferences;
import com.softwaremagico.tm.random.selectors.TraitCostPreferences;
import com.softwaremagico.tm.random.selectors.WeaponsPreferences;

public class RandomizeCharacter {
	private CharacterPlayer characterPlayer;
	private final Set<IRandomPreference> preferences;
	private final Random random = new Random();

	public RandomizeCharacter(CharacterPlayer characterPlayer, int experiencePoints, IRandomPreference... preferences) throws DuplicatedPreferenceException {
		this.characterPlayer = characterPlayer;
		this.preferences = new HashSet<>(Arrays.asList(preferences));

		checkValidPreferences();
	}

	public RandomizeCharacter(CharacterPlayer characterPlayer, IRandomProfile profile) throws DuplicatedPreferenceException, InvalidSkillException,
			TooManyBlessingsException {
		// Assign preferences
		this(characterPlayer, profile.getExperiencePoints(), profile.getPreferences().toArray(new IRandomPreference[profile.getPreferences().size()]));

		// Assign default values.
		if (profile.getCharacteristicsMinimumValues() != null) {
			for (Entry<CharacteristicName, Integer> characteristicValue : profile.getCharacteristicsMinimumValues().entrySet()) {
				characterPlayer.getCharacteristic(characteristicValue.getKey()).setValue(characteristicValue.getValue());
			}
		}

		if (profile.getSkillsMinimumValues() != null) {
			for (Entry<AvailableSkill, Integer> skillValue : profile.getSkillsMinimumValues().entrySet()) {
				characterPlayer.setSkillRank(skillValue.getKey(), skillValue.getValue());
			}
		}

		if (profile.getBlessings() != null) {
			for (Blessing blessing : profile.getBlessings()) {
				characterPlayer.addBlessing(blessing);
			}
		}
	}

	private void checkValidPreferences() throws DuplicatedPreferenceException {
		Set<Class<? extends IRandomPreference>> existingPreferences = new HashSet<>();
		// Only one of each class allowed.
		for (IRandomPreference preference : preferences) {
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

		// Weapons, armors and shield depending on combatPreferences if not
		// defined.
		WeaponsPreferences weaponPreferences = WeaponsPreferences.getSelected(preferences);
		if (weaponPreferences == null) {
			CombatPreferences combatePreferences = CombatPreferences.getSelected(preferences);
			preferences.add(combatePreferences.getDefaultWeaponPreferences());
		}
		ArmourPreferences armourPreferences = ArmourPreferences.getSelected(preferences);
		if (armourPreferences == null) {
			CombatPreferences combatePreferences = CombatPreferences.getSelected(preferences);
			preferences.add(combatePreferences.getDefaultArmourPreferences());
		}
		ShieldPreferences shieldPreferences = ShieldPreferences.getSelected(preferences);
		if (shieldPreferences == null) {
			CombatPreferences combatePreferences = CombatPreferences.getSelected(preferences);
			preferences.add(combatePreferences.getDefaultShieldPreferences());
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
		int remainingPoints = characterPlayer.getFreeStyleCharacterCreation().getFreeAvailablePoints() - CostCalculator.getCost(characterPlayer);

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
		// Set weapons.
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

		// Set armours
		ArmourPreferences armourPreferences = ArmourPreferences.getSelected(preferences);
		if (random.nextFloat() < armourPreferences.getArmourProbability()) {
			RandomArmour randomArmour = new RandomArmour(characterPlayer, preferences);
			try {
				randomArmour.assignArmour();
			} catch (InvalidArmourException e) {
				// Probably already has a shield.
				RandomGenerationLog.warning(this.getClass().getName(), e.getMessage());
			} catch (InvalidRandomElementSelectedException e) {
				RandomGenerationLog.warning(this.getClass().getName(), "No armours available for '" + characterPlayer + "'.");
			}

		}

		// Set shields.
		ShieldPreferences shieldPreferences = ShieldPreferences.getSelected(preferences);
		if (random.nextFloat() < shieldPreferences.getShieldProbability()) {
			RandomShield randomArmour = new RandomShield(characterPlayer, preferences);
			try {
				randomArmour.assignaShield();
			} catch (InvalidShieldException e) {
				// Probably already has an armour.
				RandomGenerationLog.warning(this.getClass().getName(), e.getMessage());
			} catch (InvalidRandomElementSelectedException e) {
				RandomGenerationLog.warning(this.getClass().getName(), "No armours available for '" + characterPlayer + "'.");
			}
		}
	}

	private void setExperiencePoints() {

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(characterPlayer.getNameRepresentation() + " (" + characterPlayer.getRace() + ") [" + characterPlayer.getFaction() + "]");
		sb.append(characterPlayer.getRandomDefinition().getSelectedCharacteristicsValues());
		sb.append(characterPlayer.getRandomDefinition().getDesiredSkillRanks());
		return sb.toString();
	}
}
