package com.softwaremagico.tm.character.characteristics;

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
