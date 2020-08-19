package com.softwaremagico.tm.character.cybernetics;

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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.softwaremagico.tm.ElementClassification;
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
import com.softwaremagico.tm.log.MachineXmlReaderLog;
import com.softwaremagico.tm.log.SuppressFBWarnings;

public class CyberneticDeviceFactory extends XmlFactory<CyberneticDevice> {
	private static final String TRANSLATOR_FILE = "cybernetics.xml";

	private static final String NAME = "name";
	private static final String TECH_LEVEL = "techLevel";
	private static final String POINTS = "points";

	private static final String COST = "cost";
	private static final String INCOMPATIBILITY = "incompatibility";
	private static final String TRAITS = "traits";

	private static final String BONIFICATION = "bonification";
	private static final String VALUE = "value";
	private static final String AFFECTS = "affects";
	private static final String SITUATION = "situation";

	private static final String SKILL_STATIC_VALUE = "skillsStaticValues";
	private static final String SKILL_VALUE = "skillValue";
	private static final String SKILL_SPECIALITY = "speciality";

	private static final String REQUIRES = "requires";

	private static final String WEAPON = "weapon";
	private static final String CHARACTERISTIC = "characteristic";
	private static final String SKILL = "skill";
	private static final String GOAL = "goal";
	private static final String DAMAGE = "damage";
	private static final String RANGE = "range";
	private static final String SHOTS = "shots";
	private static final String RATE = "rate";

	private static final String CLASSIFICATION = "classification";

	private Map<CyberneticDevice, Set<CyberneticDevice>> requiredBy;

	private static class CyberneticDeviceFactoryInit {
		public static final CyberneticDeviceFactory INSTANCE = new CyberneticDeviceFactory();
	}

	public static CyberneticDeviceFactory getInstance() {
		return CyberneticDeviceFactoryInit.INSTANCE;
	}

	@Override
	public void clearCache() {
		requiredBy = null;
		super.clearCache();
	}

	@Override
	protected String getTranslatorFile() {
		return TRANSLATOR_FILE;
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

	private void initializeRequirements(String language, String moduleName) {
		requiredBy = new HashMap<>();
		try {
			for (final CyberneticDevice device : getElements(language, moduleName)) {
				addRequirement(device);
			}
		} catch (InvalidXmlElementException e) {
			MachineXmlReaderLog.errorMessage(this.getClass().getName(), e);
		}
	}

	public Set<CyberneticDevice> getDevicesThatRequires(CyberneticDevice device, String language, String moduleName) {
		if (requiredBy == null) {
			initializeRequirements(language, moduleName);
		}

		final Set<CyberneticDevice> requiredDevice = new HashSet<>();
		if (requiredBy.get(device) != null) {
			for (final CyberneticDevice requirement : requiredBy.get(device)) {
				requiredDevice.add(requirement);
			}
		}
		return requiredDevice;
	}

	@Override
	@SuppressFBWarnings("REC_CATCH_EXCEPTION")
	protected CyberneticDevice createElement(ITranslator translator, String cyberneticDeviceId, String language,
			String moduleName) throws InvalidXmlElementException {
		try {
			final String name = translator.getNodeValue(cyberneticDeviceId, NAME, language);

			int techLevel = 0;
			try {
				final String techLevelName = translator.getNodeValue(cyberneticDeviceId, TECH_LEVEL);
				techLevel = Integer.parseInt(techLevelName);
			} catch (Exception e) {
				throw new InvalidCyberneticDeviceException("Invalid tech level in cybernetic device '"
						+ cyberneticDeviceId + "'.");
			}

			int points = 0;
			try {
				final String pointsName = translator.getNodeValue(cyberneticDeviceId, POINTS);
				points = Integer.parseInt(pointsName);
			} catch (Exception e) {
				throw new InvalidCyberneticDeviceException("Invalid points value in cybernetic device '"
						+ cyberneticDeviceId + "'.");
			}

			int cost = 0;
			try {
				final String costName = translator.getNodeValue(cyberneticDeviceId, COST);
				cost = Integer.parseInt(costName);
			} catch (Exception e) {
				throw new InvalidCyberneticDeviceException("Invalid cost in cybernetic device '" + cyberneticDeviceId
						+ "'.");
			}

			int incompatibility = 0;
			try {
				final String incompatibilityName = translator.getNodeValue(cyberneticDeviceId, INCOMPATIBILITY);
				incompatibility = Integer.parseInt(incompatibilityName);
			} catch (Exception e) {
				throw new InvalidCyberneticDeviceException("Invalid incompatibility value in cybernetic device '"
						+ cyberneticDeviceId + "'.");
			}

			final Set<CyberneticDeviceTrait> traits = getCommaSeparatedValues(cyberneticDeviceId, TRAITS, language,
					moduleName, CyberneticDeviceTraitFactory.getInstance());

			String requires = null;
			try {
				requires = translator.getNodeValue(cyberneticDeviceId, REQUIRES);
			} catch (NullPointerException npoe) {
				// Not mandatory
			}

			final Set<Bonification> bonifications = new HashSet<>();
			int node = 0;
			while (true) {
				try {
					final String bonificationValue = translator.getNodeValue(cyberneticDeviceId, BONIFICATION, VALUE,
							node);
					final String valueName = translator.getNodeValue(cyberneticDeviceId, BONIFICATION, AFFECTS, node);
					IValue affects = null;
					if (valueName != null) {
						affects = SpecialValue.getValue(valueName, language, moduleName);
					}
					final String situation = translator.getNodeValue(cyberneticDeviceId, SITUATION, language, node);

					final Bonification bonification = new Bonification(Integer.parseInt(bonificationValue), affects,
							situation);
					bonifications.add(bonification);
					node++;
				} catch (Exception e) {
					break;
				}
			}

			final Set<StaticValue> staticValues = new HashSet<>();
			node = 0;
			while (true) {
				try {
					final String bonificationValue = translator.getNodeValue(cyberneticDeviceId, SKILL_STATIC_VALUE,
							SKILL_VALUE, VALUE, node);
					final String skillName = translator.getNodeValue(cyberneticDeviceId, SKILL_STATIC_VALUE,
							SKILL_VALUE, AFFECTS, node);
					String skillSpeciality = null;
					try {
						skillSpeciality = translator.getNodeValue(cyberneticDeviceId, SKILL_STATIC_VALUE, SKILL_VALUE,
								SKILL_SPECIALITY, node);
					} catch (NullPointerException e) {
						// Not mandatory
					}
					IValue affects = null;
					if (skillName != null) {
						try {
							if (skillSpeciality == null) {
								affects = AvailableSkillsFactory.getInstance().getElement(skillName, language,
										moduleName);
							} else {
								affects = AvailableSkillsFactory.getInstance().getElement(skillName, skillSpeciality,
										language, moduleName);
							}
						} catch (InvalidXmlElementException e) {
							throw new InvalidCyberneticDeviceException("Skill value number '" + node
									+ "' invalid for cybernetic device '" + cyberneticDeviceId + "'.");
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
				weapon = getWeapon(translator, cyberneticDeviceId, name, techLevel, language, moduleName);
			}

			ElementClassification cyberneticClassification = ElementClassification.ENHANCEMENT;
			try {
				cyberneticClassification = ElementClassification.get(translator.getNodeValue(cyberneticDeviceId,
						CLASSIFICATION));
			} catch (Exception e) {
				throw new InvalidCyberneticDeviceException("Invalid classification value in cybernetic '"
						+ cyberneticDeviceId + "'.");
			}

			final CyberneticDevice cyberneticDevice = new CyberneticDevice(cyberneticDeviceId, name, language,
					moduleName, points, incompatibility, cost, techLevel, requires, weapon, traits, bonifications,
					staticValues, cyberneticClassification);
			return cyberneticDevice;
		} catch (Exception e) {
			throw new InvalidCyberneticDeviceException("Invalid cybernetic device definition for '"
					+ cyberneticDeviceId + "'.", e);
		}
	}

	private Weapon getWeapon(ITranslator translator, String cyberneticDeviceId, String name, int techLevel,
			String language, String moduleName) throws InvalidCyberneticDeviceException {
		CharacteristicDefinition characteristicDefintion = null;
		try {
			final String characteristicName = translator.getNodeValue(cyberneticDeviceId, WEAPON, CHARACTERISTIC);
			characteristicDefintion = CharacteristicsDefinitionFactory.getInstance().getElement(characteristicName,
					language, moduleName);
		} catch (Exception e) {
			throw new InvalidCyberneticDeviceException("Invalid characteristic name in weapon of cybernetic '"
					+ cyberneticDeviceId + "'.");
		}

		AvailableSkill skill = null;
		try {
			final String skillName = translator.getNodeValue(cyberneticDeviceId, WEAPON, SKILL);
			skill = AvailableSkillsFactory.getInstance().getElement(skillName, language, moduleName);
		} catch (Exception e) {
			throw new InvalidCyberneticDeviceException("Invalid skill name in weapon of cybernetic '"
					+ cyberneticDeviceId + "'.");
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
			throw new InvalidCyberneticDeviceException("Invalid weapon damage value in cybernetic '"
					+ cyberneticDeviceId + "'.");
		}

		String range = null;
		try {
			range = translator.getNodeValue(cyberneticDeviceId, WEAPON, RANGE);
		} catch (Exception e) {
			// Not mandatory.
		}

		Integer shots = null;
		try {
			final String shotsValue = translator.getNodeValue(cyberneticDeviceId, WEAPON, SHOTS);
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

		return new Weapon(cyberneticDeviceId, name, language, moduleName, null, goal, characteristicDefintion, skill,
				damage, 0, range, shots, rate, techLevel, false, null, "", new HashSet<DamageType>(), 0,
				new HashSet<Ammunition>(), new HashSet<Accessory>());

	}
}
