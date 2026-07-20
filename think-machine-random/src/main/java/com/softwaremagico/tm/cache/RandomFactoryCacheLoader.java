package com.softwaremagico.tm.cache;

/*-
 * #%L
 * Think Machine (Random Generator)
 * %%
 * Copyright (C) 2017 - 2021 Softwaremagico
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

import com.google.gson.GsonBuilder;
import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.benefices.BeneficeDefinition;
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.cybernetics.CyberneticDevice;
import com.softwaremagico.tm.character.cybernetics.CyberneticDeviceTrait;
import com.softwaremagico.tm.character.cybernetics.SelectedCyberneticDevice;
import com.softwaremagico.tm.character.equipment.DamageType;
import com.softwaremagico.tm.character.equipment.armours.Armour;
import com.softwaremagico.tm.character.equipment.armours.ArmourSpecification;
import com.softwaremagico.tm.character.equipment.shields.Shield;
import com.softwaremagico.tm.character.equipment.weapons.Accessory;
import com.softwaremagico.tm.character.equipment.weapons.Ammunition;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.occultism.OccultismPath;
import com.softwaremagico.tm.character.occultism.OccultismPower;
import com.softwaremagico.tm.character.occultism.OccultismType;
import com.softwaremagico.tm.character.planets.Planet;
import com.softwaremagico.tm.character.races.Race;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.SkillDefinition;
import com.softwaremagico.tm.character.values.SpecialValue;
import com.softwaremagico.tm.json.*;
import com.softwaremagico.tm.json.factories.cache.FactoryCacheLoader;
import com.softwaremagico.tm.random.selectors.IRandomPreference;

public abstract class RandomFactoryCacheLoader<T extends Element<T>> extends FactoryCacheLoader<T> {

    @Override
    protected GsonBuilder initGsonBuilder(final String language, final String moduleName) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(AvailableSkill.class, new AvailableSkillAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(CharacteristicDefinition.class, new CharacteristicDefinitionAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(Characteristic.class, new CharacteristicAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(Accessory.class, new AccessoryAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(DamageType.class, new DamageTypeAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(Ammunition.class, new AmmunitionAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(Race.class, new RaceAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(Faction.class, new FactionAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(Weapon.class, new WeaponAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(Armour.class, new ArmourAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(ArmourSpecification.class, new ArmourSpecificationAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(Shield.class, new ShieldAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(BeneficeDefinition.class, new BeneficeDefinitionAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(AvailableBenefice.class, new AvailableBeneficeAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(Blessing.class, new BlessingAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(OccultismPath.class, new OccultismPathAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(OccultismPower.class, new OccultismPowerAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(OccultismType.class, new OccultismTypeAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(CyberneticDevice.class, new CyberneticDeviceAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(CyberneticDeviceTrait.class, new CyberneticDeviceTraitAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(Planet.class, new PlanetAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(SelectedCyberneticDevice.class, new SelectedCyberneticDeviceAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(SkillDefinition.class, new SkillDefinitionAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(SpecialValue.class, new SpecialValueAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(IRandomPreference.class, new RandomPreferenceAdapter());
        return gsonBuilder;
    }
}
