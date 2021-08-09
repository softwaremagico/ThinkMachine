package com.softwaremagico.tm.random;

/*-
 * #%L
 * Think Machine (Core)
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
import com.softwaremagico.tm.character.RestrictedElementException;
import com.softwaremagico.tm.character.UnofficialElementNotAllowedException;
import com.softwaremagico.tm.character.creation.CostCalculator;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.file.PathManager;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.DifficultLevelPreferences;
import org.junit.Assert;
import org.testng.annotations.Test;

@Test(groups = {"difficulty"})
public class RandomCharacterDifficultyTests {
    private static final String LANGUAGE = "en";

    @Test(timeOut = 5000)
    public void easy() throws InvalidXmlElementException, InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0, DifficultLevelPreferences.EASY);
        randomizeCharacter.createCharacter();
        Assert.assertTrue(CostCalculator.getCost(characterPlayer) < FreeStyleCharacterCreation.getFreeAvailablePoints(characterPlayer.getInfo().getAge(),
                characterPlayer.getRace()));
    }

    @Test(timeOut = 5000)
    public void hard() throws InvalidXmlElementException, InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0, DifficultLevelPreferences.HARD);
        randomizeCharacter.createCharacter();
        Assert.assertTrue(characterPlayer.getCharacteristicsTotalPoints() >= FreeStyleCharacterCreation.getCharacteristicsPoints(characterPlayer.getInfo()
                .getAge()) + DifficultLevelPreferences.HARD.getCharacteristicsBonus());
        Assert.assertTrue(characterPlayer.getSkillsTotalPoints() >= FreeStyleCharacterCreation.getSkillsPoints(characterPlayer.getInfo().getAge())
                + DifficultLevelPreferences.HARD.getSkillsBonus());
        Assert.assertTrue(CostCalculator.getCost(characterPlayer) > FreeStyleCharacterCreation.getFreeAvailablePoints(characterPlayer.getInfo().getAge(),
                characterPlayer.getRace()));
    }

    @Test(timeOut = 5000)
    public void veryHard() throws InvalidXmlElementException, InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0, DifficultLevelPreferences.VERY_HARD);
        randomizeCharacter.createCharacter();
        Assert.assertTrue(characterPlayer.getCharacteristicsTotalPoints() >= FreeStyleCharacterCreation.getCharacteristicsPoints(characterPlayer.getInfo()
                .getAge()) + DifficultLevelPreferences.VERY_HARD.getCharacteristicsBonus());
        Assert.assertTrue(characterPlayer.getSkillsTotalPoints() >= FreeStyleCharacterCreation.getSkillsPoints(characterPlayer.getInfo().getAge())
                + DifficultLevelPreferences.VERY_HARD.getSkillsBonus());
        Assert.assertTrue(CostCalculator.getCost(characterPlayer) > FreeStyleCharacterCreation.getFreeAvailablePoints(characterPlayer.getInfo().getAge(),
                characterPlayer.getRace()));
    }
}
