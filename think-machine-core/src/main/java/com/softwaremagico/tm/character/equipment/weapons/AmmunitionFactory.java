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
import com.softwaremagico.tm.character.equipment.DamageType;
import com.softwaremagico.tm.character.equipment.DamageTypeFactory;
import com.softwaremagico.tm.character.equipment.Size;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;

public class AmmunitionFactory extends XmlFactory<Ammunition> {
	private final static ITranslator translatorWeapon = LanguagePool.getTranslator("ammunition.xml");

	private final static String NAME = "name";

	private final static String GOAL = "goal";
	private final static String DAMAGE = "damage";
	private final static String STRENGTH = "strength";
	private final static String RANGE = "range";
	private final static String COST = "cost";
	private final static String SIZE = "size";

	private final static String ACCESSORIES = "others";
	private final static String DAMAGE_TYPE = "damageType";

	private static AmmunitionFactory instance;

	private static void createInstance() {
		if (instance == null) {
			synchronized (AmmunitionFactory.class) {
				if (instance == null) {
					instance = new AmmunitionFactory();
				}
			}
		}
	}

	public static AmmunitionFactory getInstance() {
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
	protected Ammunition createElement(ITranslator translator, String ammunitionId, String language) throws InvalidXmlElementException {
		Ammunition ammunition = null;
		String name = null;
		try {
			name = translator.getNodeValue(ammunitionId, NAME, language);
		} catch (Exception e) {
			throw new InvalidWeaponException("Invalid name in ammunition '" + ammunitionId + "'.");
		}

		String goal = "";
		try {
			goal = translator.getNodeValue(ammunitionId, GOAL);
		} catch (Exception e) {
			// Not mandatory
		}

		String damage = "";
		try {
			damage = translator.getNodeValue(ammunitionId, DAMAGE);
		} catch (Exception e) {
			// Not mandatory.
		}

		Integer strength = null;
		try {
			String strengthValue = translator.getNodeValue(ammunitionId, STRENGTH);
			if (strengthValue != null) {
				strength = Integer.parseInt(strengthValue);
			}
		} catch (Exception e) {
			// Not mandatory.
		}

		Size size = null;
		try {
			size = Size.get(translator.getNodeValue(ammunitionId, SIZE));
		} catch (Exception e) {
			// Not mandatory.
		}

		String range = null;
		try {
			range = translator.getNodeValue(ammunitionId, RANGE);
		} catch (Exception e) {
			// Not mandatory.
		}

		Integer cost = null;
		try {
			cost = Integer.parseInt(translator.getNodeValue(ammunitionId, COST));
		} catch (Exception e) {
			// Not mandatory.
		}

		Set<DamageType> damageOfAmmunition = new HashSet<>();
		try {
			String damageDefinition = translator.getNodeValue(ammunitionId, DAMAGE_TYPE);
			if (damageDefinition != null) {
				StringTokenizer damageTypesTokenizer = new StringTokenizer(damageDefinition, ",");
				while (damageTypesTokenizer.hasMoreTokens()) {
					damageOfAmmunition.add(DamageTypeFactory.getInstance().getElement(damageTypesTokenizer.nextToken().trim(), language));
				}
			}
		} catch (Exception e) {
			// Not mandatory.
		}

		Set<Accessory> accessories = new HashSet<>();
		String accesoriesNames = translator.getNodeValue(ammunitionId, ACCESSORIES);
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

		ammunition = new Ammunition(ammunitionId, name, goal, damage, strength, range, size, cost, damageOfAmmunition, accessories);

		return ammunition;
	}
}
