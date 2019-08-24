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
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.itextpdf.text.DocumentException;
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
import com.softwaremagico.tm.file.PathManager;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.party.Party;
import com.softwaremagico.tm.pdf.complete.CharacterSheet;
import com.softwaremagico.tm.pdf.complete.PartySheet;
import com.softwaremagico.tm.pdf.small.SmallCharacterSheet;
import com.softwaremagico.tm.pdf.small.SmallPartySheet;

@Test(groups = { "customCharacterGeneration" })
public class CustomCharacters {
	private static final String LANGUAGE = "en";
	private static final String LOREM_IPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. In interdum varius rutrum. Interdum et malesuada fames ac ante ipsum primis in faucibus. Nam eu ipsum semper, tincidunt mi sit amet, feugiat neque. Nam vitae eleifend mauris, at rhoncus lacus. Maecenas non felis lectus. Vestibulum luctus tristique mauris sit amet ultrices. Vivamus molestie pharetra ante, facilisis dictum velit. Aenean a quam vitae nibh rhoncus ultricies sit amet et enim. Fusce ut arcu et libero viverra luctus quis at eros. Morbi consectetur leo eu ante posuere, at consectetur diam pretium. Quisque lacinia sagittis dolor ac pellentesque. Cras ullamcorper nulla fringilla nunc eleifend rhoncus. Suspendisse interdum elit eu elit vehicula, vel porta enim mattis. In scelerisque finibus mi vitae porta. Morbi nisl tellus, eleifend dapibus lorem vitae, rhoncus rutrum velit. Aenean sollicitudin suscipit mauris eget auctor. Nunc fermentum vehicula justo eu interdum. In porttitor eros et massa commodo viverra. Fusce in mi convallis, auctor mi vitae, rutrum nunc. Aliquam massa turpis, luctus in odio vitae, facilisis malesuada libero. Aliquam tincidunt lacus faucibus pharetra convallis. Duis non vestibulum arcu.";
	private Party party;

	@AfterMethod
	public void clearCache() {
		LanguagePool.clearCache();
	}

	@BeforeClass
	public void initialize() {
		party = new Party(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		party.setPartyName("Lost Wonderers");
	}

	@Test
	public void createPaolaCharacter() throws MalformedURLException, DocumentException, IOException,
			InvalidXmlElementException, TooManyBlessingsException, TooManyCyberneticDevicesException,
			RequiredCyberneticDevicesException, BeneficeAlreadyAddedException, BlessingAlreadyAddedException {
		final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		player.getInfo().addName(new Name("#5", player.getLanguage(), player.getModuleName(), Gender.FEMALE, null));
		player.getInfo().setPlayer("Paola");
		player.getInfo().setGender(Gender.FEMALE);
		player.getInfo().setAge(35);
		player.setRace(RaceFactory.getInstance().getElement("human", player.getLanguage(), player.getModuleName()));
		player.getInfo().setPlanet(
				PlanetFactory.getInstance().getElement("leagueheim", player.getLanguage(), player.getModuleName()));
		player.setFaction(FactionsFactory.getInstance().getElement("engineers", player.getLanguage(),
				player.getModuleName()));

		player.getCharacteristic(CharacteristicName.STRENGTH).setValue(3);
		player.getCharacteristic(CharacteristicName.DEXTERITY).setValue(7);
		player.getCharacteristic(CharacteristicName.ENDURANCE).setValue(5);
		player.getCharacteristic(CharacteristicName.WITS).setValue(8);
		player.getCharacteristic(CharacteristicName.PERCEPTION).setValue(6);
		player.getCharacteristic(CharacteristicName.TECH).setValue(8);
		player.getCharacteristic(CharacteristicName.PRESENCE).setValue(3);
		player.getCharacteristic(CharacteristicName.WILL).setValue(5);
		player.getCharacteristic(CharacteristicName.FAITH).setValue(3);

		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("influence", player.getLanguage(),
						player.getModuleName()), 4);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance()
						.getElement("observe", player.getLanguage(), player.getModuleName()), 5);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("sneak", player.getLanguage(), player.getModuleName()),
				6);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("vigor", player.getLanguage(), player.getModuleName()),
				5);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("lockpicking", player.getLanguage(),
						player.getModuleName()), 2);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("energyGuns", player.getLanguage(),
						player.getModuleName()), 5);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("appliedScience", player.getLanguage(),
						player.getModuleName()), 4);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("physicalScience", player.getLanguage(),
						player.getModuleName()), 1);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("spacecraft", player.getLanguage(),
						player.getModuleName()), 2);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("landcarft", player.getLanguage(),
						player.getModuleName()), 3);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("melee", player.getLanguage(), player.getModuleName()),
				1);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance()
						.getElement("empathy", player.getLanguage(), player.getModuleName()), 3);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance()
						.getElement("warfare", player.getLanguage(), player.getModuleName()), 1);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("investigation", player.getLanguage(),
						player.getModuleName()), 2);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("spacecraftOperations", LANGUAGE,
						PathManager.DEFAULT_MODULE_FOLDER), 2);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("thinkMachine", player.getLanguage(),
						player.getModuleName()), 4);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("techRedemption", player.getLanguage(),
						player.getModuleName()), 7);

		player.addBlessing(BlessingFactory.getInstance().getElement("haughty", player.getLanguage(),
				player.getModuleName()));
		player.addBlessing(BlessingFactory.getInstance().getElement("hacker", player.getLanguage(),
				player.getModuleName()));
		player.addBlessing(BlessingFactory.getInstance().getElement("greaseMonkey", player.getLanguage(),
				player.getModuleName()));
		player.addBlessing(BlessingFactory.getInstance().getElement("horribleScar", player.getLanguage(),
				player.getModuleName()));

		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("language [turingLanguage]",
				player.getLanguage(), player.getModuleName()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("commission [apprentice]",
				player.getLanguage(), player.getModuleName()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("cash [firebirds1000]",
				player.getLanguage(), player.getModuleName()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("gossipNetwork_3", player.getLanguage(),
				player.getModuleName()));

		final SelectedCyberneticDevice advancedEngineersEye = player.addCybernetics(CyberneticDeviceFactory
				.getInstance().getElement("engineersEye", player.getLanguage(), player.getModuleName()));
		advancedEngineersEye.addCustomization(CyberneticDeviceTraitFactory.getInstance().getElement("hidden", LANGUAGE,
				PathManager.DEFAULT_MODULE_FOLDER));

		player.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("secondBrain", LANGUAGE,
				PathManager.DEFAULT_MODULE_FOLDER));
		player.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("secondBrainJumpLoreSoftware", LANGUAGE,
				PathManager.DEFAULT_MODULE_FOLDER));
		player.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("secondBrainEnergyPistolsLore",
				player.getLanguage(), player.getModuleName()));
		player.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("secondBrainThinkMachineLore", LANGUAGE,
				PathManager.DEFAULT_MODULE_FOLDER));

		player.setShield(ShieldFactory.getInstance().getElement("duelingShield", player.getLanguage(),
				player.getModuleName()));

		LanguagePool.clearCache();
		final CharacterSheet sheet = new CharacterSheet(player);
		Assert.assertEquals(sheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "Paola.pdf"), 2);

		LanguagePool.clearCache();
		final SmallCharacterSheet smallSheet = new SmallCharacterSheet(player);
		Assert.assertEquals(
				smallSheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "Paola_Small.pdf"), 1);

		Assert.assertEquals(CostCalculator.getCost(player),
				FreeStyleCharacterCreation.getFreeAvailablePoints(player.getInfo().getAge()));
		Assert.assertEquals(player.getMoney(), 300);

		party.addMember(player);
	}

	@Test
	public void characterAnaCharacter() throws MalformedURLException, DocumentException, IOException,
			InvalidXmlElementException, TooManyBlessingsException, BeneficeAlreadyAddedException,
			BlessingAlreadyAddedException {
		final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		player.getInfo().addName(new Name("Arya", player.getLanguage(), player.getModuleName(), Gender.FEMALE, null));
		player.getInfo().setSurname(new Surname("Hawkwood", player.getLanguage(), player.getModuleName(), null));
		player.getInfo().setPlayer("Ana");
		player.getInfo().setGender(Gender.FEMALE);
		player.getInfo().setAge(32);
		player.setRace(RaceFactory.getInstance().getElement("human", player.getLanguage(), player.getModuleName()));
		player.getInfo().setPlanet(
				PlanetFactory.getInstance().getElement("leminkainen", player.getLanguage(), player.getModuleName()));
		player.setFaction(FactionsFactory.getInstance().getElement("hawkwood", player.getLanguage(),
				player.getModuleName()));

		player.getCharacteristic(CharacteristicName.STRENGTH).setValue(5);
		player.getCharacteristic(CharacteristicName.DEXTERITY).setValue(7);
		player.getCharacteristic(CharacteristicName.ENDURANCE).setValue(5);
		player.getCharacteristic(CharacteristicName.WITS).setValue(8);
		player.getCharacteristic(CharacteristicName.PERCEPTION).setValue(7);
		player.getCharacteristic(CharacteristicName.TECH).setValue(5);
		player.getCharacteristic(CharacteristicName.PRESENCE).setValue(7);
		player.getCharacteristic(CharacteristicName.WILL).setValue(5);
		player.getCharacteristic(CharacteristicName.FAITH).setValue(4);

		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("influence", player.getLanguage(),
						player.getModuleName()), 7);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance()
						.getElement("observe", player.getLanguage(), player.getModuleName()), 8);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("sneak", player.getLanguage(), player.getModuleName()),
				8);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("vigor", player.getLanguage(), player.getModuleName()),
				8);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("lockpicking", player.getLanguage(),
						player.getModuleName()), 4);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("arts", "drawing", player.getLanguage(),
						player.getModuleName()), 1);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("selfControl", player.getLanguage(),
						player.getModuleName()), 2);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("streetWise", player.getLanguage(),
						player.getModuleName()), 3);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance()
						.getElement("physick", player.getLanguage(), player.getModuleName()), 1);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("watercraft", player.getLanguage(),
						player.getModuleName()), 1);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("artifactMelee", player.getLanguage(),
						player.getModuleName()), 4);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("etiquette", player.getLanguage(),
						player.getModuleName()), 4);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("investigation", player.getLanguage(),
						player.getModuleName()), 4);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("leadership", player.getLanguage(),
						player.getModuleName()), 2);

		player.addBlessing(BlessingFactory.getInstance().getElement("horribleScar", player.getLanguage(),
				player.getModuleName()));

		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("nobility [knight]", player.getLanguage(),
				player.getModuleName()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("language [latinLanguage]",
				player.getLanguage(), player.getModuleName()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("imperialCharter", player.getLanguage(),
				player.getModuleName()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("fluxSword", player.getLanguage(),
				player.getModuleName()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("vendetta_2", player.getLanguage(),
				player.getModuleName()));

		LanguagePool.clearCache();
		final CharacterSheet sheet = new CharacterSheet(player);
		Assert.assertEquals(sheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "Ana.pdf"), 2);

		LanguagePool.clearCache();
		final SmallCharacterSheet smallSheet = new SmallCharacterSheet(player);
		Assert.assertEquals(
				smallSheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "Ana_Small.pdf"), 1);

		Assert.assertEquals(CostCalculator.getCost(player),
				FreeStyleCharacterCreation.getFreeAvailablePoints(player.getInfo().getAge()));
		Assert.assertEquals(player.getMoney(), 250);

		party.addMember(player);
	}

	@Test
	public void createCarlosCharacter() throws MalformedURLException, DocumentException, IOException,
			InvalidXmlElementException, TooManyBlessingsException, BeneficeAlreadyAddedException,
			BlessingAlreadyAddedException {
		final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		player.getInfo().addName(new Name("Carlos", player.getLanguage(), player.getModuleName(), Gender.MALE, null));
		player.getInfo().setPlayer("Carlos");
		player.getInfo().setGender(Gender.MALE);
		player.getInfo().setAge(38);

		player.getInfo().setCharacterDescription(LOREM_IPSUM);
		player.getInfo().setBackgroundDecription(LOREM_IPSUM);

		player.setRace(RaceFactory.getInstance().getElement("human", player.getLanguage(), player.getModuleName()));
		player.getInfo().setPlanet(
				PlanetFactory.getInstance().getElement("byzantiumSecundus", player.getLanguage(),
						player.getModuleName()));
		player.setFaction(FactionsFactory.getInstance().getElement("scravers", player.getLanguage(),
				player.getModuleName()));

		player.getCharacteristic(CharacteristicName.STRENGTH).setValue(7);
		player.getCharacteristic(CharacteristicName.DEXTERITY).setValue(8);
		player.getCharacteristic(CharacteristicName.ENDURANCE).setValue(6);
		player.getCharacteristic(CharacteristicName.WITS).setValue(6);
		player.getCharacteristic(CharacteristicName.PERCEPTION).setValue(6);
		player.getCharacteristic(CharacteristicName.TECH).setValue(6);
		player.getCharacteristic(CharacteristicName.PRESENCE).setValue(4);
		player.getCharacteristic(CharacteristicName.WILL).setValue(6);
		player.getCharacteristic(CharacteristicName.FAITH).setValue(3);

		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("influence", player.getLanguage(),
						player.getModuleName()), 5);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance()
						.getElement("observe", player.getLanguage(), player.getModuleName()), 4);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("vigor", player.getLanguage(), player.getModuleName()),
				7);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("slugGuns", player.getLanguage(),
						player.getModuleName()), 8);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance()
						.getElement("knavery", player.getLanguage(), player.getModuleName()), 4);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("selfControl", player.getLanguage(),
						player.getModuleName()), 2);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("streetWise", player.getLanguage(),
						player.getModuleName()), 3);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance()
						.getElement("physick", player.getLanguage(), player.getModuleName()), 2);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("aircraft", player.getLanguage(),
						player.getModuleName()), 1);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("fight", player.getLanguage(), player.getModuleName()),
				5);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("demolitions", player.getLanguage(),
						player.getModuleName()), 2);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("gaming", player.getLanguage(), player.getModuleName()),
				1);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("leadership", player.getLanguage(),
						player.getModuleName()), 1);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("techRedemption", player.getLanguage(),
						player.getModuleName()), 3);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("survival", player.getLanguage(),
						player.getModuleName()), 3);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance()
						.getElement("torture", player.getLanguage(), player.getModuleName()), 3);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("landcarft", player.getLanguage(),
						player.getModuleName()), 2);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("investigation", player.getLanguage(),
						player.getModuleName()), 3);

		player.addBlessing(BlessingFactory.getInstance().getElement("horribleScar", player.getLanguage(),
				player.getModuleName()));

		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("commission [entered]",
				player.getLanguage(), player.getModuleName()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("passageContracts_3",
				player.getLanguage(), player.getModuleName()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("cash [firebirds1000]",
				player.getLanguage(), player.getModuleName()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("ironHeel", player.getLanguage(),
				player.getModuleName()));

		player.addWeapon(WeaponFactory.getInstance().getElement("typicalShotgun", player.getLanguage(),
				player.getModuleName()));
		player.addWeapon(WeaponFactory.getInstance().getElement("mediumAutofeedHandgun", LANGUAGE,
				PathManager.DEFAULT_MODULE_FOLDER));

		LanguagePool.clearCache();
		final CharacterSheet sheet = new CharacterSheet(player);
		Assert.assertEquals(sheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "Carlos.pdf"), 2);

		LanguagePool.clearCache();
		final SmallCharacterSheet smallSheet = new SmallCharacterSheet(player);
		Assert.assertEquals(
				smallSheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "Carlos_Small.pdf"), 1);

		Assert.assertEquals(CostCalculator.getCost(player),
				FreeStyleCharacterCreation.getFreeAvailablePoints(player.getInfo().getAge()));
		Assert.assertEquals(player.getRank(), "Genin");
		Assert.assertEquals(500, player.getMoney());

		party.addMember(player);
	}

	@Test
	public void createNoeliaCharacer() throws MalformedURLException, DocumentException, IOException,
			InvalidXmlElementException, TooManyBlessingsException, BeneficeAlreadyAddedException,
			BlessingAlreadyAddedException {
		final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		player.getInfo().addName(new Name("Noelia", player.getLanguage(), player.getModuleName(), Gender.FEMALE, null));
		player.getInfo().setPlayer("Noelia");
		player.getInfo().setGender(Gender.FEMALE);
		player.getInfo().setAge(31);
		player.setRace(RaceFactory.getInstance().getElement("obun", player.getLanguage(), player.getModuleName()));
		player.getInfo().setPlanet(
				PlanetFactory.getInstance().getElement("velsimil", player.getLanguage(), player.getModuleName()));
		player.setFaction(FactionsFactory.getInstance()
				.getElement("obun", player.getLanguage(), player.getModuleName()));

		player.getCharacteristic(CharacteristicName.STRENGTH).setValue(3);
		player.getCharacteristic(CharacteristicName.DEXTERITY).setValue(6);
		player.getCharacteristic(CharacteristicName.ENDURANCE).setValue(3);
		player.getCharacteristic(CharacteristicName.WITS).setValue(8);
		player.getCharacteristic(CharacteristicName.PERCEPTION).setValue(4);
		player.getCharacteristic(CharacteristicName.TECH).setValue(3);
		player.getCharacteristic(CharacteristicName.PRESENCE).setValue(7);
		player.getCharacteristic(CharacteristicName.WILL).setValue(8);
		player.getCharacteristic(CharacteristicName.FAITH).setValue(8);

		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("influence", player.getLanguage(),
						player.getModuleName()), 4);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance()
						.getElement("observe", player.getLanguage(), player.getModuleName()), 6);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("vigor", player.getLanguage(), player.getModuleName()),
				5);

		player.setSkillRank(
				AvailableSkillsFactory.getInstance()
						.getElement("knavery", player.getLanguage(), player.getModuleName()), 2);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("selfControl", player.getLanguage(),
						player.getModuleName()), 6);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance()
						.getElement("physick", player.getLanguage(), player.getModuleName()), 3);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance()
						.getElement("empathy", player.getLanguage(), player.getModuleName()), 4);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("etiquette", player.getLanguage(),
						player.getModuleName()), 2);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("investigation", player.getLanguage(),
						player.getModuleName()), 3);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("lore", "kelantiLore", LANGUAGE,
						PathManager.DEFAULT_MODULE_FOLDER), 3);

		player.setPsiqueLevel(OccultismTypeFactory.getPsi(player.getLanguage(), player.getModuleName()), 6);

		player.addOccultismPower(OccultismPathFactory.getInstance()
				.getElement("farHand", player.getLanguage(), player.getModuleName()).getOccultismPowers()
				.get("liftingHand"));
		player.addOccultismPower(OccultismPathFactory.getInstance()
				.getElement("farHand", player.getLanguage(), player.getModuleName()).getOccultismPowers()
				.get("throwingHand"));
		player.addOccultismPower(OccultismPathFactory.getInstance()
				.getElement("farHand", player.getLanguage(), player.getModuleName()).getOccultismPowers()
				.get("crushingHand"));
		player.addOccultismPower(OccultismPathFactory.getInstance()
				.getElement("farHand", player.getLanguage(), player.getModuleName()).getOccultismPowers()
				.get("duelingHand"));
		player.addOccultismPower(OccultismPathFactory.getInstance()
				.getElement("farHand", player.getLanguage(), player.getModuleName()).getOccultismPowers()
				.get("farWall"));
		player.addOccultismPower(OccultismPathFactory.getInstance()
				.getElement("psyche", player.getLanguage(), player.getModuleName()).getOccultismPowers().get("intuit"));
		player.addOccultismPower(OccultismPathFactory.getInstance()
				.getElement("psyche", player.getLanguage(), player.getModuleName()).getOccultismPowers().get("emote"));
		player.addOccultismPower(OccultismPathFactory.getInstance()
				.getElement("psyche", player.getLanguage(), player.getModuleName()).getOccultismPowers()
				.get("mindSight"));
		player.setExtraWyrd(3);

		player.addBlessing(BlessingFactory.getInstance().getElement("horribleScar", player.getLanguage(),
				player.getModuleName()));

		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("ordained [novitiate]",
				player.getLanguage(), player.getModuleName()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("language [urthish]",
				player.getLanguage(), player.getModuleName()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("stigma_3", player.getLanguage(),
				player.getModuleName()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("orphan", player.getLanguage(),
				player.getModuleName()));

		LanguagePool.clearCache();
		final CharacterSheet sheet = new CharacterSheet(player);
		Assert.assertEquals(sheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "Noelia.pdf"), 2);

		LanguagePool.clearCache();
		final SmallCharacterSheet smallSheet = new SmallCharacterSheet(player);
		Assert.assertEquals(
				smallSheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "Noelia_Small.pdf"), 1);

		Assert.assertEquals(CostCalculator.getCost(player),
				FreeStyleCharacterCreation.getFreeAvailablePoints(player.getInfo().getAge()));

		party.addMember(player);
	}

	@Test
	public void createGolemCharacer() throws MalformedURLException, DocumentException, IOException,
			InvalidXmlElementException, TooManyBlessingsException, BlessingAlreadyAddedException {
		final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		player.getInfo().setPlayer("PNJ");
		player.getInfo().addName(new Name("A", player.getLanguage(), player.getModuleName(), Gender.FEMALE, null));
		player.getInfo().setSurname(new Surname("(Prototipo A)", player.getLanguage(), player.getModuleName(), null));
		player.getInfo().setGender(Gender.FEMALE);
		player.setRace(new Race("Gólem", player.getLanguage(), player.getModuleName(), 5, 5, 5, 3, 3, 6, 0, 0, 0, 6, 0,
				0, 0, 0, 0));
		player.getInfo().setPlanet(
				PlanetFactory.getInstance().getElement("leagueheim", player.getLanguage(), player.getModuleName()));
		player.setFaction(FactionsFactory.getInstance().getElement("engineers", player.getLanguage(),
				player.getModuleName()));
		player.getInfo().setAge(32);

		player.getCharacteristic(CharacteristicName.STRENGTH).setValue(12);
		player.getCharacteristic(CharacteristicName.DEXTERITY).setValue(7);
		player.getCharacteristic(CharacteristicName.ENDURANCE).setValue(10);
		player.getCharacteristic(CharacteristicName.WITS).setValue(5);
		player.getCharacteristic(CharacteristicName.PERCEPTION).setValue(7);
		player.getCharacteristic(CharacteristicName.TECH).setValue(5);

		player.setSkillRank(
				AvailableSkillsFactory.getInstance()
						.getElement("observe", player.getLanguage(), player.getModuleName()), 6);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("fight", player.getLanguage(), player.getModuleName()),
				5);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("vigor", player.getLanguage(), player.getModuleName()),
				7);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("throwing", player.getLanguage(),
						player.getModuleName()), 5);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("athletics", player.getLanguage(),
						player.getModuleName()), 4);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("selfControl", player.getLanguage(),
						player.getModuleName()), 5);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("spacecraft", player.getLanguage(),
						player.getModuleName()), 1);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("landcarft", player.getLanguage(),
						player.getModuleName()), 3);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("spacecraftOperations", LANGUAGE,
						PathManager.DEFAULT_MODULE_FOLDER), 3);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance()
						.getElement("warfare", player.getLanguage(), player.getModuleName()), 3);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("lore", "jumpwebLore", LANGUAGE,
						PathManager.DEFAULT_MODULE_FOLDER), 3);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("lore", "thinkMachineLore", LANGUAGE,
						PathManager.DEFAULT_MODULE_FOLDER), 3);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("thinkMachine", player.getLanguage(),
						player.getModuleName()), 3);
		player.setSkillRank(
				AvailableSkillsFactory.getInstance().getElement("techRedemption", player.getLanguage(),
						player.getModuleName()), 3);

		player.addBlessing(BlessingFactory.getInstance().getElement("gullible", player.getLanguage(),
				player.getModuleName()));
		player.addBlessing(BlessingFactory.getInstance().getElement("righteous", player.getLanguage(),
				player.getModuleName()));

		player.setArmour(new Armour("skin", "Piel", player.getLanguage(), player.getModuleName(), 5, 2,
				new HashSet<DamageType>(), 0));

		LanguagePool.clearCache();
		final CharacterSheet sheet = new CharacterSheet(player);
		Assert.assertEquals(sheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "Golem.pdf"), 2);

		LanguagePool.clearCache();
		final SmallCharacterSheet smallSheet = new SmallCharacterSheet(player);
		Assert.assertEquals(smallSheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "Golem.pdf"),
				1);

		Assert.assertEquals(CostCalculator.getCost(player), -5);
		Assert.assertEquals(player.getMoney(), 250);

		party.addMember(player);
	}

	@AfterClass
	public void createPartySheet() {
		final PartySheet sheet = new PartySheet(party);
		Assert.assertEquals(
				sheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + party.getPartyName() + ".pdf"),
				10);

		final SmallPartySheet smallSheet = new SmallPartySheet(party);
		Assert.assertEquals(
				smallSheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + party.getPartyName()
						+ "_Small.pdf"), 3);
	}
}
