package com.softwaremagico.tm.random.party;

/*-
 * #%L
 * Think Machine (Random Generator)
 * %%
 * Copyright (C) 2017 - 2019 Softwaremagico
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
import com.softwaremagico.tm.json.factories.cache.FactoryCacheLoader;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.random.profiles.RandomProfileFactory;
import com.softwaremagico.tm.random.selectors.IRandomPreference;
import com.softwaremagico.tm.random.selectors.RandomPreferenceUtils;

import java.util.*;

public class RandomPartyFactory extends XmlFactory<RandomParty> {
	private static final String TRANSLATOR_FILE = "parties.xml";

	private static final String NAME = "name";
	private static final String RANDOM = "random";
	private static final String PARTY_NAME = "names";
	private static final String PARTY_ADJECTIVE = "adjectives";

	private static final String MEMBER = "member";
	private static final String PROFILE = "profile";
	private static final String MINIMUM_NUMBER = "minNumber";
	private static final String MAXIMUM_NUMBER = "maxNumber";
	private static final String WEIGHT = "weight";
	private static final String EXTRA_PREFERENCES = "extraPreferences";

	// RandomParty --> Language --> Name
	private Map<RandomParty, Map<String, Set<PartyName>>> namesByParty;
	private Map<RandomParty, Map<String, Set<PartyAdjective>>> adjectivesByParty;

	private static class RandomPartyFactoryInit {
		public static final RandomPartyFactory INSTANCE = new RandomPartyFactory();
	}

	public static RandomPartyFactory getInstance() {
		return RandomPartyFactoryInit.INSTANCE;
	}

	@Override
	public FactoryCacheLoader<RandomParty> getFactoryCacheLoader() {
		return null;
	}

	@Override
    public String getTranslatorFile() {
		return TRANSLATOR_FILE;
	}

	private void addName(PartyName name) {
		if (namesByParty == null) {
			namesByParty = new HashMap<>();
		}
		namesByParty.computeIfAbsent(name.getRandomParty(), k -> new HashMap<>());
		namesByParty.get(name.getRandomParty()).computeIfAbsent(name.getLanguage(), k -> new HashSet<>());
		namesByParty.get(name.getRandomParty()).get(name.getLanguage()).add(name);
	}

	private void addAdjective(PartyAdjective adjective) {
		if (adjectivesByParty == null) {
			adjectivesByParty = new HashMap<>();
		}
		adjectivesByParty.computeIfAbsent(adjective.getRandomParty(), k -> new HashMap<>());
		adjectivesByParty.get(adjective.getRandomParty()).computeIfAbsent(adjective.getLanguage(),
				k -> new HashSet<>());
		adjectivesByParty.get(adjective.getRandomParty()).get(adjective.getLanguage()).add(adjective);
	}

	@Override
	protected RandomParty createElement(ITranslator translator, String partyId, String name, String description,
										String language, String moduleName)
			throws InvalidXmlElementException {
		final RandomParty randomParty = new RandomParty(partyId, name, language, moduleName);

		int node = 0;
		while (true) {
			try {
				final String profile = translator.getNodeValue(partyId, MEMBER, PROFILE, node);
				final String profileName = translator.getNodeValue(partyId, MEMBER, NAME, node, language);
				final String minNumberTag = translator.getNodeValue(partyId, MEMBER, MINIMUM_NUMBER, node);
				Integer minNumber = null;
				if (minNumberTag != null) {
					try {
						minNumber = Integer.parseInt(minNumberTag);
					} catch (NumberFormatException e) {
						throw new InvalidRandomPartyException(
								"Invalid random party definition. Parameter minNumber has an incorrect value '"
										+ minNumberTag + "'.", e);
					}
				}
				final String maxNumberTag = translator.getNodeValue(partyId, MEMBER, MAXIMUM_NUMBER, node);
				Integer maxNumber = null;
				if (maxNumberTag != null) {
					try {
						maxNumber = Integer.parseInt(maxNumberTag);
					} catch (NumberFormatException e) {
						throw new InvalidRandomPartyException(
								"Invalid random party definition. Parameter maxNumber has an incorrect value '"
										+ maxNumberTag + "'.", e);
					}
				}
				final String weightTag = translator.getNodeValue(partyId, MEMBER, WEIGHT, node);
				Integer weight = null;
				if (weightTag != null) {
					try {
						weight = Integer.parseInt(weightTag);
					} catch (NumberFormatException e) {
						throw new InvalidRandomPartyException(
								"Invalid random party definition. Parameter weight has an incorrect value '"
										+ weightTag + "'.", e);
					}
				}

				final Set<IRandomPreference> randomPreferences = new HashSet<>();
				final String preferencesSelectedNames = translator.getNodeValue(partyId, MEMBER, EXTRA_PREFERENCES,
						node);
				if (preferencesSelectedNames != null) {
					final StringTokenizer preferencesSelectedTokenizer = new StringTokenizer(preferencesSelectedNames,
							",");
					while (preferencesSelectedTokenizer.hasMoreTokens()) {
						randomPreferences.add(RandomPreferenceUtils.getSelectedPreference(preferencesSelectedTokenizer
								.nextToken().trim()));
					}
				}

				if (weight == null && maxNumber == null) {
					throw new InvalidRandomPartyException("Weight or maxNumber parameter must be set.");
				}

				randomParty.getRandomPartyMembers().add(
						new RandomPartyMember(partyId + "_" + node, profileName, language, moduleName,
								RandomProfileFactory.getInstance().getElement(profile, language, moduleName),
								minNumber, maxNumber, weight, randomPreferences));
				node++;
			} catch (InvalidRandomPartyException e) {
				break;
			}
		}

		// Random options
		final String partyNames = translator.getNodeValue(partyId, RANDOM, PARTY_NAME, language);
		if (partyNames != null) {
			final StringTokenizer partyNamesTokenizer = new StringTokenizer(partyNames, ",");
			while (partyNamesTokenizer.hasMoreTokens()) {
				addName(new PartyName(partyNamesTokenizer.nextToken().trim(), randomParty, language, moduleName));
			}
		}

		final String partyAdjectives = translator.getNodeValue(partyId, RANDOM, PARTY_ADJECTIVE, language);
		if (partyAdjectives != null) {
			final StringTokenizer partyAdjectivesTokenizer = new StringTokenizer(partyAdjectives, ",");
			while (partyAdjectivesTokenizer.hasMoreTokens()) {
				addAdjective(new PartyAdjective(partyAdjectivesTokenizer.nextToken().trim(), randomParty, language,
						moduleName));
			}
		}

		return randomParty;
	}

	public static String getPartyName(PartyName partyName, PartyAdjective partyAdjective, String language) {
		if (partyName == null || partyAdjective == null) {
			return null;
		}
		if (language.equals("es")) {
			final StringBuilder name = new StringBuilder();
			name.append(partyName.getName());
			name.append(" ");
			// Genre modification.
			if (partyAdjective.getName().startsWith("de ")) {
				name.append(partyAdjective.getName());
			} else if (partyName.getName().endsWith("as")) {
				name.append(partyAdjective.getName(), 0, partyAdjective.getName().length() - 2);
				name.append("as");
			}
			return name.toString();
		} else {
			return partyAdjective.getName() + " " + partyName.getName();
		}
	}

	public Set<PartyName> getNames(RandomParty randomParty) {
		try {
			return namesByParty.get(randomParty).get(randomParty.getLanguage());
		} catch (Exception e) {
			return null;
		}
	}

	public Set<PartyAdjective> getAdjectives(RandomParty randomParty) {
		try {
			return adjectivesByParty.get(randomParty).get(randomParty.getLanguage());
		} catch (Exception e) {
			return null;
		}
	}

}
