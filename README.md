<img src="./images/ThinkingMachine_logo.png" width="340" alt="The Thinking Machine" align="middle"> 
# The Thinking Machine
[![Build Status](https://travis-ci.org/jorgehortelano/TheThinkingMachine.svg?branch=master)](https://travis-ci.org/jorgehortelano/TheThinkingMachine)
[![GNU GPL 3.0 License](https://img.shields.io/badge/license-GNU_GPL_3.0-brightgreen.svg)](https://github.com/jorgehortelano/TheThinkingMachine/blob/master/license/gnugpl/license.txt)
[![Issues](https://img.shields.io/github/issues/jorgehortelano/TheThinkingMachine.svg)](https://github.com/jorgehortelano/TheThinkingMachine/issues)

This software generated in Java allows the creation of a character sheet for the role play game called **Fading Suns** (Revised Edition). Personally, I do not like the provided character sheet in the Player's Guide book, and I have designed a new one, based on some old files I have found ten years ago for previous versions of this game.

To get an idea of the final result, here you have a preview: 

<img src="./images/englishSheetPreview.png" width="600" alt="Fading Suns Character Sheet" align="middle"> 


If you like the design, and you want to use it, only the final PDF document is needed. To avoid the complexity of compilate this source code and using some programming languages, you can directly download the PDF from these links:
- [Character Sheet (English)] (./sheets/FadingSuns_EN.pdf)
- [Character Sheet (Spanish)] (./sheets/FadingSuns_ES.pdf)

Click any of these links to get a complete copy of the file. Still, the code of this application is provided in Github for some reasons:
* Maybe somebody wants to change some texts of the character sheet.
* Maybe somebody wants to translate it to a different language (see below for some instructions).
* In general, I like the idea of free source, and I release this software under the GNU General Public License. 

## Adding a new language
If you are interested in the translation of the sheet in a different language, at the (Wiki) [https://github.com/jorgehortelano/TheThinkingMachine/wiki/Adding-a-new-Language] of this project you can find some instructions. 

## Execution
The application has been created using Maven with Java. Therefore, for excuting this application you need both Maven and Java installed on your machine. Then you must execute this command inside the `thinking-machine-core` folder: 

```
mvn exec:java -Dexec.args="en /path/to/file"
```
Where `en` is the language to obtain the file (now can be `en`, `es`) and `/path/to/file` must be a valid path where the application has permissions to generate a file. If this execution is too complex for you, you can always do a pull request on this project and I will generate it for you. 

## Notes
This software has been developed using the [iText library] (http://itextpdf.com/) for PDF generation. 
Fading Suns is a TradeMark owned by Holistic Design. 
Fonts used in this project: DejaVuSans, ArchitectsDaughter and Roman Antique. 

## Versions

0.1.0 Basic PDF generation

0.2.0 Multilingual added (English and Spanish).

*0.3.0 Filling up the sheet.*
