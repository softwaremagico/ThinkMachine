package com.softwaremagico.tm.character.skills;

/**
 * An skill that already has been split in different generalizations.
 */
public class AvailableSkill extends Skill<AvailableSkill> {
	private String specialization = null;
	private SkillDefinition skillDefinition;

	public AvailableSkill(SkillDefinition skillDefinition) {
		super(skillDefinition.getName());
		this.skillDefinition = skillDefinition;
	}

	public AvailableSkill(SkillDefinition skillDefinition, String specialization) {
		this(skillDefinition);
		this.specialization = specialization;
	}

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String generalization) {
		this.specialization = generalization;
	}

	public SkillDefinition getSkillDefinition() {
		return skillDefinition;
	}

	public String getCompleteName() {
		return getCompleteName(getName(), getSpecialization());
	}

	public static String getCompleteName(String name, String specialization) {
		if (specialization == null) {
			return name;
		}
		return name + " [" + specialization + "]";
	}

	@Override
	public String toString() {
		return getCompleteName();
	}
}
