package com.softwaremagico.tm.json;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.Name;
import com.softwaremagico.tm.character.Surname;
import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.cybernetics.CyberneticDevice;
import com.softwaremagico.tm.character.equipment.armours.Armour;
import com.softwaremagico.tm.character.equipment.shields.Shield;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.occultism.OccultismPower;
import com.softwaremagico.tm.character.planets.Planet;
import com.softwaremagico.tm.character.races.Race;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.values.IValue;

public class CharacterJsonManager extends JsonManager {

	public static String toJson(CharacterPlayer characterPlayer) {
		if (characterPlayer != null) {
			final GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.setPrettyPrinting();
			gsonBuilder.setExclusionStrategies(new AnnotationExclusionStrategy()).create();
			gsonBuilder.registerTypeAdapter(IValue.class, new IValueSerializer<IValue>());
			gsonBuilder.registerTypeAdapter(Faction.class, new FactionAdapter(characterPlayer.getLanguage(), characterPlayer.getModuleName()));
			gsonBuilder.registerTypeAdapter(Blessing.class, new BlessingAdapter(characterPlayer.getLanguage(), characterPlayer.getModuleName()));
			gsonBuilder.registerTypeAdapter(AvailableBenefice.class,
					new AvailableBeneficeAdapter(characterPlayer.getLanguage(), characterPlayer.getModuleName()));
			gsonBuilder.registerTypeAdapter(AvailableSkill.class, new AvailableSkillAdapter(characterPlayer.getLanguage(), characterPlayer.getModuleName()));
			gsonBuilder.registerTypeAdapter(CharacteristicDefinition.class,
					new CharacteristicDefinitionAdapter(characterPlayer.getLanguage(), characterPlayer.getModuleName()));
			gsonBuilder.registerTypeAdapter(Race.class, new RaceAdapter(characterPlayer.getLanguage(), characterPlayer.getModuleName()));
			gsonBuilder.registerTypeAdapter(Planet.class, new PlanetAdapter(characterPlayer.getLanguage(), characterPlayer.getModuleName()));
			gsonBuilder.registerTypeAdapter(Weapon.class, new WeaponAdapter(characterPlayer.getLanguage(), characterPlayer.getModuleName()));
			gsonBuilder.registerTypeAdapter(Armour.class, new ArmourAdapter(characterPlayer.getLanguage(), characterPlayer.getModuleName()));
			gsonBuilder.registerTypeAdapter(Shield.class, new ShieldAdapter(characterPlayer.getLanguage(), characterPlayer.getModuleName()));
			gsonBuilder.registerTypeAdapter(CyberneticDevice.class,
					new CyberneticDeviceAdapter(characterPlayer.getLanguage(), characterPlayer.getModuleName()));
			gsonBuilder.registerTypeAdapter(OccultismPower.class, new OccultismPowerAdapter(characterPlayer.getLanguage(), characterPlayer.getModuleName()));
			// final Gson gson = new
			// GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
			final Gson gson = gsonBuilder.create();
			final String jsonText = gson.toJson(characterPlayer);
			return jsonText;
		}
		return null;
	}

	public static CharacterPlayer fromJson(String jsonText) throws InvalidJsonException {
		if (jsonText != null && jsonText.length() > 0) {
			final String language = getLanguage(jsonText);
			final String moduleName = getModuleName(jsonText);
			final GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.setPrettyPrinting();
			gsonBuilder.registerTypeAdapter(IValue.class, new InterfaceAdapter<IValue>());
			gsonBuilder.registerTypeAdapter(Faction.class, new FactionAdapter(language, moduleName));
			gsonBuilder.registerTypeAdapter(Blessing.class, new BlessingAdapter(language, moduleName));
			gsonBuilder.registerTypeAdapter(AvailableBenefice.class, new AvailableBeneficeAdapter(language, moduleName));
			gsonBuilder.registerTypeAdapter(AvailableSkill.class, new AvailableSkillAdapter(language, moduleName));
			gsonBuilder.registerTypeAdapter(CharacteristicDefinition.class, new CharacteristicDefinitionAdapter(language, moduleName));
			gsonBuilder.registerTypeAdapter(Race.class, new RaceAdapter(language, moduleName));
			gsonBuilder.registerTypeAdapter(Planet.class, new PlanetAdapter(language, moduleName));
			gsonBuilder.registerTypeAdapter(Weapon.class, new WeaponAdapter(language, moduleName));
			gsonBuilder.registerTypeAdapter(Armour.class, new ArmourAdapter(language, moduleName));
			gsonBuilder.registerTypeAdapter(Shield.class, new ShieldAdapter(language, moduleName));
			gsonBuilder.registerTypeAdapter(CyberneticDevice.class, new CyberneticDeviceAdapter(language, moduleName));
			gsonBuilder.registerTypeAdapter(OccultismPower.class, new OccultismPowerAdapter(language, moduleName));
			final Gson gson = gsonBuilder.create();
			final CharacterPlayer characterPlayer = gson.fromJson(jsonText, CharacterPlayer.class);
			// Update names and surnames
			final List<Name> realNames = new ArrayList<>();
			if (characterPlayer.getInfo().getNames() != null) {
				// Check existing name.
				for (final Name name : characterPlayer.getInfo().getNames()) {
					boolean isFactionName = false;
					for (final Name factionName : FactionsFactory.getInstance().getAllNames(characterPlayer.getFaction(),
							characterPlayer.getInfo().getGender())) {
						if (Objects.equals(name.getId(), factionName.getId())) {
							realNames.add(factionName);
							isFactionName = true;
							break;
						}
					}
					// Custom name
					if (!isFactionName) {
						realNames.add(new Name(name.getId(), name.getName(), language, moduleName, characterPlayer.getInfo().getGender(), null));
					}
				}
				characterPlayer.getInfo().setNames(realNames);
			}
			if (characterPlayer.getInfo().getSurname() != null) {
				boolean isFactionSurname = false;
				for (final Surname factionSurname : FactionsFactory.getInstance().getAllSurnames(characterPlayer.getFaction())) {
					if (Objects.equals(characterPlayer.getInfo().getSurname().getId(), factionSurname.getId())) {
						characterPlayer.getInfo().setSurname(factionSurname);
						isFactionSurname = true;
						break;
					}
				}
				// Custom name
				if (!isFactionSurname) {
					characterPlayer.getInfo().setSurname(new Surname(characterPlayer.getInfo().getSurname().getId(),
							characterPlayer.getInfo().getSurname().getName(), language, moduleName, characterPlayer.getFaction()));
				}
			}
			return characterPlayer;
		}
		return null;
	}

	public static CharacterPlayer fromFile(String path) throws IOException, InvalidJsonException {
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
