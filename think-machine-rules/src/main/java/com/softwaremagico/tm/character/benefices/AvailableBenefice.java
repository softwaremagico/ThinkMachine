package com.softwaremagico.tm.character.benefices;

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

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.character.races.Race;
import com.softwaremagico.tm.random.definition.RandomElementDefinition;

import java.util.Set;

public class AvailableBenefice extends Element<AvailableBenefice> {
    private BeneficeSpecialization specialization = null;
    private final BeneficeDefinition beneficeDefinition;
    private final int cost;
    private final BeneficeClassification beneficeClassification;

    /**
     * For creating empty elements.
     */
    public AvailableBenefice() {
        super();
        cost = 0;
        beneficeDefinition = null;
        beneficeClassification = null;
    }

    public AvailableBenefice(String id, String name, String description, String language, BeneficeDefinition beneficeDefinition,
                             BeneficeClassification beneficeClassification, int cost, RandomElementDefinition randomDefinition) {
        super(id, name, description, language, randomDefinition, beneficeDefinition.getModuleName());
        this.beneficeDefinition = beneficeDefinition;
        this.beneficeClassification = beneficeClassification;
        this.cost = cost;

    }

    public BeneficeDefinition getBeneficeDefinition() {
        return beneficeDefinition;
    }

    public int getCost() {
        if (getBeneficeClassification().equals(BeneficeClassification.AFFLICTION)) {
            return -cost;
        }
        return cost;
    }

    @Override
    public String toString() {
        return getName() + " (" + (beneficeDefinition != null && beneficeDefinition.getBeneficeClassification() == BeneficeClassification.AFFLICTION ? "+" : "")
                + cost + ")";
    }

    public BeneficeSpecialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(BeneficeSpecialization specialization) {
        this.specialization = specialization;
    }

    public BeneficeClassification getBeneficeClassification() {
        return beneficeClassification;
    }

    @Override
    public Set<Faction> getRestrictedToFactions() {
        return beneficeDefinition.getRestrictedToFactions();
    }

    @Override
    public FactionGroup getRestrictedToFactionGroup() {
        return beneficeDefinition.getRestrictedToFactionGroup();
    }

    @Override
    public Set<Race> getRestrictedToRaces() {
        return beneficeDefinition.getRestrictedToRaces();
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
