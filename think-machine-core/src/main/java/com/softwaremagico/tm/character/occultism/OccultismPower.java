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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.values.IValue;

public class OccultismPower extends Element<OccultismPower> {
	private final CharacteristicDefinition characteristic;
	private final List<IValue> values;
	private final int level;
	private final OccultismRange range;
	private final OccultismDuration duration;
	private final Integer cost;
	private final Set<TheurgyComponent> components;
	private boolean enabled;

	public OccultismPower(String id, String name, CharacteristicDefinition characteristic, List<IValue> values,
			int level, OccultismRange range, OccultismDuration duration, Integer cost, Set<TheurgyComponent> components) {
		super(id, name);
		this.characteristic = characteristic;
		this.values = values;
		this.level = level;
		this.range = range;
		this.duration = duration;
		this.cost = cost;
		this.components = components;
		enabled = true;
	}

	public OccultismPower(String id, String name, CharacteristicDefinition characteristic, List<IValue> values,
			int level, OccultismRange range, OccultismDuration duration, int cost, Set<TheurgyComponent> components,
			boolean enabled) {
		this(id, name, characteristic, values, level, range, duration, cost, components);
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

	public List<IValue> getValues() {
		return values;
	}

	public String getRoll() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(getCharacteristic().getAbbreviature());
		stringBuilder.append("+");
		for (int i = 0; i < values.size(); i++) {
			if (i > 0) {
				stringBuilder.append("/");
			}
			stringBuilder.append(values.get(i).getName());
		}
		return stringBuilder.toString();
	}

	public Set<TheurgyComponent> getComponents() {
		return components;
	}

	public String getComponentsRepresentation() {
		List<TheurgyComponent> sortedComponents = new ArrayList<>(getComponents());
		Collections.sort(sortedComponents);
		StringBuilder representation = new StringBuilder();
		for (TheurgyComponent theurgyComponent : sortedComponents) {
			representation.append(theurgyComponent.getAbbreviature());
		}
		return representation.toString();
	}

}
