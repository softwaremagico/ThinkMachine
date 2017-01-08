package com.softwaremagico.tm.skills;

import junit.framework.Assert;

import org.testng.annotations.Test;

@Test(groups = { "skillFactory" })
public class SkillFactoryTests {

	@Test
	public void readSkills() {
		Assert.assertEquals(9, SkillFactory.getNaturalSkills().size());
		Assert.assertEquals(47, SkillFactory.getLearnedSkills().size());
	}
}
