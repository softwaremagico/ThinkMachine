package com.softwaremagico.tm.pdf.complete.fighting;

/*-
 * #%L
 * Think Machine (Core)
 * %%
 * Copyright (C) 2017 Softwaremagico
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

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.combat.CombatAction;
import com.softwaremagico.tm.character.combat.CombatStyle;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.LateralHeaderPdfPTable;

public class MeleeWeaponsTable extends LateralHeaderPdfPTable {
	private final static float[] WIDTHS = { 1.2f, 5f, 1.5f, 3f, 6f };
	private final static int ROWS = 12;
	private final static String GAP = "____________________";
	private final static int NAME_COLUMN_WIDHT = 65;
	private final static int GOAL_COLUMN_WIDHT = 20;
	private final static int DAMAGE_COLUMN_WIDHT = 40;
	private final static int OTHERS_COLUMN_WIDHT = 70;

	public MeleeWeaponsTable(CharacterPlayer characterPlayer) {
		super(WIDTHS);
		addCell(createLateralVerticalTitle(getTranslator().getTranslatedText("meleeWeapons"), ROWS + 1));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponsAction")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponGoal")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponDamage")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponsOthers")));

		addCell(createFirstEmptyElementLine(getTranslator().getTranslatedText("actionStrike")));
		addCell(createEmptyElementLine(""));
		if (characterPlayer != null) {
			addCell(createEmptyElementLine((2 + characterPlayer.getStrengthDamangeModification()) + getTranslator().getTranslatedText("diceAbbreviature") + "/"
					+ getTranslator().getTranslatedText("weaponAbbreviature")));
		} else {
			addCell(createEmptyElementLine(2 + getTranslator().getTranslatedText("diceAbbreviature") + "/"
					+ getTranslator().getTranslatedText("weaponAbbreviature")));
		}
		addCell(createEmptyElementLine(""));

		addCell(createFirstEmptyElementLine(getTranslator().getTranslatedText("actionGrapple")));
		addCell(createEmptyElementLine(""));
		if (characterPlayer != null) {
			addCell(createEmptyElementLine((2 + characterPlayer.getStrengthDamangeModification()) + getTranslator().getTranslatedText("diceAbbreviature")));
		} else {
			addCell(createEmptyElementLine(2 + getTranslator().getTranslatedText("diceAbbreviature")));
		}
		addCell(createEmptyElementLine(getTranslator().getTranslatedText("strengthAbbreviature") + "+" + getTranslator().getTranslatedText("vigorAbbreviature")
				+ "/" + getTranslator().getTranslatedText("strengthAbbreviature") + "+" + getTranslator().getTranslatedText("vigorAbbreviature")));

		addCell(createFirstEmptyElementLine(getTranslator().getTranslatedText("actionKnockdown")));
		addCell(createEmptyElementLine(""));
		if (characterPlayer != null) {
			addCell(createEmptyElementLine((3 + characterPlayer.getStrengthDamangeModification()) + getTranslator().getTranslatedText("diceAbbreviature")));
		} else {
			addCell(createEmptyElementLine(3 + getTranslator().getTranslatedText("diceAbbreviature")));
		}
		addCell(createEmptyElementLine(getTranslator().getTranslatedText("strengthAbbreviature") + "+" + getTranslator().getTranslatedText("meleeAbbreviature")
				+ "/" + getTranslator().getTranslatedText("dexterityAbbreviature") + "+" + getTranslator().getTranslatedText("vigorAbbreviature")));

		addCell(createFirstEmptyElementLine(getTranslator().getTranslatedText("actionDisarm")));
		addCell(createEmptyElementLine("-4"));
		if (characterPlayer != null) {
			addCell(createEmptyElementLine((2 + characterPlayer.getStrengthDamangeModification()) + getTranslator().getTranslatedText("diceAbbreviature") + "/"
					+ getTranslator().getTranslatedText("weaponAbbreviature")));
		} else {
			addCell(createEmptyElementLine(3 + getTranslator().getTranslatedText("diceAbbreviature")));
		}
		addCell(createEmptyElementLine(getTranslator().getTranslatedText("weaponDamage") + "/" + getTranslator().getTranslatedText("strengthAbbreviature")
				+ "+" + getTranslator().getTranslatedText("vigorAbbreviature")));

		addCell(createFirstEmptyElementLine(getTranslator().getTranslatedText("actionKnockout")));
		addCell(createEmptyElementLine("-4"));
		if (characterPlayer != null) {
			addCell(createEmptyElementLine((2 + characterPlayer.getStrengthDamangeModification()) + getTranslator().getTranslatedText("diceAbbreviature") + "/"
					+ getTranslator().getTranslatedText("weaponAbbreviature")));
		} else {
			addCell(createEmptyElementLine(3 + getTranslator().getTranslatedText("diceAbbreviature")));
		}
		addCell(createEmptyElementLine(getTranslator().getTranslatedText("weaponSpecial")));

		addCell(createFirstEmptyElementLine(getTranslator().getTranslatedText("actionCharge")));
		addCell(createEmptyElementLine(""));
		if (characterPlayer != null) {
			addCell(createEmptyElementLine((1 + characterPlayer.getStrengthDamangeModification()) + getTranslator().getTranslatedText("diceAbbreviature") + "/"
					+ getTranslator().getTranslatedText("meterAbbreviature")));
		} else {
			addCell(createEmptyElementLine(1 + getTranslator().getTranslatedText("diceAbbreviature") + "/"
					+ getTranslator().getTranslatedText("meterAbbreviature")));
		}
		addCell(createEmptyElementLine(getTranslator().getTranslatedText("maximumAbbreviature") + " 4" + getTranslator().getTranslatedText("diceAbbreviature")));

		int addedActions = 0;
		if (characterPlayer != null) {
			for (CombatStyle style : characterPlayer.getMeleeCombatStyles()) {
				for (CombatAction action : style.getElements()) {
					addCell(createFirstElementLine(action.getName(), NAME_COLUMN_WIDHT, FadingSunsTheme.COMBAT_ACTIONS_CONTENT_FONT_SIZE));
					addCell(createElementLine(action.getGoal(), GOAL_COLUMN_WIDHT, FadingSunsTheme.COMBAT_ACTIONS_CONTENT_FONT_SIZE));
					addCell(createElementLine(action.getDamage(), DAMAGE_COLUMN_WIDHT, FadingSunsTheme.COMBAT_ACTIONS_CONTENT_FONT_SIZE));
					addCell(createElementLine(action.getOthers(), OTHERS_COLUMN_WIDHT, FadingSunsTheme.COMBAT_ACTIONS_CONTENT_FONT_SIZE));
					addedActions++;
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
