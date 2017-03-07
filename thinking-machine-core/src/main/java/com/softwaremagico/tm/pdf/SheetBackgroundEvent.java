package com.softwaremagico.tm.pdf;

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
