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
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.RandomizeCharacter;
import com.softwaremagico.tm.character.Settings;
import com.softwaremagico.tm.character.blessings.TooManyBlessingsException;
import com.softwaremagico.tm.character.exceptions.RestrictedElementException;
import com.softwaremagico.tm.character.exceptions.UnofficialElementNotAllowedException;
import com.softwaremagico.tm.log.MachineLog;
import com.softwaremagico.tm.party.Party;
import com.softwaremagico.tm.random.RandomSelector;
import com.softwaremagico.tm.random.exceptions.DuplicatedPreferenceException;
import com.softwaremagico.tm.random.exceptions.ImpossibleToAssignMandatoryElementException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.IRandomPreference;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class RandomPartyDefinition extends RandomSelector<RandomPartyMember> {
    private static final int THREAT_MARGIN = 10;
    private static final int MAX_ITERATIONS = 10000;
    private Party party = null;
    private final int threatLevel;
    private Map<RandomPartyMember, Integer> profilesAssigned = new HashMap<>();
    // RandomParty id --> Threat
    private Map<RandomPartyMember, Integer> threatByProfile = new HashMap<>();
    private final String moduleName;
    private final Settings settings;

    protected RandomPartyDefinition(RandomParty randomParty, int threatLevel, String moduleName,
                                    Set<IRandomPreference<?>> preferences) throws InvalidXmlElementException,
            UnofficialElementNotAllowedException, RestrictedElementException {
        super(null, randomParty, preferences, new HashSet<>(), new HashSet<>());
        settings = new Settings();
        this.moduleName = moduleName;
        this.threatLevel = threatLevel;
    }

    private Integer getProfileAssigned(RandomPartyMember member) {
        if (profilesAssigned == null) {
            profilesAssigned = new HashMap<>();
        }
        profilesAssigned.putIfAbsent(member, 0);
        return profilesAssigned.get(member);
    }

    private Integer getThreatByProfile(RandomPartyMember member) {
        if (threatByProfile == null) {
            threatByProfile = new HashMap<>();
        }
        threatByProfile.putIfAbsent(member, 0);
        return threatByProfile.get(member);
    }

    private void assignProfile(RandomPartyMember member) throws TooManyBlessingsException, InvalidXmlElementException,
            DuplicatedPreferenceException, InvalidRandomElementSelectedException, RestrictedElementException, UnofficialElementNotAllowedException {
        if (member.getMaxNumber() != null && member.getMaxNumber() >= getProfileAssigned(member)) {
            return;
        }
        profilesAssigned.put(member, getProfileAssigned(member) + 1);
        final CharacterPlayer characterPlayer = createCharacter(member);
        getParty().addMember(characterPlayer);
        threatByProfile.put(member, getThreatByProfile(member) + getParty().getThreatLevel(characterPlayer));
        if (member.getMaxNumber() != null && member.getMaxNumber() >= getProfileAssigned(member)) {
            removeElementWeight(member);
        }
    }

    public Party getParty() {
        if (party == null) {
            party = new Party(getElementWithRandomElements().getLanguage(), getModuleName());
        }
        return party;
    }

    @Override
    protected Collection<RandomPartyMember> getAllElements() throws InvalidXmlElementException {
        return getElementWithRandomElements().getAllElements();
    }

    @Override
    public void assign() throws InvalidXmlElementException {
        if (getParty().getPartyName() == null || getParty().getPartyName().isEmpty()) {
            getParty().setPartyName(createName());
        }

        int node = 0;
        while (node < MAX_ITERATIONS) {
            // Select a skill randomly.
            final RandomPartyMember partyMember;
            try {
                partyMember = selectElementByWeight();
                try {
                    assignProfile(partyMember);
                    // Party threat increased. Update weights.
                    updateWeights();
                } catch (TooManyBlessingsException | DuplicatedPreferenceException |
                         InvalidRandomElementSelectedException |
                         RestrictedElementException | UnofficialElementNotAllowedException e) {
                    MachineLog.errorMessage(this.getClass().getName(), e);
                }
                node++;
            } catch (InvalidRandomElementSelectedException e) {
                // No more members available;
                break;
            }
        }
    }

    private String createName() {
        return RandomPartyFactory.getPartyName(
                pickOneRandom(RandomPartyFactory.getInstance().getNames((RandomParty) getElementWithRandomElements())),
                pickOneRandom(RandomPartyFactory.getInstance().getAdjectives(
                        (RandomParty) getElementWithRandomElements())), getElementWithRandomElements().getLanguage());
    }

    private <T> T pickOneRandom(Set<T> elements) {
        if (elements == null || elements.isEmpty()) {
            return null;
        }
        final int index = random.nextInt(elements.size());
        final Iterator<T> iter = elements.iterator();
        for (int i = 0; i < index; i++) {
            iter.next();
        }
        return iter.next();
    }

    @Override
    protected void assignMandatoryValues(Set<RandomPartyMember> mandatoryValues) throws InvalidXmlElementException {
        // Already assigned
    }

    private CharacterPlayer createCharacter(RandomPartyMember member) throws InvalidXmlElementException, InvalidRandomElementSelectedException,
            RestrictedElementException, UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(member.getLanguage(), member.getModuleName());
        characterPlayer.getSettings().copy(settings);
        final RandomizeCharacter randomizeCharacter = new RandomizeCharacter(characterPlayer, getPreferences(),
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
        } catch (TooManyBlessingsException | DuplicatedPreferenceException | InvalidRandomElementSelectedException |
                 RestrictedElementException |
                 UnofficialElementNotAllowedException e) {
            throw new ImpossibleToAssignMandatoryElementException("Character Player could not be generated.", e);
        }
    }

    @Override
    protected int getWeight(RandomPartyMember member) throws InvalidRandomElementSelectedException {
        // Threat estimation.
        if (getProfileAssigned(member) > 0) {
            final int estimatedThreat = getThreatByProfile(member) / getProfileAssigned(member);
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

    public String getModuleName() {
        return moduleName;
    }
}
