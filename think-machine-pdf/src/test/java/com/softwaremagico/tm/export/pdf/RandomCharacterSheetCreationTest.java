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

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.RandomizeCharacter;
import com.softwaremagico.tm.character.blessings.TooManyBlessingsException;
import com.softwaremagico.tm.character.creation.CostCalculator;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.file.Path;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.pdf.complete.CharacterSheet;
import com.softwaremagico.tm.pdf.small.SmallCharacterSheet;
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
			InvalidRandomElementSelectedException, TooManyBlessingsException {
		final CharacterPlayer characterPlayer = new CharacterPlayer("es", Path.DEFAULT_MODULE_FOLDER);
		final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0);
		randomizeCharacter.createCharacter();

		Assert.assertEquals(CostCalculator.logCost(characterPlayer),
				FreeStyleCharacterCreation.getFreeAvailablePoints(characterPlayer.getInfo().getAge()));

		LanguagePool.clearCache();
		final CharacterSheet sheet = new CharacterSheet(characterPlayer);
		Assert.assertEquals(
				sheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "RandomCharacter.pdf"), 2);
	}

	@Test
	public void completeRandomCharacterSmallEs() throws InvalidXmlElementException, DuplicatedPreferenceException,
			InvalidRandomElementSelectedException, TooManyBlessingsException {
		final CharacterPlayer characterPlayer = new CharacterPlayer("es", Path.DEFAULT_MODULE_FOLDER);
		final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0);
		randomizeCharacter.createCharacter();

		Assert.assertEquals(CostCalculator.logCost(characterPlayer),
				FreeStyleCharacterCreation.getFreeAvailablePoints(characterPlayer.getInfo().getAge()));

		LanguagePool.clearCache();
		final SmallCharacterSheet sheet = new SmallCharacterSheet(characterPlayer);
		Assert.assertEquals(
				sheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "RandomCharacterSmall_ES.pdf"),
				1);
	}

	@Test
	public void completeRandomCharacterSmallEn() throws InvalidXmlElementException, DuplicatedPreferenceException,
			InvalidRandomElementSelectedException, TooManyBlessingsException {
		final CharacterPlayer characterPlayer = new CharacterPlayer("en", Path.DEFAULT_MODULE_FOLDER);
		final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0);
		randomizeCharacter.createCharacter();

		Assert.assertEquals(CostCalculator.logCost(characterPlayer),
				FreeStyleCharacterCreation.getFreeAvailablePoints(characterPlayer.getInfo().getAge()));

		LanguagePool.clearCache();
		final SmallCharacterSheet sheet = new SmallCharacterSheet(characterPlayer);
		Assert.assertEquals(
				sheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "RandomCharacterSmall_EN.pdf"),
				1);
	}
}
