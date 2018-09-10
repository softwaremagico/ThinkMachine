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
import com.softwaremagico.tm.character.benefices.BeneficeSpecialization;
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.race.InvalidRaceException;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.IRandomPreferences;
import com.softwaremagico.tm.random.selectors.NamesPreferences;

public class RandomName extends RandomSelector<Name> {
	private final static int GOOD_PROBABILITY = 1;

	protected RandomName(CharacterPlayer characterPlayer, Set<IRandomPreferences> preferences) throws InvalidXmlElementException {
		super(characterPlayer, preferences);
	}

	public void assignName() throws InvalidRaceException, InvalidRandomElementSelectedException {
		NamesPreferences namesPreference = NamesPreferences.getSelected(getPreferences());
		try {
			BeneficeSpecialization status = getCharacterPlayer().getStatus();
			// Nobility with more names. Unless set by the user.
			if (status != null && namesPreference == NamesPreferences.LOW && !getPreferences().contains(NamesPreferences.LOW)) {
				namesPreference = NamesPreferences.getByStatus(status.getCost());
			}
		} catch (InvalidXmlElementException e) {
			// Status error.
			RandomGenerationLog.errorMessage(this.getClass().getName(), e);
		}

		for (int i = 0; i < namesPreference.randomGaussian(); i++) {
			try {
				Name selectedName = selectElementByWeight();
				getCharacterPlayer().getInfo().addName(selectedName);
				removeElementWeight(selectedName);
				// Remove names from different factions. All names must be from
				// same faction
				for (Name name : FactionsFactory.getInstance().getAllNames()) {
					if (!Objects.equals(name.getFaction(), selectedName.getFaction())) {
						removeElementWeight(name);
					}
				}
			} catch (InvalidRandomElementSelectedException e) {
				throw new InvalidRandomElementSelectedException("No possible name for faction '" + getCharacterPlayer().getFaction() + "' at '"
						+ getCharacterPlayer().getInfo().getPlanet() + "'.", e);
			}
		}
	}

	@Override
	protected TreeMap<Integer, Name> assignElementsWeight() throws InvalidXmlElementException {
		TreeMap<Integer, Name> weightedNames = new TreeMap<>();
		int count = 1;
		for (Name name : FactionsFactory.getInstance().getAllNames()) {
			int weight = getWeight(name);
			if (weight > 0) {
				weightedNames.put(count, name);
				count += weight;
			}
		}
		return weightedNames;
	}

	@Override
	protected int getWeight(Name name) {
		// Only names of its gender.
		if (!name.getGender().equals(getCharacterPlayer().getInfo().getGender())) {
			return 0;
		}
		// Nobility almost always names of her planet.
		if (getCharacterPlayer().getFaction() != null && getCharacterPlayer().getFaction().getFactionGroup() == FactionGroup.NOBILITY) {
			if (getCharacterPlayer().getFaction().equals(name.getFaction())) {
				return GOOD_PROBABILITY;
			} else {
				return 0;
			}
		}
		// Not nobility, use names available on the planet.
		if (getCharacterPlayer().getInfo().getPlanet() != null && !getCharacterPlayer().getInfo().getPlanet().getNames().isEmpty()) {
			if (getCharacterPlayer().getInfo().getPlanet().getFactions().contains(name.getFaction())) {
				return GOOD_PROBABILITY;
			} else {
				return 0;
			}
		}
		return 1;
	}
}
