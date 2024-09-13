package com.softwaremagico.tm.pdf.complete.skills.occultism;

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

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.occultism.OccultismType;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.pdf.complete.elements.LateralHeaderPdfPTable;

public class OccultismTable extends LateralHeaderPdfPTable {
	private static final float ROW_HEIGHT = 70f;
	private static final float[] widths = { 1f, 6f };

	public OccultismTable(CharacterPlayer characterPlayer, String language, String moduleName)
			throws InvalidXmlElementException {
		super(widths);
		addCell(createLateralVerticalTitle(getTranslator().getTranslatedText("occultism"), 1));
		addCell(createContent(characterPlayer, language, moduleName));
		setWidthPercentage(100);
		getDefaultCell().setPadding(0);
		setSpacingAfter(0);
		setSpacingBefore(0);
	}

	@Override
	protected PdfPCell createLateralVerticalTitle(String title, int rowspan) {
		final PdfPCell titleCell = super.createLateralVerticalTitle(title, rowspan);
		titleCell.setMinimumHeight(ROW_HEIGHT);
		return titleCell;
	}

	private PdfPCell createContent(CharacterPlayer characterPlayer, String language, String moduleName)
			throws InvalidXmlElementException {
		final float[] widths = { 3f, 1f, 1f, 3f };
		final PdfPTable table = new PdfPTable(widths);
		BaseElement.setTablePropierties(table);
		table.getDefaultCell().setBorder(0);
		table.getDefaultCell().setPadding(0);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

		for (final OccultismType occulstimType : OccultismTypeFactory.getInstance().getElements(language, moduleName)) {
			final PdfPCell psiqueTitleCell = new PdfPCell(new Phrase(occulstimType.getName(), new Font(
					FadingSunsTheme.getLineFont(), FadingSunsTheme.CHARACTERISTICS_LINE_FONT_SIZE)));
			psiqueTitleCell.setBorder(0);
			// psiTitleCell.setMinimumHeight(30);
			psiqueTitleCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(psiqueTitleCell);

			if (characterPlayer == null) {
				table.addCell(createRectangle());
				table.addCell(createRectangle());
			} else {
				table.addCell(createRectangle(characterPlayer.getOccultismLevel(occulstimType)));
				table.addCell(createRectangle(characterPlayer.getDarkSideLevel(occulstimType)));
			}

			final PdfPCell darkSideTitleCell = new PdfPCell(new Phrase(occulstimType.getDarkSideName(), new Font(
					FadingSunsTheme.getLineFont(), FadingSunsTheme.CHARACTERISTICS_LINE_FONT_SIZE)));
			darkSideTitleCell.setBorder(0);
			darkSideTitleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(darkSideTitleCell);

		}

		final PdfPCell cell = new PdfPCell();
		cell.addElement(table);
		cell.setPadding(0);
		BaseElement.setCellProperties(cell);

		return cell;
	}

	@Override
	protected int getTitleFontSize() {
		return FadingSunsTheme.OCCULSTISM_TITLE_FONT_SIZE;
	}

}
