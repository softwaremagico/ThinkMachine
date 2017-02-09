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

import java.util.Arrays;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.skills.AvailableSkill;
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
	private final static int MAX_SKILL_COLUMN_WIDTH = 115;
	private final static String DEFAULT_NATURAL_SKILL_VALUE = " (3)";

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

		table.addCell(createTitle(getTranslator().getTranslatedText("naturalSkills")));
		for (AvailableSkill skill : SkillFactory.getNaturalSkills(language)) {
			table.addCell(createSkillElement(characterPlayer, skill));
			if (characterPlayer == null) {
				table.addCell(createSkillLine(GAP));
			} else {
				table.addCell(createSkillValue(characterPlayer.getSkillValue(skill.getName())));
			}
		}

		table.addCell(createTitle(getTranslator().getTranslatedText("learnedSkills")));
		for (int i = 0; i < Math.min(SkillFactory.getLearnedSkills(language).size(), ROWS - (2 * TITLE_ROWSPAN)
				- SkillFactory.getNaturalSkills(language).size()); i++) {
			table.addCell(createSkillElement(characterPlayer, SkillFactory.getLearnedSkills(language).get(i)));
			if (characterPlayer == null) {
				table.addCell(createSkillLine(GAP));
			} else {
				table.addCell(createSkillValue(characterPlayer.getSkillValue(SkillFactory.getLearnedSkills(language).get(i).getName())));
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
			table.addCell(createSkillElement(characterPlayer, SkillFactory.getLearnedSkills(language).get(i)));
			if (characterPlayer == null) {
				table.addCell(createSkillLine(GAP));
			} else {
				table.addCell(createSkillValue(characterPlayer.getSkillValue(SkillFactory.getLearnedSkills(language).get(i).getName())));
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
			table.addCell(createSkillElement(characterPlayer, SkillFactory.getLearnedSkills(language).get(i)));
			if (characterPlayer == null) {
				table.addCell(createSkillLine(GAP));
			} else {
				table.addCell(createSkillValue(characterPlayer.getSkillValue(SkillFactory.getLearnedSkills(language).get(i).getName())));
			}
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

	private static PdfPCell createSkillElement(CharacterPlayer characterPlayer, AvailableSkill skill) {
		PdfPCell cell = getCell(skill.getName() + createSkillSufix(characterPlayer, skill), 0, 1, Element.ALIGN_LEFT, BaseColor.WHITE,
				skill.isFromGuild() ? FadingSunsTheme.getLineItalicFont() : FadingSunsTheme.getLineFont(), FadingSunsTheme.SKILLS_LINE_FONT_SIZE);
		cell.setMinimumHeight((MainSkillsTableFactory.HEIGHT / ROWS));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

	private static PdfPCell createSkillLine(String text) {
		PdfPCell cell = getCell(text, 0, 1, Element.ALIGN_CENTER, BaseColor.WHITE, FadingSunsTheme.getLineFont(), FadingSunsTheme.SKILLS_LINE_FONT_SIZE);
		cell.setMinimumHeight((MainSkillsTableFactory.HEIGHT / ROWS));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

	private static PdfPCell createSkillValue(Integer value) {
		if (value == null) {
			return createSkillLine(GAP);
		}
		PdfPCell cell = getCell(value + "", 0, 1, Element.ALIGN_CENTER, BaseColor.WHITE, FadingSunsTheme.getHandwrittingFont(),
				FadingSunsTheme.getHandWrittingFontSize(FadingSunsTheme.SKILLS_LINE_FONT_SIZE));
		cell.setMinimumHeight((MainSkillsTableFactory.HEIGHT / ROWS));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

	private static String createSkillSufix(CharacterPlayer characterPlayer, AvailableSkill skill) {
		StringBuilder sufix = new StringBuilder();
		// Add number first to calculate length.
		if (skill.isGeneralizable()) {
			if (skill.isFromGuild()) {
				float usedWidth = FadingSunsTheme.getLineItalicFont().getWidthPoint(
						skill.getName() + " []" + (skill.isNatural() ? DEFAULT_NATURAL_SKILL_VALUE : ""), FadingSunsTheme.SKILLS_LINE_FONT_SIZE);
				sufix.append(" [");
				sufix.append(getWhiteSpaces(FadingSunsTheme.getLineItalicFont(), FadingSunsTheme.SKILLS_LINE_FONT_SIZE, MAX_SKILL_COLUMN_WIDTH - usedWidth));
				sufix.append("]");
			} else {
				float usedWidth = FadingSunsTheme.getLineFont().getWidthPoint(skill.getName() + " []" + (skill.isNatural() ? DEFAULT_NATURAL_SKILL_VALUE : ""),
						FadingSunsTheme.SKILLS_LINE_FONT_SIZE);
				sufix.append(" [");
				sufix.append(getWhiteSpaces(FadingSunsTheme.getLineFont(), FadingSunsTheme.SKILLS_LINE_FONT_SIZE, MAX_SKILL_COLUMN_WIDTH - usedWidth));
				sufix.append("]");
			}
		}
		// Put number at the end.
		if (skill.isNatural()) {
			sufix = new StringBuilder(sufix.toString().replace(DEFAULT_NATURAL_SKILL_VALUE, ""));
			sufix.append(DEFAULT_NATURAL_SKILL_VALUE);
		}

		return sufix.toString();
	}

	private static String getWhiteSpaces(BaseFont font, int fontSize, float width) {
		int numberOfSpaces = 1;

		while (true) {
			if (font.getWidthPoint(createWhiteLine(numberOfSpaces), fontSize) > width) {
				return createWhiteLine(numberOfSpaces - 1);
			}
			numberOfSpaces++;
		}
	}

	private static String createWhiteLine(int charLength) {
		char[] repeat = new char[charLength];
		Arrays.fill(repeat, ' ');
		return new String(repeat);
	}

}
