package com.softwaremagico.tm.character.planets;

import java.util.Set;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;

public class PlanetFactory extends XmlFactory<Planet> {
	private static final ITranslator translator = LanguagePool.getTranslator("planets.xml");

	private static final String NAME = "name";
	private static final String FACTION = "factions";

	private static class PlanetFactoryInit {
		public static final PlanetFactory INSTANCE = new PlanetFactory();
	}

	public static PlanetFactory getInstance() {
		return PlanetFactoryInit.INSTANCE;
	}

	@Override
	protected Planet createElement(ITranslator translator, String planetId, String language)
			throws InvalidXmlElementException {
		try {
			final String name = translator.getNodeValue(planetId, NAME, language);

			final Set<Faction> factions;
			try {
				factions = getCommaSeparatedValues(planetId, FACTION, language, FactionsFactory.getInstance());
			} catch (InvalidXmlElementException ixe) {
				throw new InvalidPlanetException(
						"Error in planet '" + planetId + "' structure. Invalid faction defintion.", ixe);
			}

			return new Planet(planetId, name, language, factions);
		} catch (Exception e) {
			throw new InvalidPlanetException("Invalid structure in planet '" + planetId + "'.", e);
		}
	}

	@Override
	protected ITranslator getTranslator() {
		return translator;
	}
}
