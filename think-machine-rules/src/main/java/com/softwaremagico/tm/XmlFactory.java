package com.softwaremagico.tm;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;

import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.races.RaceFactory;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.Language;
import com.softwaremagico.tm.log.MachineLog;
import com.softwaremagico.tm.random.definition.RandomProbabilityDefinition;

public abstract class XmlFactory<T extends Element<T>> {
	protected Map<String, List<T>> elements = new HashMap<>();

	private final static String RANDOM = "random";
	private final static String ELEMENT_PROBABILITY_MULTIPLIER = "probabilityMultiplier";
	private final static String RESTRICTED_FACTIONS = "restrictedFactions";
	private final static String MIN_TECH_LEVEL = "minTechLevel";
	private final static String MAX_TECH_LEVEL = "maxTechLevel";
	private final static String RECOMMENDED_FACTIONS = "recommendedFactions";
	private final static String RECOMMENDED_FACTION_GROUPS = "recommendedFactionGroups";
	private final static String RESTRICTED_FACTION_GROUPS = "restrictedFactionGroups";
	private final static String RESTRICTED_RACES = "restrictedRaces";
	private final static String FORBIDDEN_RACES = "forbiddenRaces";
	private final static String RECOMMENDED_RACES = "recommendedRaces";
	private final static String GENERAL_PROBABILITY = "generalProbability";
	private final static String STATIC_PROBABILITY = "staticProbability";

	protected XmlFactory() {
		initialize();
	}

	protected void initialize() {
		List<Language> languages = getTranslator().getAvailableLanguages();
		List<T> elements = new ArrayList<>();
		for (Language language : languages) {
			try {
				elements = getElements(language.getAbbreviature());
			} catch (InvalidXmlElementException e) {
				MachineLog.errorMessage(this.getClass().getName(), e);
			}
		}
		MachineLog.debug(this.getClass().getName(), "Loaded " + elements.size() + " elements at '" + this.getClass().getSimpleName() + "'.");
	}

	protected abstract ITranslator getTranslator();

	public void clearCache() {
		elements = new HashMap<>();
		initialize();
	}

	protected void setRandomConfiguration(Element<?> element, ITranslator translator, String language) throws InvalidXmlElementException {
		// Is an element restricted to a faction?
		try {
			String restrictedFactionsId = translator.getNodeValue(element.getId(), RANDOM, RESTRICTED_FACTIONS);
			if (restrictedFactionsId != null) {
				StringTokenizer factionTokenizer = new StringTokenizer(restrictedFactionsId, ",");
				while (factionTokenizer.hasMoreTokens()) {
					element.getRandomDefinition().getRestrictedFactions()
							.add(FactionsFactory.getInstance().getElement(factionTokenizer.nextToken().trim(), language));
				}
			}
		} catch (NullPointerException npe) {
			// Optional
		}

		try {
			String elementProbability = getTranslator().getNodeValue(element.getId(), RANDOM, ELEMENT_PROBABILITY_MULTIPLIER);
			if (elementProbability != null) {
				element.getRandomDefinition().setProbabilityMultiplier(Double.parseDouble(elementProbability));
			} else {
				element.getRandomDefinition().setProbabilityMultiplier(1d);
			}
		} catch (NumberFormatException nfe) {
			throw new InvalidXmlElementException("Invalid number value for element probability in '" + element.getId() + "'.");
		} catch (NullPointerException npe) {
			// Optional
		}

		try {
			String minTechLevel = translator.getNodeValue(element.getId(), RANDOM, MIN_TECH_LEVEL);
			if (minTechLevel != null) {
				element.getRandomDefinition().setMinimumTechLevel(Integer.parseInt(minTechLevel));
			}
		} catch (NumberFormatException nfe) {
			throw new InvalidXmlElementException("Invalid number value for techlevel in element '" + element.getId() + "'.");
		} catch (NullPointerException npe) {
			// Optional
		}

		try {
			String maxTechLevel = translator.getNodeValue(element.getId(), RANDOM, MAX_TECH_LEVEL);
			if (maxTechLevel != null) {
				element.getRandomDefinition().setMaximumTechLevel(Integer.parseInt(maxTechLevel));
			}
		} catch (NumberFormatException nfe) {
			throw new InvalidXmlElementException("Invalid number value for max techlevel in element '" + element.getId() + "'.");
		} catch (NullPointerException npe) {
			// Optional
		}

		try {
			String recommendedFactionGroups = translator.getNodeValue(element.getId(), RANDOM, RECOMMENDED_FACTION_GROUPS);
			if (recommendedFactionGroups != null) {
				StringTokenizer recommendedFactionGroupsTokenizer = new StringTokenizer(recommendedFactionGroups, ",");
				while (recommendedFactionGroupsTokenizer.hasMoreTokens()) {
					element.getRandomDefinition().addRecommendedFactionGroup(FactionGroup.get(recommendedFactionGroupsTokenizer.nextToken().trim()));
				}
			}
		} catch (NullPointerException npe) {
			// Optional
		}

		try {
			String restrictedFactionGroups = translator.getNodeValue(element.getId(), RANDOM, RESTRICTED_FACTION_GROUPS);
			if (restrictedFactionGroups != null) {
				StringTokenizer restrictedFactionGroupsTokenizer = new StringTokenizer(restrictedFactionGroups, ",");
				while (restrictedFactionGroupsTokenizer.hasMoreTokens()) {
					element.getRandomDefinition().addRestrictedFactionGroup(FactionGroup.get(restrictedFactionGroupsTokenizer.nextToken().trim()));
				}
			}
		} catch (NullPointerException npe) {
			// Optional
		}

		try {
			String recommendedFactions = translator.getNodeValue(element.getId(), RANDOM, RECOMMENDED_FACTIONS);
			if (recommendedFactions != null) {
				StringTokenizer recommendedFactionsOfSkill = new StringTokenizer(recommendedFactions, ",");
				while (recommendedFactionsOfSkill.hasMoreTokens()) {
					element.getRandomDefinition().addRecommendedFaction(
							FactionsFactory.getInstance().getElement(recommendedFactionsOfSkill.nextToken().trim(), language));
				}
			}
		} catch (NullPointerException npe) {
			// Optional
		}

		try {
			String restrictedRaces = translator.getNodeValue(element.getId(), RANDOM, RESTRICTED_RACES);
			if (restrictedRaces != null) {
				StringTokenizer restrictedRacesOfSkill = new StringTokenizer(restrictedRaces, ",");
				while (restrictedRacesOfSkill.hasMoreTokens()) {
					element.getRandomDefinition().addRestrictedRace(RaceFactory.getInstance().getElement(restrictedRacesOfSkill.nextToken().trim(), language));
				}
			}
		} catch (NullPointerException npe) {
			// Optional
		}

		try {
			String recommendedRaces = translator.getNodeValue(element.getId(), RANDOM, RECOMMENDED_RACES);
			if (recommendedRaces != null) {
				StringTokenizer recommendedRacesTokenizer = new StringTokenizer(recommendedRaces, ",");
				while (recommendedRacesTokenizer.hasMoreTokens()) {
					element.getRandomDefinition().addRecommendedRace(
							RaceFactory.getInstance().getElement(recommendedRacesTokenizer.nextToken().trim(), language));
				}
			}
		} catch (NullPointerException npe) {
			// Optional
		}

		try {
			String forbiddenRaces = translator.getNodeValue(element.getId(), RANDOM, FORBIDDEN_RACES);
			if (forbiddenRaces != null) {
				StringTokenizer forbiddenRacesTokenizer = new StringTokenizer(forbiddenRaces, ",");
				while (forbiddenRacesTokenizer.hasMoreTokens()) {
					element.getRandomDefinition().addForbiddenRace(RaceFactory.getInstance().getElement(forbiddenRacesTokenizer.nextToken().trim(), language));
				}
			}
		} catch (NullPointerException npe) {
			// Optional
		}

		try {
			String generalProbability = translator.getNodeValue(element.getId(), RANDOM, GENERAL_PROBABILITY);
			if (generalProbability != null) {
				element.getRandomDefinition().setProbability(RandomProbabilityDefinition.get(generalProbability));
			}
		} catch (NullPointerException npe) {
			// Optional
		}

		try {
			String staticProbability = translator.getNodeValue(element.getId(), RANDOM, STATIC_PROBABILITY);
			if (staticProbability != null) {
				element.getRandomDefinition().setStaticProbability(Integer.parseInt(staticProbability));
			}
		} catch (NumberFormatException nfe) {
			throw new InvalidXmlElementException("Invalid number value for element probability in '" + element.getId() + "'.");
		} catch (NullPointerException npe) {
			// Optional
		}
	}

	public List<T> getElements(String language) throws InvalidXmlElementException {
		if (elements.get(language) == null) {
			elements.put(language, new ArrayList<T>());
			for (String elementId : getTranslator().getAllTranslatedElements()) {
				T element = createElement(getTranslator(), elementId, language);
				setRandomConfiguration(element, getTranslator(), language);
				if (elements.get(language).contains(element)) {
					throw new ElementAlreadyExistsException("Element '" + element + "' already is inserted. Probably the ID is duplicated.");
				}
				elements.get(language).add(element);
			}
			Collections.sort(elements.get(language));
		}
		return elements.get(language);
	}

	public T getElement(String elementId, String language) throws InvalidXmlElementException {
		List<T> elements = getElements(language);
		for (T element : elements) {
			if (element.getId() != null) {
				if (Objects.equals(element.getId().toLowerCase(), elementId.trim().toLowerCase())) {
					return element;
				}
			}
		}
		throw new InvalidXmlElementException("Element '" + elementId + "' does not exists.");
	}

	protected abstract T createElement(ITranslator translator, String elementId, String language) throws InvalidXmlElementException;
}
