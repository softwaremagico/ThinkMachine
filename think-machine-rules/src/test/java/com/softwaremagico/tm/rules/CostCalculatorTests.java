package com.softwaremagico.tm.rules;

/*-
 * #%L
 * Think Machine (Rules)
 * %%
 * Copyright (C) 2017 - 2020 Softwaremagico
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
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.benefices.AvailableBeneficeFactory;
import com.softwaremagico.tm.character.benefices.BeneficeAlreadyAddedException;
import com.softwaremagico.tm.character.blessings.BlessingAlreadyAddedException;
import com.softwaremagico.tm.character.blessings.BlessingFactory;
import com.softwaremagico.tm.character.blessings.TooManyBlessingsException;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.creation.CostCalculator;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.character.cybernetics.*;
import com.softwaremagico.tm.character.equipment.armours.ArmourFactory;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.character.races.RaceFactory;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.character.skills.InvalidRanksException;
import com.softwaremagico.tm.file.PathManager;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;

@Test(groups = {"costCalculator"})
public class CostCalculatorTests {
    private static final String LANGUAGE = "es";
    private static final String MODULE = PathManager.DEFAULT_MODULE_FOLDER;

    @Test
    public void checkCharacteristicsCost() throws InvalidXmlElementException {
        final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        player.getInfo().setAge(31);
        player.setRace(RaceFactory.getInstance().getElement("human", LANGUAGE, MODULE));
        player.setFaction(FactionsFactory.getInstance().getElement("hazat", LANGUAGE, MODULE));

        CostCalculator costCalculator = new CostCalculator(player);

        player.setCharacteristic(CharacteristicName.WITS, 4);
        Assert.assertEquals(costCalculator.getCurrentCharacteristicPoints(), 1);
        player.setCharacteristic(CharacteristicName.PERCEPTION, 5);
        Assert.assertEquals(costCalculator.getCurrentCharacteristicPoints(), 3);
        player.setCharacteristic(CharacteristicName.TECH, 6);
        Assert.assertEquals(costCalculator.getCurrentCharacteristicPoints(), 6);
        player.setCharacteristic(CharacteristicName.PRESENCE, 7);
        Assert.assertEquals(costCalculator.getCurrentCharacteristicPoints(), 10);
        player.setCharacteristic(CharacteristicName.WILL, 8);
        Assert.assertEquals(costCalculator.getCurrentCharacteristicPoints(), 15);
        player.setCharacteristic(CharacteristicName.FAITH, 9);
        Assert.assertEquals(costCalculator.getCurrentCharacteristicPoints(), 20);
        Assert.assertEquals(costCalculator.getCurrentCharacteristicExtraPoints(), 1);

    }

    @Test
    public void checkNaturalSkillsCost() throws InvalidXmlElementException, InvalidRanksException {
        final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        player.getInfo().setAge(31);
        player.setRace(RaceFactory.getInstance().getElement("human", LANGUAGE, MODULE));
        player.setFaction(FactionsFactory.getInstance().getElement("hazat", LANGUAGE, MODULE));

        CostCalculator costCalculator = new CostCalculator(player);

        player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("influence", LANGUAGE, MODULE), 5);
        Assert.assertEquals(costCalculator.getCurrentSkillsPoints(), 2);

        player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("influence", LANGUAGE, MODULE), 0);
        Assert.assertEquals(costCalculator.getCurrentSkillsPoints(), 0);

        player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("influence", LANGUAGE, MODULE), 3);
        Assert.assertEquals(costCalculator.getCurrentSkillsPoints(), 0);

        player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("influence", LANGUAGE, MODULE), 4);
        Assert.assertEquals(costCalculator.getCurrentSkillsPoints(), 1);

        player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("influence", LANGUAGE, MODULE), 5);
        Assert.assertEquals(costCalculator.getCurrentSkillsPoints(), 2);
    }

    @Test
    public void checkSkillsCost() throws InvalidXmlElementException, InvalidRanksException {
        final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        player.getInfo().setAge(31);
        player.setRace(RaceFactory.getInstance().getElement("human", LANGUAGE, MODULE));
        player.setFaction(FactionsFactory.getInstance().getElement("hazat", LANGUAGE, MODULE));

        CostCalculator costCalculator = new CostCalculator(player);

        player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("influence", LANGUAGE, MODULE), 5);
        Assert.assertEquals(costCalculator.getCurrentSkillsPoints(), 2);
        player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("sneak", LANGUAGE, MODULE), 4);
        Assert.assertEquals(costCalculator.getCurrentSkillsPoints(), 3);
        player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("gaming", LANGUAGE, MODULE), 4);
        Assert.assertEquals(costCalculator.getCurrentSkillsPoints(), 7);
        player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("lockpicking", LANGUAGE, MODULE), 5);
        Assert.assertEquals(costCalculator.getCurrentSkillsPoints(), 12);
        player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("energyGuns", LANGUAGE, MODULE), 6);
        Assert.assertEquals(costCalculator.getCurrentSkillsPoints(), 18);
        player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("warfare", LANGUAGE, MODULE), 5);
        Assert.assertEquals(costCalculator.getCurrentSkillsPoints(), 23);
        player.setSkillRank(
                AvailableSkillsFactory.getInstance().getElement("lore", "jumpwebLore", LANGUAGE, MODULE), 4);
        Assert.assertEquals(costCalculator.getCurrentSkillsPoints(), 27);
        player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("lore", "beastsLore", LANGUAGE, MODULE),
                2);
        Assert.assertEquals(costCalculator.getCurrentSkillsPoints(), 29);
        player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("arts", "music", LANGUAGE, MODULE),
                6);
        Assert.assertEquals(costCalculator.getCurrentSkillsPoints(), 30);
        Assert.assertEquals(costCalculator.getCurrentSkillsExtraPoints(), 5);
        player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("knavery", LANGUAGE, MODULE),
                6);
        Assert.assertEquals(costCalculator.getCurrentSkillsPoints(), 30);
        Assert.assertEquals(costCalculator.getCurrentSkillsExtraPoints(), 11);
        player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("etiquette", LANGUAGE, MODULE),
                4);
        Assert.assertEquals(costCalculator.getCurrentSkillsPoints(), 30);
        Assert.assertEquals(costCalculator.getCurrentSkillsExtraPoints(), 15);
        //Reducing etiquete
        player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("etiquette", LANGUAGE, MODULE),
                1);
        Assert.assertEquals(costCalculator.getCurrentSkillsPoints(), 30);
        Assert.assertEquals(costCalculator.getCurrentSkillsExtraPoints(), 12);
        //Increasing again etiquette
        player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("etiquette", LANGUAGE, MODULE),
                4);
        Assert.assertEquals(costCalculator.getCurrentSkillsPoints(), 30);
        Assert.assertEquals(costCalculator.getCurrentSkillsExtraPoints(), 15);
        //Reducing knavery
        player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("knavery", LANGUAGE, MODULE),
                1);
        Assert.assertEquals(costCalculator.getCurrentSkillsPoints(), 30);
        Assert.assertEquals(costCalculator.getCurrentSkillsExtraPoints(), 10);
        //Reducing arts
        player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("arts", "music", LANGUAGE, MODULE),
                0);
        Assert.assertEquals(costCalculator.getCurrentSkillsPoints(), 30);
        Assert.assertEquals(costCalculator.getCurrentSkillsExtraPoints(), 4);
        //Reducing
        player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("energyGuns", LANGUAGE, MODULE), 0);
        Assert.assertEquals(costCalculator.getCurrentSkillsPoints(), 28);
        Assert.assertEquals(costCalculator.getCurrentSkillsExtraPoints(), 0);
        //Increasing again
        player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("energyGuns", LANGUAGE, MODULE), 3);
        Assert.assertEquals(costCalculator.getCurrentSkillsPoints(), 30);
        Assert.assertEquals(costCalculator.getCurrentSkillsExtraPoints(), 1);
        player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("influence", LANGUAGE, MODULE), 0);
        Assert.assertEquals(costCalculator.getCurrentSkillsPoints(), 29);
        Assert.assertEquals(costCalculator.getCurrentSkillsExtraPoints(), 0);
        player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("influence", LANGUAGE, MODULE), 5);
        Assert.assertEquals(costCalculator.getCurrentSkillsPoints(), 30);
        Assert.assertEquals(costCalculator.getCurrentSkillsExtraPoints(), 1);
        player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("influence", LANGUAGE, MODULE), 6);
        Assert.assertEquals(costCalculator.getCurrentSkillsPoints(), 30);
        Assert.assertEquals(costCalculator.getCurrentSkillsExtraPoints(), 2);
    }


    @Test
    public void checkOccultismLevelCost() throws InvalidXmlElementException {
        final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        player.getInfo().setAge(31);
        player.setRace(RaceFactory.getInstance().getElement("obun", LANGUAGE, MODULE));
        player.setFaction(FactionsFactory.getInstance().getElement("obun", LANGUAGE, MODULE));

        CostCalculator costCalculator = new CostCalculator(player);

        player.setOccultismLevel(OccultismTypeFactory.getPsi(LANGUAGE, MODULE), 4);
        Assert.assertEquals(costCalculator.getCurrentOccultismLevelExtraPoints(), 3);

        player.setOccultismLevel(OccultismTypeFactory.getPsi(LANGUAGE, MODULE), 0);
        Assert.assertEquals(costCalculator.getCurrentOccultismLevelExtraPoints(), 0);

        player.setOccultismLevel(OccultismTypeFactory.getPsi(LANGUAGE, MODULE), 3);
        Assert.assertEquals(costCalculator.getCurrentOccultismLevelExtraPoints(), 2);

        player.setOccultismLevel(OccultismTypeFactory.getPsi(LANGUAGE, MODULE), 4);
        Assert.assertEquals(costCalculator.getCurrentOccultismLevelExtraPoints(), 3);

        player.setOccultismLevel(OccultismTypeFactory.getPsi(LANGUAGE, MODULE), 1);
        Assert.assertEquals(costCalculator.getCurrentOccultismLevelExtraPoints(), 0);

        player.setOccultismLevel(OccultismTypeFactory.getPsi(LANGUAGE, MODULE), 4);
        Assert.assertEquals(costCalculator.getCurrentOccultismLevelExtraPoints(), 3);

        player.setOccultismLevel(OccultismTypeFactory.getPsi(LANGUAGE, MODULE), 0);
        Assert.assertEquals(costCalculator.getCurrentOccultismLevelExtraPoints(), 0);

        player.setOccultismLevel(OccultismTypeFactory.getPsi(LANGUAGE, MODULE), 1);
        Assert.assertEquals(costCalculator.getCurrentOccultismLevelExtraPoints(), 0);

        player.setOccultismLevel(OccultismTypeFactory.getPsi(LANGUAGE, MODULE), 1);
        Assert.assertEquals(costCalculator.getCurrentOccultismLevelExtraPoints(), 0);

        player.setOccultismLevel(OccultismTypeFactory.getPsi(LANGUAGE, MODULE), 0);
        Assert.assertEquals(costCalculator.getCurrentOccultismLevelExtraPoints(), 0);

        player.setOccultismLevel(OccultismTypeFactory.getPsi(LANGUAGE, MODULE), 6);
        Assert.assertEquals(costCalculator.getCurrentOccultismLevelExtraPoints(), 5);

        player.setOccultismLevel(OccultismTypeFactory.getPsi(LANGUAGE, MODULE), 5);
        Assert.assertEquals(costCalculator.getCurrentOccultismLevelExtraPoints(), 4);

        player.setOccultismLevel(OccultismTypeFactory.getPsi(LANGUAGE, MODULE), 6);
        Assert.assertEquals(costCalculator.getCurrentOccultismLevelExtraPoints(), 5);

        player.setOccultismLevel(OccultismTypeFactory.getPsi(LANGUAGE, MODULE), 7);
        Assert.assertEquals(costCalculator.getCurrentOccultismLevelExtraPoints(), 6);

        player.setOccultismLevel(OccultismTypeFactory.getPsi(LANGUAGE, MODULE), 1);
        Assert.assertEquals(costCalculator.getCurrentOccultismLevelExtraPoints(), 0);

        player.setOccultismLevel(OccultismTypeFactory.getPsi(LANGUAGE, MODULE), 0);
        Assert.assertEquals(costCalculator.getCurrentOccultismLevelExtraPoints(), 0);

        player.setOccultismLevel(OccultismTypeFactory.getPsi(LANGUAGE, MODULE), 6);
        Assert.assertEquals(costCalculator.getCurrentOccultismLevelExtraPoints(), 5);
    }

    @Test
    public void checkOccultismPowerCost() throws InvalidXmlElementException {
        final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        player.getInfo().setAge(31);
        player.setRace(RaceFactory.getInstance().getElement("obun", LANGUAGE, MODULE));
        player.setFaction(FactionsFactory.getInstance().getElement("obun", LANGUAGE, MODULE));

        CostCalculator costCalculator = new CostCalculator(player);

        player.setOccultismLevel(OccultismTypeFactory.getPsi(LANGUAGE, MODULE), 6);
        Assert.assertEquals(costCalculator.getCurrentOccultismLevelExtraPoints(), 5);

        player.addOccultismPower(
                OccultismPathFactory.getInstance().getElement("farHand", player.getLanguage(), player.getModuleName())
                        .getOccultismPowers().get("liftingHand"));
        player.addOccultismPower(
                OccultismPathFactory.getInstance().getElement("farHand", player.getLanguage(), player.getModuleName())
                        .getOccultismPowers().get("throwingHand"));
        player.addOccultismPower(OccultismPathFactory.getInstance()
                .getElement("sixthSense", player.getLanguage(), player.getModuleName()).getOccultismPowers()
                .get("sensitivity"));
        player.addOccultismPower(
                OccultismPathFactory.getInstance().getElement("soma", player.getLanguage(), player.getModuleName())
                        .getOccultismPowers().get("toughening"));
        player.addOccultismPower(
                OccultismPathFactory.getInstance().getElement("soma", player.getLanguage(), player.getModuleName())
                        .getOccultismPowers().get("strengthening"));
        player.addOccultismPower(
                OccultismPathFactory.getInstance().getElement("soma", player.getLanguage(), player.getModuleName())
                        .getOccultismPowers().get("quickening"));
        player.addOccultismPower(
                OccultismPathFactory.getInstance().getElement("soma", player.getLanguage(), player.getModuleName())
                        .getOccultismPowers().get("hardening"));
    }

    @Test
    public void checkTraitsCost() throws InvalidXmlElementException, TooManyBlessingsException,
            BlessingAlreadyAddedException, BeneficeAlreadyAddedException {
        final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        player.getInfo().setAge(31);
        player.setRace(RaceFactory.getInstance().getElement("human", LANGUAGE, MODULE));
        player.setFaction(FactionsFactory.getInstance().getElement("hazat", LANGUAGE, MODULE));

        CostCalculator costCalculator = new CostCalculator(player);

        player.addBlessing(
                BlessingFactory.getInstance().getElement("handsome", player.getLanguage(), player.getModuleName()));
        Assert.assertEquals(costCalculator.getCurrentTraitsPoints(), 1);
        player.addBlessing(
                BlessingFactory.getInstance().getElement("curious", player.getLanguage(), player.getModuleName()));
        Assert.assertEquals(costCalculator.getCurrentTraitsPoints(), 3);
        player.addBlessing(
                BlessingFactory.getInstance().getElement("missingEye", player.getLanguage(), player.getModuleName()));
        Assert.assertEquals(costCalculator.getCurrentTraitsPoints(), 0);
        player.addBlessing(
                BlessingFactory.getInstance().getElement("luckyAtCards", player.getLanguage(), player.getModuleName()));
        Assert.assertEquals(costCalculator.getCurrentTraitsPoints(), 2);

        player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("stigma_1", player.getLanguage(),
                player.getModuleName()));
        Assert.assertEquals(costCalculator.getCurrentTraitsPoints(), 1);
        player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("heir", player.getLanguage(),
                player.getModuleName()));
        Assert.assertEquals(costCalculator.getCurrentTraitsPoints(), 3);
        player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("wireblade", player.getLanguage(),
                player.getModuleName()));
        Assert.assertEquals(costCalculator.getCurrentTraitsPoints(), 10);
        Assert.assertEquals(costCalculator.getCurrentTraitsExtraPoints(), 5);
        player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("advisor", player.getLanguage(),
                player.getModuleName()));
        Assert.assertEquals(costCalculator.getCurrentTraitsPoints(), 10);
        Assert.assertEquals(costCalculator.getCurrentTraitsExtraPoints(), 10);
    }

    @Test
    public void checkCyberneticsCost()
            throws InvalidXmlElementException, TooManyCyberneticDevicesException, RequiredCyberneticDevicesException {
        final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        player.getInfo().setAge(31);
        player.setRace(RaceFactory.getInstance().getElement("human", LANGUAGE, MODULE));
        player.setFaction(FactionsFactory.getInstance().getElement("hazat", LANGUAGE, MODULE));

        CostCalculator costCalculator = new CostCalculator(player);

        SelectedCyberneticDevice engineersEye = player.addCybernetics(CyberneticDeviceFactory.getInstance()
                .getElement("engineersEye", player.getLanguage(), player.getModuleName()));
        engineersEye.addCustomization(CyberneticDeviceTraitFactory.getInstance().getElement("hidden",
                player.getLanguage(), player.getModuleName()));
        player.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("jonah", player.getLanguage(),
                player.getModuleName()));
    }

    @Test
    public void checkEquipmentCost()
            throws InvalidXmlElementException, BeneficeAlreadyAddedException {
        final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        player.getInfo().setAge(31);
        player.setRace(RaceFactory.getInstance().getElement("human", LANGUAGE, MODULE));
        player.setFaction(FactionsFactory.getInstance().getElement("hazat", LANGUAGE, MODULE));
        player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("cash [firebirds2000]", LANGUAGE,
                PathManager.DEFAULT_MODULE_FOLDER));

        CostCalculator costCalculator = new CostCalculator(player);

        float money = costCalculator.getFireBirdsExpend();

        // Mace costs 10 fbs.
        player.addWeapon(WeaponFactory.getInstance().getElement("mace", player.getLanguage(), player.getModuleName()));
        Assert.assertEquals(player.getInitialMoney() - costCalculator.getFireBirdsExpend(), 1990f);
        Assert.assertEquals(player.getMoney(), 1990);
        // Martech costs 300 fbs.
        player.addWeapon(
                WeaponFactory.getInstance().getElement("martechGold", player.getLanguage(), player.getModuleName()));
        Assert.assertEquals(player.getInitialMoney() - costCalculator.getFireBirdsExpend(), 1690f);
        Assert.assertEquals(player.getMoney(), 1690);
        // Synthsilk costs 300 fbs.
        player.setArmour(
                ArmourFactory.getInstance().getElement("synthsilk", player.getLanguage(), player.getModuleName()));
        Assert.assertEquals(player.getInitialMoney() - costCalculator.getFireBirdsExpend(), 1390f);
        Assert.assertEquals(player.getMoney(), 1390);

        // Add 3000 fbs more.
        player.removeBenefice(AvailableBeneficeFactory.getInstance().getElement("cash [firebirds2000]", LANGUAGE,
                PathManager.DEFAULT_MODULE_FOLDER));
        player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("cash [firebirds5000]", LANGUAGE,
                PathManager.DEFAULT_MODULE_FOLDER));

        // Synthsilk costs 3000 fbs.
        player.setShield(
                ShieldFactory.getInstance().getElement("assaultShield", player.getLanguage(), player.getModuleName()));
        Assert.assertEquals(player.getInitialMoney() - costCalculator.getFireBirdsExpend(), 1390f);
        Assert.assertEquals(player.getMoney(), 1390);

        //Remove equipment
        player.setArmour(null);
        player.setShield(null);
        player.setWeapons(new ArrayList<>());

        Assert.assertEquals(costCalculator.getFireBirdsExpend(), money);
        Assert.assertEquals(player.getMoney(), 5000);
    }

    @Test
    public void checkRaceCosts() throws InvalidXmlElementException {
        final CharacterPlayer character = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        character.setFaction(FactionsFactory.getInstance().getElement("vorox", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        character.setRace(RaceFactory.getInstance().getElement("vorox", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));

        CostCalculator costCalculator = new CostCalculator(character);
        Assert.assertEquals(FreeStyleCharacterCreation.getCharacteristicsPoints(character.getInfo().getAge()) - costCalculator.getCurrentCharacteristicPoints(), 20);
        Assert.assertEquals(FreeStyleCharacterCreation.getSkillsPoints(character.getInfo().getAge()) - costCalculator.getCurrentSkillsPoints(), 30);
        Assert.assertEquals(FreeStyleCharacterCreation.getTraitsPoints(character.getInfo().getAge()) - costCalculator.getCurrentTraitsPoints(), 10);
        //Race costs 9 points
        Assert.assertEquals(FreeStyleCharacterCreation.getFreeAvailablePoints(character.getInfo().getAge(), character.getRace())
                - Math.max(0, costCalculator.getTotalExtraCost()), 31);
    }

}
