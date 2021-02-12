package com.softwaremagico.tm.json.factories.cache;

/*-
 * #%L
 * Think Machine (Rules)
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

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.file.PathManager;
import com.softwaremagico.tm.file.modules.ModuleManager;
import com.softwaremagico.tm.json.factories.FactoryElements;
import com.softwaremagico.tm.json.factories.WeaponsFactoryElements;
import com.softwaremagico.tm.language.Language;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.log.ConfigurationLog;
import com.softwaremagico.tm.log.MachineLog;
import com.softwaremagico.tm.log.MachineModulesLog;
import com.softwaremagico.tm.log.MachineXmlReaderLog;
import org.reflections.Reflections;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public abstract class FactoryCacheLoader<E extends Element<E>, T extends XmlFactory<E>> {
    protected static final String GSON_TEMPORAL_FOLDER = "json";

    public static void main(String[] args) throws InvalidXmlElementException {
        disableLogs();
        final WeaponsFactoryCacheLoader weaponsFactoryCacheLoader = new WeaponsFactoryCacheLoader();
        for (final String moduleName : ModuleManager.getAvailableModules()) {
            weaponsFactoryCacheLoader.save(moduleName, WeaponFactory.getInstance().getTranslatorFile());
        }
    }

    private static void disableLogs() {
        Logger logger = (Logger) LoggerFactory.getLogger(MachineXmlReaderLog.class);
        logger.setLevel(Level.OFF);
        logger = (Logger) LoggerFactory.getLogger(ConfigurationLog.class);
        logger.setLevel(Level.OFF);
        logger = (Logger) LoggerFactory.getLogger(MachineModulesLog.class);
        logger.setLevel(Level.OFF);
        logger = (Logger) LoggerFactory.getLogger(Reflections.class);
        logger.setLevel(Level.OFF);
    }

    protected static String getJsonContent(String moduleName, String language, String file) {
        return readFile(getPath(moduleName, language, file));
    }

    protected static String getFileName(Class<?> elementFactory) {
        return elementFactory.getSimpleName() + ".json";
    }

    private static String getPath(String moduleName, String language, String file) {
        return PathManager.getModulePath(moduleName) + "/json/" + language + "/" + file;
    }

    private static String readFile(String filePath) {
        try {
            URL resource;
            if (WeaponsFactoryCacheLoader.class.getClassLoader().getResource(filePath) != null) {
                resource = WeaponsFactoryCacheLoader.class.getClassLoader().getResource(filePath);
            } else {
                // Is inside of a module.
                resource = URLClassLoader.getSystemResource(filePath);
            }
            MachineLog.debug(WeaponsFactoryCacheLoader.class.getName(), "Found json factory '" + filePath + "' at '" + resource + "'.");
            final StringBuilder resultStringBuilder = new StringBuilder();
            assert resource != null;
            try (BufferedReader read = new BufferedReader(new InputStreamReader(resource.openStream(), StandardCharsets.UTF_8.name()))) {
                String line;
                while ((line = read.readLine()) != null) {
                    resultStringBuilder.append(line).append("\n");
                }
            }
            return resultStringBuilder.toString();
        } catch (NullPointerException | IOException e) {
            MachineLog.errorMessage(WeaponsFactoryCacheLoader.class.getName(), e);
        }
        return null;
    }

    protected abstract GsonBuilder initGsonBuilder(final String language, final String moduleName);

    protected abstract FactoryElements<E> getFactoryElements(String moduleName, String language) throws InvalidXmlElementException;

    public FactoryElements<E> load(Class<?> factoryClass, String language, String moduleName) {
        final Gson gson = initGsonBuilder(language, moduleName).create();
        return gson.fromJson(getJsonContent(moduleName, language, getFileName(factoryClass)),
                (Type) factoryClass);
    }

    public void save(String moduleName, String xmlFile) throws InvalidXmlElementException {
        final List<Language> languages = LanguagePool.getTranslator(xmlFile, moduleName).getAvailableLanguages();
        for (final Language language : languages) {
            final FactoryElements<E> factoryElements = getFactoryElements(moduleName, language.getAbbreviature());
            final Gson gson = initGsonBuilder(language.getAbbreviature(), moduleName).create();
            final String jsonCode = gson.toJson(factoryElements);
            saveFile(jsonCode, WeaponsFactoryElements.class, moduleName, language.getAbbreviature());
        }
    }

    private void saveFile(String jsonCode, Class<?> factoryClass, String moduleName, String language) {
        //Stores it on target folder. Ant will move it later.
        final Path source = Paths.get(WeaponsFactoryCacheLoader.class.getResource("/").getPath());
        final Path gsonFolder = Paths.get(source.toAbsolutePath() + "/" + moduleName + "/" + GSON_TEMPORAL_FOLDER + "/" + language + "/");
        try {
            Files.createDirectories(gsonFolder);
            final Path gsonFile = gsonFolder.resolve(getFileName(factoryClass));
            try (BufferedWriter writer = Files.newBufferedWriter(gsonFile, StandardCharsets.UTF_8)) {
                writer.write(jsonCode);
            }
        } catch (IOException e) {
            MachineLog.errorMessage(WeaponsFactoryCacheLoader.class.getName(), e);
        }
    }
}
