package com.softwaremagico.tm.pdf;

/*-
 * #%L
 * The Thinking Machine (Core)
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

import java.io.IOException;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class SheetBackgroundEvent extends PdfPageEventHelper {
	private final static int IMAGE_HEIGHT = 100;
	private final static int IMAGE_WIDTH = 125;
	private final static int IMAGE_BORDER = 10;
	private Image rightCorner, leftCorner;

	@Override
	public void onOpenDocument(PdfWriter writer, Document document) {
		try {
			rightCorner = Image.getInstance(SheetBackgroundEvent.class.getResource("/" + FadingSunsTheme.RIGHT_CORNER_IMAGE));
			rightCorner.setAbsolutePosition(document.getPageSize().getWidth() - IMAGE_WIDTH, document.getPageSize().getHeight() - IMAGE_HEIGHT - IMAGE_BORDER);
			rightCorner.scaleToFit(IMAGE_WIDTH, IMAGE_HEIGHT);
		} catch (BadElementException | IOException e) {
			e.printStackTrace();
		}

		try {
			leftCorner = Image.getInstance(SheetBackgroundEvent.class.getResource("/" + FadingSunsTheme.LEFT_CORNER_IMAGE));
			leftCorner.setAbsolutePosition(IMAGE_BORDER, document.getPageSize().getHeight() - IMAGE_HEIGHT - IMAGE_BORDER);
			leftCorner.scaleToFit(IMAGE_WIDTH, IMAGE_HEIGHT);
		} catch (BadElementException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onEndPage(PdfWriter writer, Document document) {
		try {
			switch (writer.getPageNumber() % 2) {
			case 0:
				writer.getDirectContent().addImage(leftCorner);
				break;
			case 1:
				writer.getDirectContent().addImage(rightCorner);
				break;
			}
		} catch (DocumentException e) {

		}
	}
}
