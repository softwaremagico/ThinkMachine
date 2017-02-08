package com.softwaremagico.tm.character;

import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;

public class CharacterPlayer {
	private static ITranslator translator = LanguagePool.getTranslator("character_values.xml");

	private CharacterInfo info;

	public CharacterPlayer() {
		reset();
	}

	private void reset() {
		info = new CharacterInfo();
	}

	public CharacterInfo getInfo() {
		return info;
	}

	public void setInfo(CharacterInfo info) {
		this.info = info;
	}

}
