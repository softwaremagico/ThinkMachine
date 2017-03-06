package com.softwaremagico.tm.character.characteristics;

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
