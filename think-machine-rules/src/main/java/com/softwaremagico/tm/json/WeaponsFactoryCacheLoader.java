package com.softwaremagico.tm.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.equipment.DamageType;
import com.softwaremagico.tm.character.equipment.weapons.Accessory;
import com.softwaremagico.tm.character.equipment.weapons.Ammunition;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.races.Race;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.file.PathManager;
import com.softwaremagico.tm.file.modules.ModuleManager;
import com.softwaremagico.tm.json.factories.WeaponsFactoryElements;
import com.softwaremagico.tm.language.Language;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.log.MachineLog;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;

public class WeaponsFactoryCacheLoader {
    private final static String GSON_TEMPORAL_FOLDER = "json";

    public static void main(String[] args) throws InvalidXmlElementException {
        for (final String moduleName : ModuleManager.getAvailableModules()) {
            save(moduleName);
        }
    }

    public static int load(String language, String moduleName) {
        final Gson gson = new Gson();
        WeaponsFactoryElements weaponsFactoryElements = gson.fromJson(getJsonContent(moduleName, language, getFileName(WeaponsFactoryElements.class)), WeaponsFactoryElements.class);
        WeaponFactory.getInstance().setElements(Locale.getDefault().getLanguage(), moduleName, weaponsFactoryElements.getElements());
        return WeaponFactory.getInstance().getNumberOfElements(moduleName);
    }

    public static void save(String moduleName) throws InvalidXmlElementException {
        final List<Language> languages = LanguagePool.getTranslator(WeaponFactory.getInstance().getTranslatorFile(), moduleName).getAvailableLanguages();
        for (Language language : languages) {
            WeaponsFactoryElements weaponsFactoryElements = new WeaponsFactoryElements(language.getAbbreviature(), moduleName);
            final GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setPrettyPrinting();
            gsonBuilder.registerTypeAdapter(AvailableSkill.class, new AvailableSkillAdapter(language.getAbbreviature(), moduleName));
            gsonBuilder.registerTypeAdapter(CharacteristicDefinition.class, new CharacteristicDefinitionAdapter(language.getAbbreviature(), moduleName));
            gsonBuilder.registerTypeAdapter(Accessory.class, new AccessoryAdapter(language.getAbbreviature(), moduleName));
            gsonBuilder.registerTypeAdapter(DamageType.class, new DamageTypeAdapter(language.getAbbreviature(), moduleName));
            gsonBuilder.registerTypeAdapter(Ammunition.class, new DamageTypeAdapter(language.getAbbreviature(), moduleName));
            gsonBuilder.registerTypeAdapter(Race.class, new RaceAdapter(language.getAbbreviature(), moduleName));
            gsonBuilder.registerTypeAdapter(Faction.class, new FactionAdapter(language.getAbbreviature(), moduleName));
            final Gson gson = gsonBuilder.create();
            String jsonCode = gson.toJson(weaponsFactoryElements);

            //Stores it on target folder. Ant will move it later.
            Path source = Paths.get(WeaponsFactoryCacheLoader.class.getResource("/").getPath());
            Path gsonFolder = Paths.get(source.toAbsolutePath() + "/" + moduleName + "/" + GSON_TEMPORAL_FOLDER + "/" + language.getAbbreviature() + "/");
            try {
                Files.createDirectories(gsonFolder);
                Path gsonFile = gsonFolder.resolve(getFileName(WeaponsFactoryElements.class));
                try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(gsonFile.toFile())), true)) {
                    out.println(jsonCode);
                }
            } catch (IOException e) {
                MachineLog.errorMessage(WeaponsFactoryCacheLoader.class.getName(), e);
            }
        }

    }

    private static String getJsonContent(String moduleName, String language, String file) {
        return readFile(getPath(moduleName, language, file));
    }

    private static String getFileName(Class<?> elementFactory) {
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
            StringBuilder resultStringBuilder = new StringBuilder();
            assert resource != null;
            try (BufferedReader read = new BufferedReader(new InputStreamReader(resource.openStream()))) {
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
}
