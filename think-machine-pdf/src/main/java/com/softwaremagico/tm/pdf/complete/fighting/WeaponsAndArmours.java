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
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;

public class WeaponsAndArmours extends BaseElement {
	public final static int PADDING = 2;

	public static PdfPTable getWeaponsAndArmoursTable(
			CharacterPlayer characterPlayer) {
		float[] widths = { 4f, 1.1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);
		table.getDefaultCell().setPadding(PADDING);
		table.getDefaultCell().setBorder(0);

		PdfPTable leftTable = new PdfPTable(new float[] { 1f });
		setTablePropierties(leftTable);
		leftTable.getDefaultCell().setBorder(0);
		leftTable.getDefaultCell().setPadding(0);

		PdfPCell fireArmsCell = new PdfPCell(new WeaponsTable(characterPlayer));
		leftTable.addCell(fireArmsCell);

		PdfPTable stancesXpTable = new PdfPTable(new float[] { 4f, 1f });
		setTablePropierties(stancesXpTable);
		stancesXpTable.getDefaultCell().setBorder(0);
		stancesXpTable.getDefaultCell().setPadding(0);
		
		PdfPCell stancesCell = new PdfPCell(new StancesTable(characterPlayer));
		stancesXpTable.addCell(stancesCell);
		
		PdfPCell experienceCell = new PdfPCell(new ExperienceTable(characterPlayer));
		stancesXpTable.addCell(experienceCell);
		
		leftTable.addCell(stancesXpTable);

		PdfPCell leftCell = new PdfPCell(leftTable);
		leftCell.setRowspan(2);
		table.addCell(leftCell);

		PdfPCell armourCell = new PdfPCell(new ArmourTable(characterPlayer));
		table.addCell(armourCell);

		PdfPCell shieldCell = new PdfPCell(new ShieldTable(characterPlayer));
		table.addCell(shieldCell);

		return table;
	}
}
