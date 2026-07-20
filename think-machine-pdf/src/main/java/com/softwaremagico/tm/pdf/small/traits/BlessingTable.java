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

import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfPCell;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.blessings.Blessing;
import com.softwaremagico.tm.character.values.Bonification;
import com.softwaremagico.tm.configurator.MachinePdfConfigurationReader;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.VerticalTable;

public class BlessingTable extends VerticalTable {
	private static final String GAP = "_______________________________";
	private static final int BONIFICATION_COLUMN_WIDTH = 15;
	private static final int TRAIT_COLUMN_WIDTH = 50;
	private static final int SITUATION_COLUMN_WIDTH = 100;
	private static final int ROWS = 9;

	private static final float[] WIDTHS = { 2f, 5f, 10f };

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

			boolean addBlessingTitle = false;
			if (MachinePdfConfigurationReader.getInstance().isSmallPdfBlessingNameEnabled()) {
				// Calculate if we can put blessing titles.
				int usedLines = characterPlayer.getAllBlessings().size();
				for (final Blessing blessing : characterPlayer.getAllBlessings()) {
					usedLines += blessing.getBonifications().size();
				}

				addBlessingTitle = (usedLines <= ROWS);
			}

			for (final Blessing blessing : characterPlayer.getAllBlessings()) {
				if (addBlessingTitle) {
					final PdfPCell titleCell = createElementLine(blessing.getName(), BONIFICATION_COLUMN_WIDTH
							+ TRAIT_COLUMN_WIDTH + SITUATION_COLUMN_WIDTH,
							FadingSunsTheme.CHARACTER_SMALL_TRAITS_FONT_SIZE, FadingSunsTheme.getLineFont());
					titleCell.setColspan(WIDTHS.length);
					titleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
					titleCell.setPaddingLeft(16f);
					addCell(titleCell);
					added++;
				}

				final Iterator<Bonification> it = blessing.getBonifications().iterator();
				while (it.hasNext()) {
					final Bonification bonification = it.next();
					final PdfPCell bonificationCell = createElementLine(bonification.getBonification(), BONIFICATION_COLUMN_WIDTH,
							FadingSunsTheme.CHARACTER_SMALL_TRAITS_FONT_SIZE);
					bonificationCell.setPaddingLeft(3f);
					addCell(bonificationCell);
					final PdfPCell nameCell = createElementLine(bonification.getAffects() != null ? bonification
							.getAffects().getName() : "", TRAIT_COLUMN_WIDTH,
							FadingSunsTheme.CHARACTER_SMALL_TRAITS_FONT_SIZE);
					nameCell.setHorizontalAlignment(Element.ALIGN_LEFT);
					addCell(nameCell);
					final PdfPCell descriptionCell = createElementLine(bonification.getSituation(),
							SITUATION_COLUMN_WIDTH, FadingSunsTheme.CHARACTER_SMALL_TRAITS_FONT_SIZE);
					descriptionCell.setHorizontalAlignment(Element.ALIGN_LEFT);
					addCell(descriptionCell);
					added++;
				}
			}
		}

		if (characterPlayer == null) {
			for (int i = added; i < ROWS; i++) {
				addCell(createEmptyElementLine(GAP, BONIFICATION_COLUMN_WIDTH));
				addCell(createEmptyElementLine(GAP, TRAIT_COLUMN_WIDTH));
				addCell(createEmptyElementLine(GAP, SITUATION_COLUMN_WIDTH));
			}
		} else {
			for (int i = added; i < ROWS; i++) {
				for (int j = 0; j < WIDTHS.length; j++) {
					addCell(createEmptyElementLine(FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));
				}
			}
		}
	}
}
