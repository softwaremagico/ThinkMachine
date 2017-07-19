package com.softwaremagico.tm.character.race;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.log.MachineLog;

public class RaceFactory {
	private final static String MAX_VALUE = "maximumValue";
	private final static String VALUE = "value";

	private static Map<String, List<Race>> races = new HashMap<>();
	private static ITranslator translatorRaces = LanguagePool.getTranslator("races.xml");

	public static List<Race> getRaces(String language) {
		if (races.get(language) == null) {
			races.put(language, new ArrayList<Race>());
			for (String skillId : translatorRaces.getAllTranslatedElements()) {
				races.get(language).add(createRace(translatorRaces, skillId, language));
			}
			Collections.sort(races.get(language));
		}
		return races.get(language);
	}

	public static Race getRace(String raceName, String language) {
		List<Race> races = getRaces(language);
		for (Race race : races) {
			if (Objects.equals(race.getName(), raceName)) {
				return race;
			}
		}
		return null;
	}

	private static Race createRace(ITranslator translator, String raceId, String language) {
		Race race = new Race(translator.getTranslatedText(raceId, language));
		for (CharacteristicName characteristic : CharacteristicName.values()) {
			try {
				String maxValue = translator.getNodeValue(raceId, characteristic.getTranslationTag(), MAX_VALUE);
				if (maxValue != null) {
					race.setMaximumValue(characteristic, Integer.parseInt(maxValue));
				}
				String value = translator.getNodeValue(raceId, characteristic.getTranslationTag(), VALUE);
				if (value != null) {
					race.setValue(characteristic, Integer.parseInt(value));
				}
			} catch (NumberFormatException nfe) {
				MachineLog.severe(Race.class.getName(), "Invalid value for characteristic '" + characteristic.getTranslationTag() + "' in race '" + raceId
						+ "'.");
			}
		}

		return race;
	}
}
