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
import com.softwaremagico.tm.character.planets.Planet;
import com.softwaremagico.tm.character.planets.PlanetFactory;
import com.softwaremagico.tm.character.races.InvalidRaceException;
import com.softwaremagico.tm.random.RandomSelector;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.IRandomPreference;

import java.util.Collection;
import java.util.Set;

public class RandomPlanet extends RandomSelector<Planet> {
    private static final int FACTION_PLANET = 50;
    private static final int NEUTRAL_PLANET = 8;
    private static final int ENEMY_PLANET = 1;

    public RandomPlanet(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences)
            throws InvalidXmlElementException, RestrictedElementException {
        super(characterPlayer, preferences);
    }

    @Override
    public void assign() throws InvalidRaceException, InvalidRandomElementSelectedException {
        getCharacterPlayer().getInfo().setPlanet(selectElementByWeight());
    }

    @Override
    protected Collection<Planet> getAllElements() throws InvalidXmlElementException {
        return PlanetFactory.getInstance().getElements(getCharacterPlayer().getLanguage(),
                getCharacterPlayer().getModuleName());
    }

    @Override
    protected int getWeight(Planet planet) {
        //By race
        if (!getCharacterPlayer().getRace().getPlanets().isEmpty() && !getCharacterPlayer().getRace().getPlanets().contains(planet)) {
            return 0;
        }
        //By faction
        if (planet.getFactions().contains(getCharacterPlayer().getFaction())) {
            return FACTION_PLANET;
        }
        for (final Faction factionsOfPlanet : planet.getFactions()) {
            if (factionsOfPlanet.getRestrictedToFactionGroup() == getCharacterPlayer().getFaction().getFactionGroup()) {
                return ENEMY_PLANET;
            }
        }
        return NEUTRAL_PLANET;
    }

    @Override
    protected void assignIfMandatory(Planet element) throws InvalidXmlElementException {
        return;
    }

    @Override
    protected void assignMandatoryValues(Set<Planet> mandatoryValues) throws InvalidXmlElementException {
        return;
    }
}
