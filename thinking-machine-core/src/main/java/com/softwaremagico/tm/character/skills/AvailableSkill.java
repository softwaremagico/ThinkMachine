package com.softwaremagico.tm.character.skills;

/*-
 * #%L
 * The Thinking Machine (Core)
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

public class AvailableSkill extends Skill<AvailableSkill> {
	private boolean fromGuild;
	private boolean natural = false;
	private boolean generalizable;
	private String generalization = null;
	private int indexOfGeneralization = 0;
	private String skillId;
	private SkillGroup skillGroup;

	public AvailableSkill(String name) {
		super(name);
	}

	public AvailableSkill(String skillId, String name) {
		this(name.trim());
		this.skillId = skillId;
	}

	public boolean isFromGuild() {
		return fromGuild;
	}

	public boolean isNatural() {
		return natural;
	}

	public boolean isGeneralizable() {
		return generalizable;
	}

	public String getGeneralization() {
		return generalization;
	}

	public void setGeneralization(String generalization) {
		this.generalization = generalization;
	}

	public int getIndexOfGeneralization() {
		return indexOfGeneralization;
	}

	public void setIndexOfGeneralization(int indexOfGeneralization) {
		this.indexOfGeneralization = indexOfGeneralization;
	}

	public String getSkillId() {
		return skillId;
	}

	public void setGeneralizable(boolean generalizable) {
		this.generalizable = generalizable;
	}

	public void setFromGuild(boolean fromGuild) {
		this.fromGuild = fromGuild;
	}

	public SkillGroup getSkillGroup() {
		return skillGroup;
	}

	public void setSkillGroup(SkillGroup skillGroup) {
		if (skillGroup == null) {
			throw new RuntimeException("Skill group cannot be null");
		}
		this.skillGroup = skillGroup;
	}

	public void setNatural(boolean natural) {
		this.natural = natural;
	}

}
