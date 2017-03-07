package com.softwaremagico.tm.pdf.skills;

import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.pdf.FadingSunsTheme;

public class LearnedSkillsSmallTable extends SkillsTable {

	public static PdfPTable getSkillsTable(CharacterPlayer characterPlayer, String language) {
		float[] widths = { 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);
		table.getDefaultCell().setBorder(0);
		table.addCell(getSkillsColumnTable(characterPlayer, language));
		return table;
	}

	private static PdfPCell getSkillsColumnTable(CharacterPlayer characterPlayer, String language) {
		float[] widths = { 4f, 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);
		table.getDefaultCell().setBorder(0);

		table.addCell(createTitle(getTranslator().getTranslatedText("learnedSkills"), FadingSunsTheme.CHARACTER_SMALL_SKILLS_TITLE_FONT_SIZE));

		if (characterPlayer != null) {
			for (AvailableSkill skill : characterPlayer.getLearnedSkills()) {
				table.addCell(createSkillElement(characterPlayer, skill, FadingSunsTheme.CHARACTER_SMALL_SKILLS_LINE_FONT_SIZE));
				table.addCell(createSkillValue(characterPlayer.getSkillValue(skill), characterPlayer.isSkillSpecial(skill),
						FadingSunsTheme.CHARACTER_SMALL_SKILLS_LINE_FONT_SIZE));
			}
		}

		PdfPCell cell = new PdfPCell();
		setCellProperties(cell);

		cell.addElement(table);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		return cell;
	}

}
