package com.softwaremagico.tm.export.pdf;

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

import com.lowagie.text.DocumentException;
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
import com.softwaremagico.tm.character.creation.CostCalculator;
import com.softwaremagico.tm.character.cybernetics.CyberneticDeviceFactory;
import com.softwaremagico.tm.character.cybernetics.RequiredCyberneticDevicesException;
import com.softwaremagico.tm.character.cybernetics.TooManyCyberneticDevicesException;
import com.softwaremagico.tm.character.equipment.armours.ArmourFactory;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.exceptions.RestrictedElementException;
import com.softwaremagico.tm.character.exceptions.UnofficialElementNotAllowedException;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.character.planets.PlanetFactory;
import com.softwaremagico.tm.character.races.RaceFactory;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.character.skills.InvalidRanksException;
import com.softwaremagico.tm.character.xp.ElementCannotBeUpgradeWithExperienceException;
import com.softwaremagico.tm.character.xp.NotEnoughExperienceException;
import com.softwaremagico.tm.file.PathManager;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.pdf.complete.CharacterSheet;
import com.softwaremagico.tm.pdf.small.SmallCharacterSheet;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

@Test(groups = {"characterPdfGeneration"})
public class CharacterSheetCreationTest {
    private static final String PDF_PATH_OUTPUT = System.getProperty("java.io.tmpdir") + File.separator;
    private static final String LANGUAGE = "es";

    private CharacterPlayer player;

    @AfterMethod
    public void clearCache() {
        LanguagePool.clearCache();
    }

    @Test
    public void emptyPdfSpanish() throws DocumentException, IOException {
        CacheHandler.clearCache();
        final CharacterSheet sheet = new CharacterSheet(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        Assert.assertEquals(sheet.createFile(PDF_PATH_OUTPUT + "FadingSuns_ES.pdf"), 2);
    }

    @Test
    public void emptyPdfEnglish() throws DocumentException, IOException {
        CacheHandler.clearCache();
        final CharacterSheet sheet = new CharacterSheet("en", PathManager.DEFAULT_MODULE_FOLDER);
        Assert.assertEquals(sheet.createFile(PDF_PATH_OUTPUT + "FadingSuns_EN.pdf"), 2);
    }

    @Test
    public void emptyPdfSmallEn() throws InvalidXmlElementException {
        final SmallCharacterSheet sheet = new SmallCharacterSheet("en", PathManager.DEFAULT_MODULE_FOLDER);
        Assert.assertEquals(
                sheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "FadingSuns_Small_EN.pdf"), 1);
    }

    @Test
    public void emptyPdfSmallEs() {
        final SmallCharacterSheet sheet = new SmallCharacterSheet("es", PathManager.DEFAULT_MODULE_FOLDER);
        Assert.assertEquals(
                sheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "FadingSuns_Small_ES.pdf"), 1);
    }

    @Test
    public void characterPdfSpanish() throws DocumentException, InvalidXmlElementException, TooManyBlessingsException,
            TooManyCyberneticDevicesException, RequiredCyberneticDevicesException, BlessingAlreadyAddedException,
            BeneficeAlreadyAddedException, NotEnoughExperienceException, ElementCannotBeUpgradeWithExperienceException,
            InvalidRanksException, RestrictedElementException, UnofficialElementNotAllowedException {
        CacheHandler.clearCache();

        this.player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        this.player.getInfo().addName(new Name("John", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER, Gender.MALE, null));
        this.player.getInfo().setSurname(new Surname("Sephard", PathManager.DEFAULT_MODULE_FOLDER, LANGUAGE, null));
        this.player.getInfo().setPlayer("Player 1");
        this.player.getInfo().setGender(Gender.MALE);
        this.player.getInfo().setAge(30);
        this.player.setRace(RaceFactory.getInstance().getElement("human", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        this.player.getInfo().setPlanet(
                PlanetFactory.getInstance().getElement("sutek", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        this.player.setFaction(
                FactionsFactory.getInstance().getElement("hazat", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));

        this.player.getInfo().setBirthdate("4996-09-16");
        this.player.getInfo().setHair("Moreno");
        this.player.getInfo().setEyes("Marrones");
        this.player.getInfo().setComplexion("Delgado");
        this.player.getInfo().setHeight("1,76m");
        this.player.getInfo().setWeight("78kg");

        // Min DEX is 3!
        this.player.setCharacteristic(CharacteristicName.STRENGTH, 3);
        this.player.setCharacteristic(CharacteristicName.DEXTERITY, 2);
        this.player.setCharacteristic(CharacteristicName.ENDURANCE, 3);
        this.player.setCharacteristic(CharacteristicName.WITS, 4);
        this.player.setCharacteristic(CharacteristicName.PERCEPTION, 5);
        this.player.setCharacteristic(CharacteristicName.TECH, 6);
        this.player.setCharacteristic(CharacteristicName.PRESENCE, 7);
        this.player.setCharacteristic(CharacteristicName.WILL, 8);
        this.player.setCharacteristic(CharacteristicName.FAITH, 9);

        this.player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("influence", LANGUAGE,
                PathManager.DEFAULT_MODULE_FOLDER), 5);
        this.player.setSkillRank(
                AvailableSkillsFactory.getInstance().getElement("sneak", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER),
                4);
        this.player.setSkillRank(
                AvailableSkillsFactory.getInstance().getElement("gaming", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER),
                4);
        this.player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("lockpicking", LANGUAGE,
                PathManager.DEFAULT_MODULE_FOLDER), 6);
        this.player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("energyGuns", LANGUAGE,
                PathManager.DEFAULT_MODULE_FOLDER), 6);
        this.player.setSkillRank(
                AvailableSkillsFactory.getInstance().getElement("warfare", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER),
                8);
        this.player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("lore", "jumpwebLore", LANGUAGE,
                PathManager.DEFAULT_MODULE_FOLDER), 4);
        this.player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("lore", "beastsLore", LANGUAGE,
                PathManager.DEFAULT_MODULE_FOLDER), 2);

        this.player.setOccultismLevel(
                OccultismTypeFactory.getPsi(this.player.getLanguage(), this.player.getModuleName()), 4);
        this.player.setDarkSideLevel(
                OccultismTypeFactory.getPsi(this.player.getLanguage(), this.player.getModuleName()), 1);

        this.player.addOccultismPower(
                OccultismPathFactory.getInstance().getElement("farHand", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                        .getOccultismPowers().get("liftingHand"));
        this.player.addOccultismPower(
                OccultismPathFactory.getInstance().getElement("farHand", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                        .getOccultismPowers().get("throwingHand"));
        this.player.addOccultismPower(
                OccultismPathFactory.getInstance().getElement("sixthSense", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                        .getOccultismPowers().get("sensitivity"));
        this.player.addOccultismPower(
                OccultismPathFactory.getInstance().getElement("soma", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                        .getOccultismPowers().get("toughening"));
        this.player.addOccultismPower(
                OccultismPathFactory.getInstance().getElement("soma", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                        .getOccultismPowers().get("strengthening"));
        this.player.addOccultismPower(
                OccultismPathFactory.getInstance().getElement("soma", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                        .getOccultismPowers().get("quickening"));
        this.player.addOccultismPower(OccultismPathFactory.getInstance()
                .getElement("soma", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getOccultismPowers().get("hardening"));

        this.player.addBlessing(BlessingFactory.getInstance().getElement("curious", this.player.getLanguage(),
                this.player.getModuleName()));
        this.player.addBlessing(BlessingFactory.getInstance().getElement("limp", this.player.getLanguage(),
                this.player.getModuleName()));
        this.player.addBlessing(BlessingFactory.getInstance().getElement("missingEye", this.player.getLanguage(),
                this.player.getModuleName()));
        this.player.addBlessing(BlessingFactory.getInstance().getElement("incurableDisease", this.player.getLanguage(),
                this.player.getModuleName()));

        this.player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("stigma_1", this.player.getLanguage(),
                this.player.getModuleName()));
        this.player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("heir", this.player.getLanguage(),
                this.player.getModuleName()));
        this.player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("wireblade",
                this.player.getLanguage(), this.player.getModuleName()));
        this.player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("pistola", this.player.getLanguage(),
                this.player.getModuleName()));
        this.player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("shaidan", this.player.getLanguage(),
                this.player.getModuleName()));

        this.player.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("engineersEye", LANGUAGE,
                PathManager.DEFAULT_MODULE_FOLDER));
        this.player.addCybernetics(
                CyberneticDeviceFactory.getInstance().getElement("jonah", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));

        this.player
                .addWeapon(WeaponFactory.getInstance().getElement("mace", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        this.player.addWeapon(WeaponFactory.getInstance().getElement("nitobiBlasterAxe", LANGUAGE,
                PathManager.DEFAULT_MODULE_FOLDER));

        this.player.setArmour(
                ArmourFactory.getInstance().getElement("synthsilk", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));

        this.player.setShield(
                ShieldFactory.getInstance().getElement("assaultShield", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));

        // XP tests
        this.player.setExperienceEarned(60);
        this.player.setExperienceIncreasedRanks(this.player.getCharacteristic(CharacteristicName.STRENGTH), 1);
        Assert.assertEquals((int) this.player.getValue(CharacteristicName.STRENGTH), 4);
        this.player.setExperienceIncreasedRanks(AvailableSkillsFactory.getInstance().getElement("influence", LANGUAGE,
                PathManager.DEFAULT_MODULE_FOLDER), 2);
        Assert.assertEquals((int) this.player.getSkillTotalRanks(AvailableSkillsFactory.getInstance()
                .getElement("influence", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)), 7);

        LanguagePool.clearCache();
        final CharacterSheet sheet = new CharacterSheet(this.player);
        Assert.assertEquals(sheet.createFile(PDF_PATH_OUTPUT + "CharacterFS_ES.pdf"), 2);

        Assert.assertEquals(CostCalculator.getCost(this.player), 53);
    }
}
