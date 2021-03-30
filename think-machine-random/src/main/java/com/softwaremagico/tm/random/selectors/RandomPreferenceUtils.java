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

import com.softwaremagico.tm.log.MachineLog;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"unchecked", "rawtypes"})
public class RandomPreferenceUtils {
    private static HashMap<Class<? extends IRandomPreference>, Set<Class<? extends IRandomPreference>>> preferencesByGroup;


    public static IRandomPreference getSelectedPreference(String preferenceName) {
        final String preferenceClassName = AgePreferences.class.getPackage().getName() + "." + preferenceName.substring(0, preferenceName.indexOf('.'));
        final String preferenceValue = preferenceName.substring(preferenceName.indexOf('.') + 1);
        try {
            final Class enumPreference = Class.forName(preferenceClassName);
            if (enumPreference.isEnum()) {
                return (IRandomPreference) Enum.valueOf(enumPreference, preferenceValue);
            }
        } catch (ClassNotFoundException e) {
            MachineLog.errorMessage(RandomPreferenceUtils.class.getName(), e);
        }
        return null;
    }

    public static Set<Class<? extends IRandomPreference>> getByGroup(Class<? extends IRandomPreference> preferenceGroup) {
        if (preferencesByGroup == null || preferencesByGroup.get(preferenceGroup) != null) {
            return preferencesByGroup.get(preferenceGroup);
        }
        try {
            initPreferenceGroups();
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            MachineLog.errorMessage(RandomPreferenceUtils.class.getName(), e);
            return null;
        }
        return getByGroup(preferenceGroup);
    }

    public static HashMap<Class<? extends IRandomPreference>, Set<Class<? extends IRandomPreference>>> getPreferencesByGroup() {
        if (preferencesByGroup == null) {
            try {
                initPreferenceGroups();
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                MachineLog.errorMessage(RandomPreferenceUtils.class.getName(), e);
                return null;
            }
        }
        return preferencesByGroup;
    }

    private static void initPreferenceGroups() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        preferencesByGroup = new HashMap<>();
        // We use age preference to obtain the package path.
        final Reflections reflections = new Reflections(IRandomPreference.class.getPackage().getName());
        final Set<Class<? extends IRandomPreference>> availableGroups = reflections.getSubTypesOf(IRandomPreference.class);
        for (final Class<? extends IRandomPreference> randomPreference : availableGroups) {
            //Interface for grouping.
            if (randomPreference.isInterface()) {
                preferencesByGroup.put(randomPreference, new HashSet<>());
            }
        }
        for (final Class<? extends IRandomPreference> randomPreference : availableGroups) {
            //Interface for grouping.
            if (!randomPreference.isInterface()) {
                for (final Class<?> parentInterface : randomPreference.getInterfaces()) {
                    //Interface is a group
                    if (preferencesByGroup.keySet().contains(parentInterface)) {
                        preferencesByGroup.get(parentInterface).add(randomPreference);
                    }
                }
            }
        }
    }

}
