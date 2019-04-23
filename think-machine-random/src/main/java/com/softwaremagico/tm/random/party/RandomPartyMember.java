package com.softwaremagico.tm.random.party;

import java.util.Set;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.random.profiles.RandomProfile;
import com.softwaremagico.tm.random.selectors.IRandomPreference;

public class RandomPartyMember extends Element<RandomPartyMember> {
	private final RandomProfile randomProfile;
	private final Integer minNumber;
	private final Integer maxNumber;
	private final Float weight;
	private final Set<IRandomPreference> randomPreferences;

	public RandomPartyMember(String id, String name, String language, RandomProfile randomProfile, Integer minNumber,
			Integer maxNumber, Float weight, Set<IRandomPreference> randomPreferences) {
		super(id, name, language);
		this.randomProfile = randomProfile;
		this.minNumber = minNumber;
		this.maxNumber = maxNumber;
		this.weight = weight;
		this.randomPreferences = randomPreferences;
	}

	public RandomProfile getRandomProfile() {
		return randomProfile;
	}

	public Integer getMinNumber() {
		return minNumber;
	}

	public Integer getMaxNumber() {
		return maxNumber;
	}

	public Float getWeight() {
		return weight;
	}

	public Set<IRandomPreference> getRandomPreferences() {
		return randomPreferences;
	}

}
