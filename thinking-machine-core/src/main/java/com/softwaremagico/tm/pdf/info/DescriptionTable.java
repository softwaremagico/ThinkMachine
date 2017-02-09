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

import com.softwaremagico.tm.pdf.elements.VerticalTable;

public class DescriptionTable extends VerticalTable {
	private final static float[] WIDTHS = { 1f };

	public DescriptionTable() {
		super(WIDTHS);

		addCell(createTitle(getTranslator().getTranslatedText("description")));
		addCell(createEmptyElementLine(getTranslator().getTranslatedText("descriptionBirthdate")));
		addCell(createEmptyElementLine(getTranslator().getTranslatedText("descriptionHair")));
		addCell(createEmptyElementLine(getTranslator().getTranslatedText("descriptionEyes")));
		addCell(createEmptyElementLine(getTranslator().getTranslatedText("descriptionComplexion")));
		addCell(createEmptyElementLine(getTranslator().getTranslatedText("descriptionHeight")));
		addCell(createEmptyElementLine(getTranslator().getTranslatedText("descriptionWeight")));
		addCell(createEmptyElementLine(getTranslator().getTranslatedText("descriptionAppearance")));
		addCell(createEmptyElementLine("______________________________________________"));
		addCell(createEmptyElementLine("______________________________________________"));
		addCell(createEmptyElementLine("______________________________________________"));
	}
}
