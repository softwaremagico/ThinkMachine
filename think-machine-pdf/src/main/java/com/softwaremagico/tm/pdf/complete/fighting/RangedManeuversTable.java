package com.softwaremagico.tm.pdf.complete.fighting;

/*-
 * #%L
 * Think Machine (PDF Sheets)
 * %%
 * Copyright (C) 2017 - 2019 Softwaremagico
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

import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.LateralHeaderPdfPTable;
import com.softwaremagico.tm.rules.InvalidXmlElementException;
import com.softwaremagico.tm.rules.character.CharacterPlayer;
import com.softwaremagico.tm.rules.character.combat.CombatAction;
import com.softwaremagico.tm.rules.character.combat.CombatStyle;

public class RangedManeuversTable extends LateralHeaderPdfPTable {
	private static final float[] WIDTHS = { 1.2f, 4f, 3f, 2.5f, 5f };
	private static final int ROWS = 12;
	private static final String GAP = "____________________";
	private static final int NAME_COLUMN_WIDHT = 60;
	private static final int GOAL_COLUMN_WIDHT = 40;
	private static final int DAMAGE_COLUMN_WIDHT = 30;
	private static final int OTHERS_COLUMN_WIDHT = 65;

	public RangedManeuversTable(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
		super(WIDTHS);
		addCell(createLateralVerticalTitle(getTranslator().getTranslatedText("rangedWeapons"), ROWS + 1));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponsAction")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponGoal")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponDamage")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponsOthers")));

		addCell(createFirstEmptyElementLine(getTranslator().getTranslatedText("aimAction")));
		addCell(createEmptyElementLine("+1/" + getTranslator().getTranslatedText("roundAbbreviature")));
		addCell(createEmptyElementLine(""));
		addCell(createEmptyElementLine(getTranslator().getTranslatedText("aimEffect")));

		addCell(createFirstEmptyElementLine(getTranslator().getTranslatedText("burstAction") + " (3)"));
		addCell(createEmptyElementLine("+2"));
		addCell(createEmptyElementLine("+3"));
		addCell(createEmptyElementLine(""));

		addCell(createFirstEmptyElementLine(getTranslator().getTranslatedText("burstAction") + " (6)"));
		addCell(createEmptyElementLine("-2"));
		addCell(createEmptyElementLine("+5"));
		addCell(createEmptyElementLine(getTranslator().getTranslatedText("burst6Effect")));

		addCell(createFirstEmptyElementLine(getTranslator().getTranslatedText("emptyClipAction")));
		addCell(createEmptyElementLine("-4"));
		addCell(createEmptyElementLine("+7"));
		addCell(createEmptyElementLine(getTranslator().getTranslatedText("emptyClipEffect")));

		addCell(createFirstEmptyElementLine(getTranslator().getTranslatedText("supressingFireAction")));
		addCell(createEmptyElementLine("-2"));
		addCell(createEmptyElementLine("  "));
		addCell(createEmptyElementLine(getTranslator().getTranslatedText("weaponSpecial")));

		int addedActions = 0;
		if (characterPlayer != null) {
			for (final CombatStyle style : characterPlayer.getRangedCombatStyles()) {
				for (final CombatAction action : style.getCombatActions()) {
					if (action.isAvailable(characterPlayer)) {
						addCell(createFirstElementLine(action.getName(), NAME_COLUMN_WIDHT, FadingSunsTheme.COMBAT_ACTIONS_CONTENT_FONT_SIZE));
						addCell(createElementLine(action.getGoal(), GOAL_COLUMN_WIDHT, FadingSunsTheme.COMBAT_ACTIONS_CONTENT_FONT_SIZE));
						addCell(createElementLine(action.getDamage(), DAMAGE_COLUMN_WIDHT, FadingSunsTheme.COMBAT_ACTIONS_CONTENT_FONT_SIZE));
						addCell(createElementLine(action.getOthers(), OTHERS_COLUMN_WIDHT, FadingSunsTheme.COMBAT_ACTIONS_CONTENT_FONT_SIZE));
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
