package com.softwaremagico.tm.pdf.info;

import com.softwaremagico.tm.pdf.elements.VerticalTable;

public class DescriptionTable extends VerticalTable {
	private final static float[] WIDTHS = { 1f };

	public DescriptionTable() {
		super(WIDTHS);

		addCell(createTitle("Descripción"));
		addCell(createLine("Fecha de Nacimiento _________________________"));
		addCell(createLine("Cabello _______________________________________"));
		addCell(createLine("Ojos __________________________________________"));
		addCell(createLine("Complexión __________________________________"));
		addCell(createLine("Estatura _____________________________________"));
		addCell(createLine("Peso _________________________________________"));
		addCell(createLine("Imagen ______________________________________"));
		addCell(createLine("______________________________________________"));
		addCell(createLine("______________________________________________"));
		addCell(createLine("______________________________________________"));
	}
}
