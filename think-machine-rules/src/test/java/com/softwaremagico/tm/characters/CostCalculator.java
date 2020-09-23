package com.softwaremagico.tm.characters;

import com.softwaremagico.tm.CacheHandler;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.Gender;
import com.softwaremagico.tm.character.Name;
import com.softwaremagico.tm.character.Surname;
import com.softwaremagico.tm.character.benefices.AvailableBeneficeFactory;
import com.softwaremagico.tm.character.benefices.BeneficeAlreadyAddedException;
import com.softwaremagico.tm.character.blessings.BlessingAlreadyAddedException;
import com.softwaremagico.tm.character.blessings.BlessingFactory;
import com.softwaremagico.tm.character.blessings.TooManyBlessingsException;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.cybernetics.*;
import com.softwaremagico.tm.character.equipment.armours.ArmourFactory;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.character.planets.PlanetFactory;
import com.softwaremagico.tm.character.races.RaceFactory;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.file.PathManager;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"costCalculator"})
public class CostCalculator {
    private static final String LANGUAGE = "es";
    private static final String MODULE = PathManager.DEFAULT_MODULE_FOLDER;

    @Test
    public void createCharacter()
            throws InvalidXmlElementException, TooManyBlessingsException, TooManyCyberneticDevicesException,
            RequiredCyberneticDevicesException, BlessingAlreadyAddedException, BeneficeAlreadyAddedException {
        CacheHandler.clearCache();
        final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        player.getInfo().addName(new Name("Oliver", LANGUAGE, MODULE, Gender.MALE, null));
        player.getInfo().setSurname(new Surname("Queen", LANGUAGE, MODULE, null));
        player.getInfo().setPlayer("Player 1");
        player.getInfo().setGender(Gender.MALE);
        player.getInfo().setAge(31);
        player.setRace(RaceFactory.getInstance().getElement("human", LANGUAGE, MODULE));
        player.getInfo().setPlanet(PlanetFactory.getInstance().getElement("sutek", LANGUAGE, MODULE));
        player.setFaction(FactionsFactory.getInstance().getElement("hazat", LANGUAGE, MODULE));

        player.getInfo().setBirthdate("4996-09-16");
        player.getInfo().setHair("Moreno");
        player.getInfo().setEyes("Marrones");
        player.getInfo().setComplexion("Delgado");
        player.getInfo().setHeight("1,76m");
        player.getInfo().setWeight("78kg");

        com.softwaremagico.tm.character.creation.CostCalculator costCalculator = new com.softwaremagico.tm.character.creation.CostCalculator(player);

        //Min value for STG and DEX is 3!
        player.setCharacteristic(CharacteristicName.STRENGTH, 2);
        player.setCharacteristic(CharacteristicName.DEXTERITY, 2);
        player.setCharacteristic(CharacteristicName.ENDURANCE, 3);
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

        player.setPsiqueLevel(OccultismTypeFactory.getPsi(LANGUAGE, MODULE), 4);
        player.setDarkSideLevel(OccultismTypeFactory.getPsi(LANGUAGE, MODULE), 1);

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

        player.addBlessing(
                BlessingFactory.getInstance().getElement("handsome", player.getLanguage(), player.getModuleName()));
        player.addBlessing(
                BlessingFactory.getInstance().getElement("curious", player.getLanguage(), player.getModuleName()));
        player.addBlessing(
                BlessingFactory.getInstance().getElement("missingEye", player.getLanguage(), player.getModuleName()));
        player.addBlessing(
                BlessingFactory.getInstance().getElement("luckyAtCards", player.getLanguage(), player.getModuleName()));

        player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("stigma_1", player.getLanguage(),
                player.getModuleName()));
        player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("heir", player.getLanguage(),
                player.getModuleName()));
        player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("wireblade", player.getLanguage(),
                player.getModuleName()));
        player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("pistola", player.getLanguage(),
                player.getModuleName()));
        player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("shaidan", player.getLanguage(),
                player.getModuleName()));

        SelectedCyberneticDevice engineersEye = player.addCybernetics(CyberneticDeviceFactory.getInstance()
                .getElement("engineersEye", player.getLanguage(), player.getModuleName()));
        engineersEye.addCustomization(CyberneticDeviceTraitFactory.getInstance().getElement("hidden",
                player.getLanguage(), player.getModuleName()));
        player.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("jonah", player.getLanguage(),
                player.getModuleName()));

        player.addWeapon(WeaponFactory.getInstance().getElement("mace", player.getLanguage(), player.getModuleName()));
        player.addWeapon(
                WeaponFactory.getInstance().getElement("martechGold", player.getLanguage(), player.getModuleName()));

        player.setArmour(
                ArmourFactory.getInstance().getElement("synthsilk", player.getLanguage(), player.getModuleName()));

        player.setShield(
                ShieldFactory.getInstance().getElement("assaultShield", player.getLanguage(), player.getModuleName()));
    }
}
