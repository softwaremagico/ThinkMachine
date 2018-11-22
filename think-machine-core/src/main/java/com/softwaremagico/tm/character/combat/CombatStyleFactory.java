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
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.skills.AvailableSkillsFactory;
import com.softwaremagico.tm.character.values.IValue;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;

public class CombatStyleFactory extends XmlFactory<CombatStyle> {
	private final static ITranslator translatorCombatStyle = LanguagePool.getTranslator("combat_styles.xml");

	private final static String NAME = "name";
	private final static String COMBAT_STYLE_GROUP = "combatStyleGroup";

	private final static String COMBAT_ACTIONS = "combatActions";
	private final static String COMBAT_ACTION_REQUIREMENTS = "requirements";
	private final static String COMBAT_ACTION_REQUIREMENTS_SKILL = "skill";
	private final static String COMBAT_ACTION_REQUIREMENTS_VALUE = "value";
	private final static String COMBAT_ACTION_GOAL = "goal";
	private final static String COMBAT_ACTION_DAMAGE = "damage";
	private final static String COMBAT_ACTION_OTHERS = "others";

	private final static String STANCES = "stances";
	private final static String STANCE_NAME = "name";
	private final static String STANCE_DESCRIPTION = "description";

	private static CombatStyleFactory instance;

	private static void createInstance() {
		if (instance == null) {
			synchronized (CombatStyleFactory.class) {
				if (instance == null) {
					instance = new CombatStyleFactory();
				}
			}
		}
	}

	public static CombatStyleFactory getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
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
	protected CombatStyle createElement(ITranslator translator, String combatStyleId, String language) throws InvalidXmlElementException {
		try {
			String name = translator.getNodeValue(combatStyleId, NAME, language);
			CombatStyleGroup combatStyleGroup = CombatStyleGroup.get(translator.getNodeValue(combatStyleId, COMBAT_STYLE_GROUP));
			if (combatStyleGroup == null) {
				throw new InvalidCombatStyleException("Invalid group in combat style '" + combatStyleId + "'.");
			}

			CombatStyle combatStyle = new CombatStyle(combatStyleId, name, language, combatStyleGroup);

			// Adding combat actions
			Set<String> combatActionsIds = translator.getAllChildrenTags(combatStyleId, COMBAT_ACTIONS);
			for (String combatActionId : combatActionsIds) {
				String combatActionName = translator.getNodeValue(combatActionId, NAME, language);

				// Set requirements
				Set<CombatActionRequirement> requirements = new HashSet<>();
				Set<String> combatActionRequirements = translator.getAllChildrenTags(combatActionId, COMBAT_ACTION_REQUIREMENTS);
				for (String combatActionRequirementId : combatActionRequirements) {
					String skillNames = translator.getNodeValue(combatActionId, COMBAT_ACTION_REQUIREMENTS, combatActionRequirementId,
							COMBAT_ACTION_REQUIREMENTS_SKILL);

					Set<IValue> restrictions = new HashSet<>();
					StringTokenizer skillTokenizer = new StringTokenizer(skillNames, ",");
					while (skillTokenizer.hasMoreTokens()) {
						String skillName = skillTokenizer.nextToken().trim();
						try {
							restrictions.add(AvailableSkillsFactory.getInstance().getElement(skillName, language));
						} catch (InvalidXmlElementException e) {
							// Maybe is a characteristic.
							try {
								restrictions.add(CharacteristicsDefinitionFactory.getInstance().getElement(skillName, language));
							} catch (InvalidXmlElementException e2) {
								throw new InvalidCombatStyleException("Invalid requirement '" + skillName + "' in combat style '" + combatStyleId + "'.", e2);
							}
						}
					}

					try {
						String skillValue = translator.getNodeValue(combatActionId, COMBAT_ACTION_REQUIREMENTS, combatActionRequirementId,
								COMBAT_ACTION_REQUIREMENTS_VALUE);
						CombatActionRequirement combatActionRequirement = new CombatActionRequirement(restrictions, Integer.parseInt(skillValue));
						requirements.add(combatActionRequirement);
					} catch (NumberFormatException e) {
						throw new InvalidCombatStyleException("Invalid requirement value in '" + combatActionId + "' at combat style '" + combatStyleId + "'.",
								e);
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

				CombatAction combatAction = new CombatAction(combatActionId, combatActionName, language, combatActionGoal, combatActionDamage,
						combatActionOthers, requirements);
				combatStyle.addCombatAction(combatAction);
			}

			// Adding stances.
			Set<String> stancesIds = translator.getAllChildrenTags(combatStyleId, STANCES);
			for (String stanceId : stancesIds) {
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

				CombatStance stance = new CombatStance(stanceId, stanceName, language, stanceDescription);
				combatStyle.addCombatStance(stance);
			}

			return combatStyle;
		} catch (Exception e) {
			throw new InvalidCombatStyleException("Invalid structure in combat style '" + combatStyleId + "'.", e);
		}
	}
}
