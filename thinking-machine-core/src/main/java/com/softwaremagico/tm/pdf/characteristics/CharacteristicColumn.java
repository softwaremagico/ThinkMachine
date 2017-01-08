package com.softwaremagico.tm.pdf.characteristics;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.BaseElement;
import com.softwaremagico.tm.pdf.elements.CellPaddingEvent;

public class CharacteristicColumn extends PdfPTable {
	private final static int ROW_WIDTH = 60;
	private final static float[] widths = { 1f, 5f };

	public CharacteristicColumn(String title, String[] content) {
		super(widths);
		addCell(createTitle(title, content.length));
		addCell(createContent(content));
	}

	private PdfPCell createTitle(String text, int rowspan) {
		Font font = new Font(FadingSunsTheme.getTitleFont(), FadingSunsTheme.CHARACTERISTICS_TITLE_FONT_SIZE);
		font.setColor(BaseColor.WHITE);
		Phrase content = new Phrase(text, font);
		PdfPCell titleCell = new PdfPCell(content);
		titleCell.setMinimumHeight(ROW_WIDTH);
		titleCell.setRotation(90);
		titleCell.setRowspan(rowspan);
		titleCell.setBackgroundColor(BaseColor.BLACK);
		titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		return titleCell;
	}

	private PdfPCell createContent(String[] content) {
		float[] widths = { 3f, 1f , 0.1f};
		PdfPTable table = new PdfPTable(widths);
		BaseElement.setTablePropierties(table);
		table.getDefaultCell().setBorder(0);

		for (String text : content) {
			PdfPCell characteristicTitle = new PdfPCell(new Phrase(text, new Font(
					FadingSunsTheme.getLineFont(), FadingSunsTheme.CHARACTERISTICS_LINE_FONT_SIZE)));
			characteristicTitle.setBorder(0);
			characteristicTitle.setMinimumHeight(ROW_WIDTH / content.length);
			table.addCell(characteristicTitle);

			// Rectangle
			PdfPCell box = new PdfPCell();
			box.setMinimumHeight(10);
			box.setBorder(0);
			box.setCellEvent(new CellPaddingEvent());
			table.addCell(box);
			
			//Margin
			PdfPCell margin = new PdfPCell();
			margin.setBorder(0);
			table.addCell(margin);
		}

		PdfPCell cell = new PdfPCell();
		cell.addElement(table);
		BaseElement.setCellProperties(cell);

		return cell;
	}

}
