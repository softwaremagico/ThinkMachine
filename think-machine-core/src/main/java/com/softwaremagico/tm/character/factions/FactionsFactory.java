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

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.benefices.AvailableBeneficeFactory;
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.blessings.BlessingFactory;
import com.softwaremagico.tm.character.race.Race;
import com.softwaremagico.tm.character.race.RaceFactory;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.log.MachineLog;

public class FactionsFactory extends XmlFactory<Faction> {
	private final static ITranslator translatorBenefit = LanguagePool.getTranslator("factions.xml");

	private final static String NAME = "name";
	private final static String GROUP = "group";
	private final static String RANKS_TAG = "ranks";
	private final static String RANKS_TRANSLATION_TAG = "translation";
	private final static String RACE = "race";
	private final static String BLESSINGS = "blessings";
	private final static String BENEFICES = "benefices";

	private static FactionsFactory instance;

	private static void createInstance() {
		if (instance == null) {
			synchronized (FactionsFactory.class) {
				if (instance == null) {
					instance = new FactionsFactory();
				}
			}
		}
	}

	public static FactionsFactory getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}

	@Override
	protected ITranslator getTranslator() {
		return translatorBenefit;
	}

	@Override
	public Faction getElement(String elementId, String language) throws InvalidXmlElementException {
		Faction faction = super.getElement(elementId, language);
		// setBlessings(getTranslator(), faction.getId(), language);
		// setBenefices(getTranslator(), faction.getId(), language);
		return faction;
	}

	public void setBlessings(Faction faction, String language) throws InvalidFactionException {
		String mandatoryBlessingsList = getTranslator().getNodeValue(faction.getId(), BLESSINGS);
		Set<Blessing> mandatoryBlessings = new HashSet<>();
		if (mandatoryBlessingsList != null) {
			StringTokenizer mandatoyBlessingTokenizer = new StringTokenizer(mandatoryBlessingsList, ",");
			while (mandatoyBlessingTokenizer.hasMoreTokens()) {
				try {
					mandatoryBlessings.add(BlessingFactory.getInstance().getElement(
							mandatoyBlessingTokenizer.nextToken().trim(), language));
				} catch (InvalidXmlElementException ixe) {
					throw new InvalidFactionException("Error in faction '" + faction
							+ "' structure. Invalid blessing defintion. ", ixe);
				}
			}
		}
		faction.setBlessings(mandatoryBlessings);
	}

	public void setBenefices(Faction faction, String language) throws InvalidFactionException {
		String mandatoryBeneficesList = getTranslator().getNodeValue(faction.getId(), BENEFICES);
		Set<AvailableBenefice> mandatoryBenefices = new HashSet<>();
		if (mandatoryBeneficesList != null) {
			StringTokenizer mandatoyBeneficesTokenizer = new StringTokenizer(mandatoryBeneficesList, ",");
			while (mandatoyBeneficesTokenizer.hasMoreTokens()) {
				try {
					mandatoryBenefices.add(AvailableBeneficeFactory.getInstance().getElement(
							mandatoyBeneficesTokenizer.nextToken().trim(), language));
				} catch (InvalidXmlElementException ixe) {
					throw new InvalidFactionException("Error in faction '" + faction.getId()
							+ "' structure. Invalid benefice defintion. ", ixe);
				}
			}
		}
		faction.setBenefices(mandatoryBenefices);
	}

	@Override
	protected Faction createElement(ITranslator translator, String factionId, String language)
			throws InvalidXmlElementException {
		try {
			String name = translator.getNodeValue(factionId, NAME, language);
			FactionGroup factionGroup;

			try {
				String groupName = translator.getNodeValue(factionId, GROUP);
				factionGroup = FactionGroup.get(groupName);
			} catch (Exception e) {
				MachineLog.errorMessage(this.getClass().getName(), e);
				factionGroup = FactionGroup.NONE;
			}

			String raceRestrictionName = translator.getNodeValue(factionId, RACE);
			Race raceRestriction = null;
			if (raceRestrictionName != null) {
				try {
					raceRestriction = RaceFactory.getInstance().getElement(raceRestrictionName, language);
				} catch (InvalidXmlElementException ixe) {
					throw new InvalidFactionException("Error in faction '" + factionId + "' structure. Invalid race. ",
							ixe);
				}
			}

			Faction faction = new Faction(factionId, name, factionGroup, raceRestriction, language);

			for (String rankId : translator.getAllChildrenTags(factionId, RANKS_TAG)) {
				String rankName = translator.getNodeValue(factionId, rankId, RANKS_TRANSLATION_TAG, language);
				FactionRankTranslation factionRank = new FactionRankTranslation(rankId, rankName);
				faction.addRankTranslation(factionRank);
			}

			return faction;
		} catch (Exception e) {
			throw new InvalidFactionException("Invalid structure in faction '" + factionId + "'.", e);
		}
	}
}
