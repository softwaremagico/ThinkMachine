package com.softwaremagico.tm.pdf.skills;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.BaseElement;
import com.softwaremagico.tm.pdf.utils.CellUtils;

public class SkillsTable extends BaseElement {
	private final static int ROWS = 30;
	protected final static int TITLE_ROWSPAN = 2;

	private final static int MAX_SKILL_COLUMN_WIDTH = 115;
	private final static String DEFAULT_NATURAL_SKILL_VALUE = " (3)";
	private final static String DEFAULT_WHITE_SPACES = "                                ";
	public final static String SKILL_VALUE_GAP = "____";

	protected static PdfPCell createTitle(String text, int fontSize) {
		PdfPCell cell = getCell(text, 0, 2, Element.ALIGN_CENTER, BaseColor.WHITE, FadingSunsTheme.getTitleFont(), fontSize);
		cell.setMinimumHeight(MainSkillsTableFactory.HEIGHT / (ROWS / TITLE_ROWSPAN) + 1);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

	protected static PdfPCell createSkillElement(CharacterPlayer characterPlayer, AvailableSkill skill, int fontSize) {
		PdfPCell cell = getCell(createSkillSufix(characterPlayer, skill, fontSize), 0, 1, Element.ALIGN_LEFT, BaseColor.WHITE);
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

	protected static PdfPCell createSkillValue(Integer value, boolean special, int fontSize) {
		if (value == null) {
			return createSkillLine(SKILL_VALUE_GAP, fontSize);
		}
		PdfPCell cell = getCell(value + (special ? "*" : ""), 0, 1, Element.ALIGN_CENTER, BaseColor.WHITE, FadingSunsTheme.getHandwrittingFont(),
				FadingSunsTheme.getHandWrittingFontSize(fontSize));
		cell.setMinimumHeight((MainSkillsTableFactory.HEIGHT / ROWS));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

	private static Paragraph createSkillSufix(CharacterPlayer characterPlayer, AvailableSkill skill, int fontSize) {
		Paragraph paragraph = new Paragraph();
		// Add number first to calculate length.
		if (skill.isGeneralizable()) {
			if (skill.isFromGuild()) {
				paragraph.add(createGeneralizedSkill(characterPlayer, skill, FadingSunsTheme.getLineItalicFont(), fontSize));
			} else {
				paragraph.add(createGeneralizedSkill(characterPlayer, skill, FadingSunsTheme.getLineFont(), fontSize));
			}
		} else {
			if (skill.isFromGuild()) {
				paragraph.add(new Paragraph(skill.getName(), new Font(FadingSunsTheme.getLineItalicFont(), fontSize)));
			} else {
				paragraph.add(new Paragraph(skill.getName(), new Font(FadingSunsTheme.getLineFont(), fontSize)));
			}
		}
		// Put number at the end.
		if (skill.isNatural()) {
			paragraph.add(new Paragraph(DEFAULT_NATURAL_SKILL_VALUE, new Font(FadingSunsTheme.getLineFont(), fontSize)));
		}

		return paragraph;
	}

	private static Paragraph createGeneralizedSkill(CharacterPlayer characterPlayer, AvailableSkill skill, BaseFont font, int fontSize) {
		Paragraph paragraph = new Paragraph();
		float usedWidth = font.getWidthPoint(skill.getName() + " []" + (skill.isNatural() ? DEFAULT_NATURAL_SKILL_VALUE : ""), fontSize);
		paragraph.add(new Paragraph(skill.getName() + " [", new Font(font, fontSize)));
		if (characterPlayer != null && characterPlayer.getSelectedSkill(skill) == null) {
			if (skill.getGeneralization() != null) {
				paragraph.add(new Paragraph(CellUtils.getSubStringFitsIn(skill.getGeneralization(), FadingSunsTheme.getHandwrittingFont(),
						FadingSunsTheme.getHandWrittingFontSize(fontSize), MAX_SKILL_COLUMN_WIDTH - usedWidth), new Font(FadingSunsTheme.getHandwrittingFont(),
						FadingSunsTheme.getHandWrittingFontSize(fontSize))));
			} else {
				paragraph.add(new Paragraph(CellUtils.getSubStringFitsIn(DEFAULT_WHITE_SPACES, font, fontSize, MAX_SKILL_COLUMN_WIDTH - usedWidth), new Font(
						font, fontSize)));
			}
		} else {
			paragraph.add(new Paragraph(CellUtils.getSubStringFitsIn(
					characterPlayer != null ? characterPlayer.getSelectedSkill(skill).getName().replace(skill.getName(), "").replace("[", "").replace("]", "")
							.trim() : DEFAULT_WHITE_SPACES, FadingSunsTheme.getHandwrittingFont(), FadingSunsTheme.getHandWrittingFontSize(fontSize),
					MAX_SKILL_COLUMN_WIDTH - usedWidth), new Font(FadingSunsTheme.getHandwrittingFont(), FadingSunsTheme.getHandWrittingFontSize(fontSize))));
		}
		paragraph.add(new Paragraph("]", new Font(font, fontSize)));
		return paragraph;
	}

}
