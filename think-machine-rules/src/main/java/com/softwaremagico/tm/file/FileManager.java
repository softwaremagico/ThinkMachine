package com.softwaremagico.tm.file;

/*-
 * #%L
 * Think Machine (Core)
 * %%
 * Copyright (C) 2017 Softwaremagico
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.softwaremagico.tm.log.MachineLog;

public class FileManager {

	/**
	 * Creates a new instance of MyFile
	 */
	private FileManager() {
	}

	public static List<String> inLines(String filename) throws FileNotFoundException, IOException {
		final String os = System.getProperty("os.name");
		if (os.contains("Windows Vista")) {
			return readTextFileInLines(filename, "ISO8859_1");
		} else if (os.contains("Windows")) {
			return readTextFileInLines(filename, "Cp1252");
		}
		return readTextFileInLines(filename, "UTF8");
	}

	/**
	 * Return the text of a file in one string.
	 * 
	 * @param filename
	 * @param verbose
	 * @return
	 * @throws IOException
	 */
	public static String inString(String filename) throws FileNotFoundException, IOException {
		// String OS = System.getProperty("os.name");
		return readTextFile(filename, "ISO8859_1");
		/*
		 * if (OS.contains("Windows Vista") || (OS.contains("Windows 7"))) {
		 * return ReadTextFile("ISO8859_1", verbose); } else if
		 * (OS.contains("Windows")) { return ReadTextFile("Cp1252", verbose); }
		 * return ReadTextFile("UTF8", verbose);
		 */
	}

	private static List<String> readTextFileInLines(String filename, String mode) throws FileNotFoundException {
		final List<String> contents = new ArrayList<>();

		BufferedReader input = null;
		try {
			input = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filename)), mode));
			String line;
			while ((line = input.readLine()) != null) {
				contents.add(line);
			}
		} catch (FileNotFoundException ex) {
			throw new FileNotFoundException("Impossible to read the file: " + filename);
		} catch (IOException ex) {
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException ex) {
			}

		}
		return contents;
	}

	public static String readTextFile(String filename) {
		final File file = new File(filename);
		if (!file.exists()) {
			return "File not found: " + filename;
		}
		final String text;
		final byte bt[] = new byte[(int) file.length()];
		try {
			text = new String(bt, StandardCharsets.UTF_8.name());
			return text;
		} catch (UnsupportedEncodingException e) {
			MachineLog.errorMessage(FileManager.class.getName(), e);
		}
		return "";
	}

	private static String readTextFile(String filename, String mode) throws FileNotFoundException {
		final StringBuilder text = new StringBuilder();
		final List<String> doc = readTextFileInLines(filename, mode);

		for (int i = 0; i < doc.size(); i++) {
			if (!doc.get(i).startsWith("[") && !doc.get(i).startsWith("]") && !doc.get(i).startsWith("<")) {
				text.append(doc.get(i)).append("\n");
			}
		}

		return text.toString();
	}

	public static List<String> getFileFromResources(String fileName) {
		final List<String> contents = new ArrayList<>();

		// Get file from resources folder
		final ClassLoader classLoader = FileManager.class.getClassLoader();
		final File file = new File(classLoader.getResource(fileName).getFile());

		try (Scanner scanner = new Scanner(file, StandardCharsets.UTF_8.name())) {
			while (scanner.hasNextLine()) {
				final String line = scanner.nextLine();
				if (line.length() > 0) {
					contents.add(line);
				}
			}
			scanner.close();
		} catch (IOException e) {
			MachineLog.errorMessage(FileManager.class.getName(), e);
		}

		return contents;

	}

	public static List<String> readTextFromJarInLines(String file) {
		final List<String> contents = new ArrayList<>();
		String thisLine;
		try (final InputStream is = FileManager.class.getResourceAsStream(file);
				final BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8.name()));) {
			while ((thisLine = br.readLine()) != null) {
				contents.add(thisLine);
			}
		} catch (Exception e) {
			MachineLog.errorMessage(FileManager.class.getName(), e);
		}
		return contents;
	}

	public static String readTextFromJar(String file) {
		final StringBuilder totalText = new StringBuilder();
		String thisLine;
		try (final InputStream is = FileManager.class.getResourceAsStream(file);
				final BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8.name()));) {
			while ((thisLine = br.readLine()) != null) {
				totalText.append(thisLine);
			}
		} catch (Exception e) {
			MachineLog.errorMessage(FileManager.class.getName(), e);
		}
		return totalText.toString();
	}

	/**
	 * Removes a file.
	 * 
	 * @param filename
	 */
	public static boolean deleteFile(String filename) {
		final File f = new File(filename);
		if (f.exists() && f.canWrite()) {
			return f.delete();
		}
		return false;
	}

	public static String convertStreamToString(InputStream is) throws IOException {
		/*
		 * To convert the InputStream to String we use the Reader.read(char[]
		 * buffer) method. We iterate until the Reader return -1 which means
		 * there's no more data to read. We use the StringWriter class to
		 * produce the string.
		 */
		if (is != null) {
			final Writer writer = new StringWriter();

			final char[] buffer = new char[1024];
			try {
				final Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return writer.toString();
		} else {
			return "";
		}
	}

	/**
	 * Check if the file already exists.
	 * 
	 * @param path
	 * @return
	 */
	public static boolean fileExist(String path) {
		final File f = new File(path);
		if (f.exists()) {
			return true;
		}
		return false;
	}
}
