package com.softwaremagico.tm.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.BaseFont;

public abstract class FadingSunsTheme {
	public final static String LOGO_IMAGE = "fading-suns.png";
	public final static String LINE_FONT_NAME = "DejaVuSansCondensed.ttf";
	public final static String TABLE_SUBTITLE_FONT_NAME = "DejaVuSansCondensed-Oblique.ttf";
	public final static String LINE_BOLD_FONT_NAME = "DejaVuSansCondensed-Bold.ttf";
	public final static String TITLE_FONT_NAME = "Roman Antique.ttf";

	public final static int TITLE_FONT_SIZE = 18;
	public final static int CHARACTER_BASICS_FONT_SIZE = 12;
	public final static int CHARACTERISTICS_TITLE_FONT_SIZE = 14;
	public final static int CHARACTERISTICS_LINE_FONT_SIZE = 10;
	public final static int SKILLS_TITLE_FONT_SIZE = 16;
	public final static int SKILLS_LINE_FONT_SIZE = 10;
	public final static int OCCULSTISM_TITLE_FONT_SIZE = 12;
	public final static int OCCULSTISM_POWERS_TITLE_FONT_SIZE = 12;
	public final static int ANNOTATIONS_TITLE_FONT_SIZE = 12;
	public final static int ANNOTATIONS_SUBTITLE_FONT_SIZE = 9;
	public final static int PERKS_TITLE_FONT_SIZE = 12;
	public final static int TABLE_LINE_FONT_SIZE = 8;
	public final static int VICTORY_POINTS_FONT_SIZE = 6;
	public final static int VICTORY_POINTS_TITLE_FONT_SIZE = 12;
	public final static int FIGHTING_TITLE_FONT_SIZE = 12;
	
	public final static int DEFAULT_MARGIN = 3;

	private static BaseFont lineFont;
	private static BaseFont lineBoldFont;
	private static BaseFont titleFont;
	private static BaseFont tableSubtitleFont;

	public static BaseFont getLineFont() {
		if (lineFont == null) {
			Font font = FontFactory.getFont("/" + LINE_FONT_NAME, BaseFont.IDENTITY_H, BaseFont.EMBEDDED,
					0.8f, Font.NORMAL, BaseColor.BLACK);
			lineFont = font.getBaseFont();
		}
		return lineFont;
	}
	
	public static BaseFont getLineFontBold() {
		if (lineBoldFont == null) {
			Font font = FontFactory.getFont("/" + LINE_BOLD_FONT_NAME, BaseFont.IDENTITY_H, BaseFont.EMBEDDED,
					0.8f, Font.BOLD, BaseColor.BLACK);
			lineBoldFont = font.getBaseFont();
		}
		return lineBoldFont;
	}


	public static BaseFont getTitleFont() {
		if (titleFont == null) {
			Font font = FontFactory.getFont("/" + TITLE_FONT_NAME, BaseFont.IDENTITY_H, BaseFont.EMBEDDED,
					0.8f, Font.NORMAL, BaseColor.BLACK);
			titleFont = font.getBaseFont();
		}
		return titleFont;
	}
	

	public static BaseFont getSubtitleFont() {
		if (tableSubtitleFont == null) {
			Font font = FontFactory.getFont("/" + TABLE_SUBTITLE_FONT_NAME, BaseFont.IDENTITY_H, BaseFont.EMBEDDED,
					0.8f, Font.ITALIC, BaseColor.BLACK);
			tableSubtitleFont = font.getBaseFont();
		}
		return tableSubtitleFont;
	}

}
