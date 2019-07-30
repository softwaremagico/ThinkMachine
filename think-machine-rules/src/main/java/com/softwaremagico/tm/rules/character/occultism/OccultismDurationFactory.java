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

public class OccultismDurationFactory extends XmlFactory<OccultismDuration> {
	private static final ITranslator translatorBlessing = LanguagePool.getTranslator("occultismDuration.xml");

	private static final String NAME = "name";

	private static class OccultismDurationFactoryInit {
		public static final OccultismDurationFactory INSTANCE = new OccultismDurationFactory();
	}

	public static OccultismDurationFactory getInstance() {
		return OccultismDurationFactoryInit.INSTANCE;
	}

	@Override
	protected OccultismDuration createElement(ITranslator translator, String rangeId, String language)
			throws InvalidXmlElementException {
		try {
			final String name = translator.getNodeValue(rangeId, NAME, language);
			return new OccultismDuration(rangeId, name, language);
		} catch (Exception e) {
			throw new InvalidOccultismDurationException("Invalid structure in duration '" + rangeId + "'.", e);
		}
	}

	@Override
	protected ITranslator getTranslator() {
		return translatorBlessing;
	}
}
