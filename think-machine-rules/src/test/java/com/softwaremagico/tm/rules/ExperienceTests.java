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
import com.softwaremagico.tm.character.benefices.BeneficeAlreadyAddedException;
import com.softwaremagico.tm.character.blessings.BlessingAlreadyAddedException;
import com.softwaremagico.tm.character.blessings.TooManyBlessingsException;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.creation.CostCalculator;
import com.softwaremagico.tm.character.cybernetics.RequiredCyberneticDevicesException;
import com.softwaremagico.tm.character.cybernetics.TooManyCyberneticDevicesException;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.occultism.InvalidPowerLevelException;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.character.skills.InvalidSkillException;
import com.softwaremagico.tm.character.xp.ElementCannotBeUpgradeWithExperienceException;
import com.softwaremagico.tm.character.xp.NotEnoughExperienceException;
import com.softwaremagico.tm.characters.CustomCharacter;
import com.softwaremagico.tm.file.PathManager;
import com.softwaremagico.tm.json.CharacterJsonManager;
import com.softwaremagico.tm.json.InvalidJsonException;

@Test(groups = { "experience" })
public class ExperienceTests {
	private static final String LANGUAGE = "es";

	@Test
	public void addOneByOneRankToSkill() throws InvalidSkillException, InvalidXmlElementException,
			NotEnoughExperienceException, ElementCannotBeUpgradeWithExperienceException {
		final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("influence", LANGUAGE,
				PathManager.DEFAULT_MODULE_FOLDER), 5);

		player.setExperienceEarned(12);
		player.setExperienceIncreasedRanks(AvailableSkillsFactory.getInstance().getElement("influence", LANGUAGE,
				PathManager.DEFAULT_MODULE_FOLDER), 1);
		Assert.assertEquals((int) player.getSkillAssignedRanks(AvailableSkillsFactory.getInstance()
				.getElement("influence", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)), 5);
		Assert.assertEquals((int) player.getSkillTotalRanks(AvailableSkillsFactory.getInstance().getElement("influence",
				LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)), 6);
		Assert.assertEquals(player.getExperienceExpended(), 12);

		player.setExperienceEarned(26);
		player.setExperienceIncreasedRanks(AvailableSkillsFactory.getInstance().getElement("influence", LANGUAGE,
				PathManager.DEFAULT_MODULE_FOLDER), 1);
		Assert.assertEquals((int) player.getSkillAssignedRanks(AvailableSkillsFactory.getInstance()
				.getElement("influence", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)), 5);
		Assert.assertEquals((int) player.getSkillTotalRanks(AvailableSkillsFactory.getInstance().getElement("influence",
				LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)), 7);
		Assert.assertEquals(player.getExperienceExpended(), 26);
	}

	@Test
	public void addTwoRanksToSkill() throws InvalidSkillException, InvalidXmlElementException,
			NotEnoughExperienceException, ElementCannotBeUpgradeWithExperienceException {
		final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("influence", LANGUAGE,
				PathManager.DEFAULT_MODULE_FOLDER), 5);

		player.setExperienceEarned(26);
		player.setExperienceIncreasedRanks(AvailableSkillsFactory.getInstance().getElement("influence", LANGUAGE,
				PathManager.DEFAULT_MODULE_FOLDER), 2);
		Assert.assertEquals((int) player.getSkillAssignedRanks(AvailableSkillsFactory.getInstance()
				.getElement("influence", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)), 5);
		Assert.assertEquals((int) player.getSkillTotalRanks(AvailableSkillsFactory.getInstance().getElement("influence",
				LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)), 7);
		Assert.assertEquals(player.getExperienceExpended(), 26);

		player.removeExperienceIncreasedRanks((AvailableSkillsFactory.getInstance().getElement("influence", LANGUAGE,
				PathManager.DEFAULT_MODULE_FOLDER)), 7);
		Assert.assertEquals((int) player.getSkillTotalRanks(AvailableSkillsFactory.getInstance().getElement("influence",
				LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)), 6);
		Assert.assertEquals(player.getExperienceExpended(), (12));
	}

	@Test
	public void addOneByOneRankToLoreSkill() throws InvalidSkillException, InvalidXmlElementException,
			NotEnoughExperienceException, ElementCannotBeUpgradeWithExperienceException {
		final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("phoenixEmpireLore", LANGUAGE,
				PathManager.DEFAULT_MODULE_FOLDER), 5);

		player.setExperienceEarned(9);
		player.setExperienceIncreasedRanks(AvailableSkillsFactory.getInstance().getElement("phoenixEmpireLore",
				LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER), 1);
		Assert.assertEquals((int) player.getSkillAssignedRanks(AvailableSkillsFactory.getInstance()
				.getElement("phoenixEmpireLore", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)), 5);
		Assert.assertEquals((int) player.getSkillTotalRanks(AvailableSkillsFactory.getInstance()
				.getElement("phoenixEmpireLore", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)), 6);
		Assert.assertEquals(player.getExperienceExpended(), 9);

		player.setExperienceEarned(19);
		player.setExperienceIncreasedRanks(AvailableSkillsFactory.getInstance().getElement("phoenixEmpireLore",
				LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER), 1);
		Assert.assertEquals((int) player.getSkillAssignedRanks(AvailableSkillsFactory.getInstance()
				.getElement("phoenixEmpireLore", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)), 5);
		Assert.assertEquals((int) player.getSkillTotalRanks(AvailableSkillsFactory.getInstance()
				.getElement("phoenixEmpireLore", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)), 7);
		Assert.assertEquals(player.getExperienceExpended(), 19);
	}

	@Test(expectedExceptions = { NotEnoughExperienceException.class })
	public void addOneRankToSkillNotPossible() throws InvalidSkillException, InvalidXmlElementException,
			NotEnoughExperienceException, ElementCannotBeUpgradeWithExperienceException {
		final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("influence", LANGUAGE,
				PathManager.DEFAULT_MODULE_FOLDER), 5);

		player.setExperienceEarned(0);
		player.setExperienceIncreasedRanks(AvailableSkillsFactory.getInstance().getElement("influence", LANGUAGE,
				PathManager.DEFAULT_MODULE_FOLDER), 1);
	}

	@Test
	public void addOneRankToCharacteristic() throws InvalidSkillException, InvalidXmlElementException,
			NotEnoughExperienceException, ElementCannotBeUpgradeWithExperienceException {
		final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		player.setCharacteristic(CharacteristicName.STRENGTH, 5);

		player.setExperienceEarned(18);
		player.setExperienceIncreasedRanks(player.getCharacteristic(CharacteristicName.STRENGTH), 1);
		Assert.assertEquals((int) player.getRawValue(CharacteristicName.STRENGTH), 5);
		Assert.assertEquals((int) player.getValue(CharacteristicName.STRENGTH), 6);
		Assert.assertEquals(player.getExperienceExpended(), 18);
	}

	@Test(expectedExceptions = { NotEnoughExperienceException.class })
	public void addOneRankToCharacteristicNotPossible() throws InvalidSkillException, InvalidXmlElementException,
			NotEnoughExperienceException, ElementCannotBeUpgradeWithExperienceException {
		final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		player.setCharacteristic(CharacteristicName.STRENGTH, 5);

		player.setExperienceEarned(12);
		player.setExperienceIncreasedRanks(player.getCharacteristic(CharacteristicName.STRENGTH), 1);
	}

	@Test
	public void addWyrd() throws ElementCannotBeUpgradeWithExperienceException, InvalidXmlElementException,
			NotEnoughExperienceException {
		final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		player.setCharacteristic(CharacteristicName.WILL, 5);

		player.setExperienceEarned(26);
		player.setExperienceExtraWyrd(2);
		Assert.assertEquals((int) player.getWyrdValue(), 7);
		Assert.assertEquals(player.getExperienceExpended(), (12 + 14));

		player.removeExperienceExtraWyrd(7);
		Assert.assertEquals((int) player.getWyrdValue(), 6);
		Assert.assertEquals(player.getExperienceExpended(), (12));
	}

	@Test
	public void setPsiLevel() throws ElementCannotBeUpgradeWithExperienceException, InvalidXmlElementException,
			NotEnoughExperienceException {
		final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		player.setFaction(
				FactionsFactory.getInstance().getElement("hazat", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
		player.setPsiqueLevel(OccultismTypeFactory.getPsi(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER), 4);

		player.setExperienceEarned(33);
		player.setExperiencePsiLevel(OccultismTypeFactory.getPsi(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER), 2);
		Assert.assertEquals(
				(int) player.getPsiqueLevel(OccultismTypeFactory.getPsi(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)),
				6);
		Assert.assertEquals(player.getExperienceExpended(), (15 + 18));

		player.removeExperiencePsiLevel(OccultismTypeFactory.getPsi(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER), 6);
		Assert.assertEquals(
				(int) player.getPsiqueLevel(OccultismTypeFactory.getPsi(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)),
				5);
		Assert.assertEquals(player.getExperienceExpended(), (15));
	}

	@Test
	public void setPsiPowers() throws ElementCannotBeUpgradeWithExperienceException, InvalidXmlElementException,
			NotEnoughExperienceException {
		final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		player.setFaction(
				FactionsFactory.getInstance().getElement("hazat", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
		player.setPsiqueLevel(OccultismTypeFactory.getPsi(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER), 4);

		player.setExperienceEarned(6);
		player.setExperienceInOccultism(
				OccultismPathFactory.getInstance().getElement("farHand", player.getLanguage(), player.getModuleName()),
				OccultismPathFactory.getInstance().getElement("farHand", player.getLanguage(), player.getModuleName())
						.getOccultismPowers().get("liftingHand"));
		Assert.assertEquals(player.getExperienceExpended(), (2));
		player.setExperienceInOccultism(
				OccultismPathFactory.getInstance().getElement("farHand", player.getLanguage(), player.getModuleName()),
				OccultismPathFactory.getInstance().getElement("farHand", player.getLanguage(), player.getModuleName())
						.getOccultismPowers().get("throwingHand"));
		Assert.assertEquals(player.getExperienceExpended(), (2 + 4));

	}

	@Test(expectedExceptions = { InvalidPowerLevelException.class })
	public void removeInvalidPsiPowers() throws ElementCannotBeUpgradeWithExperienceException,
			InvalidXmlElementException, NotEnoughExperienceException {
		final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		player.setFaction(
				FactionsFactory.getInstance().getElement("hazat", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
		player.setPsiqueLevel(OccultismTypeFactory.getPsi(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER), 4);

		player.setExperienceEarned(6);
		player.setExperienceInOccultism(
				OccultismPathFactory.getInstance().getElement("farHand", player.getLanguage(), player.getModuleName()),
				OccultismPathFactory.getInstance().getElement("farHand", player.getLanguage(), player.getModuleName())
						.getOccultismPowers().get("liftingHand"));
		player.setExperienceInOccultism(
				OccultismPathFactory.getInstance().getElement("farHand", player.getLanguage(), player.getModuleName()),
				OccultismPathFactory.getInstance().getElement("farHand", player.getLanguage(), player.getModuleName())
						.getOccultismPowers().get("throwingHand"));
		// Remove level1 but left level2. Error
		player.removeExperienceInOccultismPower(
				OccultismPathFactory.getInstance().getElement("farHand", player.getLanguage(), player.getModuleName()),
				OccultismPathFactory.getInstance().getElement("farHand", player.getLanguage(), player.getModuleName())
						.getOccultismPowers().get("liftingHand"));

	}

	@Test
	public void removeValidOrderPsiPowers() throws ElementCannotBeUpgradeWithExperienceException,
			InvalidXmlElementException, NotEnoughExperienceException {
		final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		player.setFaction(
				FactionsFactory.getInstance().getElement("hazat", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
		player.setPsiqueLevel(OccultismTypeFactory.getPsi(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER), 4);

		player.setExperienceEarned(6);
		player.setExperienceInOccultism(
				OccultismPathFactory.getInstance().getElement("farHand", player.getLanguage(), player.getModuleName()),
				OccultismPathFactory.getInstance().getElement("farHand", player.getLanguage(), player.getModuleName())
						.getOccultismPowers().get("liftingHand"));
		player.setExperienceInOccultism(
				OccultismPathFactory.getInstance().getElement("farHand", player.getLanguage(), player.getModuleName()),
				OccultismPathFactory.getInstance().getElement("farHand", player.getLanguage(), player.getModuleName())
						.getOccultismPowers().get("throwingHand"));
		// Remove level2 and later level1
		player.removeExperienceInOccultismPower(
				OccultismPathFactory.getInstance().getElement("farHand", player.getLanguage(), player.getModuleName()),
				OccultismPathFactory.getInstance().getElement("farHand", player.getLanguage(), player.getModuleName())
						.getOccultismPowers().get("throwingHand"));
		player.removeExperienceInOccultismPower(
				OccultismPathFactory.getInstance().getElement("farHand", player.getLanguage(), player.getModuleName()),
				OccultismPathFactory.getInstance().getElement("farHand", player.getLanguage(), player.getModuleName())
						.getOccultismPowers().get("liftingHand"));
	}

	@Test
	public void checkJsonConverter()
			throws TooManyBlessingsException, BlessingAlreadyAddedException, BeneficeAlreadyAddedException,
			InvalidXmlElementException, TooManyCyberneticDevicesException, RequiredCyberneticDevicesException,
			NotEnoughExperienceException, ElementCannotBeUpgradeWithExperienceException, InvalidJsonException {
		CharacterPlayer player = CustomCharacter.create(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		Assert.assertEquals(CostCalculator.getCost(player), CustomCharacter.COST);

		player.setExperienceEarned(100);
		player.setExperienceIncreasedRanks(player.getCharacteristic(CharacteristicName.PERCEPTION), 1);
		player.setExperienceIncreasedRanks(AvailableSkillsFactory.getInstance().getElement("influence", LANGUAGE,
				PathManager.DEFAULT_MODULE_FOLDER), 2);

		String jsonText = CharacterJsonManager.toJson(player);
		final CharacterPlayer playerImported = CharacterJsonManager.fromJson(jsonText);

		Assert.assertEquals((int) playerImported.getExperienceEarned(), 100);
		Assert.assertEquals((int) playerImported.getSkillTotalRanks(AvailableSkillsFactory.getInstance()
				.getElement("influence", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)), 7);
		Assert.assertEquals((int) playerImported.getValue(CharacteristicName.PERCEPTION), 6);
		Assert.assertEquals(player.getExperienceExpended(), (18 + 12 + 14));

	}
}
