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

import com.google.gson.*;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.Name;
import com.softwaremagico.tm.character.Surname;
import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.cybernetics.CyberneticDevice;
import com.softwaremagico.tm.character.cybernetics.CyberneticDeviceTrait;
import com.softwaremagico.tm.character.cybernetics.SelectedCyberneticDevice;
import com.softwaremagico.tm.character.equipment.DamageType;
import com.softwaremagico.tm.character.equipment.armours.Armour;
import com.softwaremagico.tm.character.equipment.armours.ArmourSpecification;
import com.softwaremagico.tm.character.equipment.shields.Shield;
import com.softwaremagico.tm.character.equipment.weapons.Accessory;
import com.softwaremagico.tm.character.equipment.weapons.Ammunition;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.occultism.OccultismPower;
import com.softwaremagico.tm.character.occultism.OccultismType;
import com.softwaremagico.tm.character.planets.Planet;
import com.softwaremagico.tm.character.races.Race;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.SkillDefinition;
import com.softwaremagico.tm.character.values.IValue;
import com.softwaremagico.tm.character.values.SpecialValue;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CharacterJsonManager extends JsonManager {

    public static String toJson(CharacterPlayer characterPlayer) {
        if (characterPlayer != null) {
            final GsonBuilder gsonBuilder = initGsonBuilder(characterPlayer.getLanguage(), characterPlayer.getModuleName());
            gsonBuilder.setExclusionStrategies(new AnnotationExclusionStrategy()).create();
            gsonBuilder.registerTypeAdapter(IValue.class, new IValueSerializer<IValue>());
            final Gson gson = gsonBuilder.create();
            return gson.toJson(characterPlayer);
        }
        return null;
    }

    public static CharacterPlayer fromJson(String jsonText) throws InvalidJsonException {
        if (jsonText != null && jsonText.length() > 0) {
            final String language = getLanguage(jsonText);
            final String moduleName = getModuleName(jsonText);

            final Gson gson = initGsonBuilder(language, moduleName).create();
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
                        realNames.add(new Name(name.getId(), name.getName(), name.getCustomName(), language, moduleName, characterPlayer.getInfo().getGender(),
                                null));
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
                // Custom surname
                if (!isFactionSurname) {
                    characterPlayer.getInfo()
                            .setSurname(new Surname(characterPlayer.getInfo().getSurname().getId(), characterPlayer.getInfo().getSurname().getName(),
                                    characterPlayer.getInfo().getSurname().getCustomSurname(), language, moduleName, characterPlayer.getFaction()));
                }
            }
            if (!characterPlayer.checkIsOfficial()) {
                characterPlayer.getSettings().setOnlyOfficialAllowed(false);
            }
            return characterPlayer;
        }
        return null;
    }

    private static GsonBuilder initGsonBuilder(final String language, final String moduleName) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(IValue.class, new InterfaceAdapter<IValue>());
        gsonBuilder.registerTypeAdapter(Faction.class, new FactionAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(Blessing.class, new BlessingAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(AvailableBenefice.class, new AvailableBeneficeAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(AvailableSkill.class, new AvailableSkillAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(Characteristic.class, new CharacteristicAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(CharacteristicDefinition.class, new CharacteristicDefinitionAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(Race.class, new RaceAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(Planet.class, new PlanetAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(Weapon.class, new WeaponAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(Armour.class, new ArmourAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(Shield.class, new ShieldAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(CyberneticDevice.class, new CyberneticDeviceAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(CyberneticDeviceTrait.class, new CyberneticDeviceTraitAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(SelectedCyberneticDevice.class, new SelectedCyberneticDeviceAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(OccultismPower.class, new OccultismPowerAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(Accessory.class, new AccessoryAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(DamageType.class, new DamageTypeAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(Ammunition.class, new AmmunitionAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(SpecialValue.class, new SpecialValueAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(OccultismType.class, new OccultismTypeAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(SkillDefinition.class, new SkillDefinitionAdapter(language, moduleName));
        gsonBuilder.registerTypeAdapter(ArmourSpecification.class, new ArmourSpecificationAdapter(language, moduleName));

        return gsonBuilder;
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
