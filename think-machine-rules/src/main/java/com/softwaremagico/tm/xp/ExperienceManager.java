package com.softwaremagico.tm.xp;

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

import java.util.HashMap;
import java.util.Map;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.skills.AvailableSkill;

public class ExperienceManager {
	private int totalExperience = 0;
	private final Map<Element<?>, Integer> ranksIncreased;

	public ExperienceManager() {
		ranksIncreased = new HashMap<>();
	}

	public int getTotalExperience() {
		return totalExperience;
	}

	public Map<Element<?>, Integer> getRanksIncreased() {
		return ranksIncreased;
	}

	public static int getExperienceCostToImprove(Element<?> element, int currentValue)
			throws ElementCannotBeUpgradeWithExperienceException {
		if (element instanceof AvailableSkill) {
			return (currentValue + 1) * 2;
		}
		if (element instanceof Characteristic) {
			return (currentValue + 1) * 3;
		}
		throw new ElementCannotBeUpgradeWithExperienceException(
				"Invalid element '" + element + "'. Experience cannot be used on it.");
	}

	public void setTotalExperience(int totalExperience) {
		this.totalExperience = totalExperience;
	}
}
