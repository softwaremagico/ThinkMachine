package com.softwaremagico.tm.pdf.complete.others;

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

import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.VerticalTable;

public class OthersTable extends VerticalTable {
	private final static int EMPTY_ROWS = 7;
	private final static float[] WIDTHS = { 1f };

	public OthersTable() {
		super(WIDTHS);
		getDefaultCell().setPaddingRight(20);
		addCell(createTitle(getTranslator().getTranslatedText("othersTable"), FadingSunsTheme.VERTICALTABLE_TITLE_FONT_SIZE));

		for (int i = 0; i < EMPTY_ROWS; i++) {
			addCell(createEmptyElementLine("_______________________________________________"));
		}
	}
}
