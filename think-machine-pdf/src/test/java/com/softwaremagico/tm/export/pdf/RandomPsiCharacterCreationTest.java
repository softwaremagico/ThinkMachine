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

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.RandomizeCharacter;
import com.softwaremagico.tm.character.exceptions.RestrictedElementException;
import com.softwaremagico.tm.character.exceptions.UnofficialElementNotAllowedException;
import com.softwaremagico.tm.character.creation.CostCalculator;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.file.PathManager;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.pdf.small.SmallCharacterSheet;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.OccultismLevelPreferences;
import com.softwaremagico.tm.random.selectors.OccultismPathLevelPreferences;
import com.softwaremagico.tm.random.selectors.SpecializationPreferences;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;

@Test(groups = {"randomPsiCharacterTest"})
public class RandomPsiCharacterCreationTest {
    private static final String LANGUAGE = "en";

    @BeforeMethod
    public void clearCache() {
        LanguagePool.clearCache();
    }

    @Test
    public void createRandomPsiCharacter() throws InvalidXmlElementException, InvalidRandomElementSelectedException, RestrictedElementException,
            UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0,
                SpecializationPreferences.SPECIALIZED, OccultismPathLevelPreferences.HIGH, OccultismLevelPreferences.HIGH);
        randomizeCharacter.createCharacter();

        try {
            Assert.assertEquals(CostCalculator.getCost(characterPlayer),
                    FreeStyleCharacterCreation.getFreeAvailablePoints(characterPlayer.getInfo().getAge(), characterPlayer.getRace()));
        } catch (AssertionError e) {
            CostCalculator.logCost(characterPlayer);
            throw e;
        }

        LanguagePool.clearCache();
        final SmallCharacterSheet sheet = new SmallCharacterSheet(characterPlayer);
        Assert.assertEquals(
                sheet.createFile(System.getProperty("java.io.tmpdir") + File.separator + "RandomPsiCharacter.pdf"), 1);
    }
}
