package com.softwaremagico.tm.character.skills;

public class Skill implements Comparable<Skill> {

	private String name;

	public Skill(String name) {
		this.name = name;
	}

	public Skill() {
	}

	@Override
	public int compareTo(Skill skill) {
		return getName().compareTo(skill.getName());
	}

	@Override
	public String toString() {
		return getName();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
