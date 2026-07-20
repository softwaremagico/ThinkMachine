package com.softwaremagico.tm.file.configurator;

/*-
 * #%L
 * Think Machine (Rules)
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

import com.softwaremagico.tm.file.configurator.exceptions.PropertyNotFoundException;
import com.softwaremagico.tm.file.configurator.exceptions.PropertyNotStoredException;
import com.softwaremagico.tm.log.ConfigurationLog;

import java.io.File;
import java.util.*;

public abstract class ConfigurationReader {
	private static final String VALUES_SEPARATOR_REGEX = " *, *";
	private static final char PREFIX_SEPARATOR_CHAR = '.';
	private final Map<Class<?>, IValueConverter<?>> converter;
	private final Map<String, String> propertiesDefault;
	private final Map<String, String> propertiesFinalValue;
	private final List<IPropertiesSource> propertiesSources;
	private final Set<PropertyChangedListener> propertyChangedListeners;
	private final Map<IPropertiesSource, Map<String, String>> propertiesBySourceValues;

	public ConfigurationReader() {
		converter = new HashMap<>();
		propertiesDefault = new HashMap<>();
		propertiesFinalValue = new HashMap<>();
		propertiesSources = new ArrayList<>();
		propertyChangedListeners = new HashSet<>();
		propertiesBySourceValues = new HashMap<>();

		addConverter(Boolean.class, new BooleanValueConverter());
		addConverter(Integer.class, new IntegerValueConverter());
		addConverter(Double.class, new DoubleValueConverter());

		// Log if any property has changed the value.
		addPropertyChangedListener(new PropertyChangedListener() {

			@Override
			public void propertyChanged(String propertyId, String oldValue, String newValue) {
				ConfigurationLog.info(this.getClass().getName(),
						"Property '" + propertyId + "' has changed value from '" + oldValue + "' to '" + newValue + "'.");
			}
		});
	}

	public interface PropertyChangedListener {
		void propertyChanged(String propertyId, String oldValue, String newValue);
	}

	public <T> void addConverter(Class<T> clazz, IValueConverter<T> valueConverter) {
		converter.put(clazz, valueConverter);
	}

	@SuppressWarnings("unchecked")
	public <T> IValueConverter<T> getConverter(Class<T> clazz) {
		return (IValueConverter<T>) converter.get(clazz);
	}

	public void addPropertiesSource(IPropertiesSource propertiesSource) {
		propertiesSources.add(propertiesSource);
	}

	public void removePropertiesSource(IPropertiesSource propertiesSource) {
		if (propertiesSource != null) {
			propertiesSources.remove(propertiesSource);
		}
	}

	/**
	 * Restarts all properties to their default values and then reads all the
	 * configuration files again.
	 */
	public void readConfigurations() {
		propertiesFinalValue.clear();
		propertiesFinalValue.putAll(propertiesDefault);

		for (final IPropertiesSource propertiesSource : propertiesSources) {
			final Properties propertyFile = propertiesSource.loadFile();
			if (propertyFile != null) {
				readAllProperties(propertyFile, propertiesSource);
			}
		}
	}

	public abstract void storeProperties() throws PropertyNotStoredException;

	public abstract File getUserProperties();

	/**
	 * Reads all properties configured in this configuration reader from
	 * propertyFile. If they doesn't exist, then the current value is mantained as
	 * default value.
	 * 
	 * @param propertyFile
	 */
	private void readAllProperties(Properties propertyFile, IPropertiesSource propertiesSource) {
		for (final String propertyId : new HashSet<String>(propertiesFinalValue.keySet())) {
			final String value = propertyFile.getProperty(propertyId, propertiesFinalValue.get(propertyId));
			// Notify property change
			propertiesBySourceValues.computeIfAbsent(propertiesSource, k -> new HashMap<String, String>());

			if (propertiesBySourceValues.get(propertiesSource).get(propertyId) != null
					&& propertiesBySourceValues.get(propertiesSource).get(propertyId).length() > 0
					&& !propertiesBySourceValues.get(propertiesSource).get(propertyId).equals(value)) {
				// Launch listeners.
				for (final PropertyChangedListener propertyChangedListener : propertyChangedListeners) {
					propertyChangedListener.propertyChanged(propertyId, propertiesFinalValue.get(propertyId), value);
				}
				ConfigurationLog.info(this.getClass().getName(), "Property '" + propertyId + "' updated as '" + value + "'.");
			}
			// Store value.
			propertiesBySourceValues.get(propertiesSource).put(propertyId, value);
			propertiesFinalValue.put(propertyId, value);
			ConfigurationLog.debug(this.getClass().getName(), "Property '" + propertyId + "' set as value '" + value + "'.");
		}
	}

	/**
	 * Adds a property
	 * 
	 * @param propertyName
	 * @param defaultValue
	 */
	protected <T> void setProperty(String propertyName, T defaultValue) {
		if (defaultValue == null) {
			propertiesDefault.put(propertyName, null);
			propertiesFinalValue.put(propertyName, null);
		} else if (defaultValue instanceof String) {
			propertiesDefault.put(propertyName, ((String) defaultValue).trim());
			propertiesFinalValue.put(propertyName, ((String) defaultValue).trim());
		} else {
			propertiesDefault.put(propertyName, getConverter(defaultValue.getClass()).convertToString(defaultValue));
			propertiesFinalValue.put(propertyName, getConverter(defaultValue.getClass()).convertToString(defaultValue));
		}
	}

	/**
	 * Read all properties and set an empty string as default value.
	 */
	public void initializeAllProperties() {
		for (final IPropertiesSource propertiesSource : propertiesSources) {
			final Properties propertyFile = propertiesSource.loadFile();
			if (propertyFile != null) {
				final Enumeration<?> enumerator = propertyFile.propertyNames();
				while (enumerator.hasMoreElements()) {
					setProperty((String) enumerator.nextElement(), "");
				}
			}
		}
	}

	/**
	 * Gets all defined prefix for the properties.
	 * 
	 * @return
	 */
	public Set<String> getAllPropertiesPrefixes() {
		final Set<String> prefixes = new HashSet<>();
		for (final IPropertiesSource propertiesSource : propertiesSources) {
			final Properties propertyFile = propertiesSource.loadFile();
			if (propertyFile != null) {
				final Enumeration<?> enumerator = propertyFile.propertyNames();
				while (enumerator.hasMoreElements()) {
					final String element = (String) enumerator.nextElement();
					if (element.contains(String.valueOf(PREFIX_SEPARATOR_CHAR))) {
						prefixes.add(element.substring(0, element.indexOf(PREFIX_SEPARATOR_CHAR)));
					}
				}
			}
		}
		return prefixes;
	}

	public String getProperty(String propertyName) throws PropertyNotFoundException {
		if (propertiesFinalValue.containsKey(propertyName)) {
			if (propertiesFinalValue.get(propertyName) != null) {
				return propertiesFinalValue.get(propertyName).trim();
			} else {
				return null;
			}
		} else {
			throw new PropertyNotFoundException("Property '" + propertyName + "' not defined in the configuration reader");
		}
	}

	public <T> T getProperty(String propertyName, Class<? extends T> type) throws PropertyNotFoundException {
		final String stringValue = getProperty(propertyName);
		if (stringValue != null) {
			return getConverter(type).convertFromString(stringValue);
		} else {
			return null;
		}
	}

	public Map<String, String> getAllProperties() {
		return propertiesFinalValue;
	}

	public List<IPropertiesSource> getPropertiesSources() {
		return propertiesSources;
	}

	public void addPropertyChangedListener(PropertyChangedListener propertyChangedListener) {
		propertyChangedListeners.add(propertyChangedListener);
	}

	/**
	 * Stops file watchers in from all configuration files.
	 */
	public void stopFileWatchers() {
		for (final IPropertiesSource sources : propertiesSources) {
			if (sources instanceof PropertiesSourceFile) {
				((PropertiesSourceFile) sources).stopFileWatcher();
			}
		}
	}

	protected String[] getCommaSeparatedValues(String propertyName) throws PropertyNotFoundException {
		String value = getProperty(propertyName);
		// Remove useless spaces around commas.
		value = value.replaceAll(VALUES_SEPARATOR_REGEX, ",");
		// Split by commas.
		return value.split(",");

	}

	public abstract String getUserPropertiesPath();

}
