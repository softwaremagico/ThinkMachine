package com.softwaremagico.tm.json;

import com.google.gson.*;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.log.MachineLog;

import java.lang.reflect.Type;

/*-
 * #%L
 * Think Machine (Core)
 * %%
 * Copyright (C) 2017 - 2020 Softwaremagico
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

public class CharacteristicAdapter extends ElementAdapter<Characteristic> {
	private static final String VALUE = "value";

	public CharacteristicAdapter(String language, String moduleName) {
		super(language, moduleName);
	}

	private int getValue(JsonElement jsonElement) {
		final JsonObject jsonObject = jsonElement.getAsJsonObject();
		final JsonPrimitive elementId = (JsonPrimitive) jsonObject.get(VALUE);
		if (elementId == null) {
			return 0;
		}
		return elementId.getAsInt();
	}

	@Override
	public JsonElement serialize(Characteristic characteristic, Type elementType, JsonSerializationContext jsonSerializationContext) {
		final JsonElement jsonObject = super.serialize(characteristic, elementType, jsonSerializationContext);
		((JsonObject) jsonObject).addProperty(VALUE, characteristic.getValue());
		return jsonObject;
	}

	@Override
	public Characteristic deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
		try {
			final Characteristic characteristic = new Characteristic(
					CharacteristicsDefinitionFactory.getInstance().getElement(super.getElementId(jsonElement), super.getLanguage(), super.getModuleName()));
			characteristic.setValue(getValue(jsonElement));
			return characteristic;
		} catch (InvalidXmlElementException e) {
			MachineLog.errorMessage(this.getClass().getName(), e);
			return null;
		}
	}
}
