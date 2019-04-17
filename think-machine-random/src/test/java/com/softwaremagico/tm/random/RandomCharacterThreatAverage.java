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

import java.util.Arrays;

import org.testng.annotations.Test;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.RandomizeCharacter;
import com.softwaremagico.tm.character.ThreatLevel;
import com.softwaremagico.tm.character.blessings.TooManyBlessingsException;
import com.softwaremagico.tm.random.exceptions.DuplicatedPreferenceException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.CombatPreferences;
import com.softwaremagico.tm.random.selectors.DifficultLevelPreferences;
import com.softwaremagico.tm.random.selectors.IRandomPreference;

@Test(groups = { "threatAverage" })
public class RandomCharacterThreatAverage {
	private static final String LANGUAGE = "en";
	private static final int NUMBER_OF_TESTS = 100;

	public int calculateThread(IRandomPreference... preferences) throws TooManyBlessingsException, DuplicatedPreferenceException, InvalidXmlElementException,
			InvalidRandomElementSelectedException {
		CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0, preferences);
		randomizeCharacter.createCharacter();
		return ThreatLevel.getThreatLevel(characterPlayer);
	}

	public float calculateThreatAverage(IRandomPreference... preferences) throws TooManyBlessingsException, DuplicatedPreferenceException,
			InvalidXmlElementException, InvalidRandomElementSelectedException {
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
	public void peacefulCharacterAverage() throws TooManyBlessingsException, DuplicatedPreferenceException, InvalidXmlElementException,
			InvalidRandomElementSelectedException {
		calculateThreatAverage(CombatPreferences.PEACEFUL);
	}

	@Test
	public void fairCharacterAverage() throws TooManyBlessingsException, DuplicatedPreferenceException, InvalidXmlElementException,
			InvalidRandomElementSelectedException {
		calculateThreatAverage(CombatPreferences.FAIR);
	}

	@Test
	public void belligerentCharacterAverage() throws TooManyBlessingsException, DuplicatedPreferenceException, InvalidXmlElementException,
			InvalidRandomElementSelectedException {
		calculateThreatAverage(CombatPreferences.BELLIGERENT);
	}

	@Test
	public void easyCharacterAverage() throws TooManyBlessingsException, DuplicatedPreferenceException, InvalidXmlElementException,
			InvalidRandomElementSelectedException {
		calculateThreatAverage(DifficultLevelPreferences.EASY);
	}

	@Test
	public void hardCharacterAverage() throws TooManyBlessingsException, DuplicatedPreferenceException, InvalidXmlElementException,
			InvalidRandomElementSelectedException {
		calculateThreatAverage(DifficultLevelPreferences.HARD);
	}

	@Test
	public void mediumCharacterAverage() throws TooManyBlessingsException, DuplicatedPreferenceException, InvalidXmlElementException,
			InvalidRandomElementSelectedException {
		calculateThreatAverage(DifficultLevelPreferences.MEDIUM);
	}

	@Test
	public void veryEasyCharacterAverage() throws TooManyBlessingsException, DuplicatedPreferenceException, InvalidXmlElementException,
			InvalidRandomElementSelectedException {
		calculateThreatAverage(DifficultLevelPreferences.VERY_EASY);
	}

	@Test
	public void veryHardCharacterAverage() throws TooManyBlessingsException, DuplicatedPreferenceException, InvalidXmlElementException,
			InvalidRandomElementSelectedException {
		calculateThreatAverage(DifficultLevelPreferences.VERY_HARD);
	}
}
