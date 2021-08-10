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
import com.softwaremagico.tm.character.benefices.*;
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.blessings.RandomBlessingDefinition;
import com.softwaremagico.tm.character.blessings.RandomCursesDefinition;
import com.softwaremagico.tm.character.characteristics.*;
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
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.RandomFaction;
import com.softwaremagico.tm.character.occultism.OccultismPath;
import com.softwaremagico.tm.character.occultism.RandomPsique;
import com.softwaremagico.tm.character.occultism.RandomPsiquePath;
import com.softwaremagico.tm.character.races.Race;
import com.softwaremagico.tm.character.races.RandomRace;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.RandomSkillExperience;
import com.softwaremagico.tm.character.skills.RandomSkillExtraPoints;
import com.softwaremagico.tm.character.skills.RandomSkills;
import com.softwaremagico.tm.log.MachineLog;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.predefined.IRandomPredefined;
import com.softwaremagico.tm.random.predefined.PredefinedMerger;
import com.softwaremagico.tm.random.selectors.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class RandomizeCharacter {
    private final CharacterPlayer characterPlayer;
    private final Set<IRandomPreference<?>> preferences;
    private final Set<AvailableSkill> requiredSkills;
    private final Set<AvailableSkill> suggestedSkills;
    private final Set<Blessing> mandatoryBlessings;
    private final Set<Blessing> suggestedBlessings;
    private final Set<BeneficeDefinition> mandatoryBenefices;
    private final Set<BeneficeDefinition> suggestedBenefices;
    private final Set<AvailableBenefice> suggestedAvailableBenefices;
    private final Set<Characteristic> characteristicsMinimumValues;
    private final Set<OccultismPath> mandatoryOccultismPaths;
    private final Set<Weapon> mandatoryWeapons;
    private final Set<Armour> mandatoryArmours;
    private final Set<Shield> mandatoryShields;
    private final Faction requiredFaction;
    private final Race requiredRace;

    public RandomizeCharacter(CharacterPlayer characterPlayer, int experiencePoints, IRandomPreference<?>... preferences) {
        this(characterPlayer, experiencePoints, null, new HashSet<>(Arrays.asList(preferences)), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
    }

    public RandomizeCharacter(CharacterPlayer characterPlayer, IRandomPredefined... profiles) {
        this(characterPlayer, null, new HashSet<>(Arrays.asList(profiles)), new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
    }

    public RandomizeCharacter(CharacterPlayer characterPlayer, Set<IRandomPreference<?>> preferences, IRandomPredefined... profiles) {
        this(characterPlayer, null, new HashSet<>(Arrays.asList(profiles)), preferences, new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
    }

    public RandomizeCharacter(CharacterPlayer characterPlayer, Integer experiencePoints, Set<IRandomPredefined> profiles, Set<IRandomPreference<?>> preferences,
                              Set<AvailableSkill> requiredSkills, Set<AvailableSkill> suggestedSkills,
                              Set<Blessing> mandatoryBlessings, Set<Blessing> suggestedBlessings,
                              Set<BeneficeDefinition> mandatoryBenefices, Set<BeneficeDefinition> suggestedBenefices,
                              Set<AvailableBenefice> mandatoryAvailableBenefices, Set<AvailableBenefice> suggestedAvailableBenefices,
                              Set<OccultismPath> mandatoryOccultismPaths,
                              Set<Weapon> mandatoryWeapons, Set<Armour> mandatoryArmours, Set<Shield> mandatoryShields) {
        this.characterPlayer = characterPlayer;

        final IRandomPredefined finalProfile = PredefinedMerger.merge(profiles, preferences, requiredSkills, suggestedSkills,
                mandatoryBlessings, suggestedBlessings, mandatoryBenefices, suggestedBenefices, mandatoryAvailableBenefices,
                suggestedAvailableBenefices, mandatoryOccultismPaths, mandatoryWeapons, mandatoryArmours, mandatoryShields,
                characterPlayer.getLanguage(), characterPlayer.getModuleName());

        // Assign preferences
        this.preferences = finalProfile.getPreferences();
        this.requiredSkills = finalProfile.getRequiredSkills();
        this.suggestedSkills = finalProfile.getSuggestedSkills();
        this.mandatoryBenefices = finalProfile.getMandatoryBenefices();
        this.suggestedBenefices = finalProfile.getSuggestedBenefices();
        this.suggestedAvailableBenefices = finalProfile.getSuggestedBeneficeSpecializations();
        this.characteristicsMinimumValues = finalProfile.getCharacteristicsMinimumValues();
        this.mandatoryWeapons = finalProfile.getMandatoryWeapons();
        this.mandatoryArmours = finalProfile.getMandatoryArmours();
        this.mandatoryShields = finalProfile.getMandatoryShields();
        this.requiredFaction = finalProfile.getFaction();
        this.requiredRace = finalProfile.getRace();
        this.mandatoryBlessings = finalProfile.getMandatoryBlessings();
        this.suggestedBlessings = finalProfile.getSuggestedBlessings();
        this.mandatoryOccultismPaths = finalProfile.getMandatoryOccultismPaths();

        //Include AvailableBenefices as beneficedefinitions to random calculation probability.
        this.suggestedBenefices.addAll(suggestedAvailableBenefices.stream().map(AvailableBenefice::getBeneficeDefinition).collect(Collectors.toList()));

        setMandatoryTech();

        setMandatoryAvailableBenefices(finalProfile.getMandatoryBeneficeSpecializations());


        // Assign experience
        if (experiencePoints == null) {
            final DifficultLevelPreferences difficultLevel = DifficultLevelPreferences.getSelected(this.preferences);
            this.characterPlayer.setExperienceEarned(difficultLevel.getExperienceBonus());
        } else {
            this.characterPlayer.setExperienceEarned(experiencePoints);
        }
    }

    /**
     * Some skills requires a minimum tech.
     */
    private void setMandatoryTech() {
        Characteristic tech = characteristicsMinimumValues.stream().filter(characteristic ->
                characteristic.getCharacteristicDefinition().getCharacteristicName() ==
                        CharacteristicName.TECH).findAny().orElse(null);
        for (final AvailableSkill availableSkill : requiredSkills) {
            if (availableSkill.getRandomDefinition().getMinimumTechLevel() != null) {
                if (tech == null) {
                    try {
                        tech = new Characteristic(CharacteristicsDefinitionFactory
                                .getInstance().get(CharacteristicName.TECH, characterPlayer.getLanguage(), characterPlayer.getModuleName()));
                        tech.setValue(availableSkill.getRandomDefinition().getMinimumTechLevel());
                        characteristicsMinimumValues.add(tech);
                    } catch (InvalidCharacteristicException e) {
                        MachineLog.errorMessage(this.getClass().getName(), e);
                    }
                } else {
                    if (tech.getValue() < availableSkill.getRandomDefinition().getMinimumTechLevel()) {
                        tech.setValue(availableSkill.getRandomDefinition().getMinimumTechLevel());
                    }
                }
            }
        }
    }

    private void setMandatoryAvailableBenefices(Set<AvailableBenefice> mandatoryAvailableBenefices) {
        for (final AvailableBenefice availableBenefice : mandatoryAvailableBenefices) {
            try {
                characterPlayer.addBenefice(availableBenefice);
            } catch (InvalidBeneficeException e) {
                RandomGenerationLog.errorMessage(this.getClass().getName(), e);
            } catch (BeneficeAlreadyAddedException | RestrictedElementException | UnofficialElementNotAllowedException e) {
                //Ignore it.
            }
        }
    }

    public void createCharacter() throws InvalidXmlElementException, InvalidRandomElementSelectedException, RestrictedElementException,
            UnofficialElementNotAllowedException {
        setDefaultPreferences();
        setCharacterDefinition();
        setStartingValues();
        setExtraPoints();
        setInitialEquipment();
        // Expend XP if any.
        setExperiencePoints();
    }

    public void setDefaultPreferences() {
        final AgePreferences agePreferences = AgePreferences.getSelected(preferences);
        if (agePreferences == null) {
            preferences.add(AgePreferences.getDefaultOption());
        }

        // Point distribution is "Fair" by default.
        final SpecializationPreferences selectedSpecialization = SpecializationPreferences.getSelected(preferences);
        if (selectedSpecialization == null) {
            preferences.add(SpecializationPreferences.getDefaultOption());
        }

        final CombatActionsPreferences combatActionsPreferences = CombatActionsPreferences.getSelected(preferences);
        if (combatActionsPreferences == null) {
            preferences.add(CombatActionsPreferences.getDefaultOption());
        }

        // Low traits by default.
        final TraitCostPreferences traitCostPreferences = TraitCostPreferences.getSelected(preferences);
        if (traitCostPreferences == null || traitCostPreferences == TraitCostPreferences.LOW) {
            if (combatActionsPreferences != null) {
                preferences.remove(TraitCostPreferences.LOW);
                preferences.add(TraitCostPreferences.GOOD);
            } else {
                preferences.add(TraitCostPreferences.getDefaultOption());
            }
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

        final CashPreferences cashPreferences = CashPreferences.getSelected(preferences);
        if (cashPreferences == null) {
            //Faction and status also change the cash amount.
            final CashPreferences factionCashPreference = CashPreferences.get(FactionPreferences.getSelected(preferences));
            final CashPreferences statusCashPreference = CashPreferences.get(StatusPreferences.getSelected(preferences));

            //Equipment minimum cash.
            final AtomicReference<Float> equipmentCost = new AtomicReference<>((float) 0);
            mandatoryWeapons.forEach(weapon -> equipmentCost.updateAndGet(v -> (v + weapon.getCost())));
            mandatoryArmours.forEach(armour -> equipmentCost.updateAndGet(v -> (v + armour.getCost())));
            mandatoryShields.forEach(shield -> equipmentCost.updateAndGet(v -> (v + shield.getCost())));
            final CashPreferences equipmentCostPreference = CashPreferences.get(equipmentCost.get());

            //Select higher value.
            final List<IRandomPreference> list = Arrays.asList(factionCashPreference,
                    statusCashPreference, equipmentCostPreference);
            final Optional<IRandomPreference> cashSelected = list.stream().filter(Objects::nonNull).max(Comparator.naturalOrder());
            cashSelected.ifPresent(preferences::add);
        }
        preferences.removeIf(Objects::isNull);
    }

    protected void setCharacterDefinition() throws InvalidXmlElementException, InvalidRandomElementSelectedException, RestrictedElementException,
            UnofficialElementNotAllowedException {
        // Check if race is set.
        if (characterPlayer.getRace() == null) {
            if (requiredRace != null) {
                try {
                    characterPlayer.setRace(requiredRace);
                } catch (RestrictedElementException e) {
                    //Race cannot be added.
                }
            } else {
                final RandomRace randomRace = new RandomRace(characterPlayer, preferences);
                randomRace.assign();
            }
        }

        if (characterPlayer.getInfo().getGender() == null) {
            final GenderPreferences genderPreference = GenderPreferences.getSelected(preferences);
            characterPlayer.getInfo().setGender(genderPreference.randomGender());
        }

        if (characterPlayer.getInfo().getAge() == null) {
            final IGaussianDistribution ageDistribution = AgePreferences.getSelected(preferences);
            characterPlayer.getInfo().setAge(ageDistribution.randomGaussian());
        }

        if (characterPlayer.getFaction() == null) {
            if (requiredFaction != null) {
                try {
                    //Race not correct for this faction. Correct it.
                    if (!requiredFaction.getRestrictedToRaces().isEmpty() && !requiredFaction.getRestrictedToRaces().contains(characterPlayer.getRace())) {
                        //Gets random restricted to race.
                        Optional<Race> selectedRace = requiredFaction.getRestrictedToRaces().stream().skip((int)
                                (requiredFaction.getRestrictedToRaces().size() * Math.random())).findAny();
                        if (selectedRace.isPresent()) {
                            characterPlayer.setRace(selectedRace.get());
                        } else {
                            selectedRace = requiredFaction.getRestrictedToRaces().stream().findAny();
                            if (selectedRace.isPresent()) {
                                characterPlayer.setRace(selectedRace.get());
                            } else {
                                RandomGenerationLog.severe(this.getClass().getName(), "No race selected for mandatory faction '" +
                                        requiredFaction + "'.");
                            }
                        }
                    }
                    characterPlayer.setFaction(requiredFaction);
                } catch (RestrictedElementException e) {
                    //Faction cannot be added.
                }
            } else {
                final RandomFaction randomFaction = new RandomFaction(characterPlayer, preferences);
                randomFaction.assign();
            }
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
    private void setStartingValues() throws InvalidXmlElementException, InvalidRandomElementSelectedException, RestrictedElementException,
            UnofficialElementNotAllowedException {
        // Characteristics
        final RandomCharacteristics randomCharacteristics = new RandomCharacteristics(characterPlayer, preferences, characteristicsMinimumValues);
        randomCharacteristics.assign();
        // Skills
        final RandomSkills randomSkills = new RandomSkills(characterPlayer, preferences, requiredSkills, suggestedSkills);
        randomSkills.assign();
        // Traits
        final RandomBeneficeDefinition randomBenefice = new RandomBeneficeDefinition(characterPlayer, preferences, mandatoryBenefices, suggestedBenefices,
                suggestedAvailableBenefices);
        randomBenefice.assign();
    }

    private void setExtraPoints() throws InvalidXmlElementException, InvalidRandomElementSelectedException, RestrictedElementException,
            UnofficialElementNotAllowedException {
        // Traits.
        // First, assign curses.
        final RandomCursesDefinition randomCurses = new RandomCursesDefinition(characterPlayer, preferences);
        randomCurses.assign();
        // Set blessings.
        final RandomBlessingDefinition randomBlessing = new RandomBlessingDefinition(characterPlayer, preferences, mandatoryBlessings, suggestedBlessings);
        randomBlessing.assign();
        // Set benefices.
        final RandomBeneficeDefinition randomBenefice = new RandomExtraBeneficeDefinition(characterPlayer, preferences, suggestedBenefices,
                suggestedAvailableBenefices);
        randomBenefice.assign();

        // Set cybernetics
        final RandomCybernetics randomCybernetics = new RandomCybernetics(characterPlayer, preferences);
        randomCybernetics.assign();

        // Set psique level
        final RandomPsique randomPsique = new RandomPsique(characterPlayer, preferences);
        randomPsique.assign();

        // Set psi paths.
        final RandomPsiquePath randomPsiquePath = new RandomPsiquePath(characterPlayer, preferences, mandatoryOccultismPaths);
        randomPsiquePath.assign();

        // Set Wyrd
        final IGaussianDistribution wyrdDistribution = OccultismLevelPreferences.getSelected(preferences);
        final int extraWyrd = wyrdDistribution.randomGaussian();
        characterPlayer.addExtraWyrd(extraWyrd - characterPlayer.getBasicWyrdValue());
        RandomGenerationLog.info(this.getClass().getName(), "Added extra wyrd '{}'.", extraWyrd);

        final DifficultLevelPreferences difficultLevel = DifficultLevelPreferences.getSelected(preferences);

        // Spend remaining points in skills and characteristics.
        int remainingPoints = FreeStyleCharacterCreation.getFreeAvailablePoints(characterPlayer.getInfo().getAge(), characterPlayer.getRace())
                - CostCalculator.getCost(characterPlayer, difficultLevel.getSkillsBonus(), difficultLevel.getCharacteristicsBonus());

        RandomGenerationLog.info(this.getClass().getName(), "Remaining points '{}'.", remainingPoints);
        final SpecializationPreferences specialization = SpecializationPreferences.getSelected(preferences);

        if (remainingPoints > 0) {
            final RandomCharacteristicsExtraPoints randomCharacteristicsExtraPoints = new RandomCharacteristicsExtraPoints(characterPlayer, preferences);
            final RandomSkillExtraPoints randomSkillExtraPoints = new RandomSkillExtraPoints(characterPlayer, preferences, suggestedSkills);
            while (remainingPoints > 0) {
                remainingPoints -= randomCharacteristicsExtraPoints.spendCharacteristicsPoints(remainingPoints);

                if (remainingPoints > 0) {
                    //Two skills checks for not specialized characters.
                    if (specialization.ordinal() <= SpecializationPreferences.FAIR.ordinal()) {
                        remainingPoints -= randomSkillExtraPoints.spendSkillsPoints(remainingPoints);
                    }
                    remainingPoints -= randomSkillExtraPoints.spendSkillsPoints(remainingPoints);
                }
            }
        }
    }

    private void setInitialEquipment() throws InvalidXmlElementException, RestrictedElementException, UnofficialElementNotAllowedException {
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

    private void setExperiencePoints() throws InvalidXmlElementException, RestrictedElementException, UnofficialElementNotAllowedException {
        if (characterPlayer.getExperienceEarned() > 0) {
            final RandomCharacteristicsExperience randomCharacteristicsExperience = new RandomCharacteristicsExperience(characterPlayer, preferences);
            try {
                randomCharacteristicsExperience.assign();
            } catch (InvalidRandomElementSelectedException e) {
                // Not valid characteristic. Ignore it.
            }
            // Spend remaining XP on skills.
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

    public Set<IRandomPreference<?>> getPreferences() {
        return preferences;
    }
}
