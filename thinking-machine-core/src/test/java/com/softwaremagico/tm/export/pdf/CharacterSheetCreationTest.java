package com.softwaremagico.tm.export.pdf;

/*-
 * #%L
 * The Thinking Machine (Core)
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
import org.testng.annotations.Test;

import com.itextpdf.text.DocumentException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.CostCalculator;
import com.softwaremagico.tm.character.Gender;
import com.softwaremagico.tm.character.Race;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.combat.CombatAction;
import com.softwaremagico.tm.character.combat.CombatStyle;
import com.softwaremagico.tm.character.combat.LearnedStance;
import com.softwaremagico.tm.character.cybernetics.Device;
import com.softwaremagico.tm.character.equipment.Armour;
import com.softwaremagico.tm.character.equipment.Shield;
import com.softwaremagico.tm.character.equipment.Size;
import com.softwaremagico.tm.character.equipment.Weapon;
import com.softwaremagico.tm.character.occultism.OccultismPower;
import com.softwaremagico.tm.character.traits.Benefit;
import com.softwaremagico.tm.character.traits.Blessing;
import com.softwaremagico.tm.json.CharacterJsonManager;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.pdf.CharacterSheet;

@Test(groups = { "characterPdfGeneration" })
public class CharacterSheetCreationTest {
	private final static String PDF_PATH_OUTPUT = System.getProperty("java.io.tmpdir") + File.separator;
	private CharacterPlayer player;

	@Test
	public void emptyPdfSpanish() throws MalformedURLException, DocumentException, IOException {
		LanguagePool.clearCache();
		CharacterSheet sheet = new CharacterSheet("es");
		sheet.createFile(PDF_PATH_OUTPUT + "FadingSuns_ES.pdf");
	}

	@Test
	public void emptyPdfEnglish() throws MalformedURLException, DocumentException, IOException {
		LanguagePool.clearCache();
		CharacterSheet sheet = new CharacterSheet("en");
		sheet.createFile(PDF_PATH_OUTPUT + "FadingSuns_EN.pdf");
	}

	@Test
	public void characterPdfSpanish() throws MalformedURLException, DocumentException, IOException {
		player = new CharacterPlayer("es");
		player.getInfo().setName("John Sephard");
		player.getInfo().setPlayer("Player 1");
		player.getInfo().setGender(Gender.MALE);
		player.getInfo().setAge(30);
		player.setRace(new Race("Human", 3, 3, 3, 3, 3, 3, 3, 3, 3, 5, 0, 0, 0, 0, 0));
		player.getInfo().setPlanet("Sutek");
		player.getInfo().setAlliance("Hazat");
		player.getInfo().setRank("Knight");

		player.getInfo().setBirthdate("4996-09-16");
		player.getInfo().setHair("Moreno");
		player.getInfo().setEyes("Marrones");
		player.getInfo().setComplexion("Delgado");
		player.getInfo().setHeight("1,76m");
		player.getInfo().setWeight("78kg");

		player.getCharacteristics().getCharacteristic(CharacteristicName.STRENGTH).setValue(1);
		player.getCharacteristics().getCharacteristic(CharacteristicName.DEXTERITY).setValue(2);
		player.getCharacteristics().getCharacteristic(CharacteristicName.ENDURANCE).setValue(3);
		player.getCharacteristics().getCharacteristic(CharacteristicName.WITS).setValue(4);
		player.getCharacteristics().getCharacteristic(CharacteristicName.PERCEPTION).setValue(5);
		player.getCharacteristics().getCharacteristic(CharacteristicName.TECH).setValue(6);
		player.getCharacteristics().getCharacteristic(CharacteristicName.PRESENCE).setValue(7);
		player.getCharacteristics().getCharacteristic(CharacteristicName.WILL).setValue(8);
		player.getCharacteristics().getCharacteristic(CharacteristicName.FAITH).setValue(9);

		player.addSkill("Influenciar", 5);
		player.addSkill("Sigilo", 4);
		player.addSkill("Juego", 4);
		player.addSkill("Abrir Cerraduras", 6);
		player.addSkill("Armas de Energía", 6);
		player.addSkill("Guerra", 8);
		player.addSkill("Saber [Red de Salto]", 4);
		player.addSkill("Saber [Bestias]", 2);

		player.getOccultism().setPsiValue(4);
		player.getOccultism().setUrge(1);

		player.getOccultism().addElement(new OccultismPower("Mano Levitante", "Vol+Autoc.", 1, "Sensorial", "Temporal", "", 1));
		player.getOccultism().addElement(new OccultismPower("Mano Lanzadora", "Vol+Autoc.", 2, "Sensorial", "Temporal", "", 1));
		player.getOccultism().addElement(new OccultismPower("Sensibilidad", "Vol+Observar", 1, "Sensorial", "Temporal", "", 1));
		player.getOccultism().addElement(new OccultismPower("Fortalecer", "Vol+Vigor", 1, null, "Temporal", "", 1));
		player.getOccultism().addElement(new OccultismPower("Vigorizar", "Vol+Vigor", 2, null, "Temporal", "", 1));
		player.getOccultism().addElement(new OccultismPower("Agilizar", "Vol+Atletismo", 3, null, "Temporal", "", 1));
		player.getOccultism().addElement(new OccultismPower("Endurecer", "Vol+Vigor", 4, null, "Temporal", "", 1));

		player.addBlessing(new Blessing("Elegante", 1, 1, "Influenciar", "--"));
		player.addBlessing(new Blessing("Curioso", 2, 2, "Presencia", "Ante algo nuevo"));
		player.addBlessing(new Blessing("Crédulo", -2, -2, "Voluntad", "Si se le engatusa"));

		player.addBenefit(new Benefit("Estigma", -1));
		player.addBenefit(new Benefit("Herencia", 3));
		player.addBenefit(new Benefit("Filoespada", 12));

		player.getCybernetics().addElement(new Device("Ojo de Ingeniero", 6, 5, "Normal", "Normal", "Automático", "Visible", ""));
		player.getCybernetics().addElement(new Device("Jonás", 7, 4, "Normal", "Normal", "Ds+Arquería", "Incógnito", ""));

		CombatStyle gun = new CombatStyle("Pistola");
		gun.addElement(new CombatAction("Disparo Instantáneo", null, null, "-2 por 3 disparos"));
		gun.addElement(new CombatAction("Rueda y Dispara", null, null, "Mover 3m"));
		gun.addElement(new CombatAction("Corre y Dispara", null, null, "Especial"));
		player.getRangedCombatStyles().add(gun);

		CombatStyle shaidan = new CombatStyle("Shydan");
		shaidan.addElement(new CombatAction("Palma Real", null, "-1", ""));
		shaidan.addElement(new CombatAction("Con un Pie en el Trono", 4, null, "+4 a resistir derribos"));
		shaidan.addElement(new CombatAction("Decreto Imperial", null, "+1/1W", null));
		player.getMeleeCombatStyles().add(shaidan);
		
		player.getLearnedStances().add(new LearnedStance("Posición Acrobática", "+1 a defensa por volteretas"));

		player.getWeapons().addElement(new Weapon("Maza", "Ds+Lucha", null, 5, "4", null, null, 1, Size.L));
		player.getWeapons().addElement(new Weapon("Martech Oro", "Ds+Ar. Energía", 1, 5, "10/20", 15, "2", 6, Size.S, "Láser"));

		player.setArmour(new Armour("Cuero Sintético", 7, true, false, false, false, false, false, true, 6, -1, 0, 0, 0));

		player.setShield(new Shield("Escudo de Asalto", 5, 15, 20));

		LanguagePool.clearCache();
		CharacterSheet sheet = new CharacterSheet(player);
		sheet.createFile(PDF_PATH_OUTPUT + "CharacterFS_ES.pdf");

		Assert.assertEquals(CostCalculator.getCost(player), 51);
	}

	@Test(dependsOnMethods = { "characterPdfSpanish" })
	public void exportToJson() throws MalformedURLException, DocumentException, IOException {
		String jsonText = CharacterJsonManager.toJson(player);

		try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(PDF_PATH_OUTPUT + "CharacterFS_ES.json")), true)) {
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
