package com.softwaremagico.tm.pdf.info;

import com.softwaremagico.tm.pdf.elements.VerticalTable;

public class DescriptionTable extends VerticalTable {
	private final static float[] WIDTHS = { 1f };

	public DescriptionTable() {
		super(WIDTHS);

		addCell(createTitle("Descripción"));
		addCell(createElementLine("Fecha de Nacimiento _________________________"));
		addCell(createElementLine("Cabello _______________________________________"));
		addCell(createElementLine("Ojos __________________________________________"));
		addCell(createElementLine("Complexión __________________________________"));
		addCell(createElementLine("Estatura _____________________________________"));
		addCell(createElementLine("Peso _________________________________________"));
		addCell(createElementLine("Imagen ______________________________________"));
		addCell(createElementLine("______________________________________________"));
		addCell(createElementLine("______________________________________________"));
		addCell(createElementLine("______________________________________________"));
	}
}
