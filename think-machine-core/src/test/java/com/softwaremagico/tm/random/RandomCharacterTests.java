package com.softwaremagico.tm.random;

/*-
 * #%L
 * Think Machine (Core)
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

import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.character.race.RaceFactory;
import com.softwaremagico.tm.character.skills.SkillDefinition;
import com.softwaremagico.tm.character.skills.SkillsDefinitionsFactory;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.random.exceptions.DuplicatedPreferenceException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.FactionPreferences;
import com.softwaremagico.tm.random.selectors.RacePreferences;
import com.softwaremagico.tm.random.selectors.TechnologicalPreferences;

@Test(groups = { "randomCharacter" })
public class RandomCharacterTests {
	private Random rand = new Random();

	@AfterMethod
	public void clearCache() {
		LanguagePool.clearCache();
	}

	@Test(expectedExceptions = { DuplicatedPreferenceException.class })
	public void preferencesCollision() throws InvalidXmlElementException, DuplicatedPreferenceException {
		CharacterPlayer characterPlayer = new CharacterPlayer("es");
		new RandomizeCharacter(characterPlayer, 0, TechnologicalPreferences.MEDIEVAL, TechnologicalPreferences.FUTURIST);
	}

	@Test
	public void chooseRaceAndFactionTest() throws InvalidXmlElementException, DuplicatedPreferenceException, InvalidRandomElementSelectedException {
		CharacterPlayer characterPlayer = new CharacterPlayer("es");
		RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0, RacePreferences.HUMAN, FactionPreferences.NOBILITY);
		randomizeCharacter.setCharacterDefinition();

		Assert.assertEquals(characterPlayer.getInfo().getFaction().getFactionGroup(), FactionGroup.NOBILITY);
		Assert.assertEquals(characterPlayer.getRace(), RaceFactory.getInstance().getElement(RacePreferences.HUMAN.name(), "es"));
	}

	@Test
	public void chooseRaceAndFactionTestXeno() throws InvalidXmlElementException, DuplicatedPreferenceException, InvalidRandomElementSelectedException {
		CharacterPlayer characterPlayer = new CharacterPlayer("es");
		RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0, RacePreferences.OBUN, FactionPreferences.GUILD);
		randomizeCharacter.setCharacterDefinition();

		Assert.assertEquals(characterPlayer.getInfo().getFaction().getFactionGroup(), FactionGroup.GUILD);
		Assert.assertEquals(characterPlayer.getRace(), RaceFactory.getInstance().getElement(RacePreferences.OBUN.name(), "es"));
	}

	@Test
	public void readRandomSkillConfiguration() throws InvalidXmlElementException, DuplicatedPreferenceException {
		SkillDefinition skillDefinition = SkillsDefinitionsFactory.getInstance().get("energyGuns", "en");
		Assert.assertEquals(skillDefinition.getRandomDefinition().getMinimumTechLevel(), 5);
	}

	@Test
	public void completeRandomCharacter() throws InvalidXmlElementException, DuplicatedPreferenceException, InvalidRandomElementSelectedException {
		CharacterPlayer characterPlayer = new CharacterPlayer("es");
		RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, 0);
		randomizeCharacter.createCharacter();
		// Assert.assertEquals(CostCalculator.getCost(characterPlayer),
		// FreeStyleCharacterCreation.FREE_AVAILABLE_POINTS);
		
		
		for(int i=0; i<100;i++){
			System.out.println(randomGaussian(4, 2));
		}
	}
	
	private double randomGaussian(int mean, double variance) {
		return rand.nextGaussian() * Math.sqrt(variance) + mean;
	}
}
