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
import com.softwaremagico.tm.character.combat.LearnedStance;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.LateralHeaderPdfPTable;

public class StancesTable extends LateralHeaderPdfPTable {
	private final static float[] WIDTHS = { 2f, 11f, 23f };
	private final static String GAP = "_____________________________________";
	private final static int ROWS = 5;

	private final static int NAME_COLUMN_WIDTH = 100;
	private final static int DESCRIPTION_COLUMN_WIDTH = 200;

	protected StancesTable(CharacterPlayer characterPlayer) {
		super(WIDTHS);

		addCell(createLateralVerticalTitle(
				getTranslator().getTranslatedText("stances"), ROWS + 3));

		addCell(createTableSubtitleElement(
				getTranslator().getTranslatedText("stanceName"), 13));
		addCell(createTableSubtitleElement(
				getTranslator().getTranslatedText("stanceDescription"), 13));

		addCell(createFirstEmptyElementLine(getTranslator().getTranslatedText(
				"agressiveStance")));
		addCell(createEmptyElementLine(getTranslator().getTranslatedText(
				"agressiveStanceDescription")));

		addCell(createFirstEmptyElementLine(getTranslator().getTranslatedText(
				"defensiveStance")));
		addCell(createEmptyElementLine(getTranslator().getTranslatedText(
				"defensiveStanceDescription")));

		addCell(createFirstEmptyElementLine(getTranslator().getTranslatedText(
				"fullDefensiveStance")));
		addCell(createEmptyElementLine(getTranslator().getTranslatedText(
				"fullDefensiveStanceDescription")));

		int added = 0;
		if (characterPlayer != null) {
			for (LearnedStance stance : characterPlayer.getLearnedStances()) {
				addCell(createElementLine(stance.getName(), NAME_COLUMN_WIDTH));
				addCell(createElementLine(stance.getDescription(),
						DESCRIPTION_COLUMN_WIDTH));
				added++;
			}
		}

		for (int i = 0; i < ROWS - 3 - added; i++) {
			addCell(createEmptyElementLine(GAP, NAME_COLUMN_WIDTH));
			addCell(createEmptyElementLine(GAP + GAP, DESCRIPTION_COLUMN_WIDTH));
		}
	}

	@Override
	protected int getTitleFontSize() {
		return FadingSunsTheme.FIGHTING_TITLE_FONT_SIZE;
	}

}
