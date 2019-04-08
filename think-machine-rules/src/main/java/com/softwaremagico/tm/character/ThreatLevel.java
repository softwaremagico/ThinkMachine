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
import com.softwaremagico.tm.character.cybernetics.SelectedCyberneticDevice;
import com.softwaremagico.tm.character.equipment.armours.Armour;
import com.softwaremagico.tm.character.equipment.shields.Shield;
import com.softwaremagico.tm.character.equipment.weapons.Weapon;
import com.softwaremagico.tm.character.occultism.OccultismPath;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.character.skills.AvailableSkill;

public class ThreatLevel {

	public static int getThreatLevel(CharacterPlayer characterPlayer) throws InvalidXmlElementException {
		int threatLevel = 0;
		if (!characterPlayer.getAllWeapons().isEmpty()) {
			threatLevel += getThreatLevel(characterPlayer, characterPlayer.getMainWeapon());
			threatLevel += characterPlayer.getAllWeapons().size() - 1;
		}
		threatLevel += characterPlayer.getMeleeCombatStyles().size() * 2;
		threatLevel += characterPlayer.getRangedCombatStyles().size() * 2;
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

	private static int getThreatLevel(CharacterPlayer characterPlayer, Weapon weapon) {
		if (weapon == null) {
			return 0;
		}

		int threatLevel = 0;
		threatLevel += getThreatLevel(characterPlayer, characterPlayer.getCharacteristic(weapon.getCharacteristic().getCharacteristicName()), weapon.getSkill());
		threatLevel += weapon.getMainDamage();
		threatLevel += weapon.getDamageTypes().size() * 2;
		threatLevel += weapon.getMainRange() / 10;
		threatLevel += weapon.getMainRate();
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
		threatLevel += armour.getProtection();
		threatLevel += armour.getDamageTypes().size() * 2;
		return threatLevel;
	}

	private static int getThreatLevel(List<SelectedCyberneticDevice> cyberneticDevices) {
		int threatLevel = 0;
		for (SelectedCyberneticDevice cyberneticDevice : cyberneticDevices) {
			switch (cyberneticDevice.getCyberneticDevice().getType()) {
			case COMBAT:
				threatLevel += cyberneticDevice.getCost() * 2;
				break;
			case ENHANCEMENT:
				threatLevel += cyberneticDevice.getCost() / 2;
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
				threatLevel += occultismPath.getOccultismPowers().get(occultismPowerName).getLevel();
			}
		}
		threatLevel += characterPlayer.getExtraWyrd() * 2;
		threatLevel += characterPlayer.getPsiqueLevel(OccultismTypeFactory.getPsi(characterPlayer.getLanguage())) * 2;
		threatLevel += characterPlayer.getPsiqueLevel(OccultismTypeFactory.getTheurgy(characterPlayer.getLanguage())) * 2;
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
		return characterPlayer.getVitalityValue() * 2;
	}
}
