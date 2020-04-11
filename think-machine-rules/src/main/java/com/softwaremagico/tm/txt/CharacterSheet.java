package com.softwaremagico.tm.txt;

/*-
 * #%L
 * Think Machine (Core)
 * %%
 * Copyright (C) 2017 - 2018 Softwaremagico
 * %%
 * This software is designed by Jorge Hortelano Otero. Jorge Hortelano Otero
 * <softwaremagico@gmail.com> Valencia (Spain).
 *  
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *  
 * You should have received a copy of the GNU General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.ThreatLevel;
import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.benefices.AvailableBeneficeFactory;
import com.softwaremagico.tm.character.benefices.BeneficeClassification;
import com.softwaremagico.tm.character.benefices.BeneficeGroup;
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.characteristics.CharacteristicType;
import com.softwaremagico.tm.character.combat.CombatAction;
import com.softwaremagico.tm.character.combat.CombatStyle;
import com.softwaremagico.tm.character.combat.CombatStyleFactory;
import com.softwaremagico.tm.character.cybernetics.ICyberneticDevice;
import com.softwaremagico.tm.character.equipment.DamageType;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.occultism.OccultismPath;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.occultism.OccultismPower;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.SkillDefinition;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.language.Translator;
import com.softwaremagico.tm.log.MachineLog;

public class CharacterSheet {
	private static final String TRANSLATOR_FILE = "character_sheet.xml";
	private static final String ELEMENT_SEPARATOR = ", ";
	// private static final String THREAT_LEVEL_SYMBOL = "\u2620";

	private final CharacterPlayer characterPlayer;

	public CharacterSheet(CharacterPlayer characterPlayer) {
		this.characterPlayer = characterPlayer;
		Translator.setLanguage(characterPlayer.getLanguage());
	}

	private CharacterPlayer getCharacterPlayer() {
		return characterPlayer;
	}

	private static ITranslator getTranslator() {
		return LanguagePool.getTranslator(TRANSLATOR_FILE, (String) null);

	}

	private void setCharacterInfoText(StringBuilder stringBuilder) throws InvalidXmlElementException {
		stringBuilder.append(getCharacterPlayer().getCompleteNameRepresentation());
		// stringBuilder.append(" (" +
		// ThreatLevel.getThreatLevel(getCharacterPlayer()) +
		// THREAT_LEVEL_SYMBOL + ")");
		stringBuilder.append(" (" + getTranslator().getTranslatedText("threatLevel") + ": " + ThreatLevel.getThreatLevel(getCharacterPlayer()) + ")");
		stringBuilder.append("\n");
		if (getCharacterPlayer().getRace() != null) {
			stringBuilder.append(getCharacterPlayer().getRace().getName());
		}
		if (getCharacterPlayer().getInfo().getGender() != null) {
			stringBuilder.append(" " + getCharacterPlayer().getInfo().getTranslatedParameter("gender", getCharacterPlayer().getModuleName()));
		}
		if (getCharacterPlayer().getInfo().getAge() != null) {
			stringBuilder.append(" " + getCharacterPlayer().getInfo().getAge() + " " + getTranslator().getTranslatedText("years").toLowerCase());
		}
		if (getCharacterPlayer().getInfo().getPlanet() != null) {
			stringBuilder.append(" (" + getCharacterPlayer().getInfo().getPlanet().getName() + ")");
		}
		stringBuilder.append("\n");
		if (getCharacterPlayer().getFaction() != null) {
			stringBuilder.append(getCharacterPlayer().getFaction().getName());
		}
		if (getCharacterPlayer().getRank() != null) {
			stringBuilder.append(" (" + getCharacterPlayer().getRank() + ")");
		}
		stringBuilder.append("\n");
	}

	private void setCharacteristicsText(StringBuilder stringBuilder) {
		for (final CharacteristicType characteristicType : CharacteristicType.values()) {
			stringBuilder.append(getTranslator().getTranslatedText(characteristicType.getTranslationTag()) + ": ");
			String separator = "";
			if (getCharacterPlayer().getCharacteristics(characteristicType) == null) {
				stringBuilder.append("\n");
				continue;
			}
			final List<Characteristic> characteristics = new ArrayList<>(getCharacterPlayer().getCharacteristics(characteristicType));
			Collections.sort(characteristics);
			for (final Characteristic characteristic : characteristics) {
				stringBuilder.append(separator);
				stringBuilder.append(getTranslator().getTranslatedText(characteristic.getId()));
				stringBuilder.append(" ");
				stringBuilder.append(getCharacterPlayer().getValue(characteristic.getCharacteristicName()));
				separator = ELEMENT_SEPARATOR;
			}
			stringBuilder.append(".\n");
		}
	}

	private void representSkill(StringBuilder stringBuilder, AvailableSkill skill) {
		if (skill.getId().equalsIgnoreCase(SkillDefinition.PLANETARY_LORE_ID) && characterPlayer.getInfo().getPlanet() != null) {
			stringBuilder.append(AvailableSkill.getCompleteName(skill.getName(), characterPlayer.getInfo().getPlanet().getName()) + " (");
		} else if (skill.getId().equalsIgnoreCase(SkillDefinition.FACTORION_LORE_ID) && characterPlayer.getFaction() != null) {
			stringBuilder.append(AvailableSkill.getCompleteName(skill.getName(), characterPlayer.getFaction().getName()) + " (");
		} else {
			stringBuilder.append(AvailableSkill.getCompleteName(skill.getName(), skill.getSpecialization()) + " (");
		}
		stringBuilder.append(characterPlayer.getSkillTotalRanks(skill));
		stringBuilder.append(characterPlayer.isSkillSpecial(skill) ? "*" : "");
		stringBuilder.append(characterPlayer.hasSkillTemporalModificator(skill) && characterPlayer.getSkillTotalRanks(skill) > 0 ? "!" : "");
		stringBuilder.append(")");
	}

	private void setSkillsText(StringBuilder stringBuilder) throws InvalidXmlElementException {
		stringBuilder.append(getTranslator().getTranslatedText("naturalSkills") + ": ");
		String separator = "";
		for (final AvailableSkill skill : characterPlayer.getNaturalSkills()) {
			if (characterPlayer.getSkillTotalRanks(skill) > 0) {
				stringBuilder.append(separator);
				representSkill(stringBuilder, skill);
				separator = ELEMENT_SEPARATOR;
			}
		}
		stringBuilder.append(".\n");
		stringBuilder.append(getTranslator().getTranslatedText("learnedSkills") + ": ");
		separator = "";
		if (characterPlayer.getLearnedSkills().size() > 0) {
			for (final AvailableSkill skill : characterPlayer.getLearnedSkills()) {
				if (characterPlayer.getSkillTotalRanks(skill) > 0) {
					stringBuilder.append(separator);
					representSkill(stringBuilder, skill);
					separator = ELEMENT_SEPARATOR;
				}
			}
			stringBuilder.append(".\n");
		}
	}

	private void setVitalityRepresentation(StringBuilder stringBuilder) throws InvalidXmlElementException {
		stringBuilder.append(getTranslator().getTranslatedText("vitality"));
		stringBuilder.append(": -10/-8/-6/-4/-2");
		for (int i = 0; i < getCharacterPlayer().getVitalityValue() - 5; i++) {
			stringBuilder.append("/O");
		}
		stringBuilder.append("\n");
	}

	private void setWyrdRepresentation(StringBuilder stringBuilder) throws InvalidXmlElementException {
		if (getCharacterPlayer().getWyrdValue() > 0) {
			stringBuilder.append(getTranslator().getTranslatedText("wyrd"));
			stringBuilder.append(": ");
			stringBuilder.append(getCharacterPlayer().getWyrdValue());
			stringBuilder.append("\n");
		}
	}

	private void setBlessings(StringBuilder stringBuilder) throws InvalidXmlElementException {
		if (!getCharacterPlayer().getBlessings().isEmpty()) {
			stringBuilder.append(getTranslator().getTranslatedText("blessingTable"));
			stringBuilder.append(": ");
			String separator = "";
			for (final Blessing blessing : getCharacterPlayer().getBlessings()) {
				stringBuilder.append(separator);
				stringBuilder.append(blessing.getName());
				separator = ELEMENT_SEPARATOR;
			}
			stringBuilder.append(".\n");
		}
		if (!getCharacterPlayer().getCurses().isEmpty()) {
			stringBuilder.append(getTranslator().getTranslatedText("cursesTable"));
			stringBuilder.append(": ");
			String separator = "";
			for (final Blessing curse : getCharacterPlayer().getCurses()) {
				stringBuilder.append(separator);
				stringBuilder.append(curse.getName());
				separator = ELEMENT_SEPARATOR;
			}
			stringBuilder.append(".\n");
		}
	}

	private void representBenefice(StringBuilder stringBuilder, AvailableBenefice benefice) throws InvalidXmlElementException {
		if (benefice.getSpecialization() != null) {
			stringBuilder.append(benefice.getSpecialization().getName());
		} else {
			stringBuilder.append(benefice.getName());
		}
		if (benefice.getBeneficeDefinition().getSpecializations().size() > 1) {
			stringBuilder
					.append(" (" + (benefice.getBeneficeClassification() == BeneficeClassification.AFFLICTION ? "+" : "") + Math.abs(benefice.getCost()) + ")");
		} else if (AvailableBeneficeFactory.getInstance()
				.getAvailableBeneficesByDefinition(getCharacterPlayer().getLanguage(), getCharacterPlayer().getModuleName(), benefice.getBeneficeDefinition())
				.size() > 1) {
			stringBuilder
					.append(" (" + (benefice.getBeneficeClassification() == BeneficeClassification.AFFLICTION ? "+" : "") + Math.abs(benefice.getCost()) + ")");
		}
		if (benefice.getBeneficeDefinition().getGroup() == BeneficeGroup.FIGHTING) {
			final CombatStyle combatStyle = CombatStyleFactory.getInstance().getCombatStyle(benefice, getCharacterPlayer().getLanguage(),
					getCharacterPlayer().getModuleName());
			for (final CombatAction action : combatStyle.getCombatActions()) {
				if (action.isAvailable(getCharacterPlayer())) {
					stringBuilder.append(" (");
					if (action.getGoal() != null && action.getGoal().length() > 0 && !action.getGoal().equals("0")) {
						stringBuilder.append(action.getGoal());
						stringBuilder.append(getTranslator().getTranslatedText("weaponGoal"));
						stringBuilder.append(ELEMENT_SEPARATOR);
					}
					if (action.getDamage() != null && action.getDamage().length() > 0 && !action.getDamage().equals("0")) {
						stringBuilder.append(action.getDamage());
						if (!action.getDamage().endsWith("d")) {
							stringBuilder.append("d");
						}
						stringBuilder.append(ELEMENT_SEPARATOR);
					}
					if (action.getOthers() != null && action.getOthers().length() > 0) {
						stringBuilder.append(action.getOthers());
						stringBuilder.append(ELEMENT_SEPARATOR);
					}
					// Remove last separator
					stringBuilder.setLength(stringBuilder.length() - ELEMENT_SEPARATOR.length());
					stringBuilder.append(")");
				}
			}
		}
	}

	private void setBenefices(StringBuilder stringBuilder) throws InvalidXmlElementException {
		if (!getCharacterPlayer().getAllBenefices().isEmpty()) {
			stringBuilder.append(getTranslator().getTranslatedText("beneficesTable"));
			stringBuilder.append(": ");
			String separator = "";
			for (final AvailableBenefice benefice : getCharacterPlayer().getAllBenefices()) {
				stringBuilder.append(separator);
				representBenefice(stringBuilder, benefice);
				separator = ELEMENT_SEPARATOR;
			}
			stringBuilder.append(".\n");
		}
		if (!getCharacterPlayer().getAfflictions().isEmpty()) {
			stringBuilder.append(getTranslator().getTranslatedText("afflictionsTable"));
			stringBuilder.append(": ");
			String separator = "";
			for (final AvailableBenefice affliction : getCharacterPlayer().getAfflictions()) {
				stringBuilder.append(separator);
				representBenefice(stringBuilder, affliction);
				separator = ELEMENT_SEPARATOR;
			}
			stringBuilder.append(".\n");
		}
	}

	private void setOccultism(StringBuilder stringBuilder) {
		if (getCharacterPlayer().getPsiqueLevel(OccultismTypeFactory.getTheurgy(getCharacterPlayer().getLanguage(), getCharacterPlayer().getModuleName())) > 0
				|| getCharacterPlayer()
						.getPsiqueLevel(OccultismTypeFactory.getPsi(getCharacterPlayer().getLanguage(), getCharacterPlayer().getModuleName())) > 0) {
			stringBuilder.append(getTranslator().getTranslatedText("occultism") + ": ");
			String separator = "";
			OccultismTypeFactory.getInstance();
			if (getCharacterPlayer()
					.getPsiqueLevel(OccultismTypeFactory.getPsi(getCharacterPlayer().getLanguage(), getCharacterPlayer().getModuleName())) > 0) {
				stringBuilder.append(getTranslator().getTranslatedText("psi") + " ");
				stringBuilder.append(getCharacterPlayer()
						.getPsiqueLevel(OccultismTypeFactory.getPsi(getCharacterPlayer().getLanguage(), getCharacterPlayer().getModuleName())));
				stringBuilder.append(ELEMENT_SEPARATOR);
				stringBuilder.append(getTranslator().getTranslatedText("urge") + " ");
				stringBuilder.append(getCharacterPlayer()
						.getDarkSideLevel(OccultismTypeFactory.getPsi(getCharacterPlayer().getLanguage(), getCharacterPlayer().getModuleName())));
				separator = ELEMENT_SEPARATOR;
			}
			OccultismTypeFactory.getInstance();
			if (getCharacterPlayer()
					.getPsiqueLevel(OccultismTypeFactory.getTheurgy(getCharacterPlayer().getLanguage(), getCharacterPlayer().getModuleName())) > 0) {
				stringBuilder.append(separator);
				stringBuilder.append(getTranslator().getTranslatedText("theurgy") + " ");
				stringBuilder.append(getCharacterPlayer()
						.getPsiqueLevel(OccultismTypeFactory.getTheurgy(getCharacterPlayer().getLanguage(), getCharacterPlayer().getModuleName())));
				stringBuilder.append(ELEMENT_SEPARATOR);
				stringBuilder.append(getTranslator().getTranslatedText("hubris") + " ");
				stringBuilder.append(getCharacterPlayer()
						.getDarkSideLevel(OccultismTypeFactory.getTheurgy(getCharacterPlayer().getLanguage(), getCharacterPlayer().getModuleName())));
			}
			stringBuilder.append(".\n");
		}
	}

	private void setOccultismPowers(StringBuilder stringBuilder) throws InvalidXmlElementException {
		String separator = "";
		if (!getCharacterPlayer().getSelectedPowers().isEmpty()) {
			stringBuilder.append(getTranslator().getTranslatedText("occultismPowers") + ": ");
			final List<String> paths = new ArrayList<>(getCharacterPlayer().getSelectedPowers().keySet());
			Collections.sort(paths);
			for (final String powersPath : paths) {
				stringBuilder.append(separator);
				final OccultismPath occultismPath = OccultismPathFactory.getInstance().getElement(powersPath, getCharacterPlayer().getLanguage(),
						getCharacterPlayer().getModuleName());
				stringBuilder.append(occultismPath.getName());
				stringBuilder.append(" (");
				String powerSeparator = "";
				final List<OccultismPower> powers = new ArrayList<>(getCharacterPlayer().getSelectedPowers().get(powersPath));
				Collections.sort(powers);
				for (final OccultismPower occultismPower : powers) {
					stringBuilder.append(powerSeparator);
					stringBuilder.append(occultismPower.getName());
					powerSeparator = ELEMENT_SEPARATOR;
				}
				stringBuilder.append(")");
				separator = ELEMENT_SEPARATOR;
			}
			stringBuilder.append(".\n");
		}
	}

	private void setWeapons(StringBuilder stringBuilder) {
		for (final Weapon weapon : getCharacterPlayer().getAllWeapons()) {
			stringBuilder.append(weapon.getName());
			stringBuilder.append(" (");
			if (weapon.getGoal() != null && weapon.getGoal().length() > 0 && !weapon.getGoal().equals("0")) {
				stringBuilder.append(weapon.getGoal());
				stringBuilder.append(getTranslator().getTranslatedText("weaponGoal"));
				stringBuilder.append(ELEMENT_SEPARATOR);
			}
			stringBuilder.append(weapon.getDamageWithoutArea());
			if (!weapon.getDamageWithoutArea().endsWith("d")) {
				stringBuilder.append("d");
			}
			if (weapon.getAreaMeters() > 0) {
				stringBuilder.append(" ");
				stringBuilder.append(weapon.getAreaMeters());
			}
			stringBuilder.append(ELEMENT_SEPARATOR);
			if (weapon.getRange() != null && weapon.getRange().length() > 0) {
				stringBuilder.append(weapon.getRange());
				stringBuilder.append(ELEMENT_SEPARATOR);
			}
			if (weapon.getRate() != null && weapon.getRate().length() > 0) {
				stringBuilder.append(getTranslator().getTranslatedText("weaponRate"));
				stringBuilder.append(" ");
				stringBuilder.append(weapon.getRate());
				stringBuilder.append(ELEMENT_SEPARATOR);
			}
			for (final DamageType damageType : weapon.getDamageTypes()) {
				stringBuilder.append(damageType.getName());
				stringBuilder.append(ELEMENT_SEPARATOR);
			}
			// Remove last separator
			stringBuilder.setLength(stringBuilder.length() - ELEMENT_SEPARATOR.length());
			stringBuilder.append(")" + ELEMENT_SEPARATOR);
		}
	}

	private void setArmours(StringBuilder stringBuilder) {
		if (getCharacterPlayer().getArmour() != null) {
			stringBuilder.append(getCharacterPlayer().getArmour().getName());
			stringBuilder.append(" (");
			stringBuilder.append(getCharacterPlayer().getArmour().getProtection() + "d");
			stringBuilder.append(ELEMENT_SEPARATOR);
			if (getCharacterPlayer().getArmour().getStandardPenalizations().getDexterityModification() != 0) {
				stringBuilder.append(getTranslator().getTranslatedText(getCharacterPlayer().getCharacteristic(CharacteristicName.DEXTERITY).getId()) + " ");
				stringBuilder.append(getCharacterPlayer().getArmour().getStandardPenalizations().getDexterityModification());
				stringBuilder.append(ELEMENT_SEPARATOR);
			}
			if (getCharacterPlayer().getArmour().getStandardPenalizations().getStrengthModification() != 0) {
				stringBuilder.append(getTranslator().getTranslatedText(getCharacterPlayer().getCharacteristic(CharacteristicName.STRENGTH).getId()) + " ");
				stringBuilder.append(getCharacterPlayer().getArmour().getStandardPenalizations().getStrengthModification());
				stringBuilder.append(ELEMENT_SEPARATOR);
			}
			if (getCharacterPlayer().getArmour().getStandardPenalizations().getEnduranceModification() != 0) {
				stringBuilder.append(getTranslator().getTranslatedText(getCharacterPlayer().getCharacteristic(CharacteristicName.ENDURANCE).getId()) + " ");
				stringBuilder.append(getCharacterPlayer().getArmour().getStandardPenalizations().getEnduranceModification());
				stringBuilder.append(ELEMENT_SEPARATOR);
			}
			if (getCharacterPlayer().getArmour().getStandardPenalizations().getInitiativeModification() != 0) {
				stringBuilder.append(getTranslator().getTranslatedText(getCharacterPlayer().getCharacteristic(CharacteristicName.INITIATIVE).getId()) + " ");
				stringBuilder.append(getCharacterPlayer().getArmour().getStandardPenalizations().getInitiativeModification());
				stringBuilder.append(ELEMENT_SEPARATOR);
			}
			final List<DamageType> damages = new ArrayList<>(getCharacterPlayer().getArmour().getDamageTypes());
			Collections.sort(damages);
			for (final DamageType damageType : damages) {
				stringBuilder.append(damageType.getName());
				stringBuilder.append(ELEMENT_SEPARATOR);
			}
			stringBuilder.setLength(stringBuilder.length() - ELEMENT_SEPARATOR.length());
			stringBuilder.append(")" + ELEMENT_SEPARATOR);
		}
	}

	private void setShields(StringBuilder stringBuilder) {
		if (getCharacterPlayer().getShield() != null) {
			stringBuilder.append(getCharacterPlayer().getShield().getName());
			stringBuilder.append(" (");
			stringBuilder.append(getCharacterPlayer().getShield().getImpact());
			stringBuilder.append("/");
			stringBuilder.append(getCharacterPlayer().getShield().getForce());
			stringBuilder.append(" ");
			stringBuilder.append(getCharacterPlayer().getShield().getHits());
			stringBuilder.append(" ");
			stringBuilder.append(getTranslator().getTranslatedText("shieldHits"));
			stringBuilder.append(")" + ELEMENT_SEPARATOR);
		}
	}

	private void setEquipment(StringBuilder stringBuilder) {
		if (!getCharacterPlayer().getAllWeapons().isEmpty() || getCharacterPlayer().getShield() != null) {
			stringBuilder.append(getTranslator().getTranslatedText("equipment") + ": ");
			setWeapons(stringBuilder);
			setArmours(stringBuilder);
			setShields(stringBuilder);

			// Remove last separator
			stringBuilder.setLength(stringBuilder.length() - ELEMENT_SEPARATOR.length());
			stringBuilder.append(".\n");
		}
	}

	private void setCybernetics(StringBuilder stringBuilder) {
		if (!getCharacterPlayer().getCybernetics().isEmpty()) {
			stringBuilder.append(getTranslator().getTranslatedText("cybernetics") + ": ");

			for (final ICyberneticDevice device : getCharacterPlayer().getCybernetics()) {
				stringBuilder.append(device.getName());
				stringBuilder.append(ELEMENT_SEPARATOR);
			}
			// Remove last separator
			stringBuilder.setLength(stringBuilder.length() - ELEMENT_SEPARATOR.length());
			stringBuilder.append(".\n");
		}
	}

	private String createContent() {
		final StringBuilder stringBuilder = new StringBuilder();
		try {
			setCharacterInfoText(stringBuilder);
			stringBuilder.append("\n");
			setCharacteristicsText(stringBuilder);
			stringBuilder.append("\n");
			setSkillsText(stringBuilder);
			stringBuilder.append("\n");
			setBlessings(stringBuilder);
			stringBuilder.append("\n");
			setBenefices(stringBuilder);
			stringBuilder.append("\n");
			setOccultism(stringBuilder);
			setOccultismPowers(stringBuilder);
			stringBuilder.append("\n");
			setWyrdRepresentation(stringBuilder);
			setVitalityRepresentation(stringBuilder);
			stringBuilder.append("\n");
			setEquipment(stringBuilder);
			setCybernetics(stringBuilder);
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
