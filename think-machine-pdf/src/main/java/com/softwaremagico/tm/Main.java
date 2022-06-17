package com.softwaremagico.tm;

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.file.PathManager;
import com.softwaremagico.tm.json.CharacterJsonManager;
import com.softwaremagico.tm.json.InvalidJsonException;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.log.PdfExporterLog;
import com.softwaremagico.tm.pdf.complete.CharacterSheet;

import java.io.File;
import java.io.IOException;

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

public class Main {
    private static final int LANGUAGE_ARG = 0;
    private static final int FILE_DESTINATION_PATH_ARG = 1;
    private static final int JSON_FILE_ARG = 2;
    private static String language, destinationPath, jsonFile;

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Execute with parameters:");
            System.out.println("\t	language\tThe language to print the sheet file.");
            System.out.println("\t	path\t\tThe path to store the file.");
            System.out.println("\t	character\tThe character definition (as Json) to fill up the sheet.");
            System.out.println();
            System.out.println("Example:");
            System.out.println("\tmvn exec:java -Dexec.args=\"en /tmp character.json\"");
            System.exit(0);
        }
        setArguments(args);

        LanguagePool.clearCache();
        final CharacterSheet sheet;
        if (jsonFile == null) {
            sheet = new CharacterSheet(language, PathManager.DEFAULT_MODULE_FOLDER);
            sheet.createFile(destinationPath + "FadingSuns_" + language.toUpperCase() + ".pdf");
        } else {
            try {
                final CharacterPlayer player = CharacterJsonManager.fromFile(jsonFile);
                sheet = new CharacterSheet(player);
                sheet.createFile(destinationPath + "FadingSuns_" + language.toUpperCase() + ".pdf");
            } catch (IOException | InvalidJsonException e) {
                PdfExporterLog.errorMessage(Main.class.getName(), e);
            }
        }
    }

    private static void setArguments(String[] args) {
        if (args.length <= LANGUAGE_ARG) {
            language = "en";
        } else {
            language = args[LANGUAGE_ARG];
        }

        if (args.length <= FILE_DESTINATION_PATH_ARG) {
            destinationPath = System.getProperty("java.io.tmpdir");
        } else {
            destinationPath = args[FILE_DESTINATION_PATH_ARG] + File.separator;
        }

        if (args.length <= JSON_FILE_ARG) {
            jsonFile = null;
        } else {
            jsonFile = args[JSON_FILE_ARG];
        }
    }
}
