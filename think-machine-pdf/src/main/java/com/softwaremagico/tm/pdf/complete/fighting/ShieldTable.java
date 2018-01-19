package com.softwaremagico.tm.pdf.complete.fighting;

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
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.pdf.complete.elements.LateralHeaderPdfPTable;

public class ShieldTable extends LateralHeaderPdfPTable {
	private final static float[] WIDTHS = { 1f, 4f };
	private final static int ROWS = 4;
	private final static String GAP = "___________________";
	private final static int NAME_COLUMN_WIDTH = 70;
	private final static int HITS_COLUMN_WIDTH = 70;

	public ShieldTable(CharacterPlayer characterPlayer) {
		super(WIDTHS);
		getDefaultCell().setBorder(0);

		addCell(createLateralVerticalTitle(getTranslator().getTranslatedText("shield"), ROWS + 1));

		PdfPCell nameCell;
		if (characterPlayer == null || characterPlayer.getShield() == null) {
			nameCell = createEmptyElementLine(GAP, NAME_COLUMN_WIDTH);
		} else {
			nameCell = createElementLine(characterPlayer.getShield().getName(), NAME_COLUMN_WIDTH, FadingSunsTheme.SHIELD_CONTENT_FONT_SIZE);
		}
		nameCell.setColspan(WIDTHS.length);
		nameCell.setMinimumHeight(20);
		addCell(nameCell);

		addCell(getShieldRange(characterPlayer));
		if (characterPlayer == null || characterPlayer.getShield() == null) {
			addCell(createEmptyElementLine(getTranslator().getTranslatedText("shieldHits") + " " + GAP, HITS_COLUMN_WIDTH));
		} else {
			Paragraph paragraph = new Paragraph();
			paragraph.add(new Paragraph(getTranslator().getTranslatedText("shieldHits"), new Font(FadingSunsTheme.getLineFont(),
					FadingSunsTheme.TABLE_LINE_FONT_SIZE)));

			paragraph.add(new Paragraph(characterPlayer.getShield().getHits() + " ", new Font(FadingSunsTheme.getHandwrittingFont(),
					FadingSunsTheme.SHIELD_CONTENT_FONT_SIZE)));

			PdfPCell protectionCell = createEmptyElementLine("");
			protectionCell.setPhrase(paragraph);

			addCell(protectionCell);
		}
		addCell(createEmptyElementLine("___________________"));
		addCell(createEmptyElementLine("___________________"));
	}

	private PdfPTable getShieldRange(CharacterPlayer characterPlayer) {
		float[] widths = { 1f, 1f, 1f, 1f, 1f };
		PdfPTable table = new PdfPTable(widths);
		BaseElement.setTablePropierties(table);
		table.getDefaultCell().setBorder(0);
		table.getDefaultCell().setPadding(0);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

		table.addCell(createEmptyElementLine("("));
		if (characterPlayer == null || characterPlayer.getShield() == null) {
			table.addCell(createRectangle());
		} else {
			table.addCell(createRectangle(characterPlayer.getShield().getImpact()));
		}
		table.addCell(createEmptyElementLine("/"));
		if (characterPlayer == null || characterPlayer.getShield() == null) {
			table.addCell(createRectangle());
		} else {
			table.addCell(createRectangle(characterPlayer.getShield().getForce()));
		}
		table.addCell(createEmptyElementLine(")"));

		return table;
	}

	@Override
	protected int getTitleFontSize() {
		return FadingSunsTheme.ARMOUR_TITLE_FONT_SIZE;
	}

}
