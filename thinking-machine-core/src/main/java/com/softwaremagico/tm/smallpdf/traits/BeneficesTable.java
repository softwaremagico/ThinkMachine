package com.softwaremagico.tm.smallpdf.traits;

import com.itextpdf.text.Paragraph;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.traits.Benefit;
import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.VerticalTable;

public class BeneficesTable extends VerticalTable {
	private final static int TRAIT_COLUMN_WIDTH = 55;
	private final static float[] WIDTHS = { 1f };
	private final static int ROWS = 7;

	public BeneficesTable(CharacterPlayer characterPlayer) {
		super(WIDTHS);
		getDefaultCell().setBorder(0);

		addCell(createTitle(getTranslator().getTranslatedText("beneficesTable"), FadingSunsTheme.CHARACTER_SMALL_BENEFICES_TITLE_FONT_SIZE));

		int added = 0;
		if (characterPlayer != null) {
			for (Benefit benefit : characterPlayer.getBenefits()) {
				addCell(createElementLine(benefit.getName(), TRAIT_COLUMN_WIDTH, FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));
				added++;
			}
		}

		for (int i = added; i < ROWS; i++) {
			addCell(new Paragraph(""));
		}
	}

}
