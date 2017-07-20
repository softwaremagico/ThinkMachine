package com.softwaremagico.tm.character.race;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.softwaremagico.tm.character.OccultismType;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;

public class RaceFactory {
	private final static String MAX_VALUE = "maximumValue";
	private final static String VALUE = "value";
	private final static String COST = "cost";

	private static Map<String, List<Race>> races = new HashMap<>();
	private static ITranslator translatorRaces = LanguagePool.getTranslator("races.xml");

	public static List<Race> getRaces(String language) throws InvalidRaceException {
		if (races.get(language) == null) {
			races.put(language, new ArrayList<Race>());
			for (String skillId : translatorRaces.getAllTranslatedElements()) {
				races.get(language).add(createRace(translatorRaces, skillId, language));
			}
			Collections.sort(races.get(language));
		}
		return races.get(language);
	}

	public static Race getRace(String raceName, String language) throws InvalidRaceException {
		List<Race> races = getRaces(language);
		for (Race race : races) {
			if (race.getName() != null) {
				if (Objects.equals(race.getName().toLowerCase(), raceName.toLowerCase())) {
					return race;
				}
			}
		}
		return null;
	}

	private static Race createRace(ITranslator translator, String raceId, String language) throws InvalidRaceException {
		Race race = new Race(translator.getTranslatedText(raceId, language));
		try {
			String cost = translator.getNodeValue(raceId, COST);
			race.setCost(Integer.parseInt(cost));
		} catch (Exception e) {
			throw new InvalidRaceException("Invalid cost in race '" + raceId + "'.");
		}
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
				throw new InvalidRaceException("Invalid value for characteristic '" + characteristic.getTranslationTag() + "' in race '" + raceId + "'.");
			}
		}
		try {
			String psi = translator.getNodeValue(raceId, OccultismType.PSI.getTag());
			race.setPsi(Integer.parseInt(psi));
		} catch (Exception e) {
			throw new InvalidRaceException("Invalid psi value in race '" + raceId + "'.");
		}
		try {
			String urge = translator.getNodeValue(raceId, OccultismType.PSI.getDarkSideTag());
			race.setUrge(Integer.parseInt(urge));
		} catch (Exception e) {
			throw new InvalidRaceException("Invalid urge value in race '" + raceId + "'.");
		}
		try {
			String theurgy = translator.getNodeValue(raceId, OccultismType.THEURGY.getTag());
			race.setTheurgy(Integer.parseInt(theurgy));
		} catch (Exception e) {
			throw new InvalidRaceException("Invalid theurgy value in race '" + raceId + "'.");
		}
		try {
			String hubris = translator.getNodeValue(raceId, OccultismType.THEURGY.getDarkSideTag());
			race.setHubris(Integer.parseInt(hubris));
		} catch (Exception e) {
			throw new InvalidRaceException("Invalid hubris value in race '" + raceId + "'.");
		}
		return race;
	}
}
