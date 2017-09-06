package com.softwaremagico.tm.character.race;

/*-
 * #%L
 * The Thinking Machine (Core)
 * %%
 * Copyright (C) 2017 Softwaremagico
 * %%
 * This software is designed by Jorge Hortelano Otero. Jorge Hortelano Otero
 * <softwaremagico@gmail.com> Valencia (Spain).
 *  
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *  
 * You should have received a copy of the GNU General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.character.OccultismType;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.traits.InvalidBlessingException;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;

public class RaceFactory extends XmlFactory<Race> {
	private final static ITranslator TRANSLATOR_RACE = LanguagePool.getTranslator("races.xml");

	private final static String NAME = "name";
	private final static String MAX_VALUE = "maximumValue";
	private final static String VALUE = "value";
	private final static String COST = "cost";

	private static RaceFactory instance;

	private static void createInstance() {
		if (instance == null) {
			synchronized (RaceFactory.class) {
				if (instance == null) {
					instance = new RaceFactory();
				}
			}
		}
	}

	public static RaceFactory getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}

	@Override
	protected ITranslator getTranslator() {
		return TRANSLATOR_RACE;
	}

	@Override
	protected Race createElement(ITranslator translator, String raceId, String language) throws InvalidXmlElementException {
		Race race = null;
		try {
			String name = translator.getNodeValue(raceId, NAME, language);
			race = new Race(raceId, name);
		} catch (Exception e) {
			throw new InvalidBlessingException("Invalid name in race '" + raceId + "'.");
		}
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
