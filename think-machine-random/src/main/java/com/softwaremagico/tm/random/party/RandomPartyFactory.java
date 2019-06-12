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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.random.profiles.RandomProfileFactory;
import com.softwaremagico.tm.random.selectors.IRandomPreference;
import com.softwaremagico.tm.random.selectors.RandomPreferenceUtils;

public class RandomPartyFactory extends XmlFactory<RandomParty> {
	private static final ITranslator translatorRandomParty = LanguagePool.getTranslator("parties.xml");

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

	private static RandomPartyFactory instance;

	private static void createInstance() {
		if (instance == null) {
			synchronized (RandomPartyFactory.class) {
				if (instance == null) {
					instance = new RandomPartyFactory();
				}
			}
		}
	}

	public static RandomPartyFactory getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}

	@Override
	protected ITranslator getTranslator() {
		return translatorRandomParty;
	}

	private void addName(PartyName name) {
		if (namesByParty == null) {
			namesByParty = new HashMap<>();
		}
		if (namesByParty.get(name.getRandomParty()) == null) {
			namesByParty.put(name.getRandomParty(), new HashMap<String, Set<PartyName>>());
		}
		if (namesByParty.get(name.getRandomParty()).get(name.getLanguage()) == null) {
			namesByParty.get(name.getRandomParty()).put(name.getLanguage(), new HashSet<PartyName>());
		}
		namesByParty.get(name.getRandomParty()).get(name.getLanguage()).add(name);
	}

	private void addAdjective(PartyAdjective adjective) {
		if (adjectivesByParty == null) {
			adjectivesByParty = new HashMap<>();
		}
		if (adjectivesByParty.get(adjective.getRandomParty()) == null) {
			adjectivesByParty.put(adjective.getRandomParty(), new HashMap<String, Set<PartyAdjective>>());
		}
		if (adjectivesByParty.get(adjective.getRandomParty()).get(adjective.getLanguage()) == null) {
			adjectivesByParty.get(adjective.getRandomParty()).put(adjective.getLanguage(),
					new HashSet<PartyAdjective>());
		}
		adjectivesByParty.get(adjective.getRandomParty()).get(adjective.getLanguage()).add(adjective);
	}

	@Override
	protected RandomParty createElement(ITranslator translator, String partyId, String language)
			throws InvalidXmlElementException {
		final String name = translator.getNodeValue(partyId, NAME, language);

		final RandomParty randomParty = new RandomParty(partyId, name, language);

		int node = 0;
		while (true) {
			try {
				final String profile = translator.getNodeValue(partyId, MEMBER, PROFILE, node);
				final String profileName = translator.getNodeValue(partyId, MEMBER, NAME, language, node);
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
						new RandomPartyMember(partyId + "_" + node, profileName, language, RandomProfileFactory
								.getInstance().getElement(profile, language), minNumber, maxNumber, weight,
								randomPreferences));
				node++;
			} catch (Exception e) {
				break;
			}
		}

		// Random options
		final String partyNames = translator.getNodeValue(partyId, RANDOM, PARTY_NAME, language);
		if (partyNames != null) {
			final StringTokenizer partyNamesTokenizer = new StringTokenizer(partyNames, ",");
			while (partyNamesTokenizer.hasMoreTokens()) {
				addName(new PartyName(partyNamesTokenizer.nextToken().trim(), randomParty, language));
			}
		}

		final String partyAdjectives = translator.getNodeValue(partyId, RANDOM, PARTY_ADJECTIVE, language);
		if (partyAdjectives != null) {
			final StringTokenizer partyAdjectivesTokenizer = new StringTokenizer(partyAdjectives, ",");
			while (partyAdjectivesTokenizer.hasMoreTokens()) {
				addAdjective(new PartyAdjective(partyAdjectivesTokenizer.nextToken().trim(), randomParty, language));
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
				name.append(partyAdjective.getName().substring(0, partyAdjective.getName().length() - 2));
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
