package com.softwaremagico.tm.character.equipment.armours;

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

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.character.equipment.DamageType;
import com.softwaremagico.tm.character.equipment.DamageTypeFactory;
import com.softwaremagico.tm.character.equipment.shields.Shield;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.json.factories.cache.ArmourFactoryCacheLoader;
import com.softwaremagico.tm.json.factories.cache.FactoryCacheLoader;
import com.softwaremagico.tm.language.ITranslator;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class ArmourFactory extends XmlFactory<Armour> {
    private static final String TRANSLATOR_FILE = "armours.xml";

    private static final String TECH = "techLevel";
    private static final String PROTECTION = "protection";
    private static final String STRENGTH_MODIFICATION = "strength";
    private static final String DEXTERITY_MODIFICATION = "dexterity";
    private static final String ENDURANCE_MODIFICATION = "endurance";
    private static final String INITIATIVE_MODIFICATION = "initiative";

    private static final String STANDARD_PENALIZATIONS = "standardPenalizations";
    private static final String SPECIAL_PENALIZATIONS = "specialPenalizations";

    private static final String COST = "cost";
    private static final String DAMAGE_TYPE = "damageType";
    private static final String SHIELD = "shield";
    private static final String OTHER = "others";

    private ArmourFactoryCacheLoader armourFactoryCacheLoader;

    private static class ArmourFactoryInit {
        public static final ArmourFactory INSTANCE = new ArmourFactory();
    }

    public static ArmourFactory getInstance() {
        return ArmourFactoryInit.INSTANCE;
    }

    @Override
    public String getTranslatorFile() {
        return TRANSLATOR_FILE;
    }

    @Override
    public FactoryCacheLoader<Armour> getFactoryCacheLoader() {
        if (armourFactoryCacheLoader == null) {
            armourFactoryCacheLoader = new ArmourFactoryCacheLoader();
        }
        return armourFactoryCacheLoader;
    }

    @Override
    protected Armour createElement(ITranslator translator, String armourId, String name, String description, String language, String moduleName)
            throws InvalidXmlElementException {
        int techLevel = 0;
        try {
            final String techValue = translator.getNodeValue(armourId, TECH);
            techLevel = Integer.parseInt(techValue);
        } catch (Exception e) {
            throw new InvalidArmourException("Invalid tech value in armour '" + armourId + "'.");
        }

        int protection = 0;
        try {
            final String protectionValue = translator.getNodeValue(armourId, PROTECTION);
            protection = Integer.parseInt(protectionValue);
        } catch (Exception e) {
            throw new InvalidArmourException("Invalid protection value in armour '" + armourId + "'.");
        }

        int standardStrengthModification = 0;
        try {
            final String strengthModificationValue = translator.getNodeValue(armourId, STANDARD_PENALIZATIONS,
                    STRENGTH_MODIFICATION);
            standardStrengthModification = Integer.parseInt(strengthModificationValue);
        } catch (Exception e) {
            throw new InvalidArmourException("Invalid standard strength Modification value in armour '" + armourId
                    + "'.");
        }

        int standardDexterityModification = 0;
        try {
            final String dexterityModificationValue = translator.getNodeValue(armourId, STANDARD_PENALIZATIONS,
                    DEXTERITY_MODIFICATION);
            standardDexterityModification = Integer.parseInt(dexterityModificationValue);
        } catch (Exception e) {
            throw new InvalidArmourException("Invalid standard dexterity Modification in armour '" + armourId + "'.");
        }

        int standardEnduranceModification = 0;
        try {
            final String enduranceModificationValue = translator.getNodeValue(armourId, STANDARD_PENALIZATIONS,
                    ENDURANCE_MODIFICATION);
            standardEnduranceModification = Integer.parseInt(enduranceModificationValue);
        } catch (Exception e) {
            throw new InvalidArmourException("Invalid standard endurance Modification in armour '" + armourId + "'.");
        }

        int standardInitiativeModification = 0;
        try {
            final String initiativeModificationValue = translator.getNodeValue(armourId, STANDARD_PENALIZATIONS,
                    INITIATIVE_MODIFICATION);
            standardInitiativeModification = Integer.parseInt(initiativeModificationValue);
        } catch (Exception e) {
            throw new InvalidArmourException("Invalid standard initiative Modification in armour '" + armourId + "'.");
        }

        Integer specialStrengthModification = null;
        try {
            final String strengthModificationValue = translator.getNodeValue(armourId, SPECIAL_PENALIZATIONS,
                    STRENGTH_MODIFICATION);
            specialStrengthModification = Integer.parseInt(strengthModificationValue);
        } catch (Exception e) {
            // Not mandatory
        }

        Integer specialDexterityModification = null;
        try {
            final String dexterityModificationValue = translator.getNodeValue(armourId, SPECIAL_PENALIZATIONS,
                    DEXTERITY_MODIFICATION);
            specialDexterityModification = Integer.parseInt(dexterityModificationValue);
        } catch (Exception e) {
            // Not mandatory
        }

        Integer specialEnduranceModification = null;
        try {
            final String enduranceModificationValue = translator.getNodeValue(armourId, SPECIAL_PENALIZATIONS,
                    ENDURANCE_MODIFICATION);
            specialEnduranceModification = Integer.parseInt(enduranceModificationValue);
        } catch (Exception e) {
            // Not mandatory
        }

        Integer specialInitiativeModification = null;
        try {
            final String initiativeModificationValue = translator.getNodeValue(armourId, SPECIAL_PENALIZATIONS,
                    INITIATIVE_MODIFICATION);
            specialInitiativeModification = Integer.parseInt(initiativeModificationValue);
        } catch (Exception e) {
            // Not mandatory
        }

        float cost = 0;
        try {
            final String costValue = translator.getNodeValue(armourId, COST);
            cost = Float.parseFloat(costValue);
        } catch (Exception e) {
            throw new InvalidArmourException("Invalid cost value in armour '" + armourId + "'.");
        }

        final Set<DamageType> damageOfArmour = new HashSet<>();
        final String damageDefinition = translator.getNodeValue(armourId, DAMAGE_TYPE);
        if (damageDefinition != null) {
            final StringTokenizer damageTypesTokenizer = new StringTokenizer(damageDefinition, ",");
            while (damageTypesTokenizer.hasMoreTokens()) {
                try {
                    damageOfArmour.add(DamageTypeFactory.getInstance().getElement(
                            damageTypesTokenizer.nextToken().trim(), language, moduleName));
                } catch (InvalidXmlElementException e) {
                    throw new InvalidArmourException("Invalid damage type in armour '" + armourId + "'.", e);
                }
            }
        }

        Set<Shield> allowedShields = new HashSet<>();
        try {
            allowedShields = getCommaSeparatedValues(armourId, SHIELD, language, moduleName,
                    ShieldFactory.getInstance());
        } catch (Exception e) {
            // Not mandatory.
        }

        final ArmourPenalization standardPenalizations = new ArmourPenalization(standardDexterityModification,
                standardStrengthModification, standardInitiativeModification, standardEnduranceModification);

        ArmourPenalization specialPenalizations = null;
        if (specialDexterityModification != null && specialStrengthModification != null
                && specialInitiativeModification != null && specialEnduranceModification != null) {
            specialPenalizations = new ArmourPenalization(specialDexterityModification, specialStrengthModification,
                    specialInitiativeModification, specialEnduranceModification);
        }

        final Set<ArmourSpecification> specifications;
        try {
            specifications = getCommaSeparatedValues(armourId, OTHER, language, moduleName,
                    ArmourSpecificationFactory.getInstance());
        } catch (InvalidXmlElementException ixe) {
            throw new InvalidArmourException("Error in specifications in '" + OTHER + "' for armour '" + armourId
                    + "'. Invalid specification definition. ", ixe);
        }

        final Armour armour = new Armour(armourId, name, description, language, moduleName, techLevel, protection, damageOfArmour,
                standardPenalizations, specialPenalizations, allowedShields, specifications, cost);

        return armour;
    }
}
