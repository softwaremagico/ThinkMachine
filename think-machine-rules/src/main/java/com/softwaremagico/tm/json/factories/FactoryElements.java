package com.softwaremagico.tm.json.factories;

/*-
 * #%L
 * Think Machine (Rules)
 * %%
 * Copyright (C) 2017 - 2021 Softwaremagico
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

import com.softwaremagico.tm.Element;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public abstract class FactoryElements<E extends Element<E>> extends BaseElement {

    public int version;

    public int totalElements;

    public List<E> elements;

    public FactoryElements() {
        super();
        creationTime = new Timestamp(new Date().getTime());
    }

    public FactoryElements(List<E> elements) {
        this();
        setElements(elements);
    }

    public void setElements(List<E> elements) {
        this.elements = elements;
    }

    public List<E> getElements() {
        return this.elements;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }
}
