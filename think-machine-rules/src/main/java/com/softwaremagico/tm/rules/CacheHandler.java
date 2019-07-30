package com.softwaremagico.tm.rules;

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

import com.softwaremagico.tm.rules.character.benefices.AvailableBeneficeFactory;
import com.softwaremagico.tm.rules.character.benefices.BeneficeDefinitionFactory;
import com.softwaremagico.tm.rules.character.blessings.BlessingFactory;
import com.softwaremagico.tm.rules.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.rules.character.combat.CombatStyleFactory;
import com.softwaremagico.tm.rules.character.cybernetics.CyberneticDeviceFactory;
import com.softwaremagico.tm.rules.character.cybernetics.CyberneticDeviceTraitFactory;
import com.softwaremagico.tm.rules.character.equipment.armours.ArmourFactory;
import com.softwaremagico.tm.rules.character.equipment.shields.ShieldFactory;
import com.softwaremagico.tm.rules.character.equipment.weapons.AmmunitionFactory;
import com.softwaremagico.tm.rules.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.rules.character.factions.FactionsFactory;
import com.softwaremagico.tm.rules.character.occultism.OccultismDurationFactory;
import com.softwaremagico.tm.rules.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.rules.character.occultism.OccultismRangeFactory;
import com.softwaremagico.tm.rules.character.races.RaceFactory;
import com.softwaremagico.tm.rules.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.rules.character.skills.SkillsDefinitionsFactory;
import com.softwaremagico.tm.rules.language.LanguagePool;

public class CacheHandler {

	public static void clearCache() {
		LanguagePool.clearCache();

		AvailableSkillsFactory.getInstance().clearCache();
		SkillsDefinitionsFactory.getInstance().clearCache();
		BlessingFactory.getInstance().clearCache();
		RaceFactory.getInstance().clearCache();
		BeneficeDefinitionFactory.getInstance().clearCache();
		AvailableBeneficeFactory.getInstance().clearCache();
		WeaponFactory.getInstance().clearCache();
		AmmunitionFactory.getInstance().clearCache();
		ShieldFactory.getInstance().clearCache();
		ArmourFactory.getInstance().clearCache();
		CombatStyleFactory.getInstance().clearCache();
		CharacteristicsDefinitionFactory.getInstance().clearCache();
		FactionsFactory.getInstance().clearCache();
		BlessingFactory.getInstance().clearCache();
		OccultismPathFactory.getInstance().clearCache();
		OccultismDurationFactory.getInstance().clearCache();
		OccultismRangeFactory.getInstance().clearCache();
		CyberneticDeviceTraitFactory.getInstance().clearCache();
		CyberneticDeviceFactory.getInstance().clearCache();
	}
}
