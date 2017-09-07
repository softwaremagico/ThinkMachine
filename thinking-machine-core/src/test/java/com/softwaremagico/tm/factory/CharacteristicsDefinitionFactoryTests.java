package com.softwaremagico.tm.factory;

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;

@Test(groups = { "characteristicsFactory" })
public class CharacteristicsDefinitionFactoryTests {

	@Test
	public void readCharacteristics() throws InvalidXmlElementException {
		Assert.assertEquals(12, CharacteristicsDefinitionFactory.getInstance().getElements("es").size());
	}
}
