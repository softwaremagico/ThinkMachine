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

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.skills.Skill;
import com.softwaremagico.tm.character.skills.SkillFactory;
import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.BaseElement;
import com.softwaremagico.tm.pdf.skills.occultism.OccultismTable;

public class SkillsTable extends BaseElement {
	private final static int ROWS = 30;
	private final static int TITLE_ROWSPAN = 2;
	private static int learnedSkillsAdded = 0;
	private final static String GAP = "____";
	private final static int OCCULTISM_ROWS = 5;

	public static PdfPTable getSkillsTable(String language) {
		float[] widths = { 1f, 1f, 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);
		learnedSkillsAdded = 0;
		table.addCell(getFirstColumnTable(language));
		table.addCell(getSecondColumnTable(language));
		table.addCell(getThirdColumnTable(language));
		return table;
	}

	private static PdfPCell getFirstColumnTable(String language) {
		float[] widths = { 4f, 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);

		table.addCell(createTitle(getTranslator().getTranslatedText("naturalSkills")));
		for (Skill skill : SkillFactory.getNaturalSkills(language)) {
			table.addCell(createSkillElement(skill));
			table.addCell(createSkillLine(GAP));
		}

		table.addCell(createTitle(getTranslator().getTranslatedText("learnedSkills")));
		for (int i = 0; i < Math.min(SkillFactory.getLearnedSkills(language).size(), ROWS - (2 * TITLE_ROWSPAN)
				- SkillFactory.getNaturalSkills(language).size()); i++) {
			table.addCell(createSkillElement(SkillFactory.getLearnedSkills(language).get(i)));
			table.addCell(createSkillLine(GAP));
			learnedSkillsAdded++;
		}

		PdfPCell cell = new PdfPCell();
		setCellProperties(cell);

		cell.addElement(table);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		return cell;
	}

	private static PdfPCell getSecondColumnTable(String language) {
		float[] widths = { 4f, 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);
		PdfPCell cell = new PdfPCell();
		setCellProperties(cell);

		int maxElements = Math.min(SkillFactory.getLearnedSkills(language).size(), ROWS + learnedSkillsAdded);

		for (int i = learnedSkillsAdded; i < maxElements; i++) {
			table.addCell(createSkillElement(SkillFactory.getLearnedSkills(language).get(i)));
			table.addCell(createSkillLine(GAP));
			learnedSkillsAdded++;
		}

		cell.addElement(table);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		return cell;
	}

	private static PdfPCell getThirdColumnTable(String language) {
		float[] widths = { 4f, 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);
		PdfPCell cell = new PdfPCell();
		setCellProperties(cell);

		int addedElements = 0;
		int maxElements = Math.min(SkillFactory.getLearnedSkills(language).size(), ROWS + learnedSkillsAdded);

		for (int i = learnedSkillsAdded; i < maxElements; i++) {
			table.addCell(createSkillElement(SkillFactory.getLearnedSkills(language).get(i)));
			table.addCell(createSkillLine(GAP));
			learnedSkillsAdded++;
			addedElements++;
		}

		// Complete with empty skills.
		for (int i = addedElements; i < ROWS - OCCULTISM_ROWS; i++) {
			table.addCell(createSkillLine("_____________________"));
			table.addCell(createSkillLine(GAP));
		}

		// Add Occultism table
		PdfPTable occultismTable = new OccultismTable();
		PdfPCell occulstimCell = new PdfPCell();
		// setCellProperties(occulstimCell);
		// occulstimCell.setRowspan(widths.length);
		occulstimCell.setRowspan(OCCULTISM_ROWS);
		occulstimCell.setColspan(2);
		occulstimCell.addElement(occultismTable);
		occulstimCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		occulstimCell.setPadding(0);
		table.addCell(occulstimCell);

		cell.addElement(table);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		return cell;
	}

	private static PdfPCell createTitle(String text) {
		PdfPCell cell = getCell(text, 0, 2, Element.ALIGN_CENTER, BaseColor.WHITE, FadingSunsTheme.getTitleFont(), FadingSunsTheme.SKILLS_TITLE_FONT_SIZE);
		cell.setMinimumHeight(MainSkillsTableFactory.HEIGHT / (ROWS / TITLE_ROWSPAN) + 1);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

	private static PdfPCell createSkillElement(Skill skill) {
		PdfPCell cell = getCell(skill.getName(), 0, 1, Element.ALIGN_LEFT, BaseColor.WHITE, skill.isFromGuild() ? FadingSunsTheme.getLineItalicFont()
				: FadingSunsTheme.getLineFont(), FadingSunsTheme.SKILLS_LINE_FONT_SIZE);
		cell.setMinimumHeight((MainSkillsTableFactory.HEIGHT / ROWS));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

	private static PdfPCell createSkillLine(String text) {
		PdfPCell cell = getCell(text, 0, 1, Element.ALIGN_LEFT, BaseColor.WHITE, FadingSunsTheme.getLineFont(), FadingSunsTheme.SKILLS_LINE_FONT_SIZE);
		cell.setMinimumHeight((MainSkillsTableFactory.HEIGHT / ROWS));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

}
