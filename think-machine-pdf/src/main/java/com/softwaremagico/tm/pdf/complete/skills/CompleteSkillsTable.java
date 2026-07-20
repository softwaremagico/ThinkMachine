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

import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.character.skills.SkillDefinition;
import com.softwaremagico.tm.character.skills.SkillsDefinitionsFactory;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.skills.occultism.OccultismTable;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class CompleteSkillsTable extends SkillsTable {
    private static final int ROWS = 30;
    private static Integer totalSkillsToShow = null;
    private static final String SKILL_VALUE_GAP = "____";
    private static final int OCCULTISM_ROWS = 5;
    private static final int MAX_SKILL_COLUMN_WIDTH = 115;

    public static PdfPTable getSkillsTable(CharacterPlayer characterPlayer, String language, String moduleName)
            throws InvalidXmlElementException {
        final float[] widths = {1f, 1f, 1f};
        final PdfPTable table = new PdfPTable(widths);
        setTablePropierties(table);

        final Stack<PdfPCell> learnedSkillsRows = createLearnedSkillsRows(ROWS - (2 * TITLE_ROWSPAN)
                - SkillsDefinitionsFactory.getInstance().getNaturalSkills(language, moduleName).size() + ROWS + ROWS
                - OCCULTISM_ROWS, characterPlayer, language, moduleName);

        table.addCell(getFirstColumnTable(characterPlayer, language, moduleName, learnedSkillsRows));
        table.addCell(getSecondColumnTable(characterPlayer, language, learnedSkillsRows));
        table.addCell(getThirdColumnTable(characterPlayer, language, moduleName, learnedSkillsRows));
        return table;
    }

    private static PdfPCell getFirstColumnTable(CharacterPlayer characterPlayer, String language, String moduleName,
                                                Stack<PdfPCell> learnedSkillsRows) throws InvalidXmlElementException {
        final float[] widths = {4f, 1f};
        final PdfPTable table = new PdfPTable(widths);
        setTablePropierties(table);

        table.addCell(createTitle(getTranslator().getTranslatedText("naturalSkills"),
                FadingSunsTheme.SKILLS_TITLE_FONT_SIZE));

        if (characterPlayer == null) {
            for (final AvailableSkill skill : AvailableSkillsFactory.getInstance().getNaturalSkills(language,
                    moduleName)) {
                table.addCell(createSkillElement(null, skill, FadingSunsTheme.SKILLS_LINE_FONT_SIZE,
                        MAX_SKILL_COLUMN_WIDTH));
                table.addCell(createSkillLine(SKILL_VALUE_GAP, FadingSunsTheme.SKILLS_LINE_FONT_SIZE));
            }
        } else {
            for (final AvailableSkill skill : characterPlayer.getNaturalSkills()) {
                table.addCell(createSkillElement(characterPlayer, skill, FadingSunsTheme.SKILLS_LINE_FONT_SIZE,
                        MAX_SKILL_COLUMN_WIDTH));
                table.addCell(createSkillValue(characterPlayer.getSkillTotalRanks(skill),
                        characterPlayer.isSkillSpecial(skill) || characterPlayer.hasSkillTemporalModificator(skill),
                        characterPlayer.hasSkillModificator(skill), FadingSunsTheme.SKILLS_LINE_FONT_SIZE));
            }
        }

        table.addCell(createTitle(getTranslator().getTranslatedText("learnedSkills"),
                FadingSunsTheme.SKILLS_TITLE_FONT_SIZE));
        final int totalRows = Math.min(getTotalLearnedSkillsToShow(language, moduleName), ROWS - (2 * TITLE_ROWSPAN) - 1
                - SkillsDefinitionsFactory.getInstance().getNaturalSkills(language, moduleName).size());

        for (int i = 0; i < totalRows; i++) {
            // Two columns: skill and value.
            table.addCell(learnedSkillsRows.pop());
            table.addCell(learnedSkillsRows.pop());
        }

        final PdfPCell cell = new PdfPCell();
        setCellProperties(cell);

        cell.addElement(table);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        return cell;
    }

    private static PdfPCell getSecondColumnTable(CharacterPlayer characterPlayer, String language,
                                                 Stack<PdfPCell> learnedSkillsRows) throws InvalidXmlElementException {
        final float[] widths = {4f, 1f};
        final PdfPTable table = new PdfPTable(widths);
        setTablePropierties(table);
        final PdfPCell cell = new PdfPCell();
        setCellProperties(cell);

        for (int i = 0; i < ROWS; i++) {
            // Two columns: skill and value.
            table.addCell(learnedSkillsRows.pop());
            table.addCell(learnedSkillsRows.pop());
        }

        cell.addElement(table);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        return cell;
    }

    private static PdfPCell getThirdColumnTable(CharacterPlayer characterPlayer, String language, String moduleName,
                                                Stack<PdfPCell> learnedSkillsRows) throws InvalidXmlElementException {
        final float[] widths = {4f, 1f};
        final PdfPTable table = new PdfPTable(widths);
        setTablePropierties(table);
        final PdfPCell cell = new PdfPCell();
        setCellProperties(cell);

        for (int i = 0; i < ROWS - OCCULTISM_ROWS; i++) {
            // Two columns: skill and value.
            table.addCell(learnedSkillsRows.pop());
            table.addCell(learnedSkillsRows.pop());
        }

        // Add Occultism table
        final PdfPTable occultismTable = new OccultismTable(characterPlayer, language, moduleName);
        final PdfPCell occulstimCell = new PdfPCell();
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

    private static int getTotalLearnedSkillsToShow(String language, String moduleName)
            throws InvalidXmlElementException {
        if (totalSkillsToShow != null) {
            return totalSkillsToShow;
        }
        int total = 0;
        for (final SkillDefinition skillDefinition : SkillsDefinitionsFactory.getInstance().getLearnedSkills(language,
                moduleName)) {
            total += skillDefinition.getNumberToShow();
        }
        totalSkillsToShow = total;
        return total;
    }

    /**
     * Creates a stack of skills and values in order.
     *
     * @param totalRows       Rows to be filled up.
     * @param characterPlayer Character data.
     * @param language        Language of the skills.
     * @return The stack
     * @throws InvalidXmlElementException If content files cannot be read.
     */
    private static Stack<PdfPCell> createLearnedSkillsRows(int totalRows, CharacterPlayer characterPlayer,
                                                           String language, String moduleName) throws InvalidXmlElementException {
        final Stack<PdfPCell> rows = new Stack<>();

        int rowsAdded = 0;
        for (final SkillDefinition skillDefinition : SkillsDefinitionsFactory.getInstance().getLearnedSkills(language,
                moduleName)) {
            // We need to put empty specialized skills, but not all possible
            // specializations.
            try {
                int addedAvailableSkill = 0;
                // But first the already defined in a character.
                final List<AvailableSkill> availableSkillsByDefinition = AvailableSkillsFactory.getInstance()
                        .getAvailableSkills(skillDefinition, language, moduleName);
                for (final AvailableSkill availableSkill : availableSkillsByDefinition) {
                    // Only specializations if they have ranks.
                    if (!skillDefinition.isSpecializable()
                            || (characterPlayer != null && characterPlayer.getSkillTotalRanks(availableSkill) > 0)) {
                        rows.add(createSkillElement(characterPlayer, availableSkill,
                                FadingSunsTheme.SKILLS_LINE_FONT_SIZE, MAX_SKILL_COLUMN_WIDTH));
                        if (characterPlayer == null) {
                            rows.add(createSkillLine(SKILL_VALUE_GAP, FadingSunsTheme.SKILLS_LINE_FONT_SIZE));
                        } else {
                            rows.add(createSkillValue(
                                    characterPlayer.getSkillTotalRanks(availableSkill),
                                    characterPlayer.isSkillSpecial(availableSkill)
                                            || characterPlayer.hasSkillTemporalModificator(availableSkill),
                                    characterPlayer.hasSkillModificator(availableSkill),
                                    FadingSunsTheme.SKILLS_LINE_FONT_SIZE));
                        }
                        addedAvailableSkill++;
                        rowsAdded++;
                    }
                }
                // We want some empty specializations into the chart.
                for (int j = addedAvailableSkill; j < skillDefinition.getNumberToShow() && rowsAdded < totalRows; j++) {
                    rows.add(createSkillElement(skillDefinition, FadingSunsTheme.SKILLS_LINE_FONT_SIZE,
                            MAX_SKILL_COLUMN_WIDTH));
                    rows.add(createSkillLine(SKILL_VALUE_GAP, FadingSunsTheme.SKILLS_LINE_FONT_SIZE));
                    rowsAdded++;
                }
            } catch (IndexOutOfBoundsException iobe) {
                break;
            }
        }

        // Complete with empty skills the end of the column.
        for (int row = rowsAdded; row < totalRows; row++) {
            rows.add(createSkillLine("________________________", FadingSunsTheme.SKILLS_LINE_FONT_SIZE));
            rows.add(createSkillLine(SKILL_VALUE_GAP, FadingSunsTheme.SKILLS_LINE_FONT_SIZE));
        }

        // Reverse stack due to we need to start from the first skill.
        Collections.reverse(rows);
        return rows;
    }
}
