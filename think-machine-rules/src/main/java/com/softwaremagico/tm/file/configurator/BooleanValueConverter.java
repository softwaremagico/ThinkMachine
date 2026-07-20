package com.softwaremagico.tm.file.configurator;

/*-
 * #%L
 * Think Machine (Rules)
 * %%
 * Copyright (C) 2017 - 2019 Softwaremagico
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
