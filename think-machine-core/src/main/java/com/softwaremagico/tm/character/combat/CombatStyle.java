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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.softwaremagico.tm.Element;

public class CombatStyle extends Element<CombatStyle> {
	private final static int COMBAT_SYTLE_COST = 5;
	private final CombatStyleGroup group;
	private final List<CombatStance> combatStances;
	private List<CombatAction> combatActions;

	public CombatStyle(String id, String name, String language, CombatStyleGroup group) {
		super(id, name, language);
		combatActions = new ArrayList<CombatAction>();
		this.group = group;
		combatStances = new ArrayList<>();
	}

	public int getCost() {
		return COMBAT_SYTLE_COST;
	}

	public CombatStyleGroup getGroup() {
		return group;
	}

	public void addCombatStance(CombatStance stance) {
		combatStances.add(stance);
		Collections.sort(combatStances);
	}

	public List<CombatStance> getCombatStances() {
		return Collections.unmodifiableList(combatStances);
	}

	public void addCombatAction(CombatAction combatAction) {
		combatActions.add(combatAction);
		Collections.sort(combatActions);
	}

	public List<CombatAction> getCombatActions() {
		return Collections.unmodifiableList(combatActions);
	}
}
