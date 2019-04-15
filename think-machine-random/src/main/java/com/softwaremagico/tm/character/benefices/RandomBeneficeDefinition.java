package com.softwaremagico.tm.character.benefices;

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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.creation.CostCalculator;
import com.softwaremagico.tm.character.creation.FreeStyleCharacterCreation;
import com.softwaremagico.tm.log.RandomGenerationLog;
import com.softwaremagico.tm.random.RandomSelector;
import com.softwaremagico.tm.random.exceptions.ImpossibleToAssignMandatoryElementException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.DifficultLevelPreferences;
import com.softwaremagico.tm.random.selectors.IGaussianDistribution;
import com.softwaremagico.tm.random.selectors.IRandomPreference;
import com.softwaremagico.tm.random.selectors.StatusPreferences;
import com.softwaremagico.tm.random.selectors.TraitCostPreferences;

public class RandomBeneficeDefinition extends RandomSelector<BeneficeDefinition> {
	private static final int MAX_AFFLICTIONS = 2;
	private static final String CASH_BENEFICE_ID = "cash";

	public RandomBeneficeDefinition(CharacterPlayer characterPlayer, Set<IRandomPreference> preferences) throws InvalidXmlElementException {
		super(characterPlayer, preferences);
	}

	@Override
	public void assign() throws InvalidXmlElementException, InvalidRandomElementSelectedException {

		// Later, the others.
		while (CostCalculator.getBeneficesCosts(getCharacterPlayer()) < FreeStyleCharacterCreation.getTraitsPoints(getCharacterPlayer().getInfo().getAge())
				&& !getWeightedElements().isEmpty()) {
			// Select a benefice
			BeneficeDefinition selectedBenefice = selectElementByWeight();
			assignBenefice(
					selectedBenefice,
					FreeStyleCharacterCreation.getTraitsPoints(getCharacterPlayer().getInfo().getAge())
							- CostCalculator.getBeneficesCosts(getCharacterPlayer()));
			// Only one fighting style by character.
			if (selectedBenefice.getGroup().equals(BeneficeGroup.FIGHTING)) {
				for (BeneficeDefinition beneficeDefinition : BeneficeDefinitionFactory.getInstance().getBenefices(BeneficeGroup.FIGHTING,
						getCharacterPlayer().getLanguage())) {
					removeElementWeight(beneficeDefinition);
				}
			}
		}
	}

	protected void assignBenefice(BeneficeDefinition selectedBenefice, int maxPoints) throws InvalidXmlElementException {
		// Select the range of the benefice.
		AvailableBenefice selectedBeneficeWithLevel = assignLevelOfBenefice(selectedBenefice, maxPoints);
		if (selectedBeneficeWithLevel != null) {
			// Only a few afflictions.
			if (selectedBeneficeWithLevel.getBeneficeClassification() == BeneficeClassification.AFFLICTION) {
				if (getCharacterPlayer().getAfflictions().size() < MAX_AFFLICTIONS) {
					try {
						getCharacterPlayer().addBenefice(selectedBeneficeWithLevel);
					} catch (BeneficeAlreadyAddedException e) {
						// Not add it again.
					}
				}
			} else {
				try {
					getCharacterPlayer().addBenefice(selectedBeneficeWithLevel);
					RandomGenerationLog.info(this.getClass().getName(), "Added benefice '" + selectedBeneficeWithLevel + "'.");
				} catch (BeneficeAlreadyAddedException e) {
					// If level is bigger... replace it.
					AvailableBenefice originalBenefice = getCharacterPlayer().getBenefice(selectedBenefice.getId());
					if (originalBenefice.getCost() < selectedBeneficeWithLevel.getCost()) {
						getCharacterPlayer().removeBenefice(originalBenefice);
						try {
							getCharacterPlayer().addBenefice(selectedBeneficeWithLevel);
							RandomGenerationLog.info(this.getClass().getName(), "Replacing benefice '" + originalBenefice + "' with '"
									+ selectedBeneficeWithLevel + "'.");
						} catch (BeneficeAlreadyAddedException e1) {
							RandomGenerationLog.errorMessage(this.getClass().getName(), e1);
						}
					}
				}
			}
		}
		removeElementWeight(selectedBenefice);
	}

	@Override
	protected Collection<BeneficeDefinition> getAllElements() throws InvalidXmlElementException {
		return BeneficeDefinitionFactory.getInstance().getElements(getCharacterPlayer().getLanguage());
	}

	@Override
	protected int getWeight(BeneficeDefinition benefice) throws InvalidRandomElementSelectedException {
		// No restricted benefices.
		if (benefice.getRestrictedFactionGroup() != null && getCharacterPlayer().getFaction() != null
				&& benefice.getRestrictedFactionGroup() != getCharacterPlayer().getFaction().getFactionGroup()) {
			throw new InvalidRandomElementSelectedException("Benefice '" + benefice + "' is restricted to '" + benefice.getRestrictedFactionGroup() + "'.");
		}

		// No special benefices
		if (benefice.getGroup() == BeneficeGroup.RESTRICTED) {
			throw new InvalidRandomElementSelectedException("Benefice '" + benefice + "' is restricted.");
		}

		// PNJs likes money changes.
		if (benefice.getId().equalsIgnoreCase(CASH_BENEFICE_ID)) {
			return GOOD_PROBABILITY;
		}

		// No faction preference selected. All benefices has the same
		// probability.
		return 1;
	}

	/**
	 * Returns a cost for a benefice depending on the preferences of the
	 * character.
	 * 
	 * @param benefice
	 * @param maxPoints
	 * @return
	 * @throws InvalidXmlElementException
	 */
	private AvailableBenefice assignLevelOfBenefice(BeneficeDefinition benefice, int maxPoints) throws InvalidXmlElementException {
		IGaussianDistribution selectedTraitCost = TraitCostPreferences.getSelected(getPreferences());

		if (benefice.getId().equalsIgnoreCase(CASH_BENEFICE_ID)) {
			DifficultLevelPreferences difficultPreferences = DifficultLevelPreferences.getSelected(getPreferences());
			switch (difficultPreferences) {
			case EASY:
			case VERY_EASY:
				selectedTraitCost = TraitCostPreferences.LOW;
				break;
			case MEDIUM:
			case HARD:
				// Be careful. maxPoints can limit this value.
				selectedTraitCost = TraitCostPreferences.GOOD;
				break;
			case VERY_HARD:
				// Be careful. maxPoints can limit this value.
				selectedTraitCost = TraitCostPreferences.VERY_HIGH;
				break;
			}
		}

		if (benefice.getGroup() != null && benefice.getGroup().equals(BeneficeGroup.STATUS)) {
			// Status has also an special preference.
			IGaussianDistribution selectedStatus = StatusPreferences.getSelected(getPreferences());
			if (selectedStatus != null) {
				selectedTraitCost = selectedStatus;
			}
		}

		int maxRangeSelected = selectedTraitCost.randomGaussian();
		if (maxRangeSelected > maxPoints) {
			maxRangeSelected = maxPoints;
		}

		RandomGenerationLog.info(this.getClass().getName(), "MaxPoints of '" + benefice + "' are '" + maxRangeSelected + "'.");
		Set<AvailableBenefice> beneficeLevels = AvailableBeneficeFactory.getInstance().getAvailableBeneficesByDefinition(getCharacterPlayer().getLanguage(),
				benefice);
		if (beneficeLevels == null) {
			beneficeLevels = new HashSet<>();
		}
		List<AvailableBenefice> sortedBenefices = new ArrayList<>(beneficeLevels);
		// Sort by cost (descending). Adding if a benefice has preferences
		// (ascending).
		Collections.sort(sortedBenefices, new Comparator<AvailableBenefice>() {

			@Override
			public int compare(AvailableBenefice o1, AvailableBenefice o2) {
				double o1Preferred = getRandomDefinitionBonus(o1.getRandomDefinition());
				double o2Preferred = getRandomDefinitionBonus(o2.getRandomDefinition());

				if ((int) (o1Preferred - o2Preferred) != 0) {
					return (int) (o1Preferred - o2Preferred);
				}
				return new Integer(o2.getCost()).compareTo(new Integer(o1.getCost()));
			}
		});
		RandomGenerationLog.info(this.getClass().getName(), "Available benefice levels of '" + benefice + "' are '" + sortedBenefices + "'.");
		for (AvailableBenefice availableBenefice : sortedBenefices) {
			try {
				validateElement(availableBenefice.getRandomDefinition());
			} catch (InvalidRandomElementSelectedException e) {
				continue;
			}
			if (Math.abs(availableBenefice.getCost()) <= maxRangeSelected) {
				return availableBenefice;
			}
		}
		return null;
	}

	@Override
	protected void assignIfMandatory(BeneficeDefinition benefice) throws InvalidXmlElementException, ImpossibleToAssignMandatoryElementException {
		// Set status of the character.
		try {
			if ((benefice.getGroup() != null && benefice.getGroup().equals(BeneficeGroup.STATUS)) && getWeight(benefice) > 0
					&& getCharacterPlayer().getFaction() != null
					&& Objects.equals(benefice.getRestrictedFactionGroup(), getCharacterPlayer().getFaction().getFactionGroup())) {
				IGaussianDistribution selectedStatus = StatusPreferences.getSelected(getPreferences());
				if (selectedStatus != null) {
					RandomGenerationLog.debug(this.getClass().getName(), "Searching grade '" + selectedStatus.maximum() + "' of benefice '" + benefice + "'.");
					assignBenefice(benefice, selectedStatus.maximum());
				}
			}
		} catch (InvalidRandomElementSelectedException e) {
			// Weight is zero. Do nothing.
		}
	}

	@Override
	protected void assignMandatoryValues(Set<BeneficeDefinition> mandatoryValues) throws InvalidXmlElementException {
		return;
	}
}
