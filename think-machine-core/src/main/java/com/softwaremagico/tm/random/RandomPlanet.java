package com.softwaremagico.tm.random;

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

import java.util.Set;
import java.util.TreeMap;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.planet.Planet;
import com.softwaremagico.tm.character.planet.PlanetFactory;
import com.softwaremagico.tm.character.race.InvalidRaceException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.IRandomPreferences;

public class RandomPlanet extends RandomSelector<Planet> {
	private final static int FACTION_PLANET = 50;
	private final static int NEUTRAL_PLANET = 5;
	private final static int ENEMY_PLANET = 1;

	protected RandomPlanet(CharacterPlayer characterPlayer, Set<IRandomPreferences> preferences) throws InvalidXmlElementException {
		super(characterPlayer, preferences);
	}

	public void assignPlanet() throws InvalidRaceException, InvalidRandomElementSelectedException {
		getCharacterPlayer().getInfo().setPlanet(selectElementByWeight());
	}

	@Override
	protected TreeMap<Integer, Planet> assignElementsWeight() throws InvalidXmlElementException {
		TreeMap<Integer, Planet> weightedPlanets = new TreeMap<>();
		int count = 1;
		for (Planet planet : PlanetFactory.getInstance().getElements(getCharacterPlayer().getLanguage())) {
			int weight = getWeight(planet);
			if (weight > 0) {
				weightedPlanets.put(count, planet);
				count += weight;
			}
		}
		return weightedPlanets;
	}

	@Override
	protected int getWeight(Planet planet) {
		if (planet.getFactions().contains(getCharacterPlayer().getFaction())) {
			return FACTION_PLANET;
		}
		for (Faction factionsOfPlanet : planet.getFactions()) {
			if (factionsOfPlanet.getFactionGroup() == getCharacterPlayer().getFaction().getFactionGroup()) {
				return ENEMY_PLANET;
			}
		}
		return NEUTRAL_PLANET;
	}
}
