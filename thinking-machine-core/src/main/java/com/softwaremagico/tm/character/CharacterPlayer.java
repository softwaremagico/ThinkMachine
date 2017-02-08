package com.softwaremagico.tm.character;

import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.characteristics.Characteristics;

public class CharacterPlayer {

	// Basic description of the character.
	private CharacterInfo info;

	// Characteristics.
	private Characteristics characteristics;

	public CharacterPlayer() {
		reset();
	}

	private void reset() {
		info = new CharacterInfo();
		characteristics = new Characteristics();
	}

	public CharacterInfo getInfo() {
		return info;
	}

	public void setInfo(CharacterInfo info) {
		this.info = info;
	}

	public Characteristics getCharacteristics() {
		return characteristics;
	}

	/**
	 * Gets the starting value for a characteristic depending on the race.
	 * 
	 * @param characteristicName
	 * @return
	 */
	public Integer getStartingValue(CharacteristicName characteristicName) {
		if (CharacteristicName.DEFENSE.equals(characteristicName)) {
			return 1;
		}
		if (CharacteristicName.MOVEMENT.equals(characteristicName)) {
			return 5;
		}
		return 3;
	}

}
