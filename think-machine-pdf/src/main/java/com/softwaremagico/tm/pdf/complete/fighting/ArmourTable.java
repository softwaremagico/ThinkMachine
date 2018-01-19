package com.softwaremagico.tm.pdf.complete.fighting;

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

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.pdf.complete.elements.LateralHeaderPdfPTable;

public class ArmourTable extends LateralHeaderPdfPTable {
	private final static float[] WIDTHS = { 0.75f, 1f, 1f, 1f };
	private final static int ROWS = 5;
	private final static String GAP = "___________________";
	private final static int NAME_COLUMN_WIDTH = 70;

	public ArmourTable(CharacterPlayer characterPlayer) {
		super(WIDTHS);
		getDefaultCell().setBorder(0);

		addCell(createLateralVerticalTitle(getTranslator().getTranslatedText("armor"), ROWS + 1));

		PdfPCell nameCell;
		if (characterPlayer == null || characterPlayer.getArmour() == null) {
			nameCell = createEmptyElementLine(GAP);
		} else {
			nameCell = createElementLine(characterPlayer.getArmour().getName(), NAME_COLUMN_WIDTH, FadingSunsTheme.ARMOUR_CONTENT_FONT_SIZE);
		}
		nameCell.setColspan(WIDTHS.length);
		nameCell.setMinimumHeight(20);
		addCell(nameCell);
		PdfPCell protectionCell;
		if (characterPlayer == null || characterPlayer.getArmour() == null) {
			protectionCell = createEmptyElementLine(getTranslator().getTranslatedText("armorRating") + ": ____ "
					+ getTranslator().getTranslatedText("diceAbbreviature"));
		} else {
			Paragraph paragraph = new Paragraph();
			paragraph.add(new Paragraph(getTranslator().getTranslatedText("armorRating") + ": ", new Font(FadingSunsTheme.getLineFont(),
					FadingSunsTheme.TABLE_LINE_FONT_SIZE)));

			paragraph.add(new Paragraph(characterPlayer.getArmour().getProtection() + " ", new Font(FadingSunsTheme.getHandwrittingFont(),
					FadingSunsTheme.ARMOUR_CONTENT_FONT_SIZE)));

			paragraph.add(new Paragraph(getTranslator().getTranslatedText("diceAbbreviature"), new Font(FadingSunsTheme.getLineFont(),
					FadingSunsTheme.TABLE_LINE_FONT_SIZE)));

			protectionCell = createEmptyElementLine("");

			protectionCell.setPhrase(paragraph);
		}
		protectionCell.setColspan(WIDTHS.length);
		addCell(protectionCell);

		PdfPCell malusCell;
		if (characterPlayer == null || characterPlayer.getArmour() == null) {
			malusCell = createEmptyElementLine(getTranslator().getTranslatedText("strengthAbbreviature") + ":__  "
					+ getTranslator().getTranslatedText("dexterityAbbreviature") + ":__  "  + getTranslator().getTranslatedText("enduranceAbbreviature") + ":__ " + getTranslator().getTranslatedText("iniciativeAbbreviature")
					+ ":__");
		} else {
			Paragraph paragraph = new Paragraph();
			paragraph.add(new Paragraph(getTranslator().getTranslatedText("strengthAbbreviature") + ":", new Font(FadingSunsTheme.getLineFont(),
					FadingSunsTheme.TABLE_LINE_FONT_SIZE)));
			paragraph.add(new Paragraph(characterPlayer.getArmour().getStrengthBonus() + " ", new Font(FadingSunsTheme.getHandwrittingFont(),
					FadingSunsTheme.ARMOUR_CONTENT_FONT_SIZE)));

			paragraph.add(new Paragraph(" " + getTranslator().getTranslatedText("dexterityAbbreviature") + ":", new Font(FadingSunsTheme.getLineFont(),
					FadingSunsTheme.TABLE_LINE_FONT_SIZE)));
			paragraph.add(new Paragraph(characterPlayer.getArmour().getDexterityBonus() + " ", new Font(FadingSunsTheme.getHandwrittingFont(),
					FadingSunsTheme.ARMOUR_CONTENT_FONT_SIZE)));

			paragraph.add(new Paragraph(" " + getTranslator().getTranslatedText("enduranceAbbreviature") + ":", new Font(FadingSunsTheme.getLineFont(),
					FadingSunsTheme.TABLE_LINE_FONT_SIZE)));
			paragraph.add(new Paragraph(characterPlayer.getArmour().getEnduranceBonus() + " ", new Font(FadingSunsTheme.getHandwrittingFont(),
					FadingSunsTheme.ARMOUR_CONTENT_FONT_SIZE)));
			
			paragraph.add(new Paragraph(" " + getTranslator().getTranslatedText("iniciativeAbbreviature") + ":", new Font(FadingSunsTheme.getLineFont(),
					FadingSunsTheme.TABLE_LINE_FONT_SIZE)));
			paragraph.add(new Paragraph(characterPlayer.getArmour().getInitiativeBonus() + " ", new Font(FadingSunsTheme.getHandwrittingFont(),
					FadingSunsTheme.ARMOUR_CONTENT_FONT_SIZE)));

			malusCell = createEmptyElementLine("");

			malusCell.setPhrase(paragraph);
		}
		malusCell.setColspan(WIDTHS.length);
		addCell(malusCell);

		if (characterPlayer == null || characterPlayer.getArmour() == null) {
			addCell(getArmourProperty(getTranslator().getTranslatedText("armorHardAbbreviature"), false));
			addCell(getArmourProperty(getTranslator().getTranslatedText("armorFireAbbreviature"), false));
			addCell(getArmourProperty(getTranslator().getTranslatedText("armorLaserAbbreviature"), false));
			addCell(getArmourProperty(getTranslator().getTranslatedText("armorPlasmAbbreviature"), false));
			addCell(getArmourProperty(getTranslator().getTranslatedText("armorShockAbbreviature"), false));
			addCell(getArmourProperty(getTranslator().getTranslatedText("armorElectricAbbreviature"), false));
		} else {
			addCell(getArmourProperty(getTranslator().getTranslatedText("armorHardAbbreviature"), characterPlayer.getArmour().isHard()));
			addCell(getArmourProperty(getTranslator().getTranslatedText("armorFireAbbreviature"), characterPlayer.getArmour().isFire()));
			addCell(getArmourProperty(getTranslator().getTranslatedText("armorLaserAbbreviature"), characterPlayer.getArmour().isLaser()));
			addCell(getArmourProperty(getTranslator().getTranslatedText("armorPlasmAbbreviature"), characterPlayer.getArmour().isPlasma()));
			addCell(getArmourProperty(getTranslator().getTranslatedText("armorShockAbbreviature"), characterPlayer.getArmour().isShock()));
			addCell(getArmourProperty(getTranslator().getTranslatedText("armorElectricAbbreviature"), characterPlayer.getArmour().isElectrical()));
		}

	}

	private PdfPTable getArmourProperty(String text, boolean selected) {
		float[] widths = { 1f, 1f };
		PdfPTable table = new PdfPTable(widths);
		BaseElement.setTablePropierties(table);
		table.getDefaultCell().setBorder(0);
		table.getDefaultCell().setPadding(0);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

		if (!selected) {
			table.addCell(createRectangle());
		} else {
			table.addCell(createRectangle("X"));
		}
		table.addCell(createEmptyElementLine(text));

		return table;
	}

	@Override
	protected int getTitleFontSize() {
		return FadingSunsTheme.ARMOUR_TITLE_FONT_SIZE;
	}

}
