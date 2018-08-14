package com.softwaremagico.tm.pdf.complete;

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

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.BaseFont;

public abstract class FadingSunsTheme {
	public final static String LOGO_IMAGE = "fading-suns.png";
	public final static String RIGHT_CORNER_IMAGE = "rightCorner.png";
	public final static String LEFT_CORNER_IMAGE = "leftCorner.png";
	public final static String MAIN_TITLE_IMAGE = "pageTitle.png";
	public final static String LINE_FONT_NAME = "DejaVuSansCondensed.ttf";
	public final static String LINE_FONT_ITALIC_NAME = "DejaVuSansCondensed-Oblique.ttf";
	public final static String TABLE_SUBTITLE_FONT_NAME = "DejaVuSansCondensed-Oblique.ttf";
	public final static String LINE_BOLD_FONT_NAME = "DejaVuSansCondensed-Bold.ttf";
	public final static String TITLE_FONT_NAME = "Roman Antique.ttf";
	public final static String HANDWRITTING_FONT_NAME = "ArchitectsDaughter.ttf";

	public final static int TITLE_FONT_SIZE = 18;
	public final static int CHARACTER_BASICS_FONT_SIZE = 12;
	public final static int CHARACTERISTICS_TITLE_FONT_SIZE = 14;
	public final static int CHARACTERISTICS_LINE_FONT_SIZE = 10;
	public final static int SKILLS_TITLE_FONT_SIZE = 16;
	public final static int SKILLS_LINE_FONT_SIZE = 10;
	public final static int OCCULSTISM_TITLE_FONT_SIZE = 12;
	public final static int OCCULSTISM_POWERS_TITLE_FONT_SIZE = 12;
	public final static int OCCULSTISM_POWERS_CONTENT_FONT_SIZE = 6;
	public final static int ANNOTATIONS_TITLE_FONT_SIZE = 12;
	public final static int ANNOTATIONS_SUBTITLE_FONT_SIZE = 9;
	public final static int VERTICALTABLE_TITLE_FONT_SIZE = 12;
	public final static int TABLE_LINE_FONT_SIZE = 8;
	public final static int TRAITS_FONT_SIZE = 7;
	public final static int VICTORY_POINTS_FONT_SIZE = 6;
	public final static int VICTORY_POINTS_TITLE_FONT_SIZE = 12;
	public final static int FIGHTING_TITLE_FONT_SIZE = 12;
	public final static int ARMOUR_TITLE_FONT_SIZE = 12;
	public final static int ARMOUR_CONTENT_FONT_SIZE = 7;
	public final static int SHIELD_CONTENT_FONT_SIZE = 7;
	public final static int WEAPONS_CONTENT_FONT_SIZE = 6;
	public final static int CYBERNETICS_TITLE_FONT_SIZE = 12;
	public final static int CYBERNETICS_CONTENT_FONT_SIZE = 6;
	public final static int COMBAT_ACTIONS_CONTENT_FONT_SIZE = 6;
	public final static int INFO_CONTENT_FONT_SIZE = 7;
	public final static int FOOTER_FONT_SIZE = 8;
	public final static int POSITIONS_CONTENT_FONT_SIZE = 6;
	public final static int EXPERIENCE_VALUE_FONT_SIZE = 10;
	
	public final static int CHARACTER_SMALL_BASICS_FONT_SIZE = 9;
	public final static int CHARACTER_SMALL_TITLE_FONT_SIZE = 12;
	public final static int CHARACTER_SMALL_CHARACTERISTICS_TITLE_FONT_SIZE = 8;
	public final static int CHARACTER_SMALL_CHARACTERISTICS_LINE_FONT_SIZE = 7;
	public final static int CHARACTER_SMALL_SKILLS_TITLE_FONT_SIZE = CHARACTER_SMALL_TITLE_FONT_SIZE;
	public final static int CHARACTER_SMALL_BLESSING_TITLE_FONT_SIZE = CHARACTER_SMALL_TITLE_FONT_SIZE;
	public final static int CHARACTER_SMALL_BENEFICES_TITLE_FONT_SIZE = CHARACTER_SMALL_BLESSING_TITLE_FONT_SIZE;
	public final static int CHARACTER_SMALL_ARMOR_TITLE_FONT_SIZE = CHARACTER_SMALL_TITLE_FONT_SIZE;
	public final static int CHARACTER_VITALITY_TITLE_FONT_SIZE = 11;
	public final static int CHARACTER_VITALITY_PENALTIES_TITLE_FONT_SIZE = 9;
	public final static int CHARACTER_SMALL_SKILLS_LINE_FONT_SIZE = 7;
	public final static int CHARACTER_SMALL_TABLE_LINE_FONT_SIZE = 6;
	public final static int CHARACTER_SMALL_TRAITS_FONT_SIZE = 6;
	public final static int CHARACTER_COUNTER_POINT_SIZE = 11;
	public final static int CHARACTER_SMALL_WEAPONS_TITLE_FONT_SIZE = CHARACTER_SMALL_BLESSING_TITLE_FONT_SIZE;
	public final static int CHARACTER_SMALL_OCCULTISM_TITLE_FONT_SIZE = CHARACTER_SMALL_BLESSING_TITLE_FONT_SIZE;
	public final static int CHARACTER_SMALL_OCCULTISM_LINE_FONT_SIZE = CHARACTER_SMALL_TABLE_LINE_FONT_SIZE;
	public final static int VICTORY_SMALL_POINTS_FONT_SIZE = VICTORY_POINTS_FONT_SIZE;
	public final static int WEAPONS_SMALL_CONTENT_FONT_SIZE = 6;
	public final static int CHARACTER_SMALL_CYBERNETICS_TITLE_FONT_SIZE = CHARACTER_SMALL_BLESSING_TITLE_FONT_SIZE;
	
	public final static int HANDWRITTING_DEFAULT_FONT_SIZE = 10;

	public final static int DEFAULT_MARGIN = 3;

	private static BaseFont footerFont;
	private static BaseFont lineFont;
	private static BaseFont lineItalicFont;
	private static BaseFont lineBoldFont;
	private static BaseFont titleFont;
	private static BaseFont tableSubtitleFont;
	private static BaseFont handwrittingFont;
	
	public static BaseFont getFooterFont() {
		if (footerFont == null) {
			Font font = FontFactory.getFont("/" + TITLE_FONT_NAME, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 0.5f, Font.NORMAL, BaseColor.BLACK);
			footerFont = font.getBaseFont();
		}
		return footerFont;
	}

	public static BaseFont getLineFont() {
		if (lineFont == null) {
			Font font = FontFactory.getFont("/" + LINE_FONT_NAME, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 0.8f, Font.NORMAL, BaseColor.BLACK);
			lineFont = font.getBaseFont();
		}
		return lineFont;
	}

	public static BaseFont getLineItalicFont() {
		if (lineItalicFont == null) {
			Font font = FontFactory.getFont("/" + LINE_FONT_ITALIC_NAME, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 0.8f, Font.ITALIC, BaseColor.BLACK);
			lineItalicFont = font.getBaseFont();
		}
		return lineItalicFont;
	}

	public static BaseFont getLineFontBold() {
		if (lineBoldFont == null) {
			Font font = FontFactory.getFont("/" + LINE_BOLD_FONT_NAME, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 0.8f, Font.BOLD, BaseColor.BLACK);
			lineBoldFont = font.getBaseFont();
		}
		return lineBoldFont;
	}

	public static BaseFont getTitleFont() {
		if (titleFont == null) {
			Font font = FontFactory.getFont("/" + TITLE_FONT_NAME, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 0.8f, Font.NORMAL, BaseColor.BLACK);
			titleFont = font.getBaseFont();
		}
		return titleFont;
	}

	public static BaseFont getSubtitleFont() {
		if (tableSubtitleFont == null) {
			Font font = FontFactory.getFont("/" + TABLE_SUBTITLE_FONT_NAME, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 0.8f, Font.ITALIC, BaseColor.BLACK);
			tableSubtitleFont = font.getBaseFont();
		}
		return tableSubtitleFont;
	}

	public static BaseFont getHandwrittingFont() {
		if (handwrittingFont == null) {
			Font font = FontFactory.getFont("/" + HANDWRITTING_FONT_NAME, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 0.8f, Font.NORMAL, BaseColor.BLACK);
			handwrittingFont = font.getBaseFont();
		}
		return handwrittingFont;
	}

	public static int getHandWrittingFontSize(int originalSize) {
		return originalSize - 1;
	}

}
