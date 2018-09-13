package com.softwaremagico.tm.character.equipment;

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
import com.softwaremagico.tm.character.skills.SkillsDefinitionsFactory;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;

public class WeaponFactory extends XmlFactory<Weapon> {
	private final static ITranslator translatorWeapon = LanguagePool.getTranslator("weapons.xml");

	private final static String NAME = "name";
	private final static String CHARACTERISTIC = "characteristic";
	private final static String SKILL = "skill";
	private final static String TECH_LEVEL = "techLevel";
	private final static String GOAL = "goal";
	private final static String DAMAGE = "damage";
	private final static String STRENGTH = "strength";
	private final static String RANGE = "range";
	private final static String SHOTS = "shots";
	private final static String RATE = "rate";
	private final static String SIZE = "size";
	private final static String COST = "cost";
	private final static String SPECIAL = "special";
	private final static String DAMAGE_TYPE = "damageType";

	private static WeaponFactory instance;

	private static void createInstance() {
		if (instance == null) {
			synchronized (WeaponFactory.class) {
				if (instance == null) {
					instance = new WeaponFactory();
				}
			}
		}
	}

	public static WeaponFactory getInstance() {
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
	protected Weapon createElement(ITranslator translator, String weaponId, String language) throws InvalidXmlElementException {
		Weapon weapon = null;
		try {
			String name = translator.getNodeValue(weaponId, NAME, language);
			weapon = new Weapon(weaponId, name);
		} catch (Exception e) {
			throw new InvalidWeaponException("Invalid name in weapon '" + weaponId + "'.");
		}

		try {
			String characteristicName = translator.getNodeValue(weaponId, CHARACTERISTIC);
			CharacteristicDefinition characteristicDefintion = CharacteristicsDefinitionFactory.getInstance().getElement(characteristicName, language);
			weapon.setCharacteristic(characteristicDefintion);
		} catch (Exception e) {
			throw new InvalidWeaponException("Invalid characteristic name in weapon '" + weaponId + "'.");
		}

		try {
			String skillName = translator.getNodeValue(weaponId, SKILL);
			weapon.setSkill(SkillsDefinitionsFactory.getInstance().getElement(skillName, language));
		} catch (Exception e) {
			throw new InvalidWeaponException("Invalid skill name in weapon '" + weaponId + "'.");
		}

		try {
			String techLevel = translator.getNodeValue(weaponId, TECH_LEVEL);
			weapon.setTechLevel(Integer.parseInt(techLevel));
		} catch (Exception e) {
			throw new InvalidWeaponException("Invalid tech level in weapon '" + weaponId + "'.");
		}

		try {
			String goal = translator.getNodeValue(weaponId, GOAL);
			weapon.setGoal(goal);
		} catch (Exception e) {
			// Not mandatory
		}

		try {
			String damage = translator.getNodeValue(weaponId, DAMAGE);
			weapon.setDamage(damage);
		} catch (Exception e) {
			throw new InvalidWeaponException("Invalid damage value in weapon '" + weaponId + "'.");
		}

		try {
			String strength = translator.getNodeValue(weaponId, STRENGTH);
			weapon.setStrength(Integer.parseInt(strength));
		} catch (Exception e) {
			throw new InvalidWeaponException("Invalid strength value in weapon '" + weaponId + "'.");
		}

		try {
			String range = translator.getNodeValue(weaponId, RANGE);
			if (range != null) {
				weapon.setRange(range);
			}
		} catch (Exception e) {
			// Not mandatory.
		}

		try {
			String shots = translator.getNodeValue(weaponId, SHOTS);
			weapon.setShots(Integer.parseInt(shots));
		} catch (Exception e) {
			// Not mandatory.
		}

		try {
			String rate = translator.getNodeValue(weaponId, RATE);
			weapon.setRate(rate);
		} catch (Exception e) {
			// Not mandatory.
		}

		try {
			Size size = Size.get(translator.getNodeValue(weaponId, SIZE));
			if (size != null) {
				weapon.setSize(size);
			}
		} catch (Exception e) {
			throw new InvalidWeaponException("Invalid size value in weapon '" + weaponId + "'.");
		}

		try {
			String cost = translator.getNodeValue(weaponId, COST);
			weapon.setCost(Integer.parseInt(cost));
		} catch (Exception e) {
			throw new InvalidWeaponException("Invalid cost value in weapon '" + weaponId + "'.");
		}

		try {
			String special = translator.getNodeValue(weaponId, SPECIAL);
			weapon.setSpecial(special);
		} catch (Exception e) {
			// Not mandatory.
		}

		try {
			String damageDefinition = translator.getNodeValue(weaponId, DAMAGE_TYPE);
			if (damageDefinition != null) {
				StringTokenizer damageTypesTokenizer = new StringTokenizer(damageDefinition, ",");
				Set<DamageType> damageOfWeapon = new HashSet<>();
				while (damageTypesTokenizer.hasMoreTokens()) {
					damageOfWeapon.add(DamageTypeFactory.getInstance().getElement(damageTypesTokenizer.nextToken().trim(), language));
				}
				weapon.setDamageTypes(damageOfWeapon);
			}
		} catch (Exception e) {
			// Not mandatory.
		}

		return weapon;
	}
}
