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

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.file.modules.ModuleManager;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.Language;
import com.softwaremagico.tm.log.MachineXmlReaderLog;

import java.util.*;

public class CharacteristicsDefinitionFactory extends XmlFactory<CharacteristicDefinition> {
    private static final String TRANSLATOR_FILE = "characteristics.xml";

    private static final String ABBREVIATION = "abbreviature";
    private static final String TYPE = "type";

    private static int order = 0;

    private Map<String, Map<String, Map<CharacteristicType, List<CharacteristicDefinition>>>> characteristicsPerType;

    private static class CharacteristicsDefinitionFactoryInit {
        public static final CharacteristicsDefinitionFactory INSTANCE = new CharacteristicsDefinitionFactory();
    }

    public static CharacteristicsDefinitionFactory getInstance() {
        return CharacteristicsDefinitionFactoryInit.INSTANCE;
    }

    @Override
    public void refreshCache() {
        characteristicsPerType = null;
        super.refreshCache();
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
                    if (characteristicsPerType == null) {
                        characteristicsPerType = new HashMap<>();
                    }
                    characteristicsPerType.computeIfAbsent(language.getAbbreviature(), k -> new HashMap<>());
                    characteristicsPerType.get(language.getAbbreviature()).computeIfAbsent(moduleName, k -> new HashMap<>());
                    for (final CharacteristicDefinition characteristicsDefinition : characteristicsDefinitions) {
                        characteristicsPerType.get(language.getAbbreviature()).get(moduleName).computeIfAbsent(characteristicsDefinition.getType(),
                                k -> new ArrayList<>());

                        characteristicsPerType.get(language.getAbbreviature()).get(moduleName).
                                get(characteristicsDefinition.getType()).add(characteristicsDefinition);
                    }
                } catch (InvalidXmlElementException e) {
                    MachineXmlReaderLog.errorMessage(this.getClass().getName(), e);
                }
            }
        }
    }

    @Override
    public String getTranslatorFile() {
        return TRANSLATOR_FILE;
    }

    @Override
    protected CharacteristicDefinition createElement(ITranslator translator, String characteristicId, String name, String description,
                                                     String language, String moduleName)
            throws InvalidXmlElementException {
        CharacteristicDefinition characteristic;
        try {
            characteristic = new CharacteristicDefinition(characteristicId, name, description, order++, language, moduleName);
        } catch (Exception e) {
            throw new InvalidCharacteristicException("Invalid name in characteristic '" + characteristicId + "'.");
        }

        try {
            final String abbreviation = translator.getNodeValue(characteristicId, ABBREVIATION, language);
            characteristic.setAbbreviature(abbreviation);
        } catch (Exception e) {
            throw new InvalidCharacteristicException("Invalid abbreviation in characteristic '" + characteristicId + "'.");
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
        if (characteristicsPerType == null) {
            initialize();
        }
        return characteristicsPerType.get(language).get(moduleName);
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
