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

import java.util.HashSet;
import java.util.Set;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.equipment.DamageType;
import com.softwaremagico.tm.character.equipment.weapons.Accessory;
import com.softwaremagico.tm.character.equipment.weapons.Ammunition;
import com.softwaremagico.tm.character.equipment.weapons.InvalidWeaponException;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.skills.SkillDefinition;
import com.softwaremagico.tm.character.skills.SkillsDefinitionsFactory;
import com.softwaremagico.tm.character.values.Bonification;
import com.softwaremagico.tm.character.values.IValue;
import com.softwaremagico.tm.character.values.SpecialValue;
import com.softwaremagico.tm.character.values.StaticValue;
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
	private final static String POWER = "power";
	private final static String COST = "cost";
	private final static String PROSCRIBED = "proscribed";

	private final static String BONIFICATION = "bonification";
	private final static String VALUE = "value";
	private final static String AFFECTS = "affects";
	private final static String SITUATION = "situation";

	private final static String SKILL_VALUE = "skillValue";

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
				throw new InvalidWeaponException("Invalid tech level in cybernetic device '" + cyberneticDeviceId + "'.");
			}

			int points = 0;
			try {
				String pointsName = translator.getNodeValue(cyberneticDeviceId, POINTS);
				points = Integer.parseInt(pointsName);
			} catch (Exception e) {
				throw new InvalidWeaponException("Invalid points value in cybernetic device '" + cyberneticDeviceId + "'.");
			}

			int cost = 0;
			try {
				String costName = translator.getNodeValue(cyberneticDeviceId, COST);
				cost = Integer.parseInt(costName);
			} catch (Exception e) {
				throw new InvalidWeaponException("Invalid cost in cybernetic device '" + cyberneticDeviceId + "'.");
			}

			int incompatibility = 0;
			try {
				String incompatibilityName = translator.getNodeValue(cyberneticDeviceId, INCOMPATIBILITY);
				incompatibility = Integer.parseInt(incompatibilityName);
			} catch (Exception e) {
				throw new InvalidWeaponException("Invalid incompatibility value in cybernetic device '" + cyberneticDeviceId + "'.");
			}

			CyberneticDeviceTrait attached = null;
			try {
				attached = CyberneticDeviceTraitFactory.getInstance().getElement(translator.getNodeValue(cyberneticDeviceId, ATTACHED), language);
			} catch (NullPointerException npoe) {
				// Not mandatory
			}

			CyberneticDeviceTrait material = null;
			try {
				material = CyberneticDeviceTraitFactory.getInstance().getElement(translator.getNodeValue(cyberneticDeviceId, MATERIAL), language);
			} catch (NullPointerException npoe) {
				// Not mandatory
			}

			CyberneticDeviceTrait visibility = null;
			try {
				visibility = CyberneticDeviceTraitFactory.getInstance().getElement(translator.getNodeValue(cyberneticDeviceId, VISIBILITY), language);
			} catch (NullPointerException npoe) {
				// Not mandatory
			}

			CyberneticDeviceTrait usability = null;
			try {
				usability = CyberneticDeviceTraitFactory.getInstance().getElement(translator.getNodeValue(cyberneticDeviceId, USABILITY), language);
			} catch (NullPointerException npoe) {
				// Not mandatory
			}

			CyberneticDeviceTrait quality = null;
			try {
				quality = CyberneticDeviceTraitFactory.getInstance().getElement(translator.getNodeValue(cyberneticDeviceId, QUALITY), language);
			} catch (NullPointerException npoe) {
				// Not mandatory
			}

			CyberneticDeviceTrait power = null;
			try {
				power = CyberneticDeviceTraitFactory.getInstance().getElement(translator.getNodeValue(cyberneticDeviceId, POWER), language);
			} catch (NullPointerException npoe) {
				// Not mandatory
			}

			boolean proscribed = false;
			try {
				proscribed = Boolean.parseBoolean(translator.getNodeValue(cyberneticDeviceId, PROSCRIBED));
			} catch (NullPointerException npoe) {
				// Not mandatory
			}

			String requires = null;
			try {
				requires = translator.getNodeValue(cyberneticDeviceId, REQUIRES);
			} catch (NullPointerException npoe) {
				// Not mandatory
			}

			Set<Bonification> bonifications = new HashSet<>();
			int node = 0;
			while (true) {
				try {
					String bonificationValue = translator.getNodeValue(cyberneticDeviceId, BONIFICATION, VALUE, node);
					String valueName = translator.getNodeValue(cyberneticDeviceId, BONIFICATION, AFFECTS, node);
					IValue affects = null;
					if (valueName != null) {
						affects = SpecialValue.getValue(valueName, language);
					}
					String situation = translator.getNodeValue(cyberneticDeviceId, SITUATION, language, node);

					Bonification bonification = new Bonification(Integer.parseInt(bonificationValue), affects, situation);
					bonifications.add(bonification);
					node++;
				} catch (Exception e) {
					break;
				}
			}

			Set<StaticValue> staticValues = new HashSet<>();
			node = 0;
			while (true) {
				try {
					String bonificationValue = translator.getNodeValue(cyberneticDeviceId, SKILL_VALUE, VALUE, node);
					String skillName = translator.getNodeValue(cyberneticDeviceId, SKILL_VALUE, AFFECTS, node);
					IValue affects = null;
					if (skillName != null) {
						affects = SpecialValue.getValue(skillName, language);
					}

					StaticValue skillValue = new StaticValue(Integer.parseInt(bonificationValue), affects);
					staticValues.add(skillValue);
					node++;
				} catch (Exception e) {
					break;
				}
			}

			Weapon weapon = null;
			if (translator.existsNode(cyberneticDeviceId, WEAPON)) {
				weapon = getWeapon(translator, cyberneticDeviceId, name, techLevel, language);
			}

			CyberneticDevice cyberneticDevice = new CyberneticDevice(cyberneticDeviceId, name, language, points, incompatibility, cost, techLevel, usability,
					quality, visibility, material, attached, power, proscribed, null, requires, weapon, bonifications, staticValues);

			return cyberneticDevice;
		} catch (Exception e) {
			throw new InvalidCyberneticDeviceException("Invalid cybernetic device definition for '" + cyberneticDeviceId + "'.", e);
		}
	}

	private Weapon getWeapon(ITranslator translator, String cyberneticDeviceId, String name, int techLevel, String language)
			throws InvalidCyberneticDeviceException {
		CharacteristicDefinition characteristicDefintion = null;
		try {
			String characteristicName = translator.getNodeValue(cyberneticDeviceId, WEAPON, CHARACTERISTIC);
			characteristicDefintion = CharacteristicsDefinitionFactory.getInstance().getElement(characteristicName, language);
		} catch (Exception e) {
			throw new InvalidCyberneticDeviceException("Invalid characteristic name in weapon of cybernetic '" + cyberneticDeviceId + "'.");
		}

		SkillDefinition skill = null;
		try {
			String skillName = translator.getNodeValue(cyberneticDeviceId, WEAPON, SKILL);
			skill = SkillsDefinitionsFactory.getInstance().getElement(skillName, language);
		} catch (Exception e) {
			throw new InvalidCyberneticDeviceException("Invalid skill name in weapon of cybernetic '" + cyberneticDeviceId + "'.");
		}

		String goal = "";
		try {
			goal = translator.getNodeValue(cyberneticDeviceId, WEAPON, GOAL);
		} catch (Exception e) {
			// Not mandatory
		}

		String damage = "";
		try {
			damage = translator.getNodeValue(cyberneticDeviceId, WEAPON, DAMAGE);
		} catch (Exception e) {
			throw new InvalidCyberneticDeviceException("Invalid weapon damage value in cybernetic '" + cyberneticDeviceId + "'.");
		}

		String range = null;
		try {
			range = translator.getNodeValue(cyberneticDeviceId, WEAPON, RANGE);
		} catch (Exception e) {
			// Not mandatory.
		}

		Integer shots = null;
		try {
			String shotsValue = translator.getNodeValue(cyberneticDeviceId, WEAPON, SHOTS);
			shots = Integer.parseInt(shotsValue);
		} catch (Exception e) {
			// Not mandatory.
		}

		String rate = "";
		try {
			rate = translator.getNodeValue(cyberneticDeviceId, WEAPON, RATE);
		} catch (Exception e) {
			// Not mandatory.
		}

		return new Weapon(cyberneticDeviceId, name, language, null, goal, characteristicDefintion, skill, damage, 0, range, shots, rate, techLevel, false,
				null, "", new HashSet<DamageType>(), 0, new HashSet<Ammunition>(), new HashSet<Accessory>());

	}
}
