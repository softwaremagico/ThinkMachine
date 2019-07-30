package com.softwaremagico.tm.rules.character.occultism;

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

import com.softwaremagico.tm.rules.InvalidXmlElementException;
import com.softwaremagico.tm.rules.XmlFactory;
import com.softwaremagico.tm.rules.language.ITranslator;
import com.softwaremagico.tm.rules.language.LanguagePool;

public class TheurgyComponentFactory extends XmlFactory<TheurgyComponent> {
	private static final ITranslator translatorCharacteristics = LanguagePool.getTranslator("theurgyComponents.xml");

	private static final String NAME = "name";
	private static final String ABBREVIATURE = "abbreviature";
	private static final String CODE = "code";

	private static class TheurgyComponentFactoryInit {
		public static final TheurgyComponentFactory INSTANCE = new TheurgyComponentFactory();
	}

	public static TheurgyComponentFactory getInstance() {
		return TheurgyComponentFactoryInit.INSTANCE;
	}

	@Override
	protected ITranslator getTranslator() {
		return translatorCharacteristics;
	}

	@Override
	protected TheurgyComponent createElement(ITranslator translator, String theurgyComponentId, String language)
			throws InvalidXmlElementException {
		try {
			final String name = translator.getNodeValue(theurgyComponentId, NAME, language);

			final String abbreviature;
			try {
				abbreviature = translator.getNodeValue(theurgyComponentId, ABBREVIATURE, language);
			} catch (Exception e) {
				throw new InvalidTheurgyComponentException("Invalid abbreviature in theurgyComponent '"
						+ theurgyComponentId + "'.");
			}

			final String code;
			try {
				code = translator.getNodeValue(theurgyComponentId, CODE);
			} catch (Exception e) {
				throw new InvalidTheurgyComponentException("Invalid code in theurgyComponent '" + theurgyComponentId
						+ "'.");
			}

			final TheurgyComponent theurgyComponent = new TheurgyComponent(theurgyComponentId, name, language,
					abbreviature, code.charAt(0));
			return theurgyComponent;
		} catch (Exception e) {
			throw new InvalidTheurgyComponentException("Invalid structure in theurgyComponent '" + theurgyComponentId
					+ "'.");
		}
	}

	public TheurgyComponent getTheurgyComponent(char code, String language) throws InvalidXmlElementException {
		for (final TheurgyComponent theurgyComponent : getElements(language)) {
			if (theurgyComponent.getCode() == code) {
				return theurgyComponent;
			}
		}
		return null;
	}
}
