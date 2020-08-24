package com.softwaremagico.tm.json;

/*-
 * #%L
 * Think Machine (Rules)
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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.cybernetics.CyberneticDeviceFactory;
import com.softwaremagico.tm.character.cybernetics.CyberneticDeviceTrait;
import com.softwaremagico.tm.character.cybernetics.SelectedCyberneticDevice;
import com.softwaremagico.tm.log.MachineLog;

public class SelectedCyberneticDeviceAdapter extends ElementAdapter<SelectedCyberneticDevice> {
	private static final String CUSTOMIZATIONS = "customizations";

	protected SelectedCyberneticDeviceAdapter(String language, String moduleName) {
		super(language, moduleName);
	}

	@SuppressWarnings("unchecked")
	public static List<CyberneticDeviceTrait> parseCustomizations(String name, JsonObject jsonObject, JsonDeserializationContext context) {
		if (jsonObject.has(CUSTOMIZATIONS)) {
			final JsonArray customizations = (JsonArray) jsonObject.get(CUSTOMIZATIONS);
			if (customizations != null && !customizations.isJsonNull() && customizations.size() > 0) {
				final Type cyberneticDeviceTraitType = new TypeToken<ArrayList<CyberneticDeviceTrait>>() {
				}.getType();
				return (List<CyberneticDeviceTrait>) context.deserialize(jsonObject.get(name), cyberneticDeviceTraitType);
			}
		}
		return new ArrayList<>();
	}

	@Override
	public JsonElement serialize(SelectedCyberneticDevice selectedCyberneticDevice, Type elementType, JsonSerializationContext jsonSerializationContext) {
		final JsonElement jsonObject = super.serialize(selectedCyberneticDevice, elementType, jsonSerializationContext);
		((JsonObject) jsonObject).add(CUSTOMIZATIONS, jsonSerializationContext.serialize(selectedCyberneticDevice.getCustomizations()));
		return jsonObject;
	}

	@Override
	public SelectedCyberneticDevice deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
			throws JsonParseException {
		try {
			final SelectedCyberneticDevice selectedCyberneticDevice = new SelectedCyberneticDevice(
					CyberneticDeviceFactory.getInstance().getElement(super.getElementId(jsonElement), super.getLanguage(), super.getModuleName()));
			selectedCyberneticDevice.setCustomizations(parseCustomizations(CUSTOMIZATIONS, (JsonObject) jsonElement, jsonDeserializationContext));
			return selectedCyberneticDevice;
		} catch (InvalidXmlElementException e) {
			MachineLog.errorMessage(this.getClass().getName(), e);
			return null;
		}
	}
}
