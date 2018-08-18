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

public class OccultismDurationFactory extends XmlFactory<OccultismDuration> {
	private final static ITranslator translatorBlessing = LanguagePool.getTranslator("occultismDuration.xml");

	private final static String NAME = "name";

	private static OccultismDurationFactory instance;

	private static void createInstance() {
		if (instance == null) {
			synchronized (OccultismDurationFactory.class) {
				if (instance == null) {
					instance = new OccultismDurationFactory();
				}
			}
		}
	}

	public static OccultismDurationFactory getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}

	@Override
	protected OccultismDuration createElement(ITranslator translator, String rangeId, String language)
			throws InvalidXmlElementException {
		try {
			String name = translator.getNodeValue(rangeId, NAME, language);
			return new OccultismDuration(rangeId, name);
		} catch (Exception e) {
			throw new InvalidOccultismDurationException("Invalid structure in duration '" + rangeId + "'.", e);
		}
	}

	@Override
	protected ITranslator getTranslator() {
		return translatorBlessing;
	}
}
