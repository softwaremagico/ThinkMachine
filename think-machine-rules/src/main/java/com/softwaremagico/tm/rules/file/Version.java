package com.softwaremagico.tm.rules.file;

/*
 * #%L
 * Libro de Esher (GUI)
 * %%
 * Copyright (C) 2007 - 2014 Softwaremagico
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

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class Version {
	private static final String PROJECT_NAME = "think-machine-rules";

	public static String getVersion() {
		try {
			final String className = Version.class.getSimpleName() + ".class";
			final String classPath = Version.class.getResource(className).toString();
			Manifest manifest = null;
			// Not found, search the manifest.
			if (!classPath.startsWith("jar")) {
				final Enumeration<URL> resources = Version.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
				while (resources.hasMoreElements()) {
					final URL url = resources.nextElement();
					if (url.toString().contains(PROJECT_NAME)) {
						manifest = new Manifest(url.openStream());
					}
				}
			} else {
				final String manifestPath = classPath.substring(0, classPath.lastIndexOf("!") + 1)
						+ "/META-INF/MANIFEST.MF";
				manifest = new Manifest(new URL(manifestPath).openStream());
			}

			if (manifest != null) {
				final Attributes attributes = manifest.getMainAttributes();
				final String version = attributes.getValue("Implementation-Version");
				return version;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
