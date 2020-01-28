package com.softwaremagico.tm.character.lifepaths;

import com.softwaremagico.tm.InvalidXmlElementException;
import com.softwaremagico.tm.XmlFactory;
import com.softwaremagico.tm.language.ITranslator;

public class LifePathFactory extends XmlFactory<LifePath> {
	private static final String TRANSLATOR_FILE = "life_paths.xml";
	private static final String NAME = "name";

	private static class PlanetFactoryInit {
		public static final LifePathFactory INSTANCE = new LifePathFactory();
	}

	public static LifePathFactory getInstance() {
		return PlanetFactoryInit.INSTANCE;
	}

	@Override
	protected LifePath createElement(ITranslator translator, String lifePathtId, String language, String moduleName)
			throws InvalidXmlElementException {
		try {
			final String name = translator.getNodeValue(lifePathtId, NAME, language);

			return new LifePath(lifePathtId, name, language, moduleName);
		} catch (Exception e) {
			throw new InvalidLifePathException("Invalid structure in life path '" + lifePathtId + "'.", e);
		}
	}

	@Override
	protected String getTranslatorFile() {
		return TRANSLATOR_FILE;
	}
}
