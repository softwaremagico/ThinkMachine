package com.softwaremagico.tm.character.skills;

public class Skill implements Comparable<Skill> {

	private String name;

	private boolean fromGuild;

	public Skill(String name) {
		fromGuild = name.contains("*");
		this.name = name.replace("*", "").trim();
	}

	@Override
	public int compareTo(Skill skill) {
		return getName().compareTo(skill.getName());
	}

	public boolean isFromGuild() {
		return fromGuild;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return getName();
	}

}
