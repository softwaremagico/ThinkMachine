package com.softwaremagico.tm.configurator;

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

import com.softwaremagico.tm.file.configurator.MachineConfigurationReader;

public class MachinePdfConfigurationReader extends MachineConfigurationReader {
	// Tags
	private static final String SMALL_PDF_CHARACTER_SHIELDS = "sheet.small.shields";
	private static final String SMALL_PDF_CHARACTER_BLESSINGS_NAME = "sheet.small.blessings.name";

	private static volatile MachinePdfConfigurationReader instance;

	public static MachinePdfConfigurationReader getInstance() {
		if (instance == null) {
			synchronized (MachinePdfConfigurationReader.class) {
				if (instance == null) {
					instance = new MachinePdfConfigurationReader();
				}
			}
		}
		return instance;
	}

	private MachinePdfConfigurationReader() {
		super();

		setProperty(SMALL_PDF_CHARACTER_SHIELDS, true);
		setProperty(SMALL_PDF_CHARACTER_BLESSINGS_NAME, false);

		readConfigurations();
	}

	public boolean isSmallPdfShieldEnabled() {
		try {
			return Boolean.parseBoolean(getPropertyLogException(SMALL_PDF_CHARACTER_SHIELDS));
		} catch (Exception e) {
			return false;
		}
	}

	public void setSmallPdfShieldEnabled(boolean value) {
		setProperty(SMALL_PDF_CHARACTER_SHIELDS, value);
	}

	public boolean isSmallPdfBlessingNameEnabled() {
		try {
			return Boolean.parseBoolean(getPropertyLogException(SMALL_PDF_CHARACTER_BLESSINGS_NAME));
		} catch (Exception e) {
			return false;
		}
	}

	public void setSmallPdfBlessingNameEnabled(boolean value) {
		setProperty(SMALL_PDF_CHARACTER_BLESSINGS_NAME, value);
	}
}
