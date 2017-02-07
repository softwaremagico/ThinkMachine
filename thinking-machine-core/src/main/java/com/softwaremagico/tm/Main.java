package com.softwaremagico.tm;

import java.io.File;

import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.pdf.CharacterSheet;

/*-
 * #%L
 * The Thinking Machine (Core)
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
	private static final int LANGUAGE = 0;
	private static final int FILE_DESTINATION_PATH = 1;
	private static String language, destinationPath;

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Execute with parameters:");
			System.out.println("\t	language	The language to print the sheet file.");
			System.out.println("\t	path		The path to store the file.");
			System.out.println();
			System.out.println("Example:");
			System.out.println("\tmvn exec:java -Dexec.args=\"en /tmp\"");
			System.exit(0);
		}
		setArguments(args);

		LanguagePool.clearCache();
		CharacterSheet sheet = new CharacterSheet(language);
		sheet.createFile(destinationPath + "FadingSuns_" + language.toUpperCase() + ".pdf");
	}

	private static void setArguments(String[] args) {

		if (args.length <= LANGUAGE) {
			language = "en";
		} else {
			language = args[LANGUAGE];
		}

		if (args.length <= FILE_DESTINATION_PATH) {
			destinationPath = System.getProperty("java.io.tmpdir");
		} else {
			destinationPath = args[FILE_DESTINATION_PATH] + File.separator;
		}
	}
}
