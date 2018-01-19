package com.softwaremagico.tm.pdf.complete.cybernetics;

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

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.cybernetics.Device;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.LateralHeaderPdfPTable;

public class CyberneticsTable extends LateralHeaderPdfPTable {
	private final static float[] WIDTHS = { 0.8f, 3f, 1f, 1f, 2f, 2f, 2f, 2f, 3f };
	private final static int ROWS = 9;
	private final static String GAP = "__________________";
	private final static int NAME_COLUMN_WIDTH = 50;
	private final static int POINTS_COLUMN_WIDTH = 15;
	private final static int INCOMPATIBILITY_COLUMN_WIDTH = 15;
	private final static int USABILITY_COLUMN_WIDTH = 32;
	private final static int QUALITY_COLUMN_WIDTH = 32;
	private final static int ACTIVATION_COLUMN_WIDTH = 32;
	private final static int APPEARENCE_COLUMN_WIDTH = 32;
	private final static int OTHERS_COLUMN_WIDTH = 40;

	public CyberneticsTable(CharacterPlayer characterPlayer) {
		super(WIDTHS);

		addCell(createLateralVerticalTitle(getTranslator().getTranslatedText("cybernetics"), ROWS + 1));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("cyberneticsName")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("cyberneticsPoints")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("cyberneticsIncompatibility")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("cyberneticsUsability")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("cyberneticsQuality")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("cyberneticsActivtation")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("cyberneticsAppearence")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("cyberneticsOthers")));

		int addedDevices = 0;
		if (characterPlayer != null) {
			for (Device device : characterPlayer.getCybernetics().getElements()) {
				addCell(createFirstElementLine(device.getName(), NAME_COLUMN_WIDTH, FadingSunsTheme.CYBERNETICS_CONTENT_FONT_SIZE));
				addCell(createElementLine(device.getPoints() + "", POINTS_COLUMN_WIDTH, FadingSunsTheme.CYBERNETICS_CONTENT_FONT_SIZE));
				addCell(createElementLine(device.getIncompatibility() + "", INCOMPATIBILITY_COLUMN_WIDTH, FadingSunsTheme.CYBERNETICS_CONTENT_FONT_SIZE));
				addCell(createElementLine(device.getUsability(), USABILITY_COLUMN_WIDTH, FadingSunsTheme.CYBERNETICS_CONTENT_FONT_SIZE));
				addCell(createElementLine(device.getQuality(), QUALITY_COLUMN_WIDTH, FadingSunsTheme.CYBERNETICS_CONTENT_FONT_SIZE));
				addCell(createElementLine(device.getActivation(), ACTIVATION_COLUMN_WIDTH, FadingSunsTheme.CYBERNETICS_CONTENT_FONT_SIZE));
				addCell(createElementLine(device.getAppearence(), APPEARENCE_COLUMN_WIDTH, FadingSunsTheme.CYBERNETICS_CONTENT_FONT_SIZE));
				addCell(createElementLine(device.getOthers(), OTHERS_COLUMN_WIDTH, FadingSunsTheme.CYBERNETICS_CONTENT_FONT_SIZE));
				addedDevices++;
			}
		}

		for (int i = 0; i < ROWS - addedDevices - 1; i++) {
			addCell(createEmptyElementLine(GAP, NAME_COLUMN_WIDTH));
			addCell(createEmptyElementLine(GAP, POINTS_COLUMN_WIDTH));
			addCell(createEmptyElementLine(GAP, INCOMPATIBILITY_COLUMN_WIDTH));
			addCell(createEmptyElementLine(GAP, USABILITY_COLUMN_WIDTH));
			addCell(createEmptyElementLine(GAP, QUALITY_COLUMN_WIDTH));
			addCell(createEmptyElementLine(GAP, ACTIVATION_COLUMN_WIDTH));
			addCell(createEmptyElementLine(GAP, APPEARENCE_COLUMN_WIDTH));
			addCell(createEmptyElementLine(GAP, OTHERS_COLUMN_WIDTH));
		}
	}

	@Override
	protected int getTitleFontSize() {
		return FadingSunsTheme.CYBERNETICS_TITLE_FONT_SIZE;
	}
}
