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
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.SkillFactory;
import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.BaseElement;
import com.softwaremagico.tm.pdf.skills.occultism.OccultismTable;
import com.softwaremagico.tm.pdf.utils.CellUtils;

public class SkillsTable extends BaseElement {
	private final static int ROWS = 30;
	private final static int TITLE_ROWSPAN = 2;
	private static int learnedSkillsAdded = 0;
	private final static String SKILL_VALUE_GAP = "____";
	private final static int OCCULTISM_ROWS = 5;
	private final static int MAX_SKILL_COLUMN_WIDTH = 115;
	private final static String DEFAULT_NATURAL_SKILL_VALUE = " (3)";
	private final static String DEFAULT_WHITE_SPACES = "                                ";

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

		if (characterPlayer == null) {
			for (AvailableSkill skill : SkillFactory.getNaturalSkills(language)) {
				table.addCell(createSkillElement(characterPlayer, skill));
				table.addCell(createSkillLine(SKILL_VALUE_GAP));
			}
		} else {
			for (AvailableSkill skill : characterPlayer.getNaturalSkills()) {
				table.addCell(createSkillElement(characterPlayer, skill));
				table.addCell(createSkillValue(characterPlayer.getSkillValue(skill),
						characterPlayer.isSkillSpecial(skill)));
			}
		}

		table.addCell(createTitle(getTranslator().getTranslatedText("learnedSkills")));
		for (int i = 0; i < Math.min(SkillFactory.getLearnedSkills(language).size(), ROWS
				- (2 * TITLE_ROWSPAN) - SkillFactory.getNaturalSkills(language).size()); i++) {
			table.addCell(createSkillElement(characterPlayer, SkillFactory.getLearnedSkills(language).get(i)));
			if (characterPlayer == null) {
				table.addCell(createSkillLine(SKILL_VALUE_GAP));
			} else {
				table.addCell(createSkillValue(
						characterPlayer.getSkillValue(SkillFactory.getLearnedSkills(language).get(i)),
						characterPlayer.isSkillSpecial(SkillFactory.getLearnedSkills(language).get(i))));
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
				table.addCell(createSkillLine(SKILL_VALUE_GAP));
			} else {
				table.addCell(createSkillValue(
						characterPlayer.getSkillValue(SkillFactory.getLearnedSkills(language).get(i)),
						characterPlayer.isSkillSpecial(SkillFactory.getLearnedSkills(language).get(i))));
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
				table.addCell(createSkillLine(SKILL_VALUE_GAP));
			} else {
				table.addCell(createSkillValue(
						characterPlayer.getSkillValue(SkillFactory.getLearnedSkills(language).get(i)),
						characterPlayer.isSkillSpecial(SkillFactory.getLearnedSkills(language).get(i))));
			}
			learnedSkillsAdded++;
			addedElements++;
		}

		// Complete with empty skills.
		for (int i = addedElements; i < ROWS - OCCULTISM_ROWS; i++) {
			table.addCell(createSkillLine("________________________"));
			table.addCell(createSkillLine(SKILL_VALUE_GAP));
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

	private static PdfPCell createTitle(String text) {
		PdfPCell cell = getCell(text, 0, 2, Element.ALIGN_CENTER, BaseColor.WHITE,
				FadingSunsTheme.getTitleFont(), FadingSunsTheme.SKILLS_TITLE_FONT_SIZE);
		cell.setMinimumHeight(MainSkillsTableFactory.HEIGHT / (ROWS / TITLE_ROWSPAN) + 1);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

	private static PdfPCell createSkillElement(CharacterPlayer characterPlayer, AvailableSkill skill) {
		PdfPCell cell = getCell(createSkillSufix(characterPlayer, skill), 0, 1, Element.ALIGN_LEFT,
				BaseColor.WHITE);
		cell.setMinimumHeight((MainSkillsTableFactory.HEIGHT / ROWS));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

	private static PdfPCell createSkillLine(String text) {
		PdfPCell cell = getCell(text, 0, 1, Element.ALIGN_LEFT, BaseColor.WHITE,
				FadingSunsTheme.getLineFont(), FadingSunsTheme.SKILLS_LINE_FONT_SIZE);
		cell.setMinimumHeight((MainSkillsTableFactory.HEIGHT / ROWS));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

	private static PdfPCell createSkillValue(Integer value, boolean special) {
		if (value == null) {
			return createSkillLine(SKILL_VALUE_GAP);
		}
		PdfPCell cell = getCell(value + (special ? "*" : ""), 0, 1, Element.ALIGN_CENTER, BaseColor.WHITE,
				FadingSunsTheme.getHandwrittingFont(),
				FadingSunsTheme.getHandWrittingFontSize(FadingSunsTheme.SKILLS_LINE_FONT_SIZE));
		cell.setMinimumHeight((MainSkillsTableFactory.HEIGHT / ROWS));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

	private static Paragraph createSkillSufix(CharacterPlayer characterPlayer, AvailableSkill skill) {
		Paragraph paragraph = new Paragraph();
		// Add number first to calculate length.
		if (skill.isGeneralizable()) {
			if (skill.isFromGuild()) {
				paragraph.add(createGeneralizedSkill(characterPlayer, skill,
						FadingSunsTheme.getLineItalicFont()));
			} else {
				paragraph.add(createGeneralizedSkill(characterPlayer, skill, FadingSunsTheme.getLineFont()));
			}
		} else {
			if (skill.isFromGuild()) {
				paragraph.add(new Paragraph(skill.getName(), new Font(FadingSunsTheme.getLineItalicFont(),
						FadingSunsTheme.SKILLS_LINE_FONT_SIZE)));
			} else {
				paragraph.add(new Paragraph(skill.getName(), new Font(FadingSunsTheme.getLineFont(),
						FadingSunsTheme.SKILLS_LINE_FONT_SIZE)));
			}
		}
		// Put number at the end.
		if (skill.isNatural()) {
			paragraph.add(new Paragraph(DEFAULT_NATURAL_SKILL_VALUE, new Font(FadingSunsTheme.getLineFont(),
					FadingSunsTheme.SKILLS_LINE_FONT_SIZE)));
		}

		return paragraph;
	}

	private static Paragraph createGeneralizedSkill(CharacterPlayer characterPlayer, AvailableSkill skill,
			BaseFont font) {
		Paragraph paragraph = new Paragraph();
		float usedWidth = font.getWidthPoint(skill.getName() + " []"
				+ (skill.isNatural() ? DEFAULT_NATURAL_SKILL_VALUE : ""),
				FadingSunsTheme.SKILLS_LINE_FONT_SIZE);
		paragraph.add(new Paragraph(skill.getName() + " [", new Font(font,
				FadingSunsTheme.SKILLS_LINE_FONT_SIZE)));
		// if (skill.getGeneralization() == null) {
		if (characterPlayer != null && characterPlayer.getSelectedSkill(skill) == null) {
			if (skill.getGeneralization() != null) {
				paragraph.add(new Paragraph(CellUtils.getSubStringFitsIn(skill.getGeneralization(),
						FadingSunsTheme.getHandwrittingFont(),
						FadingSunsTheme.getHandWrittingFontSize(FadingSunsTheme.SKILLS_LINE_FONT_SIZE),
						MAX_SKILL_COLUMN_WIDTH - usedWidth), new Font(FadingSunsTheme.getHandwrittingFont(),
						FadingSunsTheme.getHandWrittingFontSize(FadingSunsTheme.SKILLS_LINE_FONT_SIZE))));
			} else {
				paragraph.add(new Paragraph(CellUtils.getSubStringFitsIn(DEFAULT_WHITE_SPACES, font,
						FadingSunsTheme.SKILLS_LINE_FONT_SIZE, MAX_SKILL_COLUMN_WIDTH - usedWidth), new Font(
						font, FadingSunsTheme.SKILLS_LINE_FONT_SIZE)));
			}
		} else {
			paragraph.add(new Paragraph(CellUtils.getSubStringFitsIn(
					characterPlayer != null ? characterPlayer.getSelectedSkill(skill).getName()
							.replace(skill.getName(), "").replace("[", "").replace("]", "").trim()
							: DEFAULT_WHITE_SPACES, FadingSunsTheme.getHandwrittingFont(), FadingSunsTheme
							.getHandWrittingFontSize(FadingSunsTheme.SKILLS_LINE_FONT_SIZE),
					MAX_SKILL_COLUMN_WIDTH - usedWidth), new Font(FadingSunsTheme.getHandwrittingFont(),
					FadingSunsTheme.getHandWrittingFontSize(FadingSunsTheme.SKILLS_LINE_FONT_SIZE))));
		}
		paragraph.add(new Paragraph("]", new Font(font, FadingSunsTheme.SKILLS_LINE_FONT_SIZE)));
		return paragraph;
	}
}
