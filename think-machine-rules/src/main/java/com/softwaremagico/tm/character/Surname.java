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

public class Surname extends Element<Surname> {

    private String customSurname;

    @ExcludeFromJson
    private final Faction faction;

    public Faction getFaction() {
        return faction;
    }

    public Surname(String id, String surname, String customSurname, String language, String moduleName, Faction faction) {
        super(id, surname, null, language, moduleName);
        this.faction = faction;
        this.customSurname = customSurname;
    }

    public Surname(String surname, String language, String moduleName, Faction faction) {
        super(getId(surname, faction, moduleName), surname, null, language, moduleName);
        this.faction = faction;
        this.customSurname = surname;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String getName() {
        if (customSurname != null) {
            return customSurname;
        }
        return super.getName();
    }

    private static String getId(String surname, Faction faction, String moduleName) {
        return surname.replaceAll("\\s+", "_").toLowerCase() + (faction != null ? "_" + faction : "") +
                (moduleName != null ? "_" + moduleName.replaceAll("\\s+", "_").toLowerCase() : "");
    }

    public String getCustomSurname() {
        return customSurname;
    }

    public void setCustomSurname(String customSurname) {
        this.customSurname = customSurname;
    }

}
