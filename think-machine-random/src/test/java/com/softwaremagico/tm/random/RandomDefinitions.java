package com.softwaremagico.tm.random;

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
import com.softwaremagico.tm.character.characteristics.CharacteristicType;
import com.softwaremagico.tm.character.exceptions.RestrictedElementException;
import com.softwaremagico.tm.character.exceptions.UnofficialElementNotAllowedException;
import com.softwaremagico.tm.character.equipment.weapons.RandomRangeWeapon;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponFactory;
import com.softwaremagico.tm.character.equipment.weapons.WeaponType;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.character.skills.RandomSkills;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.file.PathManager;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Test(groups = {"randomDefinition"})
public class RandomDefinitions {
    private static final String LANGUAGE = "en";

    @Test(timeOut = 5000)
    public void checkRandomProbabilityMultiplier() throws InvalidXmlElementException {
        Assert.assertEquals(WeaponFactory.getInstance().getElement("rock", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER).getRandomDefinition().getProbabilityMultiplier(), 0d);
    }

    @Test(timeOut = 5000)
    public void removeElementWeight() throws InvalidXmlElementException, RestrictedElementException, UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);

        final AvailableSkill sneak = AvailableSkillsFactory.getInstance().getElement("sneak", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final AvailableSkill warfare = AvailableSkillsFactory.getInstance().getElement("warfare", LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);

        final RandomSkills originalSkills = new RandomSkills(characterPlayer, null);
        final RandomSkills editedSkills = new RandomSkills(characterPlayer, null);

        Assert.assertEquals(originalSkills.getAssignedWeight(sneak), editedSkills.getAssignedWeight(sneak));
        Assert.assertEquals(originalSkills.getAssignedWeight(warfare), editedSkills.getAssignedWeight(warfare));
        Assert.assertEquals(originalSkills.getWeightedElements().size(), editedSkills.getWeightedElements().size());

        editedSkills.removeElementWeight(sneak);

        Assert.assertEquals(editedSkills.getWeightedElements().size(), originalSkills.getWeightedElements().size() - 1);
        Assert.assertNull(editedSkills.getAssignedWeight(sneak));
        Assert.assertNotNull(originalSkills.getAssignedWeight(sneak));
        Assert.assertEquals(originalSkills.getAssignedWeight(warfare), editedSkills.getAssignedWeight(warfare));

    }

    @Test(timeOut = 5000)
    public void weightByCharacteristicsWithSinglePreferredTypeMustNotFail()
            throws InvalidXmlElementException, RestrictedElementException, UnofficialElementNotAllowedException, NoSuchFieldException,
            IllegalAccessException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomSkills randomSkills = new RandomSkills(characterPlayer, null);

        final List<AvailableSkill> skills = new ArrayList<>();
        skills.addAll(AvailableSkillsFactory.getInstance().getNaturalSkills(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));
        skills.addAll(AvailableSkillsFactory.getInstance().getLearnedSkills(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER));

        AvailableSkill candidateSkill = null;
        for (final AvailableSkill availableSkill : skills) {
            if (availableSkill.getSkillDefinition().getSkillGroup().getPreferredCharacteristicsGroups() != null) {
                candidateSkill = availableSkill;
                break;
            }
        }
        Assert.assertNotNull(candidateSkill);
        final CharacteristicType candidateType = candidateSkill.getSkillDefinition().getSkillGroup().getPreferredCharacteristicsGroups();

        CharacteristicType forcedType = null;
        for (final CharacteristicType characteristicType : CharacteristicType.values()) {
            if (characteristicType != CharacteristicType.OTHERS && characteristicType != candidateType) {
                forcedType = characteristicType;
                break;
            }
        }
        Assert.assertNotNull(forcedType);

        final Field preferredCharacteristicsField = RandomSkills.class.getDeclaredField("preferredCharacteristicsTypeSorted");
        preferredCharacteristicsField.setAccessible(true);
        preferredCharacteristicsField.set(randomSkills, List.of(new AbstractMap.SimpleEntry<>(forcedType, 1)));

        Assert.assertTrue(randomSkills.getElementWeight(candidateSkill) >= 0);
    }

    @Test(timeOut = 5000)
    public void weaponWithoutPrimaryDamageMustBeRejectedWithoutNpe()
            throws InvalidXmlElementException, RestrictedElementException, UnofficialElementNotAllowedException {
        final CharacterPlayer characterPlayer = new CharacterPlayer(LANGUAGE, PathManager.DEFAULT_MODULE_FOLDER);
        final RandomRangeWeapon randomRangeWeapon = new RandomRangeWeapon(characterPlayer, null, new HashSet<>());
        final Weapon weaponWithoutDamage = new Weapon("nullDamageWeapon", "Null Damage Weapon", "", LANGUAGE,
                PathManager.DEFAULT_MODULE_FOLDER, WeaponType.BOW, Collections.singletonList(null), 0, false, null, "",
                new HashSet<>(), 0f, new HashSet<>(), new HashSet<>());

        Assert.assertThrows(InvalidRandomElementSelectedException.class,
                () -> randomRangeWeapon.validateElement(weaponWithoutDamage));
    }
}
