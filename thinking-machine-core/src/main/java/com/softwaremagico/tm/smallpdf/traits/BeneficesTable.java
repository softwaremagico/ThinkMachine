package com.softwaremagico.tm.smallpdf.traits;

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

import com.itextpdf.text.Paragraph;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.traits.Benefit;
import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.VerticalTable;

public class BeneficesTable extends VerticalTable {
	private final static int TRAIT_COLUMN_WIDTH = 60;
	private final static float[] WIDTHS = { 1f };
	private final static int ROWS = 8;

	public BeneficesTable(CharacterPlayer characterPlayer) {
		super(WIDTHS);
		getDefaultCell().setBorder(0);

		addCell(createTitle(getTranslator().getTranslatedText("beneficesTable"), FadingSunsTheme.CHARACTER_SMALL_BENEFICES_TITLE_FONT_SIZE));

		int added = 0;
		if (characterPlayer != null) {
			for (Benefit benefit : characterPlayer.getBenefits()) {
				addCell(createElementLine(benefit.getName(), TRAIT_COLUMN_WIDTH, FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));
				added++;
			}
		}

		for (int i = added; i < ROWS; i++) {
			addCell(new Paragraph(""));
		}
	}

}
