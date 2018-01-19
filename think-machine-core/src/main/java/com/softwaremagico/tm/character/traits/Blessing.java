package com.softwaremagico.tm.character.traits;

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

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.skills.SkillDefinition;

public class Blessing extends Element<Blessing> {
	private Integer cost;
	private Integer bonification;
	private SkillDefinition skill;
	private CharacteristicDefinition characteristic;
	private String situation;

	public Blessing(String id, String name) {
		super(id, name);
	}

	public Blessing(String name, Integer cost, Integer bonification, String situation) {
		this(null, name);
		this.cost = cost;
		this.bonification = bonification;
		this.situation = situation;
	}

	public Integer getCost() {
		return cost;
	}

	public Integer getBonification() {
		return bonification;
	}

	public String getTrait() {
		if (skill != null) {
			return skill.getName();
		}
		if (characteristic != null) {
			return characteristic.getName();
		}
		return "";
	}

	public String getSituation() {
		return situation;
	}

	protected void setCost(Integer cost) {
		this.cost = cost;
	}

	public void setBonification(Integer bonification) {
		this.bonification = bonification;
	}

	protected void setSituation(String situation) {
		this.situation = situation;
	}

	public SkillDefinition getSkill() {
		return skill;
	}

	public void setSkill(SkillDefinition skill) {
		this.skill = skill;
	}

	public CharacteristicDefinition getCharacteristic() {
		return characteristic;
	}

	public void setCharacteristic(CharacteristicDefinition characteristic) {
		this.characteristic = characteristic;
	}

}
