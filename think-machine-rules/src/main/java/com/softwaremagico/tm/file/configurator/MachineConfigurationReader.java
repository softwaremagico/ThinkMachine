package com.softwaremagico.tm.file.configurator;

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

	private MachineConfigurationReader() {
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

	private String getPropertyLogException(String propertyId) {
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
