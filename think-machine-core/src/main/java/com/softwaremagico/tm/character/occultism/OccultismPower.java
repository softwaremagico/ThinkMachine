package com.softwaremagico.tm.character.occultism;

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

import java.util.List;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.skills.AvailableSkill;

public class OccultismPower extends Element<OccultismPower> {
	private final CharacteristicDefinition characteristic;
	private final List<AvailableSkill> skills;
	private final int level;
	private final OccultismRange range;
	private final OccultismDuration duration;
	private final String requirements;
	private final Integer cost;
	private boolean enabled;

	public OccultismPower(String id, String name, CharacteristicDefinition characteristic, List<AvailableSkill> skills,
			int level, OccultismRange range, OccultismDuration duration, String requirements, Integer cost) {
		super(id, name);
		this.characteristic = characteristic;
		this.skills = skills;
		this.level = level;
		this.range = range;
		this.duration = duration;
		this.requirements = requirements;
		this.cost = cost;
		enabled = true;
	}

	public OccultismPower(String id, String name, CharacteristicDefinition characteristic, List<AvailableSkill> skills,
			int level, OccultismRange range, OccultismDuration duration, String requirements, int cost, boolean enabled) {
		this(id, name, characteristic, skills, level, range, duration, requirements, cost);
		setEnabled(enabled);
	}

	public int getLevel() {
		return level;
	}

	public OccultismRange getRange() {
		return range;
	}

	public OccultismDuration getDuration() {
		return duration;
	}

	public String getRequirements() {
		return requirements;
	}

	public Integer getCost() {
		return cost;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public CharacteristicDefinition getCharacteristic() {
		return characteristic;
	}

	public List<AvailableSkill> getSkills() {
		return skills;
	}

	public String getRoll() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(getCharacteristic().getAbbreviature());
		stringBuilder.append("+");
		for (int i = 0; i < skills.size(); i++) {
			if (i > 0) {
				stringBuilder.append("/");
			}
			stringBuilder.append(skills.get(i).getName());
		}
		return stringBuilder.toString();
	}

}
