package com.softwaremagico.tm.character.skills;

import com.softwaremagico.tm.character.characteristics.CharacteristicType;

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

public enum SkillGroup {

	ANALYTICAL(CharacteristicType.MIND),

	COMBAT(CharacteristicType.BODY),

	CONTROL(null),

	CREATIVE(null),

	LORE(CharacteristicType.MIND),

	MALEFACTION(null),

	PHYSICAL(CharacteristicType.BODY),

	SCIENCE(CharacteristicType.MIND),

	SOCIAL(CharacteristicType.SPIRIT),

	TECHNICAL(CharacteristicType.MIND);

	private final CharacteristicType preferredCharacteristicsGroups;

	private SkillGroup(CharacteristicType preferredCharacteristicsGroups) {
		this.preferredCharacteristicsGroups = preferredCharacteristicsGroups;
	}

	public static SkillGroup getSkillGroup(String tag) {
		if (tag != null) {
			for (SkillGroup skillGroup : SkillGroup.values()) {
				if (skillGroup.name().toLowerCase().equals(tag.toLowerCase())) {
					return skillGroup;
				}
			}
		}
		return null;
	}

	public CharacteristicType getPreferredCharacteristicsGroups() {
		return preferredCharacteristicsGroups;
	}
}
