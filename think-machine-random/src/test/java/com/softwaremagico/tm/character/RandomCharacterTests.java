package com.softwaremagico.tm.character;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.benefices.AvailableBeneficeFactory;
import com.softwaremagico.tm.character.benefices.BeneficeAlreadyAddedException;
import com.softwaremagico.tm.character.benefices.RandomBeneficeDefinition;
import com.softwaremagico.tm.character.blessings.BlessingClassification;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.characteristics.CharacteristicType;
import com.softwaremagico.tm.character.creation.CostCalculator;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.character.cybernetics.CyberneticDeviceFactory;
import com.softwaremagico.tm.character.cybernetics.RequiredCyberneticDevicesException;
import com.softwaremagico.tm.character.cybernetics.TooManyCyberneticDevicesException;
import com.softwaremagico.tm.character.equipment.weapons.RandomRangeWeapon;
import com.softwaremagico.tm.character.equipment.weapons.RandomWeapon;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.occultism.OccultismType;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.character.races.RaceFactory;
import com.softwaremagico.tm.character.skills.*;
import com.softwaremagico.tm.file.PathManager;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.log.MachineLog;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.*;
import com.softwaremagico.tm.txt.CharacterSheet;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.HashSet;

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

@Test(groups = {"randomCharacter"})
public class RandomCharacterTests {
    private static final String LANGUAGE = "es";

    @AfterMethod
    public void clearCache() {
        LanguagePool.clearCache();
    }

    @Test
    public void chooseRaceAndFactionTest() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0, RacePreferences.HUMAN,
                FactionPreferences.NOBILITY);
        randomizeCharacter.setCharacterDefinition();

        Assert.assertEquals(characterPlayer.getFaction().getFactionGroup(), FactionGroup.NOBILITY);
        Assert.assertEquals(characterPlayer.getRace(), RaceFactory.getInstance()
                .getElement(RacePreferences.HUMAN.name(), LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
    }

    @Test
    public void chooseRaceAndFactionTestXeno() throws InvalidXmlElementException, InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0, RacePreferences.XENO,
                FactionPreferences.GUILD);
        randomizeCharacter.setCharacterDefinition();

        Assert.assertEquals(characterPlayer.getFaction().getFactionGroup(), FactionGroup.GUILD);
        Assert.assertNotEquals(characterPlayer.getRace(), RaceFactory.getInstance().getElement(RacePreferences.HUMAN.name(),
                LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
    }

    @Test
    public void readRandomSkillConfigurationArchery() {
        final SkillDefinition skillDefinition = SkillsDefinitionsFactory.getInstance().get("archery", "en",
                PathManager.DEFAULT_MODULE_FOLDER);
        Assert.assertEquals(skillDefinition.getRandomDefinition().getMinimumTechLevel().intValue(), 0);
        Assert.assertEquals(skillDefinition.getRandomDefinition().getMaximumTechLevel().intValue(), 2);
    }

    @Test
    public void checkWeightLimitedByDefinition()
            throws InvalidXmlElementException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomWeapon randomWeapons = new RandomRangeWeapon(characterPlayer, null, new HashSet<>());
        final Weapon largeRock = WeaponFactory.getInstance().getElement("rock", LANGUAGE,
                PathManager.DEFAULT_MODULE_FOLDER);
        Assert.assertEquals(randomWeapons.getTotalWeight(largeRock), 0);
    }

    @Test(expectedExceptions = {InvalidRandomElementSelectedException.class})
    public void checkSkillLimitationByTechnology()
            throws InvalidXmlElementException, InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        characterPlayer.setCharacteristic(CharacteristicName.TECH, 7);

        final RandomSkills randomSkills = new RandomSkills(characterPlayer, null);
        final AvailableSkill availableSkill = AvailableSkillsFactory.getInstance().getElement("archery", LANGUAGE,
                PathManager.DEFAULT_MODULE_FOLDER);
        randomSkills.validateElement(availableSkill);
    }

    @Test(expectedExceptions = {InvalidRandomElementSelectedException.class})
    public void checkSkillLimitationByLowTechnology()
            throws InvalidXmlElementException, InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        characterPlayer.setCharacteristic(CharacteristicName.TECH, 1);

        final RandomSkills randomSkills = new RandomSkills(characterPlayer, null);
        final AvailableSkill availableSkill = AvailableSkillsFactory.getInstance().getElement("spacecraft", LANGUAGE,
                PathManager.DEFAULT_MODULE_FOLDER);
        randomSkills.validateElement(availableSkill);
    }

    @Test(expectedExceptions = {InvalidRandomElementSelectedException.class})
    public void checkBeneficeLimitationByRace()
            throws InvalidXmlElementException, InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        characterPlayer
                .setRace(RaceFactory.getInstance().getElement("human", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));

        final RandomBeneficeDefinition randomBenefice = new RandomBeneficeDefinition(characterPlayer, null);
        final AvailableBenefice benefice = AvailableBeneficeFactory.getInstance().getElement("language [urthish]",
                LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        randomBenefice.validateElement(benefice.getRandomDefinition());
    }

    @Test
    public void readRandomSkillConfigurationSlugs() {
        final SkillDefinition skillDefinition = SkillsDefinitionsFactory.getInstance().get("slugGuns", "en",
                PathManager.DEFAULT_MODULE_FOLDER);
        Assert.assertEquals(skillDefinition.getRandomDefinition().getMinimumTechLevel().intValue(), 2);
        Assert.assertEquals(skillDefinition.getRandomDefinition().getMaximumTechLevel().intValue(), 6);
    }

    @Test
    public void selectSkillGroup() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0,
                SkillGroupPreferences.COMBAT);
        randomizeCharacter.createCharacter();

        Assert.assertEquals(CostCalculator.getCost(characterPlayer),
                FreeStyleCharacterCreation.getFreeAvailablePoints(characterPlayer.getInfo().getAge()));
        Assert.assertTrue(characterPlayer.getRanksAssigned(SkillGroupPreferences.COMBAT.getSkillGroup()) > 10);
    }

    @Test
    public void mustHaveStatus() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        characterPlayer
                .setRace(RaceFactory.getInstance().getElement("human", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        characterPlayer.setFaction(
                FactionsFactory.getInstance().getElement("hazat", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0,
                StatusPreferences.HIGH);
        randomizeCharacter.createCharacter();
        Assert.assertNotNull(characterPlayer.getRank());
        Assert.assertEquals(CostCalculator.getCost(characterPlayer),
                FreeStyleCharacterCreation.getFreeAvailablePoints(characterPlayer.getInfo().getAge()));
    }

    @Test
    public void checkBlessingPreferences() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0,
                CurseNumberPreferences.FAIR, BlessingNumberPreferences.HIGH);
        characterPlayer
                .setRace(RaceFactory.getInstance().getElement("human", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        randomizeCharacter.createCharacter();
        try {
            Assert.assertTrue(characterPlayer.getCurses().size() >= CurseNumberPreferences.FAIR.minimum()
                    + characterPlayer.getFaction().getBlessings(BlessingClassification.CURSE).size());
            Assert.assertTrue(characterPlayer.getCurses().size() <= CurseNumberPreferences.FAIR.maximum()
                    + characterPlayer.getFaction().getBlessings(BlessingClassification.CURSE).size());
        } catch (Error ae) {
            final CharacterSheet characterSheet = new CharacterSheet(characterPlayer);
            System.out.println(characterSheet.toString());
            throw ae;
        }

        try {
            Assert.assertTrue(characterPlayer.getAllBlessings().size() >= BlessingNumberPreferences.HIGH.minimum()
                    + characterPlayer.getFaction().getBlessings().size());
            Assert.assertTrue(characterPlayer.getAllBlessings().size() <= BlessingNumberPreferences.HIGH.maximum()
                    + characterPlayer.getFaction().getBlessings().size());
        } catch (Exception e) {
            final CharacterSheet characterSheet = new CharacterSheet(characterPlayer);
            System.out.println(characterSheet.toString());
            throw e;
        }
    }

    @Test
    public void createPsiqueCharacter() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        characterPlayer
                .setRace(RaceFactory.getInstance().getElement("human", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0,
                SpecializationPreferences.SPECIALIZED, OccultismPathLevelPreferences.HIGH, OccultismLevelPreferences.HIGH,
                StatusPreferences.FAIR);
        randomizeCharacter.createCharacter();
        try {
            Assert.assertTrue(characterPlayer.getSelectedPowers().values().size() > 0);
        } catch (Exception e) {
            final CharacterSheet characterSheet = new CharacterSheet(characterPlayer);
            System.out.println(characterSheet.toString());
            throw e;
        }
    }

    @Test
    public void createChurchCharacter() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        characterPlayer
                .setRace(RaceFactory.getInstance().getElement("human", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        characterPlayer.setFaction(
                FactionsFactory.getInstance().getElement("orthodox", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0,
                SpecializationPreferences.SPECIALIZED, OccultismPathLevelPreferences.HIGH, OccultismLevelPreferences.HIGH,
                StatusPreferences.FAIR);
        randomizeCharacter.createCharacter();
        Assert.assertTrue(characterPlayer.getTotalSelectedPowers() > 0);
    }

    @Test
    public void voroxCannotHavePsique() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        characterPlayer
                .setRace(RaceFactory.getInstance().getElement("vorox", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        characterPlayer
                .setFaction(FactionsFactory.getInstance().getElement("vorox", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0,
                SpecializationPreferences.SPECIALIZED, OccultismPathLevelPreferences.HIGH, OccultismLevelPreferences.HIGH);
        randomizeCharacter.createCharacter();
        Assert.assertTrue(characterPlayer.getFaction().getBenefices().contains(AvailableBeneficeFactory.getInstance()
                .getElement("noOccult", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)));
        Assert.assertTrue(characterPlayer.getAfflictions().contains(AvailableBeneficeFactory.getInstance()
                .getElement("noOccult", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)));
        for (final OccultismType occultismType : OccultismTypeFactory.getInstance().getElements(LANGUAGE,
                PathManager.DEFAULT_MODULE_FOLDER)) {
            Assert.assertEquals(characterPlayer.getOccultismLevel(occultismType), 0);
        }
        Assert.assertEquals(characterPlayer.getTotalSelectedPowers(), 0);
    }

    @Test
    public void namesByStatus() throws InvalidXmlElementException, InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0,
                NamesPreferences.VERY_HIGH);
        randomizeCharacter.createCharacter();
        Assert.assertTrue(characterPlayer.getInfo().getNames().size() >= 2);
        Assert.assertNotNull(characterPlayer.getInfo().getSurname());
    }

    @Test
    public void weapons() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException, BeneficeAlreadyAddedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        characterPlayer.setSkillRank(AvailableSkillsFactory.getInstance().getElement("slugGuns", LANGUAGE,
                PathManager.DEFAULT_MODULE_FOLDER), 5);
        characterPlayer.setSkillRank(AvailableSkillsFactory.getInstance().getElement("energyGuns", LANGUAGE,
                PathManager.DEFAULT_MODULE_FOLDER), 5);
        characterPlayer.setSkillRank(
                AvailableSkillsFactory.getInstance().getElement("melee", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER),
                5);
        characterPlayer.setCharacteristic(CharacteristicName.TECH, 7);
        characterPlayer.addBenefice(AvailableBeneficeFactory.getInstance().getElement("cash [firebirds2000]", LANGUAGE,
                PathManager.DEFAULT_MODULE_FOLDER));
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0,
                CombatPreferences.BELLIGERENT);
        randomizeCharacter.createCharacter();
        Assert.assertTrue(characterPlayer.getAllWeapons().size() >= 2);
    }

    @Test
    public void age() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        characterPlayer
                .setRace(RaceFactory.getInstance().getElement("human", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0,
                AgePreferences.CHILD);
        randomizeCharacter.createCharacter();

        Assert.assertEquals(FreeStyleCharacterCreation.getMaxInitialCharacteristicsValues(CharacteristicName.DEXTERITY,
                characterPlayer.getInfo().getAge(), characterPlayer.getRace()), 4);
        Assert.assertEquals(FreeStyleCharacterCreation.getMaxInitialSkillsValues(characterPlayer.getInfo().getAge()),
                4);
        for (final Characteristic characteristic : characterPlayer.getCharacteristics(CharacteristicType.BODY)) {
            Assert.assertTrue(
                    characteristic.getValue() <= FreeStyleCharacterCreation.getMaxInitialCharacteristicsValues(
                            characteristic.getCharacteristicDefinition().getCharacteristicName(),
                            characterPlayer.getInfo().getAge(), characterPlayer.getRace()));
        }
        for (final Characteristic characteristic : characterPlayer.getCharacteristics(CharacteristicType.MIND)) {
            Assert.assertTrue(
                    characteristic.getValue() <= FreeStyleCharacterCreation.getMaxInitialCharacteristicsValues(
                            characteristic.getCharacteristicDefinition().getCharacteristicName(),
                            characterPlayer.getInfo().getAge(), characterPlayer.getRace()));
        }
        for (final Characteristic characteristic : characterPlayer.getCharacteristics(CharacteristicType.SPIRIT)) {
            Assert.assertTrue(
                    characteristic.getValue() <= FreeStyleCharacterCreation.getMaxInitialCharacteristicsValues(
                            characteristic.getCharacteristicDefinition().getCharacteristicName(),
                            characterPlayer.getInfo().getAge(), characterPlayer.getRace()));
        }

        for (final AvailableSkill skill : AvailableSkillsFactory.getInstance().getNaturalSkills(LANGUAGE,
                PathManager.DEFAULT_MODULE_FOLDER)) {
            try {
                Assert.assertTrue(characterPlayer.getSkillAssignedRanks(skill) <= FreeStyleCharacterCreation
                        .getMaxInitialSkillsValues(characterPlayer.getInfo().getAge()));
            } catch (AssertionError e) {
                MachineLog.severe(this.getClass().getName(), "Invalid skill ranks in '{}' ({}). Max allowed: {}. "
                        , skill, characterPlayer.getSkillAssignedRanks(skill),
                        FreeStyleCharacterCreation.getMaxInitialSkillsValues(characterPlayer.getInfo().getAge()));
                throw e;
            }
        }
        for (final AvailableSkill skill : AvailableSkillsFactory.getInstance().getLearnedSkills(LANGUAGE,
                PathManager.DEFAULT_MODULE_FOLDER)) {
            try {
                Assert.assertTrue(characterPlayer.getSkillAssignedRanks(skill) <= FreeStyleCharacterCreation
                        .getMaxInitialSkillsValues(characterPlayer.getInfo().getAge()));
            } catch (AssertionError e) {
                MachineLog.severe(this.getClass().getName(), "Invalid skill ranks in '{}' ({}). Max allowed: {}. "
                        , skill, characterPlayer.getSkillAssignedRanks(skill),
                        FreeStyleCharacterCreation.getMaxInitialSkillsValues(characterPlayer.getInfo().getAge()));
                throw e;
            }
        }
    }

    @Test
    public void weaponsSkills() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        characterPlayer
                .addWeapon(WeaponFactory.getInstance().getElement("axe", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        characterPlayer.addWeapon(
                WeaponFactory.getInstance().getElement("martechGold", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));

        Assert.assertNotNull(characterPlayer.hasWeaponWithSkill(
                AvailableSkillsFactory.getInstance().getElement("melee", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)));
        Assert.assertNotNull(characterPlayer.hasWeaponWithSkill(AvailableSkillsFactory.getInstance()
                .getElement("energyGuns", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)));

        characterPlayer.getCharacteristic(CharacteristicName.TECH).setValue(6);

        final RandomSkills randomSkills = new RandomSkills(characterPlayer, null);
        final AvailableSkill energyGuns = AvailableSkillsFactory.getInstance().getElement("energyGuns", LANGUAGE,
                PathManager.DEFAULT_MODULE_FOLDER);
        randomSkills.validateElement(energyGuns.getRandomDefinition());
        final AvailableSkill fight = AvailableSkillsFactory.getInstance().getElement("melee", LANGUAGE,
                PathManager.DEFAULT_MODULE_FOLDER);
        randomSkills.validateElement(fight.getRandomDefinition());

        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0);
        randomizeCharacter.createCharacter();

        Assert.assertTrue(characterPlayer.getSkillTotalRanks(AvailableSkillsFactory.getInstance().getElement("melee",
                LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)) > 0);
        Assert.assertTrue(characterPlayer.getSkillTotalRanks(AvailableSkillsFactory.getInstance()
                .getElement("energyGuns", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)) > 0);
    }

    @Test
    public void cybernetics() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0,
                CyberneticTotalDevicesPreferences.CYBORG, CyberneticPointsPreferences.SOUL_LESS);
        randomizeCharacter.createCharacter();

        Assert.assertTrue(
                characterPlayer.getCybernetics().size() >= CyberneticTotalDevicesPreferences.CYBORG.minimum());
    }

    @Test
    public void cyberneticsSkills()
            throws InvalidXmlElementException, InvalidRandomElementSelectedException,
            TooManyCyberneticDevicesException, RequiredCyberneticDevicesException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        characterPlayer.setCharacteristic(CharacteristicName.WILL, 6);
        characterPlayer.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("spyEye", LANGUAGE,
                PathManager.DEFAULT_MODULE_FOLDER));
        characterPlayer.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("etherEar", LANGUAGE,
                PathManager.DEFAULT_MODULE_FOLDER));
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0);
        randomizeCharacter.createCharacter();

        Assert.assertTrue(characterPlayer.getSkillTotalRanks(AvailableSkillsFactory.getInstance().getElement("spyEye",
                LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)) > 0);
        Assert.assertTrue(characterPlayer.getSkillTotalRanks(AvailableSkillsFactory.getInstance().getElement("etherEar",
                LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)) > 0);
    }

    @Test
    public void experience() throws
            InvalidXmlElementException, InvalidRandomElementSelectedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        int experiencePoints = 100;
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, experiencePoints,
                SpecializationPreferences.VERY_SPECIALIZED);
        randomizeCharacter.createCharacter();
        Assert.assertEquals(characterPlayer.getExperienceEarned(), experiencePoints);
        Assert.assertTrue(characterPlayer.getExperienceExpended() > 0);
        Assert.assertTrue(characterPlayer.getExperienceEarned() - characterPlayer.getExperienceExpended() < 2);
    }

}
