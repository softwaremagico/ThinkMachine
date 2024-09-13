package com.softwaremagico.tm.pdf.small.cybernetics;

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

import com.lowagie.text.Element;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.cybernetics.SelectedCyberneticDevice;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.VerticalTable;

public class CyberneticsTable extends VerticalTable {
	private static final String GAP = "__________________";
	private static final int NAME_COLUMN_WIDTH = 95;
	private static final int POINTS_COLUMN_WIDTH = 25;
	private static final float[] WIDTHS = { 5f, 2f };
	private static final int ROWS = 9;

	public CyberneticsTable(CharacterPlayer characterPlayer) {
		super(WIDTHS);
		getDefaultCell().setBorder(0);

		addCell(createTitle(getTranslator().getTranslatedText("cybernetics"),
				FadingSunsTheme.CHARACTER_SMALL_CYBERNETICS_TITLE_FONT_SIZE));

		addCell(createSubtitleLine(getTranslator().getTranslatedText("cyberneticsName"),
				FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE, Element.ALIGN_LEFT));
		addCell(createSubtitleLine(getTranslator().getTranslatedText("cyberneticsPoints"),
				FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));

		int added = 0;
		if (characterPlayer != null) {
			for (final SelectedCyberneticDevice device : characterPlayer.getCybernetics()) {
				addCell(createFirstElementLine(device.getName(), NAME_COLUMN_WIDTH,
						FadingSunsTheme.CYBERNETICS_CONTENT_FONT_SIZE));
				addCell(createElementLine(device.getPoints() + "", POINTS_COLUMN_WIDTH,
						FadingSunsTheme.CYBERNETICS_CONTENT_FONT_SIZE));
				added++;
			}
		}

		if (characterPlayer == null) {
			for (int i = added; i < ROWS; i++) {
				addCell(createEmptyElementLine(GAP, NAME_COLUMN_WIDTH));
				addCell(createEmptyElementLine(GAP, POINTS_COLUMN_WIDTH));
			}
		} else {
			for (int i = added; i < ROWS; i++) {
				for (int j = 0; j < WIDTHS.length; j++) {
					addCell(createEmptyElementLine(FadingSunsTheme.CYBERNETICS_CONTENT_FONT_SIZE));
				}
			}
		}
	}
}
