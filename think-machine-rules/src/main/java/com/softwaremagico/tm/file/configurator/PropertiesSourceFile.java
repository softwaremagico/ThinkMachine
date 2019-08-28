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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import com.softwaremagico.tm.file.configurator.exceptions.PropertyNotStoredException;
import com.softwaremagico.tm.file.watcher.FileWatcher;
import com.softwaremagico.tm.file.watcher.FileWatcher.FileModifiedListener;
import com.softwaremagico.tm.log.MachineLog;

public class PropertiesSourceFile extends SourceFile<Properties> implements IPropertiesSource {
	private Set<FileModifiedListener> fileModifiedListeners;
	private FileWatcher fileWatcher;

	public PropertiesSourceFile(String fileName) {
		super(fileName);
		fileModifiedListeners = new HashSet<>();
	}

	public PropertiesSourceFile(String filePath, String fileName) {
		super(filePath, fileName);
		fileModifiedListeners = new HashSet<>();
	}

	public void addFileModifiedListeners(FileModifiedListener fileModifiedListener) {
		fileModifiedListeners.add(fileModifiedListener);
	}

	@Override
	public void setFilePath(String filePath) {
		super.setFilePath(filePath);
		setWatcher();
	}

	public void storeInFile(Map<String, String> propertiesValues) throws IOException, PropertyNotStoredException {
		final Properties properties = new Properties();
		// Sort properties and add it.
		for (final Entry<String, String> entry : new TreeMap<String, String>(propertiesValues).entrySet()) {
			properties.setProperty(entry.getKey(), entry.getValue());
		}
		PropertiesFile.store(properties, getFilePath(), getFileName());
		MachineLog.debug(this.getClass().getName(), "Storing '" + properties + "' at properties file '" + getFilePath()
				+ File.separator + getFileName() + "'.");
	}

	@Override
	public Properties loadFile() {
		try {
			if (getFilePath() == null) {
				return PropertiesFile.load(getFileName());
			} else {
				return PropertiesFile.load(getFilePath(), getFileName());
			}
		} catch (FileNotFoundException e) {
			MachineLog.debug(this.getClass().getName(), e.getMessage());
		} catch (IOException e) {
			MachineLog.errorMessage(this.getClass().getName(), e);
		} catch (NullPointerException e) {
			MachineLog.info(this.getClass().getName(), e.getMessage());
		}
		return null;
	}

	private String getDirectoryToWatch() {
		return (getFilePath() != null ? getFilePath() : this.getClass().getClassLoader().getResource(".").getPath());
	}

	private void setWatcher() {
		final Set<String> checkedFiles = new HashSet<>(Arrays.asList(new String[] { getFileName() }));

		try {
			fileWatcher = new FileWatcher(getDirectoryToWatch(), checkedFiles);
			fileWatcher.addFileModifiedListener(new FileModifiedListener() {

				@Override
				public void changeDetected(Path pathToFile) {
					// Pass the listener to current listeners.S
					for (final FileModifiedListener fileModifiedListener : fileModifiedListeners) {
						fileModifiedListener.changeDetected(pathToFile);
					}
				}
			});
		} catch (NoSuchFileException | NullPointerException e) {
			MachineLog.warning(this.getClass().getName(), "Directory '" + getDirectoryToWatch()
					+ "' to watch not found!");
		} catch (IOException e) {
			MachineLog.errorMessage(this.getClass().getName(), e);
		}
	}

	public void stopFileWatcher() {
		if (fileWatcher != null) {
			fileWatcher.closeFileWatcher();
		}
	}
}
