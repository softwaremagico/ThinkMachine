package com.softwaremagico.tm.pdf.small.traits;

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

import java.util.Iterator;

import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.blessings.Bonification;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.VerticalTable;

public class BlessingTable extends VerticalTable {
	private final static int BONIFICATION_COLUMN_WIDTH = 15;
	private final static int TRAIT_COLUMN_WIDTH = 60;
	private final static int SITUATION_COLUMN_WIDTH = 78;
	private final static int ROWS = 9;

	private final static float[] WIDTHS = { 2f, 7f, 8f };

	public BlessingTable(CharacterPlayer characterPlayer) {
		super(WIDTHS);
		getDefaultCell().setBorder(0);

		addCell(createTitle(getTranslator().getTranslatedText("blessingTable"),
				FadingSunsTheme.CHARACTER_SMALL_BLESSING_TITLE_FONT_SIZE));

		addCell(createSubtitleLine("+/-", FadingSunsTheme.CHARACTER_SMALL_TRAITS_FONT_SIZE));
		addCell(createSubtitleLine(getTranslator().getTranslatedText("blessingTableTrait"),
				FadingSunsTheme.CHARACTER_SMALL_TRAITS_FONT_SIZE));
		addCell(createSubtitleLine(getTranslator().getTranslatedText("blessingTableSituation"),
				FadingSunsTheme.CHARACTER_SMALL_TRAITS_FONT_SIZE));

		int added = 0;
		if (characterPlayer != null) {
			for (Blessing blessing : characterPlayer.getAllBlessings()) {
				Iterator<Bonification> it = blessing.getBonifications().iterator();
				while (it.hasNext()) {
					Bonification bonification = it.next();
					addCell(createElementLine(bonification.getBonification(), BONIFICATION_COLUMN_WIDTH,
							FadingSunsTheme.CHARACTER_SMALL_TRAITS_FONT_SIZE));
					PdfPCell nameCell = createElementLine(bonification.getAffects() != null ? bonification.getAffects()
							.getName() : "", TRAIT_COLUMN_WIDTH, FadingSunsTheme.CHARACTER_SMALL_TRAITS_FONT_SIZE);
					nameCell.setHorizontalAlignment(Element.ALIGN_LEFT);
					addCell(nameCell);
					PdfPCell descriptionCell = createElementLine(bonification.getSituation(), SITUATION_COLUMN_WIDTH,
							FadingSunsTheme.CHARACTER_SMALL_TRAITS_FONT_SIZE);
					descriptionCell.setHorizontalAlignment(Element.ALIGN_LEFT);
					addCell(descriptionCell);
					added++;
				}
			}
		}

		for (int i = added; i < ROWS; i++) {
			for (int j = 0; j < WIDTHS.length; j++) {
				addCell(new Paragraph(""));
			}
		}
	}
}
