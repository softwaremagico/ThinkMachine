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

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.benefices.BeneficeDefinition;
import com.softwaremagico.tm.character.benefices.RandomBeneficeDefinition;
import com.softwaremagico.tm.character.benefices.RandomExtraBeneficeDefinition;
import com.softwaremagico.tm.character.blessings.RandomBlessingDefinition;
import com.softwaremagico.tm.character.blessings.RandomCursesDefinition;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.characteristics.RandomCharacteristics;
import com.softwaremagico.tm.character.characteristics.RandomCharacteristicsExperience;
import com.softwaremagico.tm.character.characteristics.RandomCharacteristicsExtraPoints;
import com.softwaremagico.tm.character.creation.CostCalculator;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.character.cybernetics.RandomCybernetics;
import com.softwaremagico.tm.character.equipment.armours.Armour;
import com.softwaremagico.tm.character.equipment.armours.InvalidArmourException;
import com.softwaremagico.tm.character.equipment.armours.RandomArmour;
import com.softwaremagico.tm.character.equipment.shields.InvalidShieldException;
import com.softwaremagico.tm.character.equipment.shields.RandomShield;
import com.softwaremagico.tm.character.equipment.shields.Shield;
import com.softwaremagico.tm.character.equipment.weapons.RandomMeleeWeapon;
import com.softwaremagico.tm.character.equipment.weapons.RandomRangeWeapon;
import com.softwaremagico.tm.character.equipment.weapons.RandomWeapon;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.factions.RandomFaction;
import com.softwaremagico.tm.character.occultism.RandomPsique;
import com.softwaremagico.tm.character.occultism.RandomPsiquePath;
import com.softwaremagico.tm.character.races.RandomRace;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.RandomSkillExperience;
import com.softwaremagico.tm.character.skills.RandomSkillExtraPoints;
import com.softwaremagico.tm.character.skills.RandomSkills;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.predefined.IRandomPredefined;
import com.softwaremagico.tm.random.predefined.PredefinedMerger;
import com.softwaremagico.tm.random.selectors.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class RandomizeCharacter {
    private final CharacterPlayer characterPlayer;
    private final Set<IRandomPreference> preferences;
    private final Set<AvailableSkill> requiredSkills;
    private final Set<AvailableSkill> suggestedSkills;
    private final Set<BeneficeDefinition> mandatoryBenefices;
    private final Set<BeneficeDefinition> suggestedBenefices;
    private final Set<Characteristic> characteristicsMinimumValues;
    private final Set<Weapon> mandatoryWeapons;
    private final Set<Armour> mandatoryArmours;
    private final Set<Shield> mandatoryShields;

    public RandomizeCharacter(CharacterPlayer characterPlayer, int experiencePoints, IRandomPreference... preferences) {
        this(characterPlayer, experiencePoints, null, new HashSet<>(Arrays.asList(preferences)), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
    }

    public RandomizeCharacter(CharacterPlayer characterPlayer, IRandomPredefined... profiles) {
        this(characterPlayer, null, new HashSet<>(Arrays.asList(profiles)), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>());
    }

    public RandomizeCharacter(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences, IRandomPredefined... profiles) {
        this(characterPlayer, null, new HashSet<>(Arrays.asList(profiles)), preferences, new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>());
    }

    public RandomizeCharacter(CharacterPlayer characterPlayer, Integer experiencePoints, Set<IRandomPredefined> profiles, Set<IRandomPreference> preferences,
                              Set<AvailableSkill> requiredSkills, Set<AvailableSkill> suggestedSkills, Set<BeneficeDefinition> mandatoryBenefices,
                              Set<BeneficeDefinition> suggestedBenefices, Set<Weapon> mandatoryWeapons, Set<Armour> mandatoryArmours,
                              Set<Shield> mandatoryShields) {
        this.characterPlayer = characterPlayer;

        final IRandomPredefined finalProfile = PredefinedMerger.merge(profiles, preferences, requiredSkills, suggestedSkills,
                mandatoryBenefices, suggestedBenefices, mandatoryWeapons, mandatoryArmours, mandatoryShields, characterPlayer.getLanguage(),
                characterPlayer.getModuleName());

        // Assign preferences
        this.preferences = finalProfile.getPreferences();
        this.requiredSkills = finalProfile.getRequiredSkills();
        this.suggestedSkills = finalProfile.getSuggestedSkills();
        this.mandatoryBenefices = finalProfile.getMandatoryBenefices();
        this.suggestedBenefices = finalProfile.getSuggestedBenefices();
        this.characteristicsMinimumValues = finalProfile.getCharacteristicsMinimumValues();
        this.mandatoryWeapons = finalProfile.getMandatoryWeapons();
        this.mandatoryArmours = finalProfile.getMandatoryArmours();
        this.mandatoryShields = finalProfile.getMandatoryShields();

        // Assign experience
        if (experiencePoints == null) {
            final DifficultLevelPreferences difficultLevel = DifficultLevelPreferences.getSelected(this.preferences);
            this.characterPlayer.setExperienceEarned(difficultLevel.getExperienceBonus());
        } else {
            this.characterPlayer.setExperienceEarned(experiencePoints);
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
        final AgePreferences agePreferences = AgePreferences.getSelected(preferences);
        if (agePreferences == null) {
            preferences.add(AgePreferences.getDefaultOption());
        }

        // Point distribution is "Fair" by default.
        final SpecializationPreferences selectedSpecialization = SpecializationPreferences.getSelected(preferences);
        if (selectedSpecialization == null) {
            preferences.add(SpecializationPreferences.getDefaultOption());
        }

        // Low traits by default.
        final TraitCostPreferences traitCostPreferences = TraitCostPreferences.getSelected(preferences);
        if (traitCostPreferences == null) {
            preferences.add(TraitCostPreferences.getDefaultOption());
        }

        // Weapons, armors and shield depending on combatPreferences if not
        // defined.
        final WeaponsPreferences weaponPreferences = WeaponsPreferences.getSelected(preferences);
        if (weaponPreferences == null) {
            final CombatPreferences combatPreferences = CombatPreferences.getSelected(preferences);
            preferences.add(combatPreferences.getDefaultWeaponPreferences());
        }
        final ArmourPreferences armourPreferences = ArmourPreferences.getSelected(preferences);
        if (armourPreferences == null) {
            final CombatPreferences combatPreferences = CombatPreferences.getSelected(preferences);
            preferences.add(combatPreferences.getDefaultArmourPreferences());
        }
        final ShieldPreferences shieldPreferences = ShieldPreferences.getSelected(preferences);
        if (shieldPreferences == null) {
            final CombatPreferences combatPreferences = CombatPreferences.getSelected(preferences);
            preferences.add(combatPreferences.getDefaultShieldPreferences());
        }
        preferences.removeIf(Objects::isNull);
    }

    protected void setCharacterDefinition() throws InvalidXmlElementException, InvalidRandomElementSelectedException {
        // Check if race is set.
        if (characterPlayer.getRace() == null) {
            final RandomRace randomRace = new RandomRace(characterPlayer, preferences);
            randomRace.assign();
        }

        if (characterPlayer.getInfo().getGender() == null) {
            characterPlayer.getInfo().setGender(Gender.randomGender());
        }

        if (characterPlayer.getInfo().getAge() == null) {
            final IGaussianDistribution ageDistribution = AgePreferences.getSelected(preferences);
            characterPlayer.getInfo().setAge(ageDistribution.randomGaussian());
        }

        if (characterPlayer.getFaction() == null) {
            final RandomFaction randomFaction = new RandomFaction(characterPlayer, preferences);
            randomFaction.assign();
        }

        if (characterPlayer.getInfo().getPlanet() == null) {
            final RandomPlanet randomPlanet = new RandomPlanet(characterPlayer, preferences);
            randomPlanet.assign();
        }

        if (characterPlayer.getInfo().getNames() == null || characterPlayer.getInfo().getNames().isEmpty() ||
                (characterPlayer.getInfo().getNames().stream().
                        allMatch(name -> name.getName().equals("")))) {
            final RandomName randomName = new RandomName(characterPlayer, preferences);
            randomName.assign();
        }

        if (characterPlayer.getInfo().getSurname() == null || Objects.equals(characterPlayer.getInfo().getSurname().getName(), "")) {
            final RandomSurname randomSurname = new RandomSurname(characterPlayer, preferences);
            randomSurname.assign();
        }
    }

    /**
     * Using free style character generation. Only the first points to expend in a
     * character.
     */
    private void setStartingValues() throws InvalidXmlElementException, InvalidRandomElementSelectedException {
        // Characteristics
        final RandomCharacteristics randomCharacteristics = new RandomCharacteristics(characterPlayer, preferences, characteristicsMinimumValues);
        randomCharacteristics.assign();
        // Skills
        final RandomSkills randomSkills = new RandomSkills(characterPlayer, preferences, requiredSkills, suggestedSkills);
        randomSkills.assign();
        // Traits
        final RandomBeneficeDefinition randomBenefice = new RandomBeneficeDefinition(characterPlayer, preferences, mandatoryBenefices, suggestedBenefices);
        randomBenefice.assign();
    }

    private void setExtraPoints() throws InvalidXmlElementException, InvalidRandomElementSelectedException {
        // Traits.
        // First, assign curses.
        final RandomCursesDefinition randomCurses = new RandomCursesDefinition(characterPlayer, preferences);
        randomCurses.assign();
        // Set blessings.
        final RandomBlessingDefinition randomBlessing = new RandomBlessingDefinition(characterPlayer, preferences);
        randomBlessing.assign();
        // Set benefices.
        final RandomBeneficeDefinition randomBenefice = new RandomExtraBeneficeDefinition(characterPlayer, preferences, suggestedBenefices);
        randomBenefice.assign();
        // Set psique level
        final RandomPsique randomPsique = new RandomPsique(characterPlayer, preferences);
        randomPsique.assign();
        // Set cybernetics
        final RandomCybernetics randomCybernetics = new RandomCybernetics(characterPlayer, preferences);
        randomCybernetics.assign();
        // Set Wyrd
        final IGaussianDistribution wyrdDistribution = PsiqueLevelPreferences.getSelected(preferences);
        final int extraWyrd = wyrdDistribution.randomGaussian();
        characterPlayer.addExtraWyrd(extraWyrd - characterPlayer.getBasicWyrdValue());
        RandomGenerationLog.info(this.getClass().getName(), "Added extra wyrd '{}'.", extraWyrd);
        // Set psi paths.
        final RandomPsiquePath randomPsiquePath = new RandomPsiquePath(characterPlayer, preferences);
        randomPsiquePath.assign();

        final DifficultLevelPreferences difficultLevel = DifficultLevelPreferences.getSelected(preferences);

        // Spend remaining points in skills and characteristics.
        int remainingPoints = FreeStyleCharacterCreation.getFreeAvailablePoints(characterPlayer.getInfo().getAge())
                - CostCalculator.getCost(characterPlayer, difficultLevel.getSkillsBonus(), difficultLevel.getCharacteristicsBonus());

        RandomGenerationLog.info(this.getClass().getName(), "Remaining points '{}'.", remainingPoints);
        final IGaussianDistribution specialization = SpecializationPreferences.getSelected(preferences);

        if (remainingPoints > 0) {
            final RandomCharacteristicsExtraPoints randomCharacteristicsExtraPoints = new RandomCharacteristicsExtraPoints(characterPlayer, preferences);
            final RandomSkillExtraPoints randomSkillExtraPoints = new RandomSkillExtraPoints(characterPlayer, preferences, suggestedSkills);
            while (remainingPoints > 0) {
                // Characteristics only if is a little specialized.
                if (remainingPoints >= CostCalculator.CHARACTERISTIC_EXTRA_POINTS_COST && specialization.randomGaussian() > 5) {
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
        final RandomWeapon randomRangedWeapon = new RandomRangeWeapon(characterPlayer, preferences, mandatoryWeapons);
        try {
            randomRangedWeapon.assign();
        } catch (InvalidRandomElementSelectedException ires) {
            RandomGenerationLog.warning(this.getClass().getName(), "No ranged weapons available for '{}'.", characterPlayer);
        }
        final RandomWeapon randomMeleeWeapon = new RandomMeleeWeapon(characterPlayer, preferences, mandatoryWeapons);
        try {
            randomMeleeWeapon.assign();
        } catch (InvalidRandomElementSelectedException ires) {
            RandomGenerationLog.warning(this.getClass().getName(), "No melee weapons available for '{}'.", characterPlayer);
        }

        final RandomShield randomShield = new RandomShield(characterPlayer, preferences, mandatoryShields);
        try {
            randomShield.assign();
        } catch (InvalidShieldException e) {
            // Probably already has a shield.
            RandomGenerationLog.warning(this.getClass().getName(), e.getMessage());
        } catch (InvalidRandomElementSelectedException e) {
            RandomGenerationLog.warning(this.getClass().getName(), "No shields available for '{}}'.", characterPlayer);
        }

        // Set armours
        final RandomArmour randomArmour = new RandomArmour(characterPlayer, preferences, mandatoryArmours);
        try {
            randomArmour.assign();
        } catch (InvalidArmourException e) {
            // Probably already has a shield.
            RandomGenerationLog.warning(this.getClass().getName(), e.getMessage());
        } catch (InvalidRandomElementSelectedException e) {
            RandomGenerationLog.warning(this.getClass().getName(), "No armours available for '{}'.", characterPlayer);
        }
    }

    private void setExperiencePoints() throws InvalidXmlElementException {
        if (characterPlayer.getExperienceEarned() > 0) {
            final RandomCharacteristicsExperience randomCharacteristicsExperience = new RandomCharacteristicsExperience(characterPlayer, preferences);
            try {
                randomCharacteristicsExperience.assign();
            } catch (InvalidRandomElementSelectedException e) {
                // Not valid characteristic. Ignore it.
            }
            // Spend remaingin XP on skills.
            final RandomSkillExperience randomSkillExperience = new RandomSkillExperience(characterPlayer, preferences);
            try {
                randomSkillExperience.assign();
            } catch (InvalidRandomElementSelectedException e) {
                // Not more skills to improve. Ignore it.
            }
        }
    }

    @Override
    public String toString() {
        return characterPlayer.getCompleteNameRepresentation() + " (" + characterPlayer.getRace() + ") [" + characterPlayer.getFaction() + "]";
    }
}
