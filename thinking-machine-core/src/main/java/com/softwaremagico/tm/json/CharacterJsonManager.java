package com.softwaremagico.tm.json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.softwaremagico.tm.character.CharacterPlayer;

public class CharacterJsonManager {

	public static String toJson(CharacterPlayer characterPlayer) {
		if (characterPlayer != null) {
			// final Gson gson = new
			// GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
			final Gson gson = new GsonBuilder().setPrettyPrinting().create();
			final String jsonText = gson.toJson(characterPlayer);
			return jsonText;
		}
		return null;
	}

	public static CharacterPlayer fromJson(String jsonText) {
		if (jsonText != null && jsonText.length() > 0) {
			final Gson gson = new GsonBuilder().create();
			CharacterPlayer characterPlayer = gson.fromJson(jsonText, CharacterPlayer.class);
			return characterPlayer;
		}
		return null;
	}

	public static CharacterPlayer fromFile(String path) throws IOException {
		String jsonText = new String(Files.readAllBytes(Paths.get(path)));
		return fromJson(jsonText);
	}
}
