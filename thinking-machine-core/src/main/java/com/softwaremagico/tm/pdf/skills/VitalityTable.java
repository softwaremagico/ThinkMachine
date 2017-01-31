package com.softwaremagico.tm.pdf.skills;

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
import com.itextpdf.text.Font;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.pdf.FadingSunsTheme;

public class VitalityTable extends CounterTable {
	private final static float[] WIDTHS = { 1f, 1f };
	private final static int CIRCLES = 23;
	private final static int TITLE_SPAN = 5;
	private final static int MODIFICATORS_SPAN = 5;
	private static ITranslator translator = null;

	public VitalityTable() {
		super(WIDTHS);
		translator = LanguagePool.getTranslator("character_sheet.xml");
		addCell(createLateralVerticalTitle(translator.getTranslatedText("vitality"), TITLE_SPAN));
		for (int i = 0; i < TITLE_SPAN; i++) {
			addCell(createCircle());
		}
		addCell(space(CIRCLES - TITLE_SPAN - MODIFICATORS_SPAN));
		for (int i = 0; i < CIRCLES - TITLE_SPAN - MODIFICATORS_SPAN; i++) {
			addCell(createCircle());
		}

		for (int i = 1; i <= MODIFICATORS_SPAN; i++) {
			addCell(createValue("-" + (i * 2), new Font(FadingSunsTheme.getLineFont(), FadingSunsTheme.CHARACTERISTICS_TITLE_FONT_SIZE - 6),
					Element.ALIGN_MIDDLE));
			addCell(createCircle());
		}
	}

	@Override
	protected int getTitleFontSize() {
		return FadingSunsTheme.CHARACTERISTICS_TITLE_FONT_SIZE;
	}

}
