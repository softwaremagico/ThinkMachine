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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Characteristics {
	private Map<CharacteristicType, List<Characteristic>> characteristics;

	public Characteristics() {
		createCharacteristics();
	}

	private void createCharacteristics() {
		characteristics = new HashMap<>();
		for (CharacteristicType type : CharacteristicType.values()) {
			if (characteristics.get(type) == null) {
				characteristics.put(type, new ArrayList<Characteristic>());
			}
			for (CharacteristicName characteristicName : type.getCharacteristics()) {
				characteristics.get(type).add(new Characteristic(characteristicName));
			}
		}
	}

	public Characteristic getCharacteristic(CharacteristicName characteristicName) {
		for (CharacteristicType type : CharacteristicType.values()) {
			for (Characteristic characteristic : characteristics.get(type)) {
				if (characteristic.getName().equals(characteristicName)) {
					return characteristic;
				}
			}
		}
		return null;
	}
}
