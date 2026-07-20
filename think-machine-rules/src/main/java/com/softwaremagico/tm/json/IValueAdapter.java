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
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.occultism.OccultismType;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.character.skills.SkillsDefinitionsFactory;
import com.softwaremagico.tm.character.values.IValue;
import com.softwaremagico.tm.character.values.SpecialValuesFactory;
import com.softwaremagico.tm.log.MachineLog;

import java.lang.reflect.Type;

public class IValueAdapter implements JsonSerializer<IValue>, JsonDeserializer<IValue> {
    private static final String CLASS = "class";
    private static final String ID = "id";
    private static final String SPECIALIZATION = "specialization";
    private final String moduleName;
    private final String language;

    public IValueAdapter(String language, String moduleName) {
        this.language = language;
        this.moduleName = moduleName;
    }


    protected String getLanguage() {
        return language;
    }

    protected String getModuleName() {
        return moduleName;
    }

    @Override
    public JsonElement serialize(IValue element, Type elementType, JsonSerializationContext jsonSerializationContext) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(CLASS, element.getClass().getSimpleName());
        jsonObject.addProperty(ID, element.getId());
        if (element instanceof AvailableSkill) {
            jsonObject.addProperty(SPECIALIZATION, ((AvailableSkill) element).getSpecialization().getId());
        }
        return jsonObject;
    }

    protected String getSpecialization(JsonElement jsonElement) {
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        final JsonPrimitive specialization = (JsonPrimitive) jsonObject.get(SPECIALIZATION);
        if (specialization == null) {
            return null;
        }
        return specialization.getAsString();
    }

    protected String getElementId(JsonElement jsonElement) {
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        final JsonPrimitive elementId = (JsonPrimitive) jsonObject.get(ID);
        if (elementId == null) {
            return null;
        }
        return elementId.getAsString();
    }

    @Override
    public IValue deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        try {
            if (jsonObject.get(CLASS) == null) {
                //Theurgy as IValue uses also the OccultismTypeAdapter and has no class when created.
                final OccultismType occultismType = OccultismTypeFactory.getInstance().getElement(getElementId(jsonElement), getLanguage(), getModuleName());
                if (occultismType == null) {
                    throw new JsonParseException("Invalid element '" + jsonElement + " '.");
                }
                return occultismType;
            }
            switch (jsonObject.get(CLASS).getAsString()) {
                case "AvailableSkill":
                    return AvailableSkillsFactory.getInstance().getElement(getElementId(jsonElement), getSpecialization(jsonElement),
                            getLanguage(), getModuleName());
                case "Characteristic":
                    return new Characteristic(
                            CharacteristicsDefinitionFactory.getInstance().getElement(getElementId(jsonElement), getLanguage(), getModuleName()));
                case "CharacteristicDefinition":
                    return CharacteristicsDefinitionFactory.getInstance().getElement(getElementId(jsonElement), getLanguage(), getModuleName());
                case "OccultismType":
                    return OccultismTypeFactory.getInstance().getElement(getElementId(jsonElement), getLanguage(), getModuleName());
                case "SkillDefinition":
                    return SkillsDefinitionsFactory.getInstance().getElement(getElementId(jsonElement), getLanguage(), getModuleName());
                case "SpecialValue":
                    return SpecialValuesFactory.getInstance().getElement(getElementId(jsonElement), getLanguage(), getModuleName());
                default:
                    throw new JsonParseException("Invalid element '" + jsonObject.get(CLASS).getAsString() + " '.");
            }
        } catch (InvalidXmlElementException e) {
            MachineLog.errorMessage(this.getClass().getName(), e);
            throw new JsonParseException("Invalid element '" + jsonObject.get(CLASS).getAsString() + " '.");
        }
    }
}
