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
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.Language;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.log.MachineLog;

public class CharacteristicsDefinitionFactory extends XmlFactory<CharacteristicDefinition> {
	private final static ITranslator translatorCharacteristics = LanguagePool.getTranslator("characteristics.xml");

	private final static String NAME = "name";
	private final static String ABBREVIATURE = "abbreviature";
	private final static String TYPE = "type";

	private Map<String, Map<CharacteristicType, List<CharacteristicDefinition>>> characteristics;

	private static CharacteristicsDefinitionFactory instance;

	private static void createInstance() {
		if (instance == null) {
			synchronized (CharacteristicsDefinitionFactory.class) {
				if (instance == null) {
					instance = new CharacteristicsDefinitionFactory();
				}
			}
		}
	}

	@Override
	public void clearCache() {
		characteristics = null;
		super.clearCache();
	}

	protected void initialize() {
		super.initialize();

		List<Language> languages = getTranslator().getAvailableLanguages();
		for (Language language : languages) {
			List<CharacteristicDefinition> characteristicsDefinitions;
			try {
				characteristicsDefinitions = getElements(language.getAbbreviature());
				if (characteristics == null) {
					characteristics = new HashMap<>();
				}
				if (characteristics.get(language.getAbbreviature()) == null) {
					characteristics.put(language.getAbbreviature(), new HashMap<CharacteristicType, List<CharacteristicDefinition>>());
				}
				for (CharacteristicDefinition characteristicsDefinition : characteristicsDefinitions) {
					if (characteristics.get(language.getAbbreviature()).get(characteristicsDefinition.getType()) == null) {
						characteristics.get(language.getAbbreviature()).put(characteristicsDefinition.getType(), new ArrayList<CharacteristicDefinition>());
					}

					characteristics.get(language.getAbbreviature()).get(characteristicsDefinition.getType()).add(characteristicsDefinition);
				}
			} catch (InvalidXmlElementException e) {
				MachineLog.errorMessage(this.getClass().getName(), e);
			}
		}
	}

	public static CharacteristicsDefinitionFactory getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}

	@Override
	protected ITranslator getTranslator() {
		return translatorCharacteristics;
	}

	@Override
	protected CharacteristicDefinition createElement(ITranslator translator, String characteristicId, String language) throws InvalidXmlElementException {
		CharacteristicDefinition characteristic = null;
		try {
			String name = translator.getNodeValue(characteristicId, NAME, language);
			characteristic = new CharacteristicDefinition(characteristicId, name);
		} catch (Exception e) {
			throw new InvalidCharacteristicException("Invalid name in characteristic '" + characteristicId + "'.");
		}

		try {
			String abbreviature = translator.getNodeValue(characteristicId, ABBREVIATURE, language);
			characteristic.setAbbreviature(abbreviature);
		} catch (Exception e) {
			throw new InvalidCharacteristicException("Invalid abbreviature in characteristic '" + characteristicId + "'.");
		}

		try {
			String type = translator.getNodeValue(characteristicId, TYPE);
			characteristic.setType(CharacteristicType.getType(type));
		} catch (Exception e) {
			throw new InvalidCharacteristicException("Invalid type in characteristic '" + characteristicId + "'.");
		}

		return characteristic;
	}

	public Set<CharacteristicDefinition> getAll(String language) {
		Set<CharacteristicDefinition> allCharacteristics = new HashSet<>();
		for (List<CharacteristicDefinition> characteristicsByType : characteristics.get(language).values()) {
			allCharacteristics.addAll(characteristicsByType);
		}
		return allCharacteristics;
	}

	public List<CharacteristicDefinition> getAll(CharacteristicType type, String language) {
		return characteristics.get(language).get(type);
	}

	public CharacteristicDefinition get(CharacteristicName characteristicName, String language) {
		for (CharacteristicType type : CharacteristicType.values()) {
			for (CharacteristicDefinition characteristic : characteristics.get(language).get(type)) {
				if (Objects.equals(characteristic.getId().toLowerCase(), characteristicName.getId().toLowerCase())) {
					return characteristic;
				}
			}
		}
		return null;
	}
}
