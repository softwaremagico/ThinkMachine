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

public class CharacteristicImprovement {
	private CharacteristicDefinition characteristic;
	private int bonus;
	private boolean always;

	public CharacteristicImprovement(CharacteristicDefinition characteristic, int bonus, boolean always) {
		super();
		this.characteristic = characteristic;
		this.bonus = bonus;
		this.always = always;
	}

	public CharacteristicDefinition getCharacteristic() {
		return characteristic;
	}

	public int getBonus() {
		return bonus;
	}

	public boolean isAlways() {
		return always;
	}
}
