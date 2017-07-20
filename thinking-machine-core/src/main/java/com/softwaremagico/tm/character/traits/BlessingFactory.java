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

public class BlessingFactory extends XmlFactory<Blessing> {
	private final static String NAME = "name";
	private final static String COST = "cost";
	private final static String BONIFICATION = "bonification";
	private final static String TRAIT = "trait";
	private final static String SITUATION = "situation";

	private static ITranslator translatorBlessings = LanguagePool.getTranslator("blessings.xml");

	private static BlessingFactory instance;

	private static void createInstance() {
		if (instance == null) {
			synchronized (BlessingFactory.class) {
				if (instance == null) {
					instance = new BlessingFactory();
				}
			}
		}
	}

	public static BlessingFactory getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}

	@Override
	protected Blessing createElement(ITranslator translator, String blessingId, String language) throws InvalidXmlElementException {
		try {
			String name = translator.getNodeValue(blessingId, NAME, language);
			Blessing blessing = new Blessing(name);
			try {
				String cost = translator.getNodeValue(blessingId, COST);
				blessing.setCost(Integer.parseInt(cost));
			} catch (Exception e) {
				throw new InvalidBlessingException("Invalid cost in blessing '" + blessingId + "'.");
			}
			try {
				String bonification = translator.getNodeValue(blessingId, BONIFICATION);
				blessing.setBonification(Integer.parseInt(bonification));
			} catch (Exception e) {
				throw new InvalidBlessingException("Invalid bonification in blessing '" + blessingId + "'.");
			}
			try {
				String trait = translator.getNodeValue(blessingId, TRAIT);
				blessing.setTrait(trait);
			} catch (Exception e) {
				throw new InvalidBlessingException("Invalid bonification in blessing '" + blessingId + "'.");
			}
			try {
				String situation = translator.getNodeValue(blessingId, SITUATION);
				blessing.setTrait(situation);
			} catch (Exception e) {
				throw new InvalidBlessingException("Invalid bonification in blessing '" + blessingId + "'.");
			}
			return blessing;
		} catch (Exception e) {
			throw new InvalidBlessingException("Invalid name in blessing '" + blessingId + "'.");
		}
	}

	@Override
	protected ITranslator getTranslator() {
		return translatorBlessings;
	}
}
