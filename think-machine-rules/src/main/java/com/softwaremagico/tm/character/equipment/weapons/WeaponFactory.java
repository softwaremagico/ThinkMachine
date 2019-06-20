package com.softwaremagico.tm.character.equipment.weapons;

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

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.equipment.DamageType;
import com.softwaremagico.tm.character.equipment.DamageTypeFactory;
import com.softwaremagico.tm.character.equipment.Size;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;

public class WeaponFactory extends XmlFactory<Weapon> {
	private static final ITranslator translatorWeapon = LanguagePool.getTranslator("weapons.xml");

	private static final String NAME = "name";
	private static final String CHARACTERISTIC = "characteristic";
	private static final String SKILL = "skill";
	private static final String TECH_LEVEL = "techLevel";
	private static final String TECH_LEVEL_SPECIAL = "techLevelSpecial";
	private static final String GOAL = "goal";
	private static final String DAMAGE = "damage";
	private static final String STRENGTH = "strength";
	private static final String RANGE = "range";
	private static final String SHOTS = "shots";
	private static final String RATE = "rate";
	private static final String SIZE = "size";
	private static final String COST = "cost";

	private static final String TYPE = "type";
	private static final String SPECIAL = "special";
	private static final String DAMAGE_TYPE = "damageType";

	private static final String AMMUNITION = "ammunition";
	private static final String ACCESSORIES = "others";

	private static class WeaponFactoryInit {
		public static final WeaponFactory INSTANCE = new WeaponFactory();
	}

	public static WeaponFactory getInstance() {
		return WeaponFactoryInit.INSTANCE;
	}

	@Override
	protected ITranslator getTranslator() {
		return translatorWeapon;
	}

	@Override
	protected Weapon createElement(ITranslator translator, String weaponId, String language)
			throws InvalidXmlElementException {
		Weapon weapon = null;
		String name = null;
		try {
			name = translator.getNodeValue(weaponId, NAME, language);
		} catch (Exception e) {
			throw new InvalidWeaponException("Invalid name in weapon '" + weaponId + "'.");
		}

		CharacteristicDefinition characteristicDefintion = null;
		try {
			final String characteristicName = translator.getNodeValue(weaponId, CHARACTERISTIC);
			characteristicDefintion = CharacteristicsDefinitionFactory.getInstance().getElement(characteristicName,
					language);

		} catch (Exception e) {
			throw new InvalidWeaponException("Invalid characteristic name in weapon '" + weaponId + "'.");
		}

		AvailableSkill skill = null;
		try {
			final String skillName = translator.getNodeValue(weaponId, SKILL);
			skill = AvailableSkillsFactory.getInstance().getElement(skillName, language);
		} catch (Exception e) {
			throw new InvalidWeaponException("Invalid skill name in weapon '" + weaponId + "'.");
		}

		int techLevel = 0;
		try {
			final String techLevelName = translator.getNodeValue(weaponId, TECH_LEVEL);
			techLevel = Integer.parseInt(techLevelName);
		} catch (Exception e) {
			throw new InvalidWeaponException("Invalid tech level in weapon '" + weaponId + "'.");
		}

		boolean techLevelSpecial = false;
		try {
			final String techLevelSpecialValue = translator.getNodeValue(weaponId, TECH_LEVEL_SPECIAL);
			techLevelSpecial = Boolean.parseBoolean(techLevelSpecialValue);
		} catch (Exception e) {
			throw new InvalidWeaponException("Invalid tech level special in weapon '" + weaponId + "'.");
		}

		String goal = "";
		try {
			goal = translator.getNodeValue(weaponId, GOAL);
		} catch (Exception e) {
			// Not mandatory
		}

		String damage = "";
		try {
			damage = translator.getNodeValue(weaponId, DAMAGE);
		} catch (Exception e) {
			throw new InvalidWeaponException("Invalid damage value in weapon '" + weaponId + "'.");
		}

		int strength = 0;
		try {
			final String strengthValue = translator.getNodeValue(weaponId, STRENGTH);
			strength = Integer.parseInt(strengthValue);
		} catch (Exception e) {
			throw new InvalidWeaponException("Invalid strength value in weapon '" + weaponId + "'.");
		}

		String range = null;
		try {
			range = translator.getNodeValue(weaponId, RANGE);
		} catch (Exception e) {
			// Not mandatory.
		}

		Integer shots = null;
		try {
			final String shotsValue = translator.getNodeValue(weaponId, SHOTS);
			shots = Integer.parseInt(shotsValue);
		} catch (Exception e) {
			// Not mandatory.
		}

		String rate = "";
		try {
			rate = translator.getNodeValue(weaponId, RATE);
		} catch (Exception e) {
			// Not mandatory.
		}

		Size size = Size.M;
		try {
			size = Size.get(translator.getNodeValue(weaponId, SIZE));
		} catch (Exception e) {
			throw new InvalidWeaponException("Invalid size value in weapon '" + weaponId + "'.");
		}

		float cost = 0;
		try {
			final String costValue = translator.getNodeValue(weaponId, COST);
			cost = Float.parseFloat(costValue);
		} catch (Exception e) {
			throw new InvalidWeaponException("Invalid cost value in weapon '" + weaponId + "'.");
		}

		String special = "";
		try {
			special = translator.getNodeValue(weaponId, SPECIAL);
		} catch (Exception e) {
			// Not mandatory.
		}

		WeaponType type = null;
		try {
			type = WeaponType.get(translator.getNodeValue(weaponId, TYPE));
			if (type == null) {
				throw new Exception();
			}
		} catch (Exception e) {
			throw new InvalidWeaponException("Invalid type value in weapon '" + weaponId + "'.");
		}

		final Set<DamageType> damageOfWeapon = new HashSet<>();
		try {
			final String damageDefinition = translator.getNodeValue(weaponId, DAMAGE_TYPE);
			if (damageDefinition != null) {
				final StringTokenizer damageTypesTokenizer = new StringTokenizer(damageDefinition, ",");
				while (damageTypesTokenizer.hasMoreTokens()) {
					try {
						damageOfWeapon.add(DamageTypeFactory.getInstance().getElement(
								damageTypesTokenizer.nextToken().trim(), language));
					} catch (InvalidXmlElementException e) {
						throw new InvalidWeaponException("Invalid damage type in weapon '" + weaponId + "'.", e);
					}
				}
			}
		} catch (NullPointerException e) {
			// Not mandatory.
		}

		final Set<Ammunition> ammunitions = new HashSet<>();
		final String ammunitionsNames = translator.getNodeValue(weaponId, AMMUNITION);
		if (ammunitionsNames != null) {
			final StringTokenizer ammunitionTokenizer = new StringTokenizer(ammunitionsNames, ",");
			while (ammunitionTokenizer.hasMoreTokens()) {
				try {
					ammunitions.add(AmmunitionFactory.getInstance().getElement(ammunitionTokenizer.nextToken().trim(),
							language));
				} catch (InvalidXmlElementException ixe) {
					throw new InvalidWeaponException("Error in ammunitions '" + ammunitionsNames
							+ "' structure. Invalid ammunition definition. ", ixe);
				}
			}
		}

		final Set<Accessory> accessories = new HashSet<>();
		final String accesoriesNames = translator.getNodeValue(weaponId, ACCESSORIES);
		if (accesoriesNames != null) {
			final StringTokenizer accessoryTokenizer = new StringTokenizer(accesoriesNames, ",");
			while (accessoryTokenizer.hasMoreTokens()) {
				try {
					accessories.add(AccessoryFactory.getInstance().getElement(accessoryTokenizer.nextToken().trim(),
							language));
				} catch (InvalidXmlElementException ixe) {
					throw new InvalidWeaponException("Error in accessories '" + accesoriesNames
							+ "' structure in weapon '" + weaponId + "'. Invalid accessory definition. ", ixe);
				}
			}
		}

		weapon = new Weapon(weaponId, name, language, type, goal, characteristicDefintion, skill, damage, strength,
				range, shots, rate, techLevel, techLevelSpecial, size, special, damageOfWeapon, cost, ammunitions,
				accessories);

		return weapon;
	}
}
