package com.softwaremagico.tm.pdf.complete.fighting;

import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.LateralHeaderPdfPTable;
import com.softwaremagico.tm.rules.InvalidXmlElementException;
import com.softwaremagico.tm.rules.character.CharacterPlayer;
import com.softwaremagico.tm.rules.character.combat.CombatAction;
import com.softwaremagico.tm.rules.character.combat.CombatStyle;

public class MeleeManeuversTable extends LateralHeaderPdfPTable {
	private static final float[] WIDTHS = { 1.2f, 5f, 1.5f, 3f, 6f };
	private static final int ROWS = 12;
	private static final String GAP = "____________________";
	private static final int NAME_COLUMN_WIDHT = 65;
	private static final int GOAL_COLUMN_WIDHT = 20;
	private static final int DAMAGE_COLUMN_WIDHT = 40;
	private static final int OTHERS_COLUMN_WIDHT = 70;

	public MeleeManeuversTable(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
		super(WIDTHS);
		addCell(createLateralVerticalTitle(getTranslator().getTranslatedText("meleeWeapons"), ROWS + 1));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponsAction")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponGoal")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponDamage")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponsOthers")));

		addCell(createFirstEmptyElementLine(getTranslator().getTranslatedText("actionStrike")));
		addCell(createEmptyElementLine(""));
		if (characterPlayer != null) {
			addCell(createEmptyElementLine((2 + characterPlayer.getStrengthDamangeModification())
					+ getTranslator().getTranslatedText("diceAbbreviature") + "/"
					+ getTranslator().getTranslatedText("weaponAbbreviature")));
		} else {
			addCell(createEmptyElementLine(2 + getTranslator().getTranslatedText("diceAbbreviature") + "/"
					+ getTranslator().getTranslatedText("weaponAbbreviature")));
		}
		addCell(createEmptyElementLine(""));

		addCell(createFirstEmptyElementLine(getTranslator().getTranslatedText("actionGrapple")));
		addCell(createEmptyElementLine(""));
		if (characterPlayer != null) {
			addCell(createEmptyElementLine((2 + characterPlayer.getStrengthDamangeModification())
					+ getTranslator().getTranslatedText("diceAbbreviature")));
		} else {
			addCell(createEmptyElementLine(2 + getTranslator().getTranslatedText("diceAbbreviature")));
		}
		addCell(createEmptyElementLine(getTranslator().getTranslatedText("strengthAbbreviature") + "+"
				+ getTranslator().getTranslatedText("vigorAbbreviature") + "/"
				+ getTranslator().getTranslatedText("strengthAbbreviature") + "+"
				+ getTranslator().getTranslatedText("vigorAbbreviature")));

		addCell(createFirstEmptyElementLine(getTranslator().getTranslatedText("actionKnockdown")));
		addCell(createEmptyElementLine(""));
		if (characterPlayer != null) {
			addCell(createEmptyElementLine((3 + characterPlayer.getStrengthDamangeModification())
					+ getTranslator().getTranslatedText("diceAbbreviature")));
		} else {
			addCell(createEmptyElementLine(3 + getTranslator().getTranslatedText("diceAbbreviature")));
		}
		addCell(createEmptyElementLine(getTranslator().getTranslatedText("strengthAbbreviature") + "+"
				+ getTranslator().getTranslatedText("meleeAbbreviature") + "/"
				+ getTranslator().getTranslatedText("dexterityAbbreviature") + "+"
				+ getTranslator().getTranslatedText("vigorAbbreviature")));

		addCell(createFirstEmptyElementLine(getTranslator().getTranslatedText("actionDisarm")));
		addCell(createEmptyElementLine("-4"));
		if (characterPlayer != null) {
			addCell(createEmptyElementLine((2 + characterPlayer.getStrengthDamangeModification())
					+ getTranslator().getTranslatedText("diceAbbreviature") + "/"
					+ getTranslator().getTranslatedText("weaponAbbreviature")));
		} else {
			addCell(createEmptyElementLine(3 + getTranslator().getTranslatedText("diceAbbreviature")));
		}
		addCell(createEmptyElementLine(getTranslator().getTranslatedText("weaponDamage") + "/"
				+ getTranslator().getTranslatedText("strengthAbbreviature") + "+"
				+ getTranslator().getTranslatedText("vigorAbbreviature")));

		addCell(createFirstEmptyElementLine(getTranslator().getTranslatedText("actionKnockout")));
		addCell(createEmptyElementLine("-4"));
		if (characterPlayer != null) {
			addCell(createEmptyElementLine((2 + characterPlayer.getStrengthDamangeModification())
					+ getTranslator().getTranslatedText("diceAbbreviature") + "/"
					+ getTranslator().getTranslatedText("weaponAbbreviature")));
		} else {
			addCell(createEmptyElementLine(3 + getTranslator().getTranslatedText("diceAbbreviature")));
		}
		addCell(createEmptyElementLine(getTranslator().getTranslatedText("weaponSpecial")));

		addCell(createFirstEmptyElementLine(getTranslator().getTranslatedText("actionCharge")));
		addCell(createEmptyElementLine(""));
		if (characterPlayer != null) {
			addCell(createEmptyElementLine((1 + characterPlayer.getStrengthDamangeModification())
					+ getTranslator().getTranslatedText("diceAbbreviature") + "/"
					+ getTranslator().getTranslatedText("meterAbbreviature")));
		} else {
			addCell(createEmptyElementLine(1 + getTranslator().getTranslatedText("diceAbbreviature") + "/"
					+ getTranslator().getTranslatedText("meterAbbreviature")));
		}
		addCell(createEmptyElementLine(getTranslator().getTranslatedText("maximumAbbreviature") + " 4"
				+ getTranslator().getTranslatedText("diceAbbreviature")));

		int addedActions = 0;
		if (characterPlayer != null) {
			for (final CombatStyle style : characterPlayer.getMeleeCombatStyles()) {
				for (final CombatAction action : style.getCombatActions()) {
					if (action.isAvailable(characterPlayer)) {
						addCell(createFirstElementLine(action.getName(), NAME_COLUMN_WIDHT,
								FadingSunsTheme.COMBAT_ACTIONS_CONTENT_FONT_SIZE));
						addCell(createElementLine(action.getGoal(), GOAL_COLUMN_WIDHT,
								FadingSunsTheme.COMBAT_ACTIONS_CONTENT_FONT_SIZE));
						addCell(createElementLine(action.getDamage(), DAMAGE_COLUMN_WIDHT,
								FadingSunsTheme.COMBAT_ACTIONS_CONTENT_FONT_SIZE));
						addCell(createElementLine(action.getOthers(), OTHERS_COLUMN_WIDHT,
								FadingSunsTheme.COMBAT_ACTIONS_CONTENT_FONT_SIZE));
						addedActions++;
					}
				}
			}
		}

		for (int i = 0; i < ROWS - 5 - addedActions; i++) {
			addCell(createEmptyElementLine(GAP, NAME_COLUMN_WIDHT));
			addCell(createEmptyElementLine(GAP, GOAL_COLUMN_WIDHT));
			addCell(createEmptyElementLine(GAP, DAMAGE_COLUMN_WIDHT));
			addCell(createEmptyElementLine(GAP, OTHERS_COLUMN_WIDHT));
		}

	}

	@Override
	protected int getTitleFontSize() {
		return FadingSunsTheme.FIGHTING_TITLE_FONT_SIZE;
	}

}
