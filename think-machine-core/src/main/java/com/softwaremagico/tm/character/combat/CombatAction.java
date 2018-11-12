package com.softwaremagico.tm.character.combat;

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

import java.util.Set;

import com.softwaremagico.tm.Element;

public class CombatAction extends Element<CombatAction> {
	private final String goal;
	private final String damage;
	private final String others;
	private final Set<CombatActionRequirement> requirements;

	public CombatAction(String name, String language, String goal, String damage, String others, Set<CombatActionRequirement> requirements) {
		super(null, name, language);
		this.goal = goal;
		this.damage = damage;
		this.others = others;
		this.requirements = requirements;
	}

	public String getGoal() {
		return goal;
	}

	public String getDamage() {
		return damage;
	}

	public String getOthers() {
		return others;
	}

	public Set<CombatActionRequirement> getRequirements() {
		return requirements;
	}

}
