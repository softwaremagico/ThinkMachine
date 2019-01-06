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
import java.util.HashSet;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.itextpdf.text.DocumentException;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.Gender;
import com.softwaremagico.tm.character.Name;
import com.softwaremagico.tm.character.Surname;
import com.softwaremagico.tm.character.benefices.AvailableBeneficeFactory;
import com.softwaremagico.tm.character.blessings.BlessingFactory;
import com.softwaremagico.tm.character.blessings.TooManyBlessingsException;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.creation.CostCalculator;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.character.cybernetics.CyberneticDeviceFactory;
import com.softwaremagico.tm.character.cybernetics.CyberneticDeviceTraitFactory;
import com.softwaremagico.tm.character.cybernetics.RequiredCyberneticDevicesException;
import com.softwaremagico.tm.character.cybernetics.SelectedCyberneticDevice;
import com.softwaremagico.tm.character.cybernetics.TooManyCyberneticDevicesException;
import com.softwaremagico.tm.character.equipment.DamageType;
import com.softwaremagico.tm.character.equipment.armours.Armour;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.character.planets.PlanetFactory;
import com.softwaremagico.tm.character.races.Race;
import com.softwaremagico.tm.character.races.RaceFactory;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.pdf.complete.CharacterSheet;
import com.softwaremagico.tm.pdf.small.SmallCharacterSheet;

@Test(groups = { "customCharacterGeneration" })
public class CustomCharacters {
	private final static String LANGUAGE = "en";

	@AfterMethod
	public void clearCache() {
		LanguagePool.clearCache();
	}

	@Test
	public void createPaolaCharacter() throws MalformedURLException, DocumentException, IOException, InvalidXmlElementException, TooManyBlessingsException,
			TooManyCyberneticDevicesException, RequiredCyberneticDevicesException {
		CharacterPlayer player = new CharacterPlayer(LANGUAGE);
		player.getInfo().addName(new Name("#5", LANGUAGE, Gender.FEMALE, null));
		player.getInfo().setPlayer("Paola");
		player.getInfo().setGender(Gender.FEMALE);
		player.getInfo().setAge(35);
		player.setRace(RaceFactory.getInstance().getElement("human", LANGUAGE));
		player.getInfo().setPlanet(PlanetFactory.getInstance().getElement("leagueheim", LANGUAGE));
		player.setFaction(FactionsFactory.getInstance().getElement("engineers", LANGUAGE));

		player.getCharacteristic(CharacteristicName.STRENGTH).setValue(3);
		player.getCharacteristic(CharacteristicName.DEXTERITY).setValue(7);
		player.getCharacteristic(CharacteristicName.ENDURANCE).setValue(5);
		player.getCharacteristic(CharacteristicName.WITS).setValue(8);
		player.getCharacteristic(CharacteristicName.PERCEPTION).setValue(6);
		player.getCharacteristic(CharacteristicName.TECH).setValue(8);
		player.getCharacteristic(CharacteristicName.PRESENCE).setValue(3);
		player.getCharacteristic(CharacteristicName.WILL).setValue(5);
		player.getCharacteristic(CharacteristicName.FAITH).setValue(3);

		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("influence", LANGUAGE), 4);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("observe", LANGUAGE), 5);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("sneak", LANGUAGE), 6);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("vigor", LANGUAGE), 5);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("lockpicking", LANGUAGE), 2);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("energyGuns", LANGUAGE), 5);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("appliedScience", LANGUAGE), 4);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("physicalScience", LANGUAGE), 1);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("spacecraft", LANGUAGE), 2);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("landcarft", LANGUAGE), 3);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("melee", LANGUAGE), 1);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("empathy", LANGUAGE), 3);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("warfare", LANGUAGE), 1);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("investigation", LANGUAGE), 2);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("spacecraftOperations", LANGUAGE), 2);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("thinkMachine", LANGUAGE), 4);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("techRedemption", LANGUAGE), 7);

		player.addBlessing(BlessingFactory.getInstance().getElement("haughty", LANGUAGE));
		player.addBlessing(BlessingFactory.getInstance().getElement("hacker", player.getLanguage()));
		player.addBlessing(BlessingFactory.getInstance().getElement("greaseMonkey", player.getLanguage()));
		player.addBlessing(BlessingFactory.getInstance().getElement("horribleScar", player.getLanguage()));

		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("language [turingLanguage]", player.getLanguage()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("commission [apprentice]", player.getLanguage()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("cash [firebirds1000]", player.getLanguage()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("gossipNetwork_3", player.getLanguage()));

		SelectedCyberneticDevice advancedEngineersEye = player.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("engineersEye", LANGUAGE));
		advancedEngineersEye.addCustomization(CyberneticDeviceTraitFactory.getInstance().getElement("hidden", LANGUAGE));

		player.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("secondBrain", LANGUAGE));
		player.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("secondBrainJumpLoreSoftware", LANGUAGE));
		player.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("secondBrainEnergyPistolsLore", LANGUAGE));
		player.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("secondBrainThinkMachineLore", LANGUAGE));

		player.setShield(ShieldFactory.getInstance().getElement("duelingShield", LANGUAGE));

		LanguagePool.clearCache();
		CharacterSheet sheet = new CharacterSheet(player);
		sheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "Paola.pdf");

		LanguagePool.clearCache();
		SmallCharacterSheet smallSheet = new SmallCharacterSheet(player);
		smallSheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "Paola_Small.pdf");

		Assert.assertEquals(CostCalculator.getCost(player), FreeStyleCharacterCreation.getFreeAvailablePoints(player.getInfo().getAge()));
		Assert.assertEquals(player.getMoney(), 300);
	}

	@Test
	public void characterAnaCharacter() throws MalformedURLException, DocumentException, IOException, InvalidXmlElementException, TooManyBlessingsException {
		CharacterPlayer player = new CharacterPlayer(LANGUAGE);
		player.getInfo().addName(new Name("Arya", LANGUAGE, Gender.FEMALE, null));
		player.getInfo().setSurname(new Surname("Hawkwood", LANGUAGE, null));
		player.getInfo().setPlayer("Ana");
		player.getInfo().setGender(Gender.FEMALE);
		player.getInfo().setAge(32);
		player.setRace(RaceFactory.getInstance().getElement("human", LANGUAGE));
		player.getInfo().setPlanet(PlanetFactory.getInstance().getElement("leminkainen", LANGUAGE));
		player.setFaction(FactionsFactory.getInstance().getElement("hawkwood", LANGUAGE));

		player.getCharacteristic(CharacteristicName.STRENGTH).setValue(5);
		player.getCharacteristic(CharacteristicName.DEXTERITY).setValue(7);
		player.getCharacteristic(CharacteristicName.ENDURANCE).setValue(5);
		player.getCharacteristic(CharacteristicName.WITS).setValue(8);
		player.getCharacteristic(CharacteristicName.PERCEPTION).setValue(7);
		player.getCharacteristic(CharacteristicName.TECH).setValue(5);
		player.getCharacteristic(CharacteristicName.PRESENCE).setValue(7);
		player.getCharacteristic(CharacteristicName.WILL).setValue(5);
		player.getCharacteristic(CharacteristicName.FAITH).setValue(4);

		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("influence", LANGUAGE), 7);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("observe", LANGUAGE), 8);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("sneak", LANGUAGE), 8);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("vigor", LANGUAGE), 8);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("lockpicking", LANGUAGE), 4);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("arts", "drawing", LANGUAGE), 1);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("selfControl", LANGUAGE), 2);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("streetWise", LANGUAGE), 3);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("physick", LANGUAGE), 1);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("watercraft", LANGUAGE), 1);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("artifactMelee", LANGUAGE), 4);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("etiquette", LANGUAGE), 4);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("investigation", LANGUAGE), 4);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("leadership", LANGUAGE), 2);

		player.addBlessing(BlessingFactory.getInstance().getElement("horribleScar", player.getLanguage()));

		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("nobility [knight]", player.getLanguage()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("language [latinLanguage]", player.getLanguage()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("imperialCharter", player.getLanguage()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("fluxSword", player.getLanguage()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("vendetta_2", player.getLanguage()));

		player.addWeapon(WeaponFactory.getInstance().getElement("fluxSword", LANGUAGE));

		LanguagePool.clearCache();
		CharacterSheet sheet = new CharacterSheet(player);
		sheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "Ana.pdf");

		LanguagePool.clearCache();
		SmallCharacterSheet smallSheet = new SmallCharacterSheet(player);
		smallSheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "Ana_Small.pdf");

		Assert.assertEquals(CostCalculator.getCost(player), FreeStyleCharacterCreation.getFreeAvailablePoints(player.getInfo().getAge()));
		Assert.assertEquals(player.getMoney(), 250);
	}

	@Test
	public void createCarlosCharacter() throws MalformedURLException, DocumentException, IOException, InvalidXmlElementException, TooManyBlessingsException {
		CharacterPlayer player = new CharacterPlayer(LANGUAGE);
		player.getInfo().addName(new Name("Carlos", LANGUAGE, Gender.MALE, null));
		player.getInfo().setPlayer("Carlos");
		player.getInfo().setGender(Gender.MALE);
		player.getInfo().setAge(38);
		player.setRace(RaceFactory.getInstance().getElement("human", LANGUAGE));
		player.getInfo().setPlanet(PlanetFactory.getInstance().getElement("byzantiumSecundus", LANGUAGE));
		player.setFaction(FactionsFactory.getInstance().getElement("scravers", LANGUAGE));

		player.getCharacteristic(CharacteristicName.STRENGTH).setValue(7);
		player.getCharacteristic(CharacteristicName.DEXTERITY).setValue(8);
		player.getCharacteristic(CharacteristicName.ENDURANCE).setValue(6);
		player.getCharacteristic(CharacteristicName.WITS).setValue(6);
		player.getCharacteristic(CharacteristicName.PERCEPTION).setValue(6);
		player.getCharacteristic(CharacteristicName.TECH).setValue(6);
		player.getCharacteristic(CharacteristicName.PRESENCE).setValue(4);
		player.getCharacteristic(CharacteristicName.WILL).setValue(6);
		player.getCharacteristic(CharacteristicName.FAITH).setValue(3);

		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("influence", LANGUAGE), 5);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("observe", LANGUAGE), 4);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("vigor", LANGUAGE), 7);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("slugGuns", LANGUAGE), 8);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("knavery", LANGUAGE), 4);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("selfControl", LANGUAGE), 2);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("streetWise", LANGUAGE), 3);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("physick", LANGUAGE), 2);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("aircraft", LANGUAGE), 1);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("fight", LANGUAGE), 5);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("demolitions", LANGUAGE), 2);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("gaming", LANGUAGE), 1);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("leadership", LANGUAGE), 1);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("techRedemption", LANGUAGE), 3);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("survival", LANGUAGE), 3);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("torture", LANGUAGE), 3);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("landcarft", LANGUAGE), 2);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("investigation", LANGUAGE), 3);

		player.addBlessing(BlessingFactory.getInstance().getElement("horribleScar", player.getLanguage()));

		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("commission [entered]", player.getLanguage()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("passageContracts_3", player.getLanguage()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("cash [firebirds1000]", player.getLanguage()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("ironHeel", player.getLanguage()));

		player.addWeapon(WeaponFactory.getInstance().getElement("typicalShotgun", LANGUAGE));
		player.addWeapon(WeaponFactory.getInstance().getElement("mediumAutofeedHandgun", LANGUAGE));

		LanguagePool.clearCache();
		CharacterSheet sheet = new CharacterSheet(player);
		sheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "Carlos.pdf");

		LanguagePool.clearCache();
		SmallCharacterSheet smallSheet = new SmallCharacterSheet(player);
		smallSheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "Carlos_Small.pdf");

		Assert.assertEquals(CostCalculator.getCost(player), FreeStyleCharacterCreation.getFreeAvailablePoints(player.getInfo().getAge()));
		Assert.assertEquals(player.getRank(), "Genin");
		Assert.assertEquals(500, player.getMoney());
	}

	@Test
	public void createNoeliaCharacer() throws MalformedURLException, DocumentException, IOException, InvalidXmlElementException, TooManyBlessingsException {
		CharacterPlayer player = new CharacterPlayer(LANGUAGE);
		player.getInfo().addName(new Name("Noelia", LANGUAGE, Gender.FEMALE, null));
		player.getInfo().setPlayer("Noelia");
		player.getInfo().setGender(Gender.FEMALE);
		player.getInfo().setAge(31);
		player.setRace(RaceFactory.getInstance().getElement("obun", LANGUAGE));
		player.getInfo().setPlanet(PlanetFactory.getInstance().getElement("velsimil", LANGUAGE));
		player.setFaction(FactionsFactory.getInstance().getElement("obun", LANGUAGE));

		player.getCharacteristic(CharacteristicName.STRENGTH).setValue(3);
		player.getCharacteristic(CharacteristicName.DEXTERITY).setValue(6);
		player.getCharacteristic(CharacteristicName.ENDURANCE).setValue(3);
		player.getCharacteristic(CharacteristicName.WITS).setValue(8);
		player.getCharacteristic(CharacteristicName.PERCEPTION).setValue(4);
		player.getCharacteristic(CharacteristicName.TECH).setValue(3);
		player.getCharacteristic(CharacteristicName.PRESENCE).setValue(7);
		player.getCharacteristic(CharacteristicName.WILL).setValue(8);
		player.getCharacteristic(CharacteristicName.FAITH).setValue(8);

		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("influence", LANGUAGE), 4);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("observe", LANGUAGE), 6);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("vigor", LANGUAGE), 5);

		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("knavery", LANGUAGE), 2);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("selfControl", LANGUAGE), 6);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("physick", LANGUAGE), 3);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("empathy", LANGUAGE), 4);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("etiquette", LANGUAGE), 2);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("investigation", LANGUAGE), 3);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("lore", "kelantiLore", LANGUAGE), 3);

		player.setPsiqueLevel(OccultismTypeFactory.getPsi(player.getLanguage()), 6);

		player.addOccultismPower(OccultismPathFactory.getInstance().getElement("farHand", LANGUAGE).getOccultismPowers().get("liftingHand"));
		player.addOccultismPower(OccultismPathFactory.getInstance().getElement("farHand", LANGUAGE).getOccultismPowers().get("throwingHand"));
		player.addOccultismPower(OccultismPathFactory.getInstance().getElement("farHand", LANGUAGE).getOccultismPowers().get("crushingHand"));
		player.addOccultismPower(OccultismPathFactory.getInstance().getElement("farHand", LANGUAGE).getOccultismPowers().get("duelingHand"));
		player.addOccultismPower(OccultismPathFactory.getInstance().getElement("farHand", LANGUAGE).getOccultismPowers().get("farWall"));
		player.addOccultismPower(OccultismPathFactory.getInstance().getElement("psyche", LANGUAGE).getOccultismPowers().get("intuit"));
		player.addOccultismPower(OccultismPathFactory.getInstance().getElement("psyche", LANGUAGE).getOccultismPowers().get("emote"));
		player.addOccultismPower(OccultismPathFactory.getInstance().getElement("psyche", LANGUAGE).getOccultismPowers().get("mindSight"));
		player.setExtraWyrd(3);

		player.addBlessing(BlessingFactory.getInstance().getElement("horribleScar", player.getLanguage()));

		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("ordained [novitiate]", player.getLanguage()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("language [urthish]", player.getLanguage()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("stigma_3", player.getLanguage()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("orphan", player.getLanguage()));

		LanguagePool.clearCache();
		CharacterSheet sheet = new CharacterSheet(player);
		sheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "Noelia.pdf");

		LanguagePool.clearCache();
		SmallCharacterSheet smallSheet = new SmallCharacterSheet(player);
		smallSheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "Noelia_Small.pdf");

		Assert.assertEquals(CostCalculator.getCost(player), FreeStyleCharacterCreation.getFreeAvailablePoints(player.getInfo().getAge()));
	}

	@Test
	public void createGolemCharacer() throws MalformedURLException, DocumentException, IOException, InvalidXmlElementException, TooManyBlessingsException {
		CharacterPlayer player = new CharacterPlayer(LANGUAGE);
		player.getInfo().setPlayer("PNJ");
		player.getInfo().addName(new Name("A", LANGUAGE, Gender.FEMALE, null));
		player.getInfo().setSurname(new Surname("(Prototipo A)", LANGUAGE, null));
		player.getInfo().setGender(Gender.FEMALE);
		player.setRace(new Race("Gólem", LANGUAGE, 5, 5, 5, 3, 3, 6, 0, 0, 0, 6, 0, 0, 0, 0, 0));
		player.getInfo().setPlanet(PlanetFactory.getInstance().getElement("leagueheim", LANGUAGE));
		player.setFaction(FactionsFactory.getInstance().getElement("engineers", LANGUAGE));
		player.getInfo().setAge(32);

		player.getCharacteristic(CharacteristicName.STRENGTH).setValue(12);
		player.getCharacteristic(CharacteristicName.DEXTERITY).setValue(7);
		player.getCharacteristic(CharacteristicName.ENDURANCE).setValue(10);
		player.getCharacteristic(CharacteristicName.WITS).setValue(5);
		player.getCharacteristic(CharacteristicName.PERCEPTION).setValue(7);
		player.getCharacteristic(CharacteristicName.TECH).setValue(5);

		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("observe", LANGUAGE), 6);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("fight", LANGUAGE), 5);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("vigor", LANGUAGE), 7);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("throwing", LANGUAGE), 5);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("athletics", LANGUAGE), 4);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("selfControl", LANGUAGE), 5);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("spacecraft", LANGUAGE), 1);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("landcarft", LANGUAGE), 3);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("spacecraftOperations", LANGUAGE), 3);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("warfare", LANGUAGE), 3);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("lore", "jumpwebLore", LANGUAGE), 3);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("lore", "thinkMachineLore", LANGUAGE), 3);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("thinkMachine", LANGUAGE), 3);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("techRedemption", LANGUAGE), 3);

		player.addBlessing(BlessingFactory.getInstance().getElement("gullible", player.getLanguage()));
		player.addBlessing(BlessingFactory.getInstance().getElement("righteous", player.getLanguage()));

		player.setArmour(new Armour("skin", "Piel", LANGUAGE, 5, 2, new HashSet<DamageType>(), 0));

		LanguagePool.clearCache();
		CharacterSheet sheet = new CharacterSheet(player);
		sheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "Golem.pdf");

		Assert.assertEquals(CostCalculator.getCost(player), -5);
		Assert.assertEquals(player.getMoney(), 250);
	}
}
