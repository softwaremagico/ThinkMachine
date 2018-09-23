package com.softwaremagico.tm.character.equipment.weapons;

public enum WeaponType {

	THROWING,
	BOW,
	CROSSBOW,
	SLUG,
	MINE,
	MELEE,
	MELEE_ARTIFACT,
	LASER,
	PLASMA;

	public static WeaponType get(String typeName) {
		for (WeaponType type : WeaponType.values()) {
			if (type.name().equalsIgnoreCase(typeName)) {
				return type;
			}
		}
		return null;
	}
}
