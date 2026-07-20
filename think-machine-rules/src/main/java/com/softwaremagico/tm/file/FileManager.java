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

import com.softwaremagico.tm.log.ConfigurationLog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
     * @return
     * @throws IOException
     */
    public static String inString(String filename) throws FileNotFoundException, IOException {
        return readTextFile(filename, "ISO8859_1");
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

    public static String readTextFile(String filename) throws FileNotFoundException {
        return readTextFile(new File(filename));
    }

    public static String readTextFile(File file) throws FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + file.getAbsolutePath());
        }
        final StringBuilder text = new StringBuilder();
        try (Scanner scanner = new Scanner(file, StandardCharsets.UTF_8.name())) {
            while (scanner.hasNextLine()) {
                text.append(scanner.nextLine()).append("\n");
            }
            return text.toString();
        }
    }

    public static String readTextFile(String path, Charset encoding) throws IOException {
        final byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
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
            ConfigurationLog.errorMessage(FileManager.class.getName(), e);
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
            ConfigurationLog.errorMessage(FileManager.class.getName(), e);
        }
        return contents;
    }

    public static File[] findJarFiles(String path) {
        final File dir = new File(path);
        return dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".jar");
            }
        });
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
            ConfigurationLog.errorMessage(FileManager.class.getName(), e);
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
         * To convert the InputStream to String we use the Reader.read(char[] buffer) method. We iterate until the
         * Reader return -1 which means there's no more data to read. We use the StringWriter class to produce the
         * string.
         */
        if (is != null) {
            final Writer writer = new StringWriter();

            final char[] buffer = new char[1024];
            try {
                final Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8.name()));
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

    public static File getResource(String fileName) throws NullPointerException {
        return getResource(FileManager.class, fileName);
    }

    @SuppressWarnings({"java:S2259"})
    public static File getResource(Class<?> classWithResources, String fileName) throws NullPointerException {
        final URL url = classWithResources.getClassLoader().getResource(fileName);
        if (url != null) {
            ConfigurationLog.info(FileManager.class.getName(),
                    "Resource to read '" + fileName + "' found at url '" + url + "'.");
        } else {
            ConfigurationLog.warning(FileManager.class.getName(), "Invalid resource '" + fileName + "'.");
        }
        File file = null;
        // Jetty load resource.
        try {
            // We use path to remove URI special codification that is not
            // allowed for File.
			final String path = URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8.name());
            file = new File(convert2OsPath(url));
            // Apache load resource
            if (!file.exists()) {
                file = new File(path);
                // Resource inside a jar.
                if (path.contains(".jar!")) {
                    ConfigurationLog.info(FileManager.class.getName(), "Resource inside a jar. Copy to a temporal file.");
                    // Copy to a temp file and return it.
                    try {
                        // Url has the absolute path with the correct
                        // codification for an InputStream.
                        final InputStream inputStream = url.openStream();
                        try {
                            if (inputStream != null) {
                                final File tempFile = File.createTempFile(fileName, "_jar");
                                // tempFile.deleteOnExit();
                                try (final OutputStream os = Files.newOutputStream(tempFile.toPath())) {
                                    final byte[] buffer = new byte[1024];
                                    int bytesRead;
                                    // read from is to buffer
                                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                                        os.write(buffer, 0, bytesRead);
                                    }
                                }
                                return tempFile;
                            }
                        } finally {
                            try {
                                inputStream.close();
                            } catch (Exception e) {
                                // Do nothing.
                            }
                        }
                    } catch (IOException e) {
                        ConfigurationLog.errorMessage(FileManager.class.getName(), e);
                    }
                }
                if (!file.exists()) {
                    ConfigurationLog.severe(FileManager.class.getName(), "File not found '" + path + "'.");
                }
            }
        } catch (NullPointerException npe) {
            throw new NullPointerException("File '" + fileName + "' does not exist.");
        } catch (UnsupportedEncodingException ue) {
            ConfigurationLog.errorMessage(FileManager.class.getName(), ue);
        }
        return file;
    }

    public static String convert2OsPath(URL string) {
        try {
            if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
                if (string.getPath().startsWith("file:")) {
                    return (string.getPath()).replace("file:", "").substring(1);
                } else {
                    return (string.getPath()).substring(1);
                }
            } else {
                return (string.getPath());
            }
        } catch (NullPointerException npe) {
            return null;
        }
    }

    public static void makeFolderIfNotExist(String file) {
        final File f = new File(file);
        if (f.mkdir()) {
            ConfigurationLog.debug(FileManager.class.getName(), "File '" + file + "' created.");
        }
    }
}
