package com.softwaremagico.tm.cache;

/*-
 * #%L
 * Think Machine (Random Generator)
 * %%
 * Copyright (C) 2017 - 2021 Softwaremagico
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

import com.softwaremagico.tm.random.selectors.IRandomPreference;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class RandomPreferenceClassSearcher {
    private static final Set<Class<? extends IRandomPreference>> classes = new HashSet<>();

    /**
     * If reflections fails, as happen in Android, add manually the classes.
     *
     * @param newClasses the classes to add.
     */
    public static void addClasses(Set<Class<? extends IRandomPreference>> newClasses) {
        classes.addAll(newClasses);
    }

    public static Set<Class<? extends IRandomPreference>> getClasses() {
        return classes.stream().filter(Objects::nonNull).collect(Collectors.toSet());
    }
}
