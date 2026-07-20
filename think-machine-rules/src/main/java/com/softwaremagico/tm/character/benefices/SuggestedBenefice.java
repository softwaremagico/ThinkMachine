package com.softwaremagico.tm.character.benefices;

/*-
 * #%L
 * Think Machine (Rules)
 * %%
 * Copyright (C) 2017 - 2020 Softwaremagico
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

public class SuggestedBenefice extends Element<SuggestedBenefice> {
    private final BeneficeDefinition beneficeDefinition;
    private final Integer value;

    public SuggestedBenefice(BeneficeDefinition beneficeDefinition, Integer value) {
        super(beneficeDefinition.getId(), beneficeDefinition.getName(), beneficeDefinition.getDescription(),
                beneficeDefinition.getLanguage(), beneficeDefinition.getModuleName());
        this.beneficeDefinition = beneficeDefinition;
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public BeneficeDefinition getBeneficeDefinition() {
        return beneficeDefinition;
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
