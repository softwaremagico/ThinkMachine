package com.softwaremagico.tm.character.cybernetics;

/*-
 * #%L
 * Think Machine (Core)
 * %%
 * Copyright (C) 2018 Softwaremagico
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

public class CyberneticDeviceTraitFactory extends XmlFactory<CyberneticDeviceTrait> {
	private final static ITranslator translatorCybernetics = LanguagePool.getTranslator("cybernetics_traits.xml");

	private final static String NAME = "name";
	private final static String CATEGORY = "category";

	private final static String MIN_TECH_LEVEL = "minTechLevel";
	private final static String EXTRA_POINTS = "extraPoints";
	private final static String EXTRA_COST = "extraCostMultiplier";
	private final static String EXTRA_INCOMPATIBILITY = "extraIncompatibility";

	private static CyberneticDeviceTraitFactory instance;

	private static void createInstance() {
		if (instance == null) {
			synchronized (CyberneticDeviceTraitFactory.class) {
				if (instance == null) {
					instance = new CyberneticDeviceTraitFactory();
				}
			}
		}
	}

	public static CyberneticDeviceTraitFactory getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}

	@Override
	public void clearCache() {
		super.clearCache();
	}

	@Override
	protected ITranslator getTranslator() {
		return translatorCybernetics;
	}

	@Override
	protected CyberneticDeviceTrait createElement(ITranslator translator, String cyberneticDeviceTraitId, String language) throws InvalidXmlElementException {
		try {
			String name = translator.getNodeValue(cyberneticDeviceTraitId, NAME, language);

			String categoryName = translator.getNodeValue(cyberneticDeviceTraitId, CATEGORY);
			CyberneticDeviceTraitCategory cyberneticDeviceTraitCategory = CyberneticDeviceTraitCategory.get(categoryName);
			if (cyberneticDeviceTraitCategory == null) {
				throw new InvalidCyberneticDeviceTraitException("Invalid category definition for '" + cyberneticDeviceTraitId + "'.");
			}

			int extraPoints = 0;
			try {
				String pointsName = translator.getNodeValue(cyberneticDeviceTraitId, EXTRA_POINTS);
				extraPoints = Integer.parseInt(pointsName);
			} catch (Exception e) {
				throw new InvalidCyberneticDeviceTraitException("Invalid cost in cybernetic trait '" + cyberneticDeviceTraitId + "'.");
			}

			int minimumTechLevel = 0;
			try {
				String techLevelName = translator.getNodeValue(cyberneticDeviceTraitId, MIN_TECH_LEVEL);
				minimumTechLevel = Integer.parseInt(techLevelName);
			} catch (Exception e) {
				throw new InvalidCyberneticDeviceTraitException("Invalid minimum tech level in cybernetic trait '" + cyberneticDeviceTraitId + "'.");
			}

			int extraIncompatibility = 0;
			try {
				String incompatibilityName = translator.getNodeValue(cyberneticDeviceTraitId, EXTRA_INCOMPATIBILITY);
				extraIncompatibility = Integer.parseInt(incompatibilityName);
			} catch (Exception e) {
				throw new InvalidCyberneticDeviceTraitException("Invalid extra incomptability in cybernetic trait '" + cyberneticDeviceTraitId + "'.");
			}

			int extraCost = 0;
			try {
				String extraCostName = translator.getNodeValue(cyberneticDeviceTraitId, EXTRA_INCOMPATIBILITY);
				extraCost = Integer.parseInt(extraCostName);
			} catch (NullPointerException npe) {
				// Not mandatory
			} catch (NumberFormatException e) {
				throw new InvalidCyberneticDeviceTraitException("Invalid extra cost in cybernetic trait '" + cyberneticDeviceTraitId + "'.");
			}

			float extraCostMultiplier = 1;
			try {
				String costName = translator.getNodeValue(cyberneticDeviceTraitId, EXTRA_COST);
				extraCostMultiplier = Float.parseFloat(costName);
			} catch (NullPointerException npe) {
				// Not mandatory
			} catch (NumberFormatException e) {
				throw new InvalidCyberneticDeviceTraitException("Invalid cost in cybernetic trait '" + cyberneticDeviceTraitId + "'.");
			}

			return new CyberneticDeviceTrait(cyberneticDeviceTraitId, name, language, cyberneticDeviceTraitCategory, minimumTechLevel, extraPoints, extraCost,
					extraCostMultiplier, extraIncompatibility);
		} catch (Exception e) {
			throw new InvalidCyberneticDeviceTraitException("Invalid cybernetic trait definition for '" + cyberneticDeviceTraitId + "'.", e);
		}
	}
}
