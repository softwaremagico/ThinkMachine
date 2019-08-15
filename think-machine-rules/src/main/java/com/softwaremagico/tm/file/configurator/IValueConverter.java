package com.softwaremagico.tm.file.configurator;

public abstract interface IValueConverter <T>{

	public T convertFromString(String value);
	
	public String convertToString(Object value);
	
}
