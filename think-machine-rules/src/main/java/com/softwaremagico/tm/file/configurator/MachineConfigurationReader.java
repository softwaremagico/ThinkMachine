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
import java.io.IOException;
import java.nio.file.Path;

import com.softwaremagico.tm.file.FileManager;
import com.softwaremagico.tm.file.configurator.exceptions.PropertyNotFoundException;
import com.softwaremagico.tm.file.configurator.exceptions.PropertyNotStoredException;
import com.softwaremagico.tm.file.watcher.FileWatcher.FileModifiedListener;
import com.softwaremagico.tm.log.MachineLog;
import com.softwaremagico.tm.log.SuppressFBWarnings;

public class MachineConfigurationReader extends ConfigurationReader {
	private static final String DEFAULT_CONFIG_FILE = "settings.conf";
	private static final String USER_CONFIG_FILE = "settings.conf";
	private static final String FOLDER_STORE_USER_DATA = "ThinkMachine";

	// Tags
	private static final String MODULES_PATH = "modulesPath";

	// Default

	private static MachineConfigurationReader instance;
	private final PropertiesSourceFile userSourceFile;

	@SuppressFBWarnings(value = "DC_DOUBLECHECK")
	public static MachineConfigurationReader getInstance() {
		if (instance == null) {
			synchronized (MachineConfigurationReader.class) {
				if (instance == null) {
					instance = new MachineConfigurationReader();
				}
			}
		}
		return instance;
	}

	protected MachineConfigurationReader() {
		super();

		setProperty(MODULES_PATH, "");

		final PropertiesSourceFile sourceFile = new PropertiesSourceFile(DEFAULT_CONFIG_FILE);
		sourceFile.addFileModifiedListeners(new FileModifiedListener() {

			@Override
			public void changeDetected(Path pathToFile) {
				MachineLog.info(this.getClass().getName(), "Application's settings file '" + pathToFile
						+ "' change detected.");
				readConfigurations();
			}
		});
		addPropertiesSource(sourceFile);

		userSourceFile = new PropertiesSourceFile(USER_CONFIG_FILE);
		userSourceFile.setFilePath(getSettingsFolderAtHome());
		userSourceFile.addFileModifiedListeners(new FileModifiedListener() {

			@Override
			public void changeDetected(Path pathToFile) {
				MachineLog.info(this.getClass().getName(), "Application's settings file '" + pathToFile
						+ "' change detected.");
				readConfigurations();
			}
		});
		addPropertiesSource(userSourceFile);

		// Log if any property has changed the value.
		addPropertyChangedListener(new PropertyChangedListener() {

			@Override
			public void propertyChanged(String propertyId, String oldValue, String newValue) {
				MachineLog.info(this.getClass().getName(), "Property '" + propertyId + "' has changed value from '"
						+ oldValue + "' to '" + newValue + "'.");
			}
		});

		readConfigurations();
	}

	public static String getSettingsFolderAtHome() {
		final String folder = System.getProperty("user.home") + File.separator + "." + FOLDER_STORE_USER_DATA;
		FileManager.makeFolderIfNotExist(folder);
		return folder;
	}

	@Override
	public void storeProperties() throws PropertyNotStoredException {
		try {
			userSourceFile.storeInFile(getAllProperties());
		} catch (IOException e) {
			throw new PropertyNotStoredException(MachineConfigurationReader.class.getName(), e);
		}
	}

	@Override
	public File getUserProperties() {
		return new File(userSourceFile.getFilePath() + File.separator + userSourceFile.getFileName());
	}

	protected String getPropertyLogException(String propertyId) {
		try {
			return getProperty(propertyId);
		} catch (PropertyNotFoundException e) {
			MachineLog.errorMessage(this.getClass().getName(), e);
			return null;
		}
	}

	public String getModulesPath() {
		return getPropertyLogException(MODULES_PATH);
	}
}
