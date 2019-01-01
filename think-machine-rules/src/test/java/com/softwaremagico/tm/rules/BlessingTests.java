package com.softwaremagico.tm.rules;

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
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.blessings.BlessingFactory;
import com.softwaremagico.tm.character.blessings.TooManyBlessingsException;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.characters.CustomCharacter;

@Test(groups = { "blessings" })
public class BlessingTests {
	private final static String LANGUAGE = "es";

	@Test
	public void checkVitalityModifications() throws InvalidXmlElementException, TooManyBlessingsException {
		CharacterPlayer player = CustomCharacter.create(LANGUAGE);
		int vitality = player.getVitalityValue();
		player.addBlessing(BlessingFactory.getInstance().getElement("incurableDisease", LANGUAGE));
		Assert.assertEquals((int) player.getVitalityValue(), vitality - 1);
	}

	@Test
	public void checkMovementModifications() throws InvalidXmlElementException, TooManyBlessingsException {
		CharacterPlayer player = CustomCharacter.create(LANGUAGE);
		int movement = player.getValue(CharacteristicName.MOVEMENT);
		player.addBlessing(BlessingFactory.getInstance().getElement("limp", LANGUAGE));
		Assert.assertEquals((int) player.getValue(CharacteristicName.MOVEMENT), movement - 1);
	}

	@Test
	public void checkRangedAttacksModifications() throws InvalidXmlElementException, TooManyBlessingsException {
		CharacterPlayer player = CustomCharacter.create(LANGUAGE);
		Assert.assertEquals((int) player.getSkillTotalRanks(AvailableSkillsFactory.getInstance().getElement(
				"energyGuns", LANGUAGE)), 6);
	}

	@Test
	public void getAffectedSkills() throws InvalidXmlElementException {
		Blessing missingEye = BlessingFactory.getInstance().getElement("missingEye", LANGUAGE);
		Assert.assertEquals(missingEye.getAffectedSkill(LANGUAGE).size(), 5);
		
		Blessing pilot = BlessingFactory.getInstance().getElement("crackPilot", LANGUAGE);
		Assert.assertEquals(pilot.getAffectedSkill(LANGUAGE).size(), 6);
		
		Blessing hacker = BlessingFactory.getInstance().getElement("hacker", LANGUAGE);
		Assert.assertEquals(hacker.getAffectedSkill(LANGUAGE).size(), 1);
	}
}
