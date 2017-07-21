package com.softwaremagico.tm;

import com.softwaremagico.tm.character.race.RaceFactory;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.character.skills.SkillsDefinitionsFactory;
import com.softwaremagico.tm.character.traits.BlessingFactory;
import com.softwaremagico.tm.language.LanguagePool;

public class CacheHandler {

	public static void clearCache() {
		LanguagePool.clearCache();
		AvailableSkillsFactory.getInstance().clearCache();
		SkillsDefinitionsFactory.getInstance().clearCache();
		BlessingFactory.getInstance().clearCache();
		RaceFactory.getInstance().clearCache();
	}
}
