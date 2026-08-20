
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//File reading class 
public class FileReading {

    private static final String FILE_PATH = "./data/Bags.txt";
    private static final String LEGACY_FILE_PATH = "./src/main/java/data/Bags.txt";

    /**
     * Uses the normal runtime save location, while still opening an existing
     * save created when the Java source folder was the working directory.
     */
    private static File getSaveFile() {
        File saveFile = new File(FILE_PATH);
        File legacyFile = new File(LEGACY_FILE_PATH);

        if (!saveFile.exists() && legacyFile.exists()) {
            return legacyFile;
        }

        return saveFile;
    }

    //Stores the strings from readingFile into Bags.txt.
    public static void storeTaskContents(List<String> readingFile) {

        File file = getSaveFile();

        File parentDir = file.getParentFile();

        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (FileWriter f = new FileWriter(file)) {

            for (String content : readingFile) {
                f.write(content + "\n");
            }

        } catch (IOException e) {
            System.out.println("Something went wrong while saving: " + e.getMessage());
        }
    }

    /**
     * Reads all contents from Bags.txt. Returns an empty list if there is no
     * save file.
     */
    public static List<String> readFileContents() {

        List<String> result = new ArrayList<>();

        File file = getSaveFile();

        try (Scanner s = new Scanner(file)) {

            while (s.hasNextLine()) {
                result.add(s.nextLine());
            }

        } catch (FileNotFoundException e) {
            System.out.println("No previous save file found. Creating a new session!");
        }

        return result;
    }
}
