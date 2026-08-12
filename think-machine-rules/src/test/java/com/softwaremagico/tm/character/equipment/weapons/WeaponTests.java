package com.softwaremagico.tm.character.equipment.weapons;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashSet;

@Test(groups = {"rules"})
public class WeaponTests {

    @Test(timeOut = 5000)
    public void automaticAndMeleeChecksWithNullDamageMustNotFail() {
        final Weapon weapon = new Weapon("testWeapon", "Test Weapon", "", "en", "testModule",
                WeaponType.BOW, Collections.singletonList(null), 0, false, null, "",
                new HashSet<>(), 0f, new HashSet<>(), new HashSet<>());

        Assert.assertFalse(weapon.isAutomaticWeapon());
        Assert.assertFalse(weapon.isMeleeWeapon());
    }
}
