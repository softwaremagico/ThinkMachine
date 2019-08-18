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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.itextpdf.text.DocumentException;
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
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.character.planets.PlanetFactory;
import com.softwaremagico.tm.character.races.RaceFactory;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.file.Path;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.pdf.complete.CharacterSheet;
import com.softwaremagico.tm.pdf.small.SmallCharacterSheet;

@Test(groups = { "characterPdfGeneration" })
public class CharacterSheetCreationTest {
	private static final String PDF_PATH_OUTPUT = System.getProperty("java.io.tmpdir") + File.separator;
	private static final String LANGUAGE = "es";

	private CharacterPlayer player;

	@AfterMethod
	public void clearCache() {
		LanguagePool.clearCache();
	}

	@Test
	public void emptyPdfSpanish() throws MalformedURLException, DocumentException, IOException {
		CacheHandler.clearCache();
		final CharacterSheet sheet = new CharacterSheet(LANGUAGE, Path.DEFAULT_MODULE_FOLDER);
		Assert.assertEquals(sheet.createFile(PDF_PATH_OUTPUT + "FadingSuns_ES.pdf"), 2);
	}

	@Test
	public void emptyPdfEnglish() throws MalformedURLException, DocumentException, IOException {
		CacheHandler.clearCache();
		final CharacterSheet sheet = new CharacterSheet("en", Path.DEFAULT_MODULE_FOLDER);
		Assert.assertEquals(sheet.createFile(PDF_PATH_OUTPUT + "FadingSuns_EN.pdf"), 2);
	}

	@Test
	public void emptyPdfSmallEn() throws InvalidXmlElementException {
		final SmallCharacterSheet sheet = new SmallCharacterSheet("en", Path.DEFAULT_MODULE_FOLDER);
		Assert.assertEquals(
				sheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "FadingSuns_Small_EN.pdf"), 1);
	}

	@Test
	public void emptyPdfSmallEs() {
		final SmallCharacterSheet sheet = new SmallCharacterSheet("es", Path.DEFAULT_MODULE_FOLDER);
		Assert.assertEquals(
				sheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "FadingSuns_Small_ES.pdf"), 1);
	}

	@Test
	public void characterPdfSpanish() throws MalformedURLException, DocumentException, IOException,
			InvalidXmlElementException, TooManyBlessingsException, TooManyCyberneticDevicesException,
			RequiredCyberneticDevicesException, BlessingAlreadyAddedException, BeneficeAlreadyAddedException {
		CacheHandler.clearCache();

		player = new CharacterPlayer(LANGUAGE, Path.DEFAULT_MODULE_FOLDER);
		player.getInfo().addName(new Name("John", LANGUAGE, Path.DEFAULT_MODULE_FOLDER, Gender.MALE, null));
		player.getInfo().setSurname(new Surname("Sephard", Path.DEFAULT_MODULE_FOLDER, LANGUAGE, null));
		player.getInfo().setPlayer("Player 1");
		player.getInfo().setGender(Gender.MALE);
		player.getInfo().setAge(30);
		player.setRace(RaceFactory.getInstance().getElement("human", LANGUAGE, Path.DEFAULT_MODULE_FOLDER));
		player.getInfo().setPlanet(
				PlanetFactory.getInstance().getElement("sutek", LANGUAGE, Path.DEFAULT_MODULE_FOLDER));
		player.setFaction(FactionsFactory.getInstance().getElement("hazat", LANGUAGE, Path.DEFAULT_MODULE_FOLDER));

		player.getInfo().setBirthdate("4996-09-16");
		player.getInfo().setHair("Moreno");
		player.getInfo().setEyes("Marrones");
		player.getInfo().setComplexion("Delgado");
		player.getInfo().setHeight("1,76m");
		player.getInfo().setWeight("78kg");

		player.getCharacteristic(CharacteristicName.STRENGTH).setValue(3);
		player.getCharacteristic(CharacteristicName.DEXTERITY).setValue(2);
		player.getCharacteristic(CharacteristicName.ENDURANCE).setValue(3);
		player.getCharacteristic(CharacteristicName.WITS).setValue(4);
		player.getCharacteristic(CharacteristicName.PERCEPTION).setValue(5);
		player.getCharacteristic(CharacteristicName.TECH).setValue(6);
		player.getCharacteristic(CharacteristicName.PRESENCE).setValue(7);
		player.getCharacteristic(CharacteristicName.WILL).setValue(8);
		player.getCharacteristic(CharacteristicName.FAITH).setValue(9);

		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("influence", LANGUAGE, Path.DEFAULT_MODULE_FOLDER), 5);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("sneak", LANGUAGE, Path.DEFAULT_MODULE_FOLDER), 4);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("gaming", LANGUAGE, Path.DEFAULT_MODULE_FOLDER), 4);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("lockpicking", LANGUAGE, Path.DEFAULT_MODULE_FOLDER), 6);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("energyGuns", LANGUAGE, Path.DEFAULT_MODULE_FOLDER), 6);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("warfare", LANGUAGE, Path.DEFAULT_MODULE_FOLDER), 8);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("lore", "jumpwebLore", LANGUAGE,
						Path.DEFAULT_MODULE_FOLDER), 4);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("lore", "beastsLore", LANGUAGE,
						Path.DEFAULT_MODULE_FOLDER), 2);

		player.setPsiqueLevel(OccultismTypeFactory.getPsi(player.getLanguage(), player.getModuleName()), 4);
		player.setDarkSideLevel(OccultismTypeFactory.getPsi(player.getLanguage(), player.getModuleName()), 1);

		player.addOccultismPower(OccultismPathFactory.getInstance()
				.getElement("farHand", LANGUAGE, Path.DEFAULT_MODULE_FOLDER).getOccultismPowers().get("liftingHand"));
		player.addOccultismPower(OccultismPathFactory.getInstance()
				.getElement("farHand", LANGUAGE, Path.DEFAULT_MODULE_FOLDER).getOccultismPowers().get("throwingHand"));
		player.addOccultismPower(OccultismPathFactory.getInstance()
				.getElement("sixthSense", LANGUAGE, Path.DEFAULT_MODULE_FOLDER).getOccultismPowers().get("sensitivity"));
		player.addOccultismPower(OccultismPathFactory.getInstance()
				.getElement("soma", LANGUAGE, Path.DEFAULT_MODULE_FOLDER).getOccultismPowers().get("toughening"));
		player.addOccultismPower(OccultismPathFactory.getInstance()
				.getElement("soma", LANGUAGE, Path.DEFAULT_MODULE_FOLDER).getOccultismPowers().get("strengthening"));
		player.addOccultismPower(OccultismPathFactory.getInstance()
				.getElement("soma", LANGUAGE, Path.DEFAULT_MODULE_FOLDER).getOccultismPowers().get("quickening"));
		player.addOccultismPower(OccultismPathFactory.getInstance()
				.getElement("soma", LANGUAGE, Path.DEFAULT_MODULE_FOLDER).getOccultismPowers().get("hardening"));

		player.addBlessing(BlessingFactory.getInstance().getElement("curious", player.getLanguage(),
				player.getModuleName()));
		player.addBlessing(BlessingFactory.getInstance().getElement("limp", player.getLanguage(),
				player.getModuleName()));
		player.addBlessing(BlessingFactory.getInstance().getElement("missingEye", player.getLanguage(),
				player.getModuleName()));
		player.addBlessing(BlessingFactory.getInstance().getElement("incurableDisease", player.getLanguage(),
				player.getModuleName()));

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

		player.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("engineersEye", LANGUAGE,
				Path.DEFAULT_MODULE_FOLDER));
		player.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("jonah", LANGUAGE,
				Path.DEFAULT_MODULE_FOLDER));

		player.addWeapon(WeaponFactory.getInstance().getElement("mace", LANGUAGE, Path.DEFAULT_MODULE_FOLDER));
		player.addWeapon(WeaponFactory.getInstance().getElement("martechGold", LANGUAGE, Path.DEFAULT_MODULE_FOLDER));

		player.setArmour(ArmourFactory.getInstance().getElement("synthsilk", LANGUAGE, Path.DEFAULT_MODULE_FOLDER));

		player.setShield(ShieldFactory.getInstance().getElement("assaultShield", LANGUAGE, Path.DEFAULT_MODULE_FOLDER));

		LanguagePool.clearCache();
		final CharacterSheet sheet = new CharacterSheet(player);
		Assert.assertEquals(sheet.createFile(PDF_PATH_OUTPUT + "CharacterFS_ES.pdf"), 2);

		Assert.assertEquals(CostCalculator.getCost(player), 50);
	}
}
