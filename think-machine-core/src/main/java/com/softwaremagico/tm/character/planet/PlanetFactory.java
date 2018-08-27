package com.softwaremagico.tm.character.planet;

/*-
 * #%L
 * Think Machine (Core)
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
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;

public class PlanetFactory extends XmlFactory<Planet> {
	private final static ITranslator translator = LanguagePool.getTranslator("planets.xml");

	private final static String NAME = "name";
	private final static String FACTION = "faction";

	private static PlanetFactory instance;

	private static void createInstance() {
		if (instance == null) {
			synchronized (PlanetFactory.class) {
				if (instance == null) {
					instance = new PlanetFactory();
				}
			}
		}
	}

	public static PlanetFactory getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}

	@Override
	protected Planet createElement(ITranslator translator, String planetId, String language) throws InvalidXmlElementException {
		try {
			String name = translator.getNodeValue(planetId, NAME, language);
			String factionName = translator.getNodeValue(planetId, FACTION);
			return new Planet(planetId, name, FactionsFactory.getInstance().getElement(factionName, language));
		} catch (Exception e) {
			throw new InvalidPlanetException("Invalid structure in planet '" + planetId + "'.", e);
		}
	}

	@Override
	protected ITranslator getTranslator() {
		return translator;
	}
}
