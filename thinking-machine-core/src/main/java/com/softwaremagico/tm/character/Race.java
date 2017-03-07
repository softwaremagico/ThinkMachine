package com.softwaremagico.tm.character;

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

import java.lang.reflect.Field;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.characteristics.CharacteristicValue;

public class Race extends Element<Race> {
	private final CharacteristicValue strength;
	private final CharacteristicValue dexterity;
	private final CharacteristicValue endurance;
	private final CharacteristicValue wits;
	private final CharacteristicValue perception;
	private final CharacteristicValue tech;
	private final CharacteristicValue presence;
	private final CharacteristicValue will;
	private final CharacteristicValue faith;

	private final CharacteristicValue movement;

	private final int psi;
	private final int teurgy;
	private final int urge;
	private final int hubris;

	private final int cost;

	public Race(String name, int strength, int dexterity, int endurance, int wits, int perception, int tech, int presence, int will,
			int faith, int movement, int psi, int teurgy, int urge, int hubris, int cost) {
		super(name);
		this.strength = new CharacteristicValue(CharacteristicName.STRENGTH, strength);
		this.dexterity = new CharacteristicValue(CharacteristicName.DEXTERITY, dexterity);
		this.endurance = new CharacteristicValue(CharacteristicName.ENDURANCE, endurance);
		this.wits = new CharacteristicValue(CharacteristicName.WITS, wits);
		this.perception = new CharacteristicValue(CharacteristicName.PERCEPTION, perception);
		this.tech = new CharacteristicValue(CharacteristicName.TECH, tech);
		this.presence = new CharacteristicValue(CharacteristicName.PRESENCE, presence);
		this.will = new CharacteristicValue(CharacteristicName.WILL, will);
		this.faith = new CharacteristicValue(CharacteristicName.FAITH, faith);
		this.psi = psi;
		this.teurgy = teurgy;
		this.urge = urge;
		this.hubris = hubris;
		this.movement = new CharacteristicValue(CharacteristicName.MOVEMENT, movement);
		this.cost = cost;
	}

	public CharacteristicValue getValue(CharacteristicName characteristicName) {
		for (Field field : this.getClass().getDeclaredFields()) {
			if (field.getType().isAssignableFrom(CharacteristicValue.class)) {
				CharacteristicValue value;
				try {
					value = (CharacteristicValue) field.get(this);
					if (value != null) {
						if (value.getCharacteristic().equals(characteristicName)) {
							return value;
						}
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// Not valid field.
				}
			}
		}
		return null;
	}

	public CharacteristicValue getStrength() {
		return strength;
	}

	public CharacteristicValue getDexterity() {
		return dexterity;
	}

	public CharacteristicValue getEndurance() {
		return endurance;
	}

	public CharacteristicValue getWits() {
		return wits;
	}

	public CharacteristicValue getPerception() {
		return perception;
	}

	public CharacteristicValue getTech() {
		return tech;
	}

	public CharacteristicValue getPresence() {
		return presence;
	}

	public CharacteristicValue getWill() {
		return will;
	}

	public CharacteristicValue getFaith() {
		return faith;
	}

	public CharacteristicValue getMovement() {
		return movement;
	}

	public int getPsi() {
		return psi;
	}

	public int getTeurgy() {
		return teurgy;
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
}
