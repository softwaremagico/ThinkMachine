package com.softwaremagico.tm.pdf.complete.fighting;

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

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.rules.InvalidXmlElementException;
import com.softwaremagico.tm.rules.character.CharacterPlayer;

public class FightingManeuvers extends BaseElement {
	public static final int PADDING = 2;

	public static PdfPTable getFightingManoeuvresTable(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
		final float[] widths = { 1f, 1f };
		final PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);
		table.getDefaultCell().setPadding(PADDING);
		table.getDefaultCell().setBorder(0);

		table.addCell(BaseElement.createWhiteSeparator());
		table.addCell(BaseElement.createWhiteSeparator());

		table.addCell(BaseElement.createWhiteSeparator());
		table.addCell(BaseElement.createWhiteSeparator());

		final PdfPCell fireArmsCell = new PdfPCell(new RangedManeuversTable(characterPlayer));
		table.addCell(fireArmsCell);

		final PdfPCell fencingCell = new PdfPCell(new MeleeManeuversTable(characterPlayer));
		table.addCell(fencingCell);

		return table;
	}

}
