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

import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.LateralHeaderPdfPTable;

public class WeaponsTable extends LateralHeaderPdfPTable {
	private final static float[] WIDTHS = { 1.6f, 4f, 5f, 2f, 3f, 3f, 3f, 3f, 3f, 7f };
	private final static int ROWS = 12;

	public WeaponsTable() {
		super(WIDTHS);
		addCell(createLateralVerticalTitle(getTranslator().getTranslatedText("weapons"), ROWS + 1));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weapon")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponRoll")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponGoal")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponDamage")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponRange")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponShots")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponRate")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponSize")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponsOthers")));

		for (int i = 0; i < ROWS; i++) {
			addCell(createEmptyElementLine("___________"));
			addCell(createEmptyElementLine("_____________"));
			addCell(createEmptyElementLine("_____"));
			addCell(createEmptyElementLine("______"));
			addCell(createEmptyElementLine("________"));
			addCell(createEmptyElementLine("______"));
			addCell(createEmptyElementLine("______"));
			addCell(createEmptyElementLine("______"));
			addCell(createEmptyElementLine("__________________"));
		}

		// addCell(createElementLine(""));
		// addCell(createElementLine(""));
		// addCell(createElementLine(""));
		// addCell(createElementLine(""));
		// addCell(createElementLine(""));
		// addCell(createElementLine(""));
		// addCell(createElementLine(""));
		// addCell(createElementLine(""));
		// addCell(createElementLine(""));
		// addCell(createElementLine(""));

	}

	@Override
	protected int getTitleFontSize() {
		return FadingSunsTheme.FIGHTING_TITLE_FONT_SIZE;
	}

}
