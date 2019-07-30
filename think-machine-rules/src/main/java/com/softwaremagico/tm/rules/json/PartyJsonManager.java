package com.softwaremagico.tm.rules.json;

/*-
 * #%L
 * Think Machine (Core)
 * %%
 * Copyright (C) 2017 Softwaremagico
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

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.softwaremagico.tm.rules.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.rules.character.blessings.Blessing;
import com.softwaremagico.tm.rules.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.rules.character.cybernetics.CyberneticDevice;
import com.softwaremagico.tm.rules.character.equipment.armours.Armour;
import com.softwaremagico.tm.rules.character.equipment.shields.Shield;
import com.softwaremagico.tm.rules.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.rules.character.factions.Faction;
import com.softwaremagico.tm.rules.character.races.Race;
import com.softwaremagico.tm.rules.character.skills.AvailableSkill;
import com.softwaremagico.tm.rules.character.values.IValue;
import com.softwaremagico.tm.rules.party.Party;

public class PartyJsonManager {

	public static String toJson(Party party) {
		if (party != null) {
			final GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.setPrettyPrinting();
			gsonBuilder.setExclusionStrategies(new AnnotationExclusionStrategy()).create();
			gsonBuilder.registerTypeAdapter(IValue.class, new IValueSerializer<IValue>());
			gsonBuilder.registerTypeAdapter(Faction.class, new FactionAdapter());
			gsonBuilder.registerTypeAdapter(Blessing.class, new BlessingAdapter());
			gsonBuilder.registerTypeAdapter(AvailableBenefice.class, new AvailableBeneficeAdapter());
			gsonBuilder.registerTypeAdapter(AvailableSkill.class, new AvailableSkillAdapter());
			gsonBuilder.registerTypeAdapter(CharacteristicDefinition.class, new CharacteristicDefinitionAdapter());
			gsonBuilder.registerTypeAdapter(Race.class, new RaceAdapter());
			gsonBuilder.registerTypeAdapter(Weapon.class, new WeaponAdapter());
			gsonBuilder.registerTypeAdapter(Armour.class, new ArmourAdapter());
			gsonBuilder.registerTypeAdapter(Shield.class, new ShieldAdapter());
			gsonBuilder.registerTypeAdapter(CyberneticDevice.class, new CyberneticDeviceAdapter());
			// final Gson gson = new
			// GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
			final Gson gson = gsonBuilder.create();
			final String jsonText = gson.toJson(party);
			return jsonText;
		}
		return null;
	}

	public static Party fromJson(String jsonText) {
		if (jsonText != null && jsonText.length() > 0) {
			final GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.setPrettyPrinting();
			gsonBuilder.registerTypeAdapter(IValue.class, new InterfaceAdapter<IValue>());
			gsonBuilder.registerTypeAdapter(Faction.class, new FactionAdapter());
			gsonBuilder.registerTypeAdapter(Blessing.class, new BlessingAdapter());
			gsonBuilder.registerTypeAdapter(AvailableBenefice.class, new AvailableBeneficeAdapter());
			gsonBuilder.registerTypeAdapter(AvailableSkill.class, new AvailableSkillAdapter());
			gsonBuilder.registerTypeAdapter(CharacteristicDefinition.class, new CharacteristicDefinitionAdapter());
			gsonBuilder.registerTypeAdapter(Race.class, new RaceAdapter());
			gsonBuilder.registerTypeAdapter(Weapon.class, new WeaponAdapter());
			gsonBuilder.registerTypeAdapter(Armour.class, new ArmourAdapter());
			gsonBuilder.registerTypeAdapter(Shield.class, new ShieldAdapter());
			gsonBuilder.registerTypeAdapter(CyberneticDevice.class, new CyberneticDeviceAdapter());
			final Gson gson = gsonBuilder.create();

			final Party party = gson.fromJson(jsonText, Party.class);
			return party;
		}
		return null;
	}

	public static Party fromFile(String path) throws IOException {
		final String jsonText = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8.name());
		return fromJson(jsonText);
	}

	private static class IValueSerializer<T> implements JsonSerializer<T> {
		@Override
		public JsonElement serialize(T link, Type type, JsonSerializationContext context) {
			return context.serialize(link, link.getClass());
		}
	}
}
