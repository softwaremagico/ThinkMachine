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

public class RangedWeaponsTable extends LateralHeaderPdfPTable {
	private final static float[] WIDTHS = { 1.2f, 4f, 3f, 3f, 5f };
	private final static int ROWS = 12;

	public RangedWeaponsTable() {
		super(WIDTHS);
		addCell(createLateralVerticalTitle(getTranslator().getTranslatedText("rangedWeapons"), ROWS + 1));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponsAction")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponGoal")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponDamage")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponsOthers")));

		addCell(createElementLine(getTranslator().getTranslatedText("aimAction")));
		addCell(createElementLine("+1/" + getTranslator().getTranslatedText("roundAbbreviature")));
		addCell(createElementLine(""));
		addCell(createElementLine(getTranslator().getTranslatedText("aimEffect")));

//		addCell(createElementLine("Barrido"));
//		addCell(createElementLine("-1/" + getTranslator().getTranslatedText("meterAbbreviature")));
//		addCell(createElementLine("+1"));
//		addCell(createElementLine(""));

		addCell(createElementLine(getTranslator().getTranslatedText("burstAction") + " (3)"));
		addCell(createElementLine("+2"));
		addCell(createElementLine("+3"));
		addCell(createElementLine(""));

		addCell(createElementLine(getTranslator().getTranslatedText("burstAction") + " (6)"));
		addCell(createElementLine("-2"));
		addCell(createElementLine("+5"));
		addCell(createElementLine(getTranslator().getTranslatedText("burst6Effect")));

		addCell(createElementLine(getTranslator().getTranslatedText("emptyClipAction")));
		addCell(createElementLine("-4"));
		addCell(createElementLine("+7"));
		addCell(createElementLine(getTranslator().getTranslatedText("emptyClipEffect")));

		addCell(createElementLine(getTranslator().getTranslatedText("supressingFireAction")));
		addCell(createElementLine("-2"));
		addCell(createElementLine("  "));
		addCell(createElementLine(getTranslator().getTranslatedText("weaponSpecial")));

		for (int i = 0; i < ROWS - 5; i++) {
			addCell(createElementLine("_____________"));
			addCell(createElementLine("______"));
			addCell(createElementLine("______"));
			addCell(createElementLine("_________________"));
		}

	}

	@Override
	protected int getTitleFontSize() {
		return FadingSunsTheme.FIGHTING_TITLE_FONT_SIZE;
	}

}
