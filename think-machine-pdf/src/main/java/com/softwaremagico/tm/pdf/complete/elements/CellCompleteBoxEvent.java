package com.softwaremagico.tm.pdf.complete.elements;

import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPCellEvent;
import com.lowagie.text.pdf.PdfPTable;

import java.util.Arrays;

/*-
 * #%L
 * Think Machine (Core)
 * %%
 * Copyright (C) 2017 Softwaremagico
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


public class CellCompleteBoxEvent implements PdfPCellEvent {

    public enum Border {
        TOP, LEFT, RIGHT, BOTTOM;
    }

    private int borderThickness = 1;
    private int margin = 3;
    private Border[] borders;

    public CellCompleteBoxEvent(Border[] borders) {
        this.borders = Arrays.copyOf(borders, borders.length);
    }

    public CellCompleteBoxEvent(int borderThickness, Border[] borders) {
        this.borderThickness = borderThickness;
        this.borders = Arrays.copyOf(borders, borders.length);
    }

    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        final PdfContentByte canvas = canvases[PdfPTable.LINECANVAS];
        canvas.setLineWidth(borderThickness);

        final int bottomMargin = isBorderEnabled(Border.BOTTOM) ? margin : 0;
        final int topMargin = isBorderEnabled(Border.TOP) ? margin : 0;
        final int leftMargin = isBorderEnabled(Border.LEFT) ? margin : 0;
        final int rightMargin = isBorderEnabled(Border.RIGHT) ? margin : 0;

        if (isBorderEnabled(Border.TOP)) {
            canvas.moveTo(position.getLeft() + leftMargin, position.getTop() - topMargin);
            canvas.lineTo(position.getRight() - rightMargin, position.getTop() - topMargin);
        }
        if (isBorderEnabled(Border.BOTTOM)) {
            canvas.moveTo(position.getLeft() + leftMargin, position.getBottom() + bottomMargin);
            canvas.lineTo(position.getRight() - rightMargin, position.getBottom() + bottomMargin);
        }
        if (isBorderEnabled(Border.RIGHT)) {
            canvas.moveTo(position.getRight() - rightMargin, position.getBottom() + bottomMargin);
            canvas.lineTo(position.getRight() - rightMargin, position.getTop() - topMargin);
        }
        if (isBorderEnabled(Border.LEFT)) {
            canvas.moveTo(position.getLeft() + leftMargin, position.getBottom() + bottomMargin);
            canvas.lineTo(position.getLeft() + leftMargin, position.getTop() - topMargin);
        }
        canvas.stroke();
    }

    private boolean isBorderEnabled(Border border) {
        for (final Border selectedBorder : borders) {
            if (selectedBorder.equals(border)) {
                return true;
            }
        }
        return false;
    }
}
