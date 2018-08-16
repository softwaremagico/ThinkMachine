package com.softwaremagico.tm.pdf.complete.traits;

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

import com.itextpdf.text.pdf.PdfPCell;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.VerticalTable;

public class BeneficesTable extends VerticalTable {
	private final static String GAP = "__________________";
	private final static int NAME_COLUMN_WIDTH = 80;
	private final static int COST_COLUMN_WIDTH = 25;
	private final static float[] WIDTHS = { 2f, 5f, 2f, 5f };

	public BeneficesTable(CharacterPlayer characterPlayer) {
		super(WIDTHS);
		addCell(createTitle(getTranslator().getTranslatedText("beneficesTable") + " / " + getTranslator().getTranslatedText("afflictionsTable"),
				FadingSunsTheme.VERTICALTABLE_TITLE_FONT_SIZE));

		addCell(createSubtitleLine(getTranslator().getTranslatedText("beneficesTablePoints"), FadingSunsTheme.TABLE_LINE_FONT_SIZE));
		addCell(createSubtitleLine(getTranslator().getTranslatedText("benefices"), FadingSunsTheme.TABLE_LINE_FONT_SIZE));
		addCell(createSubtitleLine(getTranslator().getTranslatedText("beneficesTablePoints"), FadingSunsTheme.TABLE_LINE_FONT_SIZE));
		addCell(createSubtitleLine(getTranslator().getTranslatedText("afflictions"), FadingSunsTheme.TABLE_LINE_FONT_SIZE));

		for (int i = 0; i < MainPerksTableFactory.EMPTY_ROWS * 2; i++) {
			if (i % 2 == 0) {
				addCell(getBeneficesCost(characterPlayer, i / 2));
				addCell(getBenefices(characterPlayer, i / 2));
			} else {
				addCell(getAfflictionsCost(characterPlayer, i / 2));
				addCell(getAfflictions(characterPlayer, i / 2));
			}
		}
	}

	private PdfPCell getBenefices(CharacterPlayer characterPlayer, int row) {
		try {
			if (characterPlayer != null) {
				return createElementLine(characterPlayer.getAllBenefices().get(row).getName(), NAME_COLUMN_WIDTH);
			}
		} catch (Exception e) {

		}
		return createEmptyElementLine(GAP, NAME_COLUMN_WIDTH);
	}

	private PdfPCell getBeneficesCost(CharacterPlayer characterPlayer, int row) {
		try {
			if (characterPlayer != null) {
				return createElementLine(characterPlayer.getAllBenefices().get(row).getCost(), COST_COLUMN_WIDTH);
			}
		} catch (Exception e) {

		}
		return createEmptyElementLine(GAP, COST_COLUMN_WIDTH);
	}

	private PdfPCell getAfflictions(CharacterPlayer characterPlayer, int row) {
		try {
			if (characterPlayer != null) {
				return createElementLine(characterPlayer.getAfflictions().get(row).getName(), NAME_COLUMN_WIDTH);
			}
		} catch (Exception e) {

		}
		return createEmptyElementLine(GAP, NAME_COLUMN_WIDTH);
	}

	private PdfPCell getAfflictionsCost(CharacterPlayer characterPlayer, int row) {
		try {
			if (characterPlayer != null) {
				return createElementLine(characterPlayer.getAfflictions().get(row).getCost(), COST_COLUMN_WIDTH);
			}
		} catch (Exception e) {

		}
		return createEmptyElementLine(GAP, COST_COLUMN_WIDTH);
	}
}
