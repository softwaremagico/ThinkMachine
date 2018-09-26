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

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.SkillDefinition;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.pdf.complete.utils.CellUtils;

public class SkillsTable extends BaseElement {
	private final static int ROWS = 30;
	protected final static int TITLE_ROWSPAN = 2;

	private final static String DEFAULT_NATURAL_SKILL_VALUE = " (3)";
	private final static String DEFAULT_WHITE_SPACES = "                                ";
	public final static String SKILL_VALUE_GAP = "____";

	protected static PdfPCell createTitle(String text, int fontSize) {
		PdfPCell cell = createCompactTitle(text, fontSize);
		cell.setMinimumHeight(MainSkillsTableFactory.HEIGHT / (ROWS / TITLE_ROWSPAN) + 1);
		return cell;
	}

	protected static PdfPCell createCompactTitle(String text, int fontSize) {
		PdfPCell cell = getCell(text, 0, 2, Element.ALIGN_CENTER, BaseColor.WHITE, FadingSunsTheme.getTitleFont(), fontSize);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

	protected static PdfPCell createSkillElement(CharacterPlayer characterPlayer, AvailableSkill skill, int fontSize, int maxColumnWidth) {
		PdfPCell cell = getCell(createSkillSufix(characterPlayer, skill, fontSize, maxColumnWidth), 0, 1, Element.ALIGN_LEFT, BaseColor.WHITE);
		cell.setMinimumHeight((MainSkillsTableFactory.HEIGHT / ROWS));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

	protected static PdfPCell createSkillElement(SkillDefinition skill, int fontSize, int maxColumnWidth) {
		PdfPCell cell = getCell(createSkillSufix(skill, fontSize, maxColumnWidth), 0, 1, Element.ALIGN_LEFT, BaseColor.WHITE);
		cell.setMinimumHeight((MainSkillsTableFactory.HEIGHT / ROWS));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

	protected static PdfPCell createSkillLine(String text, int fontSize) {
		PdfPCell cell = getCell(text, 0, 1, Element.ALIGN_LEFT, BaseColor.WHITE, FadingSunsTheme.getLineFont(), fontSize);
		cell.setMinimumHeight((MainSkillsTableFactory.HEIGHT / ROWS));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

	protected static PdfPCell createSkillValue(Integer value, boolean special, boolean modified, int fontSize) {
		if (value == null) {
			return createSkillLine(SKILL_VALUE_GAP, fontSize);
		}
		PdfPCell cell = getCell(value + (special ? "*" : "") + (modified && value > 0 ? "!" : ""), 0, 1, Element.ALIGN_CENTER, BaseColor.WHITE,
				FadingSunsTheme.getHandwrittingFont(), FadingSunsTheme.getHandWrittingFontSize(fontSize));
		cell.setMinimumHeight((MainSkillsTableFactory.HEIGHT / ROWS));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

	private static Paragraph createSkillSufix(CharacterPlayer characterPlayer, AvailableSkill availableSkill, int fontSize, int maxColumnWidth) {
		Paragraph paragraph = new Paragraph();
		// Add number first to calculate length.
		if (availableSkill.getSpecialization() != null) {
			if (availableSkill.getSkillDefinition().isLimitedToFaction()) {
				paragraph.add(createSpecializedSkill(characterPlayer, availableSkill, FadingSunsTheme.getLineItalicFont(), fontSize, maxColumnWidth));
			} else {
				paragraph.add(createSpecializedSkill(characterPlayer, availableSkill, FadingSunsTheme.getLineFont(), fontSize, maxColumnWidth));
			}
		} else {
			if (availableSkill.getSkillDefinition().isLimitedToFaction()) {
				paragraph.add(new Paragraph(availableSkill.getName(), new Font(FadingSunsTheme.getLineItalicFont(), fontSize)));
			} else {
				paragraph.add(new Paragraph(availableSkill.getName(), new Font(FadingSunsTheme.getLineFont(), fontSize)));
			}
		}
		// Put number at the end.
		if (availableSkill.getSkillDefinition().isNatural()) {
			paragraph.add(new Paragraph(DEFAULT_NATURAL_SKILL_VALUE, new Font(FadingSunsTheme.getLineFont(), fontSize)));
		}

		return paragraph;
	}

	private static Paragraph createSpecializedSkill(CharacterPlayer characterPlayer, AvailableSkill availableSkill, BaseFont font, int fontSize,
			int maxColumnWidth) {
		Paragraph paragraph = new Paragraph();
		float usedWidth = font.getWidthPoint(availableSkill.getName() + " []"
				+ (availableSkill.getSkillDefinition().isNatural() ? DEFAULT_NATURAL_SKILL_VALUE : ""), fontSize);
		paragraph.add(new Paragraph(availableSkill.getName() + " [", new Font(font, fontSize)));
		if (characterPlayer != null && characterPlayer.getSelectedSkill(availableSkill) == null) {
			if (availableSkill.getSpecialization() != null && availableSkill.getSpecialization().getName() != null) {
				paragraph.add(new Paragraph(CellUtils.getSubStringFitsIn(availableSkill.getSpecialization().getName(), FadingSunsTheme.getHandwrittingFont(),
						FadingSunsTheme.getHandWrittingFontSize(fontSize), maxColumnWidth - usedWidth), new Font(FadingSunsTheme.getHandwrittingFont(),
						FadingSunsTheme.getHandWrittingFontSize(fontSize))));
			} else {
				paragraph.add(new Paragraph(CellUtils.getSubStringFitsIn(DEFAULT_WHITE_SPACES, font, fontSize, maxColumnWidth - usedWidth), new Font(font,
						fontSize)));
			}
		} else {
			paragraph.add(new Paragraph(CellUtils.getSubStringFitsIn(characterPlayer != null ? characterPlayer.getSelectedSkill(availableSkill).getName()
					.replace(availableSkill.getName(), "").replace("[", "").replace("]", "").trim() : DEFAULT_WHITE_SPACES,
					FadingSunsTheme.getHandwrittingFont(), FadingSunsTheme.getHandWrittingFontSize(fontSize), maxColumnWidth - usedWidth), new Font(
					FadingSunsTheme.getHandwrittingFont(), FadingSunsTheme.getHandWrittingFontSize(fontSize))));
		}
		paragraph.add(new Paragraph("]", new Font(font, fontSize)));
		return paragraph;
	}

	private static Paragraph createSkillSufix(SkillDefinition skillDefinition, int fontSize, int maxColumnWidth) {
		Paragraph paragraph = new Paragraph();
		// Add number first to calculate length.
		if (skillDefinition.isSpecializable()) {
			if (skillDefinition.isLimitedToFaction()) {
				paragraph.add(createSpecializedSkill(skillDefinition, FadingSunsTheme.getLineItalicFont(), fontSize, maxColumnWidth));
			} else {
				paragraph.add(createSpecializedSkill(skillDefinition, FadingSunsTheme.getLineFont(), fontSize, maxColumnWidth));
			}
		} else {
			if (skillDefinition.isLimitedToFaction()) {
				paragraph.add(new Paragraph(skillDefinition.getName(), new Font(FadingSunsTheme.getLineItalicFont(), fontSize)));
			} else {
				paragraph.add(new Paragraph(skillDefinition.getName(), new Font(FadingSunsTheme.getLineFont(), fontSize)));
			}
		}
		// Put number at the end.
		if (skillDefinition.isNatural()) {
			paragraph.add(new Paragraph(DEFAULT_NATURAL_SKILL_VALUE, new Font(FadingSunsTheme.getLineFont(), fontSize)));
		}

		return paragraph;
	}

	private static Paragraph createSpecializedSkill(SkillDefinition skillDefinition, BaseFont font, int fontSize, int maxColumnWidth) {
		Paragraph paragraph = new Paragraph();
		float usedWidth = font.getWidthPoint(skillDefinition.getName() + " []" + (skillDefinition.isNatural() ? DEFAULT_NATURAL_SKILL_VALUE : ""), fontSize);
		paragraph.add(new Paragraph(skillDefinition.getName() + " [", new Font(font, fontSize)));
		paragraph.add(new Paragraph(CellUtils.getSubStringFitsIn(DEFAULT_WHITE_SPACES, font, fontSize, maxColumnWidth - usedWidth), new Font(font, fontSize)));
		paragraph.add(new Paragraph("]", new Font(font, fontSize)));
		return paragraph;
	}

}
