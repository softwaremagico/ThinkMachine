package com.softwaremagico.tm.export.pdf;

import java.io.IOException;
import java.net.MalformedURLException;

import org.testng.annotations.Test;

import com.itextpdf.text.DocumentException;
import com.softwaremagico.tm.pdf.CharacterSheet;

@Test(groups = { "characterPdfGeneration" })
public class CharacterSheetCreation {
	private final static String PDF_PATH_COMBINED = System.getProperty("java.io.tmpdir") + "/FadingSuns.pdf";

	@Test
	public void combinedPdf() throws MalformedURLException, DocumentException, IOException {
		CharacterSheet sheet = new CharacterSheet();
		sheet.createFile(PDF_PATH_COMBINED);
	}

}
