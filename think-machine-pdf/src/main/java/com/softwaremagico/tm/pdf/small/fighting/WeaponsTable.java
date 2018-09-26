package com.softwaremagico.tm.pdf.small.fighting;

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
import com.itextpdf.text.Paragraph;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.equipment.weapons.Ammunition;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.VerticalTable;

public class WeaponsTable extends VerticalTable {
	private final static float[] WIDTHS = { 3f, 1f, 1f, 1.5f, 1.5f, 1f };
	private final static int ROWS = 8;
	private final static int NAME_COLUMN_WIDTH = 60;
	private final static int GOAL_COLUMN_WIDTH = 15;
	private final static int DAMAGE_COLUMN_WIDTH = 18;
	private final static int RANGE_COLUMN_WIDTH = 30;
	private final static int SHOTS_COLUMN_WIDTH = 30;
	private final static int RATE_COLUMN_WIDTH = 30;

	public WeaponsTable(CharacterPlayer characterPlayer) {
		super(WIDTHS);
		getDefaultCell().setBorder(0);

		addCell(createTitle(getTranslator().getTranslatedText("combat"), FadingSunsTheme.CHARACTER_SMALL_WEAPONS_TITLE_FONT_SIZE));
		addCell(createSubtitleLine(getTranslator().getTranslatedText("weaponsAction"), FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE, Element.ALIGN_LEFT));
		addCell(createSubtitleLine(getTranslator().getTranslatedText("weaponGoal"), FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));
		addCell(createSubtitleLine(getTranslator().getTranslatedText("weaponDamage"), FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));
		addCell(createSubtitleLine(getTranslator().getTranslatedText("weaponRange"), FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));
		addCell(createSubtitleLine(getTranslator().getTranslatedText("weaponShots"), FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));
		addCell(createSubtitleLine(getTranslator().getTranslatedText("weaponRate"), FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));

		int added = 0;
		if (characterPlayer != null) {
			for (Weapon weapon : characterPlayer.getAllWeapons()) {
				addCell(createFirstElementLine(weapon.getName(), NAME_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_SMALL_CONTENT_FONT_SIZE));
				addCell(createElementLine((weapon.getGoal() != null ? weapon.getGoal() : ""), GOAL_COLUMN_WIDTH,
						FadingSunsTheme.WEAPONS_SMALL_CONTENT_FONT_SIZE));
				addCell(createElementLine(weapon.getDamage() + "d", DAMAGE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_SMALL_CONTENT_FONT_SIZE));
				addCell(createElementLine(weapon.getShots() == null ? characterPlayer.getStrengthDamangeModification() + "" : weapon.getStrengthOrRange(),
						RANGE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_SMALL_CONTENT_FONT_SIZE));
				addCell(createElementLine(weapon.getShots() + "", SHOTS_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_SMALL_CONTENT_FONT_SIZE));
				addCell(createElementLine(weapon.getRate(), RATE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_SMALL_CONTENT_FONT_SIZE));
				added++;

				for (Ammunition ammunition : weapon.getAmmunitions()) {
					addCell(createFirstElementLine(" - " + ammunition.getName(), NAME_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
					addCell(createElementLine((ammunition.getGoal() != null ? weapon.getGoal() : ""), GOAL_COLUMN_WIDTH,
							FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
					addCell(createElementLine(ammunition.getDamage() + "d", DAMAGE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
					addCell(createElementLine(ammunition.getStrengthOrRange(), RANGE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
					addCell(createElementLine("", SHOTS_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
					addCell(createElementLine("", RATE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
					added++;
				}
			}
		}

		for (int i = added; i < ROWS; i++) {
			for (int j = 0; j < WIDTHS.length; j++) {
				addCell(new Paragraph(""));
				addCell(new Paragraph(""));
				addCell(new Paragraph(""));
				addCell(new Paragraph(""));
				addCell(new Paragraph(""));
				addCell(new Paragraph(""));
			}
		}
	}
}
