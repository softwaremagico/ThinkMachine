package com.softwaremagico.tm.file.configurator;

import java.io.FileNotFoundException;

public interface ISourceFile<FileType> {

	FileType loadFile() throws FileNotFoundException;

	String getFilePath();

	String getFileName();

}
