package com.softwaremagico.tm.character.cybernetics;

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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.characteristics.CharacteristicImprovement;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.skills.CyberneticSkill;

public class Device extends Element<Device> {
	private final int points;
	private final int incompatibility;
	private final String usability;
	private final String quality;
	private final String activation;
	private final String appearence;
	private final String others;

	private Map<String, CyberneticSkill> skillImprovements;
	private Map<String, CharacteristicImprovement> characteristicImprovents;

	public Device(String name, int points, int incompatibility, String usability, String quality, String activation, String appearence, String others) {
		super(null, name);
		this.points = points;
		this.incompatibility = incompatibility;
		this.usability = usability;
		this.quality = quality;
		this.activation = activation;
		this.appearence = appearence;
		this.others = others;
		skillImprovements = new HashMap<>();
		characteristicImprovents = new HashMap<>();
	}

	public int getPoints() {
		return points;
	}

	public int getIncompatibility() {
		return incompatibility;
	}

	public String getUsability() {
		return usability;
	}

	public String getQuality() {
		return quality;
	}

	public String getActivation() {
		return activation;
	}

	public String getAppearence() {
		return appearence;
	}

	public String getOthers() {
		return others;
	}

	public void addSkillImprovement(CyberneticSkill skillImprovement) {
		skillImprovements.put(skillImprovement.getName(), skillImprovement);
	}

	public CyberneticSkill getSkillImprovement(String skillName) {
		return skillImprovements.get(skillName);
	}

	public Set<String> getSkillImprovementsNames() {
		return skillImprovements.keySet();
	}

	public void addCharacteristicImprovement(CharacteristicImprovement characteristicImprovement) {
		characteristicImprovents.put(characteristicImprovement.getCharacteristic().getId(), characteristicImprovement);
	}

	public CharacteristicImprovement getCharacteristicImprovement(CharacteristicName characteristicName) {
		return characteristicImprovents.get(characteristicName);
	}
}
