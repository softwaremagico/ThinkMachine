package com.softwaremagico.tm.smallpdf.occultism;

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

import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.VerticalTable;

public class OccultismTable extends VerticalTable {
	private final static int TRAIT_COLUMN_WIDTH = 55;
	private final static float[] WIDTHS = { 5f, 1f };
	private final static int ROWS = 10;

	public OccultismTable(CharacterPlayer characterPlayer) {
		super(WIDTHS);
		getDefaultCell().setBorder(0);

		addCell(createTitle(getTranslator().getTranslatedText("occultism"), FadingSunsTheme.CHARACTER_SMALL_OCCULTISM_TITLE_FONT_SIZE));
		addCell(createSubtitleLine(getTranslator().getTranslatedText("occultismTablePower"), FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE,
				Element.ALIGN_LEFT));
		addCell(createSubtitleLine(getTranslator().getTranslatedText("weaponGoal"), FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));

		int added = 0;
		if (characterPlayer != null) {

		}

		for (int i = added; i < ROWS; i++) {
			addCell(new Paragraph(" "));
		}
	}
}
