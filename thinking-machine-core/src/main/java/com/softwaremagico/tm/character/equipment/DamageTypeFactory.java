package com.softwaremagico.tm.character.equipment;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.language.ITranslator;
import com.softwaremagico.tm.language.LanguagePool;

public class DamageTypeFactory extends XmlFactory<DamageType> {
	private final static ITranslator translatorDamage = LanguagePool.getTranslator("damage.xml");

	private final static String NAME = "name";

	private static DamageTypeFactory instance;

	private static void createInstance() {
		if (instance == null) {
			synchronized (DamageTypeFactory.class) {
				if (instance == null) {
					instance = new DamageTypeFactory();
				}
			}
		}
	}

	public static DamageTypeFactory getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}

	@Override
	protected ITranslator getTranslator() {
		return translatorDamage;
	}

	@Override
	protected DamageType createElement(ITranslator translator, String damageId, String language) throws InvalidXmlElementException {
		DamageType damageType = null;
		try {
			String name = translator.getNodeValue(damageId, NAME, language);
			damageType = new DamageType(damageId, name);
		} catch (Exception e) {
			throw new InvalidWeaponException("Invalid name in damage '" + damageId + "'.");
		}
		return damageType;
	}

}
