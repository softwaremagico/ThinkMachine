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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.testng.annotations.Test;

import com.itextpdf.text.DocumentException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.Gender;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.pdf.CharacterSheet;

@Test(groups = { "characterPdfGeneration" })
public class CharacterSheetCreationTest {
	private final static String PDF_PATH_OUTPUT = System.getProperty("java.io.tmpdir") + File.separator;

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
	public void characterPdfEnglish() throws MalformedURLException, DocumentException, IOException {
		CharacterPlayer player = new CharacterPlayer("es");
		player.getInfo().setName("John Sephard");
		player.getInfo().setPlayer("Player 1");
		player.getInfo().setGender(Gender.MALE);
		player.getInfo().setAge(30);
		player.getInfo().setRace("Human");
		player.getInfo().setPlanet("Sutek");
		player.getInfo().setAlliance("Hazat");
		player.getInfo().setRank("Knight");

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
		
		player.getOccultism().setPsiValue(4);
		player.getOccultism().setUrge(1);

		LanguagePool.clearCache();
		CharacterSheet sheet = new CharacterSheet(player);
		sheet.createFile(PDF_PATH_OUTPUT + "CharacterFS_ES.pdf");
	}

}
