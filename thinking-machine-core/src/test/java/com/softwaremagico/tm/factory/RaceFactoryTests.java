package com.softwaremagico.tm.factory;

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.softwaremagico.tm.character.race.InvalidRaceException;
import com.softwaremagico.tm.character.race.RaceFactory;

@Test(groups = { "raceFactory" })
public class RaceFactoryTests {

	@Test
	public void readRaces() throws InvalidRaceException {
		Assert.assertEquals(2, RaceFactory.getRaces("es").size());
	}
}
