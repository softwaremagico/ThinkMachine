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

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.character.equipment.DamageType;
import com.softwaremagico.tm.character.equipment.DamageTypeFactory;
import com.softwaremagico.tm.character.equipment.Size;
import com.softwaremagico.tm.json.factories.cache.FactoryCacheLoader;
import com.softwaremagico.tm.language.ITranslator;

import java.util.HashSet;
import java.util.Set;

public class AmmunitionFactory extends XmlFactory<Ammunition> {
    private static final String TRANSLATOR_FILE = "ammunition.xml";

    private static final String GOAL = "goal";
    private static final String DAMAGE = "damage";
    private static final String STRENGTH = "strength";
    private static final String RANGE = "range";
    private static final String COST = "cost";
    private static final String SIZE = "size";

    private static final String ACCESSORIES = "others";
    private static final String DAMAGE_TYPE = "damageType";

    private static class AmmunitionFactoryInit {
        public static final AmmunitionFactory INSTANCE = new AmmunitionFactory();
    }

    public static AmmunitionFactory getInstance() {
        return AmmunitionFactoryInit.INSTANCE;
    }

    @Override
    public String getTranslatorFile() {
        return TRANSLATOR_FILE;
    }

    @Override
    public FactoryCacheLoader<Ammunition> getFactoryCacheLoader() {
        return null;
    }

    @Override
    protected Ammunition createElement(ITranslator translator, String ammunitionId, String name, String description,
                                       String language, String moduleName)
            throws InvalidXmlElementException {


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
            final String strengthValue = translator.getNodeValue(ammunitionId, STRENGTH);
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
            damageOfAmmunition = getCommaSeparatedValues(ammunitionId, DAMAGE_TYPE, language, moduleName,
                    DamageTypeFactory.getInstance());
        } catch (Exception e) {
            // Not mandatory.
        }

        final Set<Accessory> accessories;
        try {
            accessories = getCommaSeparatedValues(ammunitionId, ACCESSORIES, language, moduleName,
                    AccessoryFactory.getInstance());
        } catch (Exception e) {
            throw new InvalidWeaponException("Error in accessories of ammunition '" + ammunitionId
                    + "' structure. Invalid accessory definition. ", e);
        }

        return new Ammunition(ammunitionId, name, description, language, moduleName, goal, damage, strength, range, size,
                cost, damageOfAmmunition, accessories);
    }
}
