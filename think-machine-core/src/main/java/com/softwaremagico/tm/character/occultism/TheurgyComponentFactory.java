package com.softwaremagico.tm.character.occultism;

/*-
 * #%L
 * Think Machine (Core)
 * %%
 * Copyright (C) 2017 - 2018 Softwaremagico
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

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;

public class TheurgyComponentFactory extends XmlFactory<TheurgyComponent> {
	private final static ITranslator translatorCharacteristics = LanguagePool.getTranslator("theurgyComponents.xml");

	private final static String NAME = "name";
	private final static String ABBREVIATURE = "abbreviature";
	private final static String CODE = "code";

	private static TheurgyComponentFactory instance;

	private static void createInstance() {
		if (instance == null) {
			synchronized (TheurgyComponentFactory.class) {
				if (instance == null) {
					instance = new TheurgyComponentFactory();
				}
			}
		}
	}

	public static TheurgyComponentFactory getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}

	@Override
	protected ITranslator getTranslator() {
		return translatorCharacteristics;
	}

	@Override
	protected TheurgyComponent createElement(ITranslator translator, String theurgyComponentId, String language)
			throws InvalidXmlElementException {
		try {
			String name = translator.getNodeValue(theurgyComponentId, NAME, language);

			String abbreviature;
			try {
				abbreviature = translator.getNodeValue(theurgyComponentId, ABBREVIATURE, language);
			} catch (Exception e) {
				throw new InvalidTheurgyComponentException("Invalid abbreviature in theurgyComponent '"
						+ theurgyComponentId + "'.");
			}

			String code;
			try {
				code = translator.getNodeValue(theurgyComponentId, CODE);
			} catch (Exception e) {
				throw new InvalidTheurgyComponentException("Invalid code in theurgyComponent '" + theurgyComponentId
						+ "'.");
			}

			TheurgyComponent theurgyComponent = new TheurgyComponent(theurgyComponentId, name, abbreviature,
					code.charAt(0));
			return theurgyComponent;
		} catch (Exception e) {
			throw new InvalidTheurgyComponentException("Invalid structure in theurgyComponent '" + theurgyComponentId
					+ "'.");
		}
	}

	public TheurgyComponent getTheurgyComponent(char code, String language) throws InvalidXmlElementException {
		for (TheurgyComponent theurgyComponent : getElements(language)) {
			if (theurgyComponent.getCode() == code) {
				return theurgyComponent;
			}
		}
		return null;
	}
}
