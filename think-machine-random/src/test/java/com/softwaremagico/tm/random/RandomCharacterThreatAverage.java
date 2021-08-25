package com.softwaremagico.tm.random;

/*-
 * #%L
 * Think Machine (Random Generator)
 * %%
 * Copyright (C) 2017 - 2019 Softwaremagico
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
import com.softwaremagico.tm.character.*;
import com.softwaremagico.tm.character.exceptions.RestrictedElementException;
import com.softwaremagico.tm.character.exceptions.UnofficialElementNotAllowedException;
import com.softwaremagico.tm.file.PathManager;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.CombatPreferences;
import com.softwaremagico.tm.random.selectors.DifficultLevelPreferences;
import com.softwaremagico.tm.random.selectors.IRandomPreference;
import org.testng.annotations.Test;

import java.util.Arrays;

@Test(groups = {"threatAverage"})
public class RandomCharacterThreatAverage {
    private static final String LANGUAGE = "en";
    private static final int NUMBER_OF_TESTS = 100;

    public int calculateThread(IRandomPreference<?>... preferences) throws InvalidXmlElementException,
            InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0, preferences);
        randomizeCharacter.createCharacter();
        return ThreatLevel.getThreatLevel(characterPlayer);
    }

    public float calculateThreatAverage(IRandomPreference<?>... preferences) throws
            InvalidXmlElementException, InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
        float threatLevel = 0f;
        ThreatLevel.resetStatistics();
        for (int i = 0; i < NUMBER_OF_TESTS; i++) {
            threatLevel += calculateThread(preferences);
        }
        System.out.println("Average threat level for '" + Arrays.toString(preferences) + "' is: " + threatLevel / NUMBER_OF_TESTS);
        ThreatLevel.showStaticis();
        return threatLevel / NUMBER_OF_TESTS;
    }

    @Test(timeOut = 5000)
    public void peacefulCharacterAverage() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
        calculateThreatAverage(CombatPreferences.PEACEFUL);
    }

    @Test(timeOut = 5000)
    public void fairCharacterAverage() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
        calculateThreatAverage(CombatPreferences.FAIR);
    }

    @Test(timeOut = 5000)
    public void belligerentCharacterAverage() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
        calculateThreatAverage(CombatPreferences.BELLIGERENT);
    }

    @Test(timeOut = 5000)
    public void easyCharacterAverage() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
        calculateThreatAverage(DifficultLevelPreferences.EASY);
    }

    @Test(timeOut = 5000)
    public void hardCharacterAverage() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
        calculateThreatAverage(DifficultLevelPreferences.HARD);
    }

    @Test(timeOut = 5000)
    public void mediumCharacterAverage() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
        calculateThreatAverage(DifficultLevelPreferences.MEDIUM);
    }

    @Test(timeOut = 5000)
    public void veryEasyCharacterAverage() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
        calculateThreatAverage(DifficultLevelPreferences.VERY_EASY);
    }

    @Test(timeOut = 5000)
    public void veryHardCharacterAverage() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
        calculateThreatAverage(DifficultLevelPreferences.VERY_HARD);
    }
}
