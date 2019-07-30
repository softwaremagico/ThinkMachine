package com.softwaremagico.tm.random.character.factions;

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
import java.util.Set;

import com.softwaremagico.tm.random.RandomSelector;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.FactionPreferences;
import com.softwaremagico.tm.random.selectors.IRandomPreference;
import com.softwaremagico.tm.rules.InvalidXmlElementException;
import com.softwaremagico.tm.rules.character.CharacterPlayer;
import com.softwaremagico.tm.rules.character.factions.Faction;
import com.softwaremagico.tm.rules.character.factions.FactionsFactory;
import com.softwaremagico.tm.rules.character.races.InvalidRaceException;

public class RandomFaction extends RandomSelector<Faction> {

	public RandomFaction(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences)
			throws InvalidXmlElementException {
		super(characterPlayer, preferences);
	}

	@Override
	public void assign() throws InvalidRaceException, InvalidRandomElementSelectedException {
		getCharacterPlayer().setFaction(selectElementByWeight());
	}

	@Override
	protected Collection<Faction> getAllElements() throws InvalidXmlElementException {
		return FactionsFactory.getInstance().getElements(getCharacterPlayer().getLanguage());
	}

	@Override
	protected int getWeight(Faction faction) throws InvalidRandomElementSelectedException {
		// Specialization desired.
		final FactionPreferences selectedFactionGroup = FactionPreferences.getSelected(getPreferences());
		if (selectedFactionGroup != null) {
			if (faction.getFactionGroup() != null
					&& faction.getFactionGroup().name().equalsIgnoreCase(selectedFactionGroup.name())) {
				return 1;
			}
			// Different faction than selected.
			throw new InvalidRandomElementSelectedException("Faction '" + faction + "' not in prefereces selection '"
					+ selectedFactionGroup + "'.");
		}
		// Humans only humans factions.
		if (faction.getRestrictedRace() != null && !faction.getRestrictedRace().equals(getCharacterPlayer().getRace())) {
			throw new InvalidRandomElementSelectedException("Faction '" + faction + "' restricted for race '"
					+ faction.getRestrictedRace() + "'. Character is '" + getCharacterPlayer().getRace() + "'.");
		}
		// No faction preference selected. All factions has the same
		// probability.
		return 1;
	}

	@Override
	protected void assignIfMandatory(Faction element) throws InvalidXmlElementException {
		return;
	}

	@Override
	protected void assignMandatoryValues(Set<Faction> mandatoryValues) throws InvalidXmlElementException {
		return;
	}
}
