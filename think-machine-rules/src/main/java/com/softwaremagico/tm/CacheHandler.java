package com.softwaremagico.tm;

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

import com.softwaremagico.tm.character.benefices.AvailableBeneficeFactory;
import com.softwaremagico.tm.character.benefices.BeneficeDefinitionFactory;
import com.softwaremagico.tm.character.blessings.BlessingFactory;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.combat.CombatStyleFactory;
import com.softwaremagico.tm.character.cybernetics.CyberneticDeviceFactory;
import com.softwaremagico.tm.character.cybernetics.CyberneticDeviceTraitFactory;
import com.softwaremagico.tm.character.equipment.armours.ArmourFactory;
import com.softwaremagico.tm.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.character.equipment.weapons.AmmunitionFactory;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.occultism.OccultismDurationFactory;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.occultism.OccultismRangeFactory;
import com.softwaremagico.tm.character.races.RaceFactory;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.character.skills.SkillsDefinitionsFactory;
import com.softwaremagico.tm.language.LanguagePool;

public class CacheHandler {

	public static void clearCache() {
		LanguagePool.clearCache();

		AvailableSkillsFactory.getInstance().clearCache();
		SkillsDefinitionsFactory.getInstance().refreshCache();
		BlessingFactory.getInstance().refreshCache();
		RaceFactory.getInstance().refreshCache();
		BeneficeDefinitionFactory.getInstance().refreshCache();
		AvailableBeneficeFactory.getInstance().clearCache();
		WeaponFactory.getInstance().refreshCache();
		AmmunitionFactory.getInstance().refreshCache();
		ShieldFactory.getInstance().refreshCache();
		ArmourFactory.getInstance().refreshCache();
		CombatStyleFactory.getInstance().refreshCache();
		CharacteristicsDefinitionFactory.getInstance().refreshCache();
		FactionsFactory.getInstance().refreshCache();
		BlessingFactory.getInstance().refreshCache();
		OccultismPathFactory.getInstance().refreshCache();
		OccultismDurationFactory.getInstance().refreshCache();
		OccultismRangeFactory.getInstance().refreshCache();
		CyberneticDeviceTraitFactory.getInstance().refreshCache();
		CyberneticDeviceFactory.getInstance().refreshCache();
	}
}
