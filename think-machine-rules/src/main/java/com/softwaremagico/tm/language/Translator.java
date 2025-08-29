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

import com.softwaremagico.tm.file.PathManager;
import com.softwaremagico.tm.log.ConfigurationLog;
import com.softwaremagico.tm.log.MachineXmlReaderLog;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Translator implements ITranslator {
    public static final String DEFAULT_LANGUAGE = "en";
    private static final String LANGUAGES_FILE = "languages.xml";
    private Document doc;
    private boolean errorShowed = false;
    private boolean retried = false;
    private boolean showedMessage = false;
    private static List<Language> languagesList = null;
    private final HashMap<String, HashMap<String, String>> tagTranslations;
    private static String language = DEFAULT_LANGUAGE;

    public Translator(String filePath) {
        tagTranslations = new HashMap<>();
        doc = parseFile(null, filePath);
    }

    @Override
    public void clear() {
        doc = null;
    }

    /**
     * Parse the file
     *
     * @param usedDoc  xml doc
     * @param filePath the path to the file.
     */
    private static Document parseFile(Document usedDoc, String filePath) {
        try {
            URL resource;
            if (Translator.class.getClassLoader().getResource(filePath) != null) {
                resource = Translator.class.getClassLoader().getResource(filePath);
            } else {
                // Is inside a module.
                resource = URLClassLoader.getSystemResource(filePath);
            }
            ConfigurationLog.debug(Translator.class.getName(), "Found resource '" + filePath + "' at '" + resource + "'.");
            return parseContent(usedDoc, resource.openStream());
        } catch (NullPointerException e) {
            ConfigurationLog.severe(Translator.class.getName(), "Invalid xml at resource '" + filePath + "'.");
            ConfigurationLog.errorMessage(Translator.class.getName(), e);
        } catch (FileNotFoundException fnf) {
            final String text = "The file " + filePath
                    + " containing the translations is not found. Please, check your program files and put the translation XML files "
                    + "on the \"translations\" folder.";
            System.out.println(text);
        } catch (IOException ex) {
            Logger.getLogger(Translator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return usedDoc;
    }

    private static Document parseContent(Document usedDoc, InputStream content) {
        final DocumentBuilderFactory dbf;
        final DocumentBuilder db;

        try {
            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            usedDoc = db.parse(content);
            usedDoc.getDocumentElement().normalize();
        } catch (SAXParseException ex) {
            final String text = "Parsing error" + ".\n Line: " + ex.getLineNumber() + "\nUri: " + ex.getSystemId() + "\nMessage: " + ex.getMessage();
            ConfigurationLog.severe(Translator.class.getName(), text);
            ConfigurationLog.errorMessage(Translator.class.getName(), ex);
        } catch (SAXException | ParserConfigurationException ex) {
            ConfigurationLog.errorMessage(Translator.class.getName(), ex);
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
        tagTranslations.computeIfAbsent(language, k -> new HashMap<>());

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
        final List<String> nodes = new ArrayList<>();
        final Element element = doc.getDocumentElement();
        final NodeList nodeList = element.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            // Remove text values
            if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                nodes.add(nodeList.item(i).getNodeName());
            }
        }
        return nodes;
    }

    @Override
    public String getNodeValue(String node) {
        return getNodeValue(node, 0);
    }

    @Override
    public String getNodeValue(String tag, String node) {
        return getNodeValue(tag, node, 0);
    }

    @Override
    public boolean existsNode(String tag, String node) {
        return existsNode(tag, node, 0);
    }

    @Override
    public String getNodeValue(String node, int nodeNumber) {
        final NodeList nodeList = doc.getElementsByTagName(node);
        for (int child = 0; child < nodeList.getLength(); child++) {
            final Node firstNode = nodeList.item(child);
            // Remove text values
            if (firstNode.getNodeType() == Node.ELEMENT_NODE) {
                final Element firstElement = (Element) firstNode;
                return firstElement.getChildNodes().item(nodeNumber).getNodeValue().trim();
            }
        }
        return null;
    }

    @Override
    public String getNodeValue(String tag, String node, int nodeNumber) {
        final NodeList nodeList = doc.getElementsByTagName(tag);
        for (int child = 0; child < nodeList.getLength(); child++) {
            final Node firstNode = nodeList.item(child);
            // Remove text values
            if (firstNode.getNodeType() == Node.ELEMENT_NODE) {
                final Element firstElement = (Element) firstNode;
                try {
                    final NodeList firstNodeElementList = firstElement.getElementsByTagName(node);
                    final Element firstNodeElement = (Element) firstNodeElementList.item(0);
                    //Check is not an inner element, only first level.
                    if (firstNodeElement.getParentNode().getNodeName().equals(tag)) {
                        return firstNodeElement.getChildNodes().item(nodeNumber).getNodeValue().trim();
                    }
                } catch (NullPointerException npe) {
                    return null;
                }
            }
        }
        return null;
    }

    @Override
    public boolean existsNode(String tag, String node, int nodeNumber) {
        final NodeList nodeList = doc.getElementsByTagName(tag);
        for (int child = 0; child < nodeList.getLength(); child++) {
            final Node firstNode = nodeList.item(child);
            // Remove text values
            if (firstNode.getNodeType() == Node.ELEMENT_NODE) {
                final Element firstElement = (Element) firstNode;
                try {
                    final NodeList firstNodeElementList = firstElement.getElementsByTagName(node);
                    return firstNodeElementList.getLength() > 0;
                } catch (NullPointerException npe) {
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public String getNodeValue(String parent, String tag, String node) {
        return getNodeValue(parent, tag, node, 0);
    }

    @Override
    public boolean existsNode(String parent, String tag, String node) {
        return existsNode(parent, tag, node, 0);
    }

    @Override
    public String getNodeValue(String parent, String tag, String node, int nodeNumber) {
        final NodeList nodeList = doc.getElementsByTagName(parent);
        for (int child = 0; child < nodeList.getLength(); child++) {
            final Node parentNode = nodeList.item(child);
            // Remove text values
            if (parentNode.getNodeType() == Node.ELEMENT_NODE) {
                final Element parentElement = (Element) parentNode;
                try {
                    final NodeList childrenElementList = parentElement.getElementsByTagName(tag);
                    final Element childrenElement = (Element) childrenElementList.item(nodeNumber);
                    try {
                        final NodeList firstNodeElementList = childrenElement.getElementsByTagName(node);
                        final Element firstNodeElement = (Element) firstNodeElementList.item(0);
                        return firstNodeElement.getChildNodes().item(0).getNodeValue().trim();
                    } catch (NullPointerException npe) {
                        MachineXmlReaderLog.debug(this.getClass().getName(), "Node '{}/{}/{}' not found on xml.", parent, tag, node);
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
    public String getNodeValue(String grandparent, String parent, String tag, int nodeNumber, String node) {
        final NodeList nodeList = doc.getElementsByTagName(grandparent);
        for (int child = 0; child < nodeList.getLength(); child++) {
            final Node grandParentNode = nodeList.item(child);
            // Remove text values
            if (grandParentNode.getNodeType() == Node.ELEMENT_NODE) {
                final Element grandParentElement = (Element) grandParentNode;
                try {
                    final NodeList parentElementList = grandParentElement.getElementsByTagName(parent);
                    final Element parentElement = (Element) parentElementList.item(0);
                    try {
                        final NodeList childrenElementList = parentElement.getElementsByTagName(tag);
                        final Element childrenElement = (Element) childrenElementList.item(nodeNumber);
                        try {
                            final NodeList firstNodeElementList = childrenElement.getElementsByTagName(node);
                            final Element firstNodeElement = (Element) firstNodeElementList.item(0);
                            return firstNodeElement.getChildNodes().item(0).getNodeValue().trim();
                        } catch (NullPointerException npe) {
                            MachineXmlReaderLog.debug(this.getClass().getName(), "Node '{}/{}/{}' not found on xml.", parent, tag, node);
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
    public String getNodeValue(String root, String grandparent, String parent, int nodeNumber, String tag, String node) {
        final NodeList nodeList = doc.getElementsByTagName(root);
        for (int child = 0; child < nodeList.getLength(); child++) {
            final Node rootNode = nodeList.item(child);
            // Remove text values
            if (rootNode.getNodeType() == Node.ELEMENT_NODE) {
                final Element rootElement = (Element) rootNode;
                try {
                    final NodeList grandParentElementList = rootElement.getElementsByTagName(grandparent);
                    final Element grandParentElement = (Element) grandParentElementList.item(0);
                    try {
                        final NodeList parentElementList = grandParentElement.getElementsByTagName(parent);
                        final Element parentElement = (Element) parentElementList.item(nodeNumber);
                        try {
                            final NodeList childrenElementList = parentElement.getElementsByTagName(tag);
                            final Element childrenElement = (Element) childrenElementList.item(0);
                            try {
                                final NodeList firstNodeElementList = childrenElement.getElementsByTagName(node);
                                final Element firstNodeElement = (Element) firstNodeElementList.item(0);
                                return firstNodeElement.getChildNodes().item(0).getNodeValue().trim();
                            } catch (NullPointerException npe) {
                                MachineXmlReaderLog.debug(this.getClass().getName(), "Node '{}/{}/{}' not found on xml.", parent, tag, node);
                                return null;
                            }
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
    public boolean existsNode(String parent, String tag, String node, int nodeNumber) {
        final NodeList nodeList = doc.getElementsByTagName(parent);
        for (int child = 0; child < nodeList.getLength(); child++) {
            final Node parentNode = nodeList.item(child);
            // Remove text values
            if (parentNode.getNodeType() == Node.ELEMENT_NODE) {
                final Element parentElement = (Element) parentNode;
                try {
                    final NodeList childrenElementList = parentElement.getElementsByTagName(tag);
                    final Element childrenElement = (Element) childrenElementList.item(nodeNumber);
                    try {
                        final NodeList firstNodeElementList = childrenElement.getElementsByTagName(node);
                        return firstNodeElementList.getLength() > 0;
                    } catch (NullPointerException npe) {
                        return false;
                    }
                } catch (NullPointerException npe) {
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public String getNodeValue(String grandparent, String parent, String tag, String node) {
        final NodeList nodeList = doc.getElementsByTagName(grandparent);
        for (int child = 0; child < nodeList.getLength(); child++) {
            final Node grandParentNode = nodeList.item(child);
            // Remove text values
            if (grandParentNode.getNodeType() == Node.ELEMENT_NODE) {
                final Element grandParentElement = (Element) grandParentNode;
                try {
                    final NodeList parentElementList = grandParentElement.getElementsByTagName(parent);
                    final Element parentElement = (Element) parentElementList.item(0);
                    try {
                        final NodeList childrenElementList = parentElement.getElementsByTagName(tag);
                        final Element childrenElement = (Element) childrenElementList.item(0);
                        try {
                            final NodeList firstNodeElementList = childrenElement.getElementsByTagName(node);
                            final Element firstNodeElement = (Element) firstNodeElementList.item(0);
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
    public boolean existsNode(String grandparent, String parent, String tag, String node) {
        final NodeList nodeList = doc.getElementsByTagName(grandparent);
        for (int child = 0; child < nodeList.getLength(); child++) {
            final Node grandParentNode = nodeList.item(child);
            // Remove text values
            if (grandParentNode.getNodeType() == Node.ELEMENT_NODE) {
                final Element grandParentElement = (Element) grandParentNode;
                try {
                    final NodeList parentElementList = grandParentElement.getElementsByTagName(parent);
                    final Element parentElement = (Element) parentElementList.item(0);
                    try {
                        final NodeList childrenElementList = parentElement.getElementsByTagName(tag);
                        final Element childrenElement = (Element) childrenElementList.item(0);
                        try {
                            final NodeList firstNodeElementList = childrenElement.getElementsByTagName(node);
                            return firstNodeElementList.getLength() > 0;
                        } catch (NullPointerException npe) {
                            return false;
                        }
                    } catch (NullPointerException npe) {
                        return false;
                    }
                } catch (NullPointerException npe) {
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public Set<String> getAllChildrenTags(String parent, String group) {
        final Set<String> childrenTags = new HashSet<>();
        final NodeList nodeList = doc.getElementsByTagName(parent);
        for (int child = 0; child < nodeList.getLength(); child++) {
            final Node parentNode = nodeList.item(child);
            // Remove text values
            if (parentNode.getNodeType() == Node.ELEMENT_NODE) {
                final Element parentElement = (Element) parentNode;
                try {
                    final NodeList groupList = parentElement.getElementsByTagName(group);
                    final Element groupElement = (Element) groupList.item(0);
                    try {
                        final NodeList childrenList = groupElement.getChildNodes();
                        for (int childIndex = 0; childIndex < childrenList.getLength(); childIndex++) {
                            final Node childNode = childrenList.item(childIndex);
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

    @Override
    public Set<String> getAllChildrenTags(String grandparent, String parent, String group) {
        final Set<String> childrenTags = new HashSet<>();
        final NodeList nodeList = doc.getElementsByTagName(grandparent);
        for (int child = 0; child < nodeList.getLength(); child++) {
            final Node grandParentNode = nodeList.item(child);
            // Remove text values
            if (grandParentNode.getNodeType() == Node.ELEMENT_NODE) {
                final Element grandParentElement = (Element) grandParentNode;
                try {
                    final NodeList parentElementList = grandParentElement.getElementsByTagName(parent);
                    final Element parentElement = (Element) parentElementList.item(0);
                    try {
                        final NodeList groupList = parentElement.getElementsByTagName(group);
                        final Element groupElement = (Element) groupList.item(0);
                        try {
                            final NodeList childrenList = groupElement.getChildNodes();
                            for (int childIndex = 0; childIndex < childrenList.getLength(); childIndex++) {
                                final Node childNode = childrenList.item(childIndex);
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
                } catch (NullPointerException npe) {
                    return childrenTags;
                }
            }
        }
        return childrenTags;
    }

    private String readTag(String tag, String language) {
        try {
            final NodeList nodeList = doc.getElementsByTagName(tag);
            for (int child = 0; child < nodeList.getLength(); child++) {
                final Node firstNode = nodeList.item(child);
                // Remove text values
                if (firstNode.getNodeType() == Node.ELEMENT_NODE) {
                    final Element firstElement = (Element) firstNode;
                    final NodeList firstNodeElementList = firstElement.getElementsByTagName(language);
                    final Element firstNodeElement = (Element) firstNodeElementList.item(0);
                    try {
                        final NodeList firstNodeList = firstNodeElement.getChildNodes();
                        retried = false;
                        return firstNodeList.item(0).getNodeValue().trim();
                    } catch (NullPointerException npe) {
                        if (!retried) {
                            if (!showedMessage) {
                                ConfigurationLog.warning(Translator.class.getName(), "There is a problem with tag '{}' " +
                                        "in  language: '{}'. We tray to use english language instead.", tag, language);
                                showedMessage = true;
                            }
                            retried = true;
                            return readTag(tag, DEFAULT_LANGUAGE);
                        }
                        if (!language.equals(DEFAULT_LANGUAGE)) {
                            if (!errorShowed) {
                                ConfigurationLog.warning(this.getClass().getName(), "Selecting english language by default.");
                                errorShowed = true;
                            }
                            return readTag(tag, DEFAULT_LANGUAGE);
                        } else {
                            if (!errorShowed) {
                                ConfigurationLog.severe(this.getClass().getName(), "Language selection failed '{}' on '{}'.", language, tag);
                                errorShowed = true;
                            }
                            return null;
                        }
                    }

                }
            }
            ConfigurationLog.debug(this.getClass().getName(), "No tag for '{}'.", tag);
            return null;
        } catch (NullPointerException npe) {
            return null;
        }
    }

    @Override
    public synchronized List<Language> getAvailableLanguages() {
        if (languagesList == null) {
            languagesList = new ArrayList<>();
            final Document storedLanguages = parseFile(null, PathManager.getModulePath(null) + LANGUAGES_FILE);
            final NodeList nodeLst = storedLanguages.getElementsByTagName("languages");
            for (int s = 0; s < nodeLst.getLength(); s++) {
                final Node fstNode = nodeLst.item(s);
                try {
                    final Language lang = new Language(fstNode.getTextContent(), fstNode.getAttributes().getNamedItem("abbrev").getNodeValue(),
                            fstNode.getAttributes().getNamedItem("flag").getNodeValue());
                    languagesList.add(lang);
                } catch (NullPointerException npe) {
                    ConfigurationLog.severe(Translator.class.getName(), "Error retrieving the available languages. Check your installation.");
                }
            }
            ConfigurationLog.debug(this.getClass().getName(), "Available languages are '{}'.", languagesList);
        }
        return languagesList;
    }

    public static File getTranslatorPath(String xmlFile, String moduleName) {
        File file = new File(PathManager.getModulePath(moduleName) + xmlFile);
        if (file.exists()) {
            // Get from folder
            return file;
        }

        try {
            if (Translator.class.getClassLoader().getResource(xmlFile) != null) {
                file = new File(Translator.class.getClassLoader().getResource(xmlFile).toURI());
                if (file.exists()) {
                    return file;
                }
            }
        } catch (URISyntaxException | NullPointerException e) {
            //Nothing
        }

        try {
            if (Translator.class.getClassLoader().getResource(PathManager.MODULES_FOLDER + File.separator + xmlFile) != null) {
                file = new File(Translator.class.getClassLoader().getResource(PathManager.MODULES_FOLDER + File.separator + xmlFile).toURI());
                if (file.exists()) {
                    return file;
                }
            }
        } catch (URISyntaxException | NullPointerException e) {
            //Nothing
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
