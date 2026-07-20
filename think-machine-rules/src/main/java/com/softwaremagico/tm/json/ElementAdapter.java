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

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.softwaremagico.tm.Element;

public abstract class ElementAdapter<E extends Element<E>> implements JsonSerializer<E>, JsonDeserializer<E> {
	private static final String ID = "id";
	private final String moduleName;
	private final String language;

	protected ElementAdapter(String language, String moduleName) {
		this.language = language;
		this.moduleName = moduleName;
	}

	protected String getElementId(JsonElement jsonElement) {
		final JsonObject jsonObject = jsonElement.getAsJsonObject();
		final JsonPrimitive elementId = (JsonPrimitive) jsonObject.get(ID);
		if (elementId == null) {
			return null;
		}
		return elementId.getAsString();
	}

	protected String getLanguage() {
		return language;
	}

	protected String getModuleName() {
		return moduleName;
	}

	@Override
	public JsonElement serialize(E element, Type elementType, JsonSerializationContext jsonSerializationContext) {
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(ID, element.getId());
		return jsonObject;
	}

}
