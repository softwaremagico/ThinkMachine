package com.softwaremagico.tm.pdf.skills.occultism;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.BaseElement;

public class OccultismTable extends PdfPTable {
	private final static int ROW_WIDTH = 68;
	private final static float[] widths = { 1f, 6f };

	public OccultismTable() {
		super(widths);
		addCell(createTitle("Ocultismo"));
		addCell(createContent());
		setWidthPercentage(100);
		setPaddingTop(0);
		setSpacingAfter(0);
		setSpacingBefore(0);
	}

	private PdfPCell createTitle(String text) {
		Font font = new Font(FadingSunsTheme.getTitleFont(), FadingSunsTheme.OCCULSTISMO_TITLE_FONT_SIZE);
		font.setColor(BaseColor.WHITE);
		Phrase content = new Phrase(text, font);
		PdfPCell titleCell = new PdfPCell(content);
		titleCell.setMinimumHeight(ROW_WIDTH);
		titleCell.setRotation(90);
		titleCell.setBackgroundColor(BaseColor.BLACK);
		titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		return titleCell;
	}

	private PdfPCell createContent() {
		float[] widths = { 3f, 1f, 1f, 3f };
		PdfPTable table = new PdfPTable(widths);
		BaseElement.setTablePropierties(table);
		table.getDefaultCell().setBorder(0);

		PdfPCell psiTitleCell = new PdfPCell(new Phrase("Psi", new Font(FadingSunsTheme.getLineFont(),
				FadingSunsTheme.CHARACTERISTICS_LINE_FONT_SIZE)));
		psiTitleCell.setBorder(0);
		psiTitleCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(psiTitleCell);

		table.addCell(createRectangle());
		table.addCell(createRectangle());

		PdfPCell eagerTitleCell = new PdfPCell(new Phrase("Ansia", new Font(FadingSunsTheme.getLineFont(),
				FadingSunsTheme.CHARACTERISTICS_LINE_FONT_SIZE)));
		eagerTitleCell.setBorder(0);
		eagerTitleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(eagerTitleCell);

		PdfPCell teurgyTitleCell = new PdfPCell(new Phrase("Teúrgia", new Font(FadingSunsTheme.getLineFont(),
				FadingSunsTheme.CHARACTERISTICS_LINE_FONT_SIZE)));
		teurgyTitleCell.setBorder(0);
		teurgyTitleCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(teurgyTitleCell);

		table.addCell(createRectangle());
		table.addCell(createRectangle());

		PdfPCell proudTitleCell = new PdfPCell(new Phrase("Orgullo", new Font(FadingSunsTheme.getLineFont(),
				FadingSunsTheme.CHARACTERISTICS_LINE_FONT_SIZE)));
		proudTitleCell.setBorder(0);
		proudTitleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(proudTitleCell);

		PdfPCell cell = new PdfPCell();
		cell.addElement(table);
		BaseElement.setCellProperties(cell);

		return cell;
	}

	private PdfPCell createRectangle() {
		PdfPCell box = new PdfPCell();
		box.setMinimumHeight(10);
		box.setBorderWidthTop(0.5f);
		box.setBorderWidthLeft(0.5f);
		box.setBorderWidthRight(0.5f);
		box.setBorderWidthBottom(0.5f);
		return box;
	}

}
