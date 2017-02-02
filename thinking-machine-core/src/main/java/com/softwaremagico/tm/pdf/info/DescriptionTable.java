package com.softwaremagico.tm.pdf.info;

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

import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.pdf.elements.VerticalTable;

public class DescriptionTable extends VerticalTable {
	private final static float[] WIDTHS = { 1f };
	private static ITranslator translator = null;

	public DescriptionTable() {
		super(WIDTHS);
		translator = LanguagePool.getTranslator("character_sheet.xml");

		addCell(createTitle(translator.getTranslatedText("description")));
		addCell(createElementLine(translator.getTranslatedText("descriptionBirthdate")));
		addCell(createElementLine(translator.getTranslatedText("descriptionHair")));
		addCell(createElementLine(translator.getTranslatedText("descriptionEyes")));
		addCell(createElementLine(translator.getTranslatedText("descriptionComplexion")));
		addCell(createElementLine(translator.getTranslatedText("descriptionHeight")));
		addCell(createElementLine(translator.getTranslatedText("descriptionWeight")));
		addCell(createElementLine(translator.getTranslatedText("descriptionAppearance")));
		addCell(createElementLine("______________________________________________"));
		addCell(createElementLine("______________________________________________"));
		addCell(createElementLine("______________________________________________"));
	}
}
