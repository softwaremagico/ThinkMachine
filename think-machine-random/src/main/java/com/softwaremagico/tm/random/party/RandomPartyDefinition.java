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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.RandomizeCharacter;
import com.softwaremagico.tm.character.blessings.TooManyBlessingsException;
import com.softwaremagico.tm.log.MachineLog;
import com.softwaremagico.tm.party.Party;
import com.softwaremagico.tm.random.RandomSelector;
import com.softwaremagico.tm.random.exceptions.DuplicatedPreferenceException;
import com.softwaremagico.tm.random.exceptions.ImpossibleToAssignMandatoryElementException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.IRandomPreference;

public class RandomPartyDefinition extends RandomSelector<RandomPartyMember> {
	private final static int THREAT_MARGIN = 10;
	private Party party = null;
	private final int threatLevel;
	private Map<RandomPartyMember, Integer> profilesAssigned = new HashMap<>();
	// RandomParty id --> Threat
	private Map<RandomPartyMember, Integer> threatByProfile = new HashMap<>();

	protected RandomPartyDefinition(RandomParty randomParty, int threatLevel, Set<IRandomPreference> preferences)
			throws InvalidXmlElementException {
		super(null, randomParty, preferences, new HashSet<RandomPartyMember>(), new HashSet<RandomPartyMember>());
		this.threatLevel = threatLevel;
	}

	private Integer getProfileAssigned(RandomPartyMember member) {
		if (profilesAssigned == null) {
			profilesAssigned = new HashMap<>();
		}
		if (profilesAssigned.get(member) == null) {
			profilesAssigned.put(member, 0);
		}
		return profilesAssigned.get(member);
	}

	private Integer getThreatByProfile(RandomPartyMember member) {
		if (threatByProfile == null) {
			threatByProfile = new HashMap<>();
		}
		if (threatByProfile.get(member) == null) {
			threatByProfile.put(member, 0);
		}
		return threatByProfile.get(member);
	}

	private void assignProfile(RandomPartyMember member) throws TooManyBlessingsException, InvalidXmlElementException,
			DuplicatedPreferenceException, InvalidRandomElementSelectedException {
		if (member.getMaxNumber() != null && member.getMaxNumber() >= getProfileAssigned(member)) {
			return;
		}
		profilesAssigned.put(member, getProfileAssigned(member) + 1);
		CharacterPlayer characterPlayer = createCharacter(member);
		getParty().addCharacter(characterPlayer);
		System.out.println(getParty().getCharacterPlayers().size());
		threatByProfile.put(member, getThreatByProfile(member) + getParty().getThreatLevel(characterPlayer));
		if (member.getMaxNumber() != null && member.getMaxNumber() >= getProfileAssigned(member)) {
			removeElementWeight(member);
		}
	}

	public Party getParty() {
		if (party == null) {
			party = new Party(null, getElementWithRandomElements().getName(), getElementWithRandomElements()
					.getLanguage());
		}
		return party;
	}

	@Override
	protected Collection<RandomPartyMember> getAllElements() throws InvalidXmlElementException {
		return getElementWithRandomElements().getAllElements();
	}

	@Override
	public void assign() throws InvalidXmlElementException {
		int i = 0;
		while (true) {
			System.out.println(i++ + "->" + getWeightedElements());
			// Select a skill randomly.
			RandomPartyMember partyMember;
			try {
				partyMember = selectElementByWeight();
				try {
					assignProfile(partyMember);
					// Party threat increased. Update weights.
					updateWeights();
				} catch (TooManyBlessingsException | DuplicatedPreferenceException
						| InvalidRandomElementSelectedException e) {
					MachineLog.errorMessage(this.getClass().getName(), e);
				}
			} catch (InvalidRandomElementSelectedException e) {
				// No more members available;
				break;
			}
		}
	}

	@Override
	protected void assignMandatoryValues(Set<RandomPartyMember> mandatoryValues) throws InvalidXmlElementException {
		// Already assigned
	}

	private CharacterPlayer createCharacter(RandomPartyMember member) throws TooManyBlessingsException,
			DuplicatedPreferenceException, InvalidXmlElementException, InvalidRandomElementSelectedException {
		CharacterPlayer characterPlayer = new CharacterPlayer(member.getLanguage());
		RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, getPreferences(),
				member.getRandomProfile());
		randomizeCharacter.createCharacter();
		return characterPlayer;
	}

	@Override
	protected void assignIfMandatory(RandomPartyMember member) throws InvalidXmlElementException,
			ImpossibleToAssignMandatoryElementException {
		try {
			while (member.getMinNumber() != null && member.getMinNumber() < getProfileAssigned(member)) {
				assignProfile(member);
			}
		} catch (TooManyBlessingsException | DuplicatedPreferenceException | InvalidRandomElementSelectedException e) {
			throw new ImpossibleToAssignMandatoryElementException("Character Player could not be generated.", e);
		}
	}

	@Override
	protected int getWeight(RandomPartyMember member) throws InvalidRandomElementSelectedException {
		// Threat estimation.
		if (getProfileAssigned(member) > 0) {
			int estimatedThreat = getThreatByProfile(member) / getProfileAssigned(member);
			if (getParty().getThreatLevel() + estimatedThreat > threatLevel + THREAT_MARGIN) {
				return 0;
			}
		}
		if (member.getMaxNumber() != null && member.getMaxNumber() >= getProfileAssigned(member)) {
			return 0;
		}

		if (member.getWeight() == null) {
			return 1;
		}
		return member.getWeight();
	}
}
