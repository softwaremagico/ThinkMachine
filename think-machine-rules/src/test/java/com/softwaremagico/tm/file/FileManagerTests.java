package com.softwaremagico.tm.file;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.softwaremagico.tm.file.FileManager;
import com.softwaremagico.tm.file.configurator.MachineConfigurationReader;

@Test(groups = "fileManager")
public class FileManagerTests {

	@Test
	public void checkModuleReader() throws IOException {
		Assert.assertEquals(FileManager.getAvailableModules().size(), 0);
		// Emulate some jars.
		Set<File> modules = new HashSet<>();
		MachineConfigurationReader.getInstance().setProperty("modulesPath", "/tmp");
		for (int i = 0; i < 10; i++) {
			File module = new File(MachineConfigurationReader.getInstance().getModulesPath() + File.separator
					+ "module" + i + ".jar");
			module.createNewFile();
			modules.add(module);
		}
		Assert.assertEquals(FileManager.getAvailableModules().size(), 10);
		for (File module : modules) {
			module.delete();
		}
		Assert.assertEquals(FileManager.getAvailableModules().size(), 0);
	}
}
