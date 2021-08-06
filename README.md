<img src="./images/ThinkMachine_logo_fenix.svg" width="800" alt="Think Machine" align="middle"> 

# Think Machine
[![Supported Languages](https://img.shields.io/badge/Supported-%F0%9F%87%AA%F0%9F%87%B8%20%F0%9F%87%AC%F0%9F%87%A7languages-blue.svg)](https://github.com/softwaremagico/ThinkMachine/blob/master/modules/languages.xml)
[![Build Status](https://travis-ci.org/softwaremagico/ThinkMachine.svg?branch=master)](https://travis-ci.org/softwaremagico/ThinkMachine)
[![GNU GPL 3.0 License](https://img.shields.io/badge/license-GNU_GPL_3.0-brightgreen.svg)](https://github.com/softwaremagico/ThinkMachine/blob/master/license/gnugpl/license.txt)
[![Issues](https://img.shields.io/github/issues/softwaremagico/ThinkMachine.svg)](https://github.com/softwaremagico/ThinkMachine/issues)
[![think-machine-core](https://img.shields.io/maven-central/v/com.softwaremagico/think-machine.svg)](https://search.maven.org/remote_content?g=com.softwaremagico&a=think-machine-rules&v=latest)
[![GitHub commit activity](https://img.shields.io/github/commit-activity/y/softwaremagico/ThinkMachine)](https://github.com/softwaremagico/ThinkMachine)
[![GitHub last commit](https://img.shields.io/github/last-commit/softwaremagico/ThinkMachine)](https://github.com/softwaremagico/ThinkMachine)

This software generated in Java includes all rules needed for the creation of a character sheet for the role play game called **Fading Suns** (Revised Edition). Personally, I do not like the provided character sheet in the Player's Guide book, and I have designed a new one based on some old sheets for previous versions of this game. Also, I have added some custom rules for random character generation that can be used for PC or NPC generation. This software only includes the rules, that can be included as a dependency on other softwares, but cannot be used as a standalone application. If you are looking for a complete software to generate characters, take a look on [Think Machine: Advisor](https://github.com/softwaremagico/ThinkMachineAdvisor), a mobile application for character generation based on Android devices. This app includes everything that you are looking for. 

To get an idea of the final character sheet result, here you have a preview: 

<img src="./images/englishSheetPreview.png" width="600" alt="Fading Suns Character Sheet" align="middle"> 

If you like the design, and you want to use it, only the final PDF document is needed. To avoid the complexity of compilate this source code and using some programming languages, you can directly download the PDF from these links:
- [Character Sheet (English)](https://github.com/softwaremagico/ThinkMachine/blob/master/sheets/FadingSuns_EN.pdf)
- [Character Sheet (Spanish)](https://github.com/softwaremagico/ThinkMachine/blob/master/sheets/FadingSuns_ES.pdf)
- [Character Sheet Small Version (English)](https://github.com/softwaremagico/ThinkMachine/blob/master/sheets/FadingSuns_Small_EN.pdf)
- [Character Sheet Small Version (Spanish)](https://github.com/softwaremagico/ThinkMachine/blob/master/sheets/FadingSuns_Small_ES.pdf)

The fonts needed for these PDFs are: [ArchitectsDaughter](https://fonts.google.com/specimen/Architects+Daughter), [DejaVuSans](https://dejavu-fonts.github.io/) and [Roman Antique](http://www.steffmann.de/wordpress/). All of them can be use freely for non-commercial use. Maybe you need to download and install them to see the PDFs properly.

If you want to skip the font installation, you can also donwload these sheets as a PNG image:
- [Character Sheet (English)](https://github.com/softwaremagico/ThinkMachine/blob/master/sheets/FadingSuns_EN-0.png)
- [Character Sheet Reverse (English)](https://github.com/softwaremagico/ThinkMachine/blob/master/sheets/FadingSuns_EN-1.png)
- [Character Sheet (Spanish)](https://github.com/softwaremagico/ThinkMachine/blob/master/sheets/FadingSuns_ES-0.png)
- [Character Sheet Reverse (Spanish)](https://github.com/softwaremagico/ThinkMachine/blob/master/sheets/FadingSuns_ES-1.png)
- [Character Sheet Small Version DIN A5 (English)](https://github.com/softwaremagico/ThinkMachine/blob/master/sheets/FadingSuns_Small_EN-0.png)
- [Character Sheet Small Version DIN A5 (Spanish)](https://github.com/softwaremagico/ThinkMachine/blob/master/sheets/FadingSuns_Small_ES-0.png)

Click any of these links to get a complete updated copy of the sheet. 

## Random character generation
From version 0.4.0 exists the option to generate randomly characters sheets. This feature is very usfeul for the creation of random NPC (non-player characters). You can define some basic options for the character such us nobility status, psi, combat preferences, equipment and more; and the software will generate the complete character for you in a few seconds. Each character generated is strictly following the rules of the Fading Suns core rule book and therefore, also can be used as a standard playable character. I hope this feature will add extra color in your campaigns.

Some examples already generated are:
- [Catherine Hawkwood (English)](https://github.com/softwaremagico/ThinkMachine/blob/master/NPC/Catherine%20Hawkwood.png)
- [Shinsuke Li Halan (Spanish)](https://github.com/softwaremagico/ThinkMachine/blob/master/NPC/Shinsuke%20Li%20Halan.png)
- [Farida Benkira (English Small DIN A5)](https://github.com/softwaremagico/ThinkMachine/blob/master/NPC/Farida%20Benkira.png)
- [Oleg Decados (Spanish Small DIN A5)](https://github.com/softwaremagico/ThinkMachine/blob/master/NPC/Oleg%20Decados.png)

## Random party generation
From version 0.4.8 exists a new option for autogenerating a complete group of NPC. Only must to select the maxium threat level of the group and a party schema. The application will generate a complete party from these inputs. Very useful if you want to generate encounters quickly. The "Threat level" is a custom measure of how dangerous the party will be. The higher value, more combat actions and better equipment will have. 

## Contributing

Still, the code of this application is provided in Github for some reasons:
* Maybe somebody wants to change some texts on the character sheet.
* Maybe somebody wants to translate it to a different language (see below for some instructions).
* Maybe somebody wants to integrate it in a custom application.
* In general, I like the idea of free source, and therefore, I have released this software under the GNU General Public License. 

### Adding a new language
If you are interested in the translation of the sheet/application in a different language, at the [Wiki](https://github.com/softwaremagico/ThinkMachine/wiki/Adding-a-new-Language) of this project you can find some instructions. 

### Execution
The application has been created using Maven with Java. Therefore, for excuting this application you need both Maven and Java installed on your machine. Then you must execute this command inside the `thinking-machine-core` folder: 

```
mvn exec:java -Dexec.args="en /path/to/file"
```
Where `en` is the language to obtain the file (now can be `en`, `es`) and `/path/to/file` must be a valid path where the application has permissions to generate a file. If this execution is too complex for you, you can always do a pull request on this project and I will generate it for you. 

You can also convert PDF to PNG automatically if you have ImageMagick installed and is at the path. Execute this Maven command:

```
mvn install -Prelease
```
And all the possible PDFs will be generated and later converted to PNG. Final result is located in the `sheet` folder. 

## Integrate with your custom application

### Using as a dependency in an external project
This component is available at the [Maven Central Repository](https://mvnrepository.com/artifact/com.softwaremagico/) and can be includeded as any other dependency. For example, for Maven the dependency would be: 

```
<!-- https://mvnrepository.com/artifact/com.softwaremagico/think-machine-rules -->
<dependency>
    <groupId>com.softwaremagico</groupId>
    <artifactId>think-machine-rules</artifactId>
    <version>0.8.6</version>
</dependency>
```
Currently, this project is divided in three subprojects: `think-machine-rules` (for Fading Suns rules related to the character generation), `think-machine-random` (logic behind the creation of random characters) and `think-machine-pdf`(exporting a character to a PDF sheet). Each subproject can be included separately. For any extra information about how to do it or use Gradle, Ivy or any other, please follow [this link](https://mvnrepository.com/artifact/com.softwaremagico/).

### Modules
Since version 0.6 this application allows the inclusion of modules. Each module can have different definitions of skills, weapons, psi powers, etc. Can be useful to include custom weapons, change some skills or add different languages. Also it is interesting if you want to use the Victory Point systems for other diferent adventures without using the Fading Suns world. As an example, [The Last Week](https://github.com/softwaremagico/ThinkMachine-Last-Week) is a module that can be use as a reference for creating new ones. It is based on year 2047 and some skills and equipment has been adapted for this specific adventure. 

### Final thoughts
This application only contains the logic for defining characters and NPC using Fading Suns victory point rule system. No user interface is included and therefore, cannot be use as a standalone application. In the close future new applications will appear for different platforms that includes a UI. These applications will be listed here:

- [Think Machine: Advisor](https://github.com/softwaremagico/ThinkMachine-Advisor) is a desktop application also based on Java that allows the use of this library for the creation of Player and Non Player Characters (currently in development).

## Notes
This software has been developed using the [iText library](http://itextpdf.com/) for PDF generation. 

Fading Suns is a TradeMark owned by Holistic Design. 

Fonts used in this project: DejaVuSans, ArchitectsDaughter and Roman Antique. 

[ImageMagick](https://www.imagemagick.org/script/index.php) is a free image manipulation software.


## Versions

0.1.0 Basic PDF generation

0.2.0 Multilingual added (English and Spanish).

0.3.0 Small PDF chart generation added.

0.4.0 Random character generator and equipment included.
- 0.4.1 Weapons added. 
- 0.4.2 Armours and shields added.
- 0.4.3 Combat styles added.
- 0.4.4 Age calculations
- 0.4.5 Cybernetics included. 
- 0.4.6 Random profiles.
- 0.4.7 Threat level calculation.
- 0.4.8 Random groups generation.

0.5.0 Performance improvements and code checks. 

0.6.0 Modules system and some improvements
- 0.6.1 Improvements in Json exporter.

0.7.0 Experience added.
- 0.7.1	Faction restricted and suggested benefices.

0.8.0 UI integration improvements.
- 0.8.1 Android SDK compatibility.
- 0.8.2 Cost changes interaction with UX. 

0.9.0 Element descriptions and extra content.
- 0.9.1 Equipment descriptions.
- 0.9.2 Occultism descriptions.

0.10.0 NPC generator.
- 0.10.1 Players Companion NPCs

