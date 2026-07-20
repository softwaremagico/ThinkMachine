package com.softwaremagico.tm.pdf.complete.events;

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

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import com.softwaremagico.tm.file.Version;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;

public class FooterEvent extends PdfPageEventHelper {

	/**
	 * Adds a footer to every page
	 *
	 * @see com.lowagie.text.pdf.PdfPageEventHelper#onEndPage(com.lowagie.text.pdf.PdfWriter,
	 *      com.lowagie.text.Document)
	 */
	@Override
	public void onEndPage(PdfWriter writer, Document document) {
		if (writer.getPageNumber() % 2 == 0) {
			final PdfContentByte cb = writer.getDirectContent();
			final Phrase footer = new Phrase("Created using 'Think Machine'" + (Version.getVersion() != null ? " v" + Version.getVersion() : ""),
					new Font(FadingSunsTheme.getFooterFont(), FadingSunsTheme.FOOTER_FONT_SIZE));
			ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, footer, (document.right() - document.left()) / 2 + document.leftMargin(),
					document.bottom() + 20, 0);
		}
	}
}
