package com.softwaremagico.tm.pdf.perks;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.BaseElement;

public class VictoryPointsTable extends PdfPTable {
	private final static float[] WIDTHS = { 3f, 4f, 4f };

	public VictoryPointsTable() {
		super(WIDTHS);
		addCell(createTitle());

		addCell(createLine("Dado", BaseColor.WHITE, FadingSunsTheme.getLineFontBold()));
		addCell(createLine("PV", BaseColor.WHITE, FadingSunsTheme.getLineFontBold()));

		addCell(createLine("1", BaseColor.LIGHT_GRAY, FadingSunsTheme.getLineFont()));
		addCell(createLine("0", BaseColor.LIGHT_GRAY, FadingSunsTheme.getLineFont()));
		
		addCell(createLine("2-3", BaseColor.WHITE, FadingSunsTheme.getLineFont()));
		addCell(createLine("1", BaseColor.WHITE, FadingSunsTheme.getLineFont()));
		
		addCell(createLine("4-5", BaseColor.LIGHT_GRAY, FadingSunsTheme.getLineFont()));
		addCell(createLine("2", BaseColor.LIGHT_GRAY, FadingSunsTheme.getLineFont()));
		
		addCell(createLine("6-7", BaseColor.WHITE, FadingSunsTheme.getLineFont()));
		addCell(createLine("3", BaseColor.WHITE, FadingSunsTheme.getLineFont()));
		
		addCell(createLine("8-9", BaseColor.LIGHT_GRAY, FadingSunsTheme.getLineFont()));
		addCell(createLine("4", BaseColor.LIGHT_GRAY, FadingSunsTheme.getLineFont()));
		
		addCell(createLine("10-11", BaseColor.WHITE, FadingSunsTheme.getLineFont()));
		addCell(createLine("5", BaseColor.WHITE, FadingSunsTheme.getLineFont()));
		
		addCell(createLine("12-13", BaseColor.LIGHT_GRAY, FadingSunsTheme.getLineFont()));
		addCell(createLine("6", BaseColor.LIGHT_GRAY, FadingSunsTheme.getLineFont()));
		
		addCell(createLine("14-15", BaseColor.WHITE, FadingSunsTheme.getLineFont()));
		addCell(createLine("7", BaseColor.WHITE, FadingSunsTheme.getLineFont()));
		
		addCell(createLine("16-17", BaseColor.LIGHT_GRAY, FadingSunsTheme.getLineFont()));
		addCell(createLine("8", BaseColor.LIGHT_GRAY, FadingSunsTheme.getLineFont()));
		
		addCell(createLine("18-19", BaseColor.WHITE, FadingSunsTheme.getLineFont()));
		addCell(createLine("9", BaseColor.WHITE, FadingSunsTheme.getLineFont()));
		
		addCell(createLine("20", BaseColor.LIGHT_GRAY, FadingSunsTheme.getLineFont()));
		addCell(createLine("*", BaseColor.LIGHT_GRAY, FadingSunsTheme.getLineFont()));
		
	}

	private PdfPCell createTitle() {
		Font font = new Font(FadingSunsTheme.getTitleFont(), FadingSunsTheme.VICTORY_POINTS_TITLE_FONT_SIZE);
		font.setColor(BaseColor.WHITE);
		Phrase content = new Phrase("Tabla de Victoria", font);
		PdfPCell titleCell = new PdfPCell(content);
		titleCell.setPadding(0);
		titleCell.setRowspan(12);
		titleCell.setRotation(90);
		titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		titleCell.setBackgroundColor(BaseColor.BLACK);
		return titleCell;
	}

	private static PdfPCell createLine(String text, BaseColor color, BaseFont font) {
		PdfPCell cell = BaseElement.getCell(text, 0, 1, Element.ALIGN_CENTER, color,
				font, FadingSunsTheme.VICTORY_POINTS_FONT_SIZE);
		cell.setMinimumHeight(11);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

}
