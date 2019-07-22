package com.softwaremagico.tm.character.combat;

/*-
 * #%L
 * Think Machine (Core)
 * %%
 * Copyright (C) 2018 Softwaremagico
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

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.character.benefices.AvailableBenefice;
import com.softwaremagico.tm.character.benefices.BeneficeGroup;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.character.values.IValue;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.log.SuppressFBWarnings;

public class CombatStyleFactory extends XmlFactory<CombatStyle> {
	private static final ITranslator translatorCombatStyle = LanguagePool.getTranslator("combat_styles.xml");

	private static final String NAME = "name";
	private static final String COMBAT_STYLE_GROUP = "combatStyleGroup";

	private static final String COMBAT_ACTIONS = "combatActions";
	private static final String COMBAT_ACTION_REQUIREMENTS = "requirements";
	private static final String COMBAT_ACTION_REQUIREMENTS_SKILL = "skill";
	private static final String COMBAT_ACTION_REQUIREMENTS_VALUE = "value";
	private static final String COMBAT_ACTION_GOAL = "goal";
	private static final String COMBAT_ACTION_DAMAGE = "damage";
	private static final String COMBAT_ACTION_OTHERS = "others";

	private static final String STANCES = "stances";
	private static final String STANCE_NAME = "name";
	private static final String STANCE_DESCRIPTION = "description";

	private static class CombatStyleFactoryInit {
		public static final CombatStyleFactory INSTANCE = new CombatStyleFactory();
	}

	public static CombatStyleFactory getInstance() {
		return CombatStyleFactoryInit.INSTANCE;
	}

	@Override
	public void clearCache() {
		super.clearCache();
	}

	@Override
	protected ITranslator getTranslator() {
		return translatorCombatStyle;
	}

	@Override
	@SuppressFBWarnings("REC_CATCH_EXCEPTION")
	protected CombatStyle createElement(ITranslator translator, String combatStyleId, String language)
			throws InvalidXmlElementException {
		try {
			final String name = translator.getNodeValue(combatStyleId, NAME, language);
			final CombatStyleGroup combatStyleGroup = CombatStyleGroup
					.get(translator.getNodeValue(combatStyleId, COMBAT_STYLE_GROUP));
			if (combatStyleGroup == null) {
				throw new InvalidCombatStyleException("Invalid group in combat style '" + combatStyleId + "'.");
			}

			final CombatStyle combatStyle = new CombatStyle(combatStyleId, name, language, combatStyleGroup);

			// Adding combat actions
			final Set<String> combatActionsIds = translator.getAllChildrenTags(combatStyleId, COMBAT_ACTIONS);
			for (final String combatActionId : combatActionsIds) {
				final String combatActionName = translator.getNodeValue(combatActionId, NAME, language);

				// Set requirements
				final Set<CombatActionRequirement> requirements = new HashSet<>();
				final Set<String> combatActionRequirements = translator.getAllChildrenTags(combatActionId,
						COMBAT_ACTION_REQUIREMENTS);
				for (final String combatActionRequirementId : combatActionRequirements) {
					final String skillNames = translator.getNodeValue(combatActionId, COMBAT_ACTION_REQUIREMENTS,
							combatActionRequirementId, COMBAT_ACTION_REQUIREMENTS_SKILL);

					final Set<IValue> restrictions = new HashSet<>();
					try {
						final StringTokenizer skillTokenizer = new StringTokenizer(skillNames, ",");
						while (skillTokenizer.hasMoreTokens()) {
							final String skillName = skillTokenizer.nextToken().trim();
							try {
								restrictions.add(AvailableSkillsFactory.getInstance().getElement(skillName, language));
							} catch (InvalidXmlElementException e) {
								// Maybe is a characteristic.
								try {
									restrictions.add(CharacteristicsDefinitionFactory.getInstance()
											.getElement(skillName, language));
								} catch (InvalidXmlElementException e2) {
									throw new InvalidCombatStyleException("Invalid requirement '" + skillName
											+ "' in combat style '" + combatStyleId + "'.", e2);
								}
							}
						}
					} catch (NullPointerException e) {
						throw new InvalidCombatStyleException(
								"Invalid requirement '" + combatActionRequirementId + "' for skills '" + skillNames
										+ "' in '" + combatActionId + "' at combat style '" + combatStyleId + "'.",
								e);
					}

					try {
						final String skillValue = translator.getNodeValue(combatActionId, COMBAT_ACTION_REQUIREMENTS,
								combatActionRequirementId, COMBAT_ACTION_REQUIREMENTS_VALUE);
						final CombatActionRequirement combatActionRequirement = new CombatActionRequirement(
								restrictions, Integer.parseInt(skillValue));
						requirements.add(combatActionRequirement);
					} catch (NumberFormatException e) {
						throw new InvalidCombatStyleException("Invalid requirement value in '" + combatActionId
								+ "' at combat style '" + combatStyleId + "'.", e);
					}
				}

				String combatActionGoal = "";
				try {
					combatActionGoal = translator.getNodeValue(combatActionId, COMBAT_ACTION_GOAL);
				} catch (Exception e) {
					// Not mandatory
				}

				String combatActionDamage = "";
				try {
					combatActionDamage = translator.getNodeValue(combatActionId, COMBAT_ACTION_DAMAGE);
				} catch (Exception e) {
					// Not mandatory
				}

				String combatActionOthers = "";
				try {
					combatActionOthers = translator.getNodeValue(combatActionId, COMBAT_ACTION_OTHERS, language);
				} catch (Exception e) {
					// Not mandatory
				}

				final CombatAction combatAction = new CombatAction(combatActionId, combatActionName, language,
						combatActionGoal, combatActionDamage, combatActionOthers, requirements);
				combatStyle.addCombatAction(combatAction);
			}

			// Adding stances.
			final Set<String> stancesIds = translator.getAllChildrenTags(combatStyleId, STANCES);
			for (final String stanceId : stancesIds) {
				String stanceName = "";
				try {
					stanceName = translator.getNodeValue(stanceId, STANCE_NAME, language);
				} catch (Exception e) {
					throw new InvalidCombatStyleException("Invalid name in stance '" + stanceId + "'.", e);
				}

				String stanceDescription = "";
				try {
					stanceDescription = translator.getNodeValue(stanceId, STANCE_DESCRIPTION, language);
				} catch (Exception e) {
					throw new InvalidCombatStyleException("Invalid description in stance '" + stanceId + "'.", e);
				}

				final CombatStance stance = new CombatStance(stanceId, stanceName, language, stanceDescription);
				combatStyle.addCombatStance(stance);
			}

			return combatStyle;
		} catch (Exception e) {
			throw new InvalidCombatStyleException("Invalid structure in combat style '" + combatStyleId + "'.", e);
		}
	}

	public CombatStyle getCombatStyle(AvailableBenefice beneficeDefinition, String language)
			throws InvalidXmlElementException {
		if (beneficeDefinition.getBeneficeDefinition().getGroup() == BeneficeGroup.FIGHTING) {
			return getElement(beneficeDefinition.getId(), language);
		}
		return null;
	}
}
