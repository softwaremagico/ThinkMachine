package com.softwaremagico.tm.character.lifepaths;

import java.util.HashSet;
import java.util.Set;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.values.IValue;

public class LifePath extends Element<LifePath> {
	private final Set<IValue> bonus;

	public LifePath(String id, String name, String language, String moduleName) {
		super(id, name, language, moduleName);
		bonus = new HashSet<>();
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	public Set<IValue> getBonus() {
		return bonus;
	}
}
