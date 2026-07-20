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
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.equipment.weapons.Ammunition;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponDamage;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.log.MachineLog;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.VerticalTable;

public class WeaponsTableLong extends VerticalTable {
    private static final float[] WIDTHS = {5.5f, 1.5f, 2f, 2f, 1.1f, 1f, 1f, 4f};
    private static final int ROWS = 9;
    private static final int NAME_COLUMN_WIDTH = 82;
    private static final int GOAL_COLUMN_WIDTH = 25;
    private static final int DAMAGE_COLUMN_WIDTH = 30;
    private static final int RANGE_COLUMN_WIDTH = 30;
    private static final int SHOTS_COLUMN_WIDTH = 16;
    private static final int RATE_COLUMN_WIDTH = 15;
    private static final int SIZE_COLUMN_WIDTH = 15;
    private static final int OTHERS_COLUMN_WIDTH = 60;

    public WeaponsTableLong(CharacterPlayer characterPlayer) {
        super(WIDTHS);
        getDefaultCell().setBorder(0);
        final PdfPCell title = createTitle(getTranslator().getTranslatedText("combat"),
                FadingSunsTheme.CHARACTER_SMALL_WEAPONS_TITLE_FONT_SIZE);
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
        addCell(createSubtitleLine(getTranslator().getTranslatedText("weaponSize"),
                FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));
        addCell(createSubtitleLine(getTranslator().getTranslatedText("weaponsOthers"),
                FadingSunsTheme.CHARACTER_SMALL_TABLE_LINE_FONT_SIZE));

        int added = 0;

        for (final Weapon weapon : characterPlayer.getAllWeapons()) {
            if (weapon.getWeaponDamages().isEmpty()) {
                continue;
            }
            setDamageLine(characterPlayer, weapon.getName(), weapon, weapon.getWeaponDamages().get(0));
            added++;

            //Secondary damages.
            if (weapon.getWeaponDamages().size() > 1) {
                for (int i = 1; i < weapon.getWeaponDamages().size(); i++) {
                    setDamageLine(characterPlayer, weapon.getName(), weapon, weapon.getWeaponDamages().get(i));
                    added++;
                }
            }

            added++;

            for (final Ammunition ammunition : weapon.getAmmunition()) {
                addCell(createFirstElementLine(" - " + ammunition.getName(), NAME_COLUMN_WIDTH,
                        FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
                addCell(createElementLine((ammunition.getGoal() != null ? weapon.getWeaponDamages().get(0).getGoal() : ""), GOAL_COLUMN_WIDTH,
                        FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
                addCell(createElementLine(ammunition.getDamage() + "d", DAMAGE_COLUMN_WIDTH,
                        FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
                addCell(createElementLine(ammunition.getStrengthOrRange(), RANGE_COLUMN_WIDTH,
                        FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
                addCell(createElementLine("", SHOTS_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
                addCell(createElementLine("", RATE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
                addCell(createElementLine("", SIZE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
                addCell(createElementLine("", OTHERS_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
                added++;
            }
        }

        // If has weapons. Separate maneuvers by empty line.
        if (!characterPlayer.getAllWeapons().isEmpty()) {
            addManeuvers(characterPlayer, "", "", "", "");
            added++;
        }

        if (added < ROWS) {
            addManeuvers(
                    characterPlayer,
                    getTranslator().getTranslatedText("actionStrike"),
                    "",
                    (2 + characterPlayer.getStrengthDamangeModification())
                            + getTranslator().getTranslatedText("diceAbbreviature"), "");
            added++;
        }
        if (added < ROWS) {
            addManeuvers(
                    characterPlayer,
                    getTranslator().getTranslatedText("actionGrapple"),
                    "",
                    (2 + characterPlayer.getStrengthDamangeModification())
                            + getTranslator().getTranslatedText("diceAbbreviature"),
                    getTranslator().getTranslatedText("strengthAbbreviature") + "+"
                            + getTranslator().getTranslatedText("vigorAbbreviature") + "/"
                            + getTranslator().getTranslatedText("strengthAbbreviature") + "+"
                            + getTranslator().getTranslatedText("vigorAbbreviature"));
            added++;
        }
        if (added < ROWS) {
            addManeuvers(
                    characterPlayer,
                    getTranslator().getTranslatedText("actionCharge"),
                    "",
                    (1 + characterPlayer.getStrengthDamangeModification())
                            + getTranslator().getTranslatedText("diceAbbreviature") + "/"
                            + getTranslator().getTranslatedText("meterAbbreviature"),
                    getTranslator().getTranslatedText("maximumAbbreviature") + " 4"
                            + getTranslator().getTranslatedText("diceAbbreviature"));
            added++;
        }
        if (added < ROWS) {
            addManeuvers(
                    characterPlayer,
                    getTranslator().getTranslatedText("actionKnockdown"),
                    "",
                    (3 + characterPlayer.getStrengthDamangeModification())
                            + getTranslator().getTranslatedText("diceAbbreviature"),
                    getTranslator().getTranslatedText("strengthAbbreviature") + "+"
                            + getTranslator().getTranslatedText("meleeAbbreviature") + "/"
                            + getTranslator().getTranslatedText("dexterityAbbreviature") + "+"
                            + getTranslator().getTranslatedText("vigorAbbreviature"));
            added++;
        }
        try {
            if (added < ROWS
                    && characterPlayer.getSkillTotalRanks(AvailableSkillsFactory.getInstance().getElement("fight",
                    characterPlayer.getLanguage(), characterPlayer.getModuleName())) > 4) {
                addManeuvers(
                        characterPlayer,
                        getTranslator().getTranslatedText("actionDisarm"),
                        "-4",
                        (2 + characterPlayer.getStrengthDamangeModification())
                                + getTranslator().getTranslatedText("diceAbbreviature") + "/"
                                + getTranslator().getTranslatedText("weaponAbbreviature"),
                        getTranslator().getTranslatedText("weaponDamage") + "/"
                                + getTranslator().getTranslatedText("strengthAbbreviature") + "+"
                                + getTranslator().getTranslatedText("vigorAbbreviature"));
                added++;
            }
        } catch (InvalidXmlElementException e) {
            MachineLog.errorMessage(WeaponsTableLong.class.getName(), e);
        }
        try {
            if (added < ROWS
                    && characterPlayer.getSkillTotalRanks(AvailableSkillsFactory.getInstance().getElement("fight",
                    characterPlayer.getLanguage(), characterPlayer.getModuleName())) > 4) {
                addManeuvers(
                        characterPlayer,
                        getTranslator().getTranslatedText("actionKnockout"),
                        "-4",
                        (2 + characterPlayer.getStrengthDamangeModification())
                                + getTranslator().getTranslatedText("diceAbbreviature") + "/"
                                + getTranslator().getTranslatedText("weaponAbbreviature"), getTranslator()
                                .getTranslatedText("weaponSpecial"));
                added++;
            }
        } catch (InvalidXmlElementException e) {
            MachineLog.errorMessage(WeaponsTableLong.class.getName(), e);
        }

        for (int i = added; i < ROWS; i++) {
            for (int j = 0; j < WIDTHS.length; j++) {
                addCell(createEmptyElementLine(FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
            }
        }
    }

    private void setDamageLine(CharacterPlayer characterPlayer, String name, Weapon weapon, WeaponDamage weaponDamage) {
        addCell(createFirstElementLine(weapon.getName(), NAME_COLUMN_WIDTH,
                FadingSunsTheme.WEAPONS_SMALL_CONTENT_FONT_SIZE));
        addCell(createElementLine((weaponDamage.getGoal() != null ? weaponDamage.getGoal() : ""), GOAL_COLUMN_WIDTH,
                FadingSunsTheme.WEAPONS_SMALL_CONTENT_FONT_SIZE));
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(weaponDamage.getDamageWithoutArea());
        if (!weaponDamage.getDamageWithoutArea().endsWith(getTranslator().getTranslatedText("diceAbbreviature"))) {
            stringBuilder.append(getTranslator().getTranslatedText("diceAbbreviature"));
        }
        if (weaponDamage.getAreaMeters() > 0) {
            stringBuilder.append(" ");
            stringBuilder.append(weaponDamage.getAreaMeters());
        }
        addCell(createElementLine(stringBuilder.toString(), DAMAGE_COLUMN_WIDTH,
                FadingSunsTheme.WEAPONS_SMALL_CONTENT_FONT_SIZE));
        addCell(createElementLine(weaponDamage.getShots() == null ? characterPlayer.getStrengthDamangeModification() + ""
                : weaponDamage.getStrengthOrRange(), RANGE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_SMALL_CONTENT_FONT_SIZE));
        addCell(createElementLine(weaponDamage.getShots() == null || weaponDamage.getShots() == 0 ? "" : weaponDamage.getShots() + ""
                , SHOTS_COLUMN_WIDTH,
                FadingSunsTheme.WEAPONS_SMALL_CONTENT_FONT_SIZE));
        addCell(createElementLine(weaponDamage.getRate(), RATE_COLUMN_WIDTH,
                FadingSunsTheme.WEAPONS_SMALL_CONTENT_FONT_SIZE));
        addCell(createElementLine(weapon.getSize() != null ? weapon.getSize().toString() : "", SIZE_COLUMN_WIDTH,
                FadingSunsTheme.WEAPONS_SMALL_CONTENT_FONT_SIZE));
        addCell(createElementLine(weapon.getWeaponOthersText(), OTHERS_COLUMN_WIDTH,
                FadingSunsTheme.WEAPONS_SMALL_CONTENT_FONT_SIZE));
    }

    private void addManeuvers(CharacterPlayer characterPlayer, String name, String goal, String damage, String others) {
        addCell(createFirstElementLine(name, NAME_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_SMALL_CONTENT_FONT_SIZE));
        addCell(createElementLine(goal, GOAL_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_SMALL_CONTENT_FONT_SIZE));
        addCell(createElementLine(damage, DAMAGE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_SMALL_CONTENT_FONT_SIZE));
        addCell(createElementLine("", RANGE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_SMALL_CONTENT_FONT_SIZE));
        addCell(createElementLine("", SHOTS_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_SMALL_CONTENT_FONT_SIZE));
        addCell(createElementLine("", RATE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_SMALL_CONTENT_FONT_SIZE));
        addCell(createElementLine("", SIZE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_SMALL_CONTENT_FONT_SIZE));
        addCell(createElementLine(others, OTHERS_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_SMALL_CONTENT_FONT_SIZE));
    }
}
