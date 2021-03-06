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

import java.util.HashSet;
import java.util.Set;

public class BeneficeSpecialization extends Element<BeneficeSpecialization> {
    private Integer cost = null;
    private final Set<String> incompatibleWith = new HashSet<>();
    private BeneficeClassification classification = BeneficeClassification.BENEFICE;

    public BeneficeSpecialization(String id, String name, String description, String language, String moduleName, Set<String> incompatibleWith) {
        super(id, name, description, language, moduleName);
        this.incompatibleWith.addAll(incompatibleWith);
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public BeneficeClassification getClassification() {
        return classification;
    }

    public void setClassification(BeneficeClassification classification) {
        if (classification != null) {
            this.classification = classification;
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public Set<String> getIncompatibleWith() {
        return incompatibleWith;
    }
}
