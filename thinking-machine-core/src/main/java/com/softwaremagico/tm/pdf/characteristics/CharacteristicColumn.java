package com.softwaremagico.tm.pdf.characteristics;

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

import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.characteristics.CharacteristicType;
import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.BaseElement;
import com.softwaremagico.tm.pdf.elements.LateralHeaderPdfPTable;

public class CharacteristicColumn extends LateralHeaderPdfPTable {
	private final static String GAP = "   ";
	private final static int ROW_WIDTH = 60;
	private final static float[] widths = { 1f, 5f };

	public CharacteristicColumn(CharacterPlayer characterPlayer, CharacteristicType characteristicType, CharacteristicName[] content) {
		super(widths);
		addCell(createLateralVerticalTitle(getTranslator().getTranslatedText(characteristicType.getTranslationTag()), content.length));
		addCell(createContent(characterPlayer, content));
	}

	private PdfPCell createContent(CharacterPlayer characterPlayer, CharacteristicName[] content) {
		float[] widths = { 3f, 1f, 0.1f };
		PdfPTable table = new PdfPTable(widths);
		BaseElement.setTablePropierties(table);
		table.getDefaultCell().setBorder(0);

		for (CharacteristicName characteristicName : content) {
			Paragraph paragraph = new Paragraph();
			paragraph.add(new Paragraph(getTranslator().getTranslatedText(characteristicName.getTranslationTag()), new Font(FadingSunsTheme.getLineFont(),
					FadingSunsTheme.CHARACTERISTICS_LINE_FONT_SIZE)));
			paragraph.add(new Paragraph(" (", new Font(FadingSunsTheme.getLineFont(), FadingSunsTheme.CHARACTERISTICS_LINE_FONT_SIZE)));
			if (characterPlayer == null) {
				paragraph.add(new Paragraph(GAP, new Font(FadingSunsTheme.getLineFont(), FadingSunsTheme.CHARACTERISTICS_LINE_FONT_SIZE)));
			} else {
				paragraph.add(new Paragraph(characterPlayer.getStartingValue(characteristicName) + "", new Font(FadingSunsTheme.getHandwrittingFont(),
						FadingSunsTheme.getHandWrittingFontSize(FadingSunsTheme.CHARACTERISTICS_LINE_FONT_SIZE))));
			}
			paragraph.add(new Paragraph(")", new Font(FadingSunsTheme.getLineFont(), FadingSunsTheme.CHARACTERISTICS_LINE_FONT_SIZE)));

			PdfPCell characteristicTitle = new PdfPCell(paragraph);
			characteristicTitle.setBorder(0);
			characteristicTitle.setMinimumHeight(ROW_WIDTH / content.length);
			table.addCell(characteristicTitle);

			// Rectangle
			if (characterPlayer == null) {
				table.addCell(createRectangle());
			} else {
				table.addCell(createRectangle(getCharacteristicValueRepresentation(characterPlayer, characteristicName)));
			}

			// Margin
			PdfPCell margin = new PdfPCell();
			margin.setBorder(0);
			table.addCell(margin);
		}

		PdfPCell cell = new PdfPCell();
		cell.addElement(table);
		BaseElement.setCellProperties(cell);

		return cell;
	}

	private String getCharacteristicValueRepresentation(CharacterPlayer characterPlayer, CharacteristicName characteristicName) {
		if (characterPlayer.getOptionalValue(characteristicName) > 0) {
			return characterPlayer.getValue(characteristicName) + "/"
					+ (characterPlayer.getValue(characteristicName) + characterPlayer.getOptionalValue(characteristicName));
		}
		return characterPlayer.getValue(characteristicName) + "";
	}

	@Override
	protected int getTitleFontSize() {
		return FadingSunsTheme.CHARACTERISTICS_TITLE_FONT_SIZE;
	}

}
