package com.softwaremagico.tm.character.equipment.shield;

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

public class ShieldFactory extends XmlFactory<Shield> {
	private final static ITranslator translatorWeapon = LanguagePool.getTranslator("shields.xml");

	private final static String NAME = "name";

	private final static String TECH = "techLevel";
	private final static String IMPACT = "impact";
	private final static String FORCE = "force";
	private final static String HITS = "hits";
	private final static String COST = "cost";

	private static ShieldFactory instance;

	private static void createInstance() {
		if (instance == null) {
			synchronized (ShieldFactory.class) {
				if (instance == null) {
					instance = new ShieldFactory();
				}
			}
		}
	}

	public static ShieldFactory getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}

	@Override
	protected ITranslator getTranslator() {
		return translatorWeapon;
	}

	@Override
	protected Shield createElement(ITranslator translator, String shieldId, String language) throws InvalidXmlElementException {
		Shield shield = null;
		String name = null;
		try {
			name = translator.getNodeValue(shieldId, NAME, language);
		} catch (Exception e) {
			throw new InvalidShieldException("Invalid name in shield '" + shieldId + "'.");
		}

		int techLevel = 0;
		try {
			String techValue = translator.getNodeValue(shieldId, TECH);
			techLevel = Integer.parseInt(techValue);
		} catch (Exception e) {
			throw new InvalidShieldException("Invalid tech value in shield '" + shieldId + "'.");
		}

		int impact = 0;
		try {
			String impactValue = translator.getNodeValue(shieldId, IMPACT);
			impact = Integer.parseInt(impactValue);
		} catch (Exception e) {
			throw new InvalidShieldException("Invalid impact value in shield '" + shieldId + "'.");
		}

		int force = 0;
		try {
			String forceValue = translator.getNodeValue(shieldId, FORCE);
			force = Integer.parseInt(forceValue);
		} catch (Exception e) {
			throw new InvalidShieldException("Invalid force value in shield '" + shieldId + "'.");
		}

		int hits = 0;
		try {
			String hitsValue = translator.getNodeValue(shieldId, HITS);
			hits = Integer.parseInt(hitsValue);
		} catch (Exception e) {
			throw new InvalidShieldException("Invalid hits value in shield '" + shieldId + "'.");
		}

		float cost = 0;
		try {
			String costValue = translator.getNodeValue(shieldId, COST);
			cost = Float.parseFloat(costValue);
		} catch (Exception e) {
			throw new InvalidShieldException("Invalid cost value in shield '" + shieldId + "'.");
		}

		shield = new Shield(shieldId, name, language, techLevel, impact, force, hits, cost);

		return shield;
	}
}
