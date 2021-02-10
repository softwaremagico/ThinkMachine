package com.softwaremagico.tm.factory;

/*-
 * #%L
 * Think Machine (Core)
 * %%
 * Copyright (C) 2017 - 2018 Softwaremagico
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

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.equipment.weapons.AccessoryFactory;
import com.softwaremagico.tm.character.equipment.weapons.AmmunitionFactory;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.file.PathManager;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

@Test(groups = {"weaponsFactory"})
public class WeaponsFactoryTests {
    private static final String LANGUAGE = "es";

    private static final int DEFINED_WEAPONS = 189;

    @Test
    public void checkTotalElements() {
        Assert.assertEquals((int) WeaponFactory.getInstance().getNumberOfElements(PathManager.DEFAULT_MODULE_FOLDER),
                DEFINED_WEAPONS);
    }


    @Test
    public void readWeapons() throws InvalidXmlElementException {
        Assert.assertEquals(DEFINED_WEAPONS, WeaponFactory.getInstance().getElements(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).size());
    }

    @Test
    public void readAmmunition() throws InvalidXmlElementException {
        Assert.assertTrue(AmmunitionFactory.getInstance().getElements(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).size() > 0);
    }

    @Test
    public void readAccessory() throws InvalidXmlElementException {
        Assert.assertTrue(AccessoryFactory.getInstance().getElements(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).size() > 0);
    }

    @Test
    public void checkShotgun() throws InvalidXmlElementException {
        Assert.assertEquals(1, WeaponFactory.getInstance().getElement("typicalShotgun", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                .getAmmunition().size());
    }

    @Test
    public void checkBasicHuntingRifle() throws InvalidXmlElementException {
        Assert.assertEquals(3, WeaponFactory.getInstance().getElement("basicHuntingRifle", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                .getAccessories().size());
    }

    @Test
    public void checkRandomModifications() throws InvalidXmlElementException {
        Assert.assertEquals(0.01d, WeaponFactory.getInstance().getElement("arbata", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                .getRandomDefinition().getProbabilityMultiplier());
    }

    @Test
    public void getMainDamage() throws InvalidXmlElementException {
        Assert.assertEquals(6, WeaponFactory.getInstance().getElement("arbata", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getMainDamage());
        Assert.assertEquals(8, WeaponFactory.getInstance().getElement("typicalShotgun", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                .getMainDamage());
        Assert.assertEquals(12, WeaponFactory.getInstance().getElement("wireGrenade", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getMainDamage());
    }

    @Test
    public void getAreaDamage() throws InvalidXmlElementException {
        Assert.assertEquals(1, WeaponFactory.getInstance().getElement("goboLobberJetPistol", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                .getAreaMeters());
        Assert.assertEquals(2, WeaponFactory.getInstance().getElement("goboGarbageChucker", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                .getAreaMeters());
        Assert.assertEquals(3, WeaponFactory.getInstance().getElement("musterNightstorm", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                .getAreaMeters());
        Assert.assertEquals(5, WeaponFactory.getInstance().getElement("fragGrenades", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getAreaMeters());
    }

    @Test
    public void getDamageWithoutArea() throws InvalidXmlElementException {
        Assert.assertEquals("3", WeaponFactory.getInstance().getElement("blastPellet", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                .getDamageWithoutArea());
        Assert.assertEquals("6", WeaponFactory.getInstance().getElement("blastPelletHeavy", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                .getDamageWithoutArea());
        Assert.assertEquals("12", WeaponFactory.getInstance().getElement("fragGrenades", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER)
                .getDamageWithoutArea());
    }

    @Test
    public void setRangeWeapons() throws InvalidXmlElementException {
        final CharacterPlayer player = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        Set<Weapon> weaponsToAdd = new HashSet<>();
        weaponsToAdd.add(WeaponFactory.getInstance().getElement("typicalShotgun", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        weaponsToAdd.add(WeaponFactory.getInstance().getElement("martechSafireSniper", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        player.setRangedWeapons(weaponsToAdd);
        Assert.assertEquals(2, player.getAllWeapons().size());

        weaponsToAdd = new HashSet<>();
        weaponsToAdd.add(WeaponFactory.getInstance().getElement("typicalShotgun", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        weaponsToAdd.add(WeaponFactory.getInstance().getElement("soeCrucible", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        player.setRangedWeapons(weaponsToAdd);
        Assert.assertEquals(2, player.getAllWeapons().size());

        weaponsToAdd = new HashSet<>();
        weaponsToAdd.add(WeaponFactory.getInstance().getElement("arbata", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        player.setMeleeWeapons(weaponsToAdd);
        Assert.assertEquals(3, player.getAllWeapons().size());

        weaponsToAdd = new HashSet<>();
        weaponsToAdd.add(WeaponFactory.getInstance().getElement("nitobiBlasterAxe", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        player.setRangedWeapons(weaponsToAdd);
        Assert.assertEquals(2, player.getAllWeapons().size());

        weaponsToAdd = new HashSet<>();
        player.setRangedWeapons(weaponsToAdd);
        Assert.assertEquals(1, player.getAllWeapons().size());

        weaponsToAdd = new HashSet<>();
        player.setMeleeWeapons(weaponsToAdd);
        Assert.assertEquals(0, player.getAllWeapons().size());
    }

}
