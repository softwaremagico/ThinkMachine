package com.softwaremagico.tm.random.selectors;

import java.util.Objects;
import java.util.Set;

import org.reflections.Reflections;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class RandomPreferenceUtils {

	private static Set<Class<? extends Enum>> availablePreferences;

	private static Set<Class<? extends Enum>> getAvailablePreferences() {
		if (availablePreferences == null) {
			// We use age preference to obtain the package path.
			Reflections reflections = new Reflections(AgePreferences.class.getPackage().getName());
			availablePreferences = reflections.getSubTypesOf(Enum.class);
		}
		return availablePreferences;
	}

	public static IRandomPreference getSelectedPreference(String preferenceName) {
		for (Class<? extends Enum> classPreference : getAvailablePreferences()) {
			if (Objects.equals(classPreference.getSimpleName(),
					preferenceName.substring(0, preferenceName.indexOf('.')))) {
				return (IRandomPreference) Enum.valueOf(classPreference,
						preferenceName.substring(preferenceName.indexOf('.') + 1));
			}
		}
		return null;
	}
}
