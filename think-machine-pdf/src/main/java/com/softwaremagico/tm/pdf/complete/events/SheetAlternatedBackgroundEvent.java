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

import java.io.IOException;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.softwaremagico.tm.log.PdfExporterLog;
import com.softwaremagico.tm.pdf.complete.FadingSunsTheme;

public class SheetAlternatedBackgroundEvent extends PdfPageEventHelper {
	private final static int IMAGE_HEIGHT = 100;
	private final static int IMAGE_WIDTH = 125;
	private final static int IMAGE_BORDER = 10;
	private final static int IMAGE_TOP_BORDER = 13;

	private final static int BAR_HEIGHT = 17;

	private Image rightCorner, leftCorner, mainTitleRight, mainTitleLeft;

	@Override
	public void onOpenDocument(PdfWriter writer, Document document) {
		try {
			rightCorner = Image.getInstance(SheetAlternatedBackgroundEvent.class.getResource("/" + FadingSunsTheme.RIGHT_CORNER_IMAGE));
			rightCorner.setAbsolutePosition(document.getPageSize().getWidth() - IMAGE_WIDTH, document.getPageSize().getHeight() - IMAGE_HEIGHT - IMAGE_TOP_BORDER);
			rightCorner.scaleToFit(IMAGE_WIDTH, IMAGE_HEIGHT);
		} catch (BadElementException | IOException e) {
			PdfExporterLog.errorMessage(this.getClass().getName(), e);
		}

		try {
			leftCorner = Image.getInstance(SheetAlternatedBackgroundEvent.class.getResource("/" + FadingSunsTheme.LEFT_CORNER_IMAGE));
			// leftCorner.setAbsolutePosition(IMAGE_BORDER,
			// document.getPageSize().getHeight() - IMAGE_HEIGHT -
			// IMAGE_BORDER);
			leftCorner.setAbsolutePosition(IMAGE_BORDER, document.getPageSize().getHeight() - IMAGE_HEIGHT - IMAGE_TOP_BORDER);
			leftCorner.scaleToFit(IMAGE_WIDTH, IMAGE_HEIGHT);
		} catch (BadElementException | IOException e) {
			PdfExporterLog.errorMessage(this.getClass().getName(), e);
		}

		try {
			mainTitleRight = Image.getInstance(SheetAlternatedBackgroundEvent.class.getResource("/" + FadingSunsTheme.MAIN_TITLE_IMAGE));
			// leftCorner.setAbsolutePosition(IMAGE_BORDER,
			// document.getPageSize().getHeight() - IMAGE_HEIGHT -
			// IMAGE_BORDER);
			float barWidth = document.getPageSize().getWidth() - IMAGE_WIDTH - IMAGE_BORDER * 2;
			mainTitleRight.setAbsolutePosition(IMAGE_HEIGHT + IMAGE_BORDER * 2 + 3, document.getPageSize().getHeight() - BAR_HEIGHT - IMAGE_TOP_BORDER - 2);
			mainTitleRight.scaleAbsolute(barWidth, BAR_HEIGHT);
		} catch (BadElementException | IOException e) {
			PdfExporterLog.errorMessage(this.getClass().getName(), e);
		}

		try {
			mainTitleLeft = Image.getInstance(SheetAlternatedBackgroundEvent.class.getResource("/" + FadingSunsTheme.MAIN_TITLE_IMAGE));
			// leftCorner.setAbsolutePosition(IMAGE_BORDER,
			// document.getPageSize().getHeight() - IMAGE_HEIGHT -
			// IMAGE_BORDER);
			float barWidth = document.getPageSize().getWidth() - IMAGE_WIDTH - IMAGE_BORDER * 2;
			mainTitleLeft.setAbsolutePosition(IMAGE_BORDER * 2 + 3, document.getPageSize().getHeight() - BAR_HEIGHT - IMAGE_TOP_BORDER - 2);
			mainTitleLeft.scaleAbsolute(barWidth, BAR_HEIGHT);
		} catch (BadElementException | IOException e) {
			PdfExporterLog.errorMessage(this.getClass().getName(), e);
		}

	}

	@Override
	public void onEndPage(PdfWriter writer, Document document) {
		try {
			if (writer.getPageNumber() % 2 == 1) {
				writer.getDirectContent().addImage(leftCorner);
				writer.getDirectContent().addImage(mainTitleRight);
			}
			if (writer.getPageNumber() % 2 == 0) {
				writer.getDirectContent().addImage(rightCorner);
				writer.getDirectContent().addImage(mainTitleLeft);
			}
		} catch (DocumentException e) {
			PdfExporterLog.errorMessage(this.getClass().getName(), e);
		}
	}
}
