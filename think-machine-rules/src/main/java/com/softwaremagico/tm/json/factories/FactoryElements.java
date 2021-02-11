package com.softwaremagico.tm.json.factories;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.XmlFactory;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public abstract class FactoryElements<E extends Element<?>, T extends XmlFactory> extends BaseElement {

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
