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
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.RandomizeCharacter;
import com.softwaremagico.tm.character.RestrictedElementException;
import com.softwaremagico.tm.character.ThreatLevel;
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

    public int calculateThread(IRandomPreference... preferences) throws InvalidXmlElementException,
            InvalidRandomElementSelectedException, RestrictedElementException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0, preferences);
        randomizeCharacter.createCharacter();
        return ThreatLevel.getThreatLevel(characterPlayer);
    }

    public float calculateThreatAverage(IRandomPreference... preferences) throws
            InvalidXmlElementException, InvalidRandomElementSelectedException, RestrictedElementException {
        float threatLevel = 0f;
        ThreatLevel.resetStatistics();
        for (int i = 0; i < NUMBER_OF_TESTS; i++) {
            threatLevel += calculateThread(preferences);
        }
        System.out.println("Average threat level for '" + Arrays.toString(preferences) + "' is: " + threatLevel / NUMBER_OF_TESTS);
        ThreatLevel.showStaticis();
        return threatLevel / NUMBER_OF_TESTS;
    }

    @Test
    public void peacefulCharacterAverage() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException, RestrictedElementException {
        calculateThreatAverage(CombatPreferences.PEACEFUL);
    }

    @Test
    public void fairCharacterAverage() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException, RestrictedElementException {
        calculateThreatAverage(CombatPreferences.FAIR);
    }

    @Test
    public void belligerentCharacterAverage() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException, RestrictedElementException {
        calculateThreatAverage(CombatPreferences.BELLIGERENT);
    }

    @Test
    public void easyCharacterAverage() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException, RestrictedElementException {
        calculateThreatAverage(DifficultLevelPreferences.EASY);
    }

    @Test
    public void hardCharacterAverage() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException, RestrictedElementException {
        calculateThreatAverage(DifficultLevelPreferences.HARD);
    }

    @Test
    public void mediumCharacterAverage() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException, RestrictedElementException {
        calculateThreatAverage(DifficultLevelPreferences.MEDIUM);
    }

    @Test
    public void veryEasyCharacterAverage() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException, RestrictedElementException {
        calculateThreatAverage(DifficultLevelPreferences.VERY_EASY);
    }

    @Test
    public void veryHardCharacterAverage() throws InvalidXmlElementException,
            InvalidRandomElementSelectedException, RestrictedElementException {
        calculateThreatAverage(DifficultLevelPreferences.VERY_HARD);
    }
}
