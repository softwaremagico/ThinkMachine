package com.softwaremagico.tm.pdf.fighting;

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.LateralHeaderPdfPTable;

public class ExperienceTable extends LateralHeaderPdfPTable {
	private final static float[] WIDTHS = { 1f, 4f };

	protected ExperienceTable(CharacterPlayer characterPlayer) {
		super(WIDTHS);

		addCell(createLateralVerticalTitle(
				getTranslator().getTranslatedText("experience"), 1));
		addCell(createEmptyElementLine(""));

	}

	@Override
	protected int getTitleFontSize() {
		return FadingSunsTheme.FIGHTING_TITLE_FONT_SIZE;
	}

}
