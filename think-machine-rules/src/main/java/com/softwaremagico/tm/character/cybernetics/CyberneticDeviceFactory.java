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

import com.softwaremagico.tm.ElementClassification;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponDamage;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.character.values.Bonification;
import com.softwaremagico.tm.character.values.IValue;
import com.softwaremagico.tm.character.values.SpecialValue;
import com.softwaremagico.tm.character.values.StaticValue;
import com.softwaremagico.tm.json.factories.cache.FactoryCacheLoader;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.log.MachineXmlReaderLog;
import com.softwaremagico.tm.log.SuppressFBWarnings;

import java.util.*;

public class CyberneticDeviceFactory extends XmlFactory<CyberneticDevice> {
    private static final String TRANSLATOR_FILE = "cybernetics.xml";

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
    public void refreshCache() {
        requiredBy = null;
        super.refreshCache();
    }

    @Override
    public String getTranslatorFile() {
        return TRANSLATOR_FILE;
    }

    @Override
    public FactoryCacheLoader<CyberneticDevice> getFactoryCacheLoader() {
        return null;
    }

    private void addRequirement(CyberneticDevice device) {
        if (device == null) {
            return;
        }
        requiredBy.computeIfAbsent(device.getRequirement(), k -> new HashSet<>());
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
            requiredDevice.addAll(requiredBy.get(device));
        }
        return requiredDevice;
    }

    @Override
    @SuppressFBWarnings("REC_CATCH_EXCEPTION")
    protected CyberneticDevice createElement(ITranslator translator, String cyberneticDeviceId, String name, String description,
                                             String language, String moduleName) throws InvalidXmlElementException {
        try {
            int techLevel;
            try {
                final String techLevelName = translator.getNodeValue(cyberneticDeviceId, TECH_LEVEL);
                techLevel = Integer.parseInt(techLevelName);
            } catch (Exception e) {
                throw new InvalidCyberneticDeviceException("Invalid tech level in cybernetic device '"
                        + cyberneticDeviceId + "'.");
            }

            int points;
            try {
                final String pointsName = translator.getNodeValue(cyberneticDeviceId, POINTS);
                points = Integer.parseInt(pointsName);
            } catch (Exception e) {
                throw new InvalidCyberneticDeviceException("Invalid points value in cybernetic device '"
                        + cyberneticDeviceId + "'.");
            }

            int cost;
            try {
                final String costName = translator.getNodeValue(cyberneticDeviceId, COST);
                cost = Integer.parseInt(costName);
            } catch (Exception e) {
                throw new InvalidCyberneticDeviceException("Invalid cost in cybernetic device '" + cyberneticDeviceId
                        + "'.");
            }

            int incompatibility;
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
                weapon = getWeapon(translator, cyberneticDeviceId, name, description, techLevel, language, moduleName);
            }

            ElementClassification cyberneticClassification;
            try {
                cyberneticClassification = ElementClassification.get(translator.getNodeValue(cyberneticDeviceId,
                        CLASSIFICATION));
            } catch (Exception e) {
                throw new InvalidCyberneticDeviceException("Invalid classification value in cybernetic '"
                        + cyberneticDeviceId + "'.");
            }

            return new CyberneticDevice(cyberneticDeviceId, name, description, language,
                    moduleName, points, incompatibility, cost, techLevel, requires, weapon, traits, bonifications,
                    staticValues, cyberneticClassification);
        } catch (Exception e) {
            throw new InvalidCyberneticDeviceException("Invalid cybernetic device definition for '"
                    + cyberneticDeviceId + "'.", e);
        }
    }

    private Weapon getWeapon(ITranslator translator, String cyberneticDeviceId, String name, String description, int techLevel,
                             String language, String moduleName) throws InvalidCyberneticDeviceException {
        CharacteristicDefinition characteristicDefinition;
        try {
            final String characteristicName = translator.getNodeValue(cyberneticDeviceId, WEAPON, CHARACTERISTIC);
            characteristicDefinition = CharacteristicsDefinitionFactory.getInstance().getElement(characteristicName,
                    language, moduleName);
        } catch (Exception e) {
            throw new InvalidCyberneticDeviceException("Invalid characteristic name in weapon of cybernetic '"
                    + cyberneticDeviceId + "'.");
        }

        AvailableSkill skill;
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

        String damage;
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

        final List<WeaponDamage> weaponDamages = new ArrayList<>();
        weaponDamages.add(new WeaponDamage(goal, damage, 0, range, shots, rate, characteristicDefinition, skill));

        return new Weapon(cyberneticDeviceId, name, description, language, moduleName, null,
                weaponDamages, techLevel, false, null, "", new HashSet<>(), 0,
                new HashSet<>(), new HashSet<>());

    }
}
