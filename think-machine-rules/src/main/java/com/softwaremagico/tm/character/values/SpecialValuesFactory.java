package com.softwaremagico.tm.character.values;

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

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;

public class SpecialValuesFactory extends XmlFactory<SpecialValue> {
	private static final ITranslator translatorSpecials = LanguagePool.getTranslator("special_values.xml");

	private static final String NAME = "name";
	private static final String AFFECTS = "affects";

	private static class SpecialValuesFactoryInit {
		public static final SpecialValuesFactory INSTANCE = new SpecialValuesFactory();
	}

	public static SpecialValuesFactory getInstance() {
		return SpecialValuesFactoryInit.INSTANCE;
	}

	@Override
	protected ITranslator getTranslator() {
		return translatorSpecials;
	}

	@Override
	protected SpecialValue createElement(ITranslator translator, String specialId, String language)
			throws InvalidXmlElementException {
		try {
			final String name = translator.getNodeValue(specialId, NAME, language);
			final String affects = translator.getNodeValue(specialId, AFFECTS);

			final Set<IValue> affectsGroup = new HashSet<>();

			if (affects != null && !affects.isEmpty()) {
				final StringTokenizer affectsValue = new StringTokenizer(affects, ",");
				while (affectsValue.hasMoreTokens()) {
					affectsGroup.add(SpecialValue.getValue(affectsValue.nextToken().trim(), language));
				}
			}

			final SpecialValue specialValue = new SpecialValue(specialId, name, language, affectsGroup);
			return specialValue;
		} catch (Exception e) {
			throw new InvalidSpecialValueException("Invalid structure in special '" + specialId + "'.", e);
		}
	}

}
