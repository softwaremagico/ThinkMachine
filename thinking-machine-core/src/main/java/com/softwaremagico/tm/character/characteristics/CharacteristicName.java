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

	public static CharacteristicName[] getBasicCharacteristics() {
		return new CharacteristicName[] { CharacteristicName.STRENGTH, CharacteristicName.DEXTERITY, CharacteristicName.ENDURANCE,
				CharacteristicName.WILL, CharacteristicName.PERCEPTION, CharacteristicName.TECH, CharacteristicName.PRESENCE,
				CharacteristicName.WITS, CharacteristicName.FAITH };
	}
}
