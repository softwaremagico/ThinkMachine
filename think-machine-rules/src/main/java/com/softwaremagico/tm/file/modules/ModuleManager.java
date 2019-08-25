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

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import com.softwaremagico.tm.file.InvalidJarFileException;
import com.softwaremagico.tm.file.PathManager;
import com.softwaremagico.tm.file.configurator.MachineConfigurationReader;
import com.softwaremagico.tm.file.watcher.FileWatcher.FileModifiedListener;
import com.softwaremagico.tm.log.MachineModulesLog;

public class ModuleManager {
	private static Set<String> availableModules;
	private static String currentModuleFolder = null;

	public static void resetModules() {
		availableModules = null;
	}

	public static synchronized Set<String> getAvailableModules() {
		if (availableModules == null) {
			// Force to load modules in current module's folder and enable the file watchers.
			if (currentModuleFolder == null) {
				setModulesFolder(MachineConfigurationReader.getInstance().getModulesPath());
			}

			availableModules = listModulesInResources();
			MachineModulesLog.debug(ModuleManager.class.getName(), "Found modules '" + availableModules + "'.");
		}
		return availableModules;
	}

	/**
	 * Search for any available module on the application or in the modules folder. This code has not optimal
	 * performance but is only being executed one time.
	 * 
	 * @return A list of modules.
	 */
	private static Set<String> listModulesInResources() {
		final ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.addUrls(ClasspathHelper.forPackage(PathManager.MODULES_FOLDER, ClassLoader.getSystemClassLoader(),
				ClasspathHelper.contextClassLoader(), ClasspathHelper.staticClassLoader()));
		builder.addScanners(new ResourcesScanner());
		final Reflections reflections = new Reflections(builder);
		final Set<String> resources = reflections.getResources(Pattern.compile(".*\\.xml"));

		final Set<String> modules = new HashSet<>();
		for (final String resource : resources) {
			try {
				final String[] path = resource.split("/");
				if (path.length > 2) {
					if (path[0].equals(PathManager.MODULES_FOLDER)) {
						modules.add(path[1]);
					}
				}
			} catch (ArrayIndexOutOfBoundsException e) {

			}
		}
		if (modules.isEmpty()) {
			MachineModulesLog.severe(ModuleManager.class.getName(), "No modules found!");
		} else {
			MachineModulesLog.info(ModuleManager.class.getName(), "Found modules '" + modules + "'.");
		}

		return modules;
	}

	/**
	 * Adds the supplied Java Archive library to java.class.path. This is benign if the library is already loaded.
	 * 
	 * @param jar
	 *            The jar file to include in the application.
	 */
	public static synchronized void loadJar(File jar) throws InvalidJarFileException {
		try {
			// We are using reflection here to circumvent encapsulation; addURL is not public
			final URLClassLoader loader = (URLClassLoader) ClassLoader.getSystemClassLoader();
			final URL url = jar.toURI().toURL();
			// Disallow if already loaded
			for (final URL it : Arrays.asList(loader.getURLs())) {
				if (Objects.equals(it.toString(), (url.toString()))) {
					MachineModulesLog.info(ModuleManager.class.getName(), "JAR file '" + jar.toURI().getPath()
							+ "' already loaded.");
					return;
				}
			}

			final Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
			method.setAccessible(true); // promote the method to public access.
			method.invoke(loader, new Object[] { url });
			MachineModulesLog.info(ModuleManager.class.getName(), "Loaded JAR file '" + jar.toURI().getPath() + "'.");
		} catch (final NoSuchMethodException | IllegalAccessException | MalformedURLException
				| InvocationTargetException e) {
			throw new InvalidJarFileException("Unable to load JAR file '" + jar.toURI().getPath() + "'.", e);
		}
	}

	public static void setModulesFolder(final String modulesFolderpath) {
		MachineConfigurationReader.getInstance().setModulesPath(modulesFolderpath, new FileModifiedListener() {

			@Override
			public void changeDetected(Path pathToFile) {
				if (pathToFile.toString().endsWith(".jar")) {
					MachineModulesLog.info(ModuleManager.class.getName(), "New module '" + pathToFile + "' detected!");
					loadModules(modulesFolderpath);
				}
			}
		});
		loadModules(modulesFolderpath);
		currentModuleFolder = modulesFolderpath;
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
			MachineModulesLog.errorMessage(ModuleManager.class.getName(), "Jar cannot be loaded at '" + modulesFolder
					+ "'.", e);
		}
	}

	public static File[] getAllJarFiles(String folderPath) {
		final File dir = new File(folderPath);
		final File[] files = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".jar");
			}
		});
		return files;
	}
}
