package com.softwaremagico.tm.language;

import java.util.List;
import java.util.Set;

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

public interface ITranslator {

	String getTranslatedText(String tag);

	String getTranslatedText(String tag, String language);

	String getTranslatedText(String tag, String[] args);

	String getTranslatedText(String tag, String language, Object[] args);

	String convertToXmlTag(String text);

	List<String> getAllTranslatedElements();

	String getNodeValue(String tag, String node);

	String getNodeValue(String parent, String tag, String node);

	Set<String> getAllChildrenTags(String parent, String tag);

	List<Language> getAvailableLanguages();

	String getNodeValue(String grandparent, String parent, String tag, String node);

	String getNodeValue(String parent, String tag, String node, int nodeNumber);

	String getNodeValue(String tag, String node, int nodeNumber);

}
