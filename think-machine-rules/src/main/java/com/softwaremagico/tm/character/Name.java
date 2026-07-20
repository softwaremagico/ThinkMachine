package com.softwaremagico.tm.character;

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

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.json.ExcludeFromJson;

public class Name extends Element<Name> {

    private String customName;

    @ExcludeFromJson
    private final Gender gender;

    @ExcludeFromJson
    private final Faction faction;

    public Faction getFaction() {
        return faction;
    }

    public Name(String id, String name, String customName, String language, String moduleName, Gender gender, Faction faction) {
        super(id, name, null, language, moduleName);
        this.gender = gender;
        this.faction = faction;
        this.customName = customName;
    }

    public Name(String name, String language, String moduleName, Gender gender, Faction faction) {
        super(getId(name, faction, moduleName), name, null, language, moduleName);
        this.gender = gender;
        this.faction = faction;
        this.customName = name;
    }

    public Gender getGender() {
        return gender;
    }

    @Override
    public String getName() {
        if (customName != null) {
            return customName;
        }
        return super.getName();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    private static String getId(String name, Faction faction, String moduleName) {
        return name.replaceAll("\\s+", "_").toLowerCase() + (faction != null ? "_" + faction.getId() : "") +
                (moduleName != null ? "_" + moduleName.replaceAll("\\s+", "_").toLowerCase() : "");
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

}
