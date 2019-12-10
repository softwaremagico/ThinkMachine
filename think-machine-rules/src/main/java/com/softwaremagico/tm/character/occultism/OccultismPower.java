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
import com.softwaremagico.tm.json.ExcludeFromJson;

public class OccultismPower extends Element<OccultismPower> {
	@ExcludeFromJson
	private final CharacteristicDefinition characteristic;
	@ExcludeFromJson
	private final List<IValue> values;
	@ExcludeFromJson
	private final int level;
	@ExcludeFromJson
	private final OccultismRange range;
	@ExcludeFromJson
	private final OccultismDuration duration;
	@ExcludeFromJson
	private final Integer cost;
	@ExcludeFromJson
	private final Set<TheurgyComponent> components;
	@ExcludeFromJson
	private boolean enabled;

	public OccultismPower(String id, String name, String language, String moduleName,
			CharacteristicDefinition characteristic, List<IValue> values, int level, OccultismRange range,
			OccultismDuration duration, Integer cost, Set<TheurgyComponent> components) {
		super(id, name, language, moduleName);
		this.characteristic = characteristic;
		this.values = values;
		this.level = level;
		this.range = range;
		this.duration = duration;
		this.cost = cost;
		this.components = components;
		enabled = true;
	}

	public OccultismPower(String id, String name, String language, String moduleName,
			CharacteristicDefinition characteristic, List<IValue> values, int level, OccultismRange range,
			OccultismDuration duration, int cost, Set<TheurgyComponent> components, boolean enabled) {
		this(id, name, language, moduleName, characteristic, values, level, range, duration, cost, components);
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
		final StringBuilder stringBuilder = new StringBuilder();
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
		final List<TheurgyComponent> sortedComponents = new ArrayList<>(getComponents());
		Collections.sort(sortedComponents);
		final StringBuilder representation = new StringBuilder();
		for (final TheurgyComponent theurgyComponent : sortedComponents) {
			representation.append(theurgyComponent.getAbbreviature());
		}
		return representation.toString();
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public int compareTo(OccultismPower element) {
		if (getLevel() != element.getLevel()) {
			return Integer.compare(getLevel(), element.getLevel());
		}
		return super.compareTo(element);
	}

}
