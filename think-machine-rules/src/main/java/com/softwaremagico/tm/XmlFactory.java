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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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

	private static final String RANDOM = "random";
	private static final String ELEMENT_PROBABILITY_MULTIPLIER = "probabilityMultiplier";
	private static final String RESTRICTED_FACTIONS = "restrictedFactions";
	private static final String MIN_TECH_LEVEL = "minTechLevel";
	private static final String MAX_TECH_LEVEL = "maxTechLevel";
	private static final String RECOMMENDED_FACTIONS = "recommendedFactions";
	private static final String RECOMMENDED_FACTION_GROUPS = "recommendedFactionGroups";
	private static final String RESTRICTED_FACTION_GROUPS = "restrictedFactionGroups";
	private static final String RESTRICTED_RACES = "restrictedRaces";
	private static final String FORBIDDEN_RACES = "forbiddenRaces";
	private static final String RECOMMENDED_RACES = "recommendedRaces";
	private static final String GENERAL_PROBABILITY = "generalProbability";
	private static final String STATIC_PROBABILITY = "staticProbability";

	protected XmlFactory() {
		initialize();
	}

	protected void initialize() {
		final List<Language> languages = getTranslator().getAvailableLanguages();
		List<T> elements = new ArrayList<>();
		for (final Language language : languages) {
			try {
				elements = getElements(language.getAbbreviature());
			} catch (InvalidXmlElementException e) {
				MachineLog.errorMessage(this.getClass().getName(), e);
			}
		}
		MachineLog.debug(this.getClass().getName(),
				"Loaded " + elements.size() + " elements at '" + this.getClass().getSimpleName() + "'.");
	}

	protected abstract ITranslator getTranslator();

	public void clearCache() {
		elements = new HashMap<>();
		initialize();
	}

	protected void setRandomConfiguration(Element<?> element, ITranslator translator, String language)
			throws InvalidXmlElementException {
		// Is an element restricted to a faction?
		try {
			final String restrictedFactionsId = translator.getNodeValue(element.getId(), RANDOM, RESTRICTED_FACTIONS);
			if (restrictedFactionsId != null) {
				final StringTokenizer factionTokenizer = new StringTokenizer(restrictedFactionsId, ",");
				while (factionTokenizer.hasMoreTokens()) {
					element.getRandomDefinition().getRestrictedFactions().add(
							FactionsFactory.getInstance().getElement(factionTokenizer.nextToken().trim(), language));
				}
			}
		} catch (NullPointerException npe) {
			// Optional
		}

		try {
			final String elementProbability = getTranslator().getNodeValue(element.getId(), RANDOM,
					ELEMENT_PROBABILITY_MULTIPLIER);
			if (elementProbability != null) {
				element.getRandomDefinition().setProbabilityMultiplier(Double.parseDouble(elementProbability));
			} else {
				element.getRandomDefinition().setProbabilityMultiplier(1d);
			}
		} catch (NumberFormatException nfe) {
			throw new InvalidXmlElementException(
					"Invalid number value for element probability in '" + element.getId() + "'.");
		} catch (NullPointerException npe) {
			// Optional
		}

		try {
			final String minTechLevel = translator.getNodeValue(element.getId(), RANDOM, MIN_TECH_LEVEL);
			if (minTechLevel != null) {
				element.getRandomDefinition().setMinimumTechLevel(Integer.parseInt(minTechLevel));
			}
		} catch (NumberFormatException nfe) {
			throw new InvalidXmlElementException(
					"Invalid number value for techlevel in element '" + element.getId() + "'.");
		} catch (NullPointerException npe) {
			// Optional
		}

		try {
			final String maxTechLevel = translator.getNodeValue(element.getId(), RANDOM, MAX_TECH_LEVEL);
			if (maxTechLevel != null) {
				element.getRandomDefinition().setMaximumTechLevel(Integer.parseInt(maxTechLevel));
			}
		} catch (NumberFormatException nfe) {
			throw new InvalidXmlElementException(
					"Invalid number value for max techlevel in element '" + element.getId() + "'.");
		} catch (NullPointerException npe) {
			// Optional
		}

		try {
			final String recommendedFactionGroups = translator.getNodeValue(element.getId(), RANDOM,
					RECOMMENDED_FACTION_GROUPS);
			if (recommendedFactionGroups != null) {
				final StringTokenizer recommendedFactionGroupsTokenizer = new StringTokenizer(recommendedFactionGroups,
						",");
				while (recommendedFactionGroupsTokenizer.hasMoreTokens()) {
					element.getRandomDefinition().addRecommendedFactionGroup(
							FactionGroup.get(recommendedFactionGroupsTokenizer.nextToken().trim()));
				}
			}
		} catch (NullPointerException npe) {
			// Optional
		}

		try {
			final String restrictedFactionGroups = translator.getNodeValue(element.getId(), RANDOM,
					RESTRICTED_FACTION_GROUPS);
			if (restrictedFactionGroups != null) {
				final StringTokenizer restrictedFactionGroupsTokenizer = new StringTokenizer(restrictedFactionGroups,
						",");
				while (restrictedFactionGroupsTokenizer.hasMoreTokens()) {
					element.getRandomDefinition().addRestrictedFactionGroup(
							FactionGroup.get(restrictedFactionGroupsTokenizer.nextToken().trim()));
				}
			}
		} catch (NullPointerException npe) {
			// Optional
		}

		try {
			final String recommendedFactions = translator.getNodeValue(element.getId(), RANDOM, RECOMMENDED_FACTIONS);
			if (recommendedFactions != null) {
				final StringTokenizer recommendedFactionsOfSkill = new StringTokenizer(recommendedFactions, ",");
				while (recommendedFactionsOfSkill.hasMoreTokens()) {
					element.getRandomDefinition().addRecommendedFaction(FactionsFactory.getInstance()
							.getElement(recommendedFactionsOfSkill.nextToken().trim(), language));
				}
			}
		} catch (NullPointerException npe) {
			// Optional
		}

		try {
			final String restrictedRaces = translator.getNodeValue(element.getId(), RANDOM, RESTRICTED_RACES);
			if (restrictedRaces != null) {
				final StringTokenizer restrictedRacesOfSkill = new StringTokenizer(restrictedRaces, ",");
				while (restrictedRacesOfSkill.hasMoreTokens()) {
					element.getRandomDefinition().addRestrictedRace(
							RaceFactory.getInstance().getElement(restrictedRacesOfSkill.nextToken().trim(), language));
				}
			}
		} catch (NullPointerException npe) {
			// Optional
		}

		try {
			final String recommendedRaces = translator.getNodeValue(element.getId(), RANDOM, RECOMMENDED_RACES);
			if (recommendedRaces != null) {
				final StringTokenizer recommendedRacesTokenizer = new StringTokenizer(recommendedRaces, ",");
				while (recommendedRacesTokenizer.hasMoreTokens()) {
					element.getRandomDefinition().addRecommendedRace(RaceFactory.getInstance()
							.getElement(recommendedRacesTokenizer.nextToken().trim(), language));
				}
			}
		} catch (NullPointerException npe) {
			// Optional
		}

		try {
			final String forbiddenRaces = translator.getNodeValue(element.getId(), RANDOM, FORBIDDEN_RACES);
			if (forbiddenRaces != null) {
				final StringTokenizer forbiddenRacesTokenizer = new StringTokenizer(forbiddenRaces, ",");
				while (forbiddenRacesTokenizer.hasMoreTokens()) {
					element.getRandomDefinition().addForbiddenRace(
							RaceFactory.getInstance().getElement(forbiddenRacesTokenizer.nextToken().trim(), language));
				}
			}
		} catch (NullPointerException npe) {
			// Optional
		}

		try {
			final String generalProbability = translator.getNodeValue(element.getId(), RANDOM, GENERAL_PROBABILITY);
			if (generalProbability != null) {
				element.getRandomDefinition().setProbability(RandomProbabilityDefinition.get(generalProbability));
			}
		} catch (NullPointerException npe) {
			// Optional
		}

		try {
			final String staticProbability = translator.getNodeValue(element.getId(), RANDOM, STATIC_PROBABILITY);
			if (staticProbability != null) {
				element.getRandomDefinition().setStaticProbability(Integer.parseInt(staticProbability));
			}
		} catch (NumberFormatException nfe) {
			throw new InvalidXmlElementException(
					"Invalid number value for element probability in '" + element.getId() + "'.");
		} catch (NullPointerException npe) {
			// Optional
		}
	}

	public List<T> getElements(String language) throws InvalidXmlElementException {
		if (elements.get(language) == null) {
			elements.put(language, new ArrayList<T>());
			for (final String elementId : getTranslator().getAllTranslatedElements()) {
				final T element = createElement(getTranslator(), elementId, language);
				setRandomConfiguration(element, getTranslator(), language);
				if (elements.get(language).contains(element)) {
					throw new ElementAlreadyExistsException(
							"Element '" + element + "' already is inserted. Probably the ID is duplicated.");
				}
				elements.get(language).add(element);
			}
			Collections.sort(elements.get(language));
		}
		return elements.get(language);
	}

	public T getElement(String elementId, String language) throws InvalidXmlElementException {
		final List<T> elements = getElements(language);
		for (final T element : elements) {
			if (element.getId() != null) {
				if (Objects.equals(element.getId().toLowerCase(), elementId.trim().toLowerCase())) {
					return element;
				}
			}
		}
		throw new InvalidXmlElementException("Element '" + elementId + "' does not exists.");
	}

	protected abstract T createElement(ITranslator translator, String elementId, String language)
			throws InvalidXmlElementException;

	protected <E extends Element<E>, F extends XmlFactory<E>> Set<E> getCommaSeparatedValues(String elementId,
			String node, String language, F factory) throws InvalidXmlElementException {
		final String elementTags = getTranslator().getNodeValue(elementId, node);
		final Set<E> elements = new HashSet<>();
		try {
			if (elementTags != null) {
				final StringTokenizer elementsTokenizer = new StringTokenizer(elementTags, ",");
				while (elementsTokenizer.hasMoreTokens()) {
					try {
						elements.add(factory.getElement(elementsTokenizer.nextToken().trim(), language));
					} catch (InvalidXmlElementException e) {
						throw new InvalidXmlElementException(
								"Invalid elements '" + elementTags + "' for element '" + elementId + "'.", e);
					}
				}
			}
		} catch (NullPointerException e) {
			throw new InvalidXmlElementException(
					"Invalid tag list '" + elementTags + "' in element '" + elementId + "'.", e);
		}
		return elements;
	}
}
