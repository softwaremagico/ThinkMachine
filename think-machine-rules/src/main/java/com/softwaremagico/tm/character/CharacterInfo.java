package com.softwaremagico.tm.character;

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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.softwaremagico.tm.character.planets.Planet;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;

public class CharacterInfo {
    private static final String TRANSLATOR_FILE = "character_values.xml";

    private List<Name> names;

    private Surname surname;

    private String player;

    private Gender gender;

    private Integer age;

    private Planet planet;

    private String birthdate;

    private String hair;

    private String eyes;

    private String complexion;

    private String height;

    private String weight;

    private String characterDescription = "";

    private String backgroundDecription = "";

    public String getTranslatedParameter(String fieldName, String moduleName) {
        for (final Field field : this.getClass().getDeclaredFields()) {
            // field.setAccessible(true); //Make it public.
            if (field.getName().equals(fieldName)) {
                final Object value;
                try {
                    value = field.get(this);
                    if (value != null) {
                        return getTranslation(value, moduleName);
                    }
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    // Not valid field.
                }
            }
        }
        return null;
    }

    private String getTranslation(Object parameterValue, String moduleName) {
        try {
            final String xmlTag = parameterValue.toString().substring(0, 1).toLowerCase() + parameterValue.toString().substring(1);
            final String translatedText = getTranslator(moduleName).getTranslatedText(xmlTag);
            if (translatedText != null) {
                return translatedText;
            }
        } catch (Exception e) {

        }
        return parameterValue.toString();
    }

    public List<Name> getNames() {
        return names;
    }

    public void setNames(List<Name> names) {
        this.names = names;
    }

    public void setNames(String names) {
        if (names == null) {
            return;
        }
        this.names = new ArrayList<>();
        final String[] namesSeparated = names.split("(?=\\p{Lu})");
        for (final String name : namesSeparated) {
            addName(new Name(name, null, null, gender, null));
        }
    }

    public void addName(Name name) {
        if (names == null) {
            names = new ArrayList<>();
        }
        names.add(name);
        names = names.stream().filter(n -> !n.getName().equals("")).collect(Collectors.toList());
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        if (age == null) {
            this.age = null;
        } else if (age > 99) {
            this.age = 99;
        } else if (age < 5) {
            this.age = 5;
        } else {
            this.age = age;
        }
    }

    public Planet getPlanet() {
        return planet;
    }

    public void setPlanet(Planet planet) {
        this.planet = planet;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getHair() {
        return hair;
    }

    public void setHair(String hair) {
        this.hair = hair;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getEyes() {
        return eyes;
    }

    public void setEyes(String eyes) {
        this.eyes = eyes;
    }

    public String getComplexion() {
        return complexion;
    }

    public void setComplexion(String complexion) {
        this.complexion = complexion;
    }

    public String getNameRepresentation() {
        final StringBuilder stringBuilder = new StringBuilder("");
        if (getNames() != null && !getNames().isEmpty()) {
            for (final Name name : getNames()) {
                stringBuilder.append(name.getName());
                stringBuilder.append(" ");
            }
        }
        return stringBuilder.toString().trim();
    }

    public Surname getSurname() {
        return surname;
    }

    public void setSurname(Surname surname) {
        this.surname = surname;
    }

    public void setSurname(String surname) {
        if (surname == null || surname.isEmpty()) {
            this.surname = null;
        } else {
            this.surname = new Surname(surname, null, null, null);
        }
    }

    private ITranslator getTranslator(String moduleName) {
        return LanguagePool.getTranslator(getTranslatorFile(), moduleName);
    }

    private String getTranslatorFile() {
        return TRANSLATOR_FILE;
    }

    public String getCharacterDescription() {
        return characterDescription;
    }

    public void setCharacterDescription(String characterDescription) {
        this.characterDescription = characterDescription;
    }

    public String getBackgroundDecription() {
        return backgroundDecription;
    }

    public void setBackgroundDecription(String backgroundDecription) {
        this.backgroundDecription = backgroundDecription;
    }

}
