package com.softwaremagico.tm.file.modules;

/*-
 * #%L
 * Think Machine (Rules)
 * %%
 * Copyright (C) 2017 - 2020 Softwaremagico
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
import com.softwaremagico.tm.character.benefices.BeneficeDefinitionFactory;
import com.softwaremagico.tm.character.blessings.BlessingFactory;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.combat.CombatStyleFactory;
import com.softwaremagico.tm.character.cybernetics.CyberneticDeviceFactory;
import com.softwaremagico.tm.character.cybernetics.CyberneticDeviceTraitFactory;
import com.softwaremagico.tm.character.equipment.DamageTypeFactory;
import com.softwaremagico.tm.character.equipment.armours.ArmourFactory;
import com.softwaremagico.tm.character.equipment.armours.ArmourSpecificationFactory;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.character.equipment.weapons.AccessoryFactory;
import com.softwaremagico.tm.character.equipment.weapons.AmmunitionFactory;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.occultism.OccultismDurationFactory;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.occultism.OccultismRangeFactory;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.character.occultism.TheurgyComponentFactory;
import com.softwaremagico.tm.character.planets.PlanetFactory;
import com.softwaremagico.tm.character.races.RaceFactory;
import com.softwaremagico.tm.character.skills.SkillsDefinitionsFactory;
import com.softwaremagico.tm.character.values.SpecialValuesFactory;
import com.softwaremagico.tm.log.MachineLog;

public class ModuleLoaderEnforcer {

	public static int loadAllFactories(String language, String moduleName) throws InvalidXmlElementException {
		int loadedElements = 0;
		MachineLog.info(ModuleLoaderEnforcer.class.getName(), "Loading all factories...");
		loadedElements += AccessoryFactory.getInstance().getElements(language, moduleName).size();
		loadedElements += AmmunitionFactory.getInstance().getElements(language, moduleName).size();
		loadedElements += ArmourFactory.getInstance().getElements(language, moduleName).size();
		loadedElements += ArmourSpecificationFactory.getInstance().getElements(language, moduleName).size();
		loadedElements += BeneficeDefinitionFactory.getInstance().getElements(language, moduleName).size();
		loadedElements += BlessingFactory.getInstance().getElements(language, moduleName).size();
		loadedElements += CharacteristicsDefinitionFactory.getInstance().getElements(language, moduleName).size();
		loadedElements += CombatStyleFactory.getInstance().getElements(language, moduleName).size();
		loadedElements += CyberneticDeviceFactory.getInstance().getElements(language, moduleName).size();
		loadedElements += CyberneticDeviceTraitFactory.getInstance().getElements(language, moduleName).size();
		loadedElements += DamageTypeFactory.getInstance().getElements(language, moduleName).size();
		loadedElements += FactionsFactory.getInstance().getElements(language, moduleName).size();
		loadedElements += OccultismDurationFactory.getInstance().getElements(language, moduleName).size();
		loadedElements += OccultismPathFactory.getInstance().getElements(language, moduleName).size();
		loadedElements += OccultismRangeFactory.getInstance().getElements(language, moduleName).size();
		loadedElements += OccultismTypeFactory.getInstance().getElements(language, moduleName).size();
		loadedElements += PlanetFactory.getInstance().getElements(language, moduleName).size();
		loadedElements += RaceFactory.getInstance().getElements(language, moduleName).size();
		loadedElements += ShieldFactory.getInstance().getElements(language, moduleName).size();
		loadedElements += SkillsDefinitionsFactory.getInstance().getElements(language, moduleName).size();
		loadedElements += SpecialValuesFactory.getInstance().getElements(language, moduleName).size();
		loadedElements += TheurgyComponentFactory.getInstance().getElements(language, moduleName).size();
		loadedElements += WeaponFactory.getInstance().getElements(language, moduleName).size();
		MachineLog.info(ModuleLoaderEnforcer.class.getName(), "All factories loaded!");
		return loadedElements;
	}

}
