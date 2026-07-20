package com.softwaremagico.tm.file.configurator;

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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import com.softwaremagico.tm.file.FileManager;
import com.softwaremagico.tm.file.configurator.exceptions.PropertyNotStoredException;
import com.softwaremagico.tm.log.MachineLog;

public class PropertiesFile {

	public PropertiesFile() {
	}

	public static Properties load(String path, String fileName) throws IOException {
		final File file = new File(path, fileName);
		return load(file);
	}

	public static Properties load(String fileName) throws IOException {
		final File file = FileManager.getResource(fileName);
		return load(file);
	}

	public static Properties load(File file) throws IOException {
		if (file == null) {
			throw new IllegalArgumentException("File is not set.");
		} else {
			return load((InputStream) (new FileInputStream(file)));
		}
	}

	public static Properties load(URL url) throws IOException {
		if (url == null) {
			throw new IllegalArgumentException("Url is not set.");
		} else {
			final URLConnection connection = url.openConnection();
			return load(connection.getInputStream());
		}
	}

	public static Properties load(InputStream inputStream) throws IOException {
		final Properties properties = new Properties();
		if (inputStream == null) {
			throw new IllegalArgumentException("InputStream is not set.");
		}
		try {
			properties.load(new BufferedInputStream(inputStream));
		} finally {
			inputStream.close();
		}

		return properties;
	}

	public static boolean store(Properties properties, String folderPath, String fileName) throws IOException,
			PropertyNotStoredException {
		if (folderPath != null && fileName != null) {
			// Ensure that the folder exists.
			final File folder = new File(folderPath);
			try {
				folder.mkdirs();
			} catch (Exception e) {
				MachineLog.errorMessage(PropertiesFile.class.getName(), e);
			}
			// Create file if not exists.
			final File file = new File(folderPath + File.separator + fileName);
			if (!file.exists()) {
				if (!file.createNewFile()) {
					throw new PropertyNotStoredException("File '" + folderPath + File.separator + fileName
							+ "' cannot be created.");
				}
			}
			store(properties, file);
		}
		return false;
	}

	public static void store(Properties properties, File file) throws IOException {
		store(properties, ((OutputStream) (new FileOutputStream(file))));
	}

	public static void store(Properties properties, URL url) throws IOException {
		if (url == null) {
			throw new IllegalArgumentException("Url is not set.");
		} else {
			final URLConnection connection = url.openConnection();
			connection.setDoOutput(true);
			store(properties, connection.getOutputStream());
		}
	}

	public static void store(Properties properties, OutputStream outputStream) throws IOException {
		try {
			if (properties == null) {
				throw new IllegalArgumentException("Properties is not set.");
			}
			if (outputStream == null) {
				throw new IllegalArgumentException("OutputStream is not set.");
			}
			properties.store(new BufferedOutputStream(outputStream), null);
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}

}
