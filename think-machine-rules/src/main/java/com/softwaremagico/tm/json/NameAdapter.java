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
import com.softwaremagico.tm.character.Gender;
import com.softwaremagico.tm.character.Name;

import java.lang.reflect.Type;

public class NameAdapter extends ElementAdapter<Name> {
    private static final String CUSTOM_NAME = "customName";
    private static final String GENDER = "gender";

    public NameAdapter(String language, String moduleName) {
        super(language, moduleName);
    }

    @Override
    public JsonElement serialize(Name element, Type elementType, JsonSerializationContext jsonSerializationContext) {
        final JsonElement jsonObject = super.serialize(element, elementType, jsonSerializationContext);
        ((JsonObject) jsonObject).addProperty(CUSTOM_NAME, element.getCustomName());
        ((JsonObject) jsonObject).addProperty(GENDER, element.getGender().name());
        return jsonObject;
    }

    @Override
    public Name deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new Name(super.getElementId(jsonElement), getCustomName(jsonElement), getCustomName(jsonElement),
                getLanguage(), getModuleName(), getGender(jsonElement), null);
    }

    protected String getCustomName(JsonElement jsonElement) {
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        final JsonPrimitive customName = (JsonPrimitive) jsonObject.get(CUSTOM_NAME);
        if (customName == null) {
            return null;
        }
        return customName.getAsString();
    }

    protected Gender getGender(JsonElement jsonElement) {
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        final JsonPrimitive gender = (JsonPrimitive) jsonObject.get(GENDER);
        if (gender == null) {
            return null;
        }
        return Gender.valueOf(gender.getAsString());
    }
}
