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

import com.google.gson.*;
import com.softwaremagico.tm.character.Surname;

import java.lang.reflect.Type;

public class SurnameAdapter extends ElementAdapter<Surname> {
    private static final String CUSTOM_SURNAME = "customSurname";

    public SurnameAdapter(String language, String moduleName) {
        super(language, moduleName);
    }

    @Override
    public JsonElement serialize(Surname element, Type elementType, JsonSerializationContext jsonSerializationContext) {
        final JsonElement jsonObject = super.serialize(element, elementType, jsonSerializationContext);
        ((JsonObject) jsonObject).addProperty(CUSTOM_SURNAME, element.getCustomSurname());
        return jsonObject;
    }

    @Override
    public Surname deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new Surname(super.getElementId(jsonElement), getCustomSurname(jsonElement), getCustomSurname(jsonElement),
                getLanguage(), getModuleName(), null);
    }

    protected String getCustomSurname(JsonElement jsonElement) {
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        final JsonPrimitive customName = (JsonPrimitive) jsonObject.get(CUSTOM_SURNAME);
        if (customName == null) {
            return null;
        }
        return customName.getAsString();
    }
}
