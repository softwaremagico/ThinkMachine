package com.softwaremagico.tm.pdf.complete.traits;

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
import com.softwaremagico.tm.rules.character.CharacterPlayer;

public class MainPerksTableFactory extends BaseElement {
	protected static final int EMPTY_ROWS = 8;
	public static final int PADDING = 2;

	public static PdfPTable getPerksTable(CharacterPlayer characterPlayer) {
		final float[] widths = { 4f, 0.1f, 4f, 0.1f, 1f };
		final PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);
		table.getDefaultCell().setPadding(PADDING);

		final PdfPCell whiteSeparator = createWhiteSeparator();
		whiteSeparator.setColspan(widths.length);
		table.addCell(whiteSeparator);

		final PdfPCell blackSeparator = createBlackSeparator();
		whiteSeparator.setColspan(1);
		table.addCell(blackSeparator);
		table.addCell(whiteSeparator);
		table.addCell(blackSeparator);
		table.addCell(whiteSeparator);

		final PdfPCell victoryPointsCell = new PdfPCell(new VictoryPointsTable());
		victoryPointsCell.setPadding(0);
		victoryPointsCell.setRowspan(2);
		table.addCell(victoryPointsCell);

		final PdfPCell blessingCell = new PdfPCell(new BlessingTable(characterPlayer));
		blessingCell.setPadding(0);
		blessingCell.setBorder(0);
		table.addCell(blessingCell);

		table.addCell(whiteSeparator);

		final PdfPCell perksCell = new PdfPCell(new BeneficesTable(characterPlayer));
		perksCell.setPadding(0);
		perksCell.setBorder(0);
		table.addCell(perksCell);

		table.addCell(whiteSeparator);

		return table;
	}

}
