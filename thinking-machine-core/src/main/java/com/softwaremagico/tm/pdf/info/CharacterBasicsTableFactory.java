package com.softwaremagico.tm.pdf.info;

import java.awt.Color;
import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.log.MachineLog;
import com.softwaremagico.tm.pdf.FadingSunsTheme;
import com.softwaremagico.tm.pdf.elements.BaseElement;

public class CharacterBasicsTableFactory extends BaseElement {

	public static PdfPTable getCharacterBasicsTable() {
		float[] widths = { 1f, 1f, 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);
		table.addCell(getFirstColumnTable());
		table.addCell(getSecondColumnTable());
		table.addCell(getThirdColumnTable());
		return table;
	}

	private static PdfPCell getFirstColumnTable() {
		float[] widths = { 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);

		table.addCell(createField("Nombre:"));
		table.addCell(createField("Jugador:"));
		table.addCell(createField("Género:"));
		table.addCell(createField("Edad:"));

		PdfPCell cell = new PdfPCell();
		setCellProperties(cell);

		cell.addElement(table);

		return cell;
	}

	private static PdfPCell getSecondColumnTable() {
		float[] widths = { 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);

		table.addCell(createField("Raza:"));
		table.addCell(createField("Planeta:"));
		table.addCell(createField("Alianza:"));
		table.addCell(createField("Rango:"));

		PdfPCell cell = new PdfPCell();
		setCellProperties(cell);

		cell.addElement(table);

		return cell;
	}

	private static PdfPCell getThirdColumnTable() {
		try {
			return createLogoCell();
		} catch (DocumentException | IOException e) {
			MachineLog.errorMessage(CharacterBasicsTableFactory.class.getName(), e);
		}
		return null;
	}

	private static PdfPCell createField(String text) {
		float[] widths = { 0.7f, 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);

		Color color = new Color(255, 255, 255);
		table.addCell(getCell(text, 1, Element.ALIGN_RIGHT, color));
		table.addCell(getCell("_______________", 1, Element.ALIGN_LEFT, color));

		PdfPCell cell = new PdfPCell();
		cell.addElement(table);
		setCellProperties(cell);

		return cell;
	}

	private static PdfPCell getCell(String text, int colspan, int align, Color color) {
		PdfPCell cell = getCell(text, 0, colspan, align, BaseColor.WHITE, FadingSunsTheme.getLineFont(),
				FadingSunsTheme.CHARACTER_BASICS_FONT_SIZE);
		return cell;
	}

}
