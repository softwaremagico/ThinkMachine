package com.softwaremagico.tm.json.factories;

import com.google.gson.Gson;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.file.PathManager;
import com.softwaremagico.tm.file.modules.ModuleManager;
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

public class FactoryCacheLoader {
    private final static String GSON_TEMPORAL_FOLDER = "json";

    public static void main(String[] args) throws InvalidXmlElementException {
        for (final String moduleName : ModuleManager.getAvailableModules()) {
            save(moduleName);
        }
    }

    public static void load(String language, String moduleName) {
        final Gson gson = new Gson();
        WeaponsFactoryElements weaponsFactoryElements = gson.fromJson(getJsonContent(moduleName, language, getFileName(WeaponsFactoryElements.class)), WeaponsFactoryElements.class);
        WeaponFactory.getInstance().setElements(Locale.getDefault().getLanguage(), PathManager.DEFAULT_MODULE_FOLDER, weaponsFactoryElements.getElements());
    }

    public static void save(String moduleName) throws InvalidXmlElementException {
        final Gson gson = new Gson();

        final List<Language> languages = LanguagePool.getTranslator(WeaponFactory.getInstance().getTranslatorFile(), moduleName).getAvailableLanguages();
        for (Language language : languages) {
            WeaponsFactoryElements weaponsFactoryElements = new WeaponsFactoryElements(language.getAbbreviature(), moduleName);
            String jsonCode = gson.toJson(weaponsFactoryElements);

            Path source = Paths.get(FactoryCacheLoader.class.getResource("/").getPath());
            Path gsonFolder = Paths.get(source.toAbsolutePath() + "/" + GSON_TEMPORAL_FOLDER + "/" + moduleName + "/");
            try {
                Files.createDirectories(gsonFolder);
                Path gsonFile = gsonFolder.resolve(getFileName(WeaponsFactoryElements.class));
                try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(gsonFile.toFile())), true)) {
                    out.println(jsonCode);
                }
            } catch (IOException e) {
                MachineLog.errorMessage(FactoryCacheLoader.class.getName(), e);
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
        return PathManager.getModulePath(moduleName) + "/" + language + "/" + file;
    }

    private static String readFile(String filePath) {
        try {
            URL resource;
            if (FactoryCacheLoader.class.getClassLoader().getResource(filePath) != null) {
                resource = FactoryCacheLoader.class.getClassLoader().getResource(filePath);
            } else {
                // Is inside of a module.
                resource = URLClassLoader.getSystemResource(filePath);
            }
            MachineLog.debug(FactoryCacheLoader.class.getName(), "Found json factory '" + filePath + "' at '" + resource + "'.");
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
            MachineLog.errorMessage(FactoryCacheLoader.class.getName(), e);
        }
        return null;
    }
}
