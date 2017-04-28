package com.softwaremagico.tm.pdf.fighting;

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.LateralHeaderPdfPTable;

public class StancesTable extends LateralHeaderPdfPTable {
	private final static float[] WIDTHS = { 2f, 11f, 23f };
	private final static String GAP = "_____________________________________";
	private final static int ROWS = 5;

	private final static int NAME_COLUMN_WIDTH = 80;
	private final static int DESCRIPTION_COLUMN_WIDTH = 150;

	protected StancesTable(CharacterPlayer characterPlayer) {
		super(WIDTHS);

		addCell(createLateralVerticalTitle(
				getTranslator().getTranslatedText("stances"), ROWS + 3));

		addCell(createTableSubtitleElement(getTranslator().getTranslatedText(
				"stanceName"), 13));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText(
				"stanceDescription"), 13));

		addCell(createEmptyElementLine(getTranslator().getTranslatedText(
				"agressiveStance")));
		addCell(createEmptyElementLine(getTranslator().getTranslatedText(
				"agressiveStanceDescription")));

		addCell(createEmptyElementLine(getTranslator().getTranslatedText(
				"defensiveStance")));
		addCell(createEmptyElementLine(getTranslator().getTranslatedText(
				"defensiveStanceDescription")));

		addCell(createEmptyElementLine(getTranslator().getTranslatedText(
				"fullDefensiveStance")));
		addCell(createEmptyElementLine(getTranslator().getTranslatedText(
				"fullDefensiveStanceDescription")));

		for (int i = 0; i < ROWS - 3; i++) {
			addCell(createEmptyElementLine(GAP, NAME_COLUMN_WIDTH));
			addCell(createEmptyElementLine(GAP+GAP, DESCRIPTION_COLUMN_WIDTH));
		}
	}

	@Override
	protected int getTitleFontSize() {
		return FadingSunsTheme.FIGHTING_TITLE_FONT_SIZE;
	}

}
