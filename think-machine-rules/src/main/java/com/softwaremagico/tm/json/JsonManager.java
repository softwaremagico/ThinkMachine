package com.softwaremagico.tm.json;

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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonManager {
	public static final String LANGUAGE_PATTERN = "\"language\"\\s*:\\s*\"(.*?)\"";
	public static final String MODULE_PATTERN = "\"moduleName\"\\s*:\\s*\"(.*?)\"";
	private static final Pattern languagePattern = Pattern.compile(LANGUAGE_PATTERN);
	private static final Pattern modulePattern = Pattern.compile(MODULE_PATTERN);

	protected static String getLanguage(String jsonText) throws InvalidJsonException {
		final Matcher m = languagePattern.matcher(jsonText);
		if (!m.find()) {
			throw new InvalidJsonException("No language defined on json:\n" + jsonText);
		}
		return m.group(1);
	}

	protected static String getModuleName(String jsonText) throws InvalidJsonException {
		final Matcher m = modulePattern.matcher(jsonText);
		if (!m.find()) {
			throw new InvalidJsonException("No module defined on json:\n" + jsonText);
		}
		return m.group(1);
	}

}
