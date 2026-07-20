package com.softwaremagico.tm.character.planets;

/*-
 * #%L
 * Think Machine (Core)
 * %%
 * Copyright (C) 2017 Softwaremagico
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

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.Name;
import com.softwaremagico.tm.character.Surname;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionsFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Planet extends Element<Planet> {
    private final Set<Faction> factions;
    private final Set<Faction> humanFactions;

    public Planet(String id, String name, String description, String language, String moduleName, Set<Faction> factions) {
        super(id, name, description, language, moduleName);

        // Get only human factions from the planet. Ignore Xeno factions.
        this.factions = factions;
        this.humanFactions = factions.stream().filter(Faction::isOnlyForHuman).
                collect(Collectors.toSet());
    }

    public Set<Faction> getFactions() {
        return factions;
    }

    public Set<Faction> getHumanFactions() {
        return humanFactions;
    }

    public Set<Name> getNames() {
        final Set<Name> names = new HashSet<>();
        for (final Faction faction : getHumanFactions()) {
            names.addAll(FactionsFactory.getInstance().getAllNames(faction));
        }
        return names;
    }

    public Set<Surname> getSurnames() {
        final Set<Surname> surnames = new HashSet<>();
        for (final Faction faction : getHumanFactions()) {
            surnames.addAll(FactionsFactory.getInstance().getAllSurnames(faction));
        }
        return surnames;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
