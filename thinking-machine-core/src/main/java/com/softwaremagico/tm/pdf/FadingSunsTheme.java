package com.softwaremagico.tm.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.BaseFont;

public abstract class FadingSunsTheme {
	public final static String LOGO_IMAGE = "fading-suns.png";
	public final static String LINE_FONT_NAME = "DejaVuSans.ttf";
	public final static String TITLE_FONT_NAME = "Roman Antique.ttf";

	public final static int TITLE_FONT_SIZE = 18;
	public final static int CHARACTER_BASICS_FONT_SIZE = 12;
	public final static int CHARACTERISTICS_TITLE_FONT_SIZE = 14;
	public final static int CHARACTERISTICS_LINE_FONT_SIZE = 10;
	public final static int SKILLS_TITLE_FONT_SIZE = 16;
	public final static int SKILLS_LINE_FONT_SIZE = 10;
	public final static int OCCULSTISMO_TITLE_FONT_SIZE = 12;
	
	public final static int DEFAULT_MARGIN = 3;

	private static BaseFont lineFont;
	private static BaseFont titleFont;

	public static BaseFont getLineFont() {
		if (lineFont == null) {
			Font font = FontFactory.getFont("/" + LINE_FONT_NAME, BaseFont.IDENTITY_H, BaseFont.EMBEDDED,
					0.8f, Font.NORMAL, BaseColor.BLACK);
			lineFont = font.getBaseFont();
		}
		return lineFont;
	}

	public static BaseFont getTitleFont() {
		if (titleFont == null) {
			Font font = FontFactory.getFont("/" + TITLE_FONT_NAME, BaseFont.IDENTITY_H, BaseFont.EMBEDDED,
					0.8f, Font.NORMAL, BaseColor.BLACK);
			titleFont = font.getBaseFont();
		}
		return titleFont;
	}

}
