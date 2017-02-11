package com.softwaremagico.tm.character.skills;

public class SelectedSkill extends Skill {
	private int value;

	public SelectedSkill(String name, int value) {
		super(name);
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
