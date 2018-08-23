package com.softwaremagico.tm.character.characteristics;

import java.util.Objects;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.values.IValue;

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

public class CharacteristicDefinition extends Element<CharacteristicDefinition> implements Comparable<CharacteristicDefinition>, IValue {
	private String abbreviature;
	private CharacteristicType type;

	public CharacteristicDefinition(String id, String name) {
		super(id, name);
	}

	@Override
	public String toString() {
		return getName().toString();
	}

	public String getAbbreviature() {
		return abbreviature;
	}

	protected void setAbbreviature(String abbreviature) {
		this.abbreviature = abbreviature;
	}

	public CharacteristicType getType() {
		return type;
	}

	protected void setType(CharacteristicType type) {
		this.type = type;
	}

	public CharacteristicName getCharacteristicName() {
		for (CharacteristicName characteristicName : CharacteristicName.values()) {
			if (Objects.equals(characteristicName.getId().toLowerCase(), getId().toLowerCase())) {
				return characteristicName;
			}
		}
		return null;
	}

	@Override
	public int compareTo(CharacteristicDefinition characteristic) {
		return getCharacteristicName().compareTo(characteristic.getCharacteristicName());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((abbreviature == null) ? 0 : abbreviature.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		CharacteristicDefinition other = (CharacteristicDefinition) obj;
		if (abbreviature == null) {
			if (other.abbreviature != null)
				return false;
		} else if (!abbreviature.equals(other.abbreviature))
			return false;
		return true;
	}
}
