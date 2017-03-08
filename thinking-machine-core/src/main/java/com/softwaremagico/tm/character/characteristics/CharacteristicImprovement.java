package com.softwaremagico.tm.character.characteristics;

public class CharacteristicImprovement {
	private CharacteristicName characteristicName;
	private int bonus;
	private boolean always;

	public CharacteristicImprovement(CharacteristicName characteristicName, int bonus, boolean always) {
		super();
		this.characteristicName = characteristicName;
		this.bonus = bonus;
		this.always = always;
	}

	public CharacteristicName getCharacteristicName() {
		return characteristicName;
	}

	public int getBonus() {
		return bonus;
	}

	public boolean isAlways() {
		return always;
	}
}
