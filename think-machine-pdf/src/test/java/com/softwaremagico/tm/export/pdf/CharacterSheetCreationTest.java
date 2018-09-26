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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
import com.softwaremagico.tm.character.blessings.BlessingFactory;
import com.softwaremagico.tm.character.blessings.TooManyBlessingsException;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.combat.CombatAction;
import com.softwaremagico.tm.character.combat.CombatStyle;
import com.softwaremagico.tm.character.combat.LearnedStance;
import com.softwaremagico.tm.character.creation.CostCalculator;
import com.softwaremagico.tm.character.cybernetics.Device;
import com.softwaremagico.tm.character.equipment.Armour;
import com.softwaremagico.tm.character.equipment.Shield;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.character.planet.PlanetFactory;
import com.softwaremagico.tm.character.race.Race;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.json.CharacterJsonManager;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.pdf.complete.CharacterSheet;
import com.softwaremagico.tm.pdf.small.SmallCharacterSheet;
import com.softwaremagico.tm.random.exceptions.DuplicatedPreferenceException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;

@Test(groups = { "characterPdfGeneration" })
public class CharacterSheetCreationTest {
	private final static String PDF_PATH_OUTPUT = System.getProperty("java.io.tmpdir") + File.separator;
	private final static String LANGUAGE = "es";

	private CharacterPlayer player;

	@AfterMethod
	public void clearCache() {
		LanguagePool.clearCache();
	}

	@Test
	public void emptyPdfSpanish() throws MalformedURLException, DocumentException, IOException {
		CacheHandler.clearCache();
		CharacterSheet sheet = new CharacterSheet(LANGUAGE);
		sheet.createFile(PDF_PATH_OUTPUT + "FadingSuns_ES.pdf");
	}

	@Test
	public void emptyPdfEnglish() throws MalformedURLException, DocumentException, IOException {
		CacheHandler.clearCache();
		CharacterSheet sheet = new CharacterSheet("en");
		sheet.createFile(PDF_PATH_OUTPUT + "FadingSuns_EN.pdf");
	}
	
	
	@Test
	public void emptyPdfSmallEn() throws InvalidXmlElementException, DuplicatedPreferenceException,
			InvalidRandomElementSelectedException {
		SmallCharacterSheet sheet = new SmallCharacterSheet("en");
		sheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "RandomCharacterSmallEmpty_EN.pdf");
	}
	
	@Test
	public void emptyPdfSmallEs() throws InvalidXmlElementException, DuplicatedPreferenceException,
			InvalidRandomElementSelectedException {
		SmallCharacterSheet sheet = new SmallCharacterSheet("es");
		sheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "RandomCharacterSmallEmpty_ES.pdf");
	}

	@Test
	public void characterPdfSpanish() throws MalformedURLException, DocumentException, IOException,
			InvalidXmlElementException, TooManyBlessingsException {
		CacheHandler.clearCache();

		player = new CharacterPlayer(LANGUAGE);
		player.getInfo().addName(new Name("John", Gender.MALE, null));
		player.getInfo().setSurname(new Surname("Sephard", null));
		player.getInfo().setPlayer("Player 1");
		player.getInfo().setGender(Gender.MALE);
		player.getInfo().setAge(30);
		player.setRace(new Race("Human", 3, 3, 3, 3, 3, 3, 3, 3, 3, 5, 0, 0, 0, 0, 0));
		player.getInfo().setPlanet(PlanetFactory.getInstance().getElement("sutek", LANGUAGE));
		player.setFaction(FactionsFactory.getInstance().getElement("hazat", LANGUAGE));

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

		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("influence", LANGUAGE), 5);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("sneak", LANGUAGE), 4);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("gaming", LANGUAGE), 4);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("lockpicking", LANGUAGE), 6);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("energyGuns", LANGUAGE), 6);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("warfare", LANGUAGE), 8);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("lore", "jumpwebLore", LANGUAGE), 4);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("lore", "beastsLore", LANGUAGE), 2);

		player.setPsiqueLevel(OccultismTypeFactory.getPsi(player.getLanguage()), 4);
		player.setDarkSideLevel(OccultismTypeFactory.getPsi(player.getLanguage()), 1);

		player.addOccultismPower(
				OccultismPathFactory.getInstance().getElement("farHand", LANGUAGE).getOccultismPowers()
						.get("liftingHand"));
		player.addOccultismPower(
				OccultismPathFactory.getInstance().getElement("farHand", LANGUAGE).getOccultismPowers()
						.get("throwingHand"));
		player.addOccultismPower(
				OccultismPathFactory.getInstance().getElement("sixthSense", LANGUAGE).getOccultismPowers()
						.get("sensitivity"));
		player.addOccultismPower(
				OccultismPathFactory.getInstance().getElement("soma", LANGUAGE).getOccultismPowers().get("toughening")
				);
		player.addOccultismPower(
				OccultismPathFactory.getInstance().getElement("soma", LANGUAGE).getOccultismPowers()
						.get("strengthening"));
		player.addOccultismPower(
				OccultismPathFactory.getInstance().getElement("soma", LANGUAGE).getOccultismPowers().get("quickening"));
		player.addOccultismPower(
				OccultismPathFactory.getInstance().getElement("soma", LANGUAGE).getOccultismPowers().get("hardening"));

		player.addBlessing(BlessingFactory.getInstance().getElement("curious", player.getLanguage()));
		player.addBlessing(BlessingFactory.getInstance().getElement("limp", player.getLanguage()));
		player.addBlessing(BlessingFactory.getInstance().getElement("missingEye", player.getLanguage()));
		player.addBlessing(BlessingFactory.getInstance().getElement("incurableDisease", player.getLanguage()));

		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("stigma_1", player.getLanguage()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("heir", player.getLanguage()));
		player.addBenefice(AvailableBeneficeFactory.getInstance().getElement("wireblade", player.getLanguage()));

		player.getCybernetics().addElement(
				new Device("Ojo de Ingeniero", 6, 5, "Normal", "Normal", "Automático", "Visible", ""));
		player.getCybernetics().addElement(
				new Device("Jonás", 7, 4, "Normal", "Normal", "Ds+Arquería", "Incógnito", ""));

		CombatStyle gun = new CombatStyle("pistola");
		gun.addElement(new CombatAction("Disparo Instantáneo", null, null, "-2 por 3 disparos"));
		gun.addElement(new CombatAction("Rueda y Dispara", null, null, "Mover 3m"));
		gun.addElement(new CombatAction("Corre y Dispara", null, null, "Especial"));
		player.getRangedCombatStyles().add(gun);

		CombatStyle shaidan = new CombatStyle("shaidan");
		shaidan.addElement(new CombatAction("Palma Real", null, "-1", ""));
		shaidan.addElement(new CombatAction("Con un Pie en el Trono", 4, null, "+4 a resistir derribos"));
		shaidan.addElement(new CombatAction("Decreto Imperial", null, "+1 / 1W", null));
		player.getMeleeCombatStyles().add(shaidan);

		player.getLearnedStances().add(new LearnedStance("Posición Acrobática", "+1 a defensa por volteretas"));

		player.getWeapons().addElement(WeaponFactory.getInstance().getElement("mace", LANGUAGE));
		player.getWeapons().addElement(WeaponFactory.getInstance().getElement("martechGold", LANGUAGE));

		player.setArmour(new Armour("Cuero Sintético", 7, true, false, false, false, false, false, true, 6, -1, 0, 0, 0, 500));

		player.setShield(new Shield("Escudo de Asalto", 5, 15, 20, 3000));

		LanguagePool.clearCache();
		CharacterSheet sheet = new CharacterSheet(player);
		sheet.createFile(PDF_PATH_OUTPUT + "CharacterFS_ES.pdf");

		Assert.assertEquals(CostCalculator.getCost(player), 50);
	}

	@Test(dependsOnMethods = { "characterPdfSpanish" })
	public void exportToJson() throws MalformedURLException, DocumentException, IOException {
		String jsonText = CharacterJsonManager.toJson(player);

		try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(PDF_PATH_OUTPUT
				+ "CharacterFS_ES.json")), true)) {
			out.println(jsonText);
		}

		// get json to object.
		CharacterPlayer importedCharacter = CharacterJsonManager.fromJson(jsonText);
		CharacterSheet sheet = new CharacterSheet(importedCharacter);
		sheet.createFile(PDF_PATH_OUTPUT + "CharacterFS_ES_2.pdf");

		// byte[] f1 = Files.readAllBytes(Paths.get(PDF_PATH_OUTPUT,
		// "CharacterFS_ES.pdf"));
		// byte[] f2 = Files.readAllBytes(Paths.get(PDF_PATH_OUTPUT,
		// "CharacterFS_ES_2.pdf"));
		// Assert.assertEquals(f1, f2);
	}
}
