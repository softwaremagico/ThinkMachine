package com.softwaremagico.tm.character;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.characteristics.CharacteristicType;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;

public class CharacterPlayer {
	private static ITranslator translator = LanguagePool.getTranslator("character_values.xml");

	private CharacterInfo info;
	private Map<CharacteristicType, List<Characteristic>> characteristics;

	public CharacterPlayer() {
		reset();
	}

	private void reset() {
		info = new CharacterInfo();

		characteristics = new HashMap<>();
	}

	public CharacterInfo getInfo() {
		return info;
	}

	public void setInfo(CharacterInfo info) {
		this.info = info;
	}

}
