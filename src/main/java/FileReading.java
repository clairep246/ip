import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileReading {

    private static final String FILE_PATH = "./data/Bags.txt";

    /**
     * Stores the strings from readingFile into Bags.txt.
     */
    public static void storeTaskContents(List<String> readingFile) {

        File file = new File(FILE_PATH);

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
     * Reads all contents from Bags.txt.
     * Returns an empty list if there is no save file.
     */
    public static List<String> readFileContents() {

        List<String> result = new ArrayList<>();

        File file = new File(FILE_PATH);

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