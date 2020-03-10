package com.softwaremagico.tm.character.characteristics;

import com.softwaremagico.tm.json.ExcludeFromJson;

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

public class Characteristic extends CharacteristicDefinition {
	public static final int DEFAULT_INITIAL_VALUE = 3;
	public static final int MAX_VALUE = 12;
	private int value = DEFAULT_INITIAL_VALUE;

	@ExcludeFromJson
	private final CharacteristicDefinition characteristicDefinition;

	public Characteristic(CharacteristicDefinition characteristicDefinition) {
		super(characteristicDefinition.getId(), characteristicDefinition.getName(), characteristicDefinition.getLanguage(),
				characteristicDefinition.getModuleName());
		setAbbreviature(characteristicDefinition.getAbbreviature());
		setType(characteristicDefinition.getType());
		this.characteristicDefinition = characteristicDefinition;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public CharacteristicDefinition getCharacteristicDefinition() {
		return characteristicDefinition;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
}
