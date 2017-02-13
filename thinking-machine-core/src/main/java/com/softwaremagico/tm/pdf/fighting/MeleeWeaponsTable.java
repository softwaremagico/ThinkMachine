package com.softwaremagico.tm.pdf.fighting;

/*-
 * #%L
 * The Thinking Machine (Core)
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
import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.LateralHeaderPdfPTable;

public class MeleeWeaponsTable extends LateralHeaderPdfPTable {
	private final static float[] WIDTHS = { 1.2f, 4f, 3f, 3f, 5f };
	private final static int ROWS = 12;
	private final static String GAP = "_________________";
	private final static int NAME_COLUMN_WIDHT = 50;
	private final static int GOAL_COLUMN_WIDHT = 40;
	private final static int DAMAGE_COLUMN_WIDHT = 40;
	private final static int OTHERS_COLUMN_WIDHT = 60;

	public MeleeWeaponsTable(CharacterPlayer characterPlayer) {
		super(WIDTHS);
		addCell(createLateralVerticalTitle(getTranslator().getTranslatedText("meleeWeapons"), ROWS + 1));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponsAction")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponGoal")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponDamage")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponsOthers")));

		addCell(createEmptyElementLine(getTranslator().getTranslatedText("actionStrike")));
		addCell(createEmptyElementLine(""));
		addCell(createEmptyElementLine("2" + getTranslator().getTranslatedText("diceAbbreviature") + "/"
				+ getTranslator().getTranslatedText("weaponAbbreviature")));
		addCell(createEmptyElementLine(""));

		addCell(createEmptyElementLine(getTranslator().getTranslatedText("actionGrapple")));
		addCell(createEmptyElementLine(""));
		addCell(createEmptyElementLine("2" + getTranslator().getTranslatedText("diceAbbreviature")));
		addCell(createEmptyElementLine(getTranslator().getTranslatedText("strengthAbbreviature") + "+"
				+ getTranslator().getTranslatedText("vigorAbbreviature") + "/" + getTranslator().getTranslatedText("strengthAbbreviature")
				+ "+" + getTranslator().getTranslatedText("vigorAbbreviature")));

		addCell(createEmptyElementLine(getTranslator().getTranslatedText("actionKnockdown")));
		addCell(createEmptyElementLine(""));
		addCell(createEmptyElementLine("3" + getTranslator().getTranslatedText("diceAbbreviature")));
		addCell(createEmptyElementLine(getTranslator().getTranslatedText("strengthAbbreviature") + "+"
				+ getTranslator().getTranslatedText("meleeAbbreviature") + "/" + getTranslator().getTranslatedText("dexterityAbbreviature")
				+ "+" + getTranslator().getTranslatedText("vigorAbbreviature")));

		addCell(createEmptyElementLine(getTranslator().getTranslatedText("actionDisarm")));
		addCell(createEmptyElementLine("-4"));
		addCell(createEmptyElementLine("2" + getTranslator().getTranslatedText("diceAbbreviature") + "/"
				+ getTranslator().getTranslatedText("weaponAbbreviature")));
		addCell(createEmptyElementLine(getTranslator().getTranslatedText("weaponDamage") + "/"
				+ getTranslator().getTranslatedText("strengthAbbreviature") + "+" + getTranslator().getTranslatedText("vigorAbbreviature")));

		addCell(createEmptyElementLine(getTranslator().getTranslatedText("actionKnockout")));
		addCell(createEmptyElementLine("-4"));
		addCell(createEmptyElementLine("2" + getTranslator().getTranslatedText("diceAbbreviature") + "/"
				+ getTranslator().getTranslatedText("weaponAbbreviature")));
		addCell(createEmptyElementLine(getTranslator().getTranslatedText("weaponSpecial")));

		addCell(createEmptyElementLine(getTranslator().getTranslatedText("actionCharge")));
		addCell(createEmptyElementLine(""));
		addCell(createEmptyElementLine("1" + getTranslator().getTranslatedText("diceAbbreviature") + "/"
				+ getTranslator().getTranslatedText("meterAbbreviature")));
		addCell(createEmptyElementLine(getTranslator().getTranslatedText("maximumAbbreviature") + " 4"
				+ getTranslator().getTranslatedText("diceAbbreviature")));

		int addedActions = 0;
		if (characterPlayer != null) {
			for (CombatStyle style : characterPlayer.getMeleeCombatStyles()) {
				for (CombatAction action : style.getElements()) {
					addCell(createElementLine(action.getName(), NAME_COLUMN_WIDHT, FadingSunsTheme.COMBAT_ACTIONS_CONTENT_FONT_SIZE));
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
