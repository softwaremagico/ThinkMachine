package com.softwaremagico.tm.pdf.skills.occultism;

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

public class PowerTable extends LateralHeaderPdfPTable {
	private final static float[] WIDTHS = { 1.25f, 5f, 2f, 5f, 4f, 4f, 4f, 3f };
	private final static int ROWS = 10;
	private final static String GAP = "__";

	public PowerTable() {
		super(WIDTHS);
		addCell(createLateralVerticalTitle(getTranslator().getTranslatedText("occultismPowers"), ROWS + 1));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("occultismTablePower")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("occultismTableLevel")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("occultismTableRoll")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("occultismTableRange")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("occultismTableDuration")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("occultismTableRequirements")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("occultismTableCost")));

		for (int i = 0; i < ROWS; i++) {
			addCell(createEmptyElementLine(GAP + GAP + GAP + GAP + GAP));
			addCell(createEmptyElementLine(GAP + GAP));
			addCell(createEmptyElementLine(GAP + GAP + GAP + GAP + GAP));
			addCell(createEmptyElementLine(GAP + GAP + GAP + GAP));
			addCell(createEmptyElementLine(GAP + GAP + GAP + GAP + GAP));
			addCell(createEmptyElementLine(GAP + GAP + GAP + GAP));
			addCell(createEmptyElementLine(GAP + GAP + GAP));
		}

	}

	@Override
	protected int getTitleFontSize() {
		return FadingSunsTheme.OCCULSTISM_POWERS_TITLE_FONT_SIZE;
	}

}
