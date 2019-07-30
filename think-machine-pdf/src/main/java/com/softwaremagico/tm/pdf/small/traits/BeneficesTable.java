package com.softwaremagico.tm.pdf.small.traits;

import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.VerticalTable;
import com.softwaremagico.tm.rules.InvalidXmlElementException;
import com.softwaremagico.tm.rules.character.CharacterPlayer;
import com.softwaremagico.tm.rules.character.benefices.AvailableBenefice;

public class BeneficesTable extends VerticalTable {
	private static final String GAP = "__________________";
	private static final int TRAIT_COLUMN_WIDTH = 60;
	private static final float[] WIDTHS = { 1f };
	private static final int ROWS = 10;

	public BeneficesTable(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
		super(WIDTHS);
		getDefaultCell().setBorder(0);

		addCell(createTitle(getTranslator().getTranslatedText("beneficesTable"),
				FadingSunsTheme.CHARACTER_SMALL_BENEFICES_TITLE_FONT_SIZE));

		int added = 0;
		if (characterPlayer != null) {
			for (final AvailableBenefice benefit : characterPlayer.getAllBenefices()) {
				addCell(createElementLine(benefit.getName(), TRAIT_COLUMN_WIDTH,
						FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));
				added++;
			}
		}

		if (characterPlayer == null) {
			for (int i = added; i < ROWS; i++) {
				addCell(createEmptyElementLine(GAP, TRAIT_COLUMN_WIDTH));
			}
		} else {
			for (int i = added; i < ROWS; i++) {
				addCell(createEmptyElementLine(FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));
			}
		}
	}

}
