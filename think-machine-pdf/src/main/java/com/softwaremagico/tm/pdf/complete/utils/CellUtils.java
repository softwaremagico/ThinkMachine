package com.softwaremagico.tm.pdf.complete.utils;

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
