package com.softwaremagico.tm.pdf.complete.skills.occultism;

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

import java.util.List;
import java.util.Map.Entry;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.occultism.OccultismPath;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.occultism.OccultismPower;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.LateralHeaderPdfPTable;

public class OccultismsPowerTable extends LateralHeaderPdfPTable {
	private final static float[] WIDTHS = { 1.25f, 5f, 2f, 5f, 4f, 4f, 4f, 3f };
	private final static int ROWS = 10;
	private final static String GAP = "__________________";
	private final static int NAME_COLUMN_WIDTH = 55;
	private final static int LEVEL_COLUMN_WIDTH = 20;
	private final static int ROLL_COLUMN_WIDTH = 45;
	private final static int RANGE_COLUMN_WIDTH = 40;
	private final static int DURATION_COLUMN_WIDTH = 40;
	private final static int REQUIREMENTS_COLUMN_WIDTH = 40;
	private final static int COST_COLUMN_WIDTH = 15;

	public OccultismsPowerTable(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
		super(WIDTHS);
		addCell(createLateralVerticalTitle(getTranslator().getTranslatedText("occultismPowers"), ROWS + 1));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("occultismTablePower")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("occultismTableLevel")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("occultismTableRoll")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("occultismTableRange")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("occultismTableDuration")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("occultismTableRequirements")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("occultismTableCost")));

		int addedPowers = 0;
		if (characterPlayer != null) {
			for (Entry<String, List<String>> occultismPathEntry : characterPlayer.getSelectedPowers()
					.entrySet()) {
				OccultismPath occultismPath = OccultismPathFactory.getInstance().getElement(
						occultismPathEntry.getKey(), characterPlayer.getLanguage());
				for (String occultismPowerName : occultismPathEntry.getValue()) {
					OccultismPower occultismPower = occultismPath.getOccultismPowers().get(occultismPowerName);
					if (occultismPower.isEnabled()) {
						addCell(createFirstElementLine(occultismPower.getName(), NAME_COLUMN_WIDTH,
								FadingSunsTheme.OCCULSTISM_POWERS_CONTENT_FONT_SIZE));
						addCell(createElementLine(occultismPower.getLevel() + "", LEVEL_COLUMN_WIDTH,
								FadingSunsTheme.OCCULSTISM_POWERS_CONTENT_FONT_SIZE));
						addCell(createElementLine(occultismPower.getRoll(), ROLL_COLUMN_WIDTH,
								FadingSunsTheme.OCCULSTISM_POWERS_CONTENT_FONT_SIZE));
						addCell(createElementLine(occultismPower.getRange() != null ? occultismPower.getRange()
								.getName() : "", RANGE_COLUMN_WIDTH,
								FadingSunsTheme.OCCULSTISM_POWERS_CONTENT_FONT_SIZE));
						addCell(createElementLine(occultismPower.getDuration() != null ? occultismPower.getDuration()
								.getName() : "", DURATION_COLUMN_WIDTH,
								FadingSunsTheme.OCCULSTISM_POWERS_CONTENT_FONT_SIZE));
						addCell(createElementLine(
								occultismPower.getComponentsRepresentation().length() > 0 ? occultismPower.getComponentsRepresentation()
										: "--", REQUIREMENTS_COLUMN_WIDTH,
								FadingSunsTheme.OCCULSTISM_POWERS_CONTENT_FONT_SIZE));
						addCell(createElementLine(occultismPower.getCost() != null ? occultismPower.getCost() + ""
								: "*", COST_COLUMN_WIDTH, FadingSunsTheme.OCCULSTISM_POWERS_CONTENT_FONT_SIZE));
						addedPowers++;
					}
				}
			}
		}
		for (int i = 0; i < ROWS - addedPowers; i++) {
			addCell(createEmptyElementLine(GAP, NAME_COLUMN_WIDTH));
			addCell(createEmptyElementLine(GAP, LEVEL_COLUMN_WIDTH));
			addCell(createEmptyElementLine(GAP, ROLL_COLUMN_WIDTH));
			addCell(createEmptyElementLine(GAP, RANGE_COLUMN_WIDTH));
			addCell(createEmptyElementLine(GAP, DURATION_COLUMN_WIDTH));
			addCell(createEmptyElementLine(GAP, REQUIREMENTS_COLUMN_WIDTH));
			addCell(createEmptyElementLine(GAP, COST_COLUMN_WIDTH));
		}

	}

	@Override
	protected int getTitleFontSize() {
		return FadingSunsTheme.OCCULSTISM_POWERS_TITLE_FONT_SIZE;
	}

}
