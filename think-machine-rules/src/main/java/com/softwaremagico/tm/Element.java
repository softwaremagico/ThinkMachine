package com.softwaremagico.tm;

import com.google.common.base.Objects;
import com.softwaremagico.tm.json.ExcludeFromJson;
import com.softwaremagico.tm.random.definition.RandomElementDefinition;

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

public class Element<T extends Element<?>> implements Comparable<T> {
    public static final String DEFAULT_NULL_ID = "null";

    private final String id;

    @ExcludeFromJson
    private final String name;

    @ExcludeFromJson
    private final String description;

    @ExcludeFromJson
    private final String moduleName;

    @ExcludeFromJson
    private final String language;

    @ExcludeFromJson
    private final RandomElementDefinition randomDefinition;

    @ExcludeFromJson
    private boolean restricted = false;

    @ExcludeFromJson
    private boolean official = true;

    /**
     * For creating empty elements.
     */
    public Element() {
        this.id = DEFAULT_NULL_ID;
        this.name = "";
        this.description = "";
        this.moduleName = "";
        this.language = "";
        this.randomDefinition = new RandomElementDefinition();
        this.restricted = false;
    }

    public Element(String id, String name, String description, String language, String moduleName) {
        this(id, name, description, language, new RandomElementDefinition(), moduleName);
    }

    public Element(String id, String name, String description, String language, RandomElementDefinition randomDefinition,
                   String moduleName) {
        this.id = id != null ? id.trim() : null;
        this.name = name != null ? name.trim() : null;
        this.description = description != null ? description.trim() : null;
        this.language = language;
        this.randomDefinition = randomDefinition;
        this.moduleName = moduleName;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getNameRepresentation() {
        return getName();
    }

    public String getId() {
        return id;
    }

    @Override
    public int compareTo(T element) {
        if (getName() == null) {
            if (element.getName() == null) {
                return 0;
            }
            return -1;
        }
        if (element.getName() == null) {
            return 1;
        }
        return getName().compareTo(element.getName());
    }

    @Override
    public String toString() {
        return getId();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        @SuppressWarnings("unchecked") final T other = (T) obj;
        if (id == null) {
            if (other.getId() != null) {
                return false;
            }
        } else if (!id.equals(other.getId())) {
            return false;
        }
        return true;
    }

    public RandomElementDefinition getRandomDefinition() {
        return randomDefinition;
    }

    public String getLanguage() {
        return language;
    }

    public String getModuleName() {
        return moduleName;
    }

    public static boolean isNull(Element<?> element) {
        if (element == null) {
            return true;
        }
        return Objects.equal(element.getId(), DEFAULT_NULL_ID);
    }

    public boolean isRestricted() {
        return restricted;
    }

    public void setRestricted(boolean restricted) {
        this.restricted = restricted;
    }

    public boolean isOfficial() {
        return official;
    }

    public void setOfficial(boolean official) {
        this.official = official;
    }
}
