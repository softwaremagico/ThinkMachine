package com.softwaremagico.tm.character;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.factions.Faction;

public class Name extends Element<Name> {
	private final Gender gender;
	private final Faction faction;

	public Faction getFaction() {
		return faction;
	}

	public Name(String name) {
		this(name, null, null);
	}

	public Name(String name, Gender gender, Faction faction) {
		super(name.toLowerCase(), name);
		this.gender = gender;
		this.faction = faction;
	}

	public Gender getGender() {
		return gender;
	}

}
