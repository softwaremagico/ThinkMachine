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

import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.occultism.OccultismPower;
import com.softwaremagico.tm.character.occultism.OccultismType;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.VerticalTable;

public class OccultismTable extends VerticalTable {
	private final static int POWER_COLUMN_WIDTH = 55;
	private final static int ROLL_COLUMN_WIDTH = 25;
	private final static float[] WIDTHS = { 2f, 1f, 1f, 1f, 2f };
	private final static int ROWS = 6;

	public OccultismTable(CharacterPlayer characterPlayer) {
		super(WIDTHS);
		getDefaultCell().setBorder(0);

		addCell(createTitle(getTranslator().getTranslatedText("occultism"), FadingSunsTheme.CHARACTER_SMALL_OCCULTISM_TITLE_FONT_SIZE));

		addCell(createSubtitleLine(getTranslator().getTranslatedText(OccultismType.PSI.getTag()), FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE,
				Element.ALIGN_LEFT));
		if (characterPlayer != null) {
			addCell(createValueLine("" + characterPlayer.getOccultism().getPsiValue(), FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));
		} else {
			addCell(createValueLine(" ", FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));
		}
		addCell(createSubtitleLine("/", FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));
		if (characterPlayer != null) {
			addCell(createValueLine("" + characterPlayer.getOccultism().getUrge(), FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));
		} else {
			addCell(createValueLine(" ", FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));
		}
		addCell(createSubtitleLine(getTranslator().getTranslatedText(OccultismType.PSI.getDarkSideTag()), FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE,
				Element.ALIGN_RIGHT));

		addCell(createSubtitleLine(getTranslator().getTranslatedText(OccultismType.THEURGY.getTag()), FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE,
				Element.ALIGN_LEFT));
		if (characterPlayer != null) {
			addCell(createValueLine("" + characterPlayer.getOccultism().getTeurgyValue(), FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));
		} else {
			addCell(createValueLine(" ", FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));
		}
		addCell(createSubtitleLine("/", FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE, Element.ALIGN_LEFT));
		if (characterPlayer != null) {
			addCell(createValueLine("" + characterPlayer.getOccultism().getHubris(), FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));
		} else {
			addCell(createValueLine(" ", FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));
		}
		addCell(createSubtitleLine(getTranslator().getTranslatedText(OccultismType.THEURGY.getDarkSideTag()), FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE,
				Element.ALIGN_RIGHT));

		addCell(createSubtitleLine(getTranslator().getTranslatedText("occultismTablePower"), FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE, 4,
				Element.ALIGN_LEFT));
		addCell(createSubtitleLine(getTranslator().getTranslatedText("weaponGoal"), FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));

		int added = 0;
		if (characterPlayer != null) {
			for (OccultismPower occultismPower : characterPlayer.getOccultism().getElements()) {
				if (occultismPower.isEnabled()) {
					PdfPCell cell = createFirstElementLine(occultismPower.getName(), POWER_COLUMN_WIDTH,
							FadingSunsTheme.CHARACTER_SMALL_OCCULTISM_LINE_FONT_SIZE);
					cell.setColspan(WIDTHS.length - 1);
					addCell(cell);
					addCell(createElementLine(occultismPower.getRoll(), ROLL_COLUMN_WIDTH, FadingSunsTheme.CHARACTER_SMALL_OCCULTISM_LINE_FONT_SIZE));
					added++;
				}
			}
		}

		for (int i = added; i < ROWS; i++) {
			for (int j = 0; j < WIDTHS.length; j++) {
				addCell(new Paragraph(" "));
			}
		}
	}
}
