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

import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.equipment.DamageType;
import com.softwaremagico.tm.character.equipment.weapons.Ammunition;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.LateralHeaderPdfPTable;

public class WeaponsTable extends LateralHeaderPdfPTable {
	private final static float[] WIDTHS = { 1.6f, 6f, 5f, 2f, 3f, 3f, 3f, 3f, 2f, 7f };
	private final static int ROWS = 6;
	private final static String GAP = "__________________";
	private final static int NAME_COLUMN_WIDTH = 65;
	private final static int ROLL_COLUMN_WIDTH = 45;
	private final static int GOAL_COLUMN_WIDTH = 15;
	private final static int DAMAGE_COLUMN_WIDTH = 30;
	private final static int RANGE_COLUMN_WIDTH = 30;
	private final static int SHOTS_COLUMN_WIDTH = 30;
	private final static int RATE_COLUMN_WIDTH = 30;
	private final static int SIZE_COLUMN_WIDTH = 15;
	private final static int OTHERS_COLUMN_WIDTH = 75;

	public WeaponsTable(CharacterPlayer characterPlayer) {
		super(WIDTHS);
		addCell(createLateralVerticalTitle(getTranslator().getTranslatedText("weapons"), ROWS + 1));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weapon")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponRoll")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponGoal")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponDamage")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponRange")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponShots")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponRate")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponSize")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponsOthers")));

		int addedWeapons = 0;
		if (characterPlayer != null) {
			for (Weapon weapon : characterPlayer.getAllWeapons()) {
				addCell(createFirstElementLine(weapon.getName(), NAME_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
				addCell(createElementLine(weapon.getRoll(), ROLL_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
				addCell(createElementLine((weapon.getGoal() != null ? weapon.getGoal() : ""), GOAL_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
				addCell(createElementLine(weapon.getDamage() + "d", DAMAGE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
				addCell(createElementLine(weapon.getShots() == null ? characterPlayer.getStrengthDamangeModification() + "" : weapon.getStrengthOrRange(),
						RANGE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
				addCell(createElementLine(weapon.getShots() + "", SHOTS_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
				addCell(createElementLine(weapon.getRate(), RATE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
				addCell(createElementLine(weapon.getSize().toString(), SIZE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
				addCell(createElementLine(getWeaponOthers(weapon), OTHERS_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));

				addedWeapons++;

				for (Ammunition ammunition : weapon.getAmmunitions()) {
					addCell(createFirstElementLine(" - " + ammunition.getName(), NAME_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
					addCell(createElementLine("", ROLL_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
					addCell(createElementLine((ammunition.getGoal() != null ? weapon.getGoal() : ""), GOAL_COLUMN_WIDTH,
							FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
					addCell(createElementLine(ammunition.getDamage() + "d", DAMAGE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
					addCell(createElementLine(ammunition.getStrengthOrRange(), RANGE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
					addCell(createElementLine("",  RANGE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
					addCell(createElementLine("",  RATE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
					addCell(createElementLine("",  SIZE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
					addCell(createElementLine("",  OTHERS_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
					addedWeapons++;
				}
			}
		}

		for (int i = 0; i < ROWS - addedWeapons; i++) {
			addCell(createEmptyElementLine(GAP, NAME_COLUMN_WIDTH));
			addCell(createEmptyElementLine(GAP, ROLL_COLUMN_WIDTH));
			addCell(createEmptyElementLine(GAP, GOAL_COLUMN_WIDTH));
			addCell(createEmptyElementLine(GAP, DAMAGE_COLUMN_WIDTH));
			addCell(createEmptyElementLine(GAP, RANGE_COLUMN_WIDTH));
			addCell(createEmptyElementLine(GAP, SHOTS_COLUMN_WIDTH));
			addCell(createEmptyElementLine(GAP, RATE_COLUMN_WIDTH));
			addCell(createEmptyElementLine(GAP, SIZE_COLUMN_WIDTH));
			addCell(createEmptyElementLine(GAP, OTHERS_COLUMN_WIDTH));
		}
	}

	private String getWeaponOthers(Weapon weapon) {
		// Damage types
		StringBuilder stringBuilder = new StringBuilder();
		for (DamageType damageType : weapon.getDamageTypes()) {
			if (stringBuilder.length() > 0) {
				stringBuilder.append(", ");
			}
			stringBuilder.append(damageType.getName());
		}

		// Others
		if (weapon.getSpecial() != null && !weapon.getSpecial().isEmpty()) {
			if (stringBuilder.length() > 0) {
				stringBuilder.append(" / ");
			}
			stringBuilder.append(weapon.getSpecial());
		}

		return stringBuilder.toString();

	}

	@Override
	protected int getTitleFontSize() {
		return FadingSunsTheme.FIGHTING_TITLE_FONT_SIZE;
	}

}
