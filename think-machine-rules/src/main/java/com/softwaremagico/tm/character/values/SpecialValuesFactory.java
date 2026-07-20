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

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.json.factories.cache.FactoryCacheLoader;
import com.softwaremagico.tm.language.ITranslator;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class SpecialValuesFactory extends XmlFactory<SpecialValue> {
    private static final String TRANSLATOR_FILE = "special_values.xml";

    private static final String AFFECTS = "affects";

    private static class SpecialValuesFactoryInit {
        public static final SpecialValuesFactory INSTANCE = new SpecialValuesFactory();
    }

    public static SpecialValuesFactory getInstance() {
        return SpecialValuesFactoryInit.INSTANCE;
    }

    @Override
    public String getTranslatorFile() {
        return TRANSLATOR_FILE;
    }

    @Override
    public FactoryCacheLoader<SpecialValue> getFactoryCacheLoader() {
        return null;
    }

    @Override
    protected SpecialValue createElement(ITranslator translator, String specialId, String name, String description,
                                         String language, String moduleName)
            throws InvalidXmlElementException {
        try {
            final String affects = translator.getNodeValue(specialId, AFFECTS);

            final Set<IValue> affectsGroup = new HashSet<>();

            if (affects != null && !affects.isEmpty()) {
                final StringTokenizer affectsValue = new StringTokenizer(affects, ",");
                while (affectsValue.hasMoreTokens()) {
                    affectsGroup.add(SpecialValue.getValue(affectsValue.nextToken().trim(), language, moduleName));
                }
            }

            return new SpecialValue(specialId, name, description, language, moduleName, affectsGroup);
        } catch (Exception e) {
            throw new InvalidSpecialValueException("Invalid structure in special '" + specialId + "'.", e);
        }
    }

}
