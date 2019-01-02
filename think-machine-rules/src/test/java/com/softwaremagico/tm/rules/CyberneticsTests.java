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
import com.softwaremagico.tm.character.cybernetics.TooManyCyberneticDevicesException;

@Test(groups = { "cybernetics" })
public class CyberneticsTests {
	private final static String LANGUAGE = "es";
	private final static int WITS = 7;
	private final static int MAX_INCOMPATIBILITY = WITS * 3 + 2;

	@Test(expectedExceptions = { TooManyCyberneticDevicesException.class })
	public void tooManyCybernetics() throws TooManyCyberneticDevicesException, InvalidXmlElementException {
		CharacterPlayer player = new CharacterPlayer(LANGUAGE);
		player.getCharacteristic(CharacteristicName.WILL).setValue(7);

		Assert.assertEquals(Cybernetics.getMaxCyberneticIncompatibility(player), MAX_INCOMPATIBILITY);

		try {
			// 2 points
			player.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("centurionKnife", LANGUAGE));
			Assert.assertEquals(player.getCyberneticsIncompatibility(), 2);

			// 6 points
			player.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("goliathSkin", LANGUAGE));
			Assert.assertEquals(player.getCyberneticsIncompatibility(), 8);

			// 7 points
			player.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("oxyLung", LANGUAGE));
			Assert.assertEquals(player.getCyberneticsIncompatibility(), 15);

			// 7 points
			player.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("xEyes", LANGUAGE));
			Assert.assertEquals(player.getCyberneticsIncompatibility(), 22);
		} catch (TooManyCyberneticDevicesException e) {
			// Not correct
			Assert.assertTrue(false);
		}

		player.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("jonah", LANGUAGE));
	}

	@Test(expectedExceptions = { TooManyCyberneticDevicesException.class })
	public void cyberneticAsAWeapon() throws TooManyCyberneticDevicesException, InvalidXmlElementException {
		CharacterPlayer player = new CharacterPlayer(LANGUAGE);
		player.getCharacteristic(CharacteristicName.WILL).setValue(7);

		player.addCybernetics(CyberneticDeviceFactory.getInstance().getElement("centurionKnife", LANGUAGE));
		Assert.assertEquals(player.getWeapons().getElements().size(), 1);
	}
}
