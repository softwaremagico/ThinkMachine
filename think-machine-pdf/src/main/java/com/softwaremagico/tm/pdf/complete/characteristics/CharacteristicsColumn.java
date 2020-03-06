package com.softwaremagico.tm.pdf.complete.characteristics;

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

import java.util.List;

import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.characteristics.CharacteristicType;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.pdf.complete.elements.LateralHeaderPdfPTable;

public class CharacteristicsColumn extends LateralHeaderPdfPTable {
	private static final String GAP = "   ";
	private static final int ROW_WIDTH = 60;
	private static final float[] widths = { 1f, 5f };

	public CharacteristicsColumn(CharacterPlayer characterPlayer, CharacteristicType characteristicType, List<CharacteristicDefinition> content) {
		super(widths);
		addCell(createLateralVerticalTitle(getTranslator().getTranslatedText(characteristicType.getTranslationTag()), content != null ? content.size() : 3));
		addCell(createContent(characterPlayer, content));
	}

	private PdfPCell createContent(CharacterPlayer characterPlayer, List<CharacteristicDefinition> content) {
		final float[] widths = { 3f, 1f, 0.1f };
		final PdfPTable table = new PdfPTable(widths);
		BaseElement.setTablePropierties(table);
		table.getDefaultCell().setBorder(0);

		if (content != null) {
			for (final CharacteristicDefinition characteristic : content) {
				final Paragraph paragraph = new Paragraph();
				paragraph.add(new Paragraph(getTranslator().getTranslatedText(characteristic.getId()),
						new Font(FadingSunsTheme.getLineFont(), FadingSunsTheme.CHARACTERISTICS_LINE_FONT_SIZE)));
				paragraph.add(new Paragraph(" (", new Font(FadingSunsTheme.getLineFont(), FadingSunsTheme.CHARACTERISTICS_LINE_FONT_SIZE)));
				if (characterPlayer == null) {
					paragraph.add(new Paragraph(GAP, new Font(FadingSunsTheme.getLineFont(), FadingSunsTheme.CHARACTERISTICS_LINE_FONT_SIZE)));
				} else {
					paragraph.add(new Paragraph(characterPlayer.getStartingValue(characteristic.getCharacteristicName()) + "", new Font(
							FadingSunsTheme.getHandwrittingFont(), FadingSunsTheme.getHandWrittingFontSize(FadingSunsTheme.CHARACTERISTICS_LINE_FONT_SIZE))));
				}
				paragraph.add(new Paragraph(")", new Font(FadingSunsTheme.getLineFont(), FadingSunsTheme.CHARACTERISTICS_LINE_FONT_SIZE)));

				final PdfPCell characteristicTitle = new PdfPCell(paragraph);
				characteristicTitle.setBorder(0);
				characteristicTitle.setMinimumHeight(ROW_WIDTH / (float) content.size());
				table.addCell(characteristicTitle);

				// Rectangle
				if (characterPlayer == null) {
					table.addCell(createRectangle());
				} else {
					table.addCell(createRectangle(characterPlayer.getValue(characteristic.getCharacteristicName())
							+ getCharacteristicSpecialRepresentation(characterPlayer, characteristic.getCharacteristicName())));
				}

				// Margin
				final PdfPCell margin = new PdfPCell();
				margin.setBorder(0);
				table.addCell(margin);
			}
		}

		final PdfPCell cell = new PdfPCell();
		cell.addElement(table);
		BaseElement.setCellProperties(cell);

		return cell;
	}

	private String getCharacteristicSpecialRepresentation(CharacterPlayer characterPlayer, CharacteristicName characteristicName) {
		final StringBuilder representation = new StringBuilder("");
		if (characterPlayer.hasCharacteristicTemporalModificator(characteristicName)) {
			representation.append("*");
		}
		if (characterPlayer.hasCharacteristicModificator(characteristicName)) {
			representation.append("!");
		}
		return representation.toString();
	}

	@Override
	protected int getTitleFontSize() {
		return FadingSunsTheme.CHARACTERISTICS_TITLE_FONT_SIZE;
	}

}
