# IntelliJ IDEA / Android Studio Save action Groovy scripts Plugin
This IntelliJ IDEA / Android Studio plugin allows the user to automatically run custom Groovy scripts when a file is saved / synchronized. Any valid Groovy script is supported; moreover, the plugin exposes a simple API to make file handling especially easy, allowing to simply implement source code formatting, file backups, file transformations, and more. Groovy script execution can be conditionally enabled / disabled based on a regex check on the path of the file which is saved.

## Getting started

### Install the plugin

First of all, [download](https://github.com/codebulb/SaveActionGroovyScripts/blob/master/SaveActionGroovyScripts.zip?raw=true) and install the plugin.

### Configure the plugin
The plugin comes with a default IntelliJ IDEA settings file which is placed in ```<USER_HOME>/<IDE_VERSION>/config/options/saveactionGroovyScripts_settings.xml```. If the default settings are kept, this file does not exist. If the settings are changed back to the defaults, it is deleted. You have to create it by hand if you want to change settings. Here are the default settings:

	<application>
	  <component name="SaveActionGroovyScriptsSettings">
		<option name="activate" value="true" />
		<option name="groovyScriptConfigFilePath" value="$userHome/.IdeaIC14/config/options/onsaveGroovyScriptsConfig.properties" />
	  </component>
	</application>

It comes with these 2 options:

- ```activate```: true to activate the plugin.
- ```groovyScriptConfigFilePath```: the path to the Groovy scripts config file (see below). You may use $userHome as a substitute for the USER_HOME user variable.

### Configure the Groovy scripts
In order to simplify configuration, Groovy scripts configuration is separated from the XML-based plugin configuration into its own, independent key-value .properties file. Its file path is configured in the plugin settings (see above). This is a very simple key-value .properties file which associates a Java regex expression with a Groovy script file path.
When activated, the plugin executes each Groovy script the regex expression of which matches the saved file's path. See the next section for more information about script execution.
This is an example configuration:

	.*\.java=scripts/formatJavaFile.groovy
	.*\.resouces/.*=scripts/checkResouces.groovy

This will execute:

- formatJavaFile.groovy when saving a file with the .java file ending
- checkResouces.groovy when saving a file whose parent folder is "resouces"

Note that according to the Java regex syntax, dot (.) must be escaped (\.). Use / as the folder separator on Windows operating systems, too.

### Write the Groovy script
Any valid Groovy script is eligible for execution by the plugin. However, the plugin sets a couple of helpful variables into the binding which you can use in your script, and it exposes a simple file handling API.
The plugin sets these variables in the script:

- ```content``` (```String```): The full text content of the file which is saved
- ```file``` (```CodeFile```): The file which is saved as a CodeFile instance (see below)
- ```projectBaseDir``` (```CodeFile```): The base directory (root) of the IntelliJ project of the file which is saved as a CodeFile instance

```CodeFile``` exposes a simple API for file handing. Most notably, you can get a file's text content, save a file and navigate through the file tree. There are also helper methods to read / write Properties and simple XML files.
By default, the outcome of the script (implicitly the last expression in the script) overrides the content of the file which is saved. If the script returns ```null```, the file which is saved is left untouched.
If the file path matches more than one Groovy script, they are executed in order. In each subsequent pass, the ```content``` variable is filled with the outcome of the previous script run; however, the file's content is physically replaced only after every matching script has been executed, with the outcome of the last script call. Thus you can still retrieve the original file's content by querying for ```file```'s ```text``` property.
For illustration purposes, consider this example Groovy script:

	import ch.codebulb.saveactiongroovyscripts.util.*
	
	projectBaseDir."res-simple".values."strings.properties" = 
		PropertiesFiles.write(XmlFiles.read(content, 'name'))
	
	null

This sets the content of the file with the path ```projectBaseDir/res-simple/values/strings.properties``` to the saved file's content which is parsed as XML and translated to the Properties files format (if the target file or any of its parent folders do not exist yet, they are created). Pretty straightforward, isn't it?

Note that when referring to classes defined by the plugin's API, you have to provide the respective ```import``` instruction.

### Download Groovy scripts
I created a public [**GitHub repository**](https://github.com/codebulb/SaveActionGroovyScripts) to host Groovy scripts for use with this plugin. Feel free to look around to find something useful, or contribute your own Groovy script!

### The API
The plugin provides a simple file handling API which you may use in your Groovy script code. The API consists of these classes:
- [```CodeFile```](http://codebulb.github.io/pages/2015/06/SaveActionGroovyScripts/doc/ch/codebulb/saveactiongroovyscripts/model/CodeFile.html) provides functionality for getting / setting file text content, saving a file, and navigating through the file tree.
- [```XMLFiles```](http://codebulb.github.io/pages/2015/06/SaveActionGroovyScripts/doc/ch/codebulb/saveactiongroovyscripts/util/XmlFiles.html) provides static helper methods to read / write simple XML files; "simple XML file" here meaning that they consist only of a root element and an arbitrary number of simple name / value direct child elements. When reading / writing, comments and line breaks are preserved.
- [```PropertiesFiles```](http://codebulb.github.io/pages/2015/06/SaveActionGroovyScripts/doc/ch/codebulb/saveactiongroovyscripts/util/PropertiesFiles.html) provides static helper methods to read / write Properties files. When reading / writing, comments and line breaks are preserved.

## Questions, comments, bugs?
This plugin project really started out of necessity for working with Android Studio. It’s still experimental. I decided to open source it only because I thought it might be useful for other people as well. If you are one of them, feel free to use it!
If you have questions or comments, please leave them on the [**blog post**](http://www.codebulb.ch/2015/06/intellij-idea-android-studio-plugin-save-action-groovy-scripts.html) I wrote about this plugin on my own blog: I’ll keep an eye on those comments. Note however that I do not intend to provide support for this plugin in any way.
However, if you found a bug or if you would like to contribute to this project, please feel free to do so by making a pull request.

## Share your script!
If you created an “on save Groovy script” which you think is useful for the general public, please feel free to upload it to the [Groovy scripts repository](https://github.com/codebulb/SaveActionGroovyScriptsRepository)! Thank you!

## Credits
The entire plugin is based on the similar [Save Actions Plugin](https://github.com/dubreuia/intellij-plugin-save-actions). Although this is indeed a very good plugin, it just didn’t provide the amount of customization I was looking for, thus I used it as the solid foundation to build this plugin.
