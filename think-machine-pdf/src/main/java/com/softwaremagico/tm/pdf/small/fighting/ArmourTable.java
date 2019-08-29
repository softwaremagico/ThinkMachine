package com.softwaremagico.tm.pdf.small.fighting;

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
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.configurator.MachinePdfConfigurationReader;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.pdf.complete.elements.CustomPdfTable;

public class ArmourTable extends CustomPdfTable {
	private static final String GAP = "____________________";
	private static final int NAME_COLUMN_WIDTH = 60;
	private static final int ARMOUR_VALUE_COLUMN_WIDTH = 10;

	public ArmourTable(CharacterPlayer characterPlayer) {
		super(new float[] { 2.2f, 3 });
		getDefaultCell().setBorder(0);

		// Armor
		final Font font = new Font(FadingSunsTheme.getTitleFont(),
				FadingSunsTheme.CHARACTER_SMALL_ARMOR_TITLE_FONT_SIZE);
		final Phrase content = new Phrase(getTranslator().getTranslatedText("armor"), font);
		final PdfPCell titleCell = new PdfPCell(content);
		titleCell.setBorder(0);
		addCell(titleCell);

		final PdfPCell nameCell;
		if (characterPlayer == null) {
			nameCell = CustomPdfTable.createEmptyElementLine(GAP, NAME_COLUMN_WIDTH);
		} else if (characterPlayer.getArmour() == null) {
			nameCell = createElementLine("", NAME_COLUMN_WIDTH);
		} else {
			nameCell = createElementLine(characterPlayer.getArmour().getProtection() + "d "
					+ characterPlayer.getArmour().getName(), NAME_COLUMN_WIDTH,
					FadingSunsTheme.ARMOUR_CONTENT_FONT_SIZE - 1);
		}
		nameCell.setBorder(0);
		nameCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		nameCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		addCell(nameCell);

		// Shield
		if (MachinePdfConfigurationReader.getInstance().isSmallPdfShieldEnabled()) {
			addCell(getShieldRange(characterPlayer));

			final Paragraph paragraph = new Paragraph();
			paragraph.add(new Paragraph(getTranslator().getTranslatedText("shieldHits") + ": ", new Font(
					FadingSunsTheme.getLineFont(), FadingSunsTheme.ARMOUR_CONTENT_FONT_SIZE)));
			if (characterPlayer != null && characterPlayer.getShield() != null) {
				paragraph.add(new Paragraph(characterPlayer.getShield().getHits() + "-", new Font(FadingSunsTheme
						.getHandwrittingFont(), FadingSunsTheme
						.getHandWrittingFontSize(FadingSunsTheme.ARMOUR_CONTENT_FONT_SIZE))));
			}
			final PdfPCell shieldCell = new PdfPCell(paragraph);
			shieldCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			shieldCell.setBorder(0);
			addCell(shieldCell);
		}
	}

	private PdfPTable getShieldRange(CharacterPlayer characterPlayer) {
		final float[] widths = { 1f, 3f, 1f, 3f, 1f };
		final PdfPTable table = new PdfPTable(widths);
		BaseElement.setTablePropierties(table);
		table.getDefaultCell().setBorder(0);
		table.getDefaultCell().setPadding(0);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

		table.addCell(createEmptyElementLine("("));
		if (characterPlayer == null || characterPlayer.getShield() == null) {
			table.addCell(createEmptyElementLine(" ", ARMOUR_VALUE_COLUMN_WIDTH));
		} else {
			table.addCell(createElementLine(characterPlayer.getShield().getImpact() + "", ARMOUR_VALUE_COLUMN_WIDTH));
		}
		table.addCell(createEmptyElementLine("/"));
		if (characterPlayer == null || characterPlayer.getShield() == null) {
			table.addCell(createEmptyElementLine(" ", ARMOUR_VALUE_COLUMN_WIDTH));
		} else {
			table.addCell(createElementLine(characterPlayer.getShield().getForce() + "", ARMOUR_VALUE_COLUMN_WIDTH));
		}
		table.addCell(createEmptyElementLine(")"));

		return table;
	}

}
