package com.softwaremagico.tm.json;

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

import java.lang.reflect.Type;
import java.util.Objects;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.character.skills.Specialization;
import com.softwaremagico.tm.log.MachineLog;

public class AvailableSkillAdapter extends ElementAdapter<AvailableSkill> {
	private final static String SPECIALIZATION = "specialization";

	protected AvailableSkillAdapter(String language) {
		super(language);
	}

	@Override
	public JsonElement serialize(AvailableSkill element, Type elementType, JsonSerializationContext jsonSerializationContext) {
		JsonElement jsonObject = super.serialize(element, elementType, jsonSerializationContext);
		if (element.getSpecialization() != null) {
			((JsonObject) jsonObject).addProperty(SPECIALIZATION, element.getSpecialization().getId());
		}
		return jsonObject;
	}

	@Override
	public AvailableSkill deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
		try {
			AvailableSkill availableSkill = AvailableSkillsFactory.getInstance().getElement(super.getElementId(jsonElement), super.getLanguage(jsonElement));
			String specializationId = getSpecialization(jsonElement);
			if (specializationId != null) {
				for (Specialization specialization : availableSkill.getSkillDefinition().getSpecializations()) {
					if (Objects.equals(specialization.getId(), specializationId)) {
						System.out.println(specialization);
						availableSkill.setSpecialization(specialization);
						break;
					}
				}
			}
			return availableSkill;
		} catch (InvalidXmlElementException e) {
			MachineLog.errorMessage(this.getClass().getName(), e);
			return null;
		}
	}

	protected String getSpecialization(JsonElement jsonElement) {
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		JsonPrimitive specialization = (JsonPrimitive) jsonObject.get(SPECIALIZATION);
		if (specialization == null) {
			return null;
		}
		return specialization.getAsString();
	}
}
