package com.softwaremagico.tm.character.occultism;

/*-
 * #%L
 * Think Machine (Core)
 * %%
 * Copyright (C) 2017 Softwaremagico
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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.StringTokenizer;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.character.characteristics.CharacteristicName;
import com.softwaremagico.tm.character.characteristics.CharacteristicsDefinitionFactory;
import com.softwaremagico.tm.character.factions.Faction;
import com.softwaremagico.tm.character.factions.FactionsFactory;
import com.softwaremagico.tm.character.skills.SkillsDefinitionsFactory;
import com.softwaremagico.tm.character.values.IValue;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;
import com.softwaremagico.tm.log.MachineLog;

public class OccultismPathFactory extends XmlFactory<OccultismPath> {
	private final static ITranslator translatorBlessing = LanguagePool.getTranslator("occultismPowers.xml");

	private final static String NAME = "name";
	private final static String TYPE = "type";
	private final static String FACTIONS = "factions";

	private final static String OCCULTISM_POWER = "powers";
	private final static String POWER_NAME = "name";
	private final static String POWER_LEVEL = "level";
	private final static String POWER_CHARACTERISTIC = "characteristic";
	private final static String POWER_SKILL = "skill";
	private final static String POWER_RANGE = "range";
	private final static String POWER_DURATION = "duration";
	private final static String POWER_WYRD = "wyrd";
	private final static String POWER_COMPONENTS = "components";

	private final static String VARIABLE_WYRD = "variable";

	private static OccultismPathFactory instance;

	private final Set<OccultismPath> psiPaths;
	private final Set<OccultismPath> theurgyPaths;

	private OccultismPathFactory() {
		psiPaths = new HashSet<>();
		theurgyPaths = new HashSet<>();
	}

	private static void createInstance() {
		if (instance == null) {
			synchronized (OccultismPathFactory.class) {
				if (instance == null) {
					instance = new OccultismPathFactory();
				}
			}
		}
	}

	public static OccultismPathFactory getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}

	@Override
	public void clearCache() {
		psiPaths.clear();
		theurgyPaths.clear();
		super.clearCache();
	}

	@Override
	protected OccultismPath createElement(ITranslator translator, String occultismId, String language)
			throws InvalidXmlElementException {

		try {
			String name = translator.getNodeValue(occultismId, NAME, language);

			String typeName = translator.getNodeValue(occultismId, TYPE);
			OccultismType occultismType = OccultismTypeFactory.getInstance().getElement(typeName, language);
			if (occultismType == null) {
				throw new InvalidOccultismPowerException("No type defined for '" + occultismId + "'.");
			}

			String factionsList = translator.getNodeValue(occultismId, FACTIONS);

			Set<Faction> factions = new HashSet<>();
			if (factionsList != null) {
				StringTokenizer factionTokenizer = new StringTokenizer(factionsList, ",");
				while (factionTokenizer.hasMoreTokens()) {
					factions.add(FactionsFactory.getInstance()
							.getElement(factionTokenizer.nextToken().trim(), language));
				}
			}

			OccultismPath occultismPath = new OccultismPath(occultismId, name, occultismType, factions);

			for (String powerId : translator.getAllChildrenTags(occultismId, OCCULTISM_POWER)) {
				String powerName = translator.getNodeValue(powerId, POWER_NAME, language);
				String level = translator.getNodeValue(powerId, POWER_LEVEL);
				String characteristicName = translator.getNodeValue(powerId, POWER_CHARACTERISTIC);
				String skillNames = translator.getNodeValue(powerId, POWER_SKILL);
				String range = translator.getNodeValue(powerId, POWER_RANGE);
				String duration = translator.getNodeValue(powerId, POWER_DURATION);
				Integer wyrd = null;
				String components = translator.getNodeValue(powerId, POWER_COMPONENTS);
				try {
					wyrd = Integer.parseInt(translator.getNodeValue(powerId, POWER_WYRD));
				} catch (NumberFormatException nfe) {
					// Wyrd is not variable, is an error.
					if (!translator.getNodeValue(powerId, POWER_WYRD).equalsIgnoreCase(VARIABLE_WYRD)) {
						throw new InvalidOccultismPowerException("Invalid wyrd value for '"
								+ translator.getNodeValue(powerId, POWER_WYRD) + "' in power '" + powerId + "'.");
					}
				}

				List<IValue> values = new ArrayList<>();
				StringTokenizer skillTokenizer = new StringTokenizer(skillNames, ",");
				while (skillTokenizer.hasMoreTokens()) {
					String value = skillTokenizer.nextToken().trim();
					try {
						values.add(SkillsDefinitionsFactory.getInstance().getElement(value, language));
					} catch (InvalidXmlElementException iee) {
						values.add(OccultismTypeFactory.getInstance().getElement(value, language));
					}
				}

				OccultismRange occultismRange = null;
				if (range != null) {
					occultismRange = OccultismRangeFactory.getInstance().getElement(range, language);
				}

				OccultismDuration occultismDuration = null;
				if (duration != null) {
					occultismDuration = OccultismDurationFactory.getInstance().getElement(duration, language);
				}

				Set<TheurgyComponent> theurgyComponents = new HashSet<>();
				if (components != null) {
					for (int i = 0; i < components.length(); i++) {
						TheurgyComponent theurgyComponent = TheurgyComponentFactory.getInstance().getTheurgyComponent(
								components.charAt(i), language);
						if (theurgyComponent == null) {
							throw new InvalidTheurgyComponentException("Invalid theurgy component code '"
									+ components.charAt(i) + "'.");
						}
						theurgyComponents.add(theurgyComponent);
					}
				}

				OccultismPower occultismPower = new OccultismPower(powerId, powerName, CharacteristicsDefinitionFactory
						.getInstance().get(CharacteristicName.get(characteristicName), language), values,
						Integer.parseInt(level), occultismRange, occultismDuration, wyrd, theurgyComponents);

				occultismPath.getOccultismPowers().put(powerId, occultismPower);
			}

			return occultismPath;
		} catch (Exception e) {
			throw new InvalidOccultismPowerException("Invalid structure in occultism path '" + occultismId + "'.", e);
		}
	}

	@Override
	protected ITranslator getTranslator() {
		return translatorBlessing;
	}

	public OccultismPath getOccultismPath(OccultismPower power, String language) {
		try {
			for (OccultismPath occultismPath : getElements(language)) {
				if (occultismPath.getOccultismPowers().containsKey(power.getId())) {
					return occultismPath;
				}
			}
		} catch (InvalidXmlElementException e) {
			MachineLog.errorMessage(this.getClass().getName(), e);
		}
		return null;
	}

	public Set<OccultismPath> getPsiPaths(String language) {
		if (psiPaths.isEmpty()) {
			try {
				for (OccultismPath path : getElements(language)) {
					if (Objects.equals(path.getOccultismType(), OccultismTypeFactory.getPsi(language))) {
						psiPaths.add(path);
					}
				}
			} catch (InvalidXmlElementException e) {
				MachineLog.errorMessage(this.getClass().getName(), e);
			}
		}
		return Collections.unmodifiableSet(psiPaths);
	}

	public Set<OccultismPath> getTheurgyPaths(String language) {
		if (theurgyPaths.isEmpty()) {
			try {
				for (OccultismPath path : getElements(language)) {
					if (Objects.equals(path.getOccultismType(), OccultismTypeFactory.getTheurgy(language))) {
						theurgyPaths.add(path);
					}
				}
			} catch (InvalidXmlElementException e) {
				MachineLog.errorMessage(this.getClass().getName(), e);
			}
		}
		return Collections.unmodifiableSet(theurgyPaths);
	}
}
