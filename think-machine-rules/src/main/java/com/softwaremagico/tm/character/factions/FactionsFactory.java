package com.softwaremagico.tm.character.factions;

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
import com.softwaremagico.tm.character.Gender;
import com.softwaremagico.tm.character.Name;
import com.softwaremagico.tm.character.Surname;
import com.softwaremagico.tm.character.benefices.AvailableBeneficeFactory;
import com.softwaremagico.tm.character.benefices.BeneficeDefinitionFactory;
import com.softwaremagico.tm.character.benefices.RestrictedBenefice;
import com.softwaremagico.tm.character.benefices.SuggestedBenefice;
import com.softwaremagico.tm.character.blessings.BlessingFactory;
import com.softwaremagico.tm.character.races.Race;
import com.softwaremagico.tm.json.factories.cache.FactoryCacheLoader;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.log.MachineXmlReaderLog;
import com.softwaremagico.tm.log.SuppressFBWarnings;

import java.util.*;
import java.util.Map.Entry;

public class FactionsFactory extends XmlFactory<Faction> {
    private static final String TRANSLATOR_FILE = "factions.xml";

    private static final String GROUP = "group";
    private static final String RANKS_TAG = "ranks";
    private static final String RANKS_TRANSLATION_TAG = "translation";
    private static final String BLESSINGS = "blessings";
    private static final String BENEFICES = "benefices";
    private static final String SUGGESTED_BENEFICES = "suggestedBenefices";
    private static final String RESTRICTED_BENEFICES = "restrictedBenefices";
    private static final String BENEFICE_ID = "id";
    private static final String BENEFICE_MAX_VALUE = "maxValue";
    private static final String BENEFICE_VALUE = "value";

    private static final String RANDOM_NAMES = "names";
    private static final String MALE_NAMES = "male";
    private static final String FEMALE_NAMES = "female";
    private static final String SURNAMES = "surnames";

    private Map<Faction, Map<Gender, Set<Name>>> namesByFaction;
    private Map<Faction, Set<Surname>> surnamesByFaction;

    private static class FactionsFactoryInit {
        public static final FactionsFactory INSTANCE = new FactionsFactory();
    }

    public static FactionsFactory getInstance() {
        return FactionsFactoryInit.INSTANCE;
    }

    @Override
    public FactoryCacheLoader<Faction> getFactoryCacheLoader() {
        return null;
    }

    @Override
    public void refreshCache() {
        if (namesByFaction != null) {
            namesByFaction.clear();
        }
        if (surnamesByFaction != null) {
            surnamesByFaction.clear();
        }
        super.refreshCache();
    }

    @Override
    public String getTranslatorFile() {
        return TRANSLATOR_FILE;
    }

    public void setBlessings(Faction faction) throws InvalidFactionException {
        try {
            faction.setBlessings(getCommaSeparatedValues(faction.getId(), BLESSINGS, faction.getLanguage(),
                    faction.getModuleName(), BlessingFactory.getInstance()));
        } catch (InvalidXmlElementException ixe) {
            throw new InvalidFactionException(
                    "Error in faction '" + faction + "' structure. Invalid blessing definition. ", ixe);
        }
    }

    public void setBenefices(Faction faction) throws InvalidFactionException {
        try {
            faction.setBenefices(getCommaSeparatedValues(faction.getId(), BENEFICES, faction.getLanguage(),
                    faction.getModuleName(), AvailableBeneficeFactory.getInstance()));
        } catch (InvalidXmlElementException ixe) {
            throw new InvalidFactionException("Error in faction '" + faction.getId()
                    + "' structure. Invalid benefices definition. ", ixe);
        }
    }

    public void setSuggestedBenefices(Faction faction, String language)
            throws NumberFormatException, InvalidXmlElementException {
        final Set<SuggestedBenefice> restrictedBenefices = new HashSet<>();
        for (final String restrictedBeneficeId : getTranslator(faction.getModuleName())
                .getAllChildrenTags(faction.getId(), SUGGESTED_BENEFICES)) {
            final String beneficeName = getTranslator(faction.getModuleName()).getNodeValue(faction.getId(), SUGGESTED_BENEFICES,
                    restrictedBeneficeId, BENEFICE_ID);
            final String valueTag = getTranslator(faction.getModuleName()).getNodeValue(faction.getId(), SUGGESTED_BENEFICES,
                    restrictedBeneficeId, BENEFICE_VALUE);
            final Integer value = valueTag != null ? Integer.parseInt(valueTag) : null;
            restrictedBenefices.add(new SuggestedBenefice(
                    BeneficeDefinitionFactory.getInstance().getElement(beneficeName, language, faction.getModuleName()),
                    value));
        }
        faction.setSuggestedBenefices(restrictedBenefices);
    }

    public void setRestrictedBenefices(Faction faction, String language)
            throws NumberFormatException, InvalidXmlElementException {
        final Set<RestrictedBenefice> restrictedBenefices = new HashSet<>();
        for (final String restrictedBeneficeId : getTranslator(faction.getModuleName())
                .getAllChildrenTags(faction.getId(), RESTRICTED_BENEFICES)) {
            final String beneficeName = getTranslator(faction.getModuleName()).getNodeValue(faction.getId(), RESTRICTED_BENEFICES,
                    restrictedBeneficeId, BENEFICE_ID);
            final String maxValueTag = getTranslator(faction.getModuleName()).getNodeValue(faction.getId(), RESTRICTED_BENEFICES,
                    restrictedBeneficeId, BENEFICE_MAX_VALUE);
            final Integer maxValue = maxValueTag != null ? Integer.parseInt(maxValueTag) : null;
            restrictedBenefices.add(new RestrictedBenefice(
                    BeneficeDefinitionFactory.getInstance().getElement(beneficeName, language, faction.getModuleName()),
                    maxValue));
        }
        faction.setRestrictedBenefices(restrictedBenefices);
    }

    @Override
    @SuppressFBWarnings("REC_CATCH_EXCEPTION")
    protected Faction createElement(ITranslator translator, String factionId, String name, String description,
                                    String language, String moduleName)
            throws InvalidXmlElementException {
        try {
            FactionGroup factionGroup;

            try {
                final String groupName = translator.getNodeValue(factionId, GROUP);
                factionGroup = FactionGroup.get(groupName);
            } catch (Exception e) {
                MachineXmlReaderLog.errorMessage(this.getClass().getName(), e);
                factionGroup = FactionGroup.NONE;
            }

            final Faction faction = new Faction(factionId, name, description, factionGroup, language, moduleName);

            for (final String rankId : translator.getAllChildrenTags(factionId, RANKS_TAG)) {
                final String rankName = translator.getNodeValue(factionId, rankId, RANKS_TRANSLATION_TAG, language);
                final FactionRankTranslation factionRank = new FactionRankTranslation(rankId, rankName, language,
                        moduleName);
                faction.addRankTranslation(factionRank);
            }

            // Random options
            final String maleNames = translator.getNodeValue(factionId, RANDOM_NAMES, MALE_NAMES);
            if (maleNames != null) {
                final StringTokenizer maleNamesTokenizer = new StringTokenizer(maleNames, ",");
                while (maleNamesTokenizer.hasMoreTokens()) {
                    addName(new Name(maleNamesTokenizer.nextToken().trim(), language, moduleName, Gender.MALE,
                            faction));
                }
            }

            final String femaleNames = translator.getNodeValue(factionId, RANDOM_NAMES, FEMALE_NAMES);
            if (femaleNames != null) {
                final StringTokenizer femaleNamesTokenizer = new StringTokenizer(femaleNames, ",");
                while (femaleNamesTokenizer.hasMoreTokens()) {
                    addName(new Name(femaleNamesTokenizer.nextToken().trim(), language, moduleName, Gender.FEMALE,
                            faction));
                }
            }

            final String surnames = translator.getNodeValue(factionId, SURNAMES);
            if (surnames != null) {
                final StringTokenizer surnamesTokenizer = new StringTokenizer(surnames, ",");
                while (surnamesTokenizer.hasMoreTokens()) {
                    addSurname(new Surname(surnamesTokenizer.nextToken().trim(), language, moduleName, faction));
                }
            }

            return faction;
        } catch (Exception e) {
            throw new InvalidFactionException("Invalid structure in faction '" + factionId + "'.", e);
        }
    }

    @Override
    protected void setRestrictedToRaces(Faction faction, Set<Race> races) throws InvalidFactionException {
        if (races == null || races.isEmpty()) {
            throw new InvalidFactionException(
                    "Race not defined in faction '" + faction.getId() + "'. Factions must have a race restriction.");
        }
        faction.setRestrictedToRaces(races);
    }

    private void addName(Name name) {
        if (namesByFaction == null) {
            namesByFaction = new HashMap<>();
        }
        namesByFaction.computeIfAbsent(name.getFaction(), k -> new HashMap<>());
        namesByFaction.get(name.getFaction()).computeIfAbsent(name.getGender(), k -> new HashSet<>());
        namesByFaction.get(name.getFaction()).get(name.getGender()).add(name);
    }

    private void addSurname(Surname surname) {
        if (surnamesByFaction == null) {
            surnamesByFaction = new HashMap<>();
        }
        surnamesByFaction.computeIfAbsent(surname.getFaction(), k -> new HashSet<>());
        surnamesByFaction.get(surname.getFaction()).add(surname);
    }

    public Set<Name> getAllNames(Faction faction, Gender gender) {
        if (faction == null || gender == null || namesByFaction.get(faction) == null) {
            return new HashSet<>();
        }
        if (namesByFaction.get(faction).get(gender) == null) {
            return null;
        }
        return namesByFaction.get(faction).get(gender);
    }

    public Set<Name> getAllNames(Faction faction) {
        final Set<Name> names = new HashSet<>();
        for (final Gender gender : Gender.values()) {
            try {
                names.addAll(getAllNames(faction, gender));
            } catch (NullPointerException e) {
                //No names defined.
            }
        }
        return names;
    }

    public Set<Name> getAllNames() {
        final Set<Name> names = new HashSet<>();
        for (final Faction faction : namesByFaction.keySet()) {
            names.addAll(getAllNames(faction));
        }
        return names;
    }

    public Set<Surname> getAllSurnames(Faction faction) {
        final Set<Surname> surnames = new HashSet<>();
        if (surnamesByFaction.get(faction) != null) {
            surnames.addAll(surnamesByFaction.get(faction));
        }
        return surnames;
    }

    public Set<Surname> getAllSurnames() {
        final Set<Surname> surnames = new HashSet<>();
        for (final Entry<Faction, Set<Surname>> faction : surnamesByFaction.entrySet()) {
            surnames.addAll(faction.getValue());
        }
        return surnames;
    }

}
