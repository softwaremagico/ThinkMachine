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
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.equipment.DamageType;
import com.softwaremagico.tm.character.equipment.DamageTypeFactory;
import com.softwaremagico.tm.character.equipment.Size;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.json.factories.cache.FactoryCacheLoader;
import com.softwaremagico.tm.json.factories.cache.WeaponsFactoryCacheLoader;
import com.softwaremagico.tm.language.ITranslator;

import java.util.*;
import java.util.stream.Collectors;

public class WeaponFactory extends XmlFactory<Weapon> {
    private static final String TRANSLATOR_FILE = "weapons.xml";

    private static final String CHARACTERISTIC = "characteristic";
    private static final String SKILL = "skill";
    private static final String TECH_LEVEL = "techLevel";
    private static final String TECH_LEVEL_SPECIAL = "techLevelSpecial";
    private static final String SIZE = "size";
    private static final String COST = "cost";

    private static final String TYPE = "type";
    private static final String SPECIAL = "special";
    private static final String DAMAGE_TYPE = "damageType";
    private static final String DAMAGE_LIST = "damageList";
    private static final String WEAPON_DAMAGE = "weaponDamage";
    private static final String DAMAGE_NAME = "name";
    private static final String DAMAGE = "damage";
    private static final String GOAL = "goal";
    private static final String STRENGTH = "strength";
    private static final String RANGE = "range";
    private static final String SHOTS = "shots";
    private static final String RATE = "rate";

    private static final String AMMUNITION = "ammunition";
    private static final String ACCESSORIES = "others";

    private static class WeaponFactoryInit {
        public static final WeaponFactory INSTANCE = new WeaponFactory();
    }

    public static WeaponFactory getInstance() {
        return WeaponFactoryInit.INSTANCE;
    }

    protected Map<String, Map<String, List<Weapon>>> rangedWeapons = new HashMap<>();
    protected Map<String, Map<String, List<Weapon>>> meleeWeapons = new HashMap<>();

    private WeaponsFactoryCacheLoader weaponsFactoryCacheLoader;

    @Override
    public String getTranslatorFile() {
        return TRANSLATOR_FILE;
    }

    @Override
    public FactoryCacheLoader<Weapon> getFactoryCacheLoader() {
        if (weaponsFactoryCacheLoader == null) {
            weaponsFactoryCacheLoader = new WeaponsFactoryCacheLoader();
        }
        return weaponsFactoryCacheLoader;
    }

    @Override
    protected Weapon createElement(ITranslator translator, String weaponId, String name, String description,
                                   String language, String moduleName)
            throws InvalidXmlElementException {


        int techLevel;
        try {
            final String techLevelName = translator.getNodeValue(weaponId, TECH_LEVEL);
            techLevel = Integer.parseInt(techLevelName);
        } catch (Exception e) {
            throw new InvalidWeaponException("Invalid tech level in weapon '" + weaponId + "'.");
        }

        boolean techLevelSpecial;
        try {
            final String techLevelSpecialValue = translator.getNodeValue(weaponId, TECH_LEVEL_SPECIAL);
            techLevelSpecial = Boolean.parseBoolean(techLevelSpecialValue);
        } catch (Exception e) {
            throw new InvalidWeaponException("Invalid tech level special in weapon '" + weaponId + "'.");
        }

        final List<WeaponDamage> damages = new ArrayList<>();
        int node = 0;
        while (true) {
            String damageName = null;
            try {
                damageName = translator.getNodeValue(weaponId, DAMAGE_LIST, WEAPON_DAMAGE, DAMAGE_NAME, node);
            } catch (Exception e) {
                // Not mandatory
            }

            String goal = "";
            try {
                goal = translator.getNodeValue(weaponId, DAMAGE_LIST, WEAPON_DAMAGE, GOAL, node);
            } catch (Exception e) {
                // Not mandatory
            }

            String damage = null;
            try {
                damage = translator.getNodeValue(weaponId, DAMAGE_LIST, WEAPON_DAMAGE, DAMAGE, node);
            } catch (Exception e) {
                // Not mandatory
            }

            Integer strength = null;
            try {
                final String strengthValue = translator.getNodeValue(weaponId, DAMAGE_LIST, WEAPON_DAMAGE, STRENGTH, node);
                strength = Integer.parseInt(strengthValue);
            } catch (Exception e) {
                // Not mandatory
            }

            String range = null;
            try {
                range = translator.getNodeValue(weaponId, DAMAGE_LIST, WEAPON_DAMAGE, RANGE, node);
            } catch (Exception e) {
                // Not mandatory.
            }

            Integer shots = null;
            try {
                final String shotsValue = translator.getNodeValue(weaponId, DAMAGE_LIST, WEAPON_DAMAGE, SHOTS, node);
                shots = Integer.parseInt(shotsValue);
            } catch (Exception e) {
                // Not mandatory.
            }

            String rate = "";
            try {
                rate = translator.getNodeValue(weaponId, DAMAGE_LIST, WEAPON_DAMAGE, RATE, node);
            } catch (Exception e) {
                // Not mandatory.
            }

            if (damage == null && strength == null && range == null && shots == null && damageName == null) {
                if (damages.isEmpty()) {
                    throw new InvalidWeaponException("No damage defined for weapon '" + weaponId + "'.");
                }
                break;
            }

            CharacteristicDefinition characteristicDefinition;
            try {
                final String characteristicName = translator.getNodeValue(weaponId, DAMAGE_LIST, WEAPON_DAMAGE, CHARACTERISTIC);
                characteristicDefinition = CharacteristicsDefinitionFactory.getInstance().getElement(characteristicName,
                        language, moduleName);
            } catch (Exception e) {
                throw new InvalidWeaponException("Invalid characteristic name in weapon '" + weaponId + "'.");
            }

            AvailableSkill skill;
            try {
                final String skillName = translator.getNodeValue(weaponId, DAMAGE_LIST, WEAPON_DAMAGE, SKILL);
                skill = AvailableSkillsFactory.getInstance().getElement(skillName, language, moduleName);
            } catch (Exception e) {
                throw new InvalidWeaponException("Invalid skill name in weapon '" + weaponId + "'.");
            }

            final WeaponDamage weaponDamage = new WeaponDamage(damageName, goal, damage, strength, range, shots, rate,
                    characteristicDefinition, skill);
            damages.add(weaponDamage);
            node++;
        }


        Size size;
        try {
            size = Size.get(translator.getNodeValue(weaponId, SIZE));
        } catch (Exception e) {
            throw new InvalidWeaponException("Invalid size value in weapon '" + weaponId + "'.", e);
        }

        float cost;
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

        WeaponType type;
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
                                damageTypesTokenizer.nextToken().trim(), language, moduleName));
                    } catch (InvalidXmlElementException e) {
                        throw new InvalidWeaponException("Invalid damage type in weapon '" + weaponId + "'.", e);
                    }
                }
            }
        } catch (NullPointerException e) {
            // Not mandatory.
        }

        final Set<Ammunition> ammunition = new HashSet<>();
        final String ammunitionNames = translator.getNodeValue(weaponId, AMMUNITION);
        if (ammunitionNames != null) {
            final StringTokenizer ammunitionTokenizer = new StringTokenizer(ammunitionNames, ",");
            while (ammunitionTokenizer.hasMoreTokens()) {
                try {
                    ammunition.add(AmmunitionFactory.getInstance().getElement(ammunitionTokenizer.nextToken().trim(),
                            language, moduleName));
                } catch (InvalidXmlElementException ixe) {
                    throw new InvalidWeaponException("Error in ammunition '" + ammunitionNames
                            + "' structure. Invalid ammunition definition. ", ixe);
                }
            }
        }

        final Set<Accessory> accessories = new HashSet<>();
        final String accessoriesNames = translator.getNodeValue(weaponId, ACCESSORIES);
        if (accessoriesNames != null) {
            final StringTokenizer accessoryTokenizer = new StringTokenizer(accessoriesNames, ",");
            while (accessoryTokenizer.hasMoreTokens()) {
                try {
                    accessories.add(AccessoryFactory.getInstance().getElement(accessoryTokenizer.nextToken().trim(),
                            language, moduleName));
                } catch (InvalidXmlElementException ixe) {
                    throw new InvalidWeaponException("Error in accessories '" + accessoriesNames
                            + "' structure in weapon '" + weaponId + "'. Invalid accessory definition. ", ixe);
                }
            }
        }

        return new Weapon(weaponId, name, description, language, moduleName, type, damages,
                techLevel, techLevelSpecial, size, special, damageOfWeapon, cost,
                ammunition, accessories);
    }

    public synchronized List<Weapon> getRangedWeapons(String language, String moduleName) throws InvalidXmlElementException {
        rangedWeapons.computeIfAbsent(language, k -> new HashMap<>());
        if (rangedWeapons.get(language).get(moduleName) == null) {
            rangedWeapons.get(language).put(moduleName, getElements(language, moduleName).stream().
                    filter(Weapon::isRangedWeapon).collect(Collectors.toList()));
        }
        return rangedWeapons.get(language).get(moduleName);
    }

    public synchronized List<Weapon> getMeleeWeapons(String language, String moduleName) throws InvalidXmlElementException {
        meleeWeapons.computeIfAbsent(language, k -> new HashMap<>());
        if (meleeWeapons.get(language).get(moduleName) == null) {
            meleeWeapons.get(language).put(moduleName, getElements(language, moduleName).stream().
                    filter(Weapon::isMeleeWeapon).collect(Collectors.toList()));
        }
        return meleeWeapons.get(language).get(moduleName);
    }
}
