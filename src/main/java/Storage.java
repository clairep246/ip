import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Loads task records from, and saves task records to, the application's save file.
 */

//Use AI to generate storage class and move storage methods from Bags
public class Storage {

    private static final String LEGACY_FILE_PATH = "./src/main/java/data/Bags.txt";
    private final File saveFile;

    /**
     * Creates storage for the supplied save-file path.
     *
     * @param filePath location of the task save file
     */
    public Storage(String filePath) {
        saveFile = new File(filePath);
    }

    /**
     * Uses the configured save file, or an existing save from the previous
     * source-folder working-directory setup.
     */
    private File getSaveFile() {
        File legacyFile = new File(LEGACY_FILE_PATH);

        if (!saveFile.exists() && legacyFile.exists()) {
            return legacyFile;
        }

        return saveFile;
    }

    /** Saves every supplied task record, replacing the previous file contents. */
    public void save(List<String> taskRecords) {
        File file = getSaveFile();
        File parentDir = file.getParentFile();

        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (FileWriter writer = new FileWriter(file)) {
            for (String record : taskRecords) {
                writer.write(record + "\n");
            }
        } catch (IOException e) {
            System.out.println("Something went wrong while saving: " + e.getMessage());
        }
    }

    /**
     * Loads all saved task records, or returns an empty list when no save file exists.
     */
    public List<String> load() {
        List<String> taskRecords = new ArrayList<>();

        try (Scanner scanner = new Scanner(getSaveFile())) {
            while (scanner.hasNextLine()) {
                taskRecords.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("No previous save file found. Creating a new session!");
        }

        return taskRecords;
    }
}
