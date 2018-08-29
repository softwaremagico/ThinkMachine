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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.creation.CostCalculator;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.character.occultism.OccultismPath;
import com.softwaremagico.tm.character.occultism.OccultismPathFactory;
import com.softwaremagico.tm.character.occultism.OccultismPower;
import com.softwaremagico.tm.character.occultism.OccultismType;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.CombatPreferences;
import com.softwaremagico.tm.random.selectors.IGaussianDistribution;
import com.softwaremagico.tm.random.selectors.IRandomPreferences;
import com.softwaremagico.tm.random.selectors.PsiqueLevelPreferences;
import com.softwaremagico.tm.random.selectors.PsiquePathLevelPreferences;
import com.softwaremagico.tm.random.selectors.SpecializationPreferences;

public class RandomPsiquePath extends RandomSelector<OccultismPath> {
	private final static int MAX_PROBABILITY = 100000;
	private final static int GOOD_PROBABILITY = 10;

	private final static int TOTAL_PDF_PSI_ROWS = 7;

	private int totalPowers;

	protected RandomPsiquePath(CharacterPlayer characterPlayer, Set<IRandomPreferences> preferences)
			throws InvalidXmlElementException {
		super(characterPlayer, preferences);
	}

	public void assignPsiquePaths() throws InvalidRandomElementSelectedException, InvalidXmlElementException {
		// Random number of paths.
		IGaussianDistribution pathNumber = PsiquePathLevelPreferences.getSelected(getPreferences());
		int totalPaths = pathNumber.randomGaussian();
		totalPowers = 0;
		for (int i = 0; i < totalPaths; i++) {
			try {
				OccultismPath selectedOccultismPath = selectElementByWeight();
				// Select a level of psique.
				int pathLevel = assignMaxLevelOfPath(selectedOccultismPath);
				// Assign path to the character.
				assignPowersOfPath(selectedOccultismPath, pathLevel);
				removeElementWeight(selectedOccultismPath);
			} catch (InvalidRandomElementSelectedException irese) {
				// No elements to select, probably no power is available.
			}
		}

	}

	@Override
	protected TreeMap<Integer, OccultismPath> assignElementsWeight() throws InvalidXmlElementException {
		TreeMap<Integer, OccultismPath> weightedPaths = new TreeMap<>();
		int count = 1;
		for (OccultismPath occultismPath : OccultismPathFactory.getInstance().getElements(
				getCharacterPlayer().getLanguage())) {
			int weight = getWeight(occultismPath);
			if (weight > 0) {
				weightedPaths.put(count, occultismPath);
				count += weight;
			}
		}
		return weightedPaths;
	}

	@Override
	protected int getWeight(OccultismPath element) {
		// Other factions path are forbidden
		if (!element.getFactionsAllowed().isEmpty()
				&& !element.getFactionsAllowed().contains(getCharacterPlayer().getFaction())) {
			return 0;
		}
		// Own factions paths are a must.
		if (element.getFactionsAllowed().contains(getCharacterPlayer().getFaction())) {
			return MAX_PROBABILITY;
		}
		// Only paths with psique level.
		try {
			for (OccultismType occultismType : OccultismTypeFactory.getInstance().getElements(
					getCharacterPlayer().getLanguage())) {
				if (getCharacterPlayer().getPsiqueLevel(occultismType) == 0) {
					if (Objects.equals(element.getOccultismType(), occultismType)) {
						return 0;
					}
				}
			}
		} catch (InvalidXmlElementException e) {
			RandomGenerationLog.errorMessage(this.getClass().getName(), e);
		}

		// Combat psi characters prefer specific paths.
		CombatPreferences combatPreferences = CombatPreferences.getSelected(getPreferences());
		if (combatPreferences == CombatPreferences.BELLIGERENT
				&& (element.getId().equals("farHand") || element.getId().equals("soma"))) {
			return GOOD_PROBABILITY;
		}
		return 1;
	}

	private int assignMaxLevelOfPath(OccultismPath path) throws InvalidXmlElementException {
		// Use psique level preferences for the path level.
		IGaussianDistribution psiqueLevelSelector = PsiqueLevelPreferences.getSelected(getPreferences());
		int maxLevelSelected = psiqueLevelSelector.randomGaussian();
		if (maxLevelSelected > psiqueLevelSelector.maximum()) {
			maxLevelSelected = psiqueLevelSelector.maximum();
		}
		if (maxLevelSelected > getCharacterPlayer().getPsiqueLevel(path.getOccultismType())) {
			maxLevelSelected = getCharacterPlayer().getPsiqueLevel(path.getOccultismType());
		}
		return maxLevelSelected;
	}

	private void assignPowersOfPath(OccultismPath path, int maxLevelSelected) throws InvalidXmlElementException {
		int remainingPoints = FreeStyleCharacterCreation.FREE_AVAILABLE_POINTS
				- CostCalculator.getCost(getCharacterPlayer());
		// Select powers to set.
		List<OccultismPower> powersToAdd = new ArrayList<>();

		SpecializationPreferences specializationPreferences = SpecializationPreferences.getSelected(getPreferences());
		// Psi must have at least one power by level.
		if (Objects.equals(path.getOccultismType(), OccultismTypeFactory.getPsi(getCharacterPlayer().getLanguage()))) {
			for (int i = 1; i <= maxLevelSelected; i++) {
				List<OccultismPower> powers = new ArrayList<>(path.getPowersOfLevel(i));
				// If has more than one power at one level, choose one of them at least.
				if (!powers.isEmpty()) {
					Collections.shuffle(powers);
					powersToAdd.add(powers.get(0));

				}
			}
		}
		// Theurgy does not need to have all levels.
		if (Objects
				.equals(path.getOccultismType(), OccultismTypeFactory.getTheurgy(getCharacterPlayer().getLanguage()))) {
			// Levels to add.
			int numberOfPowers = specializationPreferences.randomGaussian();
			List<OccultismPower> powers = new ArrayList<>(path.getOccultismPowers().values());
			Collections.shuffle(powers);
			while (numberOfPowers > 0 && !powers.isEmpty()) {
				// It is possible to add this power.
				if (powers.get(0).getLevel() <= maxLevelSelected) {
					powersToAdd.add(powers.get(0));
				}
				powers.remove(0);
			}
		}

		// Add selected powers if enough point
		for (OccultismPower power : powersToAdd) {
			// Enough points
			if (totalPowers >= TOTAL_PDF_PSI_ROWS) {
				RandomGenerationLog.info(this.getClass().getName(), "No more psi power room is left.");
				break;
			}
			if (remainingPoints - power.getLevel() * CostCalculator.PATH_LEVEL_COST >= 0) {
				getCharacterPlayer().addOccultismPower(power);
				RandomGenerationLog.info(this.getClass().getName(), "Assinged power '" + power + "' to path '" + path
						+ "'.");
				remainingPoints -= power.getLevel() * CostCalculator.PATH_LEVEL_COST;
				totalPowers++;
			}

		}
	}
}
