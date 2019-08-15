package com.softwaremagico.tm.file.configurator;

import java.io.FileNotFoundException;
import java.util.Map;

public abstract class SourceFile<FileType> implements ISourceFile<FileType> {
	private String filePath;
	private final String fileName;

	public SourceFile(String fileName) {
		this.fileName = fileName;
		setFilePath(null);
	}

	public SourceFile(String filePath, String fileName) {
		this.fileName = fileName;
		setFilePath(filePath);
	}

	@Override
	public abstract FileType loadFile() throws FileNotFoundException;

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public String getFilePath() {
		return filePath;
	}

	@Override
	public String getFileName() {
		return fileName;
	}

	@Override
	public String toString() {
		return (getFilePath() != null ? getFilePath() + "/" : "") + getFileName();
	}

	public static String readEnvironmentVariable(String environmentVariable) {
		final Map<String, String> env = System.getenv();
		return env.get(environmentVariable);
	}
}
