package com.softwaremagico.tm.json;

/*-
 * #%L
 * Think Machine (Rules)
 * %%
 * Copyright (C) 2017 - 2019 Softwaremagico
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

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.occultism.OccultismPath;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.occultism.OccultismPower;
import com.softwaremagico.tm.log.MachineLog;

import java.lang.reflect.Type;
import java.util.List;

public class OccultismPowerAdapter extends ElementAdapter<OccultismPower> {

	public OccultismPowerAdapter(String language, String moduleName) {
		super(language, moduleName);
	}

	@Override
	public OccultismPower deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
		try {
			final String occultismPowerName = getElementId(jsonElement);
			final List<OccultismPath> occultismPaths = OccultismPathFactory.getInstance().getElements(super.getLanguage(), super.getModuleName());
			for (final OccultismPath occultismPath : occultismPaths) {
				if (occultismPath.getOccultismPowers().get(occultismPowerName) != null) {
					return occultismPath.getOccultismPowers().get(occultismPowerName);
				}
			}
		} catch (InvalidXmlElementException e) {
			MachineLog.errorMessage(this.getClass().getName(), e);
		}
		return null;
	}

}
