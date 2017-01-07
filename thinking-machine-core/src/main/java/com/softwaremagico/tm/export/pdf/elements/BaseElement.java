package com.softwaremagico.tm.export.pdf.elements;

import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.export.pdf.FadingSunsTheme;

public class BaseElement {


	protected static PdfPCell getCell(String text, int border, int colspan, int align, BaseColor color,
			BaseFont font, float fontSize) {
		// Paragraph p = new Paragraph(text, new Font(font, fontSize));
		Phrase content = new Phrase(text, new Font(font, fontSize));
		PdfPCell cell = new PdfPCell(content);
		cell.setColspan(colspan);
		cell.setBorderWidth(border);
		cell.setHorizontalAlignment(align);
		cell.setBackgroundColor(color);

		return cell;
	}

	protected PdfPCell getCell(String text, int border, int colspan, int align,
			com.itextpdf.text.BaseColor color, String font, int fontSize, int fontType) {
		Paragraph p = new Paragraph(text, FontFactory.getFont(font, fontSize, fontType));
		PdfPCell cell = new PdfPCell(p);
		cell.setColspan(colspan);
		cell.setBorderWidth(border);
		cell.setHorizontalAlignment(align);
		cell.setBackgroundColor(color);

		return cell;
	}

	public static PdfPCell createImageCell(String path) throws DocumentException, IOException {
		Image img = Image.getInstance(path);
		PdfPCell cell = new PdfPCell(img, true);
		setCellProperties(cell);
		return cell;
	}

	public static PdfPCell createLogoCell() throws DocumentException, IOException {
		Image image = Image.getInstance(CharacterBasicsTable.class.getResource("/" + FadingSunsTheme.LOGO_IMAGE));
		PdfPCell cell = new PdfPCell(image, true);
		setCellProperties(cell);
		cell.setPaddingTop(20);
		return cell;
	}

	protected static void setCellProperties(PdfPCell cell) {
		cell.setBorder(0);
		cell.setPadding(0);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	}

	public static PdfPCell createSeparator() {
		float[] widths = { 1f };
		PdfPTable table = new PdfPTable(widths);
		table.setWidthPercentage(100);
		table.addCell(createWhiteSeparator());
		table.addCell(createBlackSeparator());
		//table.addCell(createWhiteSeparator());

		PdfPCell cell = new PdfPCell();
		cell.addElement(table);
		setCellProperties(cell);

		return cell;
	}

	public static PdfPCell createBlackSeparator() {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(BaseColor.BLACK);
		setCellProperties(cell);
		cell.setMinimumHeight(10f);
		return cell;
	}

	public static PdfPCell createWhiteSeparator() {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(BaseColor.WHITE);
		setCellProperties(cell);
		cell.setMinimumHeight(10f);
		return cell;
	}

	protected static void setTablePropierties(PdfPTable table) {
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.setWidthPercentage(100);
		table.setPaddingTop(0);
		table.setSpacingAfter(0);
		table.setSpacingBefore(0);
	}
}
