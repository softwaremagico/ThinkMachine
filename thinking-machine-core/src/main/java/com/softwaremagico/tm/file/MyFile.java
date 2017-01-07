package com.softwaremagico.tm.file;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MyFile {

	/**
	 * Creates a new instance of MyFile
	 */
	private MyFile() {
	}

	/**
	 * Devuelve las lineas de un fichero leido anteriormente.
	 */
	public static List<String> inLines(String filename) throws FileNotFoundException, IOException {
		String OS = System.getProperty("os.name");
		if (OS.contains("Windows Vista")) {
			return readTextFileInLines(filename, "ISO8859_1");
		} else if (OS.contains("Windows")) {
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

	/**
	 * Devuelve el fichero leido como una lista de lineas.
	 */
	private static List<String> readTextFileInLines(String filename, String mode)
			throws FileNotFoundException {
		List<String> contents = new ArrayList<>();

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

	/**
	 * Devuelve el fichero leido como un unico string.
	 */
	public static String readTextFile(String filename) {
		File file = new File(filename);
		if (!file.exists()) {
			return "File not found: " + filename;
		}
		String text;
		byte bt[] = new byte[(int) file.length()];
		text = new String(bt);
		return text;
	}

	/**
	 * Devuelve el fichero leido como un unico string.
	 */
	private static String readTextFile(String filename, String mode) throws FileNotFoundException {
		String text = "";
		List<String> doc = readTextFileInLines(filename, mode);

		for (int i = 0; i < doc.size(); i++) {
			if (!doc.get(i).startsWith("[") && !doc.get(i).startsWith("]") && !doc.get(i).startsWith("<")) {
				text += doc.get(i) + "\n";
			}
		}

		return text;
	}

	public static String readTextFromJar(String s) {
		String totalText = "";
		String thisLine;
		try {
			InputStream is = MyFile.class.getResourceAsStream(s);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			while ((thisLine = br.readLine()) != null) {
				totalText += thisLine;
			}
		} catch (Exception e) {
		}
		return totalText;
	}

	/**
	 * Removes a file.
	 * 
	 * @param filename
	 */
	public static void deleteFile(String filename) {
		File f = new File(filename);
		if (f.exists() && f.canWrite()) {
			f.delete();
		}
	}

	public static String convertStreamToString(InputStream is) throws IOException {
		/*
		 * To convert the InputStream to String we use the Reader.read(char[]
		 * buffer) method. We iterate until the Reader return -1 which means
		 * there's no more data to read. We use the StringWriter class to
		 * produce the string.
		 */
		if (is != null) {
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
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
		File f = new File(path);
		if (f.exists()) {
			return true;
		}
		return false;
	}
}
