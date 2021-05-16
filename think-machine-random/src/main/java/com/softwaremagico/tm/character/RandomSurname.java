package com.softwaremagico.tm.character;

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
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.races.InvalidRaceException;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.RandomSelector;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.IRandomPreference;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public class RandomSurname extends RandomSelector<Surname> {

    public RandomSurname(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences)
            throws InvalidXmlElementException, RestrictedElementException {
        super(characterPlayer, preferences);
    }

    @Override
    public void assign() throws InvalidRaceException, InvalidRandomElementSelectedException {
        getCharacterPlayer().getInfo().setSurname(selectElementByWeight());
    }

    @Override
    protected Collection<Surname> getAllElements() {
        return FactionsFactory.getInstance().getAllSurnames();
    }

    @Override
    protected int getWeight(Surname surname) {
        // Nobility has faction as surname
        if (getCharacterPlayer().getFaction() != null
                && getCharacterPlayer().getFaction().getFactionGroup() == FactionGroup.NOBILITY) {
            if (getCharacterPlayer().getFaction().getName().contains(surname.getName())) {
                return BASIC_PROBABILITY;
            } else {
                return 0;
            }
        }
        // Not nobility no faction as surname.
        try {
            for (final Faction faction : FactionsFactory.getInstance().getElements(getCharacterPlayer().getLanguage(), getCharacterPlayer().getModuleName())) {
                if (faction.getName().contains(surname.getName())) {
                    return 0;
                }
            }
        } catch (InvalidXmlElementException e) {
            RandomGenerationLog.errorMessage(this.getClass().getName(), e);
        }
        // Name already set, use same faction to avoid weird mix.
        if (!getCharacterPlayer().getInfo().getNames().isEmpty()) {
            final Name firstName = getCharacterPlayer().getInfo().getNames().get(0);
            if (firstName.getFaction() != null && !Objects.equals(firstName.getFaction(), surname.getFaction())) {
                return 0;
            } else {
                return BASIC_PROBABILITY;
            }
        }

        // Not nobility and not name set, use surnames of the planet.
        if (getCharacterPlayer().getInfo().getPlanet() != null
                && !getCharacterPlayer().getInfo().getPlanet().getSurnames().isEmpty()) {
            if (getCharacterPlayer().getInfo().getPlanet().getHumanFactions().contains(surname.getFaction())) {
                return BASIC_PROBABILITY;
            } else {
                return 0;
            }
        }
        // Planet without factions. Then choose own faction names
        if (getCharacterPlayer().getFaction() != null
                && !FactionsFactory.getInstance().getAllSurnames(getCharacterPlayer().getFaction()).isEmpty()
                && !getCharacterPlayer().getFaction().equals(surname.getFaction())) {
            return 0;
        }
        return 1;
    }

    @Override
    protected void assignIfMandatory(Surname element) throws InvalidXmlElementException {
    }

    @Override
    protected void assignMandatoryValues(Set<Surname> mandatoryValues) throws InvalidXmlElementException {
    }

}
