package com.softwaremagico.tm.file.configurator.exceptions;

public class PropertyNotStoredException extends Exception {
	private static final long serialVersionUID = 3913034720136311516L;

	public PropertyNotStoredException(String string) {
		super(string);
	}

	public PropertyNotStoredException(String string, Throwable e) {
		super(string, e);
	}
}
