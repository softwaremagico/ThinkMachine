package com.softwaremagico.tm.random;

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
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.character.skills.RandomSkills;
import com.softwaremagico.tm.random.exceptions.DuplicatedPreferenceException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;

@Test(groups = { "randomDefinition" })
public class RandomDefinitions {
	private static final String LANGUAGE = "en";

	@Test
	public void checkRandomProbabilityMultiplier() throws InvalidXmlElementException {
		Assert.assertEquals(WeaponFactory.getInstance().getElement("veryLargeRock", LANGUAGE, MODULE).getRandomDefinition().getProbabilityMultiplier(), 0d);
	}

	@Test
	public void removeElementWeight() throws DuplicatedPreferenceException, InvalidXmlElementException, InvalidRandomElementSelectedException {
		final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, MODULE);

		final AvailableSkill ride = AvailableSkillsFactory.getInstance().getElement("ride", LANGUAGE, MODULE);
		final AvailableSkill warfare = AvailableSkillsFactory.getInstance().getElement("warfare", LANGUAGE, MODULE);

		final RandomSkills originalSkills = new RandomSkills(characterPlayer, null);
		final RandomSkills editedSkills = new RandomSkills(characterPlayer, null);

		Assert.assertEquals(originalSkills.getAssignedWeight(ride), editedSkills.getAssignedWeight(ride));
		Assert.assertEquals(originalSkills.getAssignedWeight(warfare), editedSkills.getAssignedWeight(warfare));
		Assert.assertEquals(originalSkills.getWeightedElements().size(), editedSkills.getWeightedElements().size());

		editedSkills.removeElementWeight(ride);

		Assert.assertEquals(editedSkills.getWeightedElements().size(), originalSkills.getWeightedElements().size() - 1);
		Assert.assertNull(editedSkills.getAssignedWeight(ride));
		Assert.assertNotNull(originalSkills.getAssignedWeight(ride));
		Assert.assertEquals(originalSkills.getAssignedWeight(warfare), editedSkills.getAssignedWeight(warfare));

	}
}
