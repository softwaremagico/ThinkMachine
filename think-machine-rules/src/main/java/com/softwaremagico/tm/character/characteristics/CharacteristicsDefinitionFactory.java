package com.softwaremagico.tm.character.characteristics;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.file.modules.ModuleManager;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.Language;
import com.softwaremagico.tm.log.MachineLog;

public class CharacteristicsDefinitionFactory extends XmlFactory<CharacteristicDefinition> {
	private static final String TRANSLATOR_FILE = "characteristics.xml";

	private static final String NAME = "name";
	private static final String ABBREVIATURE = "abbreviature";
	private static final String TYPE = "type";

	private Map<String, Map<String, Map<CharacteristicType, List<CharacteristicDefinition>>>> characteristics;

	private static class CharacteristicsDefinitionFactoryInit {
		public static final CharacteristicsDefinitionFactory INSTANCE = new CharacteristicsDefinitionFactory();
	}

	public static CharacteristicsDefinitionFactory getInstance() {
		return CharacteristicsDefinitionFactoryInit.INSTANCE;
	}

	@Override
	public void clearCache() {
		characteristics = null;
		super.clearCache();
	}

	@Override
	protected void initialize() {
		super.initialize();

		for (final String moduleName : ModuleManager.getAvailableModules()) {
			final List<Language> languages = getTranslator(moduleName).getAvailableLanguages();
			for (final Language language : languages) {
				final List<CharacteristicDefinition> characteristicsDefinitions;
				try {
					characteristicsDefinitions = getElements(language.getAbbreviature(), moduleName);
					if (characteristics == null) {
						characteristics = new HashMap<>();
					}
					if (characteristics.get(language.getAbbreviature()) == null) {
						characteristics.put(language.getAbbreviature(), new HashMap<String, Map<CharacteristicType, List<CharacteristicDefinition>>>());
					}
					if (characteristics.get(language.getAbbreviature()).get(moduleName) == null) {
						characteristics.get(language.getAbbreviature()).put(moduleName, new HashMap<CharacteristicType, List<CharacteristicDefinition>>());
					}
					for (final CharacteristicDefinition characteristicsDefinition : characteristicsDefinitions) {
						if (characteristics.get(language.getAbbreviature()).get(moduleName).get(characteristicsDefinition.getType()) == null) {
							characteristics.get(language.getAbbreviature()).get(moduleName).put(characteristicsDefinition.getType(),
									new ArrayList<CharacteristicDefinition>());
						}

						characteristics.get(language.getAbbreviature()).get(moduleName).get(characteristicsDefinition.getType()).add(characteristicsDefinition);
					}
				} catch (InvalidXmlElementException e) {
					MachineLog.errorMessage(this.getClass().getName(), e);
				}
			}
		}
	}

	@Override
	protected String getTranslatorFile() {
		return TRANSLATOR_FILE;
	}

	@Override
	protected CharacteristicDefinition createElement(ITranslator translator, String characteristicId, String language, String moduleName)
			throws InvalidXmlElementException {
		CharacteristicDefinition characteristic = null;
		try {
			final String name = translator.getNodeValue(characteristicId, NAME, language);
			characteristic = new CharacteristicDefinition(characteristicId, name, language, moduleName);
		} catch (Exception e) {
			throw new InvalidCharacteristicException("Invalid name in characteristic '" + characteristicId + "'.");
		}

		try {
			final String abbreviature = translator.getNodeValue(characteristicId, ABBREVIATURE, language);
			characteristic.setAbbreviature(abbreviature);
		} catch (Exception e) {
			throw new InvalidCharacteristicException("Invalid abbreviature in characteristic '" + characteristicId + "'.");
		}

		try {
			final String type = translator.getNodeValue(characteristicId, TYPE);
			characteristic.setType(CharacteristicType.getType(type));
		} catch (Exception e) {
			throw new InvalidCharacteristicException("Invalid type in characteristic '" + characteristicId + "'.");
		}

		return characteristic;
	}

	public Set<CharacteristicDefinition> getAll(String language, String moduleName) {
		final Set<CharacteristicDefinition> allCharacteristics = new HashSet<>();
		for (final List<CharacteristicDefinition> characteristicsByType : getCharacteristics(language, moduleName).values()) {
			allCharacteristics.addAll(characteristicsByType);
		}
		return allCharacteristics;
	}

	private Map<CharacteristicType, List<CharacteristicDefinition>> getCharacteristics(String language, String moduleName) {
		if (characteristics == null) {
			characteristics = new HashMap<>();
		}
		if (characteristics.get(language) == null) {
			characteristics.put(language, new HashMap<String, Map<CharacteristicType, List<CharacteristicDefinition>>>());
		}
		if (characteristics.get(language).get(moduleName) == null) {
			characteristics.get(language).put(moduleName, new HashMap<CharacteristicType, List<CharacteristicDefinition>>());
		}
		return characteristics.get(language).get(moduleName);
	}

	public List<CharacteristicDefinition> getAll(CharacteristicType type, String language, String moduleName) {
		return getCharacteristics(language, moduleName).get(type);
	}

	public CharacteristicDefinition get(CharacteristicName characteristicName, String language, String moduleName) {
		for (final CharacteristicType type : CharacteristicType.values()) {
			if (getCharacteristics(language, moduleName) != null && getCharacteristics(language, moduleName).get(type) != null) {
				for (final CharacteristicDefinition characteristic : getCharacteristics(language, moduleName).get(type)) {
					if (Objects.equals(characteristic.getId().toLowerCase(), characteristicName.getId().toLowerCase())) {
						return characteristic;
					}
				}
			}
		}
		return null;
	}
}
