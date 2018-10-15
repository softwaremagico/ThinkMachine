package com.softwaremagico.tm.txt;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.characteristics.CharacteristicType;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.language.Translator;
import com.softwaremagico.tm.log.MachineLog;

public class CharacterSheet {
	private final CharacterPlayer characterPlayer;
	private static ITranslator translator = LanguagePool.getTranslator("character_sheet.xml");

	public CharacterSheet(CharacterPlayer characterPlayer) {
		this.characterPlayer = characterPlayer;
		Translator.setLanguage(characterPlayer.getLanguage());
	}

	public CharacterPlayer getCharacterPlayer() {
		return characterPlayer;
	}

	public static ITranslator getTranslator() {
		return translator;
	}

	private void setCharacterInfoText(StringBuilder stringBuilder) throws InvalidXmlElementException {
		stringBuilder.append(getCharacterPlayer().getNameRepresentation());
		stringBuilder.append("\n");
		stringBuilder.append(getCharacterPlayer().getRace().getName());
		stringBuilder.append(" " + getCharacterPlayer().getInfo().getTranslatedParameter("gender"));
		stringBuilder.append(" (" + getCharacterPlayer().getInfo().getPlanet().getName() + ")");
		stringBuilder.append("\n");
		stringBuilder.append(getCharacterPlayer().getFaction());
		if (getCharacterPlayer().getRank() != null) {
			stringBuilder.append(" (" + getCharacterPlayer().getRank() + ")");
		}
		stringBuilder.append("\n");
	}

	private void setCharacteristicsText(StringBuilder stringBuilder) {
		for (CharacteristicType characteristicType : CharacteristicType.values()) {
			stringBuilder.append(getTranslator().getTranslatedText(characteristicType.getTranslationTag()) + ": ");
			String separator = "";
			for (Characteristic characteristic : getCharacterPlayer().getCharacteristics(characteristicType)) {
				stringBuilder.append(separator);
				stringBuilder.append(getTranslator().getTranslatedText(characteristic.getId()));
				separator = ", ";
			}
			stringBuilder.append(".");
		}
		stringBuilder.append("\n");
	}

	private void representSkill(StringBuilder stringBuilder, AvailableSkill skill) {
		stringBuilder.append(skill.getName() + " (");
		stringBuilder.append(characterPlayer.getSkillTotalRanks(skill));
		stringBuilder.append(characterPlayer.isSkillSpecial(skill) ? "*" : "");
		stringBuilder.append(characterPlayer.hasSkillTemporalModificator(skill) && characterPlayer.getSkillTotalRanks(skill) > 0 ? "!" : "");
		stringBuilder.append(skill.getName() + ")");
	}

	private void setSkillsText(StringBuilder stringBuilder) throws InvalidXmlElementException {
		stringBuilder.append(getTranslator().getTranslatedText(getTranslator().getTranslatedText("naturalSkills")) + ": ");
		for (AvailableSkill skill : characterPlayer.getNaturalSkills()) {
			if (characterPlayer.getSkillTotalRanks(skill) > 0) {
				representSkill(stringBuilder, skill);
			}
		}
		stringBuilder.append("\n");
		stringBuilder.append(getTranslator().getTranslatedText(getTranslator().getTranslatedText("learnedSkills")) + ": ");
		for (AvailableSkill skill : characterPlayer.getLearnedSkills()) {
			if (characterPlayer.getSkillTotalRanks(skill) > 0) {
				representSkill(stringBuilder, skill);
			}
		}
		stringBuilder.append("\n");
	}

	private void setVitalityRepresentation(StringBuilder stringBuilder) throws InvalidXmlElementException {
		stringBuilder.append(getTranslator().getTranslatedText("vitality"));
		stringBuilder.append("-10/-8/-6/-4/-2");
		for (int i = 0; i < getCharacterPlayer().getVitalityValue() - 5; i++) {
			stringBuilder.append("/O");
		}
		stringBuilder.append("\n");
	}

	private void setWyrdRepresentation(StringBuilder stringBuilder) throws InvalidXmlElementException {
		stringBuilder.append(getTranslator().getTranslatedText("wyrd"));
		for (int i = 0; i < getCharacterPlayer().getWyrdValue(); i++) {
			if (i > 0) {
				stringBuilder.append("/");
			}
			stringBuilder.append("O");
		}
		stringBuilder.append("\n");
	}

	public String createContent() {
		StringBuilder stringBuilder = new StringBuilder();
		try {
			setCharacterInfoText(stringBuilder);
			stringBuilder.append("\n");
			setCharacteristicsText(stringBuilder);
			stringBuilder.append("\n");
			setSkillsText(stringBuilder);
			stringBuilder.append("\n");
			setVitalityRepresentation(stringBuilder);
			stringBuilder.append("\n");
			setWyrdRepresentation(stringBuilder);
			stringBuilder.append("\n");
		} catch (InvalidXmlElementException e) {
			MachineLog.errorMessage(this.getClass().getName(), e);
		}
		return stringBuilder.toString();
	}

	@Override
	public String toString() {
		return createContent();
	}
}
