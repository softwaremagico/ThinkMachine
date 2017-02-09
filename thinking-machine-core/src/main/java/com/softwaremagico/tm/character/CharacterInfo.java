package com.softwaremagico.tm.character;

import java.lang.reflect.Field;

import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;

public class CharacterInfo {
	private static ITranslator translator = LanguagePool.getTranslator("character_values.xml");

	private String name;

	private String player;

	private Gender gender;

	private Integer age;

	private String race;

	private String planet;

	private String alliance;

	private String rank;

	public String getTranslatedParameter(String fieldName) {
		for (Field field : this.getClass().getDeclaredFields()) {
			// field.setAccessible(true); //Make it public.
			if (field.getName().equals(fieldName)) {
				Object value;
				try {
					value = field.get(this);
					if (value != null) {
						return getTranslation(value);
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// Not valid field.
				}
			}
		}
		return null;
	}

	private String getTranslation(Object parameterValue) {
		String xmlTag = parameterValue.toString().substring(0, 1).toLowerCase() + parameterValue.toString().substring(1);
		String translatedText = translator.getTranslatedText(xmlTag);
		if (translatedText != null) {
			return translatedText;
		}
		return parameterValue.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		this.age = age;
	}

	public String getRace() {
		return race;
	}

	public void setRace(String race) {
		this.race = race;
	}

	public String getPlanet() {
		return planet;
	}

	public void setPlanet(String planet) {
		this.planet = planet;
	}

	public String getAlliance() {
		return alliance;
	}

	public void setAlliance(String alliance) {
		this.alliance = alliance;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

}
