package com.softwaremagico.tm.character.creation;

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

public class FreeStyleCharacterCreation {
	private static final int MAX_INITIAL_SKILL_VALUE = 8;
	private static final int CHARACTERISTICS_POINTS = 20;
	private static final int SKILLS_POINTS = 30;
	private static final int TRAITS_POINTS = 10;
	private static final int FREE_AVAILABLE_POINTS = 40;
	private static final int MAX_CURSE_POINTS = 7;
	private static final int MAX_BLESSING_MODIFICATIONS = 7;

	public static int getMaxInitialSkillsValues(Integer age) {
		return MAX_INITIAL_SKILL_VALUE;
	}

	public static int getCharacteristicsPoints(Integer age) {
		return CHARACTERISTICS_POINTS;
	}

	public static int getSkillsPoints(Integer age) {
		return SKILLS_POINTS;
	}

	public static int getTraitsPoints(Integer age) {
		return TRAITS_POINTS;
	}

	public static int getFreeAvailablePoints(Integer age) {
		return FREE_AVAILABLE_POINTS;
	}

	public static int getMaxCursePoints(Integer age) {
		return MAX_CURSE_POINTS;
	}

	public static int getMaxBlessingModifications(Integer age) {
		return MAX_BLESSING_MODIFICATIONS;
	}

}
