package com.softwaremagico.tm.language;

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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.softwaremagico.tm.file.Path;
import com.softwaremagico.tm.log.MachineLog;

public class Translator implements ITranslator {
	public final static String DEFAULT_LANGUAGE = "en";
	private final static String LANGUAGES_FILE = "languages.xml";
	private Document doc = null;
	private boolean errorShowed = false;
	private boolean retried = false;
	private boolean showedMessage = false;
	private static List<Language> languagesList = null;
	private static HashMap<String, HashMap<String, String>> tagTranslations;
	private static String language = DEFAULT_LANGUAGE;

	public Translator(String filePath) {
		tagTranslations = new HashMap<>();
		doc = parseFile(doc, filePath);
	}

	/**
	 * Parse the file
	 *
	 * @param tagName
	 *            Tag of the data to be read
	 */
	private static Document parseFile(Document usedDoc, String filePath) {
		DocumentBuilderFactory dbf;
		DocumentBuilder db;
		try {
			File file = new File(filePath);
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			usedDoc = db.parse(file);
			usedDoc.getDocumentElement().normalize();
		} catch (SAXParseException ex) {
			String text = "Parsing error" + ".\n Line: " + ex.getLineNumber() + "\nUri: " + ex.getSystemId()
					+ "\nMessage: " + ex.getMessage();
			MachineLog.severe(Translator.class.getName(), text);
			MachineLog.errorMessage(Translator.class.getName(), ex);
		} catch (SAXException ex) {
			MachineLog.errorMessage(Translator.class.getName(), ex);
		} catch (ParserConfigurationException ex) {
			MachineLog.errorMessage(Translator.class.getName(), ex);
		} catch (FileNotFoundException fnf) {
			String text = "The file "
					+ filePath
					+ " containing the translations is not found. Please, check your program files and put the translation XML files on the \"translations\" folder.";
			System.out.println(text);
		} catch (IOException ex) {
			Logger.getLogger(Translator.class.getName()).log(Level.SEVERE, null, ex);
		}
		return usedDoc;
	}

	@Override
	public String getTranslatedText(String tag, String language) {
		return getTranslatedText(tag, language, null);
	}

	@Override
	public String getTranslatedText(String tag) {
		return getTranslatedText(tag, language, null);
	}

	@Override
	public String getTranslatedText(String tag, String[] args) {
		return getTranslatedText(tag, language, args);
	}

	@Override
	public String getTranslatedText(String tag, String language, Object[] args) {
		if (tagTranslations.get(language) == null) {
			tagTranslations.put(language, new HashMap<String, String>());
		}

		if (tagTranslations.get(language).get(tag) == null) {
			tagTranslations.get(language).put(tag, readTag(tag, language));
		}
		try {
			if (args != null) {
				return String.format(tagTranslations.get(language).get(tag), args);

			} else {
				return tagTranslations.get(language).get(tag);
			}
		} catch (NullPointerException npe) {
			return null;
		}
	}

	@Override
	public List<String> getAllTranslatedElements() {
		List<String> nodes = new ArrayList<>();
		Element element = (Element) (doc.getDocumentElement());
		NodeList nodeList = element.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			// Remove text values
			if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
				nodes.add(nodeList.item(i).getNodeName());
			}
		}
		return nodes;
	}

	@Override
	public String getNodeValue(String tag, String node) {
		return getNodeValue(tag, node, 0);
	}

	@Override
	public String getNodeValue(String tag, String node, int nodeNumber) {
		NodeList nodeList = doc.getElementsByTagName(tag);
		for (int child = 0; child < nodeList.getLength(); child++) {
			Node firstNode = nodeList.item(child);
			// Remove text values
			if (firstNode.getNodeType() == Node.ELEMENT_NODE) {
				Element firstElement = (Element) firstNode;
				try {
					NodeList firstNodeElementList = firstElement.getElementsByTagName(node);
					Element firstNodeElement = (Element) firstNodeElementList.item(0);
					return firstNodeElement.getChildNodes().item(nodeNumber).getNodeValue().trim();
				} catch (NullPointerException npe) {
					return null;
				}
			}
		}
		return null;
	}

	@Override
	public String getNodeValue(String parent, String tag, String node) {
		return getNodeValue(parent, tag, node, 0);
	}

	@Override
	public String getNodeValue(String parent, String tag, String node, int nodeNumber) {
		NodeList nodeList = doc.getElementsByTagName(parent);
		for (int child = 0; child < nodeList.getLength(); child++) {
			Node parentNode = nodeList.item(child);
			// Remove text values
			if (parentNode.getNodeType() == Node.ELEMENT_NODE) {
				Element parentElement = (Element) parentNode;
				try {
					NodeList childrenElementList = parentElement.getElementsByTagName(tag);
					Element childrenElement = (Element) childrenElementList.item(nodeNumber);
					try {
						NodeList firstNodeElementList = childrenElement.getElementsByTagName(node);
						Element firstNodeElement = (Element) firstNodeElementList.item(0);
						return firstNodeElement.getChildNodes().item(0).getNodeValue().trim();
					} catch (NullPointerException npe) {
						return null;
					}
				} catch (NullPointerException npe) {
					return null;
				}
			}
		}
		return null;
	}

	@Override
	public String getNodeValue(String grandparent, String parent, String tag, String node) {
		NodeList nodeList = doc.getElementsByTagName(grandparent);
		for (int child = 0; child < nodeList.getLength(); child++) {
			Node grandParentNode = nodeList.item(child);
			// Remove text values
			if (grandParentNode.getNodeType() == Node.ELEMENT_NODE) {
				Element grandParentElement = (Element) grandParentNode;
				try {
					NodeList parentElementList = grandParentElement.getElementsByTagName(parent);
					Element parentElement = (Element) parentElementList.item(0);
					try {
						NodeList childrenElementList = parentElement.getElementsByTagName(tag);
						Element childrenElement = (Element) childrenElementList.item(0);
						try {
							NodeList firstNodeElementList = childrenElement.getElementsByTagName(node);
							Element firstNodeElement = (Element) firstNodeElementList.item(0);
							return firstNodeElement.getChildNodes().item(0).getNodeValue().trim();
						} catch (NullPointerException npe) {
							return null;
						}
					} catch (NullPointerException npe) {
						return null;
					}
				} catch (NullPointerException npe) {
					return null;
				}
			}
		}
		return null;
	}

	@Override
	public Set<String> getAllChildrenTags(String parent, String group) {
		Set<String> childrenTags = new HashSet<>();
		NodeList nodeList = doc.getElementsByTagName(parent);
		for (int child = 0; child < nodeList.getLength(); child++) {
			Node parentNode = nodeList.item(child);
			// Remove text values
			if (parentNode.getNodeType() == Node.ELEMENT_NODE) {
				Element parentElement = (Element) parentNode;
				try {
					NodeList groupList = parentElement.getElementsByTagName(group);
					Element groupElement = (Element) groupList.item(0);
					try {
						NodeList childrenList = groupElement.getChildNodes();
						for (int childIndex = 0; childIndex < childrenList.getLength(); childIndex++) {
							Node childNode = childrenList.item(childIndex);
							// Remove text values
							if (childNode.getNodeType() == Node.ELEMENT_NODE) {
								childrenTags.add(childNode.getNodeName());
							}
						}
					} catch (NullPointerException npe) {
						return childrenTags;
					}
				} catch (NullPointerException npe) {
					return childrenTags;
				}
			}
		}
		return childrenTags;
	}

	private String readTag(String tag, String language) {
		try {
			NodeList nodeList = doc.getElementsByTagName(tag);
			for (int child = 0; child < nodeList.getLength(); child++) {
				Node firstNode = nodeList.item(child);
				// Remove text values
				if (firstNode.getNodeType() == Node.ELEMENT_NODE) {
					Element firstElement = (Element) firstNode;
					NodeList firstNodeElementList = firstElement.getElementsByTagName(language);
					Element firstNodeElement = (Element) firstNodeElementList.item(0);
					try {
						NodeList firstNodeList = firstNodeElement.getChildNodes();
						retried = false;
						return ((Node) firstNodeList.item(0)).getNodeValue().trim();
					} catch (NullPointerException npe) {
						if (!retried) {
							if (!showedMessage) {
								MachineLog.warning(Translator.class.getName(), "There is a problem with tag: " + tag
										+ " in  language: \"" + language
										+ "\". We tray to use english language instead.");
								showedMessage = true;
							}
							retried = true;
							return readTag(tag, DEFAULT_LANGUAGE);
						}
						if (!language.equals(DEFAULT_LANGUAGE)) {
							if (!errorShowed) {
								MachineLog.warning(this.getClass().getName(), "Selecting english language by default.");
								errorShowed = true;
							}
							return readTag(tag, DEFAULT_LANGUAGE);
						} else {
							if (!errorShowed) {
								MachineLog.severe(this.getClass().getName(), "Language selection failed: " + language
										+ " on " + tag + ".");
								errorShowed = true;
							}
							return null;
						}
					}

				}
			}
			MachineLog.debug(this.getClass().getName(), "No tag for: " + tag + ".");
			return null;
		} catch (NullPointerException npe) {
			return null;
		}
	}

	@Override
	public synchronized List<Language> getAvailableLanguages() {
		if (languagesList == null) {
			languagesList = new ArrayList<>();
			Document storedLanguages = null;
			storedLanguages = parseFile(storedLanguages, getTranslatorPath(LANGUAGES_FILE).getPath());
			NodeList nodeLst = storedLanguages.getElementsByTagName("languages");
			for (int s = 0; s < nodeLst.getLength(); s++) {
				Node fstNode = nodeLst.item(s);
				try {
					Language lang = new Language(fstNode.getTextContent(), fstNode.getAttributes()
							.getNamedItem("abbrev").getNodeValue(), fstNode.getAttributes().getNamedItem("flag")
							.getNodeValue());
					languagesList.add(lang);
				} catch (NullPointerException npe) {
					MachineLog.severe(Translator.class.getName(),
							"Error retrieving the available languages. Check your installation.");
				}
			}
		}
		return languagesList;
	}

	public static File getTranslatorPath(String xmlFile) {
		File file = new File(Path.getTranslatorPath() + xmlFile);
		if (file.exists()) {
			// Get from folder
			return file;
		}
		file = new File(".." + File.separator + Path.getTranslatorPath() + xmlFile);
		if (file.exists()) {
			// Get from folder
			return file;
		}

		try {
			if (Translator.class.getClassLoader().getResource(Path.TRANSLATIONS_FOLDER + File.separator + xmlFile) != null) {
				file = new File(Translator.class.getClassLoader()
						.getResource(Path.TRANSLATIONS_FOLDER + File.separator + xmlFile).toURI());
				if (file.exists()) {
					return file;
				}
			}
		} catch (URISyntaxException e) {
		}
		return null;
	}

	public static String getLanguage() {
		return language;
	}

	public static void setLanguage(String language) {
		Translator.language = language.toLowerCase();
	}

	@Override
	public String convertToXmlTag(String text) {
		return text.substring(0, 1).toLowerCase() + text.substring(1);
	}
}
