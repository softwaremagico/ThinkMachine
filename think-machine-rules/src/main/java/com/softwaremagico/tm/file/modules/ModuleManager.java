package com.softwaremagico.tm.file.modules;

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

import com.softwaremagico.tm.file.InvalidJarFileException;
import com.softwaremagico.tm.file.PathManager;
import com.softwaremagico.tm.file.configurator.MachineConfigurationReader;
import com.softwaremagico.tm.log.MachineModulesLog;
import org.reflections.Reflections;
import org.reflections.ReflectionsException;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

public class ModuleManager {
    public static final String DEFAULT_MODULE = "Fading Suns Revised Edition";
    private static Set<String> availableModules;
    private static String currentModuleFolder = null;

    public static void resetModules() {
        availableModules = null;
    }

    public static synchronized Set<String> getAvailableModules() {
        if (availableModules == null) {
            // Force to load modules in current module's folder and enable the file
            // watchers.
            if (currentModuleFolder == null) {
                setModulesFolder(MachineConfigurationReader.getInstance().getModulesPath());
            }

            availableModules = listModulesInResources();
            MachineModulesLog.debug(ModuleManager.class.getName(), "Found modules '{}'.", availableModules);
        }
        return availableModules;
    }

    public static synchronized void addAvailableModule(String moduleName) {
        if (availableModules == null) {
            availableModules = new HashSet<>();
        }
        availableModules.add(moduleName);
    }

    /**
     * Search for any available module on the application or in the modules folder.
     * This code has not optimal performance but is only being executed one time.
     *
     * @return A list of modules.
     */
    private static Set<String> listModulesInResources() {
        final ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.addUrls(ClasspathHelper.forPackage(PathManager.MODULES_FOLDER, ClassLoader.getSystemClassLoader(), ClasspathHelper.contextClassLoader(),
                ClasspathHelper.staticClassLoader()));
        builder.addScanners(new ResourcesScanner());
        final Set<String> modules = new HashSet<>();

        try {
            final Reflections reflections = new Reflections(builder);
            final Set<String> resources = reflections.getResources(Pattern.compile(".*\\.xml"));

            for (final String resource : resources) {
                try {
                    final String[] path = resource.split("/");
                    if (path.length > 2) {
                        if (path[0].equals(PathManager.MODULES_FOLDER)) {
                            modules.add(path[1]);
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException ignored) {

                }
            }
            if (modules.isEmpty()) {
                MachineModulesLog.severe(ModuleManager.class.getName(), "No modules found! Adding default one.");
                modules.add(DEFAULT_MODULE);
            } else {
                MachineModulesLog.info(ModuleManager.class.getName(), "Found modules '{}'.", modules);
            }
        } catch (ReflectionsException ignored) {

        }

        return modules;
    }

    /**
     * Adds the supplied Java Archive library to java.class.path. This is benign if
     * the library is already loaded.
     *
     * @param jar The jar file to include in the application.
     */
    public static synchronized void loadJar(File jar) throws InvalidJarFileException {
        try {
            // We are using reflection here to circumvent encapsulation; addURL is not
            // public
            final URLClassLoader loader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            final URL url = jar.toURI().toURL();
            // Disallow if already loaded
            for (final URL it : loader.getURLs()) {
                if (Objects.equals(it.toString(), (url.toString()))) {
                    MachineModulesLog.info(ModuleManager.class.getName(), "JAR file '{}' already loaded.", jar.toURI().getPath());
                    return;
                }
            }

            final Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true); // promote the method to public access.
            method.invoke(loader, url);
            MachineModulesLog.info(ModuleManager.class.getName(), "Loaded JAR file '{}'.", jar.toURI().getPath());
        } catch (final NoSuchMethodException | IllegalAccessException | MalformedURLException | InvocationTargetException e) {
            throw new InvalidJarFileException("Unable to load JAR file '" + jar.toURI().getPath() + "'.", e);
        }
    }

    public static void setModulesFolder(final String modulesFolderPath) {
        MachineConfigurationReader.getInstance().setModulesPath(modulesFolderPath, pathToFile -> {
            if (pathToFile.toString().endsWith(".jar")) {
                MachineModulesLog.info(ModuleManager.class.getName(), "New module '{}' detected!", pathToFile);
                loadModules(modulesFolderPath);
            }
        });
        loadModules(modulesFolderPath);
        currentModuleFolder = modulesFolderPath;
    }

    private static void loadModules(String modulesFolder) {
        resetModules();
        try {
            for (final File module : getAllJarFiles(modulesFolder)) {
                try {
                    loadJar(module);
                } catch (InvalidJarFileException e) {
                    MachineModulesLog.errorMessage(ModuleManager.class.getName(), e);
                }
            }
        } catch (NullPointerException e) {
            MachineModulesLog.warning(ModuleManager.class.getName(), "Jar cannot be loaded at '{}'.", modulesFolder);
        }
    }

    public static File[] getAllJarFiles(String folderPath) {
        final File dir = new File(folderPath);
        return dir.listFiles((dir1, name) -> name.endsWith(".jar"));
    }

    public static class MyClassloader extends URLClassLoader {

        public MyClassloader(URL[] urls, ClassLoader parent) {
            super(urls, parent);
        }

        public void addURL(URL url) {
            super.addURL(url);
        }
    }
}
