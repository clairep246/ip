# Bags project template

This is a project template for a greenfield Java project. This version is named _Bags_. Given below are instructions on how to use it.

## Setting up in Intellij

Prerequisites: JDK 25, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
1. Open the project into Intellij as follows:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 25** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. After that, locate the `src/main/java/Bags.java` file, right-click it, and choose `Run Bags.main()` (if the code editor is showing compile errors, try restarting the IDE). If the setup is correct, you should see something like the below as the output:
   ```
    ____        _        
   |  _ \ _   _| | _____ 
   | | | | | | | |/ / _ \
   | |_| | |_| |   <  __/
   |____/ \__,_|_|\_\___|
   ```

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.

**AI use**
All code generated were reviewed and further improved by me. I made use of Codex
to help with the codebase. AI was used for the following purposes: 
- Autocompleting coding and fixing coding errors. 
- Generating test case files. 
- Generating JavaDoc comments. 
- Creating GUI and refactoring classes to fit FXML files for GUI development. 
- Adding assertions and refactoring classes to fit code quality. 
- Adding extensions to improve GUI and improve testing quality.
- Refactoring methods that were too lengthy and deeply nested to improve code quality. 
