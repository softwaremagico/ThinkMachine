package com.softwaremagico.tm.pdf.small.info;

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

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;

public class DescriptionTableFactory extends BaseElement {
	public static final int PADDING = 2;

	public static PdfPTable getDescriptionTable(CharacterPlayer characterPlayer, String language, String moduleName) {
		final float[] widths = { 1f, 20f, 1f };
		final PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);
		table.setSpacingAfter(0);
		table.getDefaultCell().setBorder(0);
		table.getDefaultCell().setPadding(PADDING);

		final PdfPCell whiteSeparator = createWhiteSeparator();

		final PdfPCell blackSeparator = createBlackSeparator();

		table.addCell(whiteSeparator);

		final PdfPCell descriptionCell = new PdfPCell(getCharacterDescription(characterPlayer));
		descriptionCell.setPadding(0);
		descriptionCell.setPaddingTop(10);
		descriptionCell.setMinimumHeight(100);
		descriptionCell.setBorder(0);
		table.addCell(descriptionCell);
		table.addCell(whiteSeparator);

		table.addCell(whiteSeparator);
		table.addCell(blackSeparator);
		table.addCell(whiteSeparator);

		table.addCell(whiteSeparator);
		final PdfPCell backgroundCell = new PdfPCell(getCharacterBackground(characterPlayer));
		backgroundCell.setPadding(0);
		backgroundCell.setPaddingTop(10);
		backgroundCell.setBorder(0);
		table.addCell(backgroundCell);
		table.addCell(whiteSeparator);

		return table;
	}

	private static Paragraph getCharacterDescription(CharacterPlayer characterPlayer) {
		final Paragraph paragraph = new Paragraph();
		paragraph.add(new Paragraph(getTranslator().getTranslatedText("characterAnnotations") + ": ", new Font(
				FadingSunsTheme.getTitleFont(), FadingSunsTheme.CHARACTER_SMALL_DESCRIPTION_TITLE_FONT_SIZE)));
		if (characterPlayer != null) {
			paragraph.add(new Paragraph(characterPlayer.getInfo().getCharacterDescription(), new Font(FadingSunsTheme
					.getHandwrittingFont(), FadingSunsTheme
					.getHandWrittingFontSize(FadingSunsTheme.CHARACTER_SMALL_DESCRIPTION_FONT_SIZE))));
		}
		return paragraph;
	}

	private static Paragraph getCharacterBackground(CharacterPlayer characterPlayer) {
		final Paragraph paragraph = new Paragraph();
		paragraph.add(new Paragraph(getTranslator().getTranslatedText("historyAnnotations") + ": ", new Font(
				FadingSunsTheme.getTitleFont(), FadingSunsTheme.CHARACTER_SMALL_DESCRIPTION_TITLE_FONT_SIZE)));
		if (characterPlayer != null) {
			paragraph.add(new Paragraph(characterPlayer.getInfo().getBackgroundDecription(), new Font(FadingSunsTheme
					.getHandwrittingFont(), FadingSunsTheme
					.getHandWrittingFontSize(FadingSunsTheme.CHARACTER_SMALL_DESCRIPTION_FONT_SIZE))));
		}
		return paragraph;
	}

}
