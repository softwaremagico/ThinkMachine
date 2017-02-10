package com.softwaremagico.tm.character.characteristics;

public enum CharacteristicName {

	STRENGTH,

	DEXTERITY,

	ENDURANCE,

	WITS,

	PERCEPTION,

	TECH,

	PRESENCE,

	WILL,

	FAITH,

	INITIATIVE,

	MOVEMENT,

	DEFENSE;

	private CharacteristicName() {

	}

	public String getTranslationTag() {
		return name().toLowerCase() + "Characteristic";
	}

	@Override
	public String toString() {
		return name().toLowerCase();
	}
}
