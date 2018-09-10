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

import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.Name;
import com.softwaremagico.tm.character.Surname;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.race.InvalidRaceException;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.IRandomPreferences;

public class RandomSurname extends RandomSelector<Surname> {
	private final static int GOOD_PROBABILITY = 1;

	protected RandomSurname(CharacterPlayer characterPlayer, Set<IRandomPreferences> preferences) throws InvalidXmlElementException {
		super(characterPlayer, preferences);
	}

	public void assignSurname() throws InvalidRaceException, InvalidRandomElementSelectedException {
		getCharacterPlayer().getInfo().setSurname(selectElementByWeight());
	}

	@Override
	protected TreeMap<Integer, Surname> assignElementsWeight() throws InvalidXmlElementException {
		TreeMap<Integer, Surname> weightedSurnames = new TreeMap<>();
		int count = 1;
		for (Surname surname : FactionsFactory.getInstance().getAllSurnames()) {
			int weight = getWeight(surname);
			if (weight > 0) {
				weightedSurnames.put(count, surname);
				count += weight;
			}
		}
		return weightedSurnames;
	}

	@Override
	protected int getWeight(Surname surname) {
		// Nobility has faction as surname
		if (getCharacterPlayer().getFaction() != null && getCharacterPlayer().getFaction().getFactionGroup() == FactionGroup.NOBILITY) {
			if (getCharacterPlayer().getFaction().getName().contains(surname.getName())) {
				return GOOD_PROBABILITY;
			} else {
				return 0;
			}
		}
		// Not nobility no faction as surname.
		try {
			for (Faction faction : FactionsFactory.getInstance().getElements(getCharacterPlayer().getLanguage())) {
				if (faction.getName().contains(surname.getName())) {
					return 0;
				}
			}
		} catch (InvalidXmlElementException e) {
			RandomGenerationLog.errorMessage(this.getClass().getName(), e);
		}
		// Name already set, use same faction to avoid weird mix.
		if (!getCharacterPlayer().getInfo().getNames().isEmpty()) {
			Name firstName = getCharacterPlayer().getInfo().getNames().get(0);
			if (!Objects.equals(firstName.getFaction(), surname.getFaction())) {
				return 0;
			} else {
				return GOOD_PROBABILITY;
			}
		}

		// Not nobility and not name set, use surnames of the planet.
		if (getCharacterPlayer().getInfo().getPlanet() != null && !getCharacterPlayer().getInfo().getPlanet().getSurnames().isEmpty()) {
			if (getCharacterPlayer().getInfo().getPlanet().getFactions().contains(surname.getFaction())) {
				return GOOD_PROBABILITY;
			} else {
				return 0;
			}
		}
		return 1;
	}
}
