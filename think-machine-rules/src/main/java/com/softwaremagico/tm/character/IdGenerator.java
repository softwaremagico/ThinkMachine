package com.softwaremagico.tm.character;

import java.util.UUID;

public class IdGenerator {

	private IdGenerator() {
		// Private constructor to hide the implicit public one.
	}

	public static String createId() {
		final UUID uuid = java.util.UUID.randomUUID();
		return uuid.toString();
	}
}
