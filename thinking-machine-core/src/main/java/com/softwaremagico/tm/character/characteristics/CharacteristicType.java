package com.softwaremagico.tm.character.characteristics;

public enum CharacteristicType {

	BODY(CharacteristicName.STRENGTH, CharacteristicName.DEXTERITY, CharacteristicName.ENDURANCE),

	MIND(CharacteristicName.WITS, CharacteristicName.PERCEPTION, CharacteristicName.TECH),

	SPIRIT(CharacteristicName.PRESENCE, CharacteristicName.WILL, CharacteristicName.FAITH);

	private final CharacteristicName[] characteristics;

	private CharacteristicType(CharacteristicName... characteristcs) {
		this.characteristics = characteristcs;
	}

	public CharacteristicName[] getCharacteristics() {
		return characteristics;
	}

}
