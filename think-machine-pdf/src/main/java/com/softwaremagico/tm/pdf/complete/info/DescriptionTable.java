package com.softwaremagico.tm.pdf.complete.info;

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

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.VerticalTable;
import com.softwaremagico.tm.pdf.complete.utils.CellUtils;

public class DescriptionTable extends VerticalTable {
	private final static float[] WIDTHS = { 1f };
	private final static String LANGUAGE_PREFIX = "info";
	private final static String GAP = "_______________________________________________";
	private final static int COLUMN_WIDTH = 170;

	public DescriptionTable(CharacterPlayer characterPlayer) {
		super(WIDTHS);

		addCell(createTitle(getTranslator().getTranslatedText("description"), FadingSunsTheme.VERTICALTABLE_TITLE_FONT_SIZE));
		addCell(createLine(characterPlayer, "birthdate"));
		addCell(createLine(characterPlayer, "hair"));
		addCell(createLine(characterPlayer, "eyes"));
		addCell(createLine(characterPlayer, "complexion"));
		addCell(createLine(characterPlayer, "height"));
		addCell(createLine(characterPlayer, "weight"));
		addCell(createLine(characterPlayer, "appearance"));
		addCell(createEmptyElementLine(GAP, COLUMN_WIDTH));
		addCell(createEmptyElementLine(GAP, COLUMN_WIDTH));
		addCell(createEmptyElementLine(GAP, COLUMN_WIDTH));
	}

	private PdfPCell createLine(CharacterPlayer characterPlayer, String tag) {
		Paragraph paragraph = new Paragraph();

		String text = getTranslatedTag(tag);
		// Spaces at the end are eliminated. For calculating width we can put
		// the characters in different order.
		float textWidth = FadingSunsTheme.getLineFont().getWidthPoint(text + " :", FadingSunsTheme.TABLE_LINE_FONT_SIZE);

		paragraph.add(new Paragraph(text + ": ", new Font(FadingSunsTheme.getLineFont(), FadingSunsTheme.TABLE_LINE_FONT_SIZE)));
		if (characterPlayer == null || characterPlayer.getInfo().getTranslatedParameter(tag) == null) {
			paragraph.add(new Paragraph(CellUtils.getSubStringFitsIn(GAP, FadingSunsTheme.getLineFont(), FadingSunsTheme.TABLE_LINE_FONT_SIZE, COLUMN_WIDTH
					- textWidth), new Font(FadingSunsTheme.getLineFont(), FadingSunsTheme.TABLE_LINE_FONT_SIZE)));
		} else {
			paragraph.add(new Paragraph(CellUtils.getSubStringFitsIn(characterPlayer.getInfo().getTranslatedParameter(tag),
					FadingSunsTheme.getHandwrittingFont(), FadingSunsTheme.INFO_CONTENT_FONT_SIZE, COLUMN_WIDTH - textWidth), new Font(FadingSunsTheme
					.getHandwrittingFont(), FadingSunsTheme.INFO_CONTENT_FONT_SIZE)));
		}

		PdfPCell cell = createEmptyElementLine("");
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setPhrase(paragraph);

		return cell;
	}

	private static String getTranslatedTag(String tag) {
		String value = getTranslator().getTranslatedText(LANGUAGE_PREFIX + tag.substring(0, 1).toUpperCase() + tag.substring(1));
		return value;
	}
}
