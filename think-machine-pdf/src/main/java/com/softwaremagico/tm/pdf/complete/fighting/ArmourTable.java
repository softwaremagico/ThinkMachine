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
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.BaseElement;
import com.softwaremagico.tm.pdf.complete.elements.LateralHeaderPdfPTable;
import com.softwaremagico.tm.rules.InvalidXmlElementException;
import com.softwaremagico.tm.rules.character.CharacterPlayer;
import com.softwaremagico.tm.rules.character.equipment.DamageTypeFactory;

public class ArmourTable extends LateralHeaderPdfPTable {
	private static final float[] WIDTHS = { 0.75f, 1f, 1f, 1f };
	private static final int ROWS = 5;
	private static final String GAP = "___________________";
	private static final int NAME_COLUMN_WIDTH = 70;

	public ArmourTable(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
		super(WIDTHS);
		getDefaultCell().setBorder(0);

		addCell(createLateralVerticalTitle(getTranslator().getTranslatedText("armor"), ROWS + 1));

		final PdfPCell nameCell;
		if (characterPlayer == null || characterPlayer.getArmour() == null) {
			nameCell = createEmptyElementLine(GAP);
		} else {
			nameCell = createElementLine(characterPlayer.getArmour().getName(), NAME_COLUMN_WIDTH,
					FadingSunsTheme.ARMOUR_CONTENT_FONT_SIZE);
		}
		nameCell.setColspan(WIDTHS.length);
		nameCell.setMinimumHeight(20);
		addCell(nameCell);
		final PdfPCell protectionCell;
		if (characterPlayer == null || characterPlayer.getArmour() == null) {
			protectionCell = createEmptyElementLine(getTranslator().getTranslatedText("armorRating") + ": ____ "
					+ getTranslator().getTranslatedText("diceAbbreviature"));
		} else {
			final Paragraph paragraph = new Paragraph();
			paragraph.add(new Paragraph(getTranslator().getTranslatedText("armorRating") + ": ", new Font(
					FadingSunsTheme.getLineFont(), FadingSunsTheme.TABLE_LINE_FONT_SIZE)));

			paragraph.add(new Paragraph(characterPlayer.getArmour().getProtection() + " ", new Font(FadingSunsTheme
					.getHandwrittingFont(), FadingSunsTheme.ARMOUR_CONTENT_FONT_SIZE)));

			paragraph.add(new Paragraph(getTranslator().getTranslatedText("diceAbbreviature"), new Font(FadingSunsTheme
					.getLineFont(), FadingSunsTheme.TABLE_LINE_FONT_SIZE)));

			protectionCell = createEmptyElementLine("");

			protectionCell.setPhrase(paragraph);
		}
		protectionCell.setColspan(WIDTHS.length);
		addCell(protectionCell);

		final PdfPCell malusCell;
		if (characterPlayer == null || characterPlayer.getArmour() == null) {
			malusCell = createEmptyElementLine(getTranslator().getTranslatedText("strengthAbbreviature") + ":__  "
					+ getTranslator().getTranslatedText("dexterityAbbreviature") + ":__  "
					+ getTranslator().getTranslatedText("enduranceAbbreviature") + ":__ "
					+ getTranslator().getTranslatedText("iniciativeAbbreviature") + ":__");
		} else {
			final Paragraph paragraph = new Paragraph();
			paragraph.add(new Paragraph(getTranslator().getTranslatedText("strengthAbbreviature") + ":", new Font(
					FadingSunsTheme.getLineFont(), FadingSunsTheme.TABLE_LINE_FONT_SIZE)));
			paragraph.add(new Paragraph(characterPlayer.getArmour().getStandardPenalizations()
					.getStrengthModification()
					+ " ", new Font(FadingSunsTheme.getHandwrittingFont(), FadingSunsTheme.ARMOUR_CONTENT_FONT_SIZE)));

			paragraph.add(new Paragraph(" " + getTranslator().getTranslatedText("dexterityAbbreviature") + ":",
					new Font(FadingSunsTheme.getLineFont(), FadingSunsTheme.TABLE_LINE_FONT_SIZE)));
			paragraph.add(new Paragraph(characterPlayer.getArmour().getStandardPenalizations()
					.getDexterityModification()
					+ " ", new Font(FadingSunsTheme.getHandwrittingFont(), FadingSunsTheme.ARMOUR_CONTENT_FONT_SIZE)));

			paragraph.add(new Paragraph(" " + getTranslator().getTranslatedText("enduranceAbbreviature") + ":",
					new Font(FadingSunsTheme.getLineFont(), FadingSunsTheme.TABLE_LINE_FONT_SIZE)));
			paragraph.add(new Paragraph(characterPlayer.getArmour().getStandardPenalizations()
					.getEnduranceModification()
					+ " ", new Font(FadingSunsTheme.getHandwrittingFont(), FadingSunsTheme.ARMOUR_CONTENT_FONT_SIZE)));

			paragraph.add(new Paragraph(" " + getTranslator().getTranslatedText("iniciativeAbbreviature") + ":",
					new Font(FadingSunsTheme.getLineFont(), FadingSunsTheme.TABLE_LINE_FONT_SIZE)));
			paragraph.add(new Paragraph(characterPlayer.getArmour().getStandardPenalizations()
					.getEnduranceModification()
					+ " ", new Font(FadingSunsTheme.getHandwrittingFont(), FadingSunsTheme.ARMOUR_CONTENT_FONT_SIZE)));

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
			addCell(getArmourProperty(getTranslator().getTranslatedText("armorImpactAbbreviature"), false));
		} else {
			addCell(getArmourProperty(
					getTranslator().getTranslatedText("armorHardAbbreviature"),
					characterPlayer
							.getArmour()
							.getDamageTypes()
							.contains(DamageTypeFactory.getInstance().getElement("hard", characterPlayer.getLanguage()))));
			addCell(getArmourProperty(
					getTranslator().getTranslatedText("armorFireAbbreviature"),
					characterPlayer
							.getArmour()
							.getDamageTypes()
							.contains(DamageTypeFactory.getInstance().getElement("fire", characterPlayer.getLanguage()))));
			addCell(getArmourProperty(
					getTranslator().getTranslatedText("armorLaserAbbreviature"),
					characterPlayer
							.getArmour()
							.getDamageTypes()
							.contains(
									DamageTypeFactory.getInstance().getElement("laser", characterPlayer.getLanguage()))));
			addCell(getArmourProperty(
					getTranslator().getTranslatedText("armorPlasmAbbreviature"),
					characterPlayer
							.getArmour()
							.getDamageTypes()
							.contains(
									DamageTypeFactory.getInstance().getElement("plasma", characterPlayer.getLanguage()))));
			addCell(getArmourProperty(
					getTranslator().getTranslatedText("armorShockAbbreviature"),
					characterPlayer
							.getArmour()
							.getDamageTypes()
							.contains(
									DamageTypeFactory.getInstance().getElement("shock", characterPlayer.getLanguage()))));
			addCell(getArmourProperty(
					getTranslator().getTranslatedText("armorImpactAbbreviature"),
					characterPlayer
							.getArmour()
							.getDamageTypes()
							.contains(
									DamageTypeFactory.getInstance().getElement("impact", characterPlayer.getLanguage()))));
		}

	}

	private PdfPTable getArmourProperty(String text, boolean selected) {
		final float[] widths = { 1f, 1f };
		final PdfPTable table = new PdfPTable(widths);
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
