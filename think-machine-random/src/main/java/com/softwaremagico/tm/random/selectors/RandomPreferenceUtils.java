package com.softwaremagico.tm.random.selectors;

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

import java.util.Objects;
import java.util.Set;

import org.reflections.Reflections;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class RandomPreferenceUtils {

	private static Set<Class<? extends Enum>> availablePreferences;

	private static Set<Class<? extends Enum>> getAvailablePreferences() {
		if (availablePreferences == null) {
			// We use age preference to obtain the package path.
			final Reflections reflections = new Reflections(AgePreferences.class.getPackage().getName());
			availablePreferences = reflections.getSubTypesOf(Enum.class);
		}
		return availablePreferences;
	}

	public static IRandomPreference getSelectedPreference(String preferenceName) {
		for (final Class<? extends Enum> classPreference : getAvailablePreferences()) {
			if (Objects.equals(classPreference.getSimpleName(),
					preferenceName.substring(0, preferenceName.indexOf('.')))) {
				return (IRandomPreference) Enum.valueOf(classPreference,
						preferenceName.substring(preferenceName.indexOf('.') + 1));
			}
		}
		return null;
	}
}
