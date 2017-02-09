package com.softwaremagico.tm.character;

public enum Gender {

	MALE,

	FEMALE;

	@Override
	public String toString() {
		return name().toLowerCase();
	}
}
