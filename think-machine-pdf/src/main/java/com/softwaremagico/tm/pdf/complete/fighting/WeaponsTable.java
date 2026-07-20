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
import com.softwaremagico.tm.character.equipment.weapons.Ammunition;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponDamage;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.LateralHeaderPdfPTable;

public class WeaponsTable extends LateralHeaderPdfPTable {
    private static final float[] WIDTHS = {1.6f, 6f, 5f, 2f, 3f, 3f, 3f, 3f, 2f, 7f};
    private static final int ROWS = 6;
    private static final String GAP = "__________________";
    private static final int NAME_COLUMN_WIDTH = 65;
    private static final int ROLL_COLUMN_WIDTH = 45;
    private static final int GOAL_COLUMN_WIDTH = 15;
    private static final int DAMAGE_COLUMN_WIDTH = 30;
    private static final int RANGE_COLUMN_WIDTH = 30;
    private static final int SHOTS_COLUMN_WIDTH = 30;
    private static final int RATE_COLUMN_WIDTH = 30;
    private static final int SIZE_COLUMN_WIDTH = 15;
    private static final int OTHERS_COLUMN_WIDTH = 75;

    public WeaponsTable(CharacterPlayer characterPlayer) {
        super(WIDTHS);
        addCell(createLateralVerticalTitle(getTranslator().getTranslatedText("weapons"), ROWS + 1));
        addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weapon")));
        addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponRoll")));
        addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponGoal")));
        addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponDamage")));
        addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponStrength") + "/"
                + getTranslator().getTranslatedText("weaponRange")));
        addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponShots")));
        addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponRate")));
        addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponSize")));
        addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponsOthers")));

        int addedWeapons = 0;
        if (characterPlayer != null) {
            for (final Weapon weapon : characterPlayer.getAllWeapons()) {
                if (weapon.getWeaponDamages().isEmpty()) {
                    continue;
                }
                setDamageLine(characterPlayer, weapon.getName(), weapon, weapon.getWeaponDamages().get(0),
                        weapon.getSize() != null ? weapon.getSize().toString() : "", weapon.getWeaponOthersText());
                addedWeapons++;

                //Secondary damages.
                if (weapon.getWeaponDamages().size() > 1) {
                    for (int i = 1; i < weapon.getWeaponDamages().size(); i++) {
                        setDamageLine(characterPlayer, weapon.getWeaponDamages().get(i).getName(), weapon, weapon.getWeaponDamages().get(i), "", "");
                        addedWeapons++;
                    }
                }

                for (final Ammunition ammunition : weapon.getAmmunition()) {
                    addCell(createFirstElementLine(" - " + ammunition.getName(), NAME_COLUMN_WIDTH,
                            FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
                    addCell(createElementLine("", ROLL_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
                    addCell(createElementLine((ammunition.getGoal() != null ? weapon.getWeaponDamages().get(0).getGoal() : ""), GOAL_COLUMN_WIDTH,
                            FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
                    addCell(createElementLine(ammunition.getDamage() + "d", DAMAGE_COLUMN_WIDTH,
                            FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
                    addCell(createElementLine(ammunition.getStrengthOrRange(), RANGE_COLUMN_WIDTH,
                            FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
                    addCell(createElementLine("", RANGE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
                    addCell(createElementLine("", RATE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
                    addCell(createElementLine(ammunition.getSize() != null ? ammunition.getSize().toString() : "",
                            SIZE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
                    addCell(createElementLine("", OTHERS_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
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

    private void setDamageLine(CharacterPlayer characterPlayer, String name, Weapon weapon, WeaponDamage weaponDamage, String size, String others) {
        addCell(createFirstElementLine(name, NAME_COLUMN_WIDTH,
                FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
        addCell(createElementLine(weaponDamage.getRoll(), ROLL_COLUMN_WIDTH,
                FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
        addCell(createElementLine((weaponDamage.getGoal() != null ? weaponDamage.getGoal() : ""), GOAL_COLUMN_WIDTH,
                FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(weaponDamage.getDamageWithoutArea());
        if (!weaponDamage.getDamageWithoutArea().endsWith("d")) {
            stringBuilder.append("d");
        }
        if (weaponDamage.getAreaMeters() > 0) {
            stringBuilder.append(" ");
            stringBuilder.append(weaponDamage.getAreaMeters());
        }
        addCell(createElementLine(stringBuilder.toString(), DAMAGE_COLUMN_WIDTH,
                FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
        addCell(createElementLine(
                weaponDamage.getShots() == null ? characterPlayer.getStrengthDamangeModification() + ""
                        : weaponDamage.getStrengthOrRange(),
                RANGE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
        addCell(createElementLine(weaponDamage.getShots() == null || weaponDamage.getShots() == 0 ? "" :
                weaponDamage.getShots() + "", SHOTS_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
        addCell(createElementLine(weaponDamage.getRate(), RATE_COLUMN_WIDTH,
                FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
        addCell(createElementLine(size,
                SIZE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
        addCell(createElementLine(others, OTHERS_COLUMN_WIDTH,
                FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
    }

    @Override
    protected int getTitleFontSize() {
        return FadingSunsTheme.FIGHTING_TITLE_FONT_SIZE;
    }

}
