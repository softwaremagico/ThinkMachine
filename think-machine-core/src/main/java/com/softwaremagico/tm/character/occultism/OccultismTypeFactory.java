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
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.log.MachineLog;

public class OccultismTypeFactory extends XmlFactory<OccultismType> {
	private final static ITranslator translatorBlessing = LanguagePool.getTranslator("occultismTypes.xml");

	private final static String NAME = "name";
	private final static String DARK_SIDE = "darkSide";

	private final static String PSI_TAG = "psi";
	private final static String THEURGY_TAG = "theurgy";

	private static OccultismTypeFactory instance;

	private static void createInstance() {
		if (instance == null) {
			synchronized (OccultismTypeFactory.class) {
				if (instance == null) {
					instance = new OccultismTypeFactory();
				}
			}
		}
	}

	public static OccultismTypeFactory getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}

	@Override
	protected OccultismType createElement(ITranslator translator, String occulstimTypeId, String language)
			throws InvalidXmlElementException {
		try {
			String name = translator.getNodeValue(occulstimTypeId, NAME, language);
			String darkSide = translator.getNodeValue(occulstimTypeId, DARK_SIDE, language);
			return new OccultismType(occulstimTypeId, name, darkSide);
		} catch (Exception e) {
			throw new InvalidOccultismTypeException("Invalid structure in occultism type '" + occulstimTypeId + "'.", e);
		}
	}

	@Override
	protected ITranslator getTranslator() {
		return translatorBlessing;
	}

	public static OccultismType getPsi(String language) {
		try {
			return OccultismTypeFactory.getInstance().getElement(PSI_TAG, language);
		} catch (InvalidXmlElementException e) {
			MachineLog.errorMessage(OccultismTypeFactory.class.getName(), e);
			return null;
		}
	}

	public static OccultismType getTheurgy(String language) {
		try {
			return OccultismTypeFactory.getInstance().getElement(THEURGY_TAG, language);
		} catch (InvalidXmlElementException e) {
			MachineLog.errorMessage(OccultismTypeFactory.class.getName(), e);
			return null;
		}
	}
}
