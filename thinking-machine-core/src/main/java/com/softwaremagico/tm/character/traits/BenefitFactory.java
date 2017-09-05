package com.softwaremagico.tm.character.traits;

/*-
 * #%L
 * The Thinking Machine (Core)
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

public class BenefitFactory extends XmlFactory<Benefit> {
	private final static ITranslator TRANSLATOR_BENEFITS = LanguagePool.getTranslator("benefits.xml");

	private final static String NAME = "name";
	private final static String COST = "cost";

	private static BenefitFactory instance;

	private static void createInstance() {
		if (instance == null) {
			synchronized (BenefitFactory.class) {
				if (instance == null) {
					instance = new BenefitFactory();
				}
			}
		}
	}

	public static BenefitFactory getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}

	@Override
	protected ITranslator getTranslator() {
		return TRANSLATOR_BENEFITS;
	}

	@Override
	protected Benefit createElement(ITranslator translator, String benefitId, String language) throws InvalidXmlElementException {
		try {
			String name = translator.getNodeValue(benefitId, NAME, language);
			String cost = translator.getNodeValue(benefitId, COST);

			try {
				Benefit benefit = new Benefit(benefitId, name, Integer.parseInt(cost));
				return benefit;
			} catch (Exception e) {
				throw new InvalidBlessingException("Invalid cost in benefit '" + benefitId + "'.");
			}
		} catch (Exception e) {
			throw new InvalidBlessingException("Invalid name in benefit '" + benefitId + "'.");
		}
	}

}
