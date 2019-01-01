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
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.skills.CyberneticSkill;

public class CyberneticDevice extends Element<CyberneticDevice> {
	private final int points;
	private final int incompatibility;
	private final int cost;
	private final int techLevel;
	private final CyberneticDeviceTrait usability;
	private final CyberneticDeviceTrait quality;
	private final CyberneticDeviceTrait visibility;
	private final CyberneticDeviceTrait material;
	private final CyberneticDeviceTrait attached;
	private final CyberneticDeviceTrait power;
	private final String others;
	private final CyberneticDevice requirement;
	private final Weapon weapon;
	private final boolean proscribed;

	private Map<String, CyberneticSkill> skillImprovements;
	private Map<String, CharacteristicImprovement> characteristicImprovents;

	public CyberneticDevice(String id, String name, String language, int points, int incompatibility, int cost, int techLevel, CyberneticDeviceTrait usability,
			CyberneticDeviceTrait quality, CyberneticDeviceTrait visibility, CyberneticDeviceTrait material, CyberneticDeviceTrait attached,
			CyberneticDeviceTrait power, boolean proscribed, String others, CyberneticDevice requirement, Weapon weapon) {
		super(id, name, language);
		this.points = points;
		this.incompatibility = incompatibility;
		this.cost = cost;
		this.techLevel = techLevel;
		this.usability = usability;
		this.quality = quality;
		this.visibility = visibility;
		this.material = material;
		this.attached = attached;
		this.power = power;
		this.others = others;
		this.requirement = requirement;
		this.proscribed = proscribed;
		this.weapon = weapon;
		skillImprovements = new HashMap<>();
		characteristicImprovents = new HashMap<>();
	}

	public CyberneticDevice(String id, String name, String language, int points, int incompatibility, int cost, int techLevel, CyberneticDeviceTrait usability,
			CyberneticDeviceTrait quality, CyberneticDeviceTrait visibility, CyberneticDeviceTrait material, CyberneticDeviceTrait attached,
			CyberneticDeviceTrait power, boolean proscribed, String others, CyberneticDevice requirement) {
		this(id, name, language, points, incompatibility, cost, techLevel, usability, quality, visibility, material, attached, power, proscribed, others,
				requirement, null);
	}

	public int getPoints() {
		return points;
	}

	public int getIncompatibility() {
		return incompatibility;
	}

	public CyberneticDeviceTrait getUsability() {
		return usability;
	}

	public CyberneticDeviceTrait getQuality() {
		return quality;
	}

	public CyberneticDeviceTrait getVisibility() {
		return visibility;
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

	public CyberneticDevice getRequirement() {
		return requirement;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public CyberneticDeviceTrait getMaterial() {
		return material;
	}

	public CyberneticDeviceTrait getAttached() {
		return attached;
	}

	public CyberneticDeviceTrait getPower() {
		return power;
	}

	public boolean isProscribed() {
		return proscribed;
	}

	public int getCost() {
		return cost;
	}

	public int getTechLevel() {
		return techLevel;
	}
}
