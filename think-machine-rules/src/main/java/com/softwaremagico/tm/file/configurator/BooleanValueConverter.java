package com.softwaremagico.tm.file.configurator;

public class BooleanValueConverter implements IValueConverter<Boolean> {

	protected static final Object TRUE = "true";
	protected static final Object FALSE = "false";

	@Override
	public Boolean convertFromString(String value) {
		if (value == null) {
			return true;
		}
		if (value.equals(TRUE)) {
			return true;
		}
		if (value.equals(FALSE)) {
			return false;
		}
		return false;
	}

	@Override
	public String convertToString(Object value) {
		if (value != null) {
			return value.toString();
		}
		return null;
	}
}
