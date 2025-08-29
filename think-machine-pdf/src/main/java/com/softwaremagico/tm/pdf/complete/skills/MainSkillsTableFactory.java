package com.softwaremagico.tm.pdf.complete.skills;

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

import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;

public class MainSkillsTableFactory extends BaseElement {
	public static final int HEIGHT = 400;
	public static final int PADDING = 2;

	public static PdfPTable getSkillsTable(CharacterPlayer characterPlayer, String language, String moduleName)
			throws InvalidXmlElementException {
		final float[] widths = { 1f, 12f, 1f };
		final PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);

		final PdfPCell separator = createSeparator();
		separator.setPadding(PADDING);
		table.addCell(separator);
		table.addCell(separator);
		table.addCell(separator);

		final PdfPCell vitalityCell = new PdfPCell(new VitalityTable(characterPlayer));
		vitalityCell.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT | Rectangle.TOP | Rectangle.LEFT);
		vitalityCell.setPadding(0);
		table.addCell(vitalityCell);

		final PdfPCell skillsCell = new PdfPCell(CompleteSkillsTable.getSkillsTable(characterPlayer, language,
				moduleName));
		skillsCell.setBorder(0);
		skillsCell.setPadding(0);
		skillsCell.setPaddingRight(FadingSunsTheme.DEFAULT_MARGIN);
		skillsCell.setPaddingLeft(FadingSunsTheme.DEFAULT_MARGIN);
		table.addCell(skillsCell);

		final PdfPCell wyrdCell = new PdfPCell(new WyrdTable(characterPlayer));
		wyrdCell.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT | Rectangle.TOP | Rectangle.LEFT);
		wyrdCell.setPadding(0);
		table.addCell(wyrdCell);
		return table;
	}

}
