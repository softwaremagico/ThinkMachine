package com.softwaremagico.tm.character.characteristics;

public class CharacteristicValue {
	private CharacteristicName characteristic;
	private int value;

	public CharacteristicValue(CharacteristicName characteristic, int value) {
		super();
		this.characteristic = characteristic;
		this.value = value;
	}

	public CharacteristicName getCharacteristic() {
		return characteristic;
	}

	public void setCharacteristic(CharacteristicName characteristic) {
		this.characteristic = characteristic;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
