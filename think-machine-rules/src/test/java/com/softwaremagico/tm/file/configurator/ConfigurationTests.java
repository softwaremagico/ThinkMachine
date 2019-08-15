package com.softwaremagico.tm.file.configurator;

import java.io.FileNotFoundException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.softwaremagico.tm.file.FileManager;
import com.softwaremagico.tm.file.configurator.exceptions.PropertyNotStoredException;

@Test(groups = "configurationReader")
public class ConfigurationTests {

	@Test
	public void checkStoreSettings() throws PropertyNotStoredException, FileNotFoundException {
		MachineConfigurationReader.getInstance().setProperty("modulesPath", "/tmp");
		Assert.assertEquals(MachineConfigurationReader.getInstance().getModulesPath(), "/tmp");

		MachineConfigurationReader.getInstance().storeProperties();
		String content = FileManager.readTextFile(MachineConfigurationReader.getInstance().getUserProperties());
		Assert.assertTrue(content.contains("modulesPath=/tmp"));
	}
}
