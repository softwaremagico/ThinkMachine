package com.softwaremagico.tm.export.pdf;

/*-
 * #%L
 * Think Machine (PDF)
 * %%
 * Copyright (C) 2017 - 2018 Softwaremagico
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

import junit.framework.Assert;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.creation.CostCalculator;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.pdf.complete.CharacterSheet;
import com.softwaremagico.tm.pdf.small.SmallCharacterSheet;
import com.softwaremagico.tm.random.RandomizeCharacter;
import com.softwaremagico.tm.random.exceptions.DuplicatedPreferenceException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;

@Test(groups = { "randomCharacterSheetCreation" })
public class RandomCharacterSheetCreationTest {

	@BeforeMethod
	public void clearCache() {
		LanguagePool.clearCache();
	}

	@Test
	public void completeRandomCharacter() throws InvalidXmlElementException, DuplicatedPreferenceException,
			InvalidRandomElementSelectedException {
		CharacterPlayer characterPlayer = new CharacterPlayer("es");
		RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0);
		randomizeCharacter.createCharacter();
		
		Assert.assertEquals(FreeStyleCharacterCreation.FREE_AVAILABLE_POINTS, CostCalculator.logCost(characterPlayer));

		LanguagePool.clearCache();
		CharacterSheet sheet = new CharacterSheet(characterPlayer);
		sheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "RandomCharacter.pdf");
	}
	
	@Test
	public void completeRandomCharacterSmallEs() throws InvalidXmlElementException, DuplicatedPreferenceException,
			InvalidRandomElementSelectedException {
		CharacterPlayer characterPlayer = new CharacterPlayer("es");
		RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0);
		randomizeCharacter.createCharacter();
		
		Assert.assertEquals(FreeStyleCharacterCreation.FREE_AVAILABLE_POINTS, CostCalculator.logCost(characterPlayer));

		LanguagePool.clearCache();
		SmallCharacterSheet sheet = new SmallCharacterSheet(characterPlayer);
		sheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "RandomCharacterSmall_ES.pdf");
	}
	
	@Test
	public void completeRandomCharacterSmallEn() throws InvalidXmlElementException, DuplicatedPreferenceException,
			InvalidRandomElementSelectedException {
		CharacterPlayer characterPlayer = new CharacterPlayer("en");
		RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0);
		randomizeCharacter.createCharacter();
		
		Assert.assertEquals(FreeStyleCharacterCreation.FREE_AVAILABLE_POINTS, CostCalculator.logCost(characterPlayer));

		LanguagePool.clearCache();
		SmallCharacterSheet sheet = new SmallCharacterSheet(characterPlayer);
		sheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "RandomCharacterSmall_EN.pdf");
	}
}
