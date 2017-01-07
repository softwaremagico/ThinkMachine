package com.softwaremagico.tm.export.pdf.elements;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.softwaremagico.tm.export.pdf.FadingSunsTheme;

public class CharacteristicsTable extends BaseElement {

	public static PdfPTable getCharacterBasicsTable() {
		float[] widths = { 1f, 1f, 1f, 1f };
		PdfPTable table = new PdfPTable(widths);
		setTablePropierties(table);

		Phrase content = new Phrase("CARACTERÍSTICAS", new Font(FadingSunsTheme.getTitleFont(),
				FadingSunsTheme.TITLE_FONT_SIZE));
		PdfPCell titleCell = new PdfPCell(content);
		setCellProperties(titleCell);
		titleCell.setColspan(widths.length);
		titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		titleCell.setFixedHeight(30);
		table.addCell(titleCell);

		table.addCell(new CharacteristicColumn("Cuerpo", new String[] { "Fuerza (3)", "Destreza (3)",
				"Resistencia (3)" }));
		table.addCell(new CharacteristicColumn("Mente", new String[] { "Astucia (3)", "Percepción (3)",
				"Tecnología (3)" }));
		table.addCell(new CharacteristicColumn("Espíritu", new String[] { "Presencia (3)", "Voluntad (3)",
				"Fe (3)" }));
		table.addCell(new CharacteristicColumn("Otras",
				new String[] { "Iniciativa", "Movimiento", "Defensa" }));

		PdfPCell separator = createSeparator();
		separator.setColspan(widths.length);
		table.addCell(separator);

		return table;
	}
	// private static PdfPCell getBodyTable() {
	// float[] widths = { 1f };
	// PdfPTable table = new PdfPTable(widths);
	// setTablePropierties(table);
	//
	// table.addCell(createField("Nombre:"));
	// table.addCell(createField("Jugador:"));
	// table.addCell(createField("Género:"));
	// table.addCell(createField("Edad:"));
	//
	// PdfPCell cell = new PdfPCell();
	// setCellProperties(cell);
	//
	// cell.addElement(table);
	//
	// return cell;
	// }
	//
	// private static PdfPCell createCharacteristicPack() {
	// float[] widths = { 1f, 5f };
	// PdfPTable table = new PdfPTable(widths);
	// setTablePropierties(table);
	//
	// table.addCell(createField("Cuerpo"));
	// table.addCell(createField("Fuerza (3)"));
	// table.addCell(createField("Destreza (3)"));
	// table.addCell(createField("Resistencia (3)"));
	//
	// PdfPCell cell = new PdfPCell();
	// setCellProperties(cell);
	//
	// cell.addElement(table);
	//
	// return cell;
	// }
}
