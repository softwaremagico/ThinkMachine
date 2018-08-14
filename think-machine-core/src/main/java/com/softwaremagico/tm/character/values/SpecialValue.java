package com.softwaremagico.tm.character.values;

/*-
 * #%L
 * Think Machine (Core)
 * %%
 * Copyright (C) 2017 - 2018 Softwaremagico
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

import java.util.Set;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.skills.SkillsDefinitionsFactory;

public class SpecialValue extends Element<SpecialValue> implements IValue {
	public static String VITALITY = "vitality";
	public static String WYRD = "wyrd";

	private final Set<IValue> affects;

	public SpecialValue(String id, String name, Set<IValue> affects) {
		super(id, name);
		this.affects = affects;
	}

	public Set<IValue> getAffects() {
		return affects;
	}

	public static IValue getValue(String valueName, String language) throws InvalidXmlElementException {
		try {
			// Is a characteristic?
			return CharacteristicsDefinitionFactory.getInstance().getElement(valueName, language);
		} catch (InvalidXmlElementException e) {
			// Is a skill??
			try {
				return SkillsDefinitionsFactory.getInstance().getElement(valueName, language);
			} catch (InvalidXmlElementException e2) {
				// Is something else?
				try {
					return SpecialValuesFactory.getInstance().getElement(valueName, language);
				} catch (InvalidXmlElementException e3) {
					throw new InvalidXmlElementException("Invalid value '" + valueName + "'.", e3);
				}
			}
		}
	}

}
