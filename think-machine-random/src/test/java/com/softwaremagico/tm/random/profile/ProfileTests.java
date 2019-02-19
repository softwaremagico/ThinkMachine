package com.softwaremagico.tm.random.profile;

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

import org.testng.Assert;
import org.testng.annotations.Test;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.RandomizeCharacter;
import com.softwaremagico.tm.character.blessings.TooManyBlessingsException;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.random.exceptions.DuplicatedPreferenceException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.profiles.RandomProfileFactory;
import com.softwaremagico.tm.random.selectors.CombatPreferences;
import com.softwaremagico.tm.txt.CharacterSheet;

@Test(groups = "profile")
public class ProfileTests {
	private final static String LANGUAGE = "en";

	@Test
	public void checkPreferencesReader() throws DuplicatedPreferenceException, InvalidXmlElementException, InvalidRandomElementSelectedException,
			TooManyBlessingsException {
		Assert.assertEquals(RandomProfileFactory.getInstance().getElement("soldier", LANGUAGE).getPreferences().size(), 7);
		Assert.assertTrue(RandomProfileFactory.getInstance().getElement("soldier", LANGUAGE).getPreferences().contains(CombatPreferences.BELLIGERENT));
		Assert.assertEquals(
				RandomProfileFactory.getInstance().getElement("soldier", LANGUAGE).getCharacteristicsMinimumValues().get(CharacteristicName.DEXTERITY),
				new Integer(6));
	}

	@Test
	public void checkParent() throws DuplicatedPreferenceException, InvalidXmlElementException, InvalidRandomElementSelectedException,
			TooManyBlessingsException {
		Assert.assertEquals(RandomProfileFactory.getInstance().getElement("soldierLiHalan", LANGUAGE).getPreferences().size(), 7);
		Assert.assertTrue(RandomProfileFactory.getInstance().getElement("soldierLiHalan", LANGUAGE).getPreferences().contains(CombatPreferences.BELLIGERENT));
		Assert.assertEquals(
				RandomProfileFactory.getInstance().getElement("soldierLiHalan", LANGUAGE).getCharacteristicsMinimumValues().get(CharacteristicName.DEXTERITY),
				new Integer(6));
		Assert.assertEquals(
				RandomProfileFactory.getInstance().getElement("soldierLiHalan", LANGUAGE).getCharacteristicsMinimumValues().get(CharacteristicName.TECH),
				new Integer(7));
	}

	@Test
	public void soldier() throws DuplicatedPreferenceException, InvalidXmlElementException, InvalidRandomElementSelectedException, TooManyBlessingsException {
		CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, RandomProfileFactory.getInstance().getElement("soldier", LANGUAGE));
		randomizeCharacter.createCharacter();
		// CharacterSheet characterSheet = new CharacterSheet(characterPlayer);
		// System.out.println(characterSheet.toString());
	}

	@Test
	public void serf() throws DuplicatedPreferenceException, InvalidXmlElementException, InvalidRandomElementSelectedException, TooManyBlessingsException {
		CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE);
		RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, RandomProfileFactory.getInstance().getElement("serf", LANGUAGE));
		Assert.assertEquals(RandomProfileFactory.getInstance().getElement("serf", LANGUAGE).getRequiredSkills().size(), 1);
		Assert.assertEquals(RandomProfileFactory.getInstance().getElement("serf", LANGUAGE).getSuggestedSkills().size(), 2);
		randomizeCharacter.createCharacter();
		Assert.assertTrue(characterPlayer.getSkillTotalRanks(AvailableSkillsFactory.getInstance().getElement("craft", "household", LANGUAGE)) > 0);
		CharacterSheet characterSheet = new CharacterSheet(characterPlayer);
		System.out.println(characterSheet.toString());
	}
}
