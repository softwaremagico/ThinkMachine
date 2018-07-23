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
import com.softwaremagico.tm.character.race.InvalidRaceException;
import com.softwaremagico.tm.character.race.Race;
import com.softwaremagico.tm.character.race.RaceFactory;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.IRandomPreferences;
import com.softwaremagico.tm.random.selectors.RacePreferences;

public class RandomRace extends RandomSelector<Race> {

	protected RandomRace(CharacterPlayer characterPlayer, Set<IRandomPreferences> preferences) throws InvalidXmlElementException {
		super(characterPlayer, preferences);
	}

	public void assignRace() throws InvalidRaceException, InvalidRandomElementSelectedException {
		getCharacterPlayer().setRace(selectElementByWeight());
	}

	@Override
	protected TreeMap<Integer, Race> assignElementsWeight() throws InvalidXmlElementException {
		TreeMap<Integer, Race> weightedRaces = new TreeMap<>();
		int count = 1;
		for (Race race : RaceFactory.getInstance().getElements(getCharacterPlayer().getLanguage())) {
			int weight = getWeight(race);
			if (weight > 0) {
				weightedRaces.put(count, race);
				count += weight;
			}
		}
		return weightedRaces;
	}

	@Override
	protected int getWeight(Race race) {
		// Weapons only if technology is enough.
		// Specialization desired.
		RacePreferences selectedSpecialization = RacePreferences.getSelected(getPreferences());
		if (selectedSpecialization != null) {
			if (!race.getId().equalsIgnoreCase(selectedSpecialization.name())) {
				return 0;
			}
		}
		return race.getRandomDefinition().getProbability();
	}
}
