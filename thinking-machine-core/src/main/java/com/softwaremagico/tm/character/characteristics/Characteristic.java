package com.softwaremagico.tm.character.characteristics;

public class Characteristic {
	private CharacteristicName name;
	private Integer value;

	public Characteristic(CharacteristicName name) {
		this.name = name;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public CharacteristicName getName() {
		return name;
	}
}
