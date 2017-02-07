<img src="./images/ThinkingMachine_logo.png" width="340" alt="The Thinking Machine" align="middle"> 
# The Thinking Machine
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
If you are interested into translate this character sheet to a different language, you only need to translate some specific files. No programming skills are required for this purpose, you only need to open some files and add your translations. All of them in the translations folder:
- [languages.xml] (./translations/languages.xml) Defines the available languages in the application. 
- [character_sheet.xml] (./translations/character_sheet.xml) Contains all tags and words used into the PDF file. All texts are shown exactly as written, therefore take care about whitespaces, length of underscores, etc. 
- [skills] (./translations/skills/) A folder for each language containin two files: Natural Skills and Learned Skills. Any skill added here will be shown in the correct place of the character sheet. All skills are sorted alphabetically. 

The firsts two files are defined as XML, but do not worry if you do not know what it is, because it is very easy to change. The first thing to do is add the language. For this, you should modify the file included in the translations folder called `languages.xml`.

```
<?xml version="1.0" encoding="UTF-8" standalone="yes" ?>
<translator> 
  <languages abbrev="en" >English</languages> 
  <languages abbrev="es" >Español</languages> 
</translator>
```

Basically, what you must do is to add a new line under the one containing the word “Español” the same way and BEFORE the one that contains the code `</translator>`. Changing the name of the language that we want the program show and the content of “abbrev” with a code that connect with it. This code will be the one we use on the files for searching the available languages. For example, if we want to add Italian to the program we should add a line containing: 

`<languages abbrev="it" >Italiano</languages>`

Also the `abbrev` value will be used in the next steps of the translation. 

After adding a language, you must now translate the file `character_sheet.xml`. Add the new translations using the tag defined in the previous file. In our example, you need to write the new text as `<it>Some new italian text</it>`. Create a translation for any element thats appear on this file. 

The last step is adding the skills that you want to shown into the character sheet. This must be done in a folder called with the same language tag as defined before. In our example, create a folder called `it` in the `skills` folder that is inside the `translations` folder. Here you must create two files called exactly `skills-natural.txt` and `skills-learned.txt`. In each file add each skill in a separate row. The application will add the skills in the correct place of the character sheet and will sort it alphabetically for you.

If you have any doubt, check the already existing files as examples to see the structure of the needed files. It is really easy to do. 

When all texts are translated, the character sheets is ready to be generated. The application has been created using Maven with Java. Therefore, for excuting this application you need both Maven and Java installed on your machine. Then you must execute this command inside the `thinking-machine-core` folder: 

```
mvn exec:java -Dexec.args="en /path/to/file"
```
Where `en` is the language to obtain the file (now can be `en`, `es`) and `/path/to/file` must be a valid path where the application has permissions to generate a file. If this execution is too complex for you, you can always do a pull request on this project and I will generate it for you. 
