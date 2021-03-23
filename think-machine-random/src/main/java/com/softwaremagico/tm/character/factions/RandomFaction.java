package com.softwaremagico.tm.character.factions;

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
import com.softwaremagico.tm.random.RandomSelector;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.FactionPreferences;
import com.softwaremagico.tm.random.selectors.IRandomPreference;

import java.util.Collection;
import java.util.Set;

public class RandomFaction extends RandomSelector<Faction> {

    public RandomFaction(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences) throws InvalidXmlElementException {
        super(characterPlayer, preferences);
    }

    @Override
    public void assign() throws InvalidFactionException, InvalidRandomElementSelectedException {
        getCharacterPlayer().setFaction(selectElementByWeight());
    }

    @Override
    protected Collection<Faction> getAllElements() throws InvalidXmlElementException {
        return FactionsFactory.getInstance().getElements(getCharacterPlayer().getLanguage(), getCharacterPlayer().getModuleName());
    }

    @Override
    protected int getWeight(Faction faction) throws InvalidRandomElementSelectedException {
        // Humans only humans factions.
        if (!faction.getRestrictedToRaces().isEmpty() && getCharacterPlayer().getRace() != null &&
                !faction.getRestrictedToRaces().contains(getCharacterPlayer().getRace())) {
            throw new InvalidRandomElementSelectedException("Faction '" + faction + "' restricted for race '" + faction.getRestrictedToRaces()
                    + "'. Character is '" + getCharacterPlayer().getRace() + "'.");
        }
        // Specialization desired.
        final FactionPreferences selectedFactionGroup = FactionPreferences.getSelected(getPreferences());
        if (selectedFactionGroup != null) {
            if (faction.getFactionGroup() != null && faction.getFactionGroup().name().equalsIgnoreCase(selectedFactionGroup.name())) {
                return 1;
            }
            // Different faction than selected.
            throw new InvalidRandomElementSelectedException("Faction '" + faction + "' not in prefereces selection '" + selectedFactionGroup + "'.");
        }
        // No faction preference selected. Xeno factions has preferences by its own factions.
        if (getCharacterPlayer().getRace().isXeno() && faction.getRestrictedToRaces().size() == 1 &&
                faction.getRestrictedToRaces().contains(getCharacterPlayer().getRace())) {
            return 100;
        }
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
