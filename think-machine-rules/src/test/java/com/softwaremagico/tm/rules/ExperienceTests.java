package com.softwaremagico.tm.rules;

/*-
 * #%L
 * Think Machine (Rules)
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

import org.testng.Assert;
import org.testng.annotations.Test;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.character.skills.InvalidSkillException;
import com.softwaremagico.tm.chracter.xp.ElementCannotBeUpgradeWithExperienceException;
import com.softwaremagico.tm.chracter.xp.NotEnoughExperienceException;
import com.softwaremagico.tm.file.PathManager;

@Test(groups = { "experience" })
public class ExperienceTests {
	private static final String LANGUAGE = "es";

	@Test
	public void addOneByOneRankToSkill() throws InvalidSkillException, InvalidXmlElementException,
			NotEnoughExperienceException, ElementCannotBeUpgradeWithExperienceException {
		final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("influence", LANGUAGE,
				PathManager.DEFAULT_MODULE_FOLDER), 5);

		player.setEarnedExperience(12);
		player.setIncreaseRanksUsingExperience(AvailableSkillsFactory.getInstance().getElement("influence", LANGUAGE,
				PathManager.DEFAULT_MODULE_FOLDER), 1);
		Assert.assertEquals((int) player.getSkillAssignedRanks(AvailableSkillsFactory.getInstance()
				.getElement("influence", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)), 5);
		Assert.assertEquals((int) player.getSkillTotalRanks(AvailableSkillsFactory.getInstance().getElement("influence",
				LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)), 6);
		Assert.assertEquals(player.getExpendedExperience(), 12);

		player.setEarnedExperience(26);
		player.setIncreaseRanksUsingExperience(AvailableSkillsFactory.getInstance().getElement("influence", LANGUAGE,
				PathManager.DEFAULT_MODULE_FOLDER), 1);
		Assert.assertEquals((int) player.getSkillAssignedRanks(AvailableSkillsFactory.getInstance()
				.getElement("influence", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)), 5);
		Assert.assertEquals((int) player.getSkillTotalRanks(AvailableSkillsFactory.getInstance().getElement("influence",
				LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)), 7);
		Assert.assertEquals(player.getExpendedExperience(), 26);
	}

	@Test
	public void addTwoRanksToSkill() throws InvalidSkillException, InvalidXmlElementException,
			NotEnoughExperienceException, ElementCannotBeUpgradeWithExperienceException {
		final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("influence", LANGUAGE,
				PathManager.DEFAULT_MODULE_FOLDER), 5);

		player.setEarnedExperience(26);
		player.setIncreaseRanksUsingExperience(AvailableSkillsFactory.getInstance().getElement("influence", LANGUAGE,
				PathManager.DEFAULT_MODULE_FOLDER), 2);
		Assert.assertEquals((int) player.getSkillAssignedRanks(AvailableSkillsFactory.getInstance()
				.getElement("influence", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)), 5);
		Assert.assertEquals((int) player.getSkillTotalRanks(AvailableSkillsFactory.getInstance().getElement("influence",
				LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)), 7);
		Assert.assertEquals(player.getExpendedExperience(), 26);
	}

	@Test
	public void addOneByOneRankToLoreSkill() throws InvalidSkillException, InvalidXmlElementException,
			NotEnoughExperienceException, ElementCannotBeUpgradeWithExperienceException {
		final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("phoenixEmpireLore", LANGUAGE,
				PathManager.DEFAULT_MODULE_FOLDER), 5);

		player.setEarnedExperience(9);
		player.setIncreaseRanksUsingExperience(AvailableSkillsFactory.getInstance().getElement("phoenixEmpireLore",
				LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER), 1);
		Assert.assertEquals((int) player.getSkillAssignedRanks(AvailableSkillsFactory.getInstance()
				.getElement("phoenixEmpireLore", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)), 5);
		Assert.assertEquals((int) player.getSkillTotalRanks(AvailableSkillsFactory.getInstance()
				.getElement("phoenixEmpireLore", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)), 6);
		Assert.assertEquals(player.getExpendedExperience(), 9);

		player.setEarnedExperience(19);
		player.setIncreaseRanksUsingExperience(AvailableSkillsFactory.getInstance().getElement("phoenixEmpireLore",
				LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER), 1);
		Assert.assertEquals((int) player.getSkillAssignedRanks(AvailableSkillsFactory.getInstance()
				.getElement("phoenixEmpireLore", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)), 5);
		Assert.assertEquals((int) player.getSkillTotalRanks(AvailableSkillsFactory.getInstance()
				.getElement("phoenixEmpireLore", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)), 7);
		Assert.assertEquals(player.getExpendedExperience(), 19);
	}

	@Test(expectedExceptions = { NotEnoughExperienceException.class })
	public void addOneRankToSkillNotPossible() throws InvalidSkillException, InvalidXmlElementException,
			NotEnoughExperienceException, ElementCannotBeUpgradeWithExperienceException {
		final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("influence", LANGUAGE,
				PathManager.DEFAULT_MODULE_FOLDER), 5);

		player.setEarnedExperience(0);
		player.setIncreaseRanksUsingExperience(AvailableSkillsFactory.getInstance().getElement("influence", LANGUAGE,
				PathManager.DEFAULT_MODULE_FOLDER), 1);
	}

	@Test
	public void addOneRankToCharacteristic() throws InvalidSkillException, InvalidXmlElementException,
			NotEnoughExperienceException, ElementCannotBeUpgradeWithExperienceException {
		final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		player.getCharacteristic(CharacteristicName.STRENGTH).setValue(5);

		player.setEarnedExperience(18);
		player.setIncreaseRanksUsingExperience(player.getCharacteristic(CharacteristicName.STRENGTH), 1);
		Assert.assertEquals((int) player.getRawValue(CharacteristicName.STRENGTH), 5);
		Assert.assertEquals((int) player.getValue(CharacteristicName.STRENGTH), 6);
		Assert.assertEquals(player.getExpendedExperience(), 18);
	}

	@Test(expectedExceptions = { NotEnoughExperienceException.class })
	public void addOneRankToCharacteristicNotPossible() throws InvalidSkillException, InvalidXmlElementException,
			NotEnoughExperienceException, ElementCannotBeUpgradeWithExperienceException {
		final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		player.getCharacteristic(CharacteristicName.STRENGTH).setValue(5);

		player.setEarnedExperience(12);
		player.setIncreaseRanksUsingExperience(player.getCharacteristic(CharacteristicName.STRENGTH), 1);
	}
}
