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

import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.factions.InvalidFactionException;
import com.softwaremagico.tm.character.races.RaceFactory;
import com.softwaremagico.tm.file.modules.ModuleManager;
import com.softwaremagico.tm.json.factories.cache.FactoryCacheLoader;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.Language;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.log.MachineLog;
import com.softwaremagico.tm.log.MachineXmlReaderLog;
import com.softwaremagico.tm.random.definition.RandomProbabilityDefinition;

import java.util.*;

public abstract class XmlFactory<T extends Element<T>> implements IElementRetriever<T> {
    protected Map<String, Map<String, List<T>>> elements = new HashMap<>();

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
    private static final String RESTRICTED = "restricted";
    private static final String OFFICIAL = "official";

    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    protected static final String TOTAL_ELEMENTS = "totalElements";
    protected static final String VERSION = "version";

    private Integer totalElements;
    private Integer version;

    protected void initialize() {
        for (final String moduleName : ModuleManager.getAvailableModules()) {
            final List<Language> languages = getTranslator(moduleName).getAvailableLanguages();
            List<T> elements = new ArrayList<>();
            for (final Language language : languages) {
                try {
                    elements = getElements(language.getAbbreviature(), moduleName);
                } catch (InvalidXmlElementException e) {
                    MachineXmlReaderLog.errorMessage(this.getClass().getName(), e);
                }
            }
            MachineXmlReaderLog.debug(this.getClass().getName(),
                    "Loaded '{}' elements at '{}' from module '{}'.", elements.size(), this.getClass().getSimpleName(), moduleName);
        }
    }

    protected ITranslator getTranslator(String moduleName) {
        return LanguagePool.getTranslator(getTranslatorFile(), moduleName);
    }

    public abstract String getTranslatorFile();

    public void removeData() {
        elements = new HashMap<>();
    }

    public void refreshCache() {
        removeData();
        initialize();
    }

    protected void setRandomConfiguration(Element<?> element, ITranslator translator, String language, String moduleName) throws InvalidXmlElementException {
        // Is an element restricted to a faction?
        try {
            final String restrictedFactionsId = translator.getNodeValue(element.getId(), RANDOM, RESTRICTED_FACTIONS);
            if (restrictedFactionsId != null) {
                final StringTokenizer factionTokenizer = new StringTokenizer(restrictedFactionsId, ",");
                while (factionTokenizer.hasMoreTokens()) {
                    element.getRandomDefinition().getRestrictedFactions()
                            .add(FactionsFactory.getInstance().getElement(factionTokenizer.nextToken().trim(), language, moduleName));
                }
            }
        } catch (NullPointerException npe) {
            // Optional
        }

        try {
            final String elementProbability = getTranslator(moduleName).getNodeValue(element.getId(), RANDOM, ELEMENT_PROBABILITY_MULTIPLIER);
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
            final String minTechLevel = translator.getNodeValue(element.getId(), RANDOM, MIN_TECH_LEVEL);
            if (minTechLevel != null) {
                element.getRandomDefinition().setMinimumTechLevel(Integer.parseInt(minTechLevel));
            }
        } catch (NumberFormatException nfe) {
            throw new InvalidXmlElementException("Invalid number value for techLevel in element '" + element.getId() + "'.");
        } catch (NullPointerException npe) {
            // Optional
        }

        try {
            final String maxTechLevel = translator.getNodeValue(element.getId(), RANDOM, MAX_TECH_LEVEL);
            if (maxTechLevel != null) {
                element.getRandomDefinition().setMaximumTechLevel(Integer.parseInt(maxTechLevel));
            }
        } catch (NumberFormatException nfe) {
            throw new InvalidXmlElementException("Invalid number value for max techLevel in element '" + element.getId() + "'.");
        } catch (NullPointerException npe) {
            // Optional
        }

        try {
            final String recommendedFactionGroups = translator.getNodeValue(element.getId(), RANDOM, RECOMMENDED_FACTION_GROUPS);
            if (recommendedFactionGroups != null) {
                final StringTokenizer recommendedFactionGroupsTokenizer = new StringTokenizer(recommendedFactionGroups, ",");
                while (recommendedFactionGroupsTokenizer.hasMoreTokens()) {
                    element.getRandomDefinition().addRecommendedFactionGroup(FactionGroup.get(recommendedFactionGroupsTokenizer.nextToken().trim()));
                }
            }
        } catch (NullPointerException npe) {
            // Optional
        }

        try {
            final String restrictedFactionGroups = translator.getNodeValue(element.getId(), RANDOM, RESTRICTED_FACTION_GROUPS);
            if (restrictedFactionGroups != null) {
                final StringTokenizer restrictedFactionGroupsTokenizer = new StringTokenizer(restrictedFactionGroups, ",");
                while (restrictedFactionGroupsTokenizer.hasMoreTokens()) {
                    element.getRandomDefinition().addRestrictedFactionGroup(FactionGroup.get(restrictedFactionGroupsTokenizer.nextToken().trim()));
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
                    element.getRandomDefinition().addRecommendedFaction(
                            FactionsFactory.getInstance().getElement(recommendedFactionsOfSkill.nextToken().trim(), language, moduleName));
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
                    element.getRandomDefinition()
                            .addRestrictedRace(RaceFactory.getInstance().getElement(restrictedRacesOfSkill.nextToken().trim(), language, moduleName));
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
                    element.getRandomDefinition()
                            .addRecommendedRace(RaceFactory.getInstance().getElement(recommendedRacesTokenizer.nextToken().trim(), language, moduleName));
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
                    element.getRandomDefinition()
                            .addForbiddenRace(RaceFactory.getInstance().getElement(forbiddenRacesTokenizer.nextToken().trim(), language, moduleName));
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
            throw new InvalidXmlElementException("Invalid number value for element probability in '" + element.getId() + "'.");
        } catch (NullPointerException npe) {
            // Optional
        }
    }

    /**
     * Initialize the factory from external source and not from XML sources.
     *
     * @param language   language.
     * @param moduleName module classification
     * @param elements   list of elements to include.
     */
    public void setElements(String language, String moduleName, List<T> elements) {
        this.elements.computeIfAbsent(language, k -> new HashMap<>());
        this.elements.get(language).computeIfAbsent(moduleName, k -> new ArrayList<>());
        this.elements.get(language).get(moduleName).addAll(elements);
    }

    public Integer getNumberOfElements(String moduleName) {
        if (totalElements != null) {
            return totalElements;
        }
        try {
            totalElements = Integer.parseInt(getTranslator(moduleName).getNodeValue(TOTAL_ELEMENTS));
            return totalElements;
        } catch (Exception e) {
            //Not mandatory.
            MachineLog.debug(this.getClass().getName(), "No element number set on the xml '" + getTranslatorFile()
                    + "' file.");
        }
        return null;
    }

    public Integer getVersion(String moduleName) {
        if (version != null) {
            return version;
        }
        try {
            version = Integer.parseInt(getTranslator(moduleName).getNodeValue(VERSION));
            return version;
        } catch (Exception e) {
            //Not mandatory.
            MachineLog.debug(this.getClass().getName(), "No version set on the xml '" + getTranslatorFile()
                    + "' file.");
        }
        return null;
    }

    public abstract FactoryCacheLoader<T> getFactoryCacheLoader();

    public synchronized List<T> getElements(String language, String moduleName) throws InvalidXmlElementException {
        if (elements.get(language) == null || elements.get(language).get(moduleName) == null) {
            elements.computeIfAbsent(language, k -> new HashMap<>());
            elements.get(language).computeIfAbsent(moduleName, k -> new ArrayList<>());
            if (getFactoryCacheLoader() != null) {
                final List<T> cachedElements = getFactoryCacheLoader().load(language, moduleName);
                if (cachedElements != null) {
                    elements.get(language).get(moduleName).addAll(cachedElements);
                    Collections.sort(elements.get(language).get(moduleName));
                    return elements.get(language).get(moduleName);
                }
            }
            for (final String elementId : getTranslator(moduleName).getAllTranslatedElements()) {
                //Skip totalElements nodes.
                if (Objects.equals(elementId, TOTAL_ELEMENTS) || Objects.equals(elementId, VERSION)) {
                    continue;
                }
                String name;
                try {
                    name = getTranslator(moduleName).getNodeValue(elementId, NAME, language);
                } catch (Exception e) {
                    throw new InvalidXmlElementException("Invalid name in element '" + elementId + "'.");
                }

                String description = null;
                try {
                    description = getTranslator(moduleName).getNodeValue(elementId, DESCRIPTION, language);
                } catch (Exception e) {
                    //Description is not mandatory.
                }

                Boolean restricted = null;
                try {
                    restricted = Boolean.parseBoolean(getTranslator(moduleName).getNodeValue(elementId, RESTRICTED));
                } catch (Exception e) {
                    //Restriction is not mandatory.
                }

                Boolean official = null;
                try {
                    final String officialTag = getTranslator(moduleName).getNodeValue(elementId, OFFICIAL);
                    official = (officialTag == null || Boolean.parseBoolean(officialTag));
                } catch (Exception e) {
                    //Restriction is not mandatory.
                }


                final T element = createElement(getTranslator(moduleName), elementId, name, description, language, moduleName);
                setRandomConfiguration(element, getTranslator(moduleName), language, moduleName);
                if (restricted != null) {
                    element.setRestricted(restricted);
                }
                if (official != null) {
                    element.setOfficial(official);
                }
                if (elements.get(language).get(moduleName).contains(element)) {
                    throw new ElementAlreadyExistsException("Element '" + element + "' already is inserted. Probably the ID is duplicated.");
                }
                elements.get(language).get(moduleName).add(element);
            }
            Collections.sort(elements.get(language).get(moduleName));
        }
        return elements.get(language).get(moduleName);
    }

    @Override
    public T getElement(String elementId, String language, String moduleName) throws InvalidXmlElementException {
        final List<T> elements = getElements(language, moduleName);
        for (final T element : elements) {
            if (element.getId() != null && elementId != null) {
                if (Objects.equals(element.getId().toLowerCase(), elementId.trim().toLowerCase())) {
                    return element;
                }
            }
        }
        throw new InvalidXmlElementException("Element '" + elementId + "' does not exists.");
    }

    protected abstract T createElement(ITranslator translator, String elementId, String name, String description, String language,
                                       String moduleName) throws InvalidXmlElementException;

    protected <E extends Element<E>, F extends XmlFactory<E>> E getElement(String elementId, String node, String language, String moduleName,
                                                                           F factory) throws InvalidXmlElementException {
        E element = null;
        final String elementName = getTranslator(moduleName).getNodeValue(elementId, node);
        if (elementName != null) {
            try {
                element = factory.getElement(elementName, language, moduleName);
            } catch (InvalidXmlElementException e) {
                throw new InvalidFactionException("Element tag '" + elementName + "' in element '" + elementId + "'.", e);
            }
        }
        return element;
    }

    protected <E extends Element<E>, F extends IElementRetriever<E>> Set<E> getCommaSeparatedValues(
            String elementId, String node, String language, String moduleName, F factory) throws InvalidXmlElementException {
        final Set<E> elements = new HashSet<>();
        try {
            final String elementTags = getTranslator(moduleName).getNodeValue(elementId, node);
            if (elementTags != null) {
                final StringTokenizer elementsTokenizer = new StringTokenizer(elementTags, ",");
                while (elementsTokenizer.hasMoreTokens()) {
                    try {
                        elements.add(factory.getElement(elementsTokenizer.nextToken().trim(), language, moduleName));
                    } catch (InvalidXmlElementException e) {
                        throw new InvalidXmlElementException("Invalid elements '" + elementTags + "' for element '" + elementId + "'.", e);
                    }
                }
            }
        } catch (NullPointerException e) {
            return elements;
        }
        return elements;
    }

}
