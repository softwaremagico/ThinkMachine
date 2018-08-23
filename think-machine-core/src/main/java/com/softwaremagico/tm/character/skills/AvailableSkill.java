package com.softwaremagico.tm.character.skills;

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

/**
 * An skill that already has been split in different generalizations.
 */
public class AvailableSkill extends Skill<AvailableSkill> {
	private Specialization specialization = null;
	private SkillDefinition skillDefinition;

	public AvailableSkill(SkillDefinition skillDefinition) {
		super(skillDefinition.getId(), skillDefinition.getName());
		this.skillDefinition = skillDefinition;
	}

	public AvailableSkill(SkillDefinition skillDefinition, Specialization specialization) {
		this(skillDefinition);
		this.specialization = specialization;
	}

	public Specialization getSpecialization() {
		return specialization;
	}

	public void setSpecialization(Specialization generalization) {
		this.specialization = generalization;
	}

	public SkillDefinition getSkillDefinition() {
		return skillDefinition;
	}

	public String getCompleteName() {
		return getCompleteName(getName(), getSpecialization());
	}

	public static String getCompleteName(String name, Specialization specialization) {
		if (specialization == null) {
			return name;
		}
		return name + " [" + specialization.getName() + "]";
	}

	@Override
	public String toString() {
		return getCompleteName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((specialization == null) ? 0 : specialization.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		AvailableSkill other = (AvailableSkill) obj;
		if (specialization == null) {
			if (other.specialization != null)
				return false;
		} else if (!specialization.equals(other.specialization))
			return false;
		return true;
	}

	public SkillRandomDefinitions getRandomDefinition() {
		if (getSpecialization() != null) {
			return getSpecialization().getRandomDefinition();
		}
		return getSkillDefinition().getRandomDefinition();
	}
}
