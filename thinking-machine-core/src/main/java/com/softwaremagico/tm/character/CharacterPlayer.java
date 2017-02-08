package com.softwaremagico.tm.character;

import com.softwaremagico.tm.character.characteristics.Characteristics;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;

public class CharacterPlayer {
	private static ITranslator translator = LanguagePool.getTranslator("character_values.xml");

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

}
