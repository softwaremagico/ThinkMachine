package com.softwaremagico.tm.pdf.cybernetics;

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

public class CyberneticsTable extends LateralHeaderPdfPTable {
	private final static float[] WIDTHS = { 0.8f, 3f, 1f, 1f, 2f, 2f, 2f, 2f, 3f };
	private final static int ROWS = 9;
	private final static String GAP = "____";

	public CyberneticsTable() {
		super(WIDTHS);

		addCell(createLateralVerticalTitle(getTranslator().getTranslatedText("cybernetics"), ROWS + 1));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("cyberneticsName")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("cyberneticsPoints")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("cyberneticsIncompatibility")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("cyberneticsUsability")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("cyberneticsQuality")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("cyberneticsActivtation")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("cyberneticsAppearence")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("cyberneticsOthers")));

		for (int i = 0; i < ROWS - 1; i++) {
			addCell(createEmptyElementLine(GAP + GAP + GAP));
			addCell(createEmptyElementLine(GAP));
			addCell(createEmptyElementLine(GAP));
			addCell(createEmptyElementLine(GAP + GAP));
			addCell(createEmptyElementLine(GAP + GAP));
			addCell(createEmptyElementLine(GAP + GAP));
			addCell(createEmptyElementLine(GAP + GAP));
			addCell(createEmptyElementLine(GAP + GAP + GAP + GAP));
		}
	}

	@Override
	protected int getTitleFontSize() {
		return FadingSunsTheme.CYBERNETICS_TITLE_FONT_SIZE;
	}
}
