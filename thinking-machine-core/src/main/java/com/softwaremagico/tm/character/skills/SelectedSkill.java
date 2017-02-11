package com.softwaremagico.tm.character.skills;

public class SelectedSkill extends Skill {
	private final int value;
	private final boolean special;

	public SelectedSkill(String name, int value, boolean special) {
		super(name);
		this.value = value;
		this.special = special;
	}

	public int getValue() {
		return value;
	}

	public boolean isSpecial() {
		return special;
	}
}
