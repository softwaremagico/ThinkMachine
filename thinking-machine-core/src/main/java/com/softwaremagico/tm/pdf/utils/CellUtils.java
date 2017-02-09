package com.softwaremagico.tm.pdf.utils;

import com.itextpdf.text.pdf.BaseFont;

public class CellUtils {

	public static String getSubStringFitsIn(String originalText, BaseFont font, int fontSize, float width) {
		String text = originalText;

		while (!fitsIn(text, font, fontSize, width) && text.length() > 0) {
			text = text.substring(0, text.length() - 1);
		}
		return text;
	}

	public static boolean fitsIn(String text, BaseFont font, int fontSize, float width) {
		return font.getWidthPoint(text, fontSize) < width;
	}
}
