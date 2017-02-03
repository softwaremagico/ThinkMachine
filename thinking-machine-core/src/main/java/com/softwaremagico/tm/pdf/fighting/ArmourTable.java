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

public class ArmourTable extends LateralHeaderPdfPTable {
	private final static float[] WIDTHS = { 0.75f, 1f, 1f, 1f };
	private final static int ROWS = 5;

	public ArmourTable() {
		super(WIDTHS);
		getDefaultCell().setBorder(0);

		addCell(createLateralVerticalTitle(getTranslator().getTranslatedText("armor"), ROWS + 1));

		PdfPCell nameCell = createElementLine("___________________");
		nameCell.setColspan(WIDTHS.length);
		nameCell.setMinimumHeight(20);
		addCell(nameCell);
		PdfPCell protectionCell = createElementLine(getTranslator().getTranslatedText("armorRating") + ": ____ "
				+ getTranslator().getTranslatedText("diceAbbreviature"));
		protectionCell.setColspan(WIDTHS.length);
		addCell(protectionCell);

		PdfPCell malusCell = createElementLine(getTranslator().getTranslatedText("strengthAbbreviature") + ": __  "
				+ getTranslator().getTranslatedText("dexterityAbbreviature") + ": __  "
				+ getTranslator().getTranslatedText("iniciativeAbbreviature") + ": __");
		malusCell.setColspan(WIDTHS.length);
		addCell(malusCell);

		addCell(getArmourProperty(getTranslator().getTranslatedText("armorHardAbbreviature")));
		addCell(getArmourProperty(getTranslator().getTranslatedText("armorFireAbbreviature")));
		addCell(getArmourProperty(getTranslator().getTranslatedText("armorLaserAbbreviature")));
		addCell(getArmourProperty(getTranslator().getTranslatedText("armorPlasmAbbreviature")));
		addCell(getArmourProperty(getTranslator().getTranslatedText("armorShockAbbreviature")));
		addCell(getArmourProperty(getTranslator().getTranslatedText("armorElectricAbbreviature")));

	}

	private PdfPTable getArmourProperty(String text) {
		float[] widths = { 1f, 1f };
		PdfPTable table = new PdfPTable(widths);
		BaseElement.setTablePropierties(table);
		table.getDefaultCell().setBorder(0);
		table.getDefaultCell().setPadding(0);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

		table.addCell(createRectangle());
		table.addCell(createElementLine(text));

		return table;
	}

	@Override
	protected int getTitleFontSize() {
		return FadingSunsTheme.ARMOUR_TITLE_FONT_SIZE;
	}

}
