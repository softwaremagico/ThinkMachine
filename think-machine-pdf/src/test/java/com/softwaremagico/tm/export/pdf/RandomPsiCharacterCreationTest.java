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
import java.io.IOException;
import java.net.MalformedURLException;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.itextpdf.text.DocumentException;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.blessings.TooManyBlessingsException;
import com.softwaremagico.tm.character.creation.CostCalculator;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.pdf.small.SmallCharacterSheet;
import com.softwaremagico.tm.random.RandomizeCharacter;
import com.softwaremagico.tm.random.exceptions.DuplicatedPreferenceException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.PsiqueLevelPreferences;
import com.softwaremagico.tm.random.selectors.PsiquePathLevelPreferences;
import com.softwaremagico.tm.random.selectors.SpecializationPreferences;

@Test(groups = { "randomPsiCharacterTest" })
public class RandomPsiCharacterCreationTest {
	private final static String LANGUAGE = "en";

	@BeforeMethod
	public void clearCache() {
		LanguagePool.clearCache();
	}

	@Test
	public void createRandomPsiCharacter() throws MalformedURLException, DocumentException, IOException, InvalidXmlElementException, TooManyBlessingsException,
			DuplicatedPreferenceException, InvalidRandomElementSelectedException {
		CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0, SpecializationPreferences.SPECIALIZED,
				PsiquePathLevelPreferences.HIGH, PsiqueLevelPreferences.HIGH);
		randomizeCharacter.createCharacter();

		LanguagePool.clearCache();
		SmallCharacterSheet sheet = new SmallCharacterSheet(characterPlayer);
		sheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "RandomPsiCharacter.pdf");

		Assert.assertEquals(CostCalculator.logCost(characterPlayer), FreeStyleCharacterCreation.FREE_AVAILABLE_POINTS);
	}
}
