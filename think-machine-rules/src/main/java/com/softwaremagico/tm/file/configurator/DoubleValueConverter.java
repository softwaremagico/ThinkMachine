package com.softwaremagico.tm.file.configurator;

public class DoubleValueConverter implements IValueConverter<Double> {

	@Override
	public Double convertFromString(String value) {
		if (value != null) {
			return Double.parseDouble(value);
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
