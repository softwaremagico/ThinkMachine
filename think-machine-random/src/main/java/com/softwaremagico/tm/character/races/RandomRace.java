package com.softwaremagico.tm.character.races;

/*-
 * #%L
 * Think Machine (Core)
 * %%
 * Copyright (C) 2017 - 2018 Softwaremagico
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
import com.softwaremagico.tm.character.RestrictedElementException;
import com.softwaremagico.tm.character.UnofficialElementNotAllowedException;
import com.softwaremagico.tm.random.RandomSelector;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.IRandomPreference;
import com.softwaremagico.tm.random.selectors.RacePreferences;

import java.util.Collection;
import java.util.Set;

public class RandomRace extends RandomSelector<Race> {

    public RandomRace(CharacterPlayer characterPlayer, Set<IRandomPreference<?>> preferences)
            throws InvalidXmlElementException, RestrictedElementException, UnofficialElementNotAllowedException {
        super(characterPlayer, preferences);
    }

    @Override
    public void assign() throws InvalidRaceException, InvalidRandomElementSelectedException {
        try {
            getCharacterPlayer().setRace(selectElementByWeight());
        } catch (RestrictedElementException | UnofficialElementNotAllowedException e) {
            throw new InvalidRaceException("Race is restricted", e);
        }
    }

    @Override
    protected Collection<Race> getAllElements() throws InvalidXmlElementException {
        return RaceFactory.getInstance().getElements(getCharacterPlayer().getLanguage(), getCharacterPlayer().getModuleName());
    }

    @Override
    protected int getWeight(Race race) throws InvalidRandomElementSelectedException {
        // Specialization desired.
        final RacePreferences selectedSpecialization = RacePreferences.getSelected(getPreferences());
        if (selectedSpecialization != null) {
            if (selectedSpecialization == RacePreferences.HUMAN) {
                //Only human
                if (!race.getId().equalsIgnoreCase(RacePreferences.HUMAN.name())) {
                    throw new InvalidRandomElementSelectedException("Race '" + race + "' restricted by user preferences.");
                }
            } else {
                //Not human
                if (race.getId().equalsIgnoreCase(RacePreferences.HUMAN.name())) {
                    throw new InvalidRandomElementSelectedException("Race '" + race + "' restricted by user preferences.");
                }
            }
        }

        //Factions aligned with its race
        if (race.isXeno() && getCharacterPlayer().getFaction() != null && getCharacterPlayer().getFaction().getRestrictedToRaces().size() == 1 &&
                getCharacterPlayer().getFaction().getRestrictedToRaces().contains(race)) {
            return 100;
        }

        return 1;
    }

    @Override
    protected void assignIfMandatory(Race element) throws InvalidXmlElementException {
        return;
    }

    @Override
    protected void assignMandatoryValues(Set<Race> mandatoryValues) throws InvalidXmlElementException {
        return;
    }
}
