package com.softwaremagico.tm.character;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.factions.Faction;

public class Surname extends Element<Surname> {
	private final Faction faction;

	public Faction getFaction() {
		return faction;
	}

	public Surname(String surname) {
		this(surname, null);
	}

	public Surname(String surname, Faction faction) {
		super(surname.toLowerCase(), surname);
		this.faction = faction;
	}

}
