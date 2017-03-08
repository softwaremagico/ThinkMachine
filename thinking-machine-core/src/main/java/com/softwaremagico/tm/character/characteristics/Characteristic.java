package com.softwaremagico.tm.character.characteristics;

/*-
 * #%L
 * The Thinking Machine (Core)
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

public class Characteristic {
	private CharacteristicName name;
	private Integer value;

	public Characteristic(CharacteristicName name) {
		this.name = name;
	}

	public Integer getValue() {
		if (value != null) {
			return value;
		}
		return 0;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public CharacteristicName getName() {
		return name;
	}

	@Override
	public String toString() {
		return getName().toString();
	}
}
