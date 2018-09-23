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
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.occultism.OccultismDurationFactory;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.occultism.OccultismRangeFactory;
import com.softwaremagico.tm.character.race.RaceFactory;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.character.skills.SkillsDefinitionsFactory;
import com.softwaremagico.tm.language.LanguagePool;

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
		CharacteristicsDefinitionFactory.getInstance().clearCache();
		FactionsFactory.getInstance().clearCache();
		BlessingFactory.getInstance().clearCache();
		OccultismPathFactory.getInstance().clearCache();
		OccultismDurationFactory.getInstance().clearCache();
		OccultismRangeFactory.getInstance().clearCache();

	}
}
