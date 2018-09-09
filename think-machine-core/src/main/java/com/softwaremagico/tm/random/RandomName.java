package com.softwaremagico.tm.random;

import java.util.Set;
import java.util.TreeMap;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.character.CharacterPlayer;
import com.softwaremagico.tm.character.Name;
import com.softwaremagico.tm.character.factions.FactionGroup;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.race.InvalidRaceException;
import com.softwaremagico.tm.random.exceptions.InvalidRandomElementSelectedException;
import com.softwaremagico.tm.random.selectors.IRandomPreferences;

public class RandomName extends RandomSelector<Name> {
	private final static int GOOD_PROBABILITY = 1;

	protected RandomName(CharacterPlayer characterPlayer, Set<IRandomPreferences> preferences) throws InvalidXmlElementException {
		super(characterPlayer, preferences);
	}

	public void assignName() throws InvalidRaceException, InvalidRandomElementSelectedException {
		try {
			getCharacterPlayer().getInfo().setName(selectElementByWeight());
		} catch (InvalidRandomElementSelectedException e) {
			throw new InvalidRandomElementSelectedException("No possible name for faction '" + getCharacterPlayer().getFaction() + "' at '"
					+ getCharacterPlayer().getInfo().getPlanet() + "'.", e);
		}
	}

	@Override
	protected TreeMap<Integer, Name> assignElementsWeight() throws InvalidXmlElementException {
		TreeMap<Integer, Name> weightedNames = new TreeMap<>();
		int count = 1;
		for (Name name : FactionsFactory.getInstance().getAllNames()) {
			int weight = getWeight(name);
			if (weight > 0) {
				weightedNames.put(count, name);
				count += weight;
			}
		}
		return weightedNames;
	}

	@Override
	protected int getWeight(Name name) {
		// Only names of its gender.
		if (!name.getGender().equals(getCharacterPlayer().getInfo().getGender())) {
			return 0;
		}
		// Nobility almost always names of her planet.
		if (getCharacterPlayer().getFaction() != null && getCharacterPlayer().getFaction().getFactionGroup() == FactionGroup.NOBILITY) {
			if (getCharacterPlayer().getFaction().equals(name.getFaction())) {
				return GOOD_PROBABILITY;
			} else {
				return 0;
			}
		}
		// Not nobility, use names available on the planet.
		if (getCharacterPlayer().getInfo().getPlanet() != null && !getCharacterPlayer().getInfo().getPlanet().getNames().isEmpty()) {
			if (getCharacterPlayer().getInfo().getPlanet().getFactions().contains(name.getFaction())) {
				return GOOD_PROBABILITY;
			} else {
				return 0;
			}
		}
		return 1;
	}
}
