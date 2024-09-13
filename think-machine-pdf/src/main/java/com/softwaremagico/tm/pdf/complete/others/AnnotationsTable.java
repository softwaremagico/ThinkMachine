package com.softwaremagico.tm.pdf.complete.others;

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

import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.LateralHeaderPdfPTable;
import com.softwaremagico.tm.pdf.complete.utils.CellUtils;

public class AnnotationsTable extends LateralHeaderPdfPTable {
	private static final float[] WIDTHS = { 1f, 24f };
	private static final float CELL_HEIGHT = 65;
	private static final int DESCRIPTION_WIDTH = 2500;

	public AnnotationsTable(CharacterPlayer characterPlayer) {
		super(WIDTHS, false);

		final PdfPCell cell = createLateralVerticalTitle(getTranslator().getTranslatedText("annotationsTable"), 2);
		cell.setBorderWidth(2);
		cell.setMinimumHeight(CELL_HEIGHT * 2);
		addCell(cell);

		final PdfPCell descriptionCell = new PdfPCell(getCharacterDescription(characterPlayer));
		descriptionCell.setMinimumHeight(CELL_HEIGHT);
		descriptionCell.setBorderWidth(0);
		addCell(descriptionCell);

		final PdfPCell backgroundCell = new PdfPCell(getCharacterBackground(characterPlayer));
		backgroundCell.setBorderWidth(2);
		addCell(backgroundCell);
	}

	@Override
	protected int getTitleFontSize() {
		return FadingSunsTheme.ANNOTATIONS_TITLE_FONT_SIZE;
	}

	private static Paragraph getCharacterDescription(CharacterPlayer characterPlayer) {
		final Paragraph paragraph = new Paragraph();
		paragraph.add(new Paragraph(getTranslator().getTranslatedText("characterAnnotations") + ": ", new Font(
				FadingSunsTheme.getTitleFont(), FadingSunsTheme.ANNOTATIONS_SUBTITLE_FONT_SIZE)));
		if (characterPlayer != null) {
			paragraph.add(new Paragraph(CellUtils.getSubStringFitsIn(characterPlayer.getInfo()
					.getCharacterDescription(), FadingSunsTheme.getHandwrittingFont(), FadingSunsTheme
					.getHandWrittingFontSize(FadingSunsTheme.CHARACTER_DESCRIPTION_FONT_SIZE), DESCRIPTION_WIDTH),
					new Font(FadingSunsTheme.getHandwrittingFont(), FadingSunsTheme
							.getHandWrittingFontSize(FadingSunsTheme.CHARACTER_DESCRIPTION_FONT_SIZE))));
		}
		return paragraph;
	}

	private static Paragraph getCharacterBackground(CharacterPlayer characterPlayer) {
		final Paragraph paragraph = new Paragraph();
		paragraph.add(new Paragraph(getTranslator().getTranslatedText("historyAnnotations") + ": ", new Font(
				FadingSunsTheme.getTitleFont(), FadingSunsTheme.ANNOTATIONS_SUBTITLE_FONT_SIZE)));
		if (characterPlayer != null) {
			paragraph.add(new Paragraph(CellUtils.getSubStringFitsIn(characterPlayer.getInfo()
					.getBackgroundDecription(), FadingSunsTheme.getHandwrittingFont(), FadingSunsTheme
					.getHandWrittingFontSize(FadingSunsTheme.CHARACTER_DESCRIPTION_FONT_SIZE), DESCRIPTION_WIDTH),
					new Font(FadingSunsTheme.getHandwrittingFont(), FadingSunsTheme
							.getHandWrittingFontSize(FadingSunsTheme.CHARACTER_DESCRIPTION_FONT_SIZE))));
		}
		return paragraph;
	}

}
