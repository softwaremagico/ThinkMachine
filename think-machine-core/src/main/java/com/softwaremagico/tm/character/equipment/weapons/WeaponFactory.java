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
import com.softwaremagico.tm.character.equipment.InvalidWeaponException;
import com.softwaremagico.tm.character.equipment.Size;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.skills.SkillDefinition;
import com.softwaremagico.tm.character.skills.SkillsDefinitionsFactory;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;

public class WeaponFactory extends XmlFactory<Weapon> {
	private final static ITranslator translatorWeapon = LanguagePool.getTranslator("weapons.xml");

	private final static String NAME = "name";
	private final static String CHARACTERISTIC = "characteristic";
	private final static String SKILL = "skill";
	private final static String TECH_LEVEL = "techLevel";
	private final static String TECH_LEVEL_SPECIAL = "techLevelSpecial";
	private final static String GOAL = "goal";
	private final static String DAMAGE = "damage";
	private final static String STRENGTH = "strength";
	private final static String RANGE = "range";
	private final static String SHOTS = "shots";
	private final static String RATE = "rate";
	private final static String SIZE = "size";
	private final static String COST = "cost";
	private final static String FACTION = "faction";

	private final static String TYPE = "type";
	private final static String SPECIAL = "special";
	private final static String DAMAGE_TYPE = "damageType";

	private final static String AMMUNITION = "ammunition";
	private final static String ACCESSORIES = "others";

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
		String name = null;
		try {
			name = translator.getNodeValue(weaponId, NAME, language);
		} catch (Exception e) {
			throw new InvalidWeaponException("Invalid name in weapon '" + weaponId + "'.");
		}

		CharacteristicDefinition characteristicDefintion = null;
		try {
			String characteristicName = translator.getNodeValue(weaponId, CHARACTERISTIC);
			characteristicDefintion = CharacteristicsDefinitionFactory.getInstance().getElement(characteristicName, language);

		} catch (Exception e) {
			throw new InvalidWeaponException("Invalid characteristic name in weapon '" + weaponId + "'.");
		}

		SkillDefinition skill = null;
		try {
			String skillName = translator.getNodeValue(weaponId, SKILL);
			skill = SkillsDefinitionsFactory.getInstance().getElement(skillName, language);
		} catch (Exception e) {
			throw new InvalidWeaponException("Invalid skill name in weapon '" + weaponId + "'.");
		}

		int techLevel = 0;
		try {
			String techLevelName = translator.getNodeValue(weaponId, TECH_LEVEL);
			techLevel = Integer.parseInt(techLevelName);
		} catch (Exception e) {
			throw new InvalidWeaponException("Invalid tech level in weapon '" + weaponId + "'.");
		}

		boolean techLevelSpecial = false;
		try {
			String techLevelSpecialValue = translator.getNodeValue(weaponId, TECH_LEVEL_SPECIAL);
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
			String strengthValue = translator.getNodeValue(weaponId, STRENGTH);
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
			String shotsValue = translator.getNodeValue(weaponId, SHOTS);
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

		int cost = 0;
		try {
			String costValue = translator.getNodeValue(weaponId, COST);
			cost = Integer.parseInt(costValue);
		} catch (Exception e) {
			throw new InvalidWeaponException("Invalid cost value in weapon '" + weaponId + "'.");
		}

		String special = "";
		try {
			special = translator.getNodeValue(weaponId, SPECIAL);
		} catch (Exception e) {
			// Not mandatory.
		}

		String typeName = "";
		try {
			typeName = translator.getNodeValue(weaponId, TYPE);
		} catch (Exception e) {
			throw new InvalidWeaponException("Invalid type value in weapon '" + weaponId + "'.");
		}

		Set<DamageType> damageOfWeapon = new HashSet<>();
		try {
			String damageDefinition = translator.getNodeValue(weaponId, DAMAGE_TYPE);
			if (damageDefinition != null) {
				StringTokenizer damageTypesTokenizer = new StringTokenizer(damageDefinition, ",");
				while (damageTypesTokenizer.hasMoreTokens()) {
					damageOfWeapon.add(DamageTypeFactory.getInstance().getElement(damageTypesTokenizer.nextToken().trim(), language));
				}
			}
		} catch (Exception e) {
			// Not mandatory.
		}

		Set<Ammunition> ammunitions = new HashSet<>();
		String ammunitionsNames = translator.getNodeValue(weaponId, AMMUNITION);
		if (ammunitionsNames != null) {
			StringTokenizer ammunitionTokenizer = new StringTokenizer(ammunitionsNames, ",");
			while (ammunitionTokenizer.hasMoreTokens()) {
				try {
					ammunitions.add(AmmunitionFactory.getInstance().getElement(ammunitionTokenizer.nextToken().trim(), language));
				} catch (InvalidXmlElementException ixe) {
					throw new InvalidWeaponException("Error in ammunitions '" + ammunitionsNames + "' structure. Invalid ammunition definition. ", ixe);
				}
			}
		}
		
		Set<Accessory> accessories = new HashSet<>();
		String accesoriesNames = translator.getNodeValue(weaponId, ACCESSORIES);
		if (accesoriesNames != null) {
			StringTokenizer accessoryTokenizer = new StringTokenizer(accesoriesNames, ",");
			while (accessoryTokenizer.hasMoreTokens()) {
				try {
					accessories.add(AccessoryFactory.getInstance().getElement(accessoryTokenizer.nextToken().trim(), language));
				} catch (InvalidXmlElementException ixe) {
					throw new InvalidWeaponException("Error in accessories '" + accesoriesNames + "' structure. Invalid accessory definition. ", ixe);
				}
			}
		}

		// Is a weapon of a faction?
		Faction faction = null;
		String factionId = translator.getNodeValue(weaponId, FACTION);
		if (factionId != null) {
			Faction restrictedFaction = FactionsFactory.getInstance().getElement(factionId, language);
			faction = restrictedFaction;
		}

		weapon = new Weapon(weaponId, name, WeaponType.get(typeName), goal, characteristicDefintion, skill, damage, strength, range, shots, rate, techLevel,
				techLevelSpecial, size, special, damageOfWeapon, cost, ammunitions, accessories, faction);

		return weapon;
	}
}
