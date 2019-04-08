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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.equipment.DamageType;
import com.softwaremagico.tm.character.equipment.weapons.Accessory;
import com.softwaremagico.tm.character.equipment.weapons.Ammunition;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.character.values.Bonification;
import com.softwaremagico.tm.character.values.IValue;
import com.softwaremagico.tm.character.values.SpecialValue;
import com.softwaremagico.tm.character.values.StaticValue;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.log.MachineLog;

public class CyberneticDeviceFactory extends XmlFactory<CyberneticDevice> {
	private final static ITranslator translatorCybernetics = LanguagePool.getTranslator("cybernetics.xml");

	private final static String NAME = "name";
	private final static String TECH_LEVEL = "techLevel";
	private final static String POINTS = "points";

	private final static String COST = "cost";
	private final static String INCOMPATIBILITY = "incompatibility";
	private final static String TRAITS = "traits";

	private final static String BONIFICATION = "bonification";
	private final static String VALUE = "value";
	private final static String AFFECTS = "affects";
	private final static String SITUATION = "situation";

	private final static String SKILL_STATIC_VALUE = "skillsStaticValues";
	private final static String SKILL_VALUE = "skillValue";
	private final static String SKILL_SPECIALITY = "speciality";

	private final static String REQUIRES = "requires";

	private final static String WEAPON = "weapon";
	private final static String CHARACTERISTIC = "characteristic";
	private final static String SKILL = "skill";
	private final static String GOAL = "goal";
	private final static String DAMAGE = "damage";
	private final static String RANGE = "range";
	private final static String SHOTS = "shots";
	private final static String RATE = "rate";

	private final static String TYPE = "type";

	private Map<CyberneticDevice, Set<CyberneticDevice>> requiredBy;

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
		requiredBy = null;
		super.clearCache();
	}

	@Override
	protected ITranslator getTranslator() {
		return translatorCybernetics;
	}

	private void addRequirement(CyberneticDevice device) {
		if (device == null) {
			return;
		}
		if (requiredBy.get(device.getRequirement()) == null) {
			requiredBy.put(device.getRequirement(), new HashSet<CyberneticDevice>());
		}
		requiredBy.get(device.getRequirement()).add(device);
	}

	private void initializeRequirements(String language) {
		requiredBy = new HashMap<>();
		try {
			for (CyberneticDevice device : getElements(language)) {
				addRequirement(device);
			}
		} catch (InvalidXmlElementException e) {
			MachineLog.errorMessage(this.getClass().getName(), e);
		}
	}

	public Set<CyberneticDevice> getDevicesThatRequires(CyberneticDevice device, String language) {
		if (requiredBy == null) {
			initializeRequirements(language);
		}

		Set<CyberneticDevice> requiredDevice = new HashSet<>();
		if (requiredBy.get(device) != null) {
			for (CyberneticDevice requirement : requiredBy.get(device)) {
				requiredDevice.add(requirement);
			}
		}
		return requiredDevice;
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
				throw new InvalidCyberneticDeviceException("Invalid tech level in cybernetic device '" + cyberneticDeviceId + "'.");
			}

			int points = 0;
			try {
				String pointsName = translator.getNodeValue(cyberneticDeviceId, POINTS);
				points = Integer.parseInt(pointsName);
			} catch (Exception e) {
				throw new InvalidCyberneticDeviceException("Invalid points value in cybernetic device '" + cyberneticDeviceId + "'.");
			}

			int cost = 0;
			try {
				String costName = translator.getNodeValue(cyberneticDeviceId, COST);
				cost = Integer.parseInt(costName);
			} catch (Exception e) {
				throw new InvalidCyberneticDeviceException("Invalid cost in cybernetic device '" + cyberneticDeviceId + "'.");
			}

			int incompatibility = 0;
			try {
				String incompatibilityName = translator.getNodeValue(cyberneticDeviceId, INCOMPATIBILITY);
				incompatibility = Integer.parseInt(incompatibilityName);
			} catch (Exception e) {
				throw new InvalidCyberneticDeviceException("Invalid incompatibility value in cybernetic device '" + cyberneticDeviceId + "'.");
			}

			List<CyberneticDeviceTrait> traits = new ArrayList<>();
			String traitsNames = translator.getNodeValue(cyberneticDeviceId, TRAITS);
			try {
				StringTokenizer traitTokenizer = new StringTokenizer(traitsNames, ",");
				while (traitTokenizer.hasMoreTokens()) {
					String traitName = traitTokenizer.nextToken().trim();
					try {
						traits.add(CyberneticDeviceTraitFactory.getInstance().getElement(traitName, language));
					} catch (InvalidXmlElementException e) {
						throw new InvalidCyberneticDeviceException("Invalid trait '" + traitName + "' for traits '" + traitsNames + "' in cybernetic device '"
								+ cyberneticDeviceId + "'.", e);
					}
				}
			} catch (NullPointerException e) {
				throw new InvalidCyberneticDeviceException("Invalid traits '" + traitsNames + "' in cybernetic device '" + cyberneticDeviceId + "'.", e);
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
					String bonificationValue = translator.getNodeValue(cyberneticDeviceId, SKILL_STATIC_VALUE, SKILL_VALUE, VALUE, node);
					String skillName = translator.getNodeValue(cyberneticDeviceId, SKILL_STATIC_VALUE, SKILL_VALUE, AFFECTS, node);
					String skillSpeciality = null;
					try {
						skillSpeciality = translator.getNodeValue(cyberneticDeviceId, SKILL_STATIC_VALUE, SKILL_VALUE, SKILL_SPECIALITY, node);
					} catch (NullPointerException e) {
						// Not mandatory
					}
					IValue affects = null;
					if (skillName != null) {
						try {
							if (skillSpeciality == null) {
								affects = AvailableSkillsFactory.getInstance().getElement(skillName, language);
							} else {
								affects = AvailableSkillsFactory.getInstance().getElement(skillName, skillSpeciality, language);
							}
						} catch (InvalidXmlElementException e) {
							throw new InvalidCyberneticDeviceException("Skill value number '" + node + "' invalid for cybernetic device '" + cyberneticDeviceId
									+ "'.");
						}
					}
					staticValues.add(new StaticValue(Integer.parseInt(bonificationValue), affects));
					node++;
				} catch (NumberFormatException e) {
					break;
				}
			}

			Weapon weapon = null;
			if (translator.existsNode(cyberneticDeviceId, WEAPON)) {
				weapon = getWeapon(translator, cyberneticDeviceId, name, techLevel, language);
			}

			CyberneticType cyberneticType = CyberneticType.ENHANCEMENT;
			try {
				cyberneticType = CyberneticType.get(translator.getNodeValue(cyberneticDeviceId, TYPE));
			} catch (Exception e) {
				throw new InvalidCyberneticDeviceException("Invalid type value in cybernetic '" + cyberneticDeviceId + "'.");
			}

			CyberneticDevice cyberneticDevice = new CyberneticDevice(cyberneticDeviceId, name, language, points, incompatibility, cost, techLevel, requires,
					weapon, traits, bonifications, staticValues, cyberneticType);
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

		AvailableSkill skill = null;
		try {
			String skillName = translator.getNodeValue(cyberneticDeviceId, WEAPON, SKILL);
			skill = AvailableSkillsFactory.getInstance().getElement(skillName, language);
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
