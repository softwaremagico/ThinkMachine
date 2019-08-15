package com.softwaremagico.tm.file.configurator;

public class IntegerValueConverter implements IValueConverter<Integer> {

	@Override
	public Integer convertFromString(String value) {
		if (value != null) {
			return Integer.parseInt(value);
		}
		return null;
	}

	@Override
	public String convertToString(Object value) {
		if (value != null) {
			return value.toString();
		}
		return null;
	}

}
