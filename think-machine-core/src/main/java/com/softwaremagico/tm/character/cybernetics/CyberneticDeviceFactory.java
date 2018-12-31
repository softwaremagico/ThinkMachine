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
import com.softwaremagico.tm.character.equipment.weapons.InvalidWeaponException;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;

public class CyberneticDeviceFactory extends XmlFactory<CyberneticDevice> {
	private final static ITranslator translatorCybernetics = LanguagePool.getTranslator("cybernetics.xml");

	private final static String NAME = "name";
	private final static String TECH_LEVEL = "techLevel";
	private final static String POINTS = "points";
	
	private final static String INCOMPATIBILITY = "incompatibility";
	private final static String ATTACHED = "attached";
	private final static String MATERIAL = "material";
	private final static String VISIBILITY = "visibility";
	private final static String USABILITY = "usability";
	private final static String QUALITY = "quality";
	private final static String ACTIVATION = "activation";
	private final static String POWER = "power";
	
	private final static String COST = "cost";
	private final static String PROSCRIBED = "proscribed";
	private final static String BONIFICATION = "bonification";
	private final static String VALUE = "value";
	private final static String AFFECTS = "affects";
	private final static String REQUIRES = "requires";

	private final static String WEAPON = "weapon";
	private final static String CHARACTERISTIC = "characteristic";
	private final static String SKILL = "skill";
	private final static String GOAL = "goal";
	private final static String DAMAGE = "damage";
	private final static String RANGE = "range";
	private final static String SHOTS = "shots";
	private final static String RATE = "rate";

	private static CyberneticDeviceFactory instance;

	private static void createInstance() {
		if (instance == null) {
			synchronized (CyberneticDeviceFactory.class) {
				if (instance == null) {
					instance = new CyberneticDeviceFactory();
				}
			}
		}
	}

	public static CyberneticDeviceFactory getInstance() {
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
	protected CyberneticDevice createElement(ITranslator translator, String cyberneticDeviceId, String language) throws InvalidXmlElementException {
		try {
			String name = translator.getNodeValue(cyberneticDeviceId, NAME, language);

			int techLevel = 0;
			try {
				String techLevelName = translator.getNodeValue(cyberneticDeviceId, TECH_LEVEL);
				techLevel = Integer.parseInt(techLevelName);
			} catch (Exception e) {
				throw new InvalidWeaponException("Invalid tech level in weapon '" + cyberneticDeviceId + "'.");
			}
			
			int points = 0;
			try {
				String pointsName = translator.getNodeValue(cyberneticDeviceId, POINTS);
				points = Integer.parseInt(pointsName);
			} catch (Exception e) {
				throw new InvalidWeaponException("Invalid points value in weapon '" + cyberneticDeviceId + "'.");
			}
			
			int incompatibility = 0;
			try {
				String incompatibilityName = translator.getNodeValue(cyberneticDeviceId, INCOMPATIBILITY);
				incompatibility = Integer.parseInt(incompatibilityName);
			} catch (Exception e) {
				throw new InvalidWeaponException("Invalid incompatibility value in weapon '" + cyberneticDeviceId + "'.");
			}

			return null;
		} catch (Exception e) {
			throw new InvalidCyberneticDeviceException("Invalid cybernetic device definition for '" + cyberneticDeviceId + "'.", e);
		}
	}
}
