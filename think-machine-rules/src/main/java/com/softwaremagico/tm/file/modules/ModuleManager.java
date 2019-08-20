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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;

import com.softwaremagico.tm.file.FileManager;
import com.softwaremagico.tm.file.InvalidJarFileException;
import com.softwaremagico.tm.file.Path;
import com.softwaremagico.tm.log.MachineLog;

public class ModuleManager {
	private static Set<String> availableModules;

	public static void resetModules() {
		availableModules = null;
	}

	public static Set<String> getAvailableModules() {
		if (availableModules == null) {
			availableModules = listModulesInResources();
			MachineLog.debug(FileManager.class.getName(), "Found modules '" + availableModules + "'.");
		}
		return availableModules;
	}

	private static Set<String> listModulesInResources() {
		final Set<String> resources = new Reflections(Path.MODULES_FOLDER, new ResourcesScanner()).getResources(Pattern
				.compile(".*\\.xml"));

		final Set<String> modules = new HashSet<>();
		for (final String resource : resources) {
			try {
				final String[] path = resource.split("/");
				if (path.length > 2) {
					modules.add(path[1]);
				}
			} catch (ArrayIndexOutOfBoundsException e) {

			}
		}
		if (modules.isEmpty()) {
			MachineLog.severe(FileManager.class.getName(), "No modules found!");
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
				if (it.equals(url)) {
					return;
				}
			}
			final Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
			method.setAccessible(true); // promote the method to public access.FS
			method.invoke(loader, new Object[] { url });
		} catch (final NoSuchMethodException | IllegalAccessException | MalformedURLException
				| InvocationTargetException e) {
			throw new InvalidJarFileException("Unable to load JAR file '" + jar.toURI().getPath() + "'.", e);
		}
	}
}
