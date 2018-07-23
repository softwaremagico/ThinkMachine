package com.softwaremagico.tm.character.race;

import com.softwaremagico.tm.character.characteristics.CharacteristicName;

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

public class RaceCharacteristic {
	private CharacteristicName characteristic;
	private int initialValue;
	private int maximumValue;

	public RaceCharacteristic(CharacteristicName characteristic) {
		super();
		this.characteristic = characteristic;
	}

	public CharacteristicName getCharacteristic() {
		return characteristic;
	}

	public void setCharacteristic(CharacteristicName characteristic) {
		this.characteristic = characteristic;
	}

	public int getInitialValue() {
		return initialValue;
	}

	public void setInitialValue(int value) {
		this.initialValue = value;
	}

	public int getMaximumValue() {
		return maximumValue;
	}

	public void setMaximumValue(int max) {
		this.maximumValue = max;
	}

}
