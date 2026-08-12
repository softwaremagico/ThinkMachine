package com.softwaremagico.tm.character.equipment.weapons;

/*-
 * #%L
 * Think Machine (Rules)
 * %%
 * Copyright (C) 2017 - 2026 Softwaremagico
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
