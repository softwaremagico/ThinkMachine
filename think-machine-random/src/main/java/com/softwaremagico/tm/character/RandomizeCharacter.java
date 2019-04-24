package com.softwaremagico.tm.character;

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
import com.softwaremagico.tm.character.benefices.BeneficeDefinition;
import com.softwaremagico.tm.character.benefices.RandomBeneficeDefinition;
import com.softwaremagico.tm.character.benefices.RandomExtraBeneficeDefinition;
import com.softwaremagico.tm.character.blessings.RandomBlessingDefinition;
import com.softwaremagico.tm.character.blessings.RandomCursesDefinition;
import com.softwaremagico.tm.character.blessings.TooManyBlessingsException;
import com.softwaremagico.tm.character.characteristics.RandomCharacteristics;
import com.softwaremagico.tm.character.characteristics.RandomCharacteristicsExtraPoints;
import com.softwaremagico.tm.character.creation.CostCalculator;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.character.cybernetics.RandomCybernetics;
import com.softwaremagico.tm.character.equipment.armours.InvalidArmourException;
import com.softwaremagico.tm.character.equipment.armours.RandomArmour;
import com.softwaremagico.tm.character.equipment.shields.InvalidShieldException;
import com.softwaremagico.tm.character.equipment.shields.RandomShield;
import com.softwaremagico.tm.character.equipment.weapons.RandomMeleeWeapon;
import com.softwaremagico.tm.character.equipment.weapons.RandomRangeWeapon;
import com.softwaremagico.tm.character.equipment.weapons.RandomWeapon;
import com.softwaremagico.tm.character.factions.RandomFaction;
import com.softwaremagico.tm.character.occultism.RandomPsique;
import com.softwaremagico.tm.character.occultism.RandomPsiquePath;
import com.softwaremagico.tm.character.races.RandomRace;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.RandomSkillExtraPoints;
import com.softwaremagico.tm.character.skills.RandomSkills;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.exceptions.DuplicatedPreferenceException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.profiles.IRandomProfile;
import com.softwaremagico.tm.random.profiles.ProfileMerger;
import com.softwaremagico.tm.random.selectors.AgePreferences;
import com.softwaremagico.tm.random.selectors.ArmourPreferences;
import com.softwaremagico.tm.random.selectors.CombatPreferences;
import com.softwaremagico.tm.random.selectors.DifficultLevelPreferences;
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
	private final Set<AvailableSkill> requiredSkills;
	private final Set<AvailableSkill> suggestedSkills;
	private final Set<BeneficeDefinition> mandatoryBenefices;
	private final Set<BeneficeDefinition> suggestedBenefices;
	private final Random random = new Random();
	private final int experiencePoints;

	public RandomizeCharacter(CharacterPlayer characterPlayer, int experiencePoints, IRandomPreference... preferences)
			throws DuplicatedPreferenceException, TooManyBlessingsException, InvalidXmlElementException {
		this(characterPlayer, experiencePoints, null, new HashSet<>(Arrays.asList(preferences)),
				new HashSet<AvailableSkill>(), new HashSet<AvailableSkill>(), new HashSet<BeneficeDefinition>(),
				new HashSet<BeneficeDefinition>());
	}

	public RandomizeCharacter(CharacterPlayer characterPlayer, IRandomProfile... profiles)
			throws DuplicatedPreferenceException, TooManyBlessingsException, InvalidXmlElementException {
		this(characterPlayer, null, new HashSet<IRandomProfile>(Arrays.asList(profiles)),
				new HashSet<IRandomPreference>(), new HashSet<AvailableSkill>(), new HashSet<AvailableSkill>(),
				new HashSet<BeneficeDefinition>(), new HashSet<BeneficeDefinition>());
	}

	public RandomizeCharacter(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences,
			IRandomProfile... profiles) throws DuplicatedPreferenceException, TooManyBlessingsException,
			InvalidXmlElementException {
		this(characterPlayer, null, new HashSet<IRandomProfile>(Arrays.asList(profiles)),
				new HashSet<IRandomPreference>(), new HashSet<AvailableSkill>(), new HashSet<AvailableSkill>(),
				new HashSet<BeneficeDefinition>(), new HashSet<BeneficeDefinition>());
	}

	public RandomizeCharacter(CharacterPlayer characterPlayer, Integer experiencePoints, Set<IRandomProfile> profiles,
			Set<IRandomPreference> preferences, Set<AvailableSkill> requiredSkills,
			Set<AvailableSkill> suggestedSkills, Set<BeneficeDefinition> mandatoryBenefices,
			Set<BeneficeDefinition> suggestedBenefices) throws DuplicatedPreferenceException,
			TooManyBlessingsException, InvalidXmlElementException {
		this.characterPlayer = characterPlayer;

		IRandomProfile finalProfile = ProfileMerger.merge(profiles, preferences, requiredSkills, suggestedSkills,
				mandatoryBenefices, suggestedBenefices, characterPlayer.getLanguage());

		// Assign preferences
		this.preferences = finalProfile.getPreferences();
		this.requiredSkills = finalProfile.getRequiredSkills();
		this.suggestedSkills = finalProfile.getSuggestedSkills();
		this.mandatoryBenefices = finalProfile.getMandatoryBenefices();
		this.suggestedBenefices = finalProfile.getSuggestedBenefices();

		checkValidPreferences();

		// Assign experience
		if (experiencePoints == null) {
			DifficultLevelPreferences difficultLevel = DifficultLevelPreferences.getSelected(this.preferences);
			this.experiencePoints = difficultLevel.getExperienceBonus();
		} else {
			this.experiencePoints = experiencePoints;
		}
	}

	private void checkValidPreferences() throws DuplicatedPreferenceException {
		Set<Class<? extends IRandomPreference>> existingPreferences = new HashSet<>();
		// Only one of each class allowed.
		for (IRandomPreference preference : preferences) {
			if (existingPreferences.contains(preference.getClass())) {
				throw new DuplicatedPreferenceException("Preference '" + preference
						+ "' collides with another preference. Only one of each type is allowed.");
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
			randomRace.assign();
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
			randomFaction.assign();
		}

		if (characterPlayer.getInfo().getPlanet() == null) {
			RandomPlanet randomPlanet = new RandomPlanet(characterPlayer, preferences);
			randomPlanet.assign();
		}

		if (characterPlayer.getInfo().getNames() == null || characterPlayer.getInfo().getNames().isEmpty()) {
			RandomName randomName = new RandomName(characterPlayer, preferences);
			randomName.assign();
		}

		if (characterPlayer.getInfo().getSurname() == null) {
			RandomSurname randomSurname = new RandomSurname(characterPlayer, preferences);
			randomSurname.assign();
		}
	}

	/**
	 * Using free style character generation. Only the first points to expend in a character.
	 * 
	 * @throws InvalidXmlElementException
	 * @throws InvalidRandomElementSelectedException
	 */
	private void setStartingValues() throws InvalidXmlElementException, InvalidRandomElementSelectedException {
		// Characteristics
		RandomCharacteristics randomCharacteristics = new RandomCharacteristics(characterPlayer, preferences);
		randomCharacteristics.assign();
		// Skills
		RandomSkills randomSkills = new RandomSkills(characterPlayer, preferences, requiredSkills, suggestedSkills);
		randomSkills.assign();
		// Traits
		RandomBeneficeDefinition randomBenefice = new RandomBeneficeDefinition(characterPlayer, preferences,
				mandatoryBenefices, suggestedBenefices);
		randomBenefice.assign();
	}

	private void setExtraPoints() throws InvalidXmlElementException, InvalidRandomElementSelectedException {
		// Traits.
		// First, assign curses.
		RandomCursesDefinition randomCurses = new RandomCursesDefinition(characterPlayer, preferences);
		randomCurses.assign();
		// Set blessings.
		RandomBlessingDefinition randomBlessing = new RandomBlessingDefinition(characterPlayer, preferences);
		randomBlessing.assign();
		// Set benefices.
		RandomBeneficeDefinition randomBenefice = new RandomExtraBeneficeDefinition(characterPlayer, preferences,
				suggestedBenefices);
		randomBenefice.assign();
		// Set psique level
		RandomPsique randomPsique = new RandomPsique(characterPlayer, preferences);
		randomPsique.assign();
		// Set cybernetics
		RandomCybernetics randomCybernetics = new RandomCybernetics(characterPlayer, preferences);
		randomCybernetics.assign();
		// Set Wyrd
		IGaussianDistribution wyrdDistrubution = PsiqueLevelPreferences.getSelected(preferences);
		int extraWyrd = wyrdDistrubution.randomGaussian();
		characterPlayer.setExtraWyrd(extraWyrd - characterPlayer.getBasicWyrdValue());
		RandomGenerationLog.info(this.getClass().getName(), "Added extra wyrd '" + extraWyrd + "'.");
		// Set psi paths.
		RandomPsiquePath randomPsiquePath = new RandomPsiquePath(characterPlayer, preferences);
		randomPsiquePath.assign();

		DifficultLevelPreferences difficultLevel = DifficultLevelPreferences.getSelected(preferences);

		// Spend remaining points in skills and characteristics.
		int remainingPoints = FreeStyleCharacterCreation.getFreeAvailablePoints(characterPlayer.getInfo().getAge())
				- CostCalculator.getCost(characterPlayer, difficultLevel.getSkillsBonus(),
						difficultLevel.getCharacteristicsBonus());

		RandomGenerationLog.info(this.getClass().getName(), "Remaining points '" + remainingPoints + "'.");
		IGaussianDistribution specialization = SpecializationPreferences.getSelected(preferences);

		if (remainingPoints > 0) {
			RandomCharacteristicsExtraPoints randomCharacteristicsExtraPoints = new RandomCharacteristicsExtraPoints(
					characterPlayer, preferences);
			RandomSkillExtraPoints randomSkillExtraPoints = new RandomSkillExtraPoints(characterPlayer, preferences,
					suggestedSkills);
			while (remainingPoints > 0) {
				// Characteristics only if is a little specialized.
				if (remainingPoints >= CostCalculator.CHARACTERISTIC_EXTRA_POINTS_COST
						&& specialization.randomGaussian() > 5) {
					remainingPoints -= randomCharacteristicsExtraPoints.spendCharacteristicsPoints(remainingPoints);
				}

				if (remainingPoints > 0) {
					remainingPoints -= randomSkillExtraPoints.spendSkillsPoints(remainingPoints);
				}
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
					randomRangedWeapon.assign();
				} catch (InvalidRandomElementSelectedException ires) {
					RandomGenerationLog.warning(this.getClass().getName(), "No ranged weapons available for '"
							+ characterPlayer + "'.");
				}
			}
			probabilityOfRangedWeapon -= 0.3f;

			if (probabilityOfMeleeWeapon > 0 && random.nextFloat() < probabilityOfMeleeWeapon) {
				RandomWeapon randomMeleeWeapon = new RandomMeleeWeapon(characterPlayer, preferences);
				try {
					randomMeleeWeapon.assign();
				} catch (InvalidRandomElementSelectedException ires) {
					RandomGenerationLog.warning(this.getClass().getName(), "No melee weapons available for '"
							+ characterPlayer + "'.");
				}
			}
			probabilityOfMeleeWeapon -= 0.4f;
		}

		// Set shields.
		ShieldPreferences shieldPreferences = ShieldPreferences.getSelected(preferences);
		if (random.nextFloat() < shieldPreferences.getShieldProbability()) {
			RandomShield randomShield = new RandomShield(characterPlayer, preferences);
			try {
				randomShield.assign();
			} catch (InvalidShieldException e) {
				// Probably already has an armour.
				RandomGenerationLog.warning(this.getClass().getName(), e.getMessage());
			} catch (InvalidRandomElementSelectedException e) {
				RandomGenerationLog.warning(this.getClass().getName(), "No shields available for '" + characterPlayer
						+ "'.");
			}
		}

		// Set armours
		ArmourPreferences armourPreferences = ArmourPreferences.getSelected(preferences);
		if (random.nextFloat() < armourPreferences.getArmourProbability()) {
			RandomArmour randomArmour = new RandomArmour(characterPlayer, preferences);
			try {
				randomArmour.assign();
			} catch (InvalidArmourException e) {
				// Probably already has a shield.
				RandomGenerationLog.warning(this.getClass().getName(), e.getMessage());
			} catch (InvalidRandomElementSelectedException e) {
				RandomGenerationLog.warning(this.getClass().getName(), "No armours available for '" + characterPlayer
						+ "'.");
			}

		}
	}

	private void setExperiencePoints() {

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(characterPlayer.getNameRepresentation() + " (" + characterPlayer.getRace() + ") ["
				+ characterPlayer.getFaction() + "]");
		return sb.toString();
	}
}
