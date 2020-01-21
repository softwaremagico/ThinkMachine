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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.character.Gender;
import com.softwaremagico.tm.character.Name;
import com.softwaremagico.tm.character.Surname;
import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.benefices.AvailableBeneficeFactory;
import com.softwaremagico.tm.character.benefices.BeneficeDefinition;
import com.softwaremagico.tm.character.benefices.BeneficeDefinitionFactory;
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.blessings.BlessingFactory;
import com.softwaremagico.tm.character.races.Race;
import com.softwaremagico.tm.character.races.RaceFactory;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.log.MachineLog;
import com.softwaremagico.tm.log.SuppressFBWarnings;

public class FactionsFactory extends XmlFactory<Faction> {
	private static final String TRANSLATOR_FILE = "factions.xml";

	private static final String NAME = "name";
	private static final String GROUP = "group";
	private static final String RANKS_TAG = "ranks";
	private static final String RANKS_TRANSLATION_TAG = "translation";
	private static final String RACE = "race";
	private static final String BLESSINGS = "blessings";
	private static final String BENEFICES = "benefices";
	private static final String SUGGESTED_BENEFICES = "suggestedBenefices";
	private static final String RESTRICTED_BENEFICES = "restrictedBenefices";
	private static final String BENEFICE = "benefice";
	private static final String BENEFICE_MAX_VALUE = "maxValue";

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
	public void clearCache() {
		if (namesByFaction != null) {
			namesByFaction.clear();
		}
		if (surnamesByFaction != null) {
			surnamesByFaction.clear();
		}
		super.clearCache();
	}

	@Override
	protected String getTranslatorFile() {
		return TRANSLATOR_FILE;
	}

	public void setBlessings(Faction faction, String language) throws InvalidFactionException {
		final Set<Blessing> mandatoryBlessings;
		try {
			mandatoryBlessings = getCommaSeparatedValues(faction.getId(), BLESSINGS, language, faction.getModuleName(),
					BlessingFactory.getInstance());
		} catch (InvalidXmlElementException ixe) {
			throw new InvalidFactionException(
					"Error in faction '" + faction + "' structure. Invalid blessing defintion. ", ixe);
		}
		faction.setBlessings(mandatoryBlessings);
	}

	public void setBenefices(Faction faction, String language) throws InvalidFactionException {
		final String mandatoryBeneficesList = getTranslator(faction.getModuleName()).getNodeValue(faction.getId(),
				BENEFICES);
		final Set<AvailableBenefice> mandatoryBenefices = new HashSet<>();
		if (mandatoryBeneficesList != null) {
			final StringTokenizer mandatoyBeneficesTokenizer = new StringTokenizer(mandatoryBeneficesList, ",");
			while (mandatoyBeneficesTokenizer.hasMoreTokens()) {
				final String beneficeName = mandatoyBeneficesTokenizer.nextToken().trim();
				try {
					mandatoryBenefices.add(AvailableBeneficeFactory.getInstance().getElement(beneficeName, language,
							faction.getModuleName()));
				} catch (InvalidXmlElementException ixe) {
					throw new InvalidFactionException("Error in faction '" + faction.getId()
							+ "' structure. Invalid benefice '" + beneficeName + "' defintion. ", ixe);
				}
			}
		}
		faction.setBenefices(mandatoryBenefices);
	}

	public void setSuggestedBenefices(Faction faction, String language) throws InvalidFactionException {
		final String suggestedBeneficesList = getTranslator(faction.getModuleName()).getNodeValue(faction.getId(),
				SUGGESTED_BENEFICES);
		final Set<BeneficeDefinition> suggestedBenefices = new HashSet<>();
		if (suggestedBeneficesList != null) {
			final StringTokenizer suggestedBeneficesTokenizer = new StringTokenizer(suggestedBeneficesList, ",");
			while (suggestedBeneficesTokenizer.hasMoreTokens()) {
				final String suggestedBeneficeName = suggestedBeneficesTokenizer.nextToken().trim();
				try {
					suggestedBenefices.add(BeneficeDefinitionFactory.getInstance().getElement(suggestedBeneficeName,
							language, faction.getModuleName()));
				} catch (InvalidXmlElementException ixe) {
					throw new InvalidFactionException("Error in faction '" + faction.getId()
							+ "' structure. Invalid suggested benefice '" + suggestedBeneficeName + "' defintion. ",
							ixe);
				}
			}
		}
		faction.setSuggestedBenefices(suggestedBenefices);
	}

	@Override
	@SuppressFBWarnings("REC_CATCH_EXCEPTION")
	protected Faction createElement(ITranslator translator, String factionId, String language, String moduleName)
			throws InvalidXmlElementException {
		try {
			final String name = translator.getNodeValue(factionId, NAME, language);
			FactionGroup factionGroup;

			try {
				final String groupName = translator.getNodeValue(factionId, GROUP);
				factionGroup = FactionGroup.get(groupName);
			} catch (Exception e) {
				MachineLog.errorMessage(this.getClass().getName(), e);
				factionGroup = FactionGroup.NONE;
			}

			final String raceRestrictionName = translator.getNodeValue(factionId, RACE);
			Race raceRestriction = null;
			if (raceRestrictionName == null) {
				throw new InvalidFactionException(
						"Race not defined in faction '" + factionId + "'. Factions must have a race restriction.");
			}
			try {
				raceRestriction = RaceFactory.getInstance().getElement(raceRestrictionName, language, moduleName);
			} catch (InvalidXmlElementException ixe) {
				throw new InvalidFactionException("Error in faction '" + factionId + "' structure. Invalid race. ",
						ixe);
			}
			final Faction faction = new Faction(factionId, name, factionGroup, raceRestriction, language, moduleName);

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

	private void addName(Name name) {
		if (namesByFaction == null) {
			namesByFaction = new HashMap<>();
		}
		if (namesByFaction.get(name.getFaction()) == null) {
			namesByFaction.put(name.getFaction(), new HashMap<Gender, Set<Name>>());
		}
		if (namesByFaction.get(name.getFaction()).get(name.getGender()) == null) {
			namesByFaction.get(name.getFaction()).put(name.getGender(), new HashSet<Name>());
		}
		namesByFaction.get(name.getFaction()).get(name.getGender()).add(name);
	}

	private void addSurname(Surname surname) {
		if (surnamesByFaction == null) {
			surnamesByFaction = new HashMap<>();
		}
		if (surnamesByFaction.get(surname.getFaction()) == null) {
			surnamesByFaction.put(surname.getFaction(), new HashSet<Surname>());
		}
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
			names.addAll(getAllNames(faction, gender));
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
