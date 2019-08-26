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

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.softwaremagico.tm.file.FileManager;
import com.softwaremagico.tm.file.configurator.exceptions.PropertyNotStoredException;

@Test(groups = "configurationReader")
public class ConfigurationTests {

	@Test
	public void checkStoreSettings() throws PropertyNotStoredException, IOException {
		MachineConfigurationReader.getInstance().setModulesPath(System.getProperty("java.io.tmpdir"), null);
		Assert.assertEquals(MachineConfigurationReader.getInstance().getModulesPath(),
				System.getProperty("java.io.tmpdir"));

		MachineConfigurationReader.getInstance().storeProperties();
		String content = FileManager.readTextFile(MachineConfigurationReader.getInstance().getUserPropertiesPath(),
				StandardCharsets.UTF_8);
		Assert.assertTrue(content.contains("modulesPath=" + System.getProperty("java.io.tmpdir")));
	}
}
