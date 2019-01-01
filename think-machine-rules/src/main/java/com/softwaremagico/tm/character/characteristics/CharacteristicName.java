package com.softwaremagico.tm.character.characteristics;


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

public enum CharacteristicName {

	STRENGTH,

	DEXTERITY,

	ENDURANCE,

	WITS,

	PERCEPTION,

	TECH,

	PRESENCE,

	WILL,

	FAITH,

	INITIATIVE,

	MOVEMENT,

	DEFENSE;

	private CharacteristicName() {

	}

	public String getId() {
		return name().toLowerCase();
	}

	@Override
	public String toString() {
		return name().toLowerCase();
	}

	public static CharacteristicName[] getBasicCharacteristics() {
		return new CharacteristicName[] { CharacteristicName.STRENGTH, CharacteristicName.DEXTERITY,
				CharacteristicName.ENDURANCE, CharacteristicName.WITS, CharacteristicName.PERCEPTION,
				CharacteristicName.TECH, CharacteristicName.PRESENCE, CharacteristicName.WILL, CharacteristicName.FAITH };
	}

	public static CharacteristicName get(String tag) {
		for (CharacteristicName characteristicName : CharacteristicName.values()) {
			if (characteristicName.name().equalsIgnoreCase(tag)) {
				return characteristicName;
			}
		}
		return null;
	}
}
