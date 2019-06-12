package com.softwaremagico.tm.character.equipment.shields;

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
	private static final ITranslator translatorWeapon = LanguagePool.getTranslator("shields.xml");

	private static final String NAME = "name";

	private static final String TECH = "techLevel";
	private static final String IMPACT = "impact";
	private static final String FORCE = "force";
	private static final String HITS = "hits";
	private static final String COST = "cost";

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
	protected Shield createElement(ITranslator translator, String shieldId, String language)
			throws InvalidXmlElementException {
		Shield shield = null;
		String name = null;
		try {
			name = translator.getNodeValue(shieldId, NAME, language);
		} catch (Exception e) {
			throw new InvalidShieldException("Invalid name in shield '" + shieldId + "'.");
		}

		int techLevel = 0;
		try {
			final String techValue = translator.getNodeValue(shieldId, TECH);
			techLevel = Integer.parseInt(techValue);
		} catch (Exception e) {
			throw new InvalidShieldException("Invalid tech value in shield '" + shieldId + "'.");
		}

		int impact = 0;
		try {
			final String impactValue = translator.getNodeValue(shieldId, IMPACT);
			impact = Integer.parseInt(impactValue);
		} catch (Exception e) {
			throw new InvalidShieldException("Invalid impact value in shield '" + shieldId + "'.");
		}

		int force = 0;
		try {
			final String forceValue = translator.getNodeValue(shieldId, FORCE);
			force = Integer.parseInt(forceValue);
		} catch (Exception e) {
			throw new InvalidShieldException("Invalid force value in shield '" + shieldId + "'.");
		}

		int hits = 0;
		try {
			final String hitsValue = translator.getNodeValue(shieldId, HITS);
			hits = Integer.parseInt(hitsValue);
		} catch (Exception e) {
			throw new InvalidShieldException("Invalid hits value in shield '" + shieldId + "'.");
		}

		float cost = 0;
		try {
			final String costValue = translator.getNodeValue(shieldId, COST);
			cost = Float.parseFloat(costValue);
		} catch (Exception e) {
			throw new InvalidShieldException("Invalid cost value in shield '" + shieldId + "'.");
		}

		shield = new Shield(shieldId, name, language, techLevel, impact, force, hits, cost);

		return shield;
	}
}
