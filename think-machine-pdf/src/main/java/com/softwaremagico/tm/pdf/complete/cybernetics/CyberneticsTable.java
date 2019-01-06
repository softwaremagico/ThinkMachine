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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.itextpdf.text.Element;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.cybernetics.CyberneticDeviceTrait;
import com.softwaremagico.tm.character.cybernetics.SelectedCyberneticDevice;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.LateralHeaderPdfPTable;

public class CyberneticsTable extends LateralHeaderPdfPTable {
	private final static float[] WIDTHS = { 0.8f, 3f, 1f, 1f, 11f };
	private final static int ROWS = 9;
	private final static String GAP = "____________________________________________________________________________________________________";
	private final static String ELEMENT_SEPARATOR = ", ";
	private final static int NAME_COLUMN_WIDTH = 60;
	private final static int POINTS_COLUMN_WIDTH = 17;
	private final static int INCOMPATIBILITY_COLUMN_WIDTH = 17;
	private final static int TRAITS_COLUMN_WIDTH = 220;

	public CyberneticsTable(CharacterPlayer characterPlayer) {
		super(WIDTHS);

		addCell(createLateralVerticalTitle(getTranslator().getTranslatedText("cybernetics"), ROWS + 1));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("cyberneticsName")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("cyberneticsPoints")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("cyberneticsIncompatibility")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("cyberneticsTraits")));

		int addedDevices = 0;
		if (characterPlayer != null) {
			List<SelectedCyberneticDevice> devices = new ArrayList<>(characterPlayer.getAllCybernetics());
			Collections.sort(devices, new CyberneticComparatorByRequirements());
			for (SelectedCyberneticDevice device : devices) {
				addCell(createFirstElementLine(device.getRequirement() != null ? " - " + device.getName() : device.getName(), NAME_COLUMN_WIDTH,
						FadingSunsTheme.CYBERNETICS_CONTENT_FONT_SIZE));
				addCell(createElementLine(device.getPoints() + "", POINTS_COLUMN_WIDTH, FadingSunsTheme.CYBERNETICS_CONTENT_FONT_SIZE));
				addCell(createElementLine(device.getIncompatibility() + "", INCOMPATIBILITY_COLUMN_WIDTH, FadingSunsTheme.CYBERNETICS_CONTENT_FONT_SIZE));
				addCell(createElementLine(getTraitsRepresentation(device.getTraits()), TRAITS_COLUMN_WIDTH, FadingSunsTheme.CYBERNETICS_CONTENT_FONT_SIZE,
						Element.ALIGN_LEFT));
				addedDevices++;
			}
		}

		for (int i = 0; i < ROWS - addedDevices - 1; i++) {
			addCell(createEmptyElementLine(GAP, NAME_COLUMN_WIDTH));
			addCell(createEmptyElementLine(GAP, POINTS_COLUMN_WIDTH));
			addCell(createEmptyElementLine(GAP, INCOMPATIBILITY_COLUMN_WIDTH));
			addCell(createEmptyElementLine(GAP, TRAITS_COLUMN_WIDTH));
		}
	}

	private String getTraitsRepresentation(List<CyberneticDeviceTrait> traits) {
		StringBuilder stringBuilder = new StringBuilder();
		String separator = "";
		for (CyberneticDeviceTrait trait : traits) {
			stringBuilder.append(separator);
			stringBuilder.append(trait.getName());
			separator = ELEMENT_SEPARATOR;
		}
		return stringBuilder.toString();
	}

	@Override
	protected int getTitleFontSize() {
		return FadingSunsTheme.CYBERNETICS_TITLE_FONT_SIZE;
	}

	class CyberneticComparatorByRequirements implements Comparator<SelectedCyberneticDevice> {

		@Override
		public int compare(SelectedCyberneticDevice a, SelectedCyberneticDevice element) {
			if (a.getName() == null) {
				if (element.getName() == null) {
					return 0;
				}
				return -1;
			}
			if (element.getName() == null) {
				return 1;
			}
			// Use requirements to set priorities.
			String name1;
			if (a.getRequirement() != null) {
				name1 = a.getRequirement().getName() + a.getName();
			} else {
				name1 = a.getName();
			}
			String name2;
			if (element.getRequirement() != null) {
				name2 = element.getRequirement().getName() + element.getName();
			} else {
				name2 = element.getName();
			}
			return name1.compareTo(name2);
		}
	}
}
