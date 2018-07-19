package com.softwaremagico.tm.character.skills;

import java.util.HashSet;
import java.util.Set;

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

public class SkillDefinition extends Skill<SkillDefinition> {
	public final static String FACTORION_LORE_ID = "factionLore";
	public final static String PLANETARY_LORE_ID = "planetaryLore";

	private boolean fromGuild;
	private boolean natural = false;
	private Set<Specialization> specializations = new HashSet<>();
	private SkillGroup skillGroup;
	// Number of times that a skill (generalizable) is shown in the PDF.
	private int numberToShow = 1;

	private final SkillDefinitionRandomDefinitions randomDefinition;

	public SkillDefinition(String id, String name) {
		super(id, name.trim());
		randomDefinition = new SkillDefinitionRandomDefinitions();
	}

	public boolean isFromGuild() {
		return fromGuild;
	}

	public boolean isNatural() {
		return natural;
	}

	public boolean isSpecializable() {
		return !specializations.isEmpty();
	}

	public void setFromGuild(boolean fromGuild) {
		this.fromGuild = fromGuild;
	}

	public SkillGroup getSkillGroup() {
		return skillGroup;
	}

	public void setSkillGroup(SkillGroup skillGroup) {
		if (skillGroup == null) {
			throw new RuntimeException("Skill group cannot be null in skill '" + this + "'");
		}
		this.skillGroup = skillGroup;
	}

	public void setNatural(boolean natural) {
		this.natural = natural;
	}

	public Set<Specialization> getSpecializations() {
		return specializations;
	}

	public void setSpecializations(Set<Specialization> specializations) {
		this.specializations = specializations;
	}

	public int getNumberToShow() {
		return numberToShow;
	}

	public void setNumberToShow(int numberToShow) {
		this.numberToShow = numberToShow;
	}

	@Override
	public String toString() {
		return super.toString() + " (" + skillGroup + ") " + getSpecializations();
	}

	public SkillDefinitionRandomDefinitions getRandomDefinition() {
		return randomDefinition;
	}
}
