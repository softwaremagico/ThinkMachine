package com.softwaremagico.tm.pdf.small.occultism;

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
import java.util.Map.Entry;

import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfPCell;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.occultism.OccultismPower;
import com.softwaremagico.tm.character.occultism.OccultismType;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.VerticalTable;

public class OccultismTable extends VerticalTable {
	private static final String GAP = "_______________________";
	private static final int POWER_COLUMN_WIDTH = 80;
	private static final int ROLL_COLUMN_WIDTH = 30;
	private static final float[] WIDTHS = { 2f, 1f, 1f, 1f, 2f };
	private static final int ROWS = 8;

	public OccultismTable(CharacterPlayer characterPlayer, String language, String moduleName)
			throws InvalidXmlElementException {
		super(WIDTHS);
		getDefaultCell().setBorder(0);

		addCell(createTitle(getTranslator().getTranslatedText("occultism"),
				FadingSunsTheme.CHARACTER_SMALL_OCCULTISM_TITLE_FONT_SIZE));

		for (final OccultismType occultismType : OccultismTypeFactory.getInstance().getElements(language, moduleName)) {
			addCell(createSubtitleLine(occultismType.getName(), FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE,
					Element.ALIGN_LEFT));

			if (characterPlayer != null) {
				addCell(createValueLine("" + characterPlayer.getOccultismLevel(occultismType),
						FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));
			} else {
				addCell(createValueLine(" ", FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));
			}

			addCell(createSubtitleLine("/", FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));
			if (characterPlayer != null) {
				addCell(createValueLine("" + characterPlayer.getDarkSideLevel(occultismType),
						FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));
			} else {
				addCell(createValueLine(" ", FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));
			}

			addCell(createSubtitleLine(occultismType.getDarkSideName(),
					FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE, Element.ALIGN_RIGHT));

		}

		addCell(createSubtitleLine(getTranslator().getTranslatedText("occultismTablePower"),
				FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE, 4, Element.ALIGN_LEFT));
		addCell(createSubtitleLine(getTranslator().getTranslatedText("weaponGoal"),
				FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE, Element.ALIGN_RIGHT));

		int added = 0;
		if (characterPlayer != null) {
			for (final Entry<String, List<OccultismPower>> occultismPathEntry : characterPlayer.getSelectedPowers()
					.entrySet()) {
				for (final OccultismPower occultismPower : occultismPathEntry.getValue()) {
					if (occultismPower.isEnabled()) {
						final PdfPCell cell = createFirstElementLine(occultismPower.getName(), POWER_COLUMN_WIDTH,
								FadingSunsTheme.CHARACTER_SMALL_OCCULTISM_LINE_FONT_SIZE);
						cell.setColspan(WIDTHS.length - 1);
						addCell(cell);
						addCell(createElementLine(occultismPower.getRoll(), ROLL_COLUMN_WIDTH,
								FadingSunsTheme.CHARACTER_SMALL_OCCULTISM_LINE_FONT_SIZE));
						added++;
					}
					if (added >= ROWS) {
						break;
					}
				}
			}
		}

		if (characterPlayer == null) {
			for (int i = added; i < ROWS; i++) {
				final PdfPCell cell = createEmptyElementLine(GAP, POWER_COLUMN_WIDTH);
				cell.setColspan(WIDTHS.length - 1);
				addCell(cell);
				addCell(createEmptyElementLine(GAP, ROLL_COLUMN_WIDTH));
			}
		} else {
			for (int i = added; i < ROWS; i++) {
				for (int j = 0; j < WIDTHS.length; j++) {
					addCell(createEmptyElementLine(FadingSunsTheme.CHARACTER_SMALL_OCCULTISM_LINE_FONT_SIZE));
				}
			}
		}
	}
}
