package com.softwaremagico.tm.random.profile;

import java.util.Objects;

import org.testng.annotations.Test;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.RandomizeCharacter;
import com.softwaremagico.tm.character.blessings.TooManyBlessingsException;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.equipment.armours.Armour;
import com.softwaremagico.tm.character.equipment.armours.ArmourFactory;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.file.PathManager;
import com.softwaremagico.tm.random.exceptions.DuplicatedPreferenceException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.profiles.RandomProfileFactory;
import com.softwaremagico.tm.random.selectors.CombatPreferences;
import com.softwaremagico.tm.txt.CharacterSheet;
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

@Test(groups = "profile")
public class ProfileTests {
	private static final String LANGUAGE = "en";

	private void checkCharacterisic(CharacterPlayer characterPlayer, CharacteristicName characteristicName, int value) {
		try {
			Assert.assertTrue(characterPlayer.getValue(characteristicName) >= value);
		} catch (AssertionError e) {
			System.out.println("################ ERROR ################");
			final CharacterSheet characterSheet = new CharacterSheet(characterPlayer);
			System.out.println(characterSheet.toString());
			throw e;
		}
	}

	private void checkContainsWeapon(CharacterPlayer characterPlayer, Weapon weapon) {
		for (Weapon characterWeapon : characterPlayer.getAllWeapons()) {
			if (Objects.equals(characterWeapon, weapon)) {
				return;
			}
		}
		throw new AssertionError();
	}

	private void checkContainsArmour(CharacterPlayer characterPlayer, Armour armour) {
		if (!Objects.equals(characterPlayer.getArmour(), armour)) {
			throw new AssertionError();
		}
	}

	@Test
	public void checkPreferencesReader() throws DuplicatedPreferenceException, InvalidXmlElementException,
			InvalidRandomElementSelectedException, TooManyBlessingsException {
		Assert.assertEquals(RandomProfileFactory.getInstance().getElement("soldier", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getPreferences().size(),
				7);
		Assert.assertTrue(RandomProfileFactory.getInstance().getElement("soldier", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getPreferences()
				.contains(CombatPreferences.BELLIGERENT));
		Assert.assertEquals(RandomProfileFactory.getInstance().getElement("soldier", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
				.getCharacteristicMinimumValues(CharacteristicName.DEXTERITY).getValue(), 6);
	}

	@Test
	public void checkParent() throws DuplicatedPreferenceException, InvalidXmlElementException,
			InvalidRandomElementSelectedException, TooManyBlessingsException {
		Assert.assertEquals(
				RandomProfileFactory.getInstance().getElement("soldierLiHalan", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getPreferences().size(), 7);
		Assert.assertTrue(RandomProfileFactory.getInstance().getElement("soldierLiHalan", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getPreferences()
				.contains(CombatPreferences.BELLIGERENT));
		Assert.assertEquals(RandomProfileFactory.getInstance().getElement("soldierLiHalan", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
				.getCharacteristicMinimumValues(CharacteristicName.DEXTERITY).getValue(), 6);
		Assert.assertEquals(RandomProfileFactory.getInstance().getElement("soldierLiHalan", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
				.getCharacteristicMinimumValues(CharacteristicName.TECH).getValue(), 7);
	}

	@Test
	public void soldier() throws DuplicatedPreferenceException, InvalidXmlElementException,
			InvalidRandomElementSelectedException, TooManyBlessingsException {
		final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
				RandomProfileFactory.getInstance().getElement("soldier", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
		randomizeCharacter.createCharacter();
		// CharacterSheet characterSheet = new CharacterSheet(characterPlayer);
		// System.out.println(characterSheet.toString());
	}

	@Test
	public void serf() throws DuplicatedPreferenceException, InvalidXmlElementException,
			InvalidRandomElementSelectedException, TooManyBlessingsException {
		final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
				RandomProfileFactory.getInstance().getElement("serf", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
		Assert.assertEquals(RandomProfileFactory.getInstance().getElement("serf", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getRequiredSkills().size(),
				1);
		Assert.assertEquals(RandomProfileFactory.getInstance().getElement("serf", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getSuggestedSkills().size(),
				2);
		randomizeCharacter.createCharacter();
		Assert.assertTrue(characterPlayer.getSkillTotalRanks(
				AvailableSkillsFactory.getInstance().getElement("craft", "household", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)) > 0);
		// CharacterSheet characterSheet = new CharacterSheet(characterPlayer);
		// System.out.println(characterSheet.toString());
	}

	@Test
	public void thug() throws DuplicatedPreferenceException, InvalidXmlElementException,
			InvalidRandomElementSelectedException, TooManyBlessingsException {
		final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
				RandomProfileFactory.getInstance().getElement("thug", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
		Assert.assertEquals(
				RandomProfileFactory.getInstance().getElement("thug", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getMandatoryBenefices().size(), 1);
		Assert.assertEquals(
				RandomProfileFactory.getInstance().getElement("thug", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getSuggestedBenefices().size(), 5);
		randomizeCharacter.createCharacter();
		Assert.assertNotNull(characterPlayer.getBenefice("outlaw"));
	}

	@Test
	public void slayer() throws DuplicatedPreferenceException, InvalidXmlElementException,
			InvalidRandomElementSelectedException, TooManyBlessingsException {
		final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
				RandomProfileFactory.getInstance().getElement("slayer", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
		Assert.assertEquals(
				RandomProfileFactory.getInstance().getElement("slayer", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getSuggestedBenefices().size(), 4);
		randomizeCharacter.createCharacter();
	}

	@Test
	public void militia() throws DuplicatedPreferenceException, InvalidXmlElementException,
			InvalidRandomElementSelectedException, TooManyBlessingsException {
		final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
				RandomProfileFactory.getInstance().getElement("militia", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
		Assert.assertEquals(RandomProfileFactory.getInstance().getElement("militia", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
				.getCharacteristicMinimumValues(CharacteristicName.STRENGTH).getValue(), 6);
		randomizeCharacter.createCharacter();
		checkCharacterisic(characterPlayer, CharacteristicName.STRENGTH, 6);
	}

	@Test
	public void infantry() throws DuplicatedPreferenceException, InvalidXmlElementException,
			InvalidRandomElementSelectedException, TooManyBlessingsException {
		final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
				RandomProfileFactory.getInstance().getElement("infantry", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
		Assert.assertEquals(RandomProfileFactory.getInstance().getElement("infantry", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
				.getCharacteristicMinimumValues(CharacteristicName.ENDURANCE).getValue(), 6);
		randomizeCharacter.createCharacter();
		checkCharacterisic(characterPlayer, CharacteristicName.ENDURANCE, 6);
		checkContainsWeapon(characterPlayer, WeaponFactory.getInstance().getElement("imperialRifle", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
		checkContainsArmour(characterPlayer, ArmourFactory.getInstance().getElement("scaleMailMetal", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
	}

	@Test
	public void heavyInfantry() throws DuplicatedPreferenceException, InvalidXmlElementException,
			InvalidRandomElementSelectedException, TooManyBlessingsException {
		final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
				RandomProfileFactory.getInstance().getElement("heavyInfantry", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
		Assert.assertEquals(RandomProfileFactory.getInstance().getElement("heavyInfantry", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
				.getCharacteristicMinimumValues(CharacteristicName.STRENGTH).getValue(), 6);
		Assert.assertEquals(RandomProfileFactory.getInstance().getElement("heavyInfantry", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
				.getMandatoryWeapons().size(), 1);
		randomizeCharacter.createCharacter();
		checkCharacterisic(characterPlayer, CharacteristicName.STRENGTH, 6);
		checkContainsWeapon(characterPlayer, WeaponFactory.getInstance().getElement("jahnisak040MG", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
		checkContainsArmour(characterPlayer, ArmourFactory.getInstance().getElement("leatherJerkin", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
	}

	@Test
	public void tracker() throws DuplicatedPreferenceException, InvalidXmlElementException,
			InvalidRandomElementSelectedException, TooManyBlessingsException {
		final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer,
				RandomProfileFactory.getInstance().getElement("tracker", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
		Assert.assertEquals(RandomProfileFactory.getInstance().getElement("tracker", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
				.getCharacteristicMinimumValues(CharacteristicName.PERCEPTION).getValue(), 6);
		Assert.assertEquals(RandomProfileFactory.getInstance().getElement("tracker", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
				.getMandatoryWeapons().size(), 1);
		randomizeCharacter.createCharacter();
		checkCharacterisic(characterPlayer, CharacteristicName.PERCEPTION, 6);
		checkContainsWeapon(characterPlayer, WeaponFactory.getInstance().getElement("typicalSniperRifle", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
		checkContainsArmour(characterPlayer,
				ArmourFactory.getInstance().getElement("leatherJerkin", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
	}

}
