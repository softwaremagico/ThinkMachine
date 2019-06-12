package com.softwaremagico.tm.factory;

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

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.skills.SkillsDefinitionsFactory;

@Test(groups = { "skillFactory" })
public class SkillFactoryTests {
	private static final int NATURAL_SKILLS = 9;
	private static final int LEARNED_SKILLS = 47;

	@Test
	public void readSkills() throws InvalidXmlElementException {
		Assert.assertEquals(NATURAL_SKILLS, SkillsDefinitionsFactory.getInstance().getNaturalSkills("es").size());
		Assert.assertEquals(LEARNED_SKILLS, SkillsDefinitionsFactory.getInstance().getLearnedSkills("es").size());
	}
}
