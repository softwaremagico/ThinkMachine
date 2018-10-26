package com.softwaremagico.tm.character.equipment.armour;

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
import com.softwaremagico.tm.character.equipment.shield.Shield;
import com.softwaremagico.tm.character.equipment.shield.ShieldFactory;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;

public class ArmourFactory extends XmlFactory<Armour> {
	private final static ITranslator translatorWeapon = LanguagePool.getTranslator("armours.xml");

	private final static String NAME = "name";

	private final static String TECH = "techLevel";
	private final static String PROTECTION = "protection";
	private final static String STRENGTH_MODIFICATION = "strength";
	private final static String DEXTERITY_MODIFICATION = "dexterity";
	private final static String ENDURANCE_MODIFICATION = "endurance";
	private final static String INITIATIVE_MODIFICATION = "initiative";

	private final static String STANDARD_PENALIZATIONS = "standardPenalizations";
	private final static String SPECIAL_PENALIZATIONS = "specialPenalizations";

	private final static String COST = "cost";
	private final static String DAMAGE_TYPE = "damageType";
	private final static String SHIELD = "shield";
	private final static String OTHER = "others";

	private static ArmourFactory instance;

	private static void createInstance() {
		if (instance == null) {
			synchronized (ArmourFactory.class) {
				if (instance == null) {
					instance = new ArmourFactory();
				}
			}
		}
	}

	public static ArmourFactory getInstance() {
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
	protected Armour createElement(ITranslator translator, String armourId, String language) throws InvalidXmlElementException {
		String name = null;
		try {
			name = translator.getNodeValue(armourId, NAME, language);
		} catch (Exception e) {
			throw new InvalidArmourException("Invalid name in armour '" + armourId + "'.");
		}

		int techLevel = 0;
		try {
			String techValue = translator.getNodeValue(armourId, TECH);
			techLevel = Integer.parseInt(techValue);
		} catch (Exception e) {
			throw new InvalidArmourException("Invalid tech value in armour '" + armourId + "'.");
		}

		int protection = 0;
		try {
			String protectionValue = translator.getNodeValue(armourId, PROTECTION);
			protection = Integer.parseInt(protectionValue);
		} catch (Exception e) {
			throw new InvalidArmourException("Invalid protection value in armour '" + armourId + "'.");
		}

		int standardStrengthModification = 0;
		try {
			String strengthModificationValue = translator.getNodeValue(armourId, STANDARD_PENALIZATIONS, STRENGTH_MODIFICATION);
			standardStrengthModification = Integer.parseInt(strengthModificationValue);
		} catch (Exception e) {
			throw new InvalidArmourException("Invalid standard strength Modification value in armour '" + armourId + "'.");
		}

		int standardDexterityModification = 0;
		try {
			String dexterityModificationValue = translator.getNodeValue(armourId, STANDARD_PENALIZATIONS, DEXTERITY_MODIFICATION);
			standardDexterityModification = Integer.parseInt(dexterityModificationValue);
		} catch (Exception e) {
			throw new InvalidArmourException("Invalid standard dexterity Modification in armour '" + armourId + "'.");
		}

		int standardEnduranceModification = 0;
		try {
			String enduranceModificationValue = translator.getNodeValue(armourId, STANDARD_PENALIZATIONS, ENDURANCE_MODIFICATION);
			standardEnduranceModification = Integer.parseInt(enduranceModificationValue);
		} catch (Exception e) {
			throw new InvalidArmourException("Invalid standard endurance Modification in armour '" + armourId + "'.");
		}

		int standardInitiativeModification = 0;
		try {
			String initiativeModificationValue = translator.getNodeValue(armourId, STANDARD_PENALIZATIONS, INITIATIVE_MODIFICATION);
			standardInitiativeModification = Integer.parseInt(initiativeModificationValue);
		} catch (Exception e) {
			throw new InvalidArmourException("Invalid standard initiative Modification in armour '" + armourId + "'.");
		}

		Integer specialStrengthModification = null;
		try {
			String strengthModificationValue = translator.getNodeValue(armourId, SPECIAL_PENALIZATIONS, STRENGTH_MODIFICATION);
			specialStrengthModification = Integer.parseInt(strengthModificationValue);
		} catch (Exception e) {
			// Not mandatory
		}

		Integer specialDexterityModification = null;
		try {
			String dexterityModificationValue = translator.getNodeValue(armourId, SPECIAL_PENALIZATIONS, DEXTERITY_MODIFICATION);
			specialDexterityModification = Integer.parseInt(dexterityModificationValue);
		} catch (Exception e) {
			// Not mandatory
		}

		Integer specialEnduranceModification = null;
		try {
			String enduranceModificationValue = translator.getNodeValue(armourId, SPECIAL_PENALIZATIONS, ENDURANCE_MODIFICATION);
			specialEnduranceModification = Integer.parseInt(enduranceModificationValue);
		} catch (Exception e) {
			// Not mandatory
		}

		Integer specialInitiativeModification = null;
		try {
			String initiativeModificationValue = translator.getNodeValue(armourId, SPECIAL_PENALIZATIONS, INITIATIVE_MODIFICATION);
			specialInitiativeModification = Integer.parseInt(initiativeModificationValue);
		} catch (Exception e) {
			// Not mandatory
		}

		float cost = 0;
		try {
			String costValue = translator.getNodeValue(armourId, COST);
			cost = Float.parseFloat(costValue);
		} catch (Exception e) {
			throw new InvalidArmourException("Invalid cost value in armour '" + armourId + "'.");
		}

		Set<DamageType> damageOfArmour = new HashSet<>();
		String damageDefinition = translator.getNodeValue(armourId, DAMAGE_TYPE);
		if (damageDefinition != null) {
			StringTokenizer damageTypesTokenizer = new StringTokenizer(damageDefinition, ",");
			while (damageTypesTokenizer.hasMoreTokens()) {
				try {
					damageOfArmour.add(DamageTypeFactory.getInstance().getElement(damageTypesTokenizer.nextToken().trim(), language));
				} catch (InvalidXmlElementException e) {
					throw new InvalidArmourException("Invalid damage type in armour '" + armourId + "'.", e);
				}
			}
		}

		Set<Shield> allowedShields = new HashSet<>();
		try {
			String shieldNames = translator.getNodeValue(armourId, SHIELD);
			if (shieldNames != null) {
				StringTokenizer shieldTokenizer = new StringTokenizer(shieldNames, ",");
				while (shieldTokenizer.hasMoreTokens()) {
					allowedShields.add(ShieldFactory.getInstance().getElement(shieldTokenizer.nextToken().trim(), language));
				}
			}
		} catch (Exception e) {
			// Not mandatory.
		}

		ArmourPenalization standardPenalizations = new ArmourPenalization(standardDexterityModification, standardStrengthModification,
				standardInitiativeModification, standardEnduranceModification);

		ArmourPenalization specialPenalizations = null;
		if (specialDexterityModification != null && specialStrengthModification != null && specialInitiativeModification != null
				&& specialEnduranceModification != null) {
			specialPenalizations = new ArmourPenalization(specialDexterityModification, specialStrengthModification, specialInitiativeModification,
					specialEnduranceModification);
		}

		Set<ArmourSpecification> specifications = new HashSet<>();
		String specificationsNames = translator.getNodeValue(armourId, OTHER);
		if (specificationsNames != null) {
			StringTokenizer specificationTokenizer = new StringTokenizer(specificationsNames, ",");
			while (specificationTokenizer.hasMoreTokens()) {
				try {
					specifications.add(ArmourSpecificationFactory.getInstance().getElement(specificationTokenizer.nextToken().trim(), language));
				} catch (InvalidXmlElementException ixe) {
					throw new InvalidArmourException("Error in specifications '" + specificationsNames + "' in armour '" + armourId
							+ "'. Invalid spceification definition. ", ixe);
				}
			}
		}

		Armour armour = new Armour(armourId, name, language, techLevel, protection, damageOfArmour, standardPenalizations, specialPenalizations,
				allowedShields, specifications, cost);

		return armour;
	}
}
