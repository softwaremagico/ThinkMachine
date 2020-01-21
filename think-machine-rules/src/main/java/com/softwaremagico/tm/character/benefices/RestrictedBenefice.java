package com.softwaremagico.tm.character.benefices;

import com.softwaremagico.tm.Element;

public class RestrictedBenefice extends Element<RestrictedBenefice> {
	private final BeneficeDefinition beneficeDefinition;
	private final int maxValue;

	public RestrictedBenefice(BeneficeDefinition beneficeDefinition, int maxValue) {
		super(beneficeDefinition.getId(), beneficeDefinition.getName(), beneficeDefinition.getLanguage(),
				beneficeDefinition.getModuleName());
		this.beneficeDefinition = beneficeDefinition;
		this.maxValue = maxValue;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public BeneficeDefinition getBeneficeDefinition() {
		return beneficeDefinition;
	}

}
