package com.softwaremagico.tm.character.cybernetics;

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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.characteristics.CharacteristicDefinition;
import com.softwaremagico.tm.character.characteristics.CharacteristicType;
import com.softwaremagico.tm.character.creation.CostCalculator;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.character.skills.RandomSkillExtraPoints;
import com.softwaremagico.tm.character.values.Bonification;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.RandomSelector;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.CombatPreferences;
import com.softwaremagico.tm.random.selectors.CyberneticPointsPreferences;
import com.softwaremagico.tm.random.selectors.CyberneticTotalDevicesPreferences;
import com.softwaremagico.tm.random.selectors.CyberneticVisibilityPreferences;
import com.softwaremagico.tm.random.selectors.DifficultLevelPreferences;
import com.softwaremagico.tm.random.selectors.IRandomPreference;

public class RandomCybernetics extends RandomSelector<CyberneticDevice> {
	private int totalDevices;
	private int desiredCyberneticsPoints;

	public RandomCybernetics(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences)
			throws InvalidXmlElementException {
		super(characterPlayer, preferences);
		totalDevices = CyberneticTotalDevicesPreferences.getSelected(getPreferences()).randomGaussian();
		desiredCyberneticsPoints = CyberneticPointsPreferences.getSelected(getPreferences()).randomGaussian();
	}

	@Override
	public void assign() throws InvalidXmlElementException, InvalidRandomElementSelectedException {
		final DifficultLevelPreferences difficultyLevel = DifficultLevelPreferences.getSelected(getPreferences());

		int remainingPoints = FreeStyleCharacterCreation
				.getFreeAvailablePoints(getCharacterPlayer().getInfo().getAge())
				- CostCalculator.getCost(getCharacterPlayer(), difficultyLevel.getSkillsBonus(),
						difficultyLevel.getCharacteristicsBonus());
		// Select a cybernetic device.
		int guard = 0;
		while (guard < 20 && getCharacterPlayer().getCybernetics().size() < totalDevices
				&& getCharacterPlayer().getCyberneticsIncompatibility() < desiredCyberneticsPoints) {
			final CyberneticDevice selectedDevice = selectElementByWeight();
			if (selectedDevice.getPoints() > remainingPoints) {
				continue;
			}
			try {
				getCharacterPlayer().addCybernetics(selectedDevice);
				remainingPoints -= selectedDevice.getPoints();
				// Update requirements.
				for (final CyberneticDevice device : CyberneticDeviceFactory.getInstance().getDevicesThatRequires(
						selectedDevice, getCharacterPlayer().getLanguage())) {
					updateWeight(device, getWeight(device) * 20);
				}
				// Assign skills if needed.
				// Some Cybernetics needs skills
				final CyberneticDeviceTrait usability = selectedDevice
						.getTrait(CyberneticDeviceTraitCategory.USABILITY);
				if (usability != null && usability.getId().equalsIgnoreCase("skillUse")) {
					final AvailableSkill skill = AvailableSkillsFactory.getInstance().getElement(
							selectedDevice.getId(), getCharacterPlayer().getLanguage());
					if (skill != null) {
						final RandomSkillExtraPoints randomSkillExtraPoints = new RandomSkillExtraPoints(
								getCharacterPlayer(), getPreferences(), new HashSet<AvailableSkill>());
						// Assign random ranks to the skill.
						remainingPoints -= randomSkillExtraPoints.spendSkillsPoints(skill, remainingPoints);
					}
				}
			} catch (TooManyCyberneticDevicesException e) {
				// No more cybernetics is possible.
				if (getCharacterPlayer().getCyberneticsIncompatibility() >= Cybernetics
						.getMaxCyberneticIncompatibility(getCharacterPlayer())) {
					break;
				}
			} catch (RequiredCyberneticDevicesException e) {
				// Cannot be added due to a requirement.
			}
			removeElementWeight(selectedDevice);
			guard++;
		}
	}

	@Override
	protected Collection<CyberneticDevice> getAllElements() throws InvalidXmlElementException {
		return CyberneticDeviceFactory.getInstance().getElements(getCharacterPlayer().getLanguage());
	}

	@Override
	protected int getWeight(CyberneticDevice cyberneticDevice) throws InvalidRandomElementSelectedException {
		// Close to the desired points
		int weight = 1;
		if (cyberneticDevice.getIncompatibility() > desiredCyberneticsPoints / 2) {
			weight *= 5;
		}
		if (cyberneticDevice.getIncompatibility() > desiredCyberneticsPoints / 3) {
			weight *= 3;
		}
		if (cyberneticDevice.getIncompatibility() > desiredCyberneticsPoints / 4) {
			weight *= 2;
		}

		// Letal cybernetics for soldiers.
		final CombatPreferences combatPreferences = CombatPreferences.getSelected(getPreferences());
		if (combatPreferences == CombatPreferences.BELLIGERENT) {
			// Preferred weapons
			if (cyberneticDevice.getWeapon() != null) {
				weight *= BASIC_MULTIPLICATOR;
			}

			// Preferred body bonus.
			for (final Bonification bonification : cyberneticDevice.getBonifications()) {
				if (bonification.getAffects() instanceof CharacteristicDefinition) {
					if (((CharacteristicDefinition) bonification.getAffects()).getType()
							.equals(CharacteristicType.BODY)) {
						weight *= BASIC_MULTIPLICATOR;
					}
				}
			}
		}

		// Visibility
		final CyberneticDeviceTrait visibility = cyberneticDevice.getTrait(CyberneticDeviceTraitCategory.VISIBILITY);
		switch (CyberneticVisibilityPreferences.getSelected(getPreferences())) {
		case HIDDEN:
			if (visibility != null) {
				if (visibility.getId().equalsIgnoreCase("simulacra") || visibility.getId().equalsIgnoreCase("hidden")) {
					weight *= HIGH_MULTIPLICATOR;
				} else if (visibility.getId().equalsIgnoreCase("incognito")) {
					weight *= BASIC_MULTIPLICATOR;
				} else {
					// No visible devices.
					throw new InvalidRandomElementSelectedException("Visible cyberntics as '" + cyberneticDevice
							+ "' not allowed by user preferences.");
				}
			}
			break;
		case VISIBLE:
			break;
		}

		// Forbidden are rare.
		try {
			if (cyberneticDevice.getTraits().contains(
					CyberneticDeviceTraitFactory.getInstance().getElement("proscribed",
							getCharacterPlayer().getLanguage()))) {
				if (CyberneticPointsPreferences.getSelected(getPreferences()) == CyberneticPointsPreferences.SUBTLE) {
					return 0;
				}
				weight /= 3;
			}
		} catch (InvalidXmlElementException e) {
			RandomGenerationLog.errorMessage(this.getClass().getName(), e);
		}

		return weight;
	}

	@Override
	protected void assignIfMandatory(CyberneticDevice element) throws InvalidXmlElementException {
		return;
	}

	@Override
	protected void assignMandatoryValues(Set<CyberneticDevice> mandatoryValues) throws InvalidXmlElementException {
		return;
	}
}
