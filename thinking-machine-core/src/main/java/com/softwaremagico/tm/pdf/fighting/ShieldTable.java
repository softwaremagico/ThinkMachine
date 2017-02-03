package com.softwaremagico.tm.pdf.fighting;

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
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.BaseElement;
import com.softwaremagico.tm.pdf.elements.LateralHeaderPdfPTable;

public class ShieldTable extends LateralHeaderPdfPTable {
	private final static float[] WIDTHS = { 1f, 4f };
	private final static int ROWS = 4;

	public ShieldTable() {
		super(WIDTHS);
		getDefaultCell().setBorder(0);

		addCell(createLateralVerticalTitle(getTranslator().getTranslatedText("shield"), ROWS + 1));

		PdfPCell nameCell = createElementLine("___________________");
		nameCell.setColspan(WIDTHS.length);
		nameCell.setMinimumHeight(20);
		addCell(nameCell);

		addCell(getShieldRange());
		addCell(createElementLine(getTranslator().getTranslatedText("shieldHits")));
		addCell(createElementLine("___________________"));
		addCell(createElementLine("___________________"));
	}

	private PdfPTable getShieldRange() {
		float[] widths = { 1f, 1f, 1f, 1f, 1f };
		PdfPTable table = new PdfPTable(widths);
		BaseElement.setTablePropierties(table);
		table.getDefaultCell().setBorder(0);
		table.getDefaultCell().setPadding(0);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

		table.addCell(createElementLine("("));
		table.addCell(createRectangle());
		table.addCell(createElementLine("/"));
		table.addCell(createRectangle());
		table.addCell(createElementLine(")"));

		return table;
	}

	@Override
	protected int getTitleFontSize() {
		return FadingSunsTheme.ARMOUR_TITLE_FONT_SIZE;
	}

}
