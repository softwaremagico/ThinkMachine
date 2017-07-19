package com.softwaremagico.tm.factory;

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.softwaremagico.tm.character.race.RaceFactory;

@Test(groups = { "raceFactory" })
public class RaceFactoryTests {

	@Test
	public void readRaces() {
		Assert.assertEquals(1, RaceFactory.getRaces("es").size());
	}
}
