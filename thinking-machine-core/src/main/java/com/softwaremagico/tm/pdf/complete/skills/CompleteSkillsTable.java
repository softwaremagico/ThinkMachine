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

import java.util.List;

import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.character.skills.SkillDefinition;
import com.softwaremagico.tm.character.skills.SkillsDefinitionsFactory;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.skills.occultism.OccultismTable;

public class CompleteSkillsTable extends SkillsTable {
	private final static int ROWS = 30;
	private static int skillDefinitionAdded = 0;
	private static Integer totalSkillsToShow = null;
	private final static String SKILL_VALUE_GAP = "____";
	private final static int OCCULTISM_ROWS = 5;

	public static PdfPTable getSkillsTable(CharacterPlayer characterPlayer, String language) throws InvalidXmlElementException {
		float[] widths = { 1f, 1f, 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);
		skillDefinitionAdded = 0;
		table.addCell(getFirstColumnTable(characterPlayer, language));
		table.addCell(getSecondColumnTable(characterPlayer, language));
		table.addCell(getThirdColumnTable(characterPlayer, language));
		return table;
	}

	private static PdfPCell getFirstColumnTable(CharacterPlayer characterPlayer, String language) throws InvalidXmlElementException {
		float[] widths = { 4f, 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);

		table.addCell(createTitle(getTranslator().getTranslatedText("naturalSkills"), FadingSunsTheme.SKILLS_TITLE_FONT_SIZE));

		if (characterPlayer == null) {
			for (AvailableSkill skill : AvailableSkillsFactory.getInstance().getNaturalSkills(language)) {
				table.addCell(createSkillElement(characterPlayer, skill, FadingSunsTheme.SKILLS_LINE_FONT_SIZE));
				table.addCell(createSkillLine(SKILL_VALUE_GAP, FadingSunsTheme.SKILLS_LINE_FONT_SIZE));
			}
		} else {
			for (AvailableSkill skill : characterPlayer.getNaturalSkills()) {
				table.addCell(createSkillElement(characterPlayer, skill, FadingSunsTheme.SKILLS_LINE_FONT_SIZE));
				table.addCell(createSkillValue(characterPlayer.getSkillRanks(skill), characterPlayer.isSkillSpecial(skill),
						FadingSunsTheme.SKILLS_LINE_FONT_SIZE));
			}
		}

		table.addCell(createTitle(getTranslator().getTranslatedText("learnedSkills"), FadingSunsTheme.SKILLS_TITLE_FONT_SIZE));
		int totalRows = Math.min(getTotalLearnedSkillsToShow(language),
				ROWS - (2 * TITLE_ROWSPAN) - SkillsDefinitionsFactory.getInstance().getNaturalSkills(language).size());
		completeColumnWithLearnedSkills(table, totalRows, characterPlayer, language);

		PdfPCell cell = new PdfPCell();
		setCellProperties(cell);

		cell.addElement(table);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		return cell;
	}

	private static PdfPCell getSecondColumnTable(CharacterPlayer characterPlayer, String language) throws InvalidXmlElementException {
		float[] widths = { 4f, 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);
		PdfPCell cell = new PdfPCell();
		setCellProperties(cell);

		completeColumnWithLearnedSkills(table, ROWS, characterPlayer, language);

		cell.addElement(table);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		return cell;
	}

	private static PdfPCell getThirdColumnTable(CharacterPlayer characterPlayer, String language) throws InvalidXmlElementException {
		float[] widths = { 4f, 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);
		PdfPCell cell = new PdfPCell();
		setCellProperties(cell);

		completeColumnWithLearnedSkills(table, ROWS - OCCULTISM_ROWS, characterPlayer, language);

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

	private static int getTotalLearnedSkillsToShow(String language) throws InvalidXmlElementException {
		if (totalSkillsToShow != null) {
			return totalSkillsToShow;
		}
		int total = 0;
		for (SkillDefinition skillDefinition : SkillsDefinitionsFactory.getInstance().getLearnedSkills(language)) {
			total += skillDefinition.getNumberToShow();
		}
		totalSkillsToShow = total;
		return total;
	}

	private static void completeColumnWithLearnedSkills(PdfPTable table, int totalRows, CharacterPlayer characterPlayer, String language)
			throws InvalidXmlElementException {
		int rowsAdded = 0;
		while (rowsAdded < totalRows) {
			// We need to put empty specialized skills, but not all possible specializations.
			try {
				SkillDefinition skillDefinition = SkillsDefinitionsFactory.getInstance().getLearnedSkills(language).get(skillDefinitionAdded);
				skillDefinitionAdded++;
				int addedAvailableSkill = 0;
				// But first the already defined in a character.
				List<AvailableSkill> availableSkillsByDefinition = AvailableSkillsFactory.getInstance().getAvailableSkills(skillDefinition, language);
				for (AvailableSkill availableSkill : availableSkillsByDefinition) {
					// Only specializations if they have ranks.
					if (!skillDefinition.isSpecializable() || (characterPlayer != null && characterPlayer.getSkillRanks(availableSkill) > 0)) {
						table.addCell(createSkillElement(characterPlayer, availableSkill, FadingSunsTheme.SKILLS_LINE_FONT_SIZE));
						if (characterPlayer == null) {
							table.addCell(createSkillLine(SKILL_VALUE_GAP, FadingSunsTheme.SKILLS_LINE_FONT_SIZE));
						} else {
							table.addCell(createSkillValue(characterPlayer.getSkillRanks(availableSkill), characterPlayer.isSkillSpecial(availableSkill),
									FadingSunsTheme.SKILLS_LINE_FONT_SIZE));
						}
						addedAvailableSkill++;
						rowsAdded++;
					}
				}
				// We want some empty specializations into the chart.
				for (int j = addedAvailableSkill; j < skillDefinition.getNumberToShow() && rowsAdded < totalRows; j++) {
					System.out.println(rowsAdded + " < " + totalRows);
					table.addCell(createSkillElement(skillDefinition, FadingSunsTheme.SKILLS_LINE_FONT_SIZE));
					table.addCell(createSkillLine(SKILL_VALUE_GAP, FadingSunsTheme.SKILLS_LINE_FONT_SIZE));
					rowsAdded++;
				}
			} catch (IndexOutOfBoundsException iobe) {
				break;
			}
		}

		// Complete with empty skills the end of the column.
		for (int row = rowsAdded; row < totalRows; row++) {
			table.addCell(createSkillLine("________________________", FadingSunsTheme.SKILLS_LINE_FONT_SIZE));
			table.addCell(createSkillLine(SKILL_VALUE_GAP, FadingSunsTheme.SKILLS_LINE_FONT_SIZE));
		}
	}
}
