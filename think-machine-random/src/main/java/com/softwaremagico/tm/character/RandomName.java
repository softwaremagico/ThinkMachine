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

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.benefices.BeneficeSpecialization;
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.races.InvalidRaceException;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.RandomSelector;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.IRandomPreference;
import com.softwaremagico.tm.random.selectors.NamesPreferences;

public class RandomName extends RandomSelector<Name> {

	public RandomName(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences) throws InvalidXmlElementException {
		super(characterPlayer, preferences);
	}

	@Override
	public void assign() throws InvalidRaceException, InvalidRandomElementSelectedException {
		NamesPreferences namesPreference = NamesPreferences.getSelected(getPreferences());
		try {
			final BeneficeSpecialization status = getCharacterPlayer().getStatus();
			// Nobility with more names. Unless set by the user.
			if (status != null && namesPreference == NamesPreferences.LOW && !getPreferences().contains(NamesPreferences.LOW)) {
				namesPreference = NamesPreferences.getByStatus(status.getCost());
			}
		} catch (InvalidXmlElementException e) {
			// Status
			RandomGenerationLog.errorMessage(this.getClass().getName(), e);
		}

		for (int i = 0; i < namesPreference.randomGaussian(); i++) {
			try {
				final Name selectedName = selectElementByWeight();
				getCharacterPlayer().getInfo().addName(selectedName);
				removeElementWeight(selectedName);
				// Remove names from different factions. All names must be from
				// same faction
				for (final Name name : FactionsFactory.getInstance().getAllNames()) {
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
	protected Collection<Name> getAllElements() throws InvalidXmlElementException {
		return FactionsFactory.getInstance().getAllNames();
	}

	@Override
	protected int getWeight(Name name) throws InvalidRandomElementSelectedException {
		// Only names of its gender.
		if (!name.getGender().equals(getCharacterPlayer().getInfo().getGender())) {
			throw new InvalidRandomElementSelectedException("Name '" + name.getGender() + "' not valid for gender '"
					+ getCharacterPlayer().getInfo().getGender() + "'.");
		}
		// Nobility almost always names of her planet.
		if (getCharacterPlayer().getFaction() != null && getCharacterPlayer().getFaction().getFactionGroup() == FactionGroup.NOBILITY) {
			if (getCharacterPlayer().getFaction().equals(name.getFaction())) {
				return BASIC_PROBABILITY;
			} else {
				throw new InvalidRandomElementSelectedException("Name '" + name.getGender() + "' not allowed for a nobility based character.");
			}
		}
		// Not nobility, use names available on the planet.
		if (getCharacterPlayer().getInfo().getPlanet() != null && !getCharacterPlayer().getInfo().getPlanet().getNames().isEmpty()) {
			if (getCharacterPlayer().getInfo().getPlanet().getFactions().contains(name.getFaction())) {
				return BASIC_PROBABILITY;
			} else {
				throw new InvalidRandomElementSelectedException("Name '" + name.getGender() + "' not existing in planet '"
						+ getCharacterPlayer().getInfo().getPlanet() + "'.");
			}
		}
		return 1;
	}

	@Override
	protected void assignIfMandatory(Name element) throws InvalidXmlElementException {
		return;
	}

	@Override
	protected void assignMandatoryValues(Set<Name> mandatoryValues) throws InvalidXmlElementException {
		return;
	}
}
