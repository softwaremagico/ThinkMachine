package com.softwaremagico.tm.pdf.complete.fighting;

import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.LateralHeaderPdfPTable;
import com.softwaremagico.tm.rules.InvalidXmlElementException;
import com.softwaremagico.tm.rules.character.CharacterPlayer;
import com.softwaremagico.tm.rules.character.combat.CombatStance;
import com.softwaremagico.tm.rules.character.combat.CombatStyle;

public class StancesTable extends LateralHeaderPdfPTable {
	private static final float[] WIDTHS = { 2f, 11f, 23f };
	private static final String GAP = "_____________________________________";
	private static final int ROWS = 5;

	private static final int NAME_COLUMN_WIDTH = 100;
	private static final int DESCRIPTION_COLUMN_WIDTH = 200;

	protected StancesTable(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
		super(WIDTHS);

		addCell(createLateralVerticalTitle(getTranslator().getTranslatedText("stances"), ROWS + 3));

		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("stanceName"), 13));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("stanceDescription"), 13));

		addCell(createFirstEmptyElementLine(getTranslator().getTranslatedText("agressiveStance")));
		addCell(createEmptyElementLine(getTranslator().getTranslatedText("agressiveStanceDescription")));

		addCell(createFirstEmptyElementLine(getTranslator().getTranslatedText("defensiveStance")));
		addCell(createEmptyElementLine(getTranslator().getTranslatedText("defensiveStanceDescription")));

		addCell(createFirstEmptyElementLine(getTranslator().getTranslatedText("fullDefensiveStance")));
		addCell(createEmptyElementLine(getTranslator().getTranslatedText("fullDefensiveStanceDescription")));

		int added = 0;
		if (characterPlayer != null) {
			for (final CombatStyle combatStyle : characterPlayer.getMeleeCombatStyles()) {
				for (final CombatStance stance : combatStyle.getCombatStances()) {
					addCell(createElementLine(stance.getName(), NAME_COLUMN_WIDTH));
					addCell(createElementLine(stance.getDescription(), DESCRIPTION_COLUMN_WIDTH));
					added++;
				}
			}
			for (final CombatStyle combatStyle : characterPlayer.getRangedCombatStyles()) {
				for (final CombatStance stance : combatStyle.getCombatStances()) {
					addCell(createElementLine(stance.getName(), NAME_COLUMN_WIDTH));
					addCell(createElementLine(stance.getDescription(), DESCRIPTION_COLUMN_WIDTH));
					added++;
				}
			}
		}

		for (int i = 0; i < ROWS - 3 - added; i++) {
			addCell(createEmptyElementLine(GAP, NAME_COLUMN_WIDTH));
			addCell(createEmptyElementLine(GAP + GAP, DESCRIPTION_COLUMN_WIDTH));
		}
	}

	@Override
	protected int getTitleFontSize() {
		return FadingSunsTheme.FIGHTING_TITLE_FONT_SIZE;
	}

}
