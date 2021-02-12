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

public class TheurgyComponentFactory extends XmlFactory<TheurgyComponent> {
    private static final String TRANSLATOR_FILE = "theurgyComponents.xml";

    private static final String ABBREVIATION = "abbreviature";
    private static final String CODE = "code";

    private static class TheurgyComponentFactoryInit {
        public static final TheurgyComponentFactory INSTANCE = new TheurgyComponentFactory();
    }

    public static TheurgyComponentFactory getInstance() {
        return TheurgyComponentFactoryInit.INSTANCE;
    }

    @Override
    public String getTranslatorFile() {
        return TRANSLATOR_FILE;
    }

    @Override
    protected TheurgyComponent createElement(ITranslator translator, String theurgyComponentId, String name, String description, String language,
                                             String moduleName) throws InvalidXmlElementException {
        try {
            final String abbreviature;
            try {
                abbreviature = translator.getNodeValue(theurgyComponentId, ABBREVIATION, language);
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

            return new TheurgyComponent(theurgyComponentId, name, description, language,
                    moduleName, abbreviature, code.charAt(0));
        } catch (Exception e) {
            throw new InvalidTheurgyComponentException("Invalid structure in theurgyComponent '" + theurgyComponentId
                    + "'.");
        }
    }

    public TheurgyComponent getTheurgyComponent(char code, String language, String moduleName)
            throws InvalidXmlElementException {
        for (final TheurgyComponent theurgyComponent : getElements(language, moduleName)) {
            if (theurgyComponent.getCode() == code) {
                return theurgyComponent;
            }
        }
        return null;
    }
}
