package com.softwaremagico.tm.chracter.xp;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.character.characteristics.Characteristic;
import com.softwaremagico.tm.character.skills.AvailableSkill;
import com.softwaremagico.tm.character.skills.SkillGroup;

public class Experience {
	private int totalExperience = 0;
	// Element id, set of increases.
	private final Map<String, Set<ExperienceIncrease>> ranksIncreased;
	private int extraWyrd = 0;

	public Experience() {
		ranksIncreased = new HashMap<>();
	}

	public int getTotalExperience() {
		return totalExperience;
	}

	public Map<String, Set<ExperienceIncrease>> getRanksIncreased() {
		return Collections.unmodifiableMap(ranksIncreased);
	}

	public Set<ExperienceIncrease> getExperienceIncreased(Element<?> element) {
		if (getRanksIncreased().get(element.getId()) == null) {
			return new HashSet<>();
		}
		return getRanksIncreased().get(element.getId());
	}

	public ExperienceIncrease setExperienceIncrease(Element<?> element, int value, int cost) {
		if (ranksIncreased.get(element.getId()) == null) {
			ranksIncreased.put(element.getId(), new HashSet<>());
		}
		final ExperienceIncrease experienceIncrease = new ExperienceIncrease(element.getId(), value, cost);
		ranksIncreased.get(element.getId()).add(experienceIncrease);
		return experienceIncrease;
	}

	public void remove(ExperienceIncrease experienceIncrease) {
		if (ranksIncreased.get(experienceIncrease.getElementId()) != null) {
			ranksIncreased.get(experienceIncrease.getElementId()).remove(experienceIncrease);
		}
	}

	public static int getExperienceCostFor(Element<?> element, int valueToPurchase)
			throws ElementCannotBeUpgradeWithExperienceException {
		if (element instanceof AvailableSkill) {
			// Lore skills are cheaper
			if (((AvailableSkill) element).getSkillDefinition().getSkillGroup().equals(SkillGroup.LORE)) {
				return (int) (valueToPurchase * 1.5);
			}
			return valueToPurchase * 2;
		}
		if (element instanceof Characteristic) {
			return valueToPurchase * 3;
		}
		throw new ElementCannotBeUpgradeWithExperienceException(
				"Not upgradable element '" + element + "'. Experience cannot be used on it.");
	}

	public void setTotalExperience(int totalExperience) {
		this.totalExperience = totalExperience;
	}

	public int getExtraWyrd() {
		return extraWyrd;
	}

	public void setExtraWyrd(int extraWyrd) {
		this.extraWyrd = extraWyrd;
	}
}
