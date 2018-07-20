package com.softwaremagico.tm.character.race;

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

import java.lang.reflect.Field;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.log.MachineLog;

@SuppressWarnings("unused")
public class Race extends Element<Race> {
	private RaceCharacteristic strength = new RaceCharacteristic(CharacteristicName.STRENGTH);
	private RaceCharacteristic dexterity = new RaceCharacteristic(CharacteristicName.DEXTERITY);
	private RaceCharacteristic endurance = new RaceCharacteristic(CharacteristicName.ENDURANCE);
	private RaceCharacteristic wits = new RaceCharacteristic(CharacteristicName.WITS);
	private RaceCharacteristic perception = new RaceCharacteristic(CharacteristicName.PERCEPTION);
	private RaceCharacteristic tech = new RaceCharacteristic(CharacteristicName.TECH);
	private RaceCharacteristic presence = new RaceCharacteristic(CharacteristicName.PRESENCE);
	private RaceCharacteristic will = new RaceCharacteristic(CharacteristicName.WILL);
	private RaceCharacteristic faith = new RaceCharacteristic(CharacteristicName.FAITH);

	private RaceCharacteristic movement = new RaceCharacteristic(CharacteristicName.MOVEMENT);
	private RaceCharacteristic initiative = new RaceCharacteristic(CharacteristicName.INITIATIVE);
	private RaceCharacteristic defense = new RaceCharacteristic(CharacteristicName.DEFENSE);

	private int psi;
	private int theurgy;
	private int urge;
	private int hubris;

	private int cost;

	private final RaceRandomDefinitions randomDefinition;

	public Race(String id, String name) {
		super(id, name);
		randomDefinition = new RaceRandomDefinitions();
	}

	public Race(String name, int strength, int dexterity, int endurance, int wits, int perception, int tech, int presence, int will, int faith, int movement,
			int psi, int teurgy, int urge, int hubris, int cost) {
		this(null, name);
		setValue(CharacteristicName.STRENGTH, strength);
		setValue(CharacteristicName.DEXTERITY, dexterity);
		setValue(CharacteristicName.ENDURANCE, endurance);
		setValue(CharacteristicName.WITS, wits);
		setValue(CharacteristicName.PERCEPTION, perception);
		setValue(CharacteristicName.TECH, tech);
		setValue(CharacteristicName.PRESENCE, presence);
		setValue(CharacteristicName.WILL, will);
		setValue(CharacteristicName.FAITH, faith);
		setValue(CharacteristicName.MOVEMENT, movement);
		this.psi = psi;
		this.theurgy = teurgy;
		this.urge = urge;
		this.hubris = hubris;
		this.cost = cost;
	}

	public RaceCharacteristic getParameter(CharacteristicName characteristicName) {
		for (Field field : this.getClass().getDeclaredFields()) {
			if (field.getType().isAssignableFrom(RaceCharacteristic.class)) {
				RaceCharacteristic parameter;
				try {
					parameter = (RaceCharacteristic) field.get(this);
					if (parameter != null) {
						if (parameter.getCharacteristic().equals(characteristicName)) {
							return parameter;
						}
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// Not valid field.
				}
			}
		}
		return null;
	}

	public void setMaximumValue(CharacteristicName characteristicName, int maxValue) {
		try {
			getParameter(characteristicName).setMaximumValue(maxValue);
		} catch (NullPointerException npe) {
			MachineLog.severe(this.getClass().getName(), "Invalid maximum parameter '" + characteristicName + "'.");
		}
	}

	public void setValue(CharacteristicName characteristicName, int value) {
		try {
			getParameter(characteristicName).setValue(value);
		} catch (NullPointerException npe) {
			MachineLog.severe(this.getClass().getName(), "Invalid value parameter '" + characteristicName + "'.");
		}
	}

	public RaceCharacteristic get(CharacteristicName characteristicName) {
		return getParameter(characteristicName);
	}

	public int getPsi() {
		return psi;
	}

	public int getTheurgy() {
		return theurgy;
	}

	public int getUrge() {
		return urge;
	}

	public int getHubris() {
		return hubris;
	}

	public int getCost() {
		return cost;
	}

	public void setPsi(int psi) {
		this.psi = psi;
	}

	public void setTheurgy(int teurgy) {
		this.theurgy = teurgy;
	}

	public void setUrge(int urge) {
		this.urge = urge;
	}

	public void setHubris(int hubris) {
		this.hubris = hubris;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public RaceRandomDefinitions getRandomDefinition() {
		return randomDefinition;
	}
}
