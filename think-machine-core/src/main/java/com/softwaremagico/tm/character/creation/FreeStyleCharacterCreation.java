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

import java.util.HashMap;

import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.skills.AvailableSkill;

public class FreeStyleCharacterCreation {
	public static final int MAX_INITIAL_SKILL_VALUE = 8;

	public static final int CHARACTERISTICS_POINTS = 20;
	public static final int SKILLS_POINTS = 30;
	public static final int TRAITS_POINTS = 10;
	public static final int FREE_AVAILABLE_POINTS = 40;
	public static final int MAX_CURSE_POINTS = 7;
	public static final int MAX_BLESSING_MODIFICATIONS = 7;

	private final HashMap<CharacteristicName, Integer> selectedCharacteristicsValues;

	private final HashMap<AvailableSkill, Integer> desiredSkillRanks;

	public FreeStyleCharacterCreation() {
		selectedCharacteristicsValues = new HashMap<>();
		desiredSkillRanks = new HashMap<>();
	}

	public HashMap<AvailableSkill, Integer> getDesiredSkillRanks() {
		return desiredSkillRanks;
	}

	public HashMap<CharacteristicName, Integer> getSelectedCharacteristicsValues() {
		return selectedCharacteristicsValues;
	}

}
