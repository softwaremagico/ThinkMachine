package com.softwaremagico.tm.character;

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
