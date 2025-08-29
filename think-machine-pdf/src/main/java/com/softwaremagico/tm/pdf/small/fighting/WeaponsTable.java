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

import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfPCell;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.equipment.weapons.Ammunition;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.VerticalTable;

public class WeaponsTable extends VerticalTable {
    private static final String GAP = "__________________";
    private static final float[] WIDTHS = {3f, 1f, 1f, 1.5f, 1.5f, 1f};
    private static final int ROWS = 8;
    private static final int NAME_COLUMN_WIDTH = 60;
    private static final int GOAL_COLUMN_WIDTH = 15;
    private static final int DAMAGE_COLUMN_WIDTH = 15;
    private static final int RANGE_COLUMN_WIDTH = 25;
    private static final int SHOTS_COLUMN_WIDTH = 25;
    private static final int RATE_COLUMN_WIDTH = 15;

    public WeaponsTable(CharacterPlayer characterPlayer) {
        super(WIDTHS);
        getDefaultCell().setBorder(0);
        final PdfPCell title = createTitle(getTranslator().getTranslatedText("combat"),
                FadingSunsTheme.CHARACTER_SMALL_WEAPONS_TITLE_FONT_SIZE);
        // To adapt height with Occultism.
        title.setMinimumHeight(20);
        addCell(title);
        addCell(createSubtitleLine(getTranslator().getTranslatedText("weaponsAction"),
                FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE, Element.ALIGN_LEFT));
        addCell(createSubtitleLine(getTranslator().getTranslatedText("weaponGoal"),
                FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));
        addCell(createSubtitleLine(getTranslator().getTranslatedText("weaponDamage"),
                FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));
        addCell(createSubtitleLine(getTranslator().getTranslatedText("weaponStrength") + "/"
                        + getTranslator().getTranslatedText("weaponRange"),
                FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));
        addCell(createSubtitleLine(getTranslator().getTranslatedText("weaponShots"),
                FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));
        addCell(createSubtitleLine(getTranslator().getTranslatedText("weaponRate"),
                FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));

        int added = 0;
        if (characterPlayer != null) {
            for (final Weapon weapon : characterPlayer.getAllWeapons()) {
                if (weapon.getWeaponDamages().isEmpty()) {
                    continue;
                }
                addCell(createFirstElementLine(weapon.getName(), NAME_COLUMN_WIDTH,
                        FadingSunsTheme.WEAPONS_SMALL_CONTENT_FONT_SIZE));
                addCell(createElementLine((weapon.getWeaponDamages().get(0).getGoal() != null ?
                                weapon.getWeaponDamages().get(0).getGoal() : ""), GOAL_COLUMN_WIDTH,
                        FadingSunsTheme.WEAPONS_SMALL_CONTENT_FONT_SIZE));
                final StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(weapon.getWeaponDamages().get(0).getDamageWithoutArea());
                if (!weapon.getWeaponDamages().get(0).getDamageWithoutArea().endsWith("d")) {
                    stringBuilder.append("d");
                }
                if (weapon.getWeaponDamages().get(0).getAreaMeters() > 0) {
                    stringBuilder.append(" ");
                    stringBuilder.append(weapon.getWeaponDamages().get(0).getAreaMeters());
                }
                addCell(createElementLine(stringBuilder.toString(), DAMAGE_COLUMN_WIDTH,
                        FadingSunsTheme.WEAPONS_SMALL_CONTENT_FONT_SIZE));
                addCell(createElementLine(weapon.getWeaponDamages().get(0).getShots() == null ? characterPlayer.getStrengthDamangeModification()
                                + "" : weapon.getWeaponDamages().get(0).getStrengthOrRange(), RANGE_COLUMN_WIDTH,
                        FadingSunsTheme.WEAPONS_SMALL_CONTENT_FONT_SIZE));
                addCell(createElementLine(weapon.getWeaponDamages().get(0).getShots() + "", SHOTS_COLUMN_WIDTH,
                        FadingSunsTheme.WEAPONS_SMALL_CONTENT_FONT_SIZE));
                addCell(createElementLine(weapon.getWeaponDamages().get(0).getRate(), RATE_COLUMN_WIDTH,
                        FadingSunsTheme.WEAPONS_SMALL_CONTENT_FONT_SIZE));
                added++;

                for (final Ammunition ammunition : weapon.getAmmunition()) {
                    addCell(createFirstElementLine(" - " + ammunition.getName(), NAME_COLUMN_WIDTH,
                            FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
                    addCell(createElementLine((ammunition.getGoal() != null ? weapon.getWeaponDamages().get(0).getGoal() : ""),
                            GOAL_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
                    addCell(createElementLine(ammunition.getDamage() + "d", DAMAGE_COLUMN_WIDTH,
                            FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
                    addCell(createElementLine(ammunition.getStrengthOrRange(), RANGE_COLUMN_WIDTH,
                            FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
                    addCell(createElementLine("", SHOTS_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
                    addCell(createElementLine("", RATE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
                    added++;
                }
            }
        }

        if (characterPlayer == null) {
            for (int i = added; i < ROWS; i++) {
                addCell(createEmptyElementLine(GAP, NAME_COLUMN_WIDTH));
                addCell(createEmptyElementLine(GAP, GOAL_COLUMN_WIDTH));
                addCell(createEmptyElementLine(GAP, DAMAGE_COLUMN_WIDTH));
                addCell(createEmptyElementLine(GAP, RANGE_COLUMN_WIDTH));
                addCell(createEmptyElementLine(GAP, SHOTS_COLUMN_WIDTH));
                addCell(createEmptyElementLine(GAP, RATE_COLUMN_WIDTH));
            }
        } else {
            for (int i = added; i < ROWS; i++) {
                for (int j = 0; j < WIDTHS.length; j++) {
                    addCell(createEmptyElementLine(FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
                }
            }
        }
    }
}
