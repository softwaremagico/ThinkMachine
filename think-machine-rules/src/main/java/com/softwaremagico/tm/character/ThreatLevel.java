package com.softwaremagico.tm.character;

/*-
 * #%L
 * Think Machine (Rules)
 * %%
 * Copyright (C) 2017 - 2019 Softwaremagico
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

import java.util.List;
import java.util.Map.Entry;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.cybernetics.SelectedCyberneticDevice;
import com.softwaremagico.tm.character.equipment.armours.Armour;
import com.softwaremagico.tm.character.equipment.shields.Shield;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.equipment.weapons.WeaponType;
import com.softwaremagico.tm.character.occultism.OccultismPath;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;

public class ThreatLevel {
	private final static int DAMAGE_THREAT_MULTIPLICATOR = 2;
	private final static int DAMAGE_AREA_THREAT_MULTIPLICATOR = 3;
	private final static int DAMAGE_TYPES_THREAT_MULTIPLICATOR = 2;
	private final static int COMBAT_STYLES_THREAT_MULTIPLICATOR = 5;
	private final static int EXTRA_WYRD_THREAT_MULTIPLICATOR = 3;
	private final static int PSI_LEVEL_THREAT_MULTIPLICATOR = 2;
	private final static int VITALITY_THREAT_MULTIPLICATOR = 2;

	public static int getThreatLevel(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
		int threatLevel = 0;
		threatLevel += getCombatThreatLevel(characterPlayer);
		threatLevel += getThreatLevel(characterPlayer.getArmour());
		threatLevel += getVitalityThreatLevel(characterPlayer);
		threatLevel += getThreatLevel(characterPlayer.getCybernetics());
		threatLevel += getOccultismThreatLevel(characterPlayer);
		threatLevel *= getThreatLevelMultiplicator(characterPlayer.getShield());
		return threatLevel;
	}

	private static int getThreatLevel(CharacterPlayer characterPlayer, Characteristic characteristic, AvailableSkill skill) {
		return characterPlayer.getSkillTotalRanks(skill) + characteristic.getValue();
	}

	private static int getCombatThreatLevel(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
		int threatLevel = 0;
		if (!characterPlayer.getAllWeapons().isEmpty()) {
			Weapon mainWeapon = characterPlayer.getMainWeapon();
			threatLevel += getThreatLevel(characterPlayer, characterPlayer.getCharacteristic(mainWeapon.getCharacteristic().getCharacteristicName()),
					mainWeapon.getSkill()) * 2;
			threatLevel += getThreatLevel(mainWeapon);
			if (WeaponType.getMeleeTypes().contains(mainWeapon.getType())) {
				threatLevel += characterPlayer.getStrengthDamangeModification() * DAMAGE_THREAT_MULTIPLICATOR;
			}
			threatLevel += characterPlayer.getAllWeapons().size() - 1;
		}
		threatLevel += characterPlayer.getMeleeCombatStyles().size() * COMBAT_STYLES_THREAT_MULTIPLICATOR;
		threatLevel += characterPlayer.getRangedCombatStyles().size() * COMBAT_STYLES_THREAT_MULTIPLICATOR;
		threatLevel += getThreatLevel(characterPlayer, characterPlayer.getCharacteristic(CharacteristicName.DEXTERITY), AvailableSkillsFactory.getInstance()
				.getElement("fight", characterPlayer.getLanguage()));
		return threatLevel;
	}

	private static int getThreatLevel(Weapon weapon) {
		if (weapon == null) {
			return 0;
		}
		int threatLevel = 0;
		threatLevel += weapon.getMainDamage() * DAMAGE_THREAT_MULTIPLICATOR;
		threatLevel += weapon.getAreaDamage() * DAMAGE_AREA_THREAT_MULTIPLICATOR;
		threatLevel += weapon.getDamageTypes().size() * DAMAGE_TYPES_THREAT_MULTIPLICATOR;
		threatLevel += weapon.getMainRange() / 10;
		threatLevel += weapon.getMainRate();
		// threatLevel += weapon.getAccesories().size();
		if (weapon.isAutomaticWeapon()) {
			threatLevel *= 2;
		}
		return threatLevel;
	}

	private static int getThreatLevel(Armour armour) {
		if (armour == null) {
			return 0;
		}
		int threatLevel = 0;
		threatLevel += armour.getProtection() * DAMAGE_THREAT_MULTIPLICATOR;
		threatLevel += armour.getDamageTypes().size() * DAMAGE_TYPES_THREAT_MULTIPLICATOR;
		return threatLevel;
	}

	private static int getThreatLevel(List<SelectedCyberneticDevice> cyberneticDevices) {
		int threatLevel = 0;
		for (SelectedCyberneticDevice cyberneticDevice : cyberneticDevices) {
			switch (cyberneticDevice.getCyberneticDevice().getClassification()) {
			case COMBAT:
				threatLevel += cyberneticDevice.getPoints() * 2;
				break;
			case ENHANCEMENT:
			case ALTERATION:
				threatLevel += cyberneticDevice.getPoints() / 2;
				break;
			case OTHERS:
				break;
			}
		}
		return threatLevel;
	}

	private static int getOccultismThreatLevel(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
		int threatLevel = 0;
		for (Entry<String, List<String>> occulstismPathEntry : characterPlayer.getSelectedPowers().entrySet()) {
			OccultismPath occultismPath = OccultismPathFactory.getInstance().getElement(occulstismPathEntry.getKey(), characterPlayer.getLanguage());
			for (String occultismPowerName : occulstismPathEntry.getValue()) {
				switch (occultismPath.getClassification()) {
				case COMBAT:
					threatLevel += occultismPath.getOccultismPowers().get(occultismPowerName).getLevel();
					break;
				case ENHANCEMENT:
				case ALTERATION:
				case OTHERS:
					threatLevel += occultismPath.getOccultismPowers().get(occultismPowerName).getLevel() / 2;
					break;
				}
			}
		}
		threatLevel += characterPlayer.getExtraWyrd() * EXTRA_WYRD_THREAT_MULTIPLICATOR;
		threatLevel += characterPlayer.getPsiqueLevel(OccultismTypeFactory.getPsi(characterPlayer.getLanguage())) * PSI_LEVEL_THREAT_MULTIPLICATOR;
		threatLevel += characterPlayer.getPsiqueLevel(OccultismTypeFactory.getTheurgy(characterPlayer.getLanguage())) * PSI_LEVEL_THREAT_MULTIPLICATOR;
		return threatLevel;
	}

	private static float getThreatLevelMultiplicator(Shield shield) {
		if (shield == null) {
			return 1;
		}
		float threatLevel = 0;
		threatLevel += ((shield.getForce() - shield.getImpact()) + 5) / 10;
		threatLevel += ((float) shield.getImpact()) / 10;
		return threatLevel;
	}

	private static int getVitalityThreatLevel(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
		return characterPlayer.getVitalityValue() * VITALITY_THREAT_MULTIPLICATOR;
	}
}
