package com.softwaremagico.tm.random;

import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.character.occultism.OccultismType;
import com.softwaremagico.tm.character.occultism.OccultismTypeFactory;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.IGaussianDistribution;
import com.softwaremagico.tm.random.selectors.IRandomPreferences;
import com.softwaremagico.tm.random.selectors.PsiqueLevelPreferences;

public class RandomPsique extends RandomSelector<OccultismType> {

	protected RandomPsique(CharacterPlayer characterPlayer, Set<IRandomPreferences> preferences)
			throws InvalidXmlElementException {
		super(characterPlayer, preferences);
	}

	public void assignPsiqueLevel() throws InvalidRandomElementSelectedException, InvalidXmlElementException {
		// Select which type of psique.
		OccultismType selectedOccultismType = selectElementByWeight();
		// Select a level of psique.
		int level = assignLevelOfPsique(selectedOccultismType);
		// Assign to the character.
		getCharacterPlayer().getOccultism().setPsiqueLevel(selectedOccultismType, level);

	}

	@Override
	protected TreeMap<Integer, OccultismType> assignElementsWeight() throws InvalidXmlElementException {
		TreeMap<Integer, OccultismType> weightedPsiques = new TreeMap<>();
		int count = 1;
		for (OccultismType occultismType : OccultismTypeFactory.getInstance().getElements(
				getCharacterPlayer().getLanguage())) {
			int weight = getWeight(occultismType);
			if (weight > 0) {
				weightedPsiques.put(count, occultismType);
				count += weight;
			}
		}
		return weightedPsiques;
	}

	@Override
	protected int getWeight(OccultismType element) {
		// Church factions must have always theurgy.
		if (Objects.equals(element, OccultismTypeFactory.getPsi(getCharacterPlayer().getLanguage()))) {
			if (getCharacterPlayer().getFaction().getFactionGroup() == FactionGroup.CHURCH) {
				return 0;
			}
			// No church factions have psi.
		} else if (Objects.equals(element, OccultismTypeFactory.getTheurgy(getCharacterPlayer().getLanguage()))) {
			if (getCharacterPlayer().getFaction().getFactionGroup() != FactionGroup.CHURCH) {
				return 0;
			}
		}
		return 1;
	}

	private int assignLevelOfPsique(OccultismType psique) throws InvalidXmlElementException {
		IGaussianDistribution psiqueLevelSelector = PsiqueLevelPreferences.getSelected(getPreferences());
		int maxLevelSelected = psiqueLevelSelector.randomGaussian();
		if (maxLevelSelected > psiqueLevelSelector.maximum()) {
			maxLevelSelected = psiqueLevelSelector.maximum();
		}
		return maxLevelSelected;
	}
}
