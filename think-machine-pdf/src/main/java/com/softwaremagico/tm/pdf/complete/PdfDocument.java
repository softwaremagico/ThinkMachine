package com.softwaremagico.tm.pdf.complete;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;
import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.log.PdfExporterLog;
import com.softwaremagico.tm.pdf.complete.events.FooterEvent;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/*
 * #%L
 * KendoTournamentGenerator
 * %%
 * Copyright (C) 2008 - 2012 Softwaremagico
 * %%
 * This software is designed by Jorge Hortelano Otero. Jorge Hortelano Otero
 * <softwaremagico@gmail.com> Valencia (Spain).
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

public abstract class PdfDocument {
    private final int rightMargin = 30;
    private final int leftMargin = 30;
    private final int topMargin = 30;
    private final int bottomMargin = 30;
    private final String language;
    private final String moduleName;

    protected PdfDocument(String language, String moduleName) {
        this.language = language;
        this.moduleName = moduleName;
    }

    protected Document addMetaData(Document document) {
        document.addTitle("Fading Suns Character Sheet");
        document.addAuthor("Software Magico");
        document.addCreator("Think Machine");
        document.addSubject("RPG");
        document.addKeywords("RPG, Fading Suns, FS, " + this.language);
        document.addCreationDate();
        return document;
    }

    private void generatePDF(Document document, PdfWriter writer)
            throws EmptyPdfBodyException, InvalidXmlElementException, DocumentException {
        this.addMetaData(document);
        document.open();
        this.createContent(document);
        document.close();
    }

    protected abstract void createContent(Document document) throws InvalidXmlElementException, DocumentException;

    protected void addEvent(PdfWriter writer) {
        writer.setPageEvent(new FooterEvent());
    }

    /**
     * Pdf as byte array. Be careful with big PDF files.
     *
     * @return
     * @throws EmptyPdfBodyException
     * @throws DocumentException
     * @throws InvalidXmlElementException
     */
    public final byte[] generate() throws EmptyPdfBodyException, DocumentException, InvalidXmlElementException {
        final Document document = new Document(this.getPageSize(), this.rightMargin, this.leftMargin, this.topMargin,
                this.bottomMargin);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final PdfWriter writer = PdfWriter.getInstance(document, baos);
        this.addEvent(writer);
        this.generatePDF(document, writer);
        return baos.toByteArray();

    }

    protected abstract void addDocumentWriterEvents(PdfWriter writer);

    public int createFile(String path) {
        if (!path.endsWith(".pdf")) {
            path += ".pdf";
        }

        // DIN A6 105 x 148 mm
        final Document document = new Document(this.getPageSize(), this.rightMargin, this.leftMargin, this.topMargin,
                this.bottomMargin);

        // if (!MyFile.fileExist(path)) {
        try (final OutputStream outputStream = Files.newOutputStream(Paths.get(path))) {
            final PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            this.addEvent(writer);
            this.generatePDF(document, writer);
            return writer.getPageNumber() - 1;
        } catch (final Exception e) {
            PdfExporterLog.errorMessage(this.getClass().getName(), e);
            return 0;
        } finally {
            // 'generatePDF' already closes the document. Closing it twice raises a NPE on
            // OpenPDF internals.
            if (document.isOpen()) {
                document.close();
            }
        }
        // }
    }

    protected abstract Rectangle getPageSize();

    protected abstract void createCharacterPDF(Document document, CharacterPlayer character) throws Exception;

    public String getLanguage() {
        return this.language;
    }

    public String getModuleName() {
        return this.moduleName;
    }
}
