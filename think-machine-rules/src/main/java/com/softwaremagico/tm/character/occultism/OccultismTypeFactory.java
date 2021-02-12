package com.softwaremagico.tm.character.occultism;

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

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.log.MachineXmlReaderLog;

public class OccultismTypeFactory extends XmlFactory<OccultismType> {
    private static final String TRANSLATOR_FILE = "occultismTypes.xml";

    private static final String DARK_SIDE = "darkSide";

    public static final String PSI_TAG = "psi";
    public static final String THEURGY_TAG = "theurgy";

    private static class OccultismTypeFactoryInit {
        public static final OccultismTypeFactory INSTANCE = new OccultismTypeFactory();
    }

    public static OccultismTypeFactory getInstance() {
        return OccultismTypeFactoryInit.INSTANCE;
    }

    @Override
    protected OccultismType createElement(ITranslator translator, String occulstimTypeId, String name, String description,
                                          String language, String moduleName) throws InvalidXmlElementException {
        try {
            final String darkSide = translator.getNodeValue(occulstimTypeId, DARK_SIDE, language);
            return new OccultismType(occulstimTypeId, name, description, language, moduleName, darkSide);
        } catch (Exception e) {
            throw new InvalidOccultismTypeException("Invalid structure in occultism type '" + occulstimTypeId + "'.", e);
        }
    }

    @Override
    public String getTranslatorFile() {
        return TRANSLATOR_FILE;
    }

    public static OccultismType getPsi(String language, String moduleName) {
        try {
            return OccultismTypeFactory.getInstance().getElement(PSI_TAG, language, moduleName);
        } catch (InvalidXmlElementException e) {
            MachineXmlReaderLog.errorMessage(OccultismTypeFactory.class.getName(), e);
            return null;
        }
    }

    public static OccultismType getTheurgy(String language, String moduleName) {
        try {
            return OccultismTypeFactory.getInstance().getElement(THEURGY_TAG, language, moduleName);
        } catch (InvalidXmlElementException e) {
            MachineXmlReaderLog.errorMessage(OccultismTypeFactory.class.getName(), e);
            return null;
        }
    }
}
