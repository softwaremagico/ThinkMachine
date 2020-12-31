package com.softwaremagico.tm.random.party;

/*-
 * #%L
 * Think Machine (Random Generator)
 * %%
 * Copyright (C) 2017 - 2019 Softwaremagico
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

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.random.profiles.RandomProfile;
import com.softwaremagico.tm.random.selectors.IRandomPreference;

public class RandomPartyMember extends Element<RandomPartyMember> {
    private final RandomProfile randomProfile;
    private final Integer minNumber;
    private final Integer maxNumber;
    private final Integer weight;
    private final Set<IRandomPreference> randomPreferences;

    public RandomPartyMember(String id, String name, String language, String moduleName, RandomProfile randomProfile, Integer minNumber,
                             Integer maxNumber, Integer weight, Set<IRandomPreference> randomPreferences) {
        super(id, name, null, language, moduleName);
        this.randomProfile = randomProfile;
        this.minNumber = minNumber;
        this.maxNumber = maxNumber;
        this.weight = weight;
        this.randomPreferences = randomPreferences;
    }

    public RandomProfile getRandomProfile() {
        return randomProfile;
    }

    public Integer getMinNumber() {
        return minNumber;
    }

    public Integer getMaxNumber() {
        return maxNumber;
    }

    public Integer getWeight() {
        return weight;
    }

    public Set<IRandomPreference> getRandomPreferences() {
        return randomPreferences;
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
