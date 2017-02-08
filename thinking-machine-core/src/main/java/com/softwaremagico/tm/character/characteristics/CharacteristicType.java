package com.softwaremagico.tm.character.characteristics;

public enum CharacteristicType {

	BODY(CharacteristicName.STRENGTH, CharacteristicName.DEXTERITY, CharacteristicName.ENDURANCE),

	MIND(CharacteristicName.WITS, CharacteristicName.PERCEPTION, CharacteristicName.TECH),

	SPIRIT(CharacteristicName.PRESENCE, CharacteristicName.WILL, CharacteristicName.FAITH),

	OTHERS(CharacteristicName.INITIATIVE, CharacteristicName.MOVEMENT, CharacteristicName.DEFENSE);

	private final CharacteristicName[] characteristics;

	private CharacteristicType(CharacteristicName... characteristcs) {
		this.characteristics = characteristcs;
	}

	public CharacteristicName[] getCharacteristics() {
		return characteristics;
	}

	public String getTranslationTag() {
		return name().toLowerCase() + "Characteristics";
	}

}
