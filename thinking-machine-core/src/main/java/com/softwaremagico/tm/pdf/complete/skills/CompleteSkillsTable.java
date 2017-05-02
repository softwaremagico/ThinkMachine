package com.softwaremagico.tm.pdf.complete.skills;

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
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.SkillFactory;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.skills.occultism.OccultismTable;

public class CompleteSkillsTable extends SkillsTable {
	private final static int ROWS = 30;
	private static int learnedSkillsAdded = 0;
	private final static String SKILL_VALUE_GAP = "____";
	private final static int OCCULTISM_ROWS = 5;

	public static PdfPTable getSkillsTable(CharacterPlayer characterPlayer, String language) {
		float[] widths = { 1f, 1f, 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);
		learnedSkillsAdded = 0;
		table.addCell(getFirstColumnTable(characterPlayer, language));
		table.addCell(getSecondColumnTable(characterPlayer, language));
		table.addCell(getThirdColumnTable(characterPlayer, language));
		return table;
	}

	private static PdfPCell getFirstColumnTable(CharacterPlayer characterPlayer, String language) {
		float[] widths = { 4f, 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);

		table.addCell(createTitle(getTranslator().getTranslatedText("naturalSkills"), FadingSunsTheme.SKILLS_TITLE_FONT_SIZE));

		if (characterPlayer == null) {
			for (AvailableSkill skill : SkillFactory.getNaturalSkills(language)) {
				table.addCell(createSkillElement(characterPlayer, skill, FadingSunsTheme.SKILLS_LINE_FONT_SIZE));
				table.addCell(createSkillLine(SKILL_VALUE_GAP, FadingSunsTheme.SKILLS_LINE_FONT_SIZE));
			}
		} else {
			for (AvailableSkill skill : characterPlayer.getNaturalSkills()) {
				table.addCell(createSkillElement(characterPlayer, skill, FadingSunsTheme.SKILLS_LINE_FONT_SIZE));
				table.addCell(createSkillValue(characterPlayer.getSkillValue(skill), characterPlayer.isSkillSpecial(skill),
						FadingSunsTheme.SKILLS_LINE_FONT_SIZE));
			}
		}

		table.addCell(createTitle(getTranslator().getTranslatedText("learnedSkills"), FadingSunsTheme.SKILLS_TITLE_FONT_SIZE));
		for (int i = 0; i < Math.min(SkillFactory.getLearnedSkills(language).size(), ROWS - (2 * TITLE_ROWSPAN)
				- SkillFactory.getNaturalSkills(language).size()); i++) {
			table.addCell(createSkillElement(characterPlayer, SkillFactory.getLearnedSkills(language).get(i), FadingSunsTheme.SKILLS_LINE_FONT_SIZE));
			if (characterPlayer == null) {
				table.addCell(createSkillLine(SKILL_VALUE_GAP, FadingSunsTheme.SKILLS_LINE_FONT_SIZE));
			} else {
				table.addCell(createSkillValue(characterPlayer.getSkillValue(SkillFactory.getLearnedSkills(language).get(i)),
						characterPlayer.isSkillSpecial(SkillFactory.getLearnedSkills(language).get(i)), FadingSunsTheme.SKILLS_LINE_FONT_SIZE));
			}
			learnedSkillsAdded++;
		}

		PdfPCell cell = new PdfPCell();
		setCellProperties(cell);

		cell.addElement(table);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		return cell;
	}

	private static PdfPCell getSecondColumnTable(CharacterPlayer characterPlayer, String language) {
		float[] widths = { 4f, 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);
		PdfPCell cell = new PdfPCell();
		setCellProperties(cell);

		int maxElements = Math.min(SkillFactory.getLearnedSkills(language).size(), ROWS + learnedSkillsAdded);

		for (int i = learnedSkillsAdded; i < maxElements; i++) {
			table.addCell(createSkillElement(characterPlayer, SkillFactory.getLearnedSkills(language).get(i), FadingSunsTheme.SKILLS_LINE_FONT_SIZE));
			if (characterPlayer == null) {
				table.addCell(createSkillLine(SKILL_VALUE_GAP, FadingSunsTheme.SKILLS_LINE_FONT_SIZE));
			} else {
				table.addCell(createSkillValue(characterPlayer.getSkillValue(SkillFactory.getLearnedSkills(language).get(i)),
						characterPlayer.isSkillSpecial(SkillFactory.getLearnedSkills(language).get(i)), FadingSunsTheme.SKILLS_LINE_FONT_SIZE));
			}
			learnedSkillsAdded++;
		}

		cell.addElement(table);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		return cell;
	}

	private static PdfPCell getThirdColumnTable(CharacterPlayer characterPlayer, String language) {
		float[] widths = { 4f, 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);
		PdfPCell cell = new PdfPCell();
		setCellProperties(cell);

		int addedElements = 0;
		int maxElements = Math.min(SkillFactory.getLearnedSkills(language).size(), ROWS + learnedSkillsAdded);

		for (int i = learnedSkillsAdded; i < maxElements; i++) {
			table.addCell(createSkillElement(characterPlayer, SkillFactory.getLearnedSkills(language).get(i), FadingSunsTheme.SKILLS_LINE_FONT_SIZE));
			if (characterPlayer == null) {
				table.addCell(createSkillLine(SKILL_VALUE_GAP, FadingSunsTheme.SKILLS_LINE_FONT_SIZE));
			} else {
				table.addCell(createSkillValue(characterPlayer.getSkillValue(SkillFactory.getLearnedSkills(language).get(i)),
						characterPlayer.isSkillSpecial(SkillFactory.getLearnedSkills(language).get(i)), FadingSunsTheme.SKILLS_LINE_FONT_SIZE));
			}
			learnedSkillsAdded++;
			addedElements++;
		}

		// Complete with empty skills.
		for (int i = addedElements; i < ROWS - OCCULTISM_ROWS; i++) {
			table.addCell(createSkillLine("________________________", FadingSunsTheme.SKILLS_LINE_FONT_SIZE));
			table.addCell(createSkillLine(SKILL_VALUE_GAP, FadingSunsTheme.SKILLS_LINE_FONT_SIZE));
		}

		// Add Occultism table
		PdfPTable occultismTable = new OccultismTable(characterPlayer);
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
}
