package com.softwaremagico.tm.file.configurator;

import java.util.Properties;

public interface IPropertiesSource {

	Properties loadFile();

	String getFilePath();

	String getFileName();

}
