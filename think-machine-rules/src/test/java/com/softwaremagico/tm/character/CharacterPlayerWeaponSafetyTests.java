package com.softwaremagico.tm.character;

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

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponType;
import com.softwaremagico.tm.character.exceptions.UnofficialElementNotAllowedException;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.file.PathManager;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashSet;

@Test(groups = {"rules"})
public class CharacterPlayerWeaponSafetyTests {
    private static final String LANGUAGE = "en";

    @Test(timeOut = 5000)
    public void nullPrimaryDamageWeaponMustBeIgnoredOnSkillSearchAndMainWeaponSelection()
            throws InvalidXmlElementException, UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final AvailableSkill fightSkill = AvailableSkillsFactory.getInstance().getElement("fight", LANGUAGE,
                PathManager.DEFAULT_MODULE_FOLDER);
        final Weapon weaponWithoutPrimaryDamage = new Weapon("nullDamageWeapon", "Null Damage Weapon", "", LANGUAGE,
                PathManager.DEFAULT_MODULE_FOLDER, WeaponType.BOW, Collections.singletonList(null), 0, false, null, "",
                new HashSet<>(), 0f, new HashSet<>(), new HashSet<>());

        characterPlayer.addWeapon(weaponWithoutPrimaryDamage);

        Assert.assertNull(characterPlayer.hasWeaponWithSkill(fightSkill));
        Assert.assertNull(characterPlayer.getMainWeapon());
    }
}
