package com.softwaremagico.tm.random.selectors;

import com.softwaremagico.tm.character.skills.SkillGroup;

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

public enum SkillGroupPreferences implements IRandomPreferences {

	ANALYTICAL(SkillGroup.ANALYTICAL),

	COMBAT(SkillGroup.COMBAT),

	CONTROL(SkillGroup.CONTROL),

	CREATIVE(SkillGroup.CREATIVE),

	LORE(SkillGroup.LORE),

	MALEFACTION(SkillGroup.MALEFACTION),

	PHYSICAL(SkillGroup.PHYSICAL),

	SCIENCE(SkillGroup.SCIENCE),

	SOCIAL(SkillGroup.SOCIAL),

	TECHNICAL(SkillGroup.TECHNICAL);

	private final SkillGroup skillGroup;

	private SkillGroupPreferences(SkillGroup skillGroup) {
		this.skillGroup = skillGroup;
	}

	public static SkillGroupPreferences getSkillGroupPreference(String tag) {
		if (tag != null) {
			for (SkillGroupPreferences skillGroupPreference : SkillGroupPreferences.values()) {
				if (skillGroupPreference.name().equalsIgnoreCase(tag.toLowerCase())) {
					return skillGroupPreference;
				}
			}
		}
		return null;
	}

	@Override
	public int maximum() {
		return SpecializationPreferences.SPECIALIZED.maximum();
	}

	@Override
	public int minimum() {
		return SpecializationPreferences.SPECIALIZED.minimum();
	}

	public SkillGroup getSkillGroup() {
		return skillGroup;
	}

}
