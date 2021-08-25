package com.softwaremagico.tm.cache;

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
import com.softwaremagico.tm.random.selectors.IRandomPreference;
import org.reflections.Reflections;

import java.lang.reflect.Type;
import java.util.Set;

public class RandomPreferenceAdapter implements JsonSerializer<IRandomPreference>, JsonDeserializer<IRandomPreference> {
    private static final String PREFERENCE = "preference";
    private static final String VALUE = "value";
    private final Set<Class<? extends IRandomPreference>> classes;

    public RandomPreferenceAdapter() {
        final Reflections reflections = new Reflections("com.softwaremagico.tm");
        classes = reflections.getSubTypesOf(IRandomPreference.class);
    }


    @Override
    public JsonElement serialize(IRandomPreference element, Type elementType, JsonSerializationContext jsonSerializationContext) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(PREFERENCE, element.getClass().getSimpleName());
        jsonObject.addProperty(VALUE, element.name());
        return jsonObject;
    }

    @Override
    public IRandomPreference deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        for (final Class<? extends IRandomPreference> preference : classes) {
            if (preference.getSimpleName().equals(jsonObject.get(PREFERENCE).getAsString())) {
                for (final Object constant : preference.getEnumConstants()) {
                    if (constant.toString().equals(jsonObject.get(VALUE).getAsString())) {
                        return (IRandomPreference) constant;
                    }
                }
            }
        }
        return null;
    }
}
