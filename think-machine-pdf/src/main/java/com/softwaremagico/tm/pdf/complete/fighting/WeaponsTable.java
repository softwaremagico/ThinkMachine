package com.softwaremagico.tm.pdf.complete.fighting;

import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;
import com.softwaremagico.tm.pdf.complete.elements.LateralHeaderPdfPTable;
import com.softwaremagico.tm.rules.character.CharacterPlayer;
import com.softwaremagico.tm.rules.character.equipment.DamageType;
import com.softwaremagico.tm.rules.character.equipment.weapons.Ammunition;
import com.softwaremagico.tm.rules.character.equipment.weapons.Weapon;

public class WeaponsTable extends LateralHeaderPdfPTable {
	private static final float[] WIDTHS = { 1.6f, 6f, 5f, 2f, 3f, 3f, 3f, 3f, 2f, 7f };
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
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponRange")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponShots")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponRate")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponSize")));
		addCell(createTableSubtitleElement(getTranslator().getTranslatedText("weaponsOthers")));

		int addedWeapons = 0;
		if (characterPlayer != null) {
			for (final Weapon weapon : characterPlayer.getAllWeapons()) {
				addCell(createFirstElementLine(weapon.getName(), NAME_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
				addCell(createElementLine(weapon.getRoll(), ROLL_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
				addCell(createElementLine((weapon.getGoal() != null ? weapon.getGoal() : ""), GOAL_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
				final StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append(weapon.getDamageWithoutArea());
				if (!weapon.getDamageWithoutArea().endsWith("d")) {
					stringBuilder.append("d");
				}
				if (weapon.getAreaMeters() > 0) {
					stringBuilder.append(" ");
					stringBuilder.append(weapon.getAreaMeters());
				}
				addCell(createElementLine(stringBuilder.toString(), DAMAGE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
				addCell(createElementLine(weapon.getShots() == null ? characterPlayer.getStrengthDamangeModification() + "" : weapon.getStrengthOrRange(),
						RANGE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
				addCell(createElementLine(weapon.getShots() + "", SHOTS_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
				addCell(createElementLine(weapon.getRate(), RATE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
				addCell(createElementLine(weapon.getSize() != null ? weapon.getSize().toString() : "", SIZE_COLUMN_WIDTH,
						FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
				addCell(createElementLine(getWeaponOthers(weapon), OTHERS_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));

				addedWeapons++;

				for (final Ammunition ammunition : weapon.getAmmunitions()) {
					addCell(createFirstElementLine(" - " + ammunition.getName(), NAME_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
					addCell(createElementLine("", ROLL_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
					addCell(createElementLine((ammunition.getGoal() != null ? weapon.getGoal() : ""), GOAL_COLUMN_WIDTH,
							FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
					addCell(createElementLine(ammunition.getDamage() + "d", DAMAGE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
					addCell(createElementLine(ammunition.getStrengthOrRange(), RANGE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
					addCell(createElementLine("", RANGE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
					addCell(createElementLine("", RATE_COLUMN_WIDTH, FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
					addCell(createElementLine(ammunition.getSize() != null ? ammunition.getSize().toString() : "", SIZE_COLUMN_WIDTH,
							FadingSunsTheme.WEAPONS_CONTENT_FONT_SIZE));
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

	private String getWeaponOthers(Weapon weapon) {
		// Damage types
		final StringBuilder stringBuilder = new StringBuilder();
		for (final DamageType damageType : weapon.getDamageTypes()) {
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
