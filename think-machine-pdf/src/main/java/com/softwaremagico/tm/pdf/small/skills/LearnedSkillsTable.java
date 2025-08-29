package com.softwaremagico.tm.pdf.small.skills;

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
import com.softwaremagico.tm.configurator.MachinePdfConfigurationReader;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.CustomPdfTable;
import com.softwaremagico.tm.pdf.complete.skills.SkillsTable;

public class LearnedSkillsTable extends SkillsTable {
	private static final String GAP = "__________________";
	private static final int ROWS = 23;
	private static final int MAX_SKILL_COLUMN_WIDTH = 115;
	private static final int MAX_SKILL_RANK_WIDTH = 15;

	public static PdfPTable getSkillsTable(CharacterPlayer characterPlayer)
			throws InvalidXmlElementException {
		final float[] widths = { 1f };
		final PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);
		table.getDefaultCell().setBorder(0);
		table.addCell(getSkillsColumnTable(characterPlayer));
		return table;
	}

	private static PdfPCell getSkillsColumnTable(CharacterPlayer characterPlayer) {
		final float[] widths = { 4f, 1f };
		final PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);
		table.getDefaultCell().setBorder(0);

		table.addCell(createCompactTitle(getTranslator().getTranslatedText("learnedSkills"),
				FadingSunsTheme.CHARACTER_SMALL_SKILLS_TITLE_FONT_SIZE));

		int added = 0;
		if (characterPlayer != null) {
			for (final AvailableSkill skill : characterPlayer.getLearnedSkills()) {
				if (characterPlayer.getSkillTotalRanks(skill) > 0) {
					table.addCell(createSkillElement(characterPlayer, skill,
							FadingSunsTheme.CHARACTER_SMALL_SKILLS_LINE_FONT_SIZE, MAX_SKILL_COLUMN_WIDTH));
					table.addCell(createSkillValue(
							characterPlayer.getSkillTotalRanks(skill),
							characterPlayer.isSkillSpecial(skill) || characterPlayer.hasSkillTemporalModificator(skill),
							characterPlayer.hasSkillModificator(skill),
							FadingSunsTheme.CHARACTER_SMALL_SKILLS_LINE_FONT_SIZE));
					added++;
				}
			}
		}

		final int rows = MachinePdfConfigurationReader.getInstance().isSmallPdfShieldEnabled() ? ROWS : ROWS + 1;
		if (characterPlayer == null) {
			for (int i = added; i < rows; i++) {
				table.addCell(CustomPdfTable.createEmptyElementLine(GAP, MAX_SKILL_COLUMN_WIDTH,
						FadingSunsTheme.CHARACTER_SMALL_SKILLS_LINE_FONT_SIZE));
				table.addCell(CustomPdfTable.createEmptyElementLine(GAP, MAX_SKILL_RANK_WIDTH,
						FadingSunsTheme.CHARACTER_SMALL_SKILLS_LINE_FONT_SIZE));
			}
		} else {
			for (int i = added; i < rows; i++) {
				table.addCell(CustomPdfTable.createEmptyElementLine(" ", MAX_SKILL_COLUMN_WIDTH,
						FadingSunsTheme.CHARACTER_SMALL_SKILLS_LINE_FONT_SIZE));
				table.addCell(CustomPdfTable.createEmptyElementLine(" ", MAX_SKILL_RANK_WIDTH,
						FadingSunsTheme.CHARACTER_SMALL_SKILLS_LINE_FONT_SIZE));
			}
		}

		final PdfPCell cell = new PdfPCell();
		setCellProperties(cell);

		cell.addElement(table);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		return cell;
	}
}
