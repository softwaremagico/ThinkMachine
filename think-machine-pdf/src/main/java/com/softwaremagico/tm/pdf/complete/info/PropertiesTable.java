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
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.VerticalTable;
import com.softwaremagico.tm.pdf.complete.utils.CellUtils;

public class PropertiesTable extends VerticalTable {
	private final static float[] WIDTHS = { 1f };
	private final static int ROWS = 8;

	public PropertiesTable(CharacterPlayer characterPlayer) {
		super(WIDTHS);

		addCell(createTitle(getTranslator().getTranslatedText("properties"), FadingSunsTheme.VERTICALTABLE_TITLE_FONT_SIZE));
		addCell(getCell(getMoney(characterPlayer), 0, 1, Element.ALIGN_LEFT));
		for (int i = 0; i < ROWS; i++) {
			addCell(createEmptyElementLine("______________________________________________"));
		}
	}

	private Paragraph getMoney(CharacterPlayer characterPlayer) {
		Paragraph paragraph = new Paragraph();
		paragraph.add(new Paragraph(getTranslator().getTranslatedText("firebirds"), new Font(FadingSunsTheme.getLineFont(),
				FadingSunsTheme.TABLE_LINE_FONT_SIZE)));
		String moneyText = "";
		float usedWidth = 0;
		if (characterPlayer != null) {
			moneyText = "  " + characterPlayer.getMoney() + "- ";
			usedWidth = FadingSunsTheme.getHandwrittingFont().getWidthPoint(moneyText, FadingSunsTheme.TABLE_LINE_FONT_SIZE - 1);
			paragraph.add(new Paragraph(moneyText, new Font(FadingSunsTheme.getHandwrittingFont(), FadingSunsTheme
					.getHandWrittingFontSize(FadingSunsTheme.TABLE_LINE_FONT_SIZE - 1))));
		}
		moneyText = CellUtils.getSubStringFitsIn("____________________________________________", FadingSunsTheme.getLineFont(),
				FadingSunsTheme.TABLE_LINE_FONT_SIZE, 138 - usedWidth);
		paragraph.add(new Paragraph(moneyText, new Font(FadingSunsTheme.getLineFont(), FadingSunsTheme.TABLE_LINE_FONT_SIZE)));
		return paragraph;
	}
}
