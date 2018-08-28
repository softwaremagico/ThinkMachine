package com.softwaremagico.tm;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.Language;
import com.softwaremagico.tm.log.MachineLog;

public abstract class XmlFactory<T extends Element<T>> {
	protected Map<String, List<T>> elements = new HashMap<>();

	protected XmlFactory() {
		initialize();
	}

	protected void initialize() {
		List<Language> languages = getTranslator().getAvailableLanguages();
		for (Language language : languages) {
			try {
				getElements(language.getAbbreviature());
			} catch (InvalidXmlElementException e) {
				MachineLog.errorMessage(this.getClass().getName(), e);
			}
		}
	}

	protected abstract ITranslator getTranslator();

	public void clearCache() {
		elements = new HashMap<>();
		initialize();
	}

	public List<T> getElements(String language) throws InvalidXmlElementException {
		if (elements.get(language) == null) {
			elements.put(language, new ArrayList<T>());
			for (String elementId : getTranslator().getAllTranslatedElements()) {
				T element = createElement(getTranslator(), elementId, language);
				if (elements.get(language).contains(element)) {
					throw new ElementAlreadyExistsException("Element '" + element + "' already is inserted. Probably the ID is duplicated.");
				}
				elements.get(language).add(element);
			}
			Collections.sort(elements.get(language));
		}
		return elements.get(language);
	}

	public T getElement(String elementId, String language) throws InvalidXmlElementException {
		List<T> elements = getElements(language);
		for (T element : elements) {
			if (element.getId() != null) {
				if (Objects.equals(element.getId().toLowerCase(), elementId.trim().toLowerCase())) {
					return element;
				}
			}
		}
		throw new InvalidXmlElementException("Element '" + elementId + "' does not exists.");
	}

	protected abstract T createElement(ITranslator translator, String elementId, String language) throws InvalidXmlElementException;
}
