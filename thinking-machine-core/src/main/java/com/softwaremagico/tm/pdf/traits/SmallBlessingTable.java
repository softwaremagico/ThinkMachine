package com.softwaremagico.tm.pdf.traits;

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

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.traits.Blessing;
import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.VerticalTable;

public class SmallBlessingTable extends VerticalTable {
	private final static int BONIFICATION_COLUMN_WIDTH = 15;
	private final static int TRAIT_COLUMN_WIDTH = 42;
	private final static int SITUATION_COLUMN_WIDTH = 80;

	private final static float[] WIDTHS = { 2f, 5f, 10f };

	public SmallBlessingTable(CharacterPlayer characterPlayer) {
		super(WIDTHS);
		addCell(createTitle(getTranslator().getTranslatedText("blessingTable"), FadingSunsTheme.CHARACTER_SMALL_BLESSING_TITLE_FONT_SIZE));

		addCell(createSubtitleLine("+/-", FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));
		addCell(createSubtitleLine(getTranslator().getTranslatedText("blessingTableTrait"), FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));
		addCell(createSubtitleLine(getTranslator().getTranslatedText("blessingTableSituation"), FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));

		if (characterPlayer != null) {
			for (Blessing blessing : characterPlayer.getBlessings()) {
				addCell(createElementLine(blessing.getBonification(), BONIFICATION_COLUMN_WIDTH, FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));
				addCell(createElementLine(blessing.getTrait(), TRAIT_COLUMN_WIDTH, FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));
				addCell(createElementLine(blessing.getSituation(), SITUATION_COLUMN_WIDTH, FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));
			}
		}
	}
}
