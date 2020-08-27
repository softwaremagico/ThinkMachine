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
import com.softwaremagico.tm.character.cybernetics.CyberneticDeviceFactory;
import com.softwaremagico.tm.character.cybernetics.Cybernetics;
import com.softwaremagico.tm.character.cybernetics.RequiredCyberneticDevicesException;
import com.softwaremagico.tm.character.cybernetics.TooManyCyberneticDevicesException;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.file.PathManager;

@Test(groups = { "cybernetics" })
public class CyberneticsTests {
	private static final String LANGUAGE = "es";

	private static final int WITS = 7;
	private static final int MAX_INCOMPATIBILITY = WITS * 3 + 2;

	@Test(expectedExceptions = { TooManyCyberneticDevicesException.class })
	public void tooManyCybernetics()
			throws TooManyCyberneticDevicesException, InvalidXmlElementException, RequiredCyberneticDevicesException {
		final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		player.setCharacteristic(CharacteristicName.WILL, 7);

		Assert.assertEquals(Cybernetics.getMaxCyberneticIncompatibility(player), MAX_INCOMPATIBILITY);

		try {
			// 2 points
			player.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("centurionKnife", LANGUAGE,
					PathManager.DEFAULT_MODULE_FOLDER));
			Assert.assertEquals(player.getCyberneticsIncompatibility(), 2);

			// 6 points
			player.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("goliathSkin", LANGUAGE,
					PathManager.DEFAULT_MODULE_FOLDER));
			Assert.assertEquals(player.getCyberneticsIncompatibility(), 8);

			// 7 points
			player.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("oxyLung", LANGUAGE,
					PathManager.DEFAULT_MODULE_FOLDER));
			Assert.assertEquals(player.getCyberneticsIncompatibility(), 15);

			// 7 points
			player.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("xEyes", LANGUAGE,
					PathManager.DEFAULT_MODULE_FOLDER));
			Assert.assertEquals(player.getCyberneticsIncompatibility(), 22);
		} catch (TooManyCyberneticDevicesException e) {
			// Not correct
			Assert.assertTrue(false);
		}

		player.addCybernetics(
				CyberneticDeviceFactory.getInstance().getElement("jonah", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
	}

	@Test(expectedExceptions = { RequiredCyberneticDevicesException.class })
	public void restrictedDevice()
			throws InvalidXmlElementException, TooManyCyberneticDevicesException, RequiredCyberneticDevicesException {
		final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		player.setCharacteristic(CharacteristicName.WILL, 8);

		player.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("secondBrainEnergyPistolsLore", LANGUAGE,
				PathManager.DEFAULT_MODULE_FOLDER));
	}

	@Test
	public void restrictedDeviceAcepted()
			throws InvalidXmlElementException, TooManyCyberneticDevicesException, RequiredCyberneticDevicesException {
		final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		player.setCharacteristic(CharacteristicName.WILL, 8);

		player.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("secondBrain", LANGUAGE,
				PathManager.DEFAULT_MODULE_FOLDER));
		player.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("secondBrainEnergyPistolsLore", LANGUAGE,
				PathManager.DEFAULT_MODULE_FOLDER));
	}

	@Test
	public void cyberneticAsAWeapon()
			throws TooManyCyberneticDevicesException, InvalidXmlElementException, RequiredCyberneticDevicesException {
		final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		player.setCharacteristic(CharacteristicName.WILL, 7);

		player.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("centurionKnife", LANGUAGE,
				PathManager.DEFAULT_MODULE_FOLDER));
		Assert.assertEquals(player.getAllWeapons().size(), 1);
	}

	@Test
	public void cyberneticCharacteristicsImprovement()
			throws InvalidXmlElementException, TooManyCyberneticDevicesException, RequiredCyberneticDevicesException {
		final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		player.setCharacteristic(CharacteristicName.WILL, 8);
		player.setCharacteristic(CharacteristicName.WITS, 6);

		player.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("secondBrain", LANGUAGE,
				PathManager.DEFAULT_MODULE_FOLDER));
		Assert.assertEquals((int) player.getValue(CharacteristicName.WITS), 8);
	}

	@Test
	public void cyberneticSkillStaticValue()
			throws InvalidXmlElementException, TooManyCyberneticDevicesException, RequiredCyberneticDevicesException {
		final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		player.setCharacteristic(CharacteristicName.WILL, 8);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("lore", "energyPistolsLore", LANGUAGE,
				PathManager.DEFAULT_MODULE_FOLDER), 3);

		player.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("secondBrain", LANGUAGE,
				PathManager.DEFAULT_MODULE_FOLDER));
		player.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("secondBrainEnergyPistolsLore", LANGUAGE,
				PathManager.DEFAULT_MODULE_FOLDER));

		Assert.assertEquals((int) player.getSkillTotalRanks(AvailableSkillsFactory.getInstance().getElement("lore",
				"energyPistolsLore", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)), 4);
	}

	@Test
	public void cyberneticSkillStaticValueSurpassed()
			throws InvalidXmlElementException, TooManyCyberneticDevicesException, RequiredCyberneticDevicesException {
		final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
		player.setCharacteristic(CharacteristicName.WILL, 8);
		player.setSkillRank(AvailableSkillsFactory.getInstance().getElement("lore", "energyPistolsLore", LANGUAGE,
				PathManager.DEFAULT_MODULE_FOLDER), 6);

		player.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("secondBrain", LANGUAGE,
				PathManager.DEFAULT_MODULE_FOLDER));
		player.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("secondBrainEnergyPistolsLore", LANGUAGE,
				PathManager.DEFAULT_MODULE_FOLDER));

		Assert.assertEquals((int) player.getSkillTotalRanks(AvailableSkillsFactory.getInstance().getElement("lore",
				"energyPistolsLore", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)), 6);
	}
}
